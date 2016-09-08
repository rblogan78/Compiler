package Compiler;

import java.util.*;

/**
 * A class to run the recursive descent algorithm. Takes valid tokens and 
 * parses them into a syntax tree
 * @author Robert Logan - c3165020
 */
public class Parser {
    private InputController scanner;
    private OutputController out;
    private Token currentToken;
    private TreeNode root;
    private HashMap<String,StRec>[] sTable;
    private final int NUMARRAYS = 4;
    
    public Parser(InputController sc, OutputController oc){
        scanner = sc;//copy to local variables
        out = oc;
        currentToken = null;
        root = createNode(Node.NPROG); //create the top level node
        sTable = new HashMap[NUMARRAYS];
        for (int i=0;i<NUMARRAYS;i++){
            sTable[i] = new HashMap<String, StRec>();
        } 
        //sTable[0] = HashMap<String, StRec> sTable = new HashMap<>();
    }
    
    public TreeNode recursiveDescent(){
        currentToken = this.getNextToken();
        TreeNode root = program();
        return root;
    }
    
    private Token getNextToken(){
        do{
            currentToken = scanner.getToken();
        }while(currentToken==null);
 
        return currentToken;
    }
    
    private TreeNode createNode(Node n){
        TreeNode node = new TreeNode(n);
        return node;
    }
    
    private TreeNode createNullNode(){
        TreeNode node = new TreeNode();
        return node;
    }
    
    private StRec createSymbolRec(String tknStr, TokId type){
        StRec s = new StRec(tknStr);
        switch(type){
            case TIDNT:
                sTable[1].put(tknStr, s);
                break;
            case TILIT:
                sTable[0].put(tknStr, s);
                break;
            case TFLIT:
                sTable[0].put(tknStr, s);
                break;
            case TSTRG:
                sTable[2].put(tknStr, s);
                break;
        }
        return s;
    }
    
    private TreeNode program(){
        if (currentToken.value()!=TokId.TCD16){
            //error
        }else{
           currentToken= this.getNextToken();
           //token =currentToken.value();
        }
        if (currentToken.value()!=TokId.TIDNT){
            //error
        }else{
            //put the current token into the symbol tabl
            root.setName(createSymbolRec(currentToken.getStr(),currentToken.value()));
           currentToken= this.getNextToken();//and get next valid token
           //token = currentToken.value();
        }
        if(currentToken.value()==TokId.TCONS||currentToken.value()==TokId.TTYPS||currentToken.value()==TokId.TARRS){
            root.setLeft(globals());
        }else{
            //no globals so create a null Globals Node
            root.setLeft(createNullNode());
        }
        if(currentToken.value()==TokId.TFUNC){
            root.setMiddle(funcs());
        }else{
            root.setMiddle(createNullNode());
        }
        if(currentToken.value()!=TokId.TMAIN){
            //error
        }else{
            root.setMiddle(mbody());
        } 
        currentToken= this.getNextToken();
        //token =currentToken.value();
            
        if(currentToken.value()!=TokId.TEOF){
            //error
        }
        
        return root;
    }
    
    private TreeNode globals(){
        TreeNode n = createNode(Node.NGLOB);
        if(currentToken.value()==TokId.TCONS){
            n.setLeft(consts());//the consts node
        }else{
            n.setLeft(createNullNode());
        }
        if(currentToken.value()==TokId.TTYPS){
            n.setMiddle(types());//the types node
        }else{
            n.setMiddle(createNullNode());
        }
        if(currentToken.value()==TokId.TARRS){
            n.setRight(arrays());//the arrays node
        }else{
            n.setRight(createNullNode());
        }
        return n;
    }
    
    private TreeNode funcs(){
        TreeNode n = createNode(Node.NFUND);
        currentToken= this.getNextToken();
        if (currentToken.value()!=TokId.TFUNC){
            //error
        }else{
            
        }
        return n;
    }
    
    private TreeNode mbody(){
        TreeNode n = createNode(Node.NMAIN);
        
        return n;
    }
    
    private TreeNode consts(){
       currentToken= this.getNextToken();
        TreeNode n = createNode(Node.NILIST);
        if(currentToken.value()!=TokId.TIDNT){
            //error
        }else{
            n.setLeft(initList());
           currentToken= this.getNextToken();
            if(currentToken.value()!=TokId.TCOMA){
                //error
            }else{
                n.setRight(consts());//recursive call
            }
        }
        return n;
    }
    
    private TreeNode initList(){
        TreeNode n = createNode(Node.NINIT);
        n.setName(createSymbolRec(currentToken.getStr(), currentToken.value()));//put the identifier into the symbol table and the node
        currentToken= this.getNextToken();
        if(currentToken.value()!=TokId.TISKW){
            //error
        }else{
           currentToken= this.getNextToken();//should be an expression
            n.setLeft(expression());
        }
        return n;
    }
    
    private TreeNode types(){
        TreeNode n = createNode(Node.NUNDEF);//dummy
        return n;
    }
    
    private TreeNode arrays(){
        TreeNode n = createNode(Node.NUNDEF);//dummy
        return n;
    }
    
    private TreeNode expression(){
        TreeNode n = createNode(Node.NUNDEF);//dummy
        return n;
    }

    
    
}
