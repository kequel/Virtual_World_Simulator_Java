package VWsrc;
import VWsrc.Animals.*;
import java.util.Random;

public abstract class Animal extends Organism {
    protected boolean hasReproduced;
    private boolean attackBlocked;

    public Animal(int row, int column, int initiative, int strength, World virtualWorld) {
        super(row, column, initiative, strength, virtualWorld);
        this.hasReproduced=false;
        this.attackBlocked=false;
    }

    public void setHasReproduced(boolean alreadyReproduced) {
        this.hasReproduced = alreadyReproduced;
    }

    public boolean isAttackBlocked() {
        return attackBlocked;
    }

    public void setAttackBlocked(boolean attackBlocked) {
        this.attackBlocked = attackBlocked;
    }

    public void reproduction(Animal opponentAnimal){
        for (int n = -1; n <= 1; n++) {
            for (int m = -1; m <= 1; m++) {
                int checkRow = getRow() + n;
                int checkColumn = getColumn() + m;
                if (virtualWorld.isPositionInTheBoard(checkRow, checkColumn) && virtualWorld.getOrganismAtPosition(checkRow, checkColumn) == null) {
                    addNewAnimal(checkRow, checkColumn);
                    this.hasReproduced=true;
                    opponentAnimal.hasReproduced=true;
                    virtualWorld.addCollisionResult(this.getTypeName() + "s " + "are breeding.");
                    return;
                }
            }
        }
    }

    @Override
    public void collision(Organism opponent) {
        if (opponent != null && !opponent.getIsDead()) {
            Animal opponentAnimal = (opponent instanceof Animal) ? (Animal) opponent : null;
            Plant opponentPlant = (opponent instanceof Plant) ? (Plant) opponent : null;

            if (opponentAnimal != null) {
                if(this.getTypeName().equals(opponentAnimal.getTypeName()) && opponentAnimal!=this && !opponentAnimal.getIsDead()){
                    if(!this.hasReproduced && !opponentAnimal.hasReproduced && virtualWorld.getRoundCounter()>2){
                        reproduction(opponentAnimal);
                    }
                }
                else if(opponentAnimal.attackBlocked){
                    return;
                }
                else if (this.getStrength() < opponentAnimal.getStrength() && !this.getIsDead()) { // attacker loses
                    this.setIsDead(true);
                    virtualWorld.addCollisionResult(opponentAnimal.getTypeName() + " kill " + this.getTypeName() + ".");
                } else if (!this.getIsDead()) { // this strength bigger or equal
                    opponentAnimal.setIsDead(true);
                    opponentAnimal.collision(this);
                    if (opponentAnimal.isAttackBlocked()) {
                        opponentAnimal.setIsDead(false);
                    } else {
                        int newX = opponent.getRow();
                        int newY = opponent.getColumn();
                        virtualWorld.updatePosition(this, newX, newY);
                        virtualWorld.addCollisionResult(this.getTypeName() + " kill " + opponent.getTypeName() + ".");
                    }
                }
            } else if (opponentPlant != null) { // plant
                if (this.getStrength() < opponentPlant.getStrength()) { // deadly
                    this.setIsDead(true);
                    opponentPlant.setIsDead(true);
                    opponentPlant.collision(this);
                    virtualWorld.addCollisionResult(this.getTypeName() + " eat " + opponent.getTypeName() + " and die.");
                } else { // nice plant
                    int newX = opponent.getRow();
                    int newY = opponent.getColumn();
                    opponentPlant.collision(this); // fe. gurana adds strength
                    opponentPlant.setIsDead(true);
                    virtualWorld.updatePosition(this, newX, newY);
                    virtualWorld.addCollisionResult(this.getTypeName() + " eat " + opponent.getTypeName() + ".");
                }
            }
        }
    }

    @Override
    public void action(int key) {
        Random random = new Random();
        int dRow = random.nextInt(3) - 1;  // -1, 0, 1
        int dColumn = random.nextInt(3) - 1;
        if(dRow==0 && dColumn==0) return; //same positon
        int newRow = this.getRow() + dRow;
        int newColumn = this.getColumn() + dColumn;
        if(this.virtualWorld.isPositionInTheBoard(newRow, newColumn)){
            Organism opponent = virtualWorld.getOrganismAtPosition(newRow,newColumn);
            if(opponent==null || opponent.getIsDead()){
                virtualWorld.updatePosition(this, newRow, newColumn);
            }
            else collision(opponent);
        }
    }

    public void addNewAnimal(int newRow, int newColumn) {
        switch(this.getTypeName()) {
            case "wolf": {
                Wolf child = new Wolf(newRow, newColumn, virtualWorld);
                this.virtualWorld.addOrganism(child);
                break;
            }
            case "sheep": {
                Sheep child = new Sheep(newRow, newColumn, virtualWorld);
                this.virtualWorld.addOrganism(child);
                break;
            }
            case "fox": {
                Fox child = new Fox(newRow, newColumn, virtualWorld);
                this.virtualWorld.addOrganism(child);
                break;
            }
            case "antelope": {
                Antelope child = new Antelope(newRow, newColumn, virtualWorld);
                this.virtualWorld.addOrganism(child);
                break;
            }
            case "turtle": {
                Turtle child = new Turtle(newRow, newColumn, virtualWorld);
                this.virtualWorld.addOrganism(child);
                break;
            }

        }
    }
}
