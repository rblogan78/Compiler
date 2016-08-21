/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Compiler;

/**
 * A class to run the recursive descent algorithm. Takes valid tokens and 
 * parses them into a syntax tree
 * @author Robert Logan - c3165020
 */
public class Parser {
    
    public Parser(){
        TreeNode node = new TreeNode(Node.NPROG);//create the top level node
    }
    
    public void recursive(Token t){
        program(t.value());
    }
    
    public TreeNode createNode(Node n){
    TreeNode node = new TreeNode(n);
    return node;
    }
    
    public void program(TokId token){
        if (token!=TokId.TCD16){
            //error
        }else{
            //Token t = getNextToken();
            //token = t.value();
        }
        if (token!=TokId.TIDNT){
            //error
        }else{
            //t = getNextToken();
            //token = t.value();
        }
        TreeNode node = new TreeNode(Node.NPROG);
        node.setLeft(createNode(Node.NGLOB));
        node.setMiddle(createNode(Node.NFUNCS));
        node.setRight(createNode(Node.NMAIN));
        
        globals(node.getLeft());
        
        funcs(node.getMiddle());
        
        mbody(node.getRight());
        
        if(token!=TokId.TEOF){
            //error
        }
    }
    
    public void globals(TreeNode n){
        n.setLeft(createNode(Node.NILIST));//the consts node
        n.setMiddle(createNode(Node.NTYPEL));//the types node
        n.setRight(createNode(Node.NARRD));//the arrays node
        consts(n.getLeft());
        types(n.getMiddle());
        arrays(n.getRight());
    }
    public void funcs(TreeNode n){
        
    }
    
    public void mbody(TreeNode n){
        
    }
    

    
    
}
