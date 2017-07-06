package com.example.arshdeep.minesweeper;

import android.content.Context;
import android.widget.Button;

/**
 * Created by Arshdeep on 6/17/2017.
 */

public class MyButton extends Button {
    private int Xp;
    private int Yp;
    private int value = 0;
    boolean isRevealed = false;
    boolean isFlagged = false;
    boolean isBombed = false;

    public MyButton(Context context) {
        super(context);
    }

    public int getXp() {
        return Xp;
    }

    public void setXp(int xp) {
        Xp = xp;
    }

    public int getYp() {
        return Yp;
    }

    public void setYp(int yp) {
        Yp = yp;
    }

    public boolean isBombed() {
        return isBombed;
    }

    public void setBombed(boolean bombed) {
        isBombed = bombed;
    }


    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public void setRevealed(boolean revealed) {
        isRevealed = revealed;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public void setFlagged(boolean flagged) {
        isFlagged = flagged;
    }

}
