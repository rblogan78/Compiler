package Compiler;

/**
 *
 * @author Robert Logan c3165020
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
