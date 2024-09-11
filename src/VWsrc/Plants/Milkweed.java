package VWsrc.Plants;
import VWsrc.World;
import VWsrc.Plant;
import javax.swing.*;
import java.util.Objects;

public class Milkweed extends Plant {
    public Milkweed(int row, int column, World virtualWorld) {
        super(row, column, 0, 0, virtualWorld);
    }
    @Override
    public ImageIcon getImage() {
        return new ImageIcon(Objects.requireNonNull(getClass().getResource("/VWsrc/icons/milkweed.png")));
    }

    @Override
    public String getTypeName() {
        return "milkweed";
    }

    @Override
    public void action(int key) {
        for (int i = 0; i < 3; i++) {
            super.action(key);
        }
    }
}
