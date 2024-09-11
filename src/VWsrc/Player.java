package VWsrc;

import javax.swing.*;
import java.util.Objects;

public class Player extends Animal {
    boolean shieldActive;
    int shieldCounter;

    public Player(int row, int column, World virtualWorld) {
        super(row, column, 4, 5, virtualWorld);
        this.shieldActive = false;
        this.shieldCounter = 0;
    }

    @Override
    public ImageIcon getImage() {
        return new ImageIcon(Objects.requireNonNull(getClass().getResource("/VWsrc/icons/player.png")));
    }

    @Override
    public String getTypeName() {
        return "you";
    }

    @Override
    public void action(int key) {
        int dr = 0;
        int dc = 0;
        switch (key) {
            case 0:
                dr = -1;
                break;
            case 1:
                dr = 1;
                break;
            case 2:
                dc = -1;
                break;
            case 3:
                dc = 1;
                break;
        }

        int newRow = getRow() + dr;
        int newColumn = getColumn() + dc;

        if (this.virtualWorld.isPositionInTheBoard(newRow, newColumn)) {
            Organism opponent = virtualWorld.getOrganismAtPosition(newRow, newColumn);
            if (opponent == null || opponent.getIsDead()) {
                virtualWorld.updatePosition(this, newRow, newColumn);
            } else {
                collision(opponent);
            }
        }
    }

    @Override
    public void collision(Organism opponent) {
        Animal opponentAnimal = (opponent instanceof Animal) ? (Animal) opponent : null;
        if (opponentAnimal!=null && shieldActive && opponent.getStrength() >= this.getStrength()) {
            for (int dx = -1; dx <= 1; ++dx) {
                for (int dy = -1; dy <= 1; ++dy) {
                    if (dx == 0 && dy == 0) continue;

                    int newX = opponent.getRow() + dx;
                    int newY = opponent.getColumn() + dy;

                    if (virtualWorld.isPositionFree(newX, newY)) {
                        virtualWorld.updatePosition(opponent, newX, newY);
                        virtualWorld.addCollisionResult(opponent.getTypeName() + " was deflected by Alzur's Shield.");
                        this.setAttackBlocked(true);
                        return;
                    }
                }
            }
            super.collision(opponent); //no free position
        } else {
            super.collision(opponent);
        }
    }

    int getShieldCounter() {
        return shieldCounter;
    }

    boolean isShieldActive(){
        return shieldActive;
    }

    void setShieldCounter(int count){
        this.shieldCounter=count;
    }

    void activateTheShield() {
        if (!shieldActive && shieldCounter == 0) {
            shieldActive = true;
            shieldCounter = 10; // 5 working + 5 cooldown
            virtualWorld.addCollisionResult(this.getTypeName() + " activated Alzur Shield!");
        }
        else{
            virtualWorld.addCollisionResult(this.getTypeName() + " can not activate Alzur Shield right now.");
        }

    }

    void decreaseShieldCounter() {
        if (shieldCounter > 0) {
            shieldCounter--;
            if (shieldCounter == 5) {
                shieldActive = false;
            }
        }
    }

}
