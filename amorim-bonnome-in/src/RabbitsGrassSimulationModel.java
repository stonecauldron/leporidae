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

/**
 * Class that implements the simulation model for the rabbits grass
 * simulation.  This is the first class which needs to be setup in
 * order to run Repast simulation. It manages the entire RePast
 * environment and the simulation.
 *
 * @author
 */


public class RabbitsGrassSimulationModel extends SimModelImpl {
    // default values
    private static final int NUM_AGENTS = 100;
    private static final int GRID_SIZE = 20;
    private static final int BIRTH_THRESHOLD = 50;
    private static final int GRASS_GROWTH = 1;
    private static final int GRASS_ENERGY = 10;
    private static final int INITIAL_ENERGY = 50;

    private int numAgents = NUM_AGENTS;
    private int gridSize = GRID_SIZE;
    private int birthThreshold = BIRTH_THRESHOLD;
    private int grassGrowthRate = GRASS_GROWTH;
    private int grassEnergy = GRASS_ENERGY;
    private int initialEnergy = INITIAL_ENERGY;

    private Schedule schedule;

    private RabbitsGrassSimulationSpace rbSpace;

    private ArrayList agentList;

    private DisplaySurface displaySurface;

    public static void main(String[] args) {
        SimInit init = new SimInit();
        RabbitsGrassSimulationModel model = new RabbitsGrassSimulationModel();
        init.loadModel(model, "", false);
    }

    public void setup() {
        System.out.println("Running setup");
        rbSpace = null;
        agentList = new ArrayList();
        schedule = new Schedule(1);

        if (displaySurface != null) {
            displaySurface.dispose();
        }
        displaySurface = null;

        displaySurface = new DisplaySurface(this, "Rabbits Window 1");
        registerDisplaySurface("Rabbits Window 1", displaySurface);
    }

    public void begin() {
        buildModel();
        buildSchedule();
        buildDisplay();

        displaySurface.display();
    }

    public void buildModel() {
        System.out.println("Running buildmodel");
        rbSpace = new RabbitsGrassSimulationSpace(gridSize);
        for (int i = 0; i < numAgents; i++) {
            addNewAgent();
        }
        for (int i = 0; i < agentList.size(); i++) {
            RabbitsGrassSimulationAgent rba = (RabbitsGrassSimulationAgent) agentList.get(i);
            rba.report();
        }
    }

    public void buildSchedule() {
        System.out.println("Running buildSchedule");

        class RabbitsGrassStep extends BasicAction {
            public void execute() {
                SimUtilities.shuffle(agentList);
                for (int i = 0; i < agentList.size(); i++) {
                    RabbitsGrassSimulationAgent agnt = (RabbitsGrassSimulationAgent) agentList.get(i);
                    agnt.step();
                }
                removeDeadAgents();
                rbSpace.growGrass(grassGrowthRate, grassEnergy);
                displaySurface.updateDisplay();
            }
        }
        schedule.scheduleActionBeginning(0, new RabbitsGrassStep());
    }

    public void buildDisplay() {
        System.out.println("Running buildDisplay");

        ColorMap map = new ColorMap();

        map.mapColor(0, Color.black);
        map.mapColor(1, new Color(0, (int) (127), 0));

        Value2DDisplay displayGrass = new Value2DDisplay(rbSpace.getGrassSpace(), map);
        displaySurface.addDisplayable(displayGrass, "Grass");

        Object2DDisplay displayAgents = new Object2DDisplay(rbSpace.getAgentSpace());
        displayAgents.setObjectList(agentList);

        displaySurface.addDisplayable(displayAgents, "Agents");
    }

    private void addNewAgent() {
        RabbitsGrassSimulationAgent a = new RabbitsGrassSimulationAgent(initialEnergy);
        agentList.add(a);
        rbSpace.addAgent(a);
    }

    private void removeDeadAgents() {
        for (int i = 0; i < agentList.size(); i++) {
            RabbitsGrassSimulationAgent agnt = (RabbitsGrassSimulationAgent) agentList.get(i);
            if (agnt.getEnergy() < 1) {
                rbSpace.removeAgentAt(agnt.getX(), agnt.getY());
                agentList.remove(i);
            }
        }
    }

    public String[] getInitParam() {
        String[] initParams = {"NumAgents", "GridSize", "BirthThreshold", "GrassGrowthRate"};
        return initParams;
    }

    public String getName() {
        return "Rabbits";
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public int getNumAgents() {
        return numAgents;
    }

    public void setNumAgents(int na) {
        numAgents = na;
    }

    public int getGridSize() {
        return gridSize;
    }

    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
    }

    public int getBirthThreshold() {
        return birthThreshold;
    }

    public void setBirthThreshold(int birthThreshold) {
        this.birthThreshold = birthThreshold;
    }

    public int getGrassGrowthRate() {
        return grassGrowthRate;
    }

    public void setGrassGrowthRate(int grassGrowthRate) {
        this.grassGrowthRate = grassGrowthRate;
    }

    public int getInitialEnergy() {
        return initialEnergy;
    }

    public void setInitialEnergy(int e) {
        initialEnergy = e;
    }
}
