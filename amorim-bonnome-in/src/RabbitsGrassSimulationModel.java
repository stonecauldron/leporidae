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
 *
 * @author
 */


public class RabbitsGrassSimulationModel extends SimModelImpl {




    // default values
    private static final int NUM_AGENTS = 5;
    private static final int GRID_SIZE = 20;
    private static final int BIRTH_THRESHOLD = 60;
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


    private OpenSequenceGraph totalRabbitHisto;
    private OpenSequenceGraph totalGrassHisto;


    class TotalRabbit implements DataSource, Sequence {

        public Object execute() {
            return new Double(getSValue());
        }

        public double getSValue() {
            return (double)agentList.size();
        }

    }

    class TotalGrass implements DataSource, Sequence {

        public Object execute() {
            return new Double(getSValue());
        }

        public double getSValue() {
            return (double)rbSpace.getTotalGrassInSpace();
        }

    }


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
        displaySurface = new DisplaySurface(this, "Rabbits Window 1");
        registerDisplaySurface("Rabbits Window 1", displaySurface);


        if(totalRabbitHisto != null){
            totalRabbitHisto.dispose();
        }
        totalRabbitHisto = new OpenSequenceGraph("Total of living rabbits in space",this);
        this.registerMediaProducer("Plot", totalRabbitHisto);

        if(totalGrassHisto!= null){
            totalGrassHisto.dispose();
        }
        totalGrassHisto = new OpenSequenceGraph("Total amount of grass in space",this);
        this.registerMediaProducer("Plot", totalGrassHisto);

    }


    public void begin() {
        buildModel();
        buildSchedule();
        buildDisplay();

        displaySurface.display();
        totalRabbitHisto.display();
        totalGrassHisto.display();
    }


    public void buildModel() {
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



    public void buildSchedule() {
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
                totalRabbitHisto.step();
            }
        });

        schedule.scheduleActionAtInterval(10, new BasicAction() {
            @Override
            public void execute() {
                totalGrassHisto.step();
            }
        });


    }



    public void buildDisplay() {
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

        totalRabbitHisto.addSequence("Total rabbit in space", new TotalRabbit());
        totalGrassHisto.addSequence("Total grass in space", new TotalGrass());

    }


    private void addNewAgent() {
        RabbitsGrassSimulationAgent a = new RabbitsGrassSimulationAgent(initialEnergy,grassEnergy);
        if(rbSpace.addAgent(a)) {
            agentList.add(a);
        }
    }


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


    private void removeDeadAgents() {
        for (int i = 0; i < agentList.size(); i++) {
            RabbitsGrassSimulationAgent agnt = agentList.get(i);
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
