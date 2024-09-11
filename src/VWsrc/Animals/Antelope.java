package VWsrc.Animals;
import VWsrc.Organism;
import VWsrc.World;
import VWsrc.Animal;
import javax.swing.*;
import java.util.Objects;
import java.util.Random;

public class Antelope extends Animal {
    public Antelope(int row, int column, World virtualWorld) {
        super(row, column, 4, 4, virtualWorld);
    }

    public void tryToAvoid(Organism opponent){
        Random random = new Random();
        int dRow = random.nextInt(3) - 1;  // -1, 0, 1
        int dColumn = random.nextInt(3) - 1;
        if(this.getTypeName()!=opponent.getTypeName()) { //it dont wants to avoid antelope
            if (virtualWorld.isPositionFree(getRow() + dRow, getColumn() + dColumn)) {
                virtualWorld.updatePosition(this, getRow() + dRow, getColumn() + dColumn);
                virtualWorld.addCollisionResult(this.getTypeName() + " escaped from collision with " + opponent.getTypeName() + ".");
            }
        }
    }

    @Override
    public ImageIcon getImage() {
        return new ImageIcon(Objects.requireNonNull(getClass().getResource("/VWsrc/icons/antelope.png")));
    }

    @Override
    public void action(int key){
        Random random = new Random();
        int dRow = (random.nextInt(3) - 1)*2;  // -2, 0, 2
        int dColumn = (random.nextInt(3) - 1)*2;
        if(dRow==0 && dColumn==0) return;
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

    @Override
    public void collision(Organism opponent){
        Random random = new Random();
        int randomInt = random.nextInt(2);
        Animal opponentAnimal = (opponent instanceof Animal) ? (Animal) opponent : null;
        if(randomInt==1 && opponentAnimal!=null && !opponentAnimal.getTypeName().equals(this.getTypeName())){
           tryToAvoid(opponent);
        }
        else super.collision(opponent);
    }

    @Override
    public String getTypeName() {
        return "antelope";
    }

}
