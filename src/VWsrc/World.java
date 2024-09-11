package VWsrc;
import VWsrc.Animals.*;
import VWsrc.Plants.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class World {
        private int columns;
        private int rows;
        public Organism[][] board;
        private List<Organism> organismsList;
        public List<String> collisionResults;
        private int roundCounter;

        public World() {
                this.organismsList = new ArrayList<>();
                this.collisionResults = new ArrayList<>();
                this.roundCounter = 0;
        }

        public void setWorld(int rows, int columns) { //needed seperate
                organismsList.clear();
                this.rows = rows;
                this.columns = columns;
                board = new Organism[rows][columns];
                for (int i = 0; i < rows; i++) {
                        for (int j = 0; j < columns; j++) {
                                board[i][j] = null;
                        }
                }
        }

        public void addOrganism(Organism org) { //both to list and board
                int x = org.getRow();
                int y = org.getColumn();
                if (x >= 0 && x < rows && y >= 0 && y < columns && board[x][y] == null) {
                        board[x][y] = org;
                        organismsList.add(org);
                }
        }

        public void executeTurn(int key) {
                roundCounter++;
                sortOrganisms();
                clearCollisionResults();

                //setting "states"
                for (int i = 0; i < organismsList.size(); i++) {
                        Organism org = organismsList.get(i);
                        if (org instanceof Animal && !org.getIsDead()) {
                                ((Animal) org).setHasReproduced(false);
                                ((Animal) org).setAttackBlocked(false);
                                org.increaseAge();
                        }
                        if (org instanceof Plant && !org.getIsDead()) {
                                org.increaseAge();
                        }
                        if (org instanceof Player && !org.getIsDead()) {
                                ((Player) org).decreaseShieldCounter();
                        }
                }

                //action
                for (int i = 0; i < organismsList.size(); i++) {
                        Organism org = organismsList.get(i);
                        if (org.getAge() > 0 && !org.getIsDead()) {
                                org.action(key);
                        }
                }

                //collision
                for (int i = 0; i < organismsList.size(); i++) {
                        Organism org = organismsList.get(i);
                        if (!org.getIsDead()) {
                                int x = org.getRow();
                                int y = org.getColumn();
                                Organism target = getOrganismAtPosition(x, y);
                                if (target != null && target != org) {
                                        org.collision(target);
                                }
                        }
                }
                removeDeadOrganisms();
                //to board with new ones
                for (int i = 0; i < organismsList.size(); i++) {
                        Organism org = organismsList.get(i);
                        board[org.getRow()][org.getColumn()] = org;
                }
        }

        public int getRoundCounter() {
                return roundCounter;
        }

        public void setRoundCounter(int count) {
                this.roundCounter=count;
        }

        public boolean isPlayerTheLastAnimal() {
                int animalCount = 0;
                for (Organism organism : organismsList) {
                        if (organism instanceof Animal) {
                                animalCount++;
                                if (!(organism instanceof Player)) {
                                        return false;
                                }
                        }
                }
                return animalCount == 1;
        }

        public boolean isPlayerAlive() {
                for (Organism organism : organismsList) {
                        if (organism instanceof Player && !organism.getIsDead()) {
                                return true;
                        }
                }
                return false;
        }

        public void saveGame(String filename) throws IOException {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
                        writer.write(rows + "x" + columns); // rows x columns
                        writer.newLine(); //new line
                        Player player = getPlayer();
                        if (player != null) { //shield
                                writer.write(player.isShieldActive() ? "t" + player.getShieldCounter() : "n");
                        } else {
                                writer.write("n");
                        }
                        writer.newLine(); //new line
                        for (Organism organism : organismsList) { //organisms
                                writer.write(organism.getTypeName() + "," + organism.getAge() + "," + organism.getStrength() + "," + organism.getRow() + "," + organism.getColumn());
                                writer.newLine(); //new line after ech
                        }
                }
        }

        public void addRandomOrganisms(){
                Random random = new Random();
                int row=0;
                int column=0;
                int organismcounter=1+(rows*columns/50);
                for(int i=0; i<organismcounter; i++){
                        while(row==0 && column==0){
                                row = random.nextInt(rows) ;  // -1, 0, 1
                                column = random.nextInt(columns);
                        }
                        addOrganism(new Sheep(row, column, this));
                        row=0;
                        column=0;
                }
                for(int i=0; i<organismcounter; i++){
                        while(row==0 && column==0){
                                row = random.nextInt(rows) ;  // -1, 0, 1
                                column = random.nextInt(columns);
                        }
                        addOrganism(new Wolf(row, column, this));
                        row=0;
                        column=0;
                }
                for(int i=0; i<organismcounter; i++){
                        while(row==0 && column==0){
                                row = random.nextInt(rows) ;  // -1, 0, 1
                                column = random.nextInt(columns);
                        }
                        addOrganism(new Turtle(row, column, this));
                        row=0;
                        column=0;
                }
                for(int i=0; i<organismcounter; i++){
                        while(row==0 && column==0){
                                row = random.nextInt(rows) ;  // -1, 0, 1
                                column = random.nextInt(columns);
                        }
                        addOrganism(new Fox(row, column, this));
                        row=0;
                        column=0;
                }
                for(int i=0; i<organismcounter; i++){
                        while(row==0 && column==0){
                                row = random.nextInt(rows) ;  // -1, 0, 1
                                column = random.nextInt(columns);
                        }
                        addOrganism(new Antelope(row, column, this));
                        row=0;
                        column=0;
                }
                for(int i=0; i<organismcounter; i++){
                        while(row==0 && column==0){
                                row = random.nextInt(rows) ;  // -1, 0, 1
                                column = random.nextInt(columns);
                        }
                        addOrganism(new Borscht(row, column, this));
                        row=0;
                        column=0;
                }
                for(int i=0; i<organismcounter; i++){
                        while(row==0 && column==0){
                                row = random.nextInt(rows) ;  // -1, 0, 1
                                column = random.nextInt(columns);
                        }
                        addOrganism(new Grass(row, column, this));
                        row=0;
                        column=0;
                }
                for(int i=0; i<organismcounter; i++){
                        while(row==0 && column==0){
                                row = random.nextInt(rows) ;  // -1, 0, 1
                                column = random.nextInt(columns);
                        }
                        addOrganism(new Guarana(row, column, this));
                        row=0;
                        column=0;
                }
                for(int i=0; i<organismcounter; i++){
                        while(row==0 && column==0){
                                row = random.nextInt(rows) ;  // -1, 0, 1
                                column = random.nextInt(columns);
                        }
                        addOrganism(new Milkweed(row, column, this));
                        row=0;
                        column=0;
                }
                for(int i=0; i<organismcounter; i++){
                        while(row==0 && column==0){
                                row = random.nextInt(rows) ;  // -1, 0, 1
                                column = random.nextInt(columns);
                        }
                        addOrganism(new Nightshadeberries(row, column, this));
                        row=0;
                        column=0;
                }

        }

        public Player getPlayer() {
                for (Organism organism : organismsList) {
                        if (organism instanceof Player) {
                                return (Player) organism;
                        }
                }
                return null;
        }

        private void removeDeadOrganisms() {
                Iterator<Organism> iterator = organismsList.iterator();
                while (iterator.hasNext()) {
                        Organism org = iterator.next();
                        if (org.getIsDead()) {
                                iterator.remove();
                                board[org.getRow()][org.getColumn()] = null;
                        }
                }
        }

        public boolean isPositionFree(int row, int column) {
                return isPositionInTheBoard(row, column) && board[row][column] == null;
        }

        public boolean isPositionInTheBoard(int rowToCheck, int columnToCheck) {
                return rowToCheck >= 0 && rowToCheck < this.rows && columnToCheck >= 0 && columnToCheck < this.columns;
        }

        public Organism getOrganismAtPosition(int row, int column) {
                if (isPositionInTheBoard(row, column)) {
                        return board[row][column];
                }
                return null;
        }

        public void updatePosition(Organism org, int newRow, int newColumn) {
                board[org.getRow()][org.getColumn()] = null;
                org.setPrevRow(org.getRow()); //setting prev-s
                org.setPrevColumn(org.getColumn());
                org.setRow(newRow); //new
                org.setColumn(newColumn);
                board[newRow][newColumn] = org;
        }

        public int getColumns() {
                return columns;
        }

        public int getRows() {
                return rows;
        }

        public void addCollisionResult(String result) {
                collisionResults.add(result);
        }

        public void clearCollisionResults() {
                collisionResults.clear();
        }

        private void sortOrganisms() {
                organismsList.sort((o1, o2) -> {
                        if (o1.getInitiative() != o2.getInitiative()) {
                                return o2.getInitiative() - o1.getInitiative(); //initiative
                        } else {
                                return o2.getAge() - o1.getAge(); //age
                        }
                });
        }

        public Organism createOrganismFromChoice(String choice, int row, int column, World virtualWorld) {
                switch (choice.toLowerCase()) {
                        case "wolf":
                                return new Wolf(row, column, virtualWorld);
                        case "sheep":
                                return new Sheep(row, column, virtualWorld);
                        case "fox":
                                return new Fox(row, column, virtualWorld);
                        case "antelope":
                                return new Antelope(row, column, virtualWorld);
                        case "turtle":
                                return new Turtle(row, column, virtualWorld);
                        case "milkweed":
                                return new Milkweed(row, column, virtualWorld);
                        case "sosnowski borscht":
                                return new Borscht(row, column, virtualWorld);
                        case "guarana":
                                return new Guarana(row, column, virtualWorld);
                        case "nightshade berries":
                                return new Nightshadeberries(row, column, virtualWorld);
                        case "grass":
                                return new Grass(row, column, virtualWorld);
                        default:
                                return null;
                }
        }

        public Organism createOrganismThatExisted(String typeName, int row, int column, int strength, int age) {
                Organism organism = null;
                switch (typeName) {
                        case "wolf":
                                organism = new Wolf(row, column, this);
                                break;
                        case "sheep":
                                organism = new Sheep(row, column, this);
                                break;
                        case "fox":
                                organism = new Fox(row, column, this);
                                break;
                        case "antelope":
                                organism = new Antelope(row, column, this);
                                break;
                        case "turtle":
                                organism = new Turtle(row, column, this);
                                break;
                        case "you":
                                organism = new Player(row, column, this);
                                break;
                        case "milkweed":
                                organism = new Milkweed(row, column, this);
                                break;
                        case "Sosnowski borscht":
                                organism = new Borscht(row, column, this);
                                break;
                        case "guarana":
                                organism = new Guarana(row, column, this);
                                break;
                        case "nightshade berries":
                                organism = new Nightshadeberries(row, column, this);
                                break;
                        case "grass":
                                organism = new Grass(row, column, this);
                                break;

                }
                if (organism != null) {
                        organism.setStrength(strength);
                        for (int i = 0; i < age; i++) {
                                organism.increaseAge();
                        }
                }
                return organism;
        }
}
