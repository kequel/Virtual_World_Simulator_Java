package VWsrc;
import javax.swing.*;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1); //wait for latch (seting world)
        World virtualWorld = new World();
        Gui window = new Gui(latch);
        window.getSizeForGui(virtualWorld);
        latch.await();

        if (window.isContinueRequested()) {
            window.updateGameInfo("game loaded.");

        } else {
            Player player = new Player(0, 0, virtualWorld);
            virtualWorld.addOrganism(player);
            virtualWorld.addRandomOrganisms();
            window.createGrid(virtualWorld.getRows(), virtualWorld.getColumns(), virtualWorld);
            window.updateGrid(virtualWorld);
        }
        window.updateGameInfo("all world updates will appear here.\n");
        while (true) {
            if (window.isClicked()) {
                window.clearGameInfo();
                Player player = virtualWorld.getPlayer();
                if (window.getTurn() == 4 && player != null) {
                    player.activateTheShield();
                } else {
                    virtualWorld.executeTurn(window.getTurn());
                }
                window.setClicked(false);
                window.updateGrid(virtualWorld);
                for (String upd : virtualWorld.collisionResults) {
                    window.updateGameInfo(upd);
                }
                window.updateRoundCounter(virtualWorld);
                //ending:
                if (!virtualWorld.isPlayerAlive()) {
                    JOptionPane.showMessageDialog(null, "you were not careful enough and died - you lose!");
                    System.exit(0);
                } else if (virtualWorld.isPlayerTheLastAnimal()) {
                    JOptionPane.showMessageDialog(null, "you are the last animal in the virtual world - you win!");
                    System.exit(0);
                }
            }
            //saving:
            if (window.isSaveRequested()) {
                try {
                    virtualWorld.saveGame("game_save.txt");
                    window.updateGameInfo("game saved.");
                } catch (IOException e) {
                    window.updateGameInfo("can not save.");
                }
                window.setSaveRequested(false);
            }
            Thread.sleep(100);
        }
    }
}
