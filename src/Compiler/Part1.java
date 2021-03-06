package Compiler;

import java.io.*;

/**
 * @author Robert Logan C3165020 on 28/07/16.
 * the Class is the main driver for the project and calls
 * the InputController object(Scanner) to begin scanning characters
 */
public class Part1 {
    private static PrintWriter l;
    private static StringBuffer buff;
    private File file;
    private static FileReader srcFile;
    private static TreeNode root;
    public static void main(String[] args){
        System.out.println("Scanning New File:");
        try{
            srcFile = new FileReader("src/Compiler/"+args[0]);
            InputController sc = new InputController(srcFile);
            OutputController oc = new OutputController(l, buff);
            //Parser p = new Parser(sc, oc);
            //root = p.recursiveDescent();
            
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
