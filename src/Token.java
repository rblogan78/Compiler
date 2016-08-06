/**
 * Created by Rob on 28/07/16.
 */
public class Token {
    private String tid;		// token identifier
    private int line;		// line number on listing
    private int pos;		// character position within line
    private String str;		// actual lexeme character string from scanner
    private TokMap tokens = new TokMap();
//	private StRec symbol;		// symbol table entry - used in Pt3 for the Parser, not needed in Pt1

    public Token(String t, int ln, int p, String s) {
        tid = t;
        line = ln;
        pos = p;
        str = s;
        if (tid.equals("TIDNT")) {		// Identifier token could be a reserved keyword in CD16
            String v = checkKeywords(s);			// (match is case-insensitive)
            if (!v.equals("TIDNT")) { tid = v; str = null; }	// if keyword, alter token type
        }
//		symbol = null;	// initially null, got from Parser SymTab lookup if TIDNT/TILIT/TFLIT/TSTRG
    }

    public String value() { return tid; }

    public int getLn() { return line; }

    public int getPos() { return pos; }

    public String getStr() { return str; }

//	public StRec getSymbol() { return symbol; }			// ready for Part 3

//	public void setSymbol(StRec x) {symbol = x; }			// ready for Part 3


    private String checkKeywords(String s) {
        s = s.toLowerCase();		// change to lower case before checking
        String str = tokens.getTokId(s);
        if (str.equals("")){
            return "TIDNT";// not a Keyword, therefore an <id>
        } else{
            return str;
        }
    }
    
    @Override
    public String toString() {		// toString method is only meant to be used for debug printing
        String s = tid;
        while (s.length() % 6 != 0) s = s + " ";
        s = s +" " + line + " " + pos;
        if (str == null) return s;
        if (!tid.equals("TUNDF"))
            s += " " + str;
        else {
            s += " ";
            for (int i=0; i<str.length(); i++) { // output non-printables as ascii codes
                char ch = str.charAt(i);
                int j = (int)ch;
                if (j <= 31 || j >= 127) s += "\\" +j; else s += ch;
            }
        }
        return s;
    }

    public String shortString() {		// provides a String that may be useful for Part 1 printed output
        String s = tid + " ";
        if (str == null) return s;
        if (!tid.equals("TUNDF")) {
            if (tid.equals("TSTRG"))
                s += "\"" + str + "\" ";
            else
                s += str + " ";
            int j = (6 - s.length()%6) % 6;
            for (int i=0; i<j; i++)
                s += " ";
            return s;
        }
        s = "\n" + s;
        for (int i=0; i<str.length(); i++) { // output non-printables as ascii codes
            char ch = str.charAt(i);
            int j = (int)ch;
            if (j <= 31 || j >= 127) s += "\\" +j; else s += ch;
        }
        s += "\n";
        return s;
    }
}
