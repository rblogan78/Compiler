package Compiler;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author c3165020
 */
public class Error{
    private final String errName;
    private final String errType;
    private final int errLine;
    private final int errPos;
    
    public Error(String n, String t, int l, int p){
        this.errName = n;
        this.errType = t;
        this.errLine = l;
        this.errPos = p;
    }
    
    public String getErrorName(){ return errName; }
    
    public String getErrorType(){ return errType; }
    
    public int getErrorLine(){ return errLine; }
    
    public int getErrorPos(){ return errPos; }
    
}
