package Compiler;

import java.io.*;
import java.util.*;

/**
 * Created by Rob on 28/07/16.
 */

public class InputController {
    private int counter = 0;
    private FileReader srcFile = null;
    private BufferedReader reader = null;
    private boolean eofFlag = false;
    private boolean unflag = false;
    private boolean d  = false;
    private int currLine = 1;
    private int startLine = 1;
    private int currPos = 0;
    private int startPos = 0;
    private int val = 0;
    private HashMap<String, TokId> tokenMap = new HashMap<>();
    private ArrayList<Character> tok = new ArrayList<Character>();
    private State state = new State();
    private static final Set<Integer> delims = new HashSet<Integer>(Arrays.asList(40,41,42,43,44,45,46,47,37,58,59,91,93,94));
    private File file;
    private PrintWriter l;
    private StringBuffer buff;
    private OutputController out;
    
        public InputController(FileReader f){
        file = new File("P1Output.txt");
        try{
            l = new PrintWriter(file);
        }catch(FileNotFoundException e){
            System.out.println(e);
        }
        buff = new StringBuffer();
        out = new OutputController(l,buff);
        
        srcFile = f;
        reader = new BufferedReader(srcFile);
        tokenMap.put("cd16", TokId.TCD16);
        tokenMap.put("constants", TokId.TCONS);
        tokenMap.put("types", TokId.TTYPS);
        tokenMap.put("is", TokId.TISKW);
        tokenMap.put("arrays", TokId.TARRS);
        tokenMap.put("main", TokId.TMAIN);
        tokenMap.put("begin", TokId.TBEGN);
        tokenMap.put("end", TokId.TENDK);
        tokenMap.put("array", TokId.TARRY);
        tokenMap.put("of", TokId.TOFKW);
        tokenMap.put("func", TokId.TFUNC);
        tokenMap.put("void", TokId.TVOID);
        tokenMap.put("const", TokId.TCNST);
        tokenMap.put("integer", TokId.TINTG);
        tokenMap.put("real", TokId.TREAL);
        tokenMap.put("boolean", TokId.TBOOL);
        tokenMap.put("for", TokId.TFORK);
        tokenMap.put("repeat", TokId.TREPT);
        tokenMap.put("until", TokId.TUNTL);
        tokenMap.put("if", TokId.TIFKW);
        tokenMap.put("else", TokId.TELSE);
        tokenMap.put("in", TokId.TINKW);
        tokenMap.put("out", TokId.TOUTP);
        tokenMap.put("line", TokId.TOUTL);
        tokenMap.put("return", TokId.TRETN);
        tokenMap.put("not", TokId.TNOTK);
        tokenMap.put("and", TokId.TANDK);
        tokenMap.put("or", TokId.TORKW);
        tokenMap.put("xor", TokId.TXORK);
        tokenMap.put("true", TokId.TTRUE);
        tokenMap.put("false", TokId.TFALS);
        tokenMap.put("[", TokId.TLBRK);
        tokenMap.put("]", TokId.TRBRK);
        tokenMap.put("(", TokId.TLPAR);
        tokenMap.put(")", TokId.TRPAR);
        tokenMap.put(";", TokId.TSEMI);
        tokenMap.put(",", TokId.TCOMA);
        tokenMap.put(":", TokId.TCOLN);
        tokenMap.put(".", TokId.TDOTT);
        tokenMap.put("<<", TokId.TASGN);
        tokenMap.put(">>", TokId.TINPT);
        tokenMap.put("==", TokId.TDEQL);
        tokenMap.put(">", TokId.TGRTR);
        tokenMap.put("<=", TokId.TLEQL);
        tokenMap.put("<", TokId.TLESS);
        tokenMap.put(">=", TokId.TGREQ);
        tokenMap.put("+", TokId.TADDT);
        tokenMap.put("-", TokId.TSUBT);
        tokenMap.put("*", TokId.TMULT);
        tokenMap.put("/", TokId.TDIVT);
        tokenMap.put("%", TokId.TPERC);
        tokenMap.put("^", TokId.TCART);
        tokenMap.put("!=", TokId.TNEQL);
    }
    public void closeWriter(){
        l.close();
    }
        
