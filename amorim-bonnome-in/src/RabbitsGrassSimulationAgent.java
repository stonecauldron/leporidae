import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;
import uchicago.src.sim.space.Object2DGrid;

import java.awt.*;


/**
 * A rabbit agent for the simulation.
 * Our agent wanders randomly at each step over a 2D space (torus)
 * and is defined by its energy level.
 */

public class RabbitsGrassSimulationAgent implements Drawable {



    private static int IDNumber = 0;
    private int ID = IDNumber++;

    private int x = -1,y=-1;
    private int energy;
    private float grassEnergy;
    private RabbitsGrassSimulationSpace rbSpace=null;



    /**
     * DIRECTION ENUM : NORTH,EAST,SOUTH,WEST
     * helper enum to facilitate the rabbit's movement
     */
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


    /**
     *
     * @param initialEnergy : initial energy of the agent
     * @param grassEnergy : conversion grass => energy
     * @param rbSpace : rabbits/grass space
     */
    public RabbitsGrassSimulationAgent(
            int initialEnergy,
            float grassEnergy,
            RabbitsGrassSimulationSpace rbSpace) {

        this.energy = initialEnergy;
        this.grassEnergy = grassEnergy;
        this.rbSpace = rbSpace;
    }


    /**
     * update local position of the rabbit, position over the shared space and local energy.
     */
    public void step() {

        DIRECTION inDir = DIRECTION.rnd();
        Object2DGrid grid = rbSpace.getAgentSpace();

        int newX = ( inDir.displacedX(x)+grid.getSizeX() ) % grid.getSizeX();
        int newY = ( inDir.displacedY(y)+grid.getSizeY() ) % grid.getSizeY();

        // if move succeeded
        if (rbSpace.moveAgentAt(x, y, newX, newY)) {
            energy += grassEnergy*rbSpace.eatGrassAt(x, y);
        }

        energy--;
    }

    /**
     * used by repast to draw the rabbit on the space
     */
    @Override
    public void draw(SimGraphics G) {
        G.drawFastRoundRect(Color.white);
    }

    /**
     * print information about the agent
     */
    public void report() {

        System.out.println(getID() + ","
                        + getX()
                        + "," + getY()
                        + "," + getEnergy());
    }

    /**
     *
     * @return the id of the agent
     */
    public String getID() {
        return "A-" + ID;
    }


    /**
     *
     * @return the energy of the agent
     */
    public int getEnergy() {
        return energy;
    }


    /**
     *
     * @param value : energy
     * put the agent's energy to the given value
     */
    public void setEnergy(int value){
        this.energy = value;
    }

    /**
     *
     * @return the x position of the agent
     */
    public int getX() {
        return x;
    }

    /**
     *
     * @return the y position of the agent
     */
    public int getY() {
        return y;
    }


    /**
     *
     * @param newX
     * @param newY
     * set the position of the agent to the given parameters
     */
    public void setXY(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }
}
