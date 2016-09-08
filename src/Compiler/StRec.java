
package Compiler;
/**
 *
 * @author Robert Logan - c3165020
 */
public class StRec {
    private String name;
    private String type;
    private float value;// for literals
    
    public StRec(String n){//constructor for the Symbol Table Record
        name = n;
    }
    
    public void setName(String n){
        name = n;
    }
    
    public String getName(){
        return name;
    }
    
    public void setType(String t){
        type = t;
    }
    
    public String getType(){
        return type;
    }
    
    public void setVal(float f){
        value = f;
    }
    
    public float getVal(){
        return value;
    }
    
}
