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


public class State {
    boolean agentInPosition;
    int row;
    int col;
    boolean fertilized = false;

    @Override
    public String toString() {
        return "Agent in Position: " + agentInPosition + " Row: " + row + ", Col: " + col + " Fertilized: " + fertilized;
    }
}
