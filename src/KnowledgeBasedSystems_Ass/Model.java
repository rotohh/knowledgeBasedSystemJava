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


public class Model {

    State[][] states;
    int rows;
    int cols;

    public Model(int rows, int cols){
        this.rows = rows;
        this.cols = cols;
        states = new State[rows][cols];

        //Initialize all states
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                states[i][j] = new State();
                states[i][j].row = i;
                states[i][j].col = j;
            }
        }
    }

    /**
     * Return a copy of this model
     */
    public Model getCopy(){
        Model model = new Model(rows, cols);

        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                State state = states[i][j];

                //Copy properties
                model.states[i][j].agentInPosition = state.agentInPosition;
                model.states[i][j].fertilized = state.fertilized;
                model.states[i][j].row = state.row;
                model.states[i][j].col = state.col;
            }
        }

        return model;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("\n");

        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                State state = states[i][j];

                //Format output
                String output = "";
                output += state.agentInPosition? "A" : "";
                output += state.fertilized ? "F" : "";
                output = state.agentInPosition ^ state.fertilized ? output + " ": output;
                output = output.equals("") ? output + "  " : output;
                output = "| " + output + " |";
                builder.append(output);
                builder.append("\t");
            }

            builder.append("\n");
        }

        builder.append("\n");
        return builder.toString();
    }
}
