/**
 *
 * @author $Robert Logan
 */
public class TokMap {
    private static final String[][] tokens = {
        {"cd16", "TCD16"},
        {"constants", "TCONS"},
        {"types", "TTYPS"},
        {"is", "TISKW"},
        {"arrays", "TARRS"},
        {"main", "TMAIN"},
        {"begin", "TBEGIN"},
        {"end", "TENDK"},
        {"array", "TARRY"},
        {"of", "TOFKW"},
        {"func", "TFUNC"},
        {"void", "TVOID"},
        {"const", "TCNST"},
        {"integer", "TINTG"},
        {"real", "TREAL"},
        {"boolean", "TBOOL"},
        {"for", "TFORK"},
        {"repeat", "TREPT"},
        {"until", "TUNTL"},
        {"if", "TIFKW"},
        {"else", "TELSE"},
        {"in", "TINKW"},
        {"out", "TOUTP"},
        {"line", "TOUTL"},
        {"return", "TRETN"},
        {"not", "TNOTK"},
        {"and", "TANDK"},
        {"or", "TORKW"},
        {"xor", "TXORK"},
        {"true", "TTRUE"},
        {"false", "TFALS"},
        {"[", "TLBRK"},
        {"]", "TRBRK"},
        {"(", "TLPAR"},
        {")", "TRPAR"},
        {";", "TSEMI"},
        {",", "TCOMA"},
        {":", "TCOLN"},
        {".", "TDOTT"},
        {"<<", "TASGN"},
        {">>", "TINPT"},
        {"==", "TDEQL"},
        {">", "TGRTR"},
        {"<=", "TLEQL"},
        {"<", "TLESS"},
        {">=", "TGREQ"},
        {"+", "TADDT"},
        {"-", "TSUBT"},
        {"*", "TMULT"},
        {"/", "TDIVT"},
        {"%", "TPERC"},
        {"^", "TACRT"},
        {"!=", "TNEQL"},
    };
    
    public String getTokId(String s){
        boolean flag=false;
        int i=0;
        while (!flag && i<54){
            if (s.equals(tokens[i])){
                flag=true;
            }
            i++;
        }
        if(flag) {
            return tokens[i][0];
        }else{
            return "";
        }
    }
}
