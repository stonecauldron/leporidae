import uchicago.src.sim.space.Object2DGrid;

/**
 * Class that implements the simulation space of the rabbits grass simulation.
 * It store the agent on a 2D space and the total amount of grass by cell.
 * The total amount of grass are bounded to 32 unit by cell.
 */

public class RabbitsGrassSimulationSpace {



    private Object2DGrid grassSpace;
    private Object2DGrid agentSpace;
    private int maxGrassByCell = 0;


    /**
     * @param gridSize
     * @param maxGrassByCell
     *
     * instanciate the space without agent and with 0 unit grass by cell.
     */
    public RabbitsGrassSimulationSpace(int gridSize, int maxGrassByCell) {

        this.grassSpace = new Object2DGrid(gridSize, gridSize);
        this.agentSpace = new Object2DGrid(gridSize, gridSize);

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                this.grassSpace.putObjectAt(i, j, 0);
            }
        }

        this.maxGrassByCell = maxGrassByCell;

    }


    /**
     *
     * @param grass
     * randomly spread the total amount of grass into the 2DSpace.
     * If a unit is assgined to a cell that already have 32 unit of grass,
     * the surplus is drop.
     */
    public void growGrass(int grass) {

        for(int i = 0; i<grass; i++) {
            int x = (int) (Math.random() * (grassSpace.getSizeX()));
            int y = (int) (Math.random() * (grassSpace.getSizeY()));
            int newValue = Math.min(getGrassAt(x, y) + 1, maxGrassByCell);
            grassSpace.putObjectAt(x, y, newValue);
        }

    }


    /**
     *
     * @param x
     * @param y
     * @return the total amount of grass at the given position.
     */
    public int getGrassAt(int x, int y) {

        boolean isOutside = x<0 || y<0 || x>=grassSpace.getSizeX() || y>= grassSpace.getSizeY();

        if(isOutside){
            return 0;
        }

        return (int)grassSpace.getObjectAt(x, y);

    }


    /**
     *
     * @return the grass space.
     */
    public Object2DGrid getGrassSpace() {
        return grassSpace;
    }


    /**
     *
     * @return the agent space
     */
    public Object2DGrid getAgentSpace() {
        return agentSpace;
    }


    /**
     *
     * @param x
     * @param y
     * @param grid
     * @return true if the cell already contains a value that's not null
     */
    public boolean isCellOccupied(int x, int y, Object2DGrid grid) {
        boolean retValue = false;
        if (grid.getObjectAt(x, y) != null) retValue = true;
        return retValue;
    }


    /**
     *
     * @param agent
     * @return add the agent and return true if he is correctly added to the space
     */
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
                retVal = true;
            }
            count++;
        }
        return retVal;
    }

    /**
     * @param x
     * @param y
     * remove the agent at the given position
     */
    public void removeAgentAt(int x, int y) {
        agentSpace.putObjectAt(x, y, null);
    }

    /**
     *
     * @param x
     * @param y
     * @return remove the grass at (x,y) and return the value
     */
    public int eatGrassAt(int x, int y) {
        int grass = getGrassAt(x, y);
        grassSpace.putObjectAt(x, y, 0);
        return grass;
    }

    /**
     *
     * @param x
     * @param y
     * @param newX
     * @param newY
     * @return if the agent can be moved, the method returns true.
     * move the agent at the given (x,y) to the new position (newX,newY)
     * if no agent is currently at the new position.
     */
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


    /**
     *
     * @return the total amount of grass over the space.
     */
    public int getTotalGrassInSpace(){
        int total = 0;

        for(int x=0;x<grassSpace.getSizeX();x++){
            for(int y=0;y<grassSpace.getSizeY();y++){
                total += getGrassAt(x,y);
            }
        }

        return total;
    }


}































































