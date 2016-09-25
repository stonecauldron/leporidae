


Pedro Caldeira
Hugo Bonnome




______________________________________________________________ RABBIT GRASS SIMULATION






__________________________________________________ MAIN RABBIT

Main class of the software.
It used to start a rabbits grass simulation.


/**
 * @param args
 * start repast on execution
 */
public static void main(String[] args)







__________________________________________________ MODEL

Class that implements the simulation model for the rabbits grass
simulation.  This is the first class which needs to be setup in
order to run Repast simulation. It manages the entire RePast
environment and the simulation.



/**
 * Class executed by scheduler to construct histogram.
 * It counts the total number of living rabbits.
 *
 */
class TotalRabbit implements DataSource, Sequence

/**
 * Class executed by scheduler to construct histogram.
 * It counts the total number of grass available on the space.
 */
class TotalGrass implements DataSource, Sequence

/**
 *
 * @param args
 * instanciate a new simulation and the model.
 */
public static void main(String[] args)

/**
 *
 * reset and setup the simulation before running it.
 */
@Override
public void setup()

/**
 * Use by repast when the user want to start a new simulation.
 * it build the model, the scheduler and the display.
 */
@Override
public void begin()

/**
 * build the model based on the simulation values
 */
private void buildModel()

/**
 * build the schedule
 */
private void buildSchedule()

/**
 * build the display of the simulation
 */
private void buildDisplay()

/**
 * add a new agent in the simulation.
 */
private void addNewAgent()

/**
 * create new agent in the simulation for all agents having their energy upper than
 * the birthThreshold. All agents that give birth to a new rabbit will have their energy
 * decrease by the BIRTH_COST.
 */
private void makeBirth()

/**
 * remove all agents having less than 1 point of energy
 */
private void removeDeadAgents()

/**
 *
 * @return parameters that can be changed on repast
 */
public String[] getInitParam()

/**
 *
 * @return the name of the simulation
 */
public String getName()

/**
 *
 * @return the scheduler of the simulation
 */
public Schedule getSchedule()

/**
 *
 * @return the total number of agents available at the start of the simulation
 */
public int getNumAgents()

/**
 *
 * parametrize total number of agents available at the start of the simulation
 */
public void setNumAgents(int na)

/**
 *
 * @return the grid dimension
 */
public int getGridSize()

/**
 * @return the birthThreeshold parameters used to define at which level rabbit reproduces
 */
public int getBirthThreshold()

/**
 * set the birthThreeshold parameters used to define at which level rabbit reproduces
 */
public void setBirthThreshold(int birthThreshold)

/**
 * @return the total number of grass added at each step
 */
public int getGrassGrowthRate()

/**
 * set the total number of grass added at each step
 */
public void setGrassGrowthRate(int grassGrowthRate)

/**
 * @return the initial energy given to the rabbits
*/
public int getInitialEnergy()

/**
 * set the initial energy given to the rabbits
 */
public void setInitialEnergy(int e)




__________________________________________________ SPACE

Class that implements the simulation space of the rabbits grass simulation.
It store the agent on a 2D space and the total amount of grass by cell.
The total amount of grass are bounded to 32 unit by cell.



/**
 * @param gridSize
 * @param maxGrassByCell
 *
 * instanciate the space without agent and with 0 unit grass by cell.
 */
public RabbitsGrassSimulationSpace(int gridSize, int maxGrassByCell)


/**
 * @param grass
 * randomly spread the total amount of grass into the 2DSpace.
 * If a unit is assgined to a cell that already have 32 unit of grass,
 * the surplus is drop.
 */
public void growGrass(int grass)

/**
 * @param x
 * @param y
 * @return the total amount of grass at the given position.
 */
public int getGrassAt(int x, int y)


/**
 * @return the grass space.
 */
public Object2DGrid getGrassSpace()


/**
 * @return the agent space
 */
public Object2DGrid getAgentSpace()


/**
 * @param x
 * @param y
 * @param grid
 * @return true if the cell already contains a value that's not null
 */
public boolean isCellOccupied(int x, int y, Object2DGrid grid)


/**
 * @param agent
 * @return add the agent and return true if he is correctly added to the space
 */
public boolean addAgent(RabbitsGrassSimulationAgent agent)

/**
 * @param x
 * @param y
 * remove the agent at the given position
 */
public void removeAgentAt(int x, int y)

/**
 * @param x
 * @param y
 * @return remove the grass at (x,y) and return the value
 */
public int eatGrassAt(int x, int y)

/**
 * @param x
 * @param y
 * @param newX
 * @param newY
 * @return if the agent can be moved, the method returns true.
 * move the agent at the given (x,y) to the new position (newX,newY)
 * if no agent is currently at the new position.
 */
public boolean moveAgentAt(int x, int y, int newX, int newY)

/**
 * @return the total amount of grass over the space.
 */
public int getTotalGrassInSpace()








__________________________________________________ AGENT

A rabbit agent for the simulation.
Our agent wander randomly on each step over a 2D space (torus)
and is defined by is energy level.




/**
 * DIRECTION ENUM : NORTH,EAST,SOUTH,WEST
 * help to define the next coordinates where the rabbit should go.
 */
private enum DIRECTION


/**
 * @param initialEnergy : initial energy of the agent
 * @param grassEnergy : convertion grass => energy
 * @param rbSpace : rabbits/grass space
 */
public RabbitsGrassSimulationAgent(
        int initialEnergy,
        float grassEnergy,
        RabbitsGrassSimulationSpace rbSpace)

/**
 * update local position of the rabbit, position over the shared space and local energy.
 */
public void step()

/**
 * used by repast to draw the rabbit on the space
 */
@Override
public void draw(SimGraphics G)

/**
 * print information about the agent
 */
public void report()

/**
 * @return the id of the agent
 */
public String getID()


/**
 * @return the energy of the agent
 */
public int getEnergy()


/**
 * @param value : energy
 * put the agent's energy to the given value
 */
public void setEnergy(int value)

/**
 * @return the x position of the agent
 */
public int getX()

/**
 * @return the y position of the agent
 */
public int getY()


/**
 * @param newX
 * @param newY
 * set the position of the agent to the given parameters
 */
public void setXY(int newX, int newY)


