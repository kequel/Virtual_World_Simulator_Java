package VWsrc.Plants;
import VWsrc.Organism;
import VWsrc.Plant;
import VWsrc.World;
import javax.swing.*;
import java.util.Objects;

public class Nightshadeberries extends Plant {
    public Nightshadeberries(int row, int column, World virtualWorld) {
        super(row, column, 0, 99, virtualWorld);
    }

    @Override
    public void collision(Organism opponent) {
        this.setIsDead(true);
        opponent.setIsDead(true);
    }

    @Override
    public ImageIcon getImage() {
        return new ImageIcon(Objects.requireNonNull(getClass().getResource("/VWsrc/icons/nightshadeberries.png")));
    }

    @Override
    public String getTypeName() {
        return "nightshade berries";
    }
}
