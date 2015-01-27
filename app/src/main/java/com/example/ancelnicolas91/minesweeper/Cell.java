package com.example.ancelnicolas91.minesweeper;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ancelnicolas91 on 27/01/15.
 */
public class Cell implements Parcelable{
    private int _x;
    private int _y;

    private boolean _mined;
    private boolean _flaged;
    private boolean _discovered;
    private int _nbrOfMinesArround;

    public Cell(int x, int y, boolean mine){
        _x = x;
        _y = y;
        _mined = mine;
        _discovered = false;
        _nbrOfMinesArround = 0;
    }

    public boolean isMined(){
        return _mined;
    }

    public boolean isFlaged(){
        return _flaged;
    }

    public void setFlag(boolean flag){
        _flaged = flag;
    }

    public void mine(){
        _mined = true;
    }

    public void discover(){
        _discovered = true;
    }

    public boolean isDiscovered(){
        return _discovered;
    }

    public void setNbrOfMinesArround(int numberOfMinesArround){
        _nbrOfMinesArround = numberOfMinesArround;
    }

    public int getNbrOfMinesArround(){
        return _nbrOfMinesArround;
    }

    public int getX(){
        return _x;
    }

    public int getY(){
        return _y;
    }


    // PARCELABLE

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(_x);
        dest.writeInt(_y);
        dest.writeInt(_nbrOfMinesArround);

        dest.writeByte((byte) (_mined ? 1 : 0));
        dest.writeByte((byte) (_flaged ? 1 : 0));
        dest.writeByte((byte) (_discovered ? 1 : 0));
    }

    public static final Parcelable.Creator<Cell> CREATOR = new Parcelable.Creator<Cell>()
    {
        @Override
        public Cell createFromParcel(Parcel source)
        {
            return new Cell(source);
        }

        @Override
        public Cell[] newArray(int size)
        {
            return new Cell[size];
        }
    };

    public Cell(Parcel in) {

        this._x = in.readInt();
        this._y = in.readInt();
        this._nbrOfMinesArround = in.readInt();


        this._mined = in.readByte() != 0;
        this._flaged = in.readByte() != 0;
        this._discovered = in.readByte() != 0;
    }


}
