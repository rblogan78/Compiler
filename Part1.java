package Compiler;

import java.io.*;

/**
 * @author Robert Logan C3165020 on 28/07/16.
 * the Class is the main driver for the project and calls
 * the InputController object(Scanner) to begin scanning characters
 */
public class Part1 {
    
    public static void main(String[] args){
        System.out.println("Scanning New File:");
        try{
            FileReader f = new FileReader("src/Compiler/"+args[0]);
            InputController sc = new InputController(f);
            
            while(!sc.getEof()){
                Token t = sc.getToken();
                if(t!=null){
                    sc.printToken(t);
                }
                if(sc.getEof()){
                    t = sc.getToken();
                }
            }
            sc.closeWriter();
        } catch(FileNotFoundException e){
            System.out.println(e);
        }
    }
}
