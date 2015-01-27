package com.example.ancelnicolas91.minesweeper;
import android.os.Parcelable;
import android.os.Parcel;

import java.util.ArrayList;

/**
 * Created by ancelnicolas91 on 27/01/15.
 */

public class Grid implements Parcelable{
    private ArrayList<Cell> _tab;
    private int _width;
    private int _height;
    private int _nbrMines;
    private int _nbrDiscover;
    private int _nbrFlags;

    public Grid(int width, int height, int nbrMines){
        _width = width;
        _height = height;
        _nbrMines = nbrMines;
        _nbrDiscover = 0;
        _nbrFlags = 0;
        _tab = new ArrayList<Cell>();

        int countMine = 0;


        for (int i = 0; i < _height; i++) {
            for (int j = 0; j < _width; j++) {
                _tab.add(new Cell(j, i, false));
            }
        }

        while (countMine<_nbrMines) {
            int rand = (int)(Math.random()*(_width*_height));

            Cell cell = getCell(rand);
            if(!cell.isMined()){
                cell.mine();
                countMine++;
            }
        }

    }

    public void displayGrid(){
        for (int i = 0; i < _tab.size(); i++) {
            if(getCell(i).isMined()){
                System.out.print("X");
            }
            else System.out.print("-");
            if((i+1)%_width == 0){
                System.out.print("\n");
            }
        }
    }


    public Cell getCell(int pos){
        if(pos >= 0 && pos < _width*_height)
            return _tab.get(pos);
        else return new Cell(0, 0, false);
    }

    public Cell getCell(int x, int y){
        if(x >= 0 && y >= 0 && x < _width && y < _height) {
            return _tab.get((y * _width) + x);
        }
        else return new Cell(0, 0, false);
    }


    public int getCountMineArround(Cell cell){
        int nbBomb = 0;
        int xTest = cell.getX();
        int yTest = cell.getY();

        if(getCell(xTest + 1, yTest).isMined()) { nbBomb ++; }
        if(getCell(xTest + 1, yTest + 1).isMined()) { nbBomb ++; }
        if(getCell(xTest, yTest + 1).isMined()) { nbBomb ++; }
        if(getCell(xTest - 1, yTest + 1).isMined()) { nbBomb ++; }
        if(getCell(xTest - 1, yTest - 1).isMined()) { nbBomb ++; }
        if(getCell(xTest + 1, yTest - 1).isMined()) { nbBomb ++; }
        if(getCell(xTest, yTest - 1).isMined()) { nbBomb ++; }
        if(getCell(xTest - 1, yTest).isMined()) { nbBomb ++; }

        return nbBomb;
    }

    public int getWidth(){
        return _width;
    }

    public int getHeight(){
        return _height;
    }

    public void discoverCell(Cell cell){
        cell.discover();
        _nbrDiscover++;
    }

    public void setFlagCell(Cell cell, boolean flag){
        cell.setFlag(flag);
        if(flag)
            _nbrFlags++;
        else
            _nbrFlags--;
    }

    public int getNbrFlags(){
        return _nbrFlags;
    }


    public int getNbrDiscover(){
        return _nbrDiscover;
    }

    public int getNbrMines() {
        return _nbrMines;
    }


    //PARCELABLE

    public Grid(Parcel in ) {
        readFromParcel( in );
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Grid createFromParcel(Parcel in ) {
            return new Grid(in);
        }

        public Grid[] newArray(int size) {
            return new Grid[size];
        }
    };

    public void readFromParcel(Parcel in){
        _width = in.readInt();
        _height = in.readInt();
        _nbrMines = in.readInt();
        _nbrDiscover = in.readInt();
        _nbrFlags = in.readInt();


        Cell[] _tab = (Cell[]) in.readParcelableArray(Cell.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeParcelableArray(_tab.toArray(new Cell[_tab.size()]), flags);

        dest.writeInt(_width);
        dest.writeInt(_height);
        dest.writeInt(_nbrMines);
        dest.writeInt(_nbrDiscover);
        dest.writeInt(_nbrFlags);

    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }



}
