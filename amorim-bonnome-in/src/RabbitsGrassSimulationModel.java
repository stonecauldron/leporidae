import uchicago.src.sim.analysis.DataSource;
import uchicago.src.sim.analysis.OpenSequenceGraph;
import uchicago.src.sim.analysis.Sequence;
import uchicago.src.sim.engine.BasicAction;
import uchicago.src.sim.engine.Schedule;
import uchicago.src.sim.engine.SimInit;
import uchicago.src.sim.engine.SimModelImpl;
import uchicago.src.sim.gui.ColorMap;
import uchicago.src.sim.gui.DisplaySurface;
import uchicago.src.sim.gui.Object2DDisplay;
import uchicago.src.sim.gui.Value2DDisplay;
import uchicago.src.sim.util.SimUtilities;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that implements the simulation model for the rabbits grass
 * simulation.  This is the first class which needs to be setup in
 * order to run Repast simulation. It manages the entire RePast
 * environment and the simulation.
 */


public class RabbitsGrassSimulationModel extends SimModelImpl {




    // default values
    private static final int NUM_AGENTS = 5;
    private static final int GRID_SIZE = 20;
    private static final int BIRTH_THRESHOLD = 80;
    private static final int BIRTH_COST = 20;
    private static final int GRASS_GROWTH = 10;
    private static final float GRASS_ENERGY = 1.5f;
    private static final int INITIAL_ENERGY = 50;
    private static final int MAX_GRASS_BY_CELL = 32;

    private int numAgents = NUM_AGENTS;
    private int gridSize = GRID_SIZE;
    private int birthThreshold = BIRTH_THRESHOLD;
    private int grassGrowthRate = GRASS_GROWTH;
    private float grassEnergy = GRASS_ENERGY;
    private int initialEnergy = INITIAL_ENERGY;

    private Schedule schedule;

    private RabbitsGrassSimulationSpace rbSpace;

    private List<RabbitsGrassSimulationAgent> agentList;

    private DisplaySurface displaySurface;


    private OpenSequenceGraph populationGraph;




    /**
     * Class executed by the scheduler to construct a histogram of
     * the rabbit's population.
     */
    class TotalRabbit implements DataSource, Sequence {

        @Override
        public Object execute() {
            return new Double(getSValue());
        }

        @Override
        public double getSValue() {
            return (double)agentList.size();
        }

    }



    /**
     * Class executed by the scheduler to construct a histogram of
     * the total amount of grass in the simulation
     */
    class TotalGrass implements DataSource, Sequence {

        @Override
        public Object execute() {
            return new Double(getSValue());
        }

        @Override
        public double getSValue() {
            return (double)rbSpace.getTotalGrassInSpace()/10;
        }

    }



    /**
     *
     * @param args
     * instantiate a new simulation and the model.
     */
    public static void main(String[] args) {
        SimInit init = new SimInit();
        RabbitsGrassSimulationModel model = new RabbitsGrassSimulationModel();
        init.loadModel(model, "", false);
    }



    /**
     *
     * reset and setup the simulation before running it.
     */
    @Override
    public void setup() {
        System.out.println("Running setup");

        rbSpace = null;
        agentList = new ArrayList();
        schedule = new Schedule(1);


        if (displaySurface != null) {
            displaySurface.dispose();
        }
        displaySurface = new DisplaySurface(this, "Rabbits Window 1");
        registerDisplaySurface("Rabbits Window 1", displaySurface);


        if(populationGraph != null){
            populationGraph.dispose();
        }
        populationGraph = new OpenSequenceGraph("Population graph",this);
        this.registerMediaProducer("Plot", populationGraph);
    }


    /**
     * Used by repast when the user want to start a new simulation.
     * it builds the model, the scheduler and the display.
     */
    @Override
    public void begin() {
        buildModel();
        buildSchedule();
        buildDisplay();

        displaySurface.display();
        populationGraph.display();
    }


    /**
     * build the model based on the simulation values
     */
    private void buildModel() {
        System.out.println("Running buildmodel");
        rbSpace = new RabbitsGrassSimulationSpace(gridSize,MAX_GRASS_BY_CELL);
        for (int i = 0; i < numAgents; i++) {
            addNewAgent();
        }
        for (int i = 0; i < agentList.size(); i++) {
            RabbitsGrassSimulationAgent rba = agentList.get(i);
            rba.report();
        }
    }


