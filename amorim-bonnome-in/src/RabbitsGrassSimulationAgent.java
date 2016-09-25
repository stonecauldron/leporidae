import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;
import uchicago.src.sim.space.Object2DGrid;

import java.awt.*;


/**
 * Class that implements the simulation agent for the rabbits grass simulation.
 *
 * @author
 */

public class RabbitsGrassSimulationAgent implements Drawable {



    private static int IDNumber = 0;
    private int ID = IDNumber++;

    private int x = -1,y=-1;
    private int energy;
    private float grassEnergy;
    private RabbitsGrassSimulationSpace rbSpace=null;


    private enum DIRECTION{
        NORTH,EAST,SOUTH,WEST;

        public static DIRECTION rnd(){
            int tmp = (int)(Math.random()*4);
            return DIRECTION.values()[tmp];
        }

        public int displacedX(int fromX){
            return this == EAST ? fromX +1:
                   this == WEST ? fromX-1:
                   fromX;
        }

        public int displacedY(int fromY){
            return this == NORTH ? fromY-1:
                   this == SOUTH ? fromY+1:
                   fromY;
        }


    }

    public RabbitsGrassSimulationAgent(int initialEnergy, float grassEnergy) {

        this.energy = initialEnergy;
        this.grassEnergy = grassEnergy;
    }



    public void step() {

        DIRECTION inDir = DIRECTION.rnd();
        Object2DGrid grid = rbSpace.getAgentSpace();

        int newX = ( inDir.displacedX(x)+grid.getSizeX() ) % grid.getSizeX();
        int newY = ( inDir.displacedY(y)+grid.getSizeY() ) % grid.getSizeY();

        if (tryMove(newX, newY)) {
            energy += grassEnergy*rbSpace.eatGrassAt(x, y);
        }

        energy--;
    }


    public void draw(SimGraphics G) {
        G.drawFastRoundRect(Color.white);
    }

    public void report() {
        System.out.println(getID() + "," + getX() + "," + getY() + "," + getEnergy());
    }

    public String getID() {
        return "A-" + ID;
    }

    public int getEnergy() {
        return energy;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setXY(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }

    public void setRbSpace(RabbitsGrassSimulationSpace spc) {
        rbSpace = spc;
    }

    private boolean tryMove(int newX, int newY) {
        return rbSpace.moveAgentAt(x, y, newX, newY);
    }



}
