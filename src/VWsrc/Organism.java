package VWsrc;

import javax.swing.*;
import java.awt.*;

public abstract class Organism {
    private int row;
    private int column;
    private int prevRow;
    private int prevColumn;
    private int age;
    private boolean isDead;
    private int initiative;
    private int strength;

    public World virtualWorld;

    public Organism(int x, int y, int initiative, int strength, World virtualWorld) {
        this.row = x;
        this.column = y;
        this.prevRow = -1;
        this.prevColumn = -1;
        this.initiative = initiative;
        this.strength = strength;
        this.age = 0;
        this.isDead = false;
        this.virtualWorld = virtualWorld;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int r) {
        this.row = r;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int c) {
        this.column = c;
    }

    public int getPrevRow() {
        return prevRow;
    }

    public void setPrevRow(int r) {
        this.prevRow = r;
    }

    public int getPrevColumn() {
        return prevColumn;
    }

    public void setPrevColumn(int c) {
        this.prevColumn = c;
    }

    public int getAge() {
        return age;
    }

    public void increaseAge() {
        this.age++;
    }

    public int getInitiative() {
        return initiative;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int str){
        this.strength=str;
    }

    public boolean getIsDead() {
        return isDead;
    }

    public void setIsDead(boolean dead) {
        isDead = dead;
    }

    public ImageIcon getImage(){
        return null;
    };

    public ImageIcon getScaledImage(int width, int height) {
        ImageIcon icon = getImage(); //orginal
        Image img = icon.getImage(); //gets image from it
        Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImg);
    }

    public abstract void collision(Organism opponent);

    public abstract void action(int key);

    public abstract String getTypeName();
}
