package VWsrc.Plants;
import VWsrc.Animal;
import VWsrc.Organism;
import VWsrc.Plant;
import VWsrc.World;
import javax.swing.*;
import java.util.Objects;

public class Borscht extends Plant {
    public Borscht(int row, int column, World virtualWorld) {
        super(row, column, 0, 10, virtualWorld);
    }

    @Override
    public void collision(Organism opponent) {
        this.setIsDead(true);
        opponent.setIsDead(true);
        virtualWorld.addCollisionResult(opponent.getTypeName() + " dies from eating " + this.getTypeName() + ".");
    }

    @Override
    public void action(int key) {
        for (int dx = -1; dx <= 1; ++dx) {
            for (int dy = -1; dy <= 1; ++dy) {
                if (dx == 0 && dy == 0) continue;  // skip this
                int newRow = getRow() + dx;
                int newColumn = getColumn() + dy;
                Organism opponent=virtualWorld.getOrganismAtPosition(newRow, newColumn);
                Animal opponentAnimal = (opponent instanceof Animal) ? (Animal) opponent : null;
                if (opponentAnimal!=null) {
                    if (!opponentAnimal.getIsDead()) {
                        opponentAnimal.setIsDead(true);
                        opponentAnimal.setIsDead(true);
                        virtualWorld.addCollisionResult(opponentAnimal.getTypeName() + " dies from  " + this.getTypeName() + ".");                    }
                }
            }
        }
        super.action(key);
    }

    @Override
    public ImageIcon getImage() {
        return new ImageIcon(Objects.requireNonNull(getClass().getResource("/VWsrc/icons/borscht.png")));
    }

    @Override
    public String getTypeName() {
        return "sosnowski borscht";
    }
}
