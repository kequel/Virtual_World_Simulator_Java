package VWsrc.Plants;
import VWsrc.World;
import VWsrc.Plant;
import javax.swing.*;
import java.util.Objects;

public class Grass extends Plant {
    public Grass(int row, int column, World virtualWorld) {
        super(row, column, 0, 0, virtualWorld);
    }
    @Override
    public ImageIcon getImage() {
        return new ImageIcon(Objects.requireNonNull(getClass().getResource("/VWsrc/icons/grass.png")));
    }

    @Override
    public String getTypeName() {
        return "grass";
    }
}
