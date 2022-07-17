/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package KnowledgeBasedSystems_Ass;

/**
 *
 * @author rotohh
 */
import java.util.HashMap;

public class Environment {

    //Size of farm
    public static int ROWS = 4;
    public static int COLS = 5;

    //Farm world
    FertilizerAgent fertilizingAgent;
    Model model;
    Model goal;

    public void init() {

        //Init the states
        model = getInitialState();
        goal = getGoalState();


        //Init the agent
        fertilizingAgent = new FertilizerAgent();
        fertilizingAgent.setGoalState(goal);
        fertilizingAgent.setInitialState(model.states[0][0]);
    }

    private static Model getGoalState() {
        Model model = new Model(ROWS, COLS);

        //All crops are fertilized
        for (int i = 0; i < ROWS; ++i) {
            for (int j = 0; j < COLS; ++j) {
                State state = model.states[i][j];
                state.agentInPosition = false;
                state.fertilized = true;
            }
        }

        return model;
    }

    private static Model getInitialState() {
        Model model = new Model(ROWS, COLS);

        //None of the crops are fertilized
        for (int i = 0; i < ROWS; ++i) {
            for (int j = 0; j < COLS; ++j) {
                State state = model.states[i][j];
                state.agentInPosition = false;
                state.fertilized = false;
            }
        }

        //Agent is in the first position
        model.states[0][0].agentInPosition = true;
        return model;
    }

    /**
     * Start simulation
     */
    public void simulate() {
        AgentAction agentAction = null;
        State currentState = model.states[0][0]; //Start from the first position. Row: 0, Col: 0
        do {
            System.out.print(model.toString()); //Print out the current state

            //Generate the percepts from the current state
            HashMap<String, Object> percepts = new HashMap<>();
            percepts.put("fertilized", currentState.fertilized);
            percepts.put("row", currentState.row);
            percepts.put("col", currentState.col);

            //Send the percepts to agent
            agentAction = fertilizingAgent.execute(percepts);

            //Apply agentAction to environment
            if (agentAction != null) {
                currentState = applyAction(agentAction);
            }
        }
        while (agentAction != null);
        System.out.println("Goal state  reached");
    }

    /**
     * Apply agentAction from agent and return new agent state
     */
    public State applyAction(AgentAction agentAction) {
        System.out.println("AgentAction: " + agentAction.name());
        State currentState = fertilizingAgent.getState();
        State newAgentState = null;

        if (agentAction == AgentAction.FERTILIZE_CROP) {
            //Fertilize
            model.states[currentState.row][currentState.col].fertilized = true;
            newAgentState = model.states[currentState.row][currentState.col];
        } else if (agentAction == AgentAction.RIGHT) {
            //Right
            model.states[currentState.row][currentState.col].agentInPosition = false;
            model.states[currentState.row][currentState.col + 1].agentInPosition = true;
            newAgentState = model.states[currentState.row][currentState.col + 1];
        } else if (agentAction == AgentAction.LEFT) {
            //Left
            model.states[currentState.row][currentState.col].agentInPosition = false;
            model.states[currentState.row][currentState.col - 1].agentInPosition = true;
            newAgentState = model.states[currentState.row][currentState.col - 1];
        } else if (agentAction == AgentAction.DOWN) {
            //Down
            model.states[currentState.row][currentState.col].agentInPosition = false;
            model.states[currentState.row + 1][currentState.col].agentInPosition = true;
            newAgentState = model.states[currentState.row + 1][currentState.col];
        } else if (agentAction == AgentAction.UP) {
            //Up
            model.states[currentState.row][currentState.col].agentInPosition = false;
            model.states[currentState.row - 1][currentState.col].agentInPosition = true;
            newAgentState = model.states[currentState.row - 1][currentState.col];
        }

        return newAgentState;
    }
}
