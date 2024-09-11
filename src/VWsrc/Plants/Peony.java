package VWsrc.Plants;
import VWsrc.Organism;
import VWsrc.World;
import VWsrc.Plant;
import javax.swing.*;
import java.util.Objects;

public class Peony extends Plant {
    public Peony(int row, int column, World virtualWorld) {
        super(row, column, 0, 0, virtualWorld);
    }

    @Override
    public void collision(Organism opponent) {
        this.setIsDead(true);
        if(opponent.getTypeName()=="you"){
            virtualWorld.addCollisionResult(opponent.getTypeName() + " ate " + this.getTypeName() + " and now have a power to create new organism.");
            virtualWorld.peonyEaten=true;
        }else{
            virtualWorld.addCollisionResult(opponent.getTypeName() + " ate " + this.getTypeName() + ".");
        }
    }

    @Override
    public ImageIcon getImage() {
        return new ImageIcon(Objects.requireNonNull(getClass().getResource("/VWsrc/icons/peony.png")));
    }

    @Override
    public String getTypeName() {
        return "peony";
    }
}