    public Token getToken(){
        Token t = null;
        String s = "";

        state.setState(StateEnum.START);
        if(val!=0){
            if(Character.isLetter(val)){
                state.setState(StateEnum.IDENT);
            }else{
                state.setState(StateEnum.DELIMITER);
            }
            s = this.stateMachine(val);
            if(!d){
                val=0;
            }
        }else{
            s = chkNextChar(this.getVal());
        }
        if(s.equals("")){
            s = chkNextChar(this.getVal());
            if(val==10){
            currLine++;
            currPos = 0;
            startPos = 0;
            }
        }
        
        switch(state.getState()){
            case INTEGER:
                t = new Token(TokId.TILIT,startLine,startPos,s);
                break;
            case REAL:
                t = new Token(TokId.TFLIT,startLine,startPos,s);
                break;
            case STRING:
                t = new Token(TokId.TSTRG,startLine,startPos,s);
                break;
            case IDENT:
                t = new Token(TokId.TIDNT,startLine,startPos,s);
                break;
            case DELIMITER:
                TokId tokenId = this.getTokId(s);
                t = new Token(tokenId,startLine,startPos, null);
                break;
            case COMMENT:
                //do not create a token
                break;
            case EOL:
                break;
            case UNDEF:
                t = new Token(TokId.TUNDF,startLine,startPos, s);
                out.setError("^illegal symbol "+s);
                break;
            case EOF:
                t = new Token(TokId.TEOF,startLine,startPos, null);
                break;
        }
        d = false;
        return t;
    }
    public void printToken(Token prt){
        String s = prt.shortString();
        int length = s.length();
        counter = counter+length;
        if(prt.value()==TokId.TUNDF){
            System.out.println();
            System.out.print(s);
            counter=0;
        }else if(counter>60){
            System.out.println(s);
            counter = 0;
        }else{
            System.out.print(s);
        }
    }
            
    public String chkNextChar(int value){
        while(Character.isWhitespace(value)){
            value = this.getVal();//find the next non whitespace
        }
        String str = "";
        startPos=currPos;
        startLine=currLine;
        if (Character.isDigit(value)){//integer
            state.setState(StateEnum.INTEGER);
            str = this.stateMachine(value);
        }else if (Character.isLetter(value)){//identifier
            state.setState(StateEnum.IDENT);
            str = this.stateMachine(value);
        }else if (value==34){
            state.setState(StateEnum.STRING);
            str = this.stateMachine(value);
        }else if (value==13){
            this.getVal();
            state.setState(StateEnum.EOL);
            currLine++;
            currPos = 0;
        }else if(value==-1){
            state.setState(StateEnum.EOF);
            this.setEof(true);
        }else{
            state.setState(StateEnum.DELIMITER);
            str = this.stateMachine(value);
        }
        return str;
    }
    
