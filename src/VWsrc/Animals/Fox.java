package VWsrc.Animals;
import VWsrc.Organism;
import VWsrc.World;
import VWsrc.Animal;
import javax.swing.*;
import java.util.Objects;
import java.util.Random;

public class Fox extends Animal {
    public Fox(int row, int column, World virtualWorld) {
        super(row, column, 7, 3, virtualWorld);
    }
    @Override
    public ImageIcon getImage() {
        return new ImageIcon(Objects.requireNonNull(getClass().getResource("/VWsrc/icons/fox.png")));
    }

    @Override
    public void action(int key){
        boolean safe=false;
        while(!safe){
            Random random = new Random();
            int dRow = random.nextInt(3) - 1;  // -1, 0, 1
            int dColumn = random.nextInt(3) - 1;
            if(dRow==0 && dColumn==0) return;
            int newRow = this.getRow() + dRow;
            int newColumn = this.getColumn() + dColumn;
            if(this.virtualWorld.isPositionInTheBoard(newRow, newColumn)){
                Organism opponent = virtualWorld.getOrganismAtPosition(newRow,newColumn);
                if(opponent==null || opponent.getIsDead()){ //free position
                    safe=true;
                    virtualWorld.updatePosition(this, newRow, newColumn);
                }
                else if(opponent.getStrength()<this.getStrength()){ //weaker opponent
                    safe=true;
                    collision(opponent);
                }
            }
        }
    }

    @Override
    public String getTypeName() {
        return "fox";
    }

}
