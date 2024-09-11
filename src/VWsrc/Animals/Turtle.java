package VWsrc.Animals;
import VWsrc.Organism;
import VWsrc.Plant;
import VWsrc.World;
import VWsrc.Animal;
import javax.swing.*;
import java.util.Objects;
import java.util.Random;

public class Turtle extends Animal {
    public Turtle(int row, int column, World virtualWorld) {
        super(row, column, 1, 2, virtualWorld);
    }

    @Override
    public ImageIcon getImage() {
        return new ImageIcon(Objects.requireNonNull(getClass().getResource("/VWsrc/icons/turtle.png")));
    }

    @Override
    public void action(int key){
        Random random = new Random();
        int randomNumber = random.nextInt(4);
        if(randomNumber==1){
            super.action(key);
        }
    }

    @Override
    public void collision(Organism opponent){
        if (opponent != null && !opponent.getIsDead()) {
            Animal opponentAnimal = (opponent instanceof Animal) ? (Animal) opponent : null;
            Plant opponentPlant = (opponent instanceof Plant) ? (Plant) opponent : null;
            if (opponentAnimal != null) {
                if(this.getTypeName().equals(opponentAnimal.getTypeName()) && opponentAnimal!=this && !opponentAnimal.getIsDead()){ //other turtle
                    super.collision(opponentAnimal);
                }
                else if(opponentAnimal.isAttackBlocked()){
                    return;
                }
                else if (this.getStrength() > opponentAnimal.getStrength() && !this.getIsDead() ) { //turtle wins
                    super.collision(opponentAnimal);
                } else if(opponentAnimal.getStrength()<5) { //deflect
                    this.setAttackBlocked(true);
                    if(opponentAnimal.getPrevRow()==-1 && opponentAnimal.getPrevColumn()==-1){
                        opponentAnimal.setIsDead(true); //cant deflect so he kills it
                    } else{
                        virtualWorld.updatePosition(opponentAnimal, opponentAnimal.getPrevRow(), opponentAnimal.getPrevColumn());
                        virtualWorld.addCollisionResult(this.getTypeName() + " has deflect attack from " + opponent.getTypeName() + ".");

                    }
                    } else if (!this.getIsDead()) { // this strength bigger or equal
                    super.collision(opponentAnimal);
                }
            } else if (opponentPlant != null) { // plant
                super.collision(opponentPlant);
            }
        }
    }

    @Override
    public String getTypeName() {
        return "turtle";
    }

}