    public String stateMachine(int value){
        char c;
        String str = "";
        Boolean isValid = false;
        unflag = false;
        while(value!=-1 && !Character.isWhitespace(value)){
            switch(state.getState()){
                case INTEGER:
                    while(state.getState()==StateEnum.INTEGER){
                        c = (char)value; 
                        tok.add(c);
                        value = this.getVal();
                        if (value==46){//could be real
                            c = (char)value;
                            tok.add(c);
                            state.setState(StateEnum.REAL);
                        }else if(value<48 || value>57){//reached a delimiter
                            if(Character.isWhitespace(value)){
                                isValid=true;
                                break;
                            }else{
                                isValid=true;
                                val=value;
                                break;
                            }
                        }
                    }
                    break;
                case IDENT:
                    while(state.getState()==StateEnum.IDENT){
                        c = (char)value;
                        tok.add(c);
                        value = this.getVal();
                        if(!Character.isLetterOrDigit(value)){
                            isValid = true;
                            val = value;
                            break;
                        }
                    }
                    break;
                case REAL:
                    while(state.getState()==StateEnum.REAL){
                        value = this.getVal();
                        if ((value<48 || value>57) && !isValid){//undefined token
                            state.setState(StateEnum.UNDEF);
                            break;
                        }else if(Character.isWhitespace(value) && isValid){
                            break;
                        }else if(Character.isDigit(value)){
                            c = (char)value;
                            tok.add(c);
                            isValid = true;
                        }else{
                            val = value;
                            break;
                        }
                    }
                    break;
                case DELIMITER:
                    startPos=currPos;
                    if(tok.isEmpty()){
                        c = (char)value;
                        tok.add(c);
                        switch(value){
                            case 60:
                                value = this.getVal();
                                if(value==60 || value==61){//look for either another 60 or a 61
                                    c = (char)value;
                                    tok.add(c);
                                    isValid=true;
                                }else{
                                    val=value;
                                    d = true;
                                    isValid=true;
                                }
                                break;
                            case 62:
                                value = this.getVal();
                                if(value==62||value==61){//look for either another 62 or a 61
                                    c = (char)value;
                                    tok.add(c);
                                    isValid=true;
                                }else{
                                    val=value;
                                    d=true;
                                    isValid=true;
                                }
                                break;
                            case 33:
                                value = this.getVal(); 
                                if(value==61){//look for an equals else undef
                                    c = (char)value;
                                    tok.add(c);
                                    isValid=true;
                                }else{
                                    state.setState(StateEnum.UNDEF);
                                    unflag = true;
                                    if (!Character.isWhitespace(value)){
                                        val = value;
                                    }
                                }
                                break;
                            case 61:
                                value = this.getVal();
                                if(value==61){//look for another 61 else undef
                                    c = (char)value;
                                    tok.add(c);
                                    isValid=true;
                                }else{
                                    state.setState(StateEnum.UNDEF);
                                    //unflag = true;
                                }
                                break;
                            case 47:
                                value = this.getVal();
                                if(value==45){
                                    c = (char)value;
                                    tok.add(c);
                                    state.setState(StateEnum.COMMENT);
                                }else{
                                    isValid=true;
                                }
                                
                                break;
                            default:
                                if(!delims.contains(value)){
                                    state.setState(StateEnum.UNDEF);
                                    //unflag = true;
                                }else{
                                    isValid = true;
                                    val=0;
                                }
                                break;
                        }
                    }else{
                        val = value;
                    }
                    break;
                case STRING:
                    while(state.getState()==StateEnum.STRING){                   
                        if(value!=34){
                            c = (char)value;
                            tok.add(c);                              
                        }
                        value = this.getVal();
                        if(value==34){
                            isValid = true;
                            break;
                        }
                        if(value==13){
                            state.setState(StateEnum.UNDEF);
                            break;
                        }
                    }
                    break;
                case UNDEF:
                    //do something with the undefined token
                    System.out.println("It's undefined");
                    break;
                case COMMENT:
                    //the comment state
                    value = this.getVal();
                    if(value==45){
                        isValid = true;
                        c = (char)value;
                        tok.add(c);
                        while(((value=this.getVal())!=13) && value!=-1){
                            c = (char)value;
                            tok.add(c);
                        }
                    }
                    break;
            }
            if (value==13){
                value = this.getVal();//consume another token
                val=0;
                currLine++;
                currPos=0;
            }
            if (value==-1){
                this.setEof(true);
            }
            if(state.getState()==StateEnum.UNDEF){
                if(unflag){
                   break;
                }
                if(Character.isLetter(value)){
                    val=value;
                    break;
                }
                while(!Character.isWhitespace(value = this.getVal()) && !delims.contains(value) && !Character.isLetterOrDigit(value)){
                    c = (char)value;
                    tok.add(c);
                }
                val = value;
                break;
            }
            if (isValid){
                break;
            }
        }
        str = this.parseString(tok);
        tok.clear();
        return str;
    }
    
    public void setEof(boolean state){
        eofFlag=state;
    }
    
    public boolean getEof(){
        return eofFlag;
    }
    
    public TokId getTokId(String s){
        TokId t = tokenMap.get(s);
        return t;
    }
    
    public int getVal(){
        int v = 0;
        try{
            v = reader.read();
            currPos++;
            out.printChar((char)v);
        }catch (IOException e){
            System.out.println(e);
        }
        return v;
    }
    
    public String parseString(ArrayList t){
        String str = "";
        StringBuilder builder = new StringBuilder(t.size());
        for(Object ch: t){
            builder.append(ch);
        }
        str = builder.toString();
        str = str.toLowerCase();
        return str;
    }
}
