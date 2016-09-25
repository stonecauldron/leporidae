import uchicago.src.sim.space.Object2DGrid;

/**
 * Class that implements the simulation space of the rabbits grass simulation.
 *
 * @author
 */

public class RabbitsGrassSimulationSpace {
    private Object2DGrid grassSpace;
    private Object2DGrid agentSpace;

    public RabbitsGrassSimulationSpace(int gridSize) {
        grassSpace = new Object2DGrid(gridSize, gridSize);
        agentSpace = new Object2DGrid(gridSize, gridSize);

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                grassSpace.putObjectAt(i, j, 0);
            }
        }
    }

    public void growGrass(int grass, int grassEnergy) {
        int count = 0;
        while (count < grass) {
            int x = (int) (Math.random() * (grassSpace.getSizeX()));
            int y = (int) (Math.random() * (grassSpace.getSizeY()));

            if (isCellOccupied(x, y, grassSpace)) {
                grassSpace.putObjectAt(x, y, 1);
                count++;
            }
        }
    }

    public int getGrassAt(int x, int y) {
        int i;
        if (grassSpace.getObjectAt(x, y) != null) {
            i = (int) grassSpace.getObjectAt(x, y);
        } else {
            i = 0;
        }
        return i;
    }

    public Object2DGrid getGrassSpace() {
        return grassSpace;
    }

    public Object2DGrid getAgentSpace() {
        return agentSpace;
    }

    public boolean isCellOccupied(int x, int y, Object2DGrid grid) {
        boolean retValue = false;
        if (grid.getObjectAt(x, y) != null) retValue = true;
        return retValue;
    }

    public boolean addAgent(RabbitsGrassSimulationAgent agent) {
        boolean retVal = false;
        int count = 0;
        int countLimit = 10 * agentSpace.getSizeX() * agentSpace.getSizeY();

        while ((retVal == false) && (count < countLimit)) {
            int x = (int) (Math.random() * (agentSpace.getSizeX()));
            int y = (int) (Math.random() * (agentSpace.getSizeY()));

            if (!isCellOccupied(x, y, agentSpace)) {
                agentSpace.putObjectAt(x, y, agent);
                agent.setXY(x, y);
                agent.setRbSpace(this);
                retVal = true;
            }
            count++;
        }
        return retVal;
    }

    public void removeAgentAt(int x, int y) {
        agentSpace.putObjectAt(x, y, null);
    }

    public int eatGrassAt(int x, int y) {
        int grass = getGrassAt(x, y);
        grassSpace.putObjectAt(x, y, 0);
        return grass;
    }

    public boolean moveAgentAt(int x, int y, int newX, int newY) {
        boolean retVal = false;
        if (!isCellOccupied(newX, newY, agentSpace)) {
            RabbitsGrassSimulationAgent agnt = (RabbitsGrassSimulationAgent) agentSpace.getObjectAt(x, y);
            removeAgentAt(x, y);
            agnt.setXY(newX, newY);
            agentSpace.putObjectAt(newX, newY, agnt);
            retVal = true;
        }
        return retVal;
    }
}































































