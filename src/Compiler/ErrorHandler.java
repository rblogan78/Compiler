/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Compiler;
import java.util.*;
/**
 *
 * @author c3165020
 */
public class ErrorHandler {
    LinkedList<Error> errlist = new LinkedList<>();
    private Token currentToken;
    InputController scanner;
    
    public ErrorHandler(InputController s){
        currentToken = null;
        scanner = s;
    }
    
    public LinkedList getList(){
        return errlist;
    }
    
    public void printErrors(){
        
    }
    
    private boolean addError(Error e){
        return errlist.add(e);
    }
    
    public Error createError(String n, String t, int l, int p){
        Error err = new Error(n,t,l,p);
        addError(err);
        
        return err;
    }
    
    public Token handleError(Error e, String routine, Token curr){
        switch(routine){
            case "stats":
                List<TokId> stats = Arrays.asList(TokId.TFORK, TokId.TREPT, 
                        TokId.TUNTL, TokId.TIFKW, TokId.TENDK, TokId.TIDNT, 
                        TokId.TINKW, TokId.TOUTP, TokId.TRETN);
                System.out.println(e.getErrorName()+" on line "+e.getErrorLine()
                        +" - "+curr.value());
                this.currentToken = scanner.getToken();
                System.out.println("Discarding tokens to find valid.");
                while(!stats.contains(currentToken.value())){
                    System.out.println(currentToken.shortString());
                    this.currentToken = scanner.getToken();
                }
                break;
            default:
                System.out.println(e.getErrorName()+" on line "+e.getErrorLine()
                        +" - "+curr.value());
                break;
        }
        System.out.println(currentToken.value()+" found. Continuing parse routine.");
        return currentToken;
    }
}
