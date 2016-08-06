import java.util.*;
import java.io.*;

/**
 * Created by Rob on 28/07/16.
 */
public class Part1 {
    //private String s;

    public static void main(String[] args){
        
        try{
            FileReader f = new FileReader("testinput.txt");
            InputController sc = new InputController(f);
            
            Token t = sc.getToken();
            
        } catch(FileNotFoundException e){
            System.out.println(e);
        }
    }
}
