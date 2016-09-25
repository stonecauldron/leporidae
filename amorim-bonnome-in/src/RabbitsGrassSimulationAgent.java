import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;
import uchicago.src.sim.space.Object2DGrid;

import java.awt.*;
import java.util.Arrays;
import java.util.List;


/**
 * Class that implements the simulation agent for the rabbits grass simulation.
 *
 * @author
 */

public class RabbitsGrassSimulationAgent implements Drawable {
    private int x;
    private int y;
    private int vX;
    private int vY;
    private int energy;
    private static int IDNumber = 0;
    private int ID;
    private RabbitsGrassSimulationSpace rbSpace;

    public RabbitsGrassSimulationAgent(int initialEnergy) {
        x = -1;
        y = -1;
        energy = initialEnergy;
        IDNumber++;
        ID = IDNumber;
    }

    private void setVxVy() {
        vX = 0;
        vY = 0;
        while ((vX == 0) && (vY == 0)) {
            vX = (int) Math.floor(Math.random() * 3) -1;
            vY = (int) Math.floor(Math.random() * 3) -1;
        }
    }

    public void step() {
        int newX = x + vX;
        int newY = y + vY;

        Object2DGrid grid = rbSpace.getAgentSpace();
        newX = (newX + grid.getSizeX()) % grid.getSizeX();
        newY = (newY + grid.getSizeY()) % grid.getSizeY();

        if (tryMove(newX, newY)) {
            energy += rbSpace.eatGrassAt(x, y);
        } else {
            setVxVy();
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
        x = newX;
        y = newY;
    }

    public void setRbSpace(RabbitsGrassSimulationSpace spc) {
        rbSpace = spc;
    }

    private boolean tryMove(int newX, int newY) {
        return rbSpace.moveAgentAt(x, y, newX, newY);
    }

}
