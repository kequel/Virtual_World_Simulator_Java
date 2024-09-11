package VWsrc.Animals;
import VWsrc.World;
import VWsrc.Animal;
import javax.swing.*;
import java.util.Objects;

public class Wolf extends Animal {
    public Wolf(int row, int column, World virtualWorld) {
        super(row, column, 5, 9, virtualWorld);
    }
    @Override
    public ImageIcon getImage() {
        return new ImageIcon(Objects.requireNonNull(getClass().getResource("/VWsrc/icons/wolf.png")));
    }

    @Override
    public String getTypeName() {
        return "wolf";
    }

}
