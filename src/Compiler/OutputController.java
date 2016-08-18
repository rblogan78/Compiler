package Compiler;

/**
 * @author Robert Logan on 28/07/16.
 */
import java.io.*;

public class OutputController {

    private int line = 0;
    private int charPos = 0;
    private int errorCount = 0;
    private InputStreamReader src = null;
    private PrintWriter listing = null;
    private StringBuffer err = null;
    private String currLine = null;
    private String errLine = null;


    public OutputController(PrintWriter l, StringBuffer e) {
        listing = l;		// copy the file references to local attributes
        err = e;
        currLine = "  1: ";
        errLine = "";
        line = 1;
        charPos = 0;
        errorCount = 0;
    }

    public int getErrorCount() { return errorCount; }

    public void printImmediateError(String msg) {	// set for immediate o/p of lexical error found at eol
        listing.println(msg);
        err.append(msg+"\n");
        errorCount++;
    }

    public void setError(String msg) {	// save up an error to be output at eol
        if (!errLine.equals("")) errLine += "\n";
        errLine += msg;
        errorCount++;
    }

    public void printChar(char ch) {	// stores next char - Prints a listing line if a newline char

        if (ch == '\n') {			// at newline - produce the next line of the listing
            listing.println(currLine);
            // Trace output if reqd - System.out.println(currLine);
            if (! errLine.equals("")) {		// if there are errors then report them as well
                listing.println(errLine);
                err.append(currLine+"\n");	// put source line that contains the errors into text area
                err.append( errLine+"\n");	// put error messages for this line into text area
                // Trace output if reqd - System.out.println(errline);
                errLine = "";
            }
            line++;
            if (line < 10) currLine = "  "+line+": ";
            else if (line < 100) currLine = " "+line+": ";
            else currLine = line+": ";
            charPos = 0;
        } else {			// put the character into the output buffer
            currLine += ""+ch;
            charPos++;
        }
    }

    public void printChar( ) {		// Due to sliding window of tokens, errors near eof must be explicitly flushed

        if (! errLine.equals("")) {		// if there are errors then report them as well
            listing.println(errLine);
            err.append(currLine+"\n");	// put source line that contains the errors into text area
            err.append( errLine+"\n");	// put error messages for this line into text area
            // Trace output if reqd - System.out.println(errline);
            errLine = "";
        }
    }

}
