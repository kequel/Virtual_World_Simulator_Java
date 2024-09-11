package VWsrc;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class Gui {
    private static final int MAX_WINDOW_WIDTH = 900;
    private static final int MAX_WINDOW_HEIGHT = 600;

    private final JFrame getSizeFrame;
    private final JFrame boardFrame;
    private int rows;
    private int columns;
    private final JButton createButton;
    private final JButton continueButton;
    private final JButton shieldButton;
    private final JButton saveButton;
    private final JButton quitButton;
    private final JTextField widthField;
    private final JTextField heightField;
    private final CountDownLatch latch;
    private final JLabel infoLabel;
    private final JTextArea updatesArea;
    private final JLabel roundLabel;
    private int turn;
    private boolean clicked;
    private boolean saveRequested;
    private boolean continueRequested;

    public Gui(CountDownLatch latch) {
        getSizeFrame = new JFrame();
        createButton = new JButton("create the world");
        continueButton = new JButton("load old game");
        shieldButton = new JButton("use Alzur Shield");
        saveButton = new JButton("save the Game");
        quitButton = new JButton("quit");
        widthField = new JTextField(5);
        heightField = new JTextField(5);
        boardFrame = new JFrame();
        this.latch = latch;
        this.clicked = false;
        this.turn = 4;
        this.saveRequested = false;
        this.continueRequested = false;
        infoLabel = new JLabel(" ", SwingConstants.LEFT);
        infoLabel.setVerticalAlignment(SwingConstants.TOP); // text - top
        updatesArea = new JTextArea(5, 20);
        updatesArea.setEditable(false);
        updatesArea.setLineWrap(true);
        updatesArea.setWrapStyleWord(true); //in words
        roundLabel = new JLabel("ROUND: 0", SwingConstants.RIGHT);
        roundLabel.setVerticalAlignment(SwingConstants.TOP); // text - top
    }

    public void getSizeForGui(World virtualWorld) {
        getSizeFrame.setSize(300, 150);
        getSizeFrame.setTitle("enter dimensions");

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));

        panel.add(new JLabel("width:"));
        panel.add(widthField);
        panel.add(new JLabel("height:"));
        panel.add(heightField);
        panel.add(new JLabel(""));
        createButton.setBackground(new Color(106, 132, 106));
        panel.add(createButton);
        panel.add(new JLabel(""));
        continueButton.setBackground(new Color(210, 180, 140));
        panel.add(continueButton);

        getSizeFrame.add(panel);
        getSizeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getSizeFrame.setVisible(true);

        createButton.addActionListener(e -> {
            columns = Integer.parseInt(widthField.getText());
            rows = Integer.parseInt(heightField.getText());
            if(rows*columns<11){ //too small for everyone
                JOptionPane.showMessageDialog(boardFrame, "this world would be too small, not every organism would fit :( game has ended.");
                System.exit(0);
            }
            virtualWorld.setWorld(rows, columns);
            createGrid(rows, columns, virtualWorld);
            latch.countDown(); //done
        });

        continueButton.addActionListener(e -> {
            continueRequested = true;
            try (BufferedReader reader = new BufferedReader(new FileReader("game_save.txt"))) {
                String[] size = reader.readLine().split("x");
                rows = Integer.parseInt(size[0]);
                columns = Integer.parseInt(size[1]);
                virtualWorld.setWorld(rows, columns);//world created
                createGrid(rows, columns, virtualWorld);
                String shieldStatus = reader.readLine();
                boolean shieldActive; //later to add to player
                int shieldCounter; //later to add to player
                if(shieldStatus.charAt(0) == 't'){
                    shieldActive =true;
                    shieldCounter =Integer.parseInt(shieldStatus.substring(1));
                } else{
                    shieldActive =false;
                    shieldCounter = 0;
                }

                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    String typeName = parts[0];
                    int age = Integer.parseInt(parts[1]);
                    int strength = Integer.parseInt(parts[2]);
                    int row = Integer.parseInt(parts[3]);
                    int column = Integer.parseInt(parts[4]);

                    Organism organism = virtualWorld.createOrganismThatExisted(typeName, row, column, strength, age);
                    if (organism != null) {
                        virtualWorld.addOrganism(organism);
                        if (organism instanceof Player) {
                            Player player = (Player) organism;
                            if (shieldActive) {
                                player.activateTheShield();
                            }
                            player.setShieldCounter(shieldCounter);
                            virtualWorld.setRoundCounter(player.getAge());
                        }
                    }
                }
                updateGrid(virtualWorld);
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            latch.countDown();
        });
    }

    public void createGrid(int w, int h, World virtualWorld) {
        boardFrame.setSize(MAX_WINDOW_WIDTH, MAX_WINDOW_HEIGHT);
        boardFrame.setTitle("virtual world");
        boardFrame.setLayout(new BorderLayout());

        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(w, h));

        int buttonWidth = MAX_WINDOW_WIDTH / w;
        int buttonHeight = MAX_WINDOW_HEIGHT / h;

        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                JButton button = new JButton();
                if (virtualWorld.board[i][j] != null) {
                    button.setIcon(virtualWorld.board[i][j].getScaledImage(buttonWidth / 2, buttonHeight / 2));
                } else {
                    button.setText(" ");
                }
                button.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
                int finalI = i;
                int finalJ = j;
                button.addActionListener(e -> {
                    if (virtualWorld.board[finalI][finalJ] == null) {
                        String[] options = {"wolf", "sheep", "fox", "antelope", "turtle", "milkweed", "Sosnowski borscht", "guarana", "nightshade berries", "grass"};
                        String choice = (String) JOptionPane.showInputDialog(boardFrame, "choose organism to add:", "add organism", JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
                        if (choice != null) {
                            Organism newOrg = virtualWorld.createOrganismFromChoice(choice, finalI, finalJ, virtualWorld);
                            if (newOrg != null) {
                                virtualWorld.addOrganism(newOrg);
                                updateGrid(virtualWorld); // update with new
                            }
                        }
                    }
                });
                gridPanel.add(button);
            }
        }

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(infoLabel, BorderLayout.WEST);
        topPanel.add(roundLabel, BorderLayout.EAST);

        JPanel updatesPanel = new JPanel();
        updatesPanel.setLayout(new BoxLayout(updatesPanel, BoxLayout.Y_AXIS));
        updatesPanel.add(infoLabel);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(3, 1));
        buttonsPanel.add(shieldButton);
        shieldButton.setBackground(new Color(210, 180, 140));
        buttonsPanel.add(saveButton);
        saveButton.setBackground(new Color(106, 132, 106));
        buttonsPanel.add(quitButton);
        quitButton.setBackground(new Color(161, 107, 107));

        updatesPanel.add(buttonsPanel);
        updatesPanel.add(new JScrollPane(updatesArea));

        boardFrame.add(topPanel, BorderLayout.NORTH);
        boardFrame.add(updatesPanel, BorderLayout.WEST);
        boardFrame.add(gridPanel, BorderLayout.CENTER);

        shieldButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                turn = 4;
                clicked = true;
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveRequested = true;
            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(boardFrame, "player has left the game by pressing Quit");
                System.exit(0);
            }
        });

        boardFrame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        turn = 0;
                        clicked = true;
                        break;
                    case KeyEvent.VK_DOWN:
                        turn = 1;
                        clicked = true;
                        break;
                    case KeyEvent.VK_LEFT:
                        turn = 2;
                        clicked = true;
                        break;
                    case KeyEvent.VK_RIGHT:
                        turn = 3;
                        clicked = true;
                        break;
                }
            }
        });
        boardFrame.setFocusable(true);
        boardFrame.requestFocusInWindow(); //for keys
        boardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        boardFrame.setVisible(true);
    }

    void updateGrid(World virtualWorld) {
        boardFrame.getContentPane().removeAll();
        JPanel gridPanel = new JPanel();

        gridPanel.setLayout(new GridLayout(rows, columns));

        int buttonWidth = MAX_WINDOW_WIDTH / rows;
        int buttonHeight = MAX_WINDOW_HEIGHT / columns;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                JButton button = new JButton();
                if (virtualWorld.board[i][j] != null) {
                    button.setIcon(virtualWorld.board[i][j].getScaledImage(buttonWidth / 2, buttonHeight / 2));
                } else {
                    button.setText(" ");
                }
                button.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
                int finalI = i;
                int finalJ = j;
                button.addActionListener(e -> {
                    if (virtualWorld.board[finalI][finalJ] == null) {
                        String[] options = {"wolf", "sheep", "fox", "antelope", "turtle", "milkweed", "Sosnowski borscht", "guarana", "nightshade berries", "grass"};
                        String choice = (String) JOptionPane.showInputDialog(boardFrame, "choose organism to add:", "add Organism", JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
                        if (choice != null) {
                            Organism newOrg = virtualWorld.createOrganismFromChoice(choice, finalI, finalJ, virtualWorld);
                            if (newOrg != null) {
                                virtualWorld.addOrganism(newOrg);
                                updateGrid(virtualWorld);
                            }
                        }
                    }
                });
                gridPanel.add(button);
            }
        }

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(infoLabel, BorderLayout.WEST);
        topPanel.add(roundLabel, BorderLayout.EAST);

        JPanel updatesPanel = new JPanel();
        updatesPanel.setLayout(new BoxLayout(updatesPanel, BoxLayout.Y_AXIS));
        updatesPanel.add(infoLabel);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(3, 1));
        buttonsPanel.add(shieldButton);
        buttonsPanel.add(saveButton);
        buttonsPanel.add(quitButton);

        updatesPanel.add(buttonsPanel);
        updatesPanel.add(new JScrollPane(updatesArea));

        boardFrame.add(topPanel, BorderLayout.NORTH);
        boardFrame.add(updatesPanel, BorderLayout.WEST);
        boardFrame.add(gridPanel, BorderLayout.CENTER);

        boardFrame.revalidate();
        boardFrame.repaint();
        boardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        boardFrame.setVisible(true);
    }

    public void updateGameInfo(String info) {
        updatesArea.append(info + "\n");
    }

    public void updateRoundCounter(World world) {
        roundLabel.setText("ROUND: " + world.getRoundCounter());
    }

    public int getTurn() {
        return turn;
    }

    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    public void clearGameInfo() {
        updatesArea.setText("");
    }

    public boolean isSaveRequested() {
        return saveRequested;
    }

    public void setSaveRequested(boolean saveRequested) {
        this.saveRequested = saveRequested;
    }

    public boolean isContinueRequested() {
        return continueRequested;
    }

}
