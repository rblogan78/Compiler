import java.io.*;
import java.util.*;

/**
 * Created by Rob on 28/07/16.
 */

public class InputController {
    private FileReader srcFile = null;
    private boolean eofFlag = false;
    private int line = 0;
    private int pos = 0;
    private TokMap tokenId = new TokMap();
    
    public InputController(FileReader f){
        srcFile = f;
    }
    
    public Token getToken(){
        try{
            BufferedReader reader = new BufferedReader(srcFile);
            String s = "";
            int val = 0;
            line = 1;
            ArrayList tok = new ArrayList();
            val=reader.read();
            pos = 1;
            while(val!=13 && val!=32 && val!=-1){
                
                tok.add((char)val);
                s = tokenId.getTokId(")");
                //if(tokenId.getTokId(tok.get(0).equals(""))){
                
                val = reader.read();    
            }
                //call the token generator
            reader.close();
        } catch(FileNotFoundException e){
            System.out.println(e);
        } catch(IOException i){
            System.out.println(i);
        }
        Token t = new Token("TMULT",1,1,null);
        return t;
    }
    
    
    public void seteof(boolean state){
        eofFlag=state;
    }
    
    public boolean geteof(){
        return eofFlag;
    }
}
