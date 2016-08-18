package Compiler;

import java.io.*;

/**
 * Created by Rob on 28/07/16.
 */
public class Part1 {
    
    public static void main(String[] args){
        System.out.println("Scanning New File:");
        try{
            FileReader f = new FileReader("src/Compiler/cd16src1.txt");
            InputController sc = new InputController(f);
            File file = new File("P1Output.txt");
            PrintWriter l = new PrintWriter(file);
            StringBuffer buff = new StringBuffer();
            OutputController out = new OutputController(l,buff);
            while(!sc.getEof()){
                Token t = sc.getToken();
                if(t!=null){
                    sc.printToken(t);
                }
                if(sc.getEof()){
                    t = sc.getToken();
                }
            }
        } catch(FileNotFoundException e){
            System.out.println(e);
        }
    }
}
