package VWsrc.Plants;
import VWsrc.Organism;
import VWsrc.World;
import VWsrc.Plant;
import javax.swing.*;
import java.util.Objects;

public class Guarana extends Plant {
    public Guarana(int row, int column, World virtualWorld) {
        super(row, column, 0, 0, virtualWorld);
    }

    @Override
    public void collision(Organism opponent) {
        this.setIsDead(true);
        opponent.setStrength(opponent.getStrength()+3);
        virtualWorld.addCollisionResult(opponent.getTypeName() + " gets +3 strength from eating " + this.getTypeName() + ".");
    }

    @Override
    public ImageIcon getImage() {
        return new ImageIcon(Objects.requireNonNull(getClass().getResource("/VWsrc/icons/guarana.png")));
    }

    @Override
    public String getTypeName() {
        return "guarana";
    }
}
