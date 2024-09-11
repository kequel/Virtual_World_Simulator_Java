package VWsrc;

import VWsrc.Organism;
import VWsrc.Plants.*;

import javax.swing.*;
import java.util.Random;

public abstract class Plant extends Organism {
    public Plant(int row, int column, int initiative, int strength, World virtualWorld) {
        super(row, column, initiative, strength, virtualWorld);
    }

    @Override
    public void collision(Organism opponent) {
        this.setIsDead(true);
    }

    @Override
    public void action(int key) {
        Random random = new Random();
        int chanceOfItSpreading = random.nextInt(20);
        if (chanceOfItSpreading < 2) {  // 10% chance
            int dRow = random.nextInt(3) - 1;  // -1, 0, 1
            int dColumn = random.nextInt(3) - 1;  // -1, 0, 1
            int newRow = this.getRow() + dRow;
            int newColumn = this.getColumn() + dColumn;
            if (this.virtualWorld.isPositionFree(newRow, newColumn)) {
                addNewPlant(newRow, newColumn);
            }
        }
    }


    public void addNewPlant(int newRow, int newColumn) {
        switch(this.getTypeName()) {
            case "milkweed": {
                Milkweed spreaded = new Milkweed(newRow, newColumn, virtualWorld);
                this.virtualWorld.addOrganism(spreaded);
                break;
            }
            case "grass": {
                Grass spreaded = new Grass(newRow, newColumn, virtualWorld);
                this.virtualWorld.addOrganism(spreaded);
                break;
            }
            case "guarana": {
                Guarana spreaded = new Guarana(newRow, newColumn, virtualWorld);
                this.virtualWorld.addOrganism(spreaded);
                break;
            }
            case "nightshade berries": {
                Nightshadeberries spreaded = new Nightshadeberries(newRow, newColumn, virtualWorld);
                this.virtualWorld.addOrganism(spreaded);
                break;
            }
            case "Sosnowski borscht": {
                Borscht spreaded = new Borscht(newRow, newColumn, virtualWorld);
                this.virtualWorld.addOrganism(spreaded);
                break;
            }
            case "peony": {
                Peony spreaded = new Peony(newRow, newColumn, virtualWorld);
                this.virtualWorld.addOrganism(spreaded);
                break;
            }
        }
    }


    public abstract ImageIcon getImage();

    public abstract String getTypeName();
}
