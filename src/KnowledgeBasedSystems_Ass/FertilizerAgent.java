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


import java.util.Arrays;
import java.util.HashMap;

public class FertilizerAgent {

    //The agents current conception of the world state
    State state;

    //a description of how the next state depends on current state and agentAction
    Model model;

    //The most recent agentAction, initially none
    AgentAction agentAction;

    //The goal state
    Model goal;

    public FertilizerAgent() {
        //Initialize the model
        model = new Model(Environment.ROWS, Environment.COLS);
    }

    public void setGoalState(Model goalState) {
        this.goal = goalState;
    }

    public void setInitialState(State initialState) {
        this.state = initialState;
    }

    public AgentAction execute(HashMap<String, Object> percepts) {
        //Update state
        state = updateState(state, agentAction, percepts, model);

        //Best agentAction
        agentAction = bestAction(goal, state);

        //AgentAction
        return agentAction;
    }

    private State updateState(State state, AgentAction agentAction, HashMap<String, Object> percepts, Model model) {

        //Get the current state from the percepts
        State newState = new State();

        newState.fertilized = (boolean) percepts.get("fertilized");
        newState.agentInPosition = true;
        newState.row = (int) percepts.get("row");
        newState.col = (int) percepts.get("col");

        //Update the model
        this.model.states[state.row][state.col].agentInPosition = false;
        this.model.states[newState.row][newState.col] = newState;

        return newState;
    }

    /**
     * Get the best agentAction that would help in achieving the goal
     *
     * @param goal
     * @param state
     * @return
     */
    private AgentAction bestAction(Model goal, State state) {
        AgentAction agentAction = null;

        //Check if goal state reached
        if(isGoalStatedReached(model, goal))
            return  null;

        //Calculate the agentAction scores and compare
        int fertilizeActionScore = actionScore(goal, predictState(model, AgentAction.FERTILIZE_CROP));
        int upActionScore = actionScore(goal, predictState(model, AgentAction.UP));
        int downActionScore = actionScore(goal, predictState(model, AgentAction.DOWN));
        int rightActionScore = actionScore(goal, predictState(model, AgentAction.RIGHT));
        int leftActionScore = actionScore(goal, predictState(model, AgentAction.LEFT));

        //Get the agentAction with the highest score
        int maxActionScore = getMaxValue(new int[]{fertilizeActionScore, upActionScore, downActionScore, rightActionScore,
                leftActionScore});

        if (maxActionScore > 0) {
            if (fertilizeActionScore == maxActionScore)
                agentAction = AgentAction.FERTILIZE_CROP;
            else if (rightActionScore == maxActionScore)
                agentAction = AgentAction.RIGHT;
            else if (leftActionScore == maxActionScore)
                agentAction = AgentAction.LEFT;
            else if (downActionScore == maxActionScore)
                agentAction = AgentAction.DOWN;
            else if (upActionScore == maxActionScore)
                agentAction = AgentAction.UP;
        }

        return agentAction;
    }

    /**
     * How the world would be after applying some agentAction
     *
     * @param model
     * @param agentAction
     * @return
     */
    private Model predictState(Model model, AgentAction agentAction) {
        Model predictedModel = model.getCopy();
        State currentState = this.state;
        State predictedNewState = null;

        try {
            if (agentAction == AgentAction.FERTILIZE_CROP) {
                //Fertilize
                if (predictedModel.states[currentState.row][currentState.col].fertilized)
                    predictedModel = null;
                else
                    predictedModel.states[currentState.row][currentState.col].fertilized = true;
            } else if (agentAction == AgentAction.RIGHT) {
                //Right
                predictedModel.states[currentState.row][currentState.col].agentInPosition = false;
                predictedModel.states[currentState.row][currentState.col + 1].agentInPosition = true;
                predictedNewState = predictedModel.states[currentState.row][currentState.col + 1];
            } else if (agentAction == AgentAction.LEFT) {
                //Left
                predictedModel.states[currentState.row][currentState.col].agentInPosition = false;
                predictedModel.states[currentState.row][currentState.col - 1].agentInPosition = true;
                predictedNewState = predictedModel.states[currentState.row][currentState.col - 1];
            } else if (agentAction == AgentAction.DOWN) {
                //Down
                predictedModel.states[currentState.row][currentState.col].agentInPosition = false;
                predictedModel.states[currentState.row + 1][currentState.col].agentInPosition = true;
                predictedNewState = predictedModel.states[currentState.row + 1][currentState.col];
            } else if (agentAction == AgentAction.UP) {
                //Up
                predictedModel.states[currentState.row][currentState.col].agentInPosition = false;
                predictedModel.states[currentState.row - 1][currentState.col].agentInPosition = true;
                predictedNewState = predictedModel.states[currentState.row - 1][currentState.col];
            }

            //Return null if predicted state is already fertilized
            if(predictedNewState != null && predictedNewState.fertilized)
                predictedModel = null;

        } catch (Exception e) {
            predictedModel = null;
        }

        return predictedModel;
    }

    /**
     * Helper method to get the agentAction score which specifies how close the predicated state is to the goal state
     *
     * @param goal
     * @param predictedModel
     * @return
     */
    private int actionScore(Model goal, Model predictedModel) {
        if (predictedModel == null)
            return 0;

        int actionScore = 0;

        for (int i = 0; i < Environment.ROWS; ++i) {
            for (int j = 0; j < Environment.COLS; ++j) {
                State goalState = goal.states[i][j];
                State predictedState = predictedModel.states[i][j];

                if (goalState.agentInPosition && predictedState.agentInPosition)
                    ++actionScore;

                if (goalState.fertilized && predictedState.fertilized)
                    ++actionScore;
            }
        }

        return actionScore;
    }

    private int getMaxValue(int[] values) {
        Arrays.sort(values);
        return values[values.length - 1];
    }

    public State getState() {
        return this.state;
    }

    /**
     * Check if goal state reached
     */
    private boolean isGoalStatedReached(Model model, Model goal) {
        boolean goalStateReached = true;

        for (int i = 0; i < Environment.ROWS; ++i) {
            for (int j = 0; j < Environment.COLS; ++j) {
                State goalState = goal.states[i][j];
                State modelState = model.states[i][j];

                boolean statesMatch = (goalState.fertilized == modelState.fertilized);

                if (!statesMatch) {
                    goalStateReached = false;
                    break;
                }
            }

            if (!goalStateReached)
                break;
        }

        return goalStateReached;
    }
}

