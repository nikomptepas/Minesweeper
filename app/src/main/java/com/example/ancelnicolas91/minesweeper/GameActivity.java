package com.example.ancelnicolas91.minesweeper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GameActivity extends Activity implements View.OnClickListener, View.OnLongClickListener{

    private Grid myGrid;
    private EditText mUserText;

    private int nbrColumns;
    private int nbrRows;
    private int nbrCells;

    private int nbrMines;
    private TextView countMines;

    private boolean gameover;
    private boolean winner;
    private int mineOfDeath;

    private ScoresBDD scoresBDD;
    private String timerValueString;

    private String nickName;

    private TextView timerValue;
    private long startTime = 0L;
    private Handler customHandler = new Handler();
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        scoresBDD = new ScoresBDD(this);
        setContentView(R.layout.activity_game);

        gameover = false;
        winner = false;

        final Intent intent = getIntent();

        if(savedInstanceState != null)
        {
            myGrid = savedInstanceState.getParcelable("myGrid");

            nbrColumns = myGrid.getWidth();
            nbrRows = myGrid.getHeight();
            nbrCells = nbrColumns*nbrRows;
            nbrMines = myGrid.getNbrMines();

            gameover = savedInstanceState.getByte("gameover")  != 0;
            winner = savedInstanceState.getByte("winner")  != 0;
            mineOfDeath = savedInstanceState.getInt("mineOfDeath");
            timeSwapBuff = savedInstanceState.getLong("timeSwapBuff");
            nickName = savedInstanceState.getString("nickName");

        }
        else
        {
            Bundle receiveBundle = intent.getBundleExtra("GameParams");

            nbrColumns = receiveBundle.getInt("Cols");
            nbrRows = receiveBundle.getInt("Rows");
            nbrCells = nbrColumns*nbrRows;

            nbrMines = receiveBundle.getInt("Mines");

            myGrid = new Grid(nbrColumns, nbrRows, nbrMines);
        }

        countMines = (TextView)findViewById(R.id.countMines);
        countMines.setText(Integer.toString(myGrid.getNbrMines()-myGrid.getNbrFlags()));
        timerValue = (TextView)findViewById(R.id.timer);

        LinearLayout layout = (LinearLayout)findViewById(R.id.game_container);

        for(int i = 0; i < nbrRows; i++)
        {
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(params);

            LinearLayout.LayoutParams paramsCell = new LinearLayout.LayoutParams(80,80);

            for(int j = 0; j < nbrColumns; j++)
            {
                ImageButton cell = new ImageButton(this);
                int id = j + (i * nbrColumns);
                cell.setLayoutParams(paramsCell);

                Cell currentCell = myGrid.getCell(id);
                if(currentCell.isDiscovered())
                {
                    int nbrOfMinesArround = currentCell.getNbrOfMinesArround();
                    cell.setImageResource(getResources().getIdentifier("cells_" + nbrOfMinesArround, "drawable", getPackageName()));
                }
                else if(currentCell.isFlaged())
                {
                    if(gameover)
                    {
                        if(currentCell.isMined())
                        {
                            cell.setImageResource(R.drawable.cells_flag);
                        }
                        else
                        {
                            cell.setImageResource(R.drawable.cells_false);
                        }
                    }
                    else
                    {
                        cell.setImageResource(R.drawable.cells_flag);
                    }

                }
                else
                {
                    cell.setImageResource(R.drawable.cells_hide);
                }

                cell.setId(id);
                cell.setScaleType(ImageView.ScaleType.FIT_XY);
                cell.setPadding(0, 0, 0, 0);
                cell.setOnClickListener(this);
                cell.setOnLongClickListener(this);
                row.addView(cell);
            }

            layout.addView(row);
        }

        if(gameover)
        {
            ((ImageButton)findViewById(mineOfDeath)).setImageResource(R.drawable.cells_dead);

            discoverMines();
            View content = findViewById(android.R.id.content);
            displayPrompt(content);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState)
    {
        savedInstanceState.putParcelable("myGrid", myGrid);

        savedInstanceState.putByte("gameover",(byte) (gameover ? 1 : 0));
        savedInstanceState.putByte("winner",(byte) (winner ? 1 : 0));
        savedInstanceState.putInt("mineOfDeath",mineOfDeath);

        if(gameover)
        {
            nickName = mUserText.getText().toString();
        }

        savedInstanceState.putString("nickName",nickName);

        timeSwapBuff += timeInMilliseconds;
        customHandler.removeCallbacks(updateTimerThread);
        savedInstanceState.putLong("timeSwapBuff",timeSwapBuff);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);
    }

    public void resetGame(View v)
    {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        ImageButton imgBtn = (ImageButton)findViewById(id);

        Cell currentCell = myGrid.getCell(v.getId());
        if(currentCell.isDiscovered() || currentCell.isFlaged())
            return;

        if(currentCell.isMined())
        {
            imgBtn.setImageResource(R.drawable.cells_dead);
            myGrid.discoverCell(currentCell);
            discoverMines();
            gameover = true;
            mineOfDeath=id;
            displayPrompt(v);
            timeSwapBuff += timeInMilliseconds;
            customHandler.removeCallbacks(updateTimerThread);
        }
        else
        {
            int nbrMinesArround = myGrid.getCountMineArround(currentCell);
            myGrid.discoverCell(currentCell);
            currentCell.setNbrOfMinesArround(nbrMinesArround);
            imgBtn.setImageResource(getResources().getIdentifier("cells_" + Integer.toString(nbrMinesArround), "drawable", getPackageName()));

            if(nbrMinesArround < 1){
                int currentRow = id/nbrColumns;
                int currentCol = id/nbrRows;

                boolean right = false;
                boolean left = false;
                boolean top = false;
                boolean bottom = false;

                // Si la case sur la droite est sur la même ligne
                if((id+1)/nbrRows==currentRow && (id+1)<nbrCells)
                {
                    ((ImageButton)findViewById(id+1)).performClick();
                    right = true;
                }

                // Si la case sur la gauche est sur la même ligne
                if((id-1)/nbrRows==currentRow && (id-1)>=0)
                {
                    ((ImageButton)findViewById(id-1)).performClick();
                    left = true;
                }

                // Si la case en haut ne dépasse pas la taille de la grille
                if(id-nbrColumns >= 0)
                {
                    ((ImageButton)findViewById(id-nbrColumns)).performClick();
                    top = true;
                }

                // Si la case en bas ne dépasse pas la taille de la grille
                if(id+nbrColumns < nbrCells)
                {
                    ((ImageButton)findViewById(id+nbrColumns)).performClick();
                    bottom = true;
                }

                if(right)
                {
                    if(top)
                        ((ImageButton)findViewById(id-nbrColumns+1)).performClick();
                    if(bottom)
                        ((ImageButton)findViewById(id+nbrColumns+1)).performClick();
                }

                if(left)
                {
                    if(top)
                        ((ImageButton)findViewById(id-nbrColumns-1)).performClick();
                    if(bottom)
                        ((ImageButton)findViewById(id+nbrColumns-1)).performClick();
                }
            }

            if(myGrid.getNbrDiscover()==nbrCells-nbrMines && Integer.parseInt(countMines.getText().toString())==0)
            {
                winner = true;
                gameover = true;
                displayPrompt(v);
            }
        }
    }

    public void discoverMines()
    {
        for (int i=0; i<nbrCells; i++)
        {
            if((myGrid.getCell(i)).isMined() && !(myGrid.getCell(i)).isFlaged()  && !(myGrid.getCell(i)).isDiscovered())
                ((ImageButton)findViewById(i)).setImageResource(R.drawable.cells_mine);
            if(myGrid.getCell(i).isFlaged() && !(myGrid.getCell(i)).isMined())
                ((ImageButton)findViewById(i)).setImageResource(R.drawable.cells_false);
        }
    }

    public boolean onLongClick(View v)
    {
        int id = v.getId();

        Cell currentCell = myGrid.getCell(v.getId());
        if(!currentCell.isDiscovered() && !currentCell.isFlaged())
        {
            ImageButton imgBtn = (ImageButton)findViewById(id);
            myGrid.setFlagCell(currentCell, true);
            imgBtn.setImageResource(R.drawable.cells_flag);
        }
        else if(!currentCell.isDiscovered() && currentCell.isFlaged())
        {
            ImageButton imgBtn = (ImageButton)findViewById(id);
            myGrid.setFlagCell(currentCell, false);
            imgBtn.setImageResource(R.drawable.cells_hide);
        }

        countMines.setText(Integer.toString(myGrid.getNbrMines()-myGrid.getNbrFlags()));

        if(myGrid.getNbrDiscover()==nbrCells-nbrMines && Integer.parseInt(countMines.getText().toString())==0)
        {
            winner = true;
            gameover = true;
            displayPrompt(v);
        }

        return true;
    }

    public void displayPrompt(View v)
    {
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.editscores_info, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        String title = "";
        if(winner)
        {
            alert.setTitle(getString(R.string.winner));
        }
        else
        {
            alert.setTitle(getString(R.string.looser));
        }

        alert.setMessage("Nickname :");
        alert.setView(textEntryView);

        mUserText = (EditText) textEntryView.findViewById(R.id.newPseudo);
        mUserText.setText(nickName);

        // SI l'utilisateur clique sur OK
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {

                // On récupère ce qui a été saisie par l'utilisateur
                nickName = mUserText.getText().toString();

                //Création d'un SCORE
                Scores score1 = new Scores(nickName, nbrCells, nbrMines,timerValueString,winner?"WIN":"LOOSE");

                //On ouvre la base de données pour écrire dedans
                scoresBDD.open();
                //On insère le score que l'on vient de créer
                scoresBDD.insertScore(score1);

                scoresBDD.close();

                onBackPressed();

                return;
            }
        });

        // SI il clique sur Cancel
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                onBackPressed();
                return;
            }
        });
        alert.show();
    }

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            timerValue.setText("" + mins + ":"+ String.format("%02d", secs));
            timerValueString = "" + mins + ":"+ String.format("%02d", secs);
            customHandler.postDelayed(this, 0);
        }
    };
}
