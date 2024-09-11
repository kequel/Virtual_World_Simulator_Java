package VWsrc.Animals;
import VWsrc.World;
import VWsrc.Animal;
import javax.swing.*;
import java.util.Objects;

public class Sheep extends Animal {
    public Sheep(int row, int column, World virtualWorld) {
        super(row, column, 4, 4, virtualWorld);
    }
    @Override
    public ImageIcon getImage() {
        return new ImageIcon(Objects.requireNonNull(getClass().getResource("/VWsrc/icons/sheep.png")));
    }

    @Override
    public String getTypeName() {
        return "sheep";
    }

}
