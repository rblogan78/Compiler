/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Compiler;

/**
 *
 * @author c3165
 */
public class State {
    private StateEnum currState = null;
    public State(){
        currState = StateEnum.START;
    }
    
    public StateEnum getState(){
        return currState;
    }
    
    public void setState(StateEnum st){
        currState = st;
    }
}
