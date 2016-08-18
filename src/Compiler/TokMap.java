package Compiler;

import java.util.*;
/**
 *
 * @author $Robert Logan
 */
public class TokMap {
    private HashMap<String, String> tokenMap = new HashMap<>();
    
    public TokMap(){
        tokenMap.put("cd16", "TCD16");
        tokenMap.put("constants", "TCONS");
        tokenMap.put("types", "TTYPS");
        tokenMap.put("is", "TISKW");
        tokenMap.put("arrays", "TARRS");
        tokenMap.put("main", "TMAIN");
        tokenMap.put("begin", "TBEGIN");
        tokenMap.put("end", "TENDK");
        tokenMap.put("array", "TARRY");
        tokenMap.put("of", "TOFKW");
        tokenMap.put("func", "TFUNC");
        tokenMap.put("void", "TVOID");
        tokenMap.put("const", "TCNST");
        tokenMap.put("integer", "TINTG");
        tokenMap.put("real", "TREAL");
        tokenMap.put("boolean", "TBOOL");
        tokenMap.put("for", "TFORK");
        tokenMap.put("repeat", "TREPT");
        tokenMap.put("until", "TUNTL");
        tokenMap.put("if", "TIFKW");
        tokenMap.put("else", "TELSE");
        tokenMap.put("in", "TINKW");
        tokenMap.put("out", "TOUTP");
        tokenMap.put("line", "TOUTL");
        tokenMap.put("return", "TRETN");
        tokenMap.put("not", "TNOTK");
        tokenMap.put("and", "TANDK");
        tokenMap.put("or", "TORKW");
        tokenMap.put("xor", "TXORK");
        tokenMap.put("true", "TTRUE");
        tokenMap.put("false", "TFALS");
        tokenMap.put("[", "TLBRK");
        tokenMap.put("]", "TRBRK");
        tokenMap.put("(", "TLPAR");
        tokenMap.put(")", "TRPAR");
        tokenMap.put(";", "TSEMI");
        tokenMap.put(",", "TCOMA");
        tokenMap.put(":", "TCOLN");
        tokenMap.put(".", "TDOTT");
        tokenMap.put("<<", "TASGN");
        tokenMap.put(">>", "TINPT");
        tokenMap.put("==", "TDEQL");
        tokenMap.put(">", "TGRTR");
        tokenMap.put("<=", "TLEQL");
        tokenMap.put("<", "TLESS");
        tokenMap.put(">=", "TGREQ");
        tokenMap.put("+", "TADDT");
        tokenMap.put("-", "TSUBT");
        tokenMap.put("*", "TMULT");
        tokenMap.put("/", "TDIVT");
        tokenMap.put("%", "TPERC");
        tokenMap.put("^", "TACRT");
        tokenMap.put("!=", "TNEQL");    
    }
    
    public String getTokId(String s){
        String t = tokenMap.get(s);
        return t;
    }
}
