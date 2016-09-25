import uchicago.src.sim.space.Object2DGrid;

/**
 * Class that implements the simulation space of the rabbits grass simulation.
 *
 * @author
 */

public class RabbitsGrassSimulationSpace {



    private Object2DGrid grassSpace;
    private Object2DGrid agentSpace;

    private int maxGrassByCell = 0;



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


    public void growGrass(int grass) {

        for(int i = 0; i<grass; i++) {
            int x = (int) (Math.random() * (grassSpace.getSizeX()));
            int y = (int) (Math.random() * (grassSpace.getSizeY()));
            int newValue = Math.min(getGrassAt(x, y) + 1, maxGrassByCell);
            grassSpace.putObjectAt(x, y, newValue);
        }

    }


    public int getGrassAt(int x, int y) {

        boolean isOutside = x<0 || y<0 || x>=grassSpace.getSizeX() || y>= grassSpace.getSizeY();

        if(isOutside){
            return 0;
        }

        return (int)grassSpace.getObjectAt(x, y);

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































