    /**
     * build the schedule
     */
    private void buildSchedule() {
        System.out.println("Running buildSchedule");

        class RabbitsGrassStep extends BasicAction {
            public void execute() {
                SimUtilities.shuffle(agentList);
                for (int i = 0; i < agentList.size(); i++) {
                    RabbitsGrassSimulationAgent agnt =  agentList.get(i);
                    agnt.step();
                }
                makeBirth();
                removeDeadAgents();
                rbSpace.growGrass(grassGrowthRate);
                displaySurface.updateDisplay();
            }
        }
        schedule.scheduleActionBeginning(0, new RabbitsGrassStep());


        schedule.scheduleActionAtInterval(10, new BasicAction() {
            @Override
            public void execute() {
                populationGraph.step();
            }
        });
    }


    /**
     * build the display of the simulation
     */
    private void buildDisplay() {
        System.out.println("Running buildDisplay");

        ColorMap map = new ColorMap();

        map.mapColor(0, Color.black);
        for(int i = 1; i<=MAX_GRASS_BY_CELL; i++){
            map.mapColor(i, new Color(0, 127+i*4, 0));
        }

        Value2DDisplay displayGrass = new Value2DDisplay(rbSpace.getGrassSpace(), map);
        displaySurface.addDisplayable(displayGrass, "Grass");

        Object2DDisplay displayAgents = new Object2DDisplay(rbSpace.getAgentSpace());
        displayAgents.setObjectList(agentList);

        displaySurface.addDisplayable(displayAgents, "Agents");

        populationGraph.addSequence("rabbits", new TotalRabbit());
        populationGraph.addSequence("grass", new TotalGrass());

    }



    /**
     * add a new agent in the simulation.
     */
    private void addNewAgent() {
        RabbitsGrassSimulationAgent a = new RabbitsGrassSimulationAgent(initialEnergy,grassEnergy, rbSpace);
        if(rbSpace.addAgent(a)) {
            agentList.add(a);
        }
    }


    /**
     * create a new agent in the simulation for all agents that have their energy levels above
     * the birthThreshold. All agents that give birth to a new rabbit will have their energy
     * levels decrease by the BIRTH_COST.
     */
    private void makeBirth(){

        int totalBirth = 0;
        for(RabbitsGrassSimulationAgent agnt: agentList){
            if(agnt.getEnergy()>=this.birthThreshold){
                agnt.setEnergy(agnt.getEnergy()-BIRTH_COST);
                totalBirth++;
            }
        }

        for(int i=0;i<totalBirth; i++){
            addNewAgent();
        }

    }


    /**
     * remove all agents that have less than 1 point of energy left
     */
    private void removeDeadAgents() {
        for (int i = 0; i < agentList.size(); i++) {
            RabbitsGrassSimulationAgent agnt = agentList.get(i);
            if (agnt.getEnergy() < 1) {
                rbSpace.removeAgentAt(agnt.getX(), agnt.getY());
                agentList.remove(i);
            }
        }
    }


    /**
     *
     * @return parameters that can be changed on repast
     */
    public String[] getInitParam() {
        String[] initParams = {"NumAgents", "GridSize", "BirthThreshold", "GrassGrowthRate"};
        return initParams;
    }


    /**
     *
     * @return the name of the simulation
     */
    public String getName() {
        return "Rabbits";
    }

    /**
     *
     * @return the scheduler of the simulation
     */
    public Schedule getSchedule() {
        return schedule;
    }

    /**
     *
     * @return the total number of agents available at the start of the simulation
     */
    public int getNumAgents() {
        return numAgents;
    }

    /**
     *
     * parametrize total number of agents available at the start of the simulation
     */
    public void setNumAgents(int na) {
        numAgents = na;
    }

    /**
     *
     * @return the grid dimension
     */
    public int getGridSize() {
        return gridSize;
    }

    /**
     * parametrize the grid dimension
     */
    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
    }

    /**
     * @return the birthThreeshold parameters used to define at which level rabbit reproduces
     */
    public int getBirthThreshold() {
        return birthThreshold;
    }

    /**
     * set the birthThreeshold parameters used to define at which level rabbit reproduces
     */
    public void setBirthThreshold(int birthThreshold) {
        this.birthThreshold = birthThreshold;
    }

    /**
     * @return the total number of grass added at each step
     */
    public int getGrassGrowthRate() {
        return grassGrowthRate;
    }

    /**
     * set the total number of grass added at each step
     */
    public void setGrassGrowthRate(int grassGrowthRate) {
        this.grassGrowthRate = grassGrowthRate;
    }


    /**
     * @return the initial energy given to the rabbits
    */
    public int getInitialEnergy() {
        return initialEnergy;
    }


    /**
     * set the initial energy given to the rabbits
     */
    public void setInitialEnergy(int e) {
        initialEnergy = e;
    }
}
