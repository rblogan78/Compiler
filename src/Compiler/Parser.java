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
            System.out.println("Unexpected Token!");
        }else{
           currentToken= this.getNextToken();
           //token =currentToken.value();
        }
        if (currentToken.value()!=TokId.TIDNT){
            System.out.println("Unexpected Token!");
        }else{
            //put the current token into the symbol table
            root.setName(createSymbolRec(currentToken.getStr(),currentToken.value()));
           currentToken= this.getNextToken();//and get next valid token
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
            System.out.println("Unexpected Token!");
        }else{
            root.setRight(mbody());
        } 
        currentToken= this.getNextToken();
        //token =currentToken.value();
            
        if(currentToken.value()!=TokId.TEOF){
            System.out.println("Unexpected Token!");
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
            currentToken=this.getNextToken();
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
        TreeNode n = createNode(Node.NFUNCS);
        currentToken= this.getNextToken();
        if (currentToken.value()!=TokId.TIDNT){
            System.out.println("Unexpected Token!");
        }else{
            TreeNode left = createNode(Node.NFUND);
            n.setLeft(left);//create the NFUND Node (left)
            left.setName(createSymbolRec(currentToken.getStr(),currentToken.value()));//put the identifier (name of function) into the NFUND Node
            currentToken = this.getNextToken();//check for a left paren - else error
            if(currentToken.value()!=TokId.TLPAR){
                System.out.println("Unexpected Token!");
            }else{
                currentToken = this.getNextToken();
                switch(currentToken.value()){
                    case TIDNT:
                        left.setLeft(plist(false));//call plist()(as left child of NFUND)
                        break;
                    case TRPAR:
                        break;
                    case TCONS:
                        currentToken = this.getNextToken();
                        if(currentToken.value()!=TokId.TIDNT){
                            //erro
                        }else{
                            left.setLeft(plist(true));
                        }
                        break;
                }              
                if(currentToken.value()!=TokId.TRPAR){
                    System.out.println("Unexpected Token!");
                }else{
                    currentToken = this.getNextToken();
                    if(currentToken.value()!=TokId.TCOLN){
                        System.out.println("Unexpected Token!");
                    }else{
                        currentToken = this.getNextToken();//return type of the function
                        TokId val = currentToken.value();
                        if(val==TokId.TINTG||val==TokId.TREAL||val==TokId.TBOOL||val==TokId.TVOID){
                            //put the return type into the NFUND node
                        }else{
                            System.out.println("Unexpected Token!");
                        }
                        currentToken = this.getNextToken();
                        if(val!=TokId.TIDNT){
                            System.out.println("Unexpected Token!");
                        }else{
                            left.setMiddle(locals());//call locals()(as middle child of NFUND)
                            if(currentToken.value()!=TokId.TBEGN){//check for "begin" keyword - else error
                                System.out.println("Unexpected Token!");
                            }else{
                                left.setRight(stats());//call stats()(as right child of NFUND) -->this is a big one
                                currentToken = this.getNextToken();
                                if(currentToken.value()!=TokId.TENDK){
                                    System.out.println("Unexpected Token!");
                                }else{
                                    currentToken = this.getNextToken();
                                    if(currentToken.value()==TokId.TFUNC){
                                        n.setRight(funcs());
                                    }else if(currentToken.value()!=TokId.TMAIN){
                                        System.out.println("Unexpected Token!");
                                    }
                                }
                            }
                        }
                    }
                }
            }
            
            //check for "end" keyword - else error
            //if next token is "TFUNC" call funcs again as right child of n(NFUNCS)
        }
        return n;
    }
    
    private TreeNode plist(boolean flag){
        TreeNode n = createNode(Node.NPLIST);        
        TreeNode left = createNode(Node.NUNDEF);
        TreeNode temp = createNode(Node.NUNDEF);
        n.setLeft(left);
        left.setLeft(temp);
        temp.setName(createSymbolRec(currentToken.getStr(),currentToken.value()));//add the ID to the Node but we don't know what it is yet
        currentToken = this.getNextToken();
        if(currentToken.value()!=TokId.TCOLN){
            System.out.println("Unexpected Token!");
        }
        currentToken = this.getNextToken();
        TokId val = currentToken.value();
        if(val==TokId.TIDNT){//it's a <typeid>
            if(flag){
                left.setValue(Node.NARRC); 
            }else{
                left.setValue(Node.NARRP);
            }
            temp.setValue(Node.NARRD);
        }else if(val==TokId.TINTG||val==TokId.TREAL||val==TokId.TBOOL){//it's an <stype> 
            left.setValue(Node.NSIMP);
            temp.setValue(Node.NSDECL);
            //put the return type into the NSDECL Node as well
        }
        currentToken = this.getNextToken();
        if(currentToken.value()==TokId.TCOMA){
            currentToken = this.getNextToken();
            if(currentToken.value()==TokId.TIDNT){
                left.setRight(plist(false)); 
            }else if(currentToken.value()==TokId.TCONS){
                currentToken = this.getNextToken();
                if(currentToken.value()!=TokId.TIDNT){
                    System.out.println("Unexpected Token!");
                }else{
                    left.setLeft(plist(true));
                }
            }   
        }  
        return n;
    }
    
    private TreeNode locals(){
        TreeNode dlist = createNode(Node.NDLIST);
        TreeNode left = createNode(Node.NUNDEF);
        left.setName(createSymbolRec(currentToken.getStr(),currentToken.value()));
        dlist.setLeft(left);
        currentToken = this.getNextToken();
        if(currentToken.value()!=TokId.TCOLN){
            System.out.println("Unexpected Token!");
        }else{
            currentToken = this.getNextToken();
            TokId val = currentToken.value();
            if(val==TokId.TIDNT){//it's a <typeid>
                left.setValue(Node.NARRD);
                //put the return type into the NARRD Node
            }else if(val==TokId.TINTG||val==TokId.TREAL||val==TokId.TBOOL){//it's an <stype> 
                left.setValue(Node.NSDECL);
                //put the return type into the NSDECL Node as well
            }
        }
        currentToken = this.getNextToken();
        if(currentToken.value()==TokId.TCOMA){
            currentToken = this.getNextToken();
            if(currentToken.value()!=TokId.TIDNT){
                System.out.println("Unexpected Token!");
            }else{
                dlist.setRight(locals());
            }
        }
        return dlist;
    }
    
    private TreeNode stats(){
        TreeNode stats = createNode(Node.NSTATS);
        currentToken = this.getNextToken();
        TokId val = currentToken.value();
        switch(val){
            case TREPT:
                currentToken = this.getNextToken();
                stats.setLeft(repeat());
                break;
            case TIDNT:
                TreeNode n = createNode(Node.NUNDEF);//could be an <asgnstat> or a <callstat> 
                stats.setLeft(n);
                StRec s = createSymbolRec(currentToken.getStr(),currentToken.value());//the <id>
                n.setName(s);
                currentToken = this.getNextToken();//
                if(currentToken.value()==TokId.TLPAR){
                    n.setValue(Node.NCALL);
                    n.setLeft(call());
                }else{
                    n.setLeft(asgn());
                }
                break;
            case TINKW:
                currentToken = this.getNextToken();
                if(currentToken.value()!=TokId.TINPT){
                    System.out.println("Unexpected Token");
                }else{
                    currentToken = this.getNextToken();
                    stats.setLeft(vlist());
                }
                break;
            case TOUTP:
                currentToken = this.getNextToken();
                if(currentToken.value()!=TokId.TASGN){
                    System.out.println("Unexpected Token");
                }else{
                    currentToken = this.getNextToken();
                    if(currentToken.value()==TokId.TOUTL){
                        TreeNode outl = createNode(Node.NOUTL);
                        currentToken = this.getNextToken();
                        break;
                    }else{
                        
                    }
                }
                break;
            case TRETN:
                currentToken = this.getNextToken();
                if(currentToken.value()!=TokId.TSEMI){
                    stats.setLeft(expression());
                }
                break;
            case TFORK:
                break;
            case TIFKW:
                break;
            default:
                System.out.println("Unexpected Token!");
                break;
        }
        if(currentToken.value()==TokId.TSEMI || currentToken.value()==TokId.TENDK){
            stats.setRight(stats());//recursive call
        }
        return stats;
    }
    
    private TreeNode asgn(){
        TreeNode asgn = createNode(Node.NASGN);
        asgn.setLeft(var());
        if(currentToken.value()!=TokId.TASGN){
            System.out.println("Unexpected Token!");
        }else{
            asgn.setRight(bool());
        }
        return asgn;
    }
    
    private TreeNode vlist(){
        TreeNode vlist = createNode(Node.NVLIST);
        currentToken = this.getNextToken();
        vlist.setLeft(var());
        currentToken = this.getNextToken();
        if (currentToken.value()==TokId.TCOMA){
            currentToken = this.getNextToken();
            vlist.setRight(vlist());
        }
        
        return vlist;
    }
    
    private TreeNode var(){
        TreeNode var = createNode(Node.NUNDEF);
        if(currentToken.value()==TokId.TASGN){
            var.setValue(Node.NSIMV);
        }else if(currentToken.value()==TokId.TLBRK){
            var.setLeft(expression());
            if(currentToken.value()!=TokId.TRBRK){
                System.out.println("Unexpected Token!");
            }else{
                currentToken = this.getNextToken();
                if(currentToken.value()!=TokId.TDOTT){
                    var.setValue(Node.NAELT);
                }else{
                    currentToken = this.getNextToken();
                    if(currentToken.value()!=TokId.TIDNT){
                        System.out.println("Unexpected Token!");
                    }else{
                        var.setValue(Node.NARRV);
                    }
                }
            }
        }
        return var;
    }
    
    private TreeNode repeat(){
        TreeNode rpt = createNode(Node.NREPT);
        if(currentToken.value()==TokId.TRPAR){
            //do nothing
        }else{
            currentToken = this.getNextToken();
            rpt.setLeft(alist());
        }
        rpt.setMiddle(stats());
        if(currentToken.value()!=TokId.TUNTL){
            System.out.println("Unexpected token");
        }else{
            //currentToken = this.getNextToken();
            rpt.setRight(bool());
        }
        return rpt;
    }
    
    private TreeNode alist(){
        TreeNode alist = createNode(Node.NASGNS);
        alist.setLeft(asgn());
        return alist;
    }
    
    private TreeNode call(){
        TreeNode expl = createNode(Node.NEXPL);
        currentToken = this.getNextToken();
        if (currentToken.value()==TokId.TRPAR){
            //do nothing 
        }else{
            expl.setLeft(elist());
            if(currentToken.value()!=TokId.TRPAR){
                System.out.println("Unexpected Token!");
            }else{
                //do nothing else
            }
        }
        return expl;
    }
    
    private TreeNode elist(){
        TreeNode elist = createNode(Node.NUNDEF);//delayed naming of node.
        elist.setLeft(bool());
        if(currentToken.value()==TokId.TCOMA){
            elist.setLeft(elist());
        }
        return elist;
    }
    
    private TreeNode bool(){
        TreeNode bool = createNode(Node.NBOOL);
        currentToken = this.getNextToken();
        bool.setLeft(rel());
        currentToken = this.getNextToken();
        switch(currentToken.value()){
            case TANDK:
                bool.setValue(Node.NAND);
                currentToken = this.getNextToken();
                bool.setRight(rel());
                break;
            case TORKW:
                bool.setValue(Node.NOR);
                currentToken = this.getNextToken();
                bool.setRight(rel());
                break;
            case TXORK:
                bool.setValue(Node.NXOR);
                currentToken = this.getNextToken();
                bool.setRight(rel());
                break;
            default:
                break;
        }
        
        return bool;
    }
    
    private TreeNode rel(){
        boolean flag = false;
        TreeNode rel = createNode(Node.NUNDEF);
        if(currentToken.value()==TokId.TNOTK){
            currentToken = this.getNextToken();
            rel.setValue(Node.NNOT);
            flag = true;
        }
        rel.setLeft(expression());
        currentToken = this.getNextToken();
        switch(currentToken.value()){
            case TDEQL:
                if(flag){
                    TreeNode r = createNode(Node.NEQL);
                    rel.setLeft(r);
                }else{
                    rel.setValue(Node.NEQL);
                }
                break;
            case TNEQL:
                if(flag){
                    TreeNode r = createNode(Node.NNEQ);
                    rel.setLeft(r);
                }else{
                    rel.setValue(Node.NNEQ);
                }
                break;
            case TGRTR:
                if(flag){
                    TreeNode r = createNode(Node.NGRT);
                    rel.setLeft(r);
                }else{
                    rel.setValue(Node.NGRT);
                }
                break;
            case TLEQL:
                if(flag){
                    TreeNode r = createNode(Node.NLEQ);
                    rel.setLeft(r);
                }else{
                    rel.setValue(Node.NLEQ);
                }
                break;
            case TLESS:
                if(flag){
                    TreeNode r = createNode(Node.NLSS);
                    rel.setLeft(r);
                }else{
                    rel.setValue(Node.NLSS);
                }
                break;
            case TGREQ:
                if(flag){
                    TreeNode r = createNode(Node.NGEQ);
                    rel.setLeft(r);
                }else{
                    rel.setValue(Node.NGEQ);
                }
                break;
            default:
                return rel;
        }
        currentToken = this.getNextToken();
        rel.setRight(expression());
        
        return rel;
    }
    
    private TreeNode mbody(){
        TreeNode main = createNode(Node.NMAIN);
        //calls another thing
        return main;
    }
    
    private TreeNode consts(){
        currentToken = this.getNextToken();
        TreeNode ilist = createNode(Node.NILIST);
        if(currentToken.value()!=TokId.TIDNT){
            System.out.println("Unexpected Token!");
        }else{
            ilist.setLeft(initList());
            if(currentToken.value()==TokId.TCOMA){//the 
                ilist.setRight(consts());//recursive call
            }
        }
        return ilist;
    }
    
    private TreeNode initList(){
        TreeNode init = createNode(Node.NINIT);
        init.setName(createSymbolRec(currentToken.getStr(), currentToken.value()));//put the identifier into the symbol table and the node
        currentToken = this.getNextToken();
        if(currentToken.value()!=TokId.TISKW){
            System.out.println("Unexpected Token!");
        }else{
            currentToken = this.getNextToken();//should be an expression
            init.setLeft(expression());
        }
        return init;
    }
    
    private TreeNode types(){
        TreeNode tlist = createNode(Node.NTYPEL);
        if(currentToken.value()!=TokId.TIDNT){
            System.out.println("Unexpected Token!");
        }else{
            tlist.setLeft(typeList());
            currentToken = this.getNextToken();
            if(currentToken.value()==TokId.TIDNT){
                tlist.setRight(types());//recursive call
            }
        }
        return tlist;
    }
    
    private TreeNode arrays(){
        TreeNode alist = createNode(Node.NALIST); 
        TreeNode left = arrDecl();
        alist.setLeft(left);
        currentToken = this.getNextToken();
        if(currentToken.value()==TokId.TCOMA){
            alist.setRight(arrays());
        }else if(currentToken.value()!=TokId.TFUNC){
            System.out.println("Unexpected Token!");
        }
        return alist;
    }
    
    private TreeNode arrDecl(){
        TreeNode arrd = createNode(Node.NARRD);
        currentToken = this.getNextToken();
        if(currentToken.value()!=TokId.TIDNT){
            System.out.println("Unexpected Token!");
        }else{
            arrd.setName(createSymbolRec(currentToken.getStr(), currentToken.value()));//put the identifier into the symbol table and the node
            currentToken = this.getNextToken();
            if(currentToken.value()!=TokId.TCOLN){
                System.out.println("Unexpected Token!");
            }else{
                currentToken = this.getNextToken();//should be a typeid
                //TODO something with the typeid               
            }
        }
        return arrd;
    }
    
    private TreeNode expression(){
        TreeNode term = term(); 
        currentToken = this.getNextToken(); 
        TreeNode expr = createNode(Node.NUNDEF);
        switch(currentToken.value()){
            case TCOMA:
                return term;
            case TADDT:
                expr.setValue(Node.NADD);
                break;
            case TSUBT:
                expr.setValue(Node.NSUB);
                break;
            case TMULT:
                expr.setValue(Node.NMUL);
                break;
            case TDIVT:
                expr.setValue(Node.NDIV);
                break;
            case TPERC:
                expr.setValue(Node.NMOD);
                break;
            case TCART:
                expr.setValue(Node.NPOW);
                break;
            default:
                return term;
        }
        expr.setLeft(term);
        expr.setRight(term());
        return expr;
    }
   
    
    private TreeNode term(){
        return factor();
    }

    private TreeNode factor(){
        return exponent();
    }
    
    private TreeNode exponent(){
        TreeNode expon = createNode(Node.NUNDEF);
        TokId val = currentToken.value();
        switch(val){
            case TIDNT:
                expon.setName(createSymbolRec(currentToken.getStr(), currentToken.value()));
                currentToken = this.getNextToken();
                if(currentToken.value()==TokId.TLPAR){
                    expon.setValue(Node.NFCALL);
                    expon.setLeft(call());
                }else{
                    expon = var();
                }
                break;
            case TILIT:
                expon.setValue(Node.NILIT);
                expon.setName(createSymbolRec(currentToken.getStr(), currentToken.value()));
                break;
            case TFLIT:
                expon.setValue(Node.NFLIT);
                expon.setName(createSymbolRec(currentToken.getStr(), currentToken.value()));
                break;
            case TTRUE:
                expon.setValue(Node.NTRUE);
                break;
            case TFALS:
                expon.setValue(Node.NFALS);
                break;
            case TLPAR:
                //NBOOL
                break;
            default:
                System.out.println("Unexpected Token!");
                break;
        }   
        return expon;
    }   
    
    private TreeNode typeList(){
        TreeNode n = createNode(Node.NUNDEF);
        n.setName(createSymbolRec(currentToken.getStr(), currentToken.value()));//put the identifier into the symbol table and the node
        currentToken=this.getNextToken();
        if(currentToken.value()!=TokId.TISKW){
            System.out.println("Unexpected Token!");
        }else{
            currentToken = this.getNextToken();
            if(currentToken.value()==TokId.TARRY){
                n.setValue(Node.NATYPE);
                currentToken = this.getNextToken();
                if(currentToken.value()!=TokId.TLBRK){
                    System.out.println("Unexpected Token!");
                }else{
                    n.setLeft(expression());
                    currentToken = this.getNextToken();
                    if(currentToken.value()!=TokId.TRBRK){
                        System.out.println("Unexpected Token!");
                    }else{
                        currentToken = this.getNextToken();
                        if(currentToken.value()!=TokId.TOFKW){
                            System.out.println("Unexpected Token!");
                        }else{
                            currentToken = this.getNextToken();
                            if(currentToken.value()!=TokId.TIDNT){
                                System.out.println("Unexpected Token!");
                            }else{
                                //TODO do something with the STRUCTID
                            }
                        }
                    }
                }
            }else if(currentToken.value()==TokId.TIDNT){
                n.setValue(Node.NRTYPE);
                TreeNode left = fieldList();
                n.setLeft(left);     
            }
        }
        return n;
    }
    
    private TreeNode fieldList(){
        TreeNode flist = createNode(Node.NFLIST);
        flist.setLeft(stdDecl());
        currentToken = this.getNextToken();
        if(currentToken.value()==TokId.TCOMA){
            currentToken = this.getNextToken();
            flist.setRight(fieldList());//recursive call
        }
        if(currentToken.value()!=TokId.TENDK){
            System.out.println("Unexpected Token!");
        }
        return flist;
    }
    
    private TreeNode stdDecl(){
        TreeNode s = createNode(Node.NSDECL);
        s.setName(createSymbolRec(currentToken.getStr(), currentToken.value()));//put the identifier into the symbol table and the node
        currentToken = this.getNextToken();
        if(currentToken.value()!=TokId.TCOLN){
            System.out.println("Unexpected Token!");
        }else{
            currentToken = this.getNextToken();
            //s.setType(); set the symbol table record for the type
            //currentToken = this.getNextToken();
        }
        return s;
    }
}
