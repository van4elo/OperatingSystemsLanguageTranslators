
 
import java_cup.runtime.*;
/* import sym; */
 
public class scanner {
  /* single lookahead character */
  protected static int next_char;
 
  /* advance input by one character */
  protected static void advance()
    throws java.io.IOException
    { next_char = System.in.read(); }
 
  /* initialize the scanner */
  public static void init()
    throws java.io.IOException
    { advance(); }
 
  /* recognize and return the next complete token */
  public static Symbol next_token()
    throws java.io.IOException
    {
      for (;;)
        switch (next_char)
          {
            case '0': case '1': case '2': case '3': case '4':
            case '5': case '6': case '7': case '8': case '9':
              /* parse a decimal integer */
              int i_val = 0;
              do {
                i_val = i_val * 10 + (next_char - '0');
                advance();
              } while (next_char >= '0' && next_char <= '9');
            return new Symbol(sym.INT, new Integer(i_val));
 
            case 'p': advance(); advance(); advance(); advance(); 
                      advance();return new Symbol(sym.PRINT);
            case 'd': advance(); advance(); return new Symbol(sym.DO);
            case 'w': advance(); advance(); advance(); advance(); 
                      advance(); return new Symbol(sym.WHILE);
            case 's': advance(); advance(); advance(); advance(); 
		      advance();return new Symbol(sym.START);
            case 'f': advance(); advance(); advance();  advance(); advance(); advance();
		      return new Symbol(sym.FINISH);
            case ';': advance(); return new Symbol(sym.SEMI);
            case '+': advance(); return new Symbol(sym.PLUS);
            case '-': advance(); return new Symbol(sym.MINUS);
            case ',': advance(); return new Symbol(sym.COMMA);
            case '*': advance(); return new Symbol(sym.TIMES);
            case '/': advance(); return new Symbol(sym.DIVIDE);
            case '(': advance(); return new Symbol(sym.LPAREN);
            case ')': advance(); return new Symbol(sym.RPAREN);
          
	    case '<': if (System.in.read() == '=')
			{advance(); advance(); return new Symbol(sym.LESSEQ);}
		      else { advance(); return new Symbol(sym.LT);}

	    case '>': if (System.in.read() == '=')
			{advance(); advance(); return new Symbol(sym.GREATEQ);}
		      else { advance(); return new Symbol(sym.GT);}
	
	
	    case '=': if (System.in.read() == '='){
			{advance(); advance(); return new Symbol(sym.EQUALS);}
		      }
		      else {advance(); return new Symbol(sym.ASSIGNS);}
	    
	    case '!': if (System.in.read() == '='){
			{advance(); advance(); return new Symbol(sym.NOTEQUALS);}
		      }
		      
	

		
            case 'x': advance(); return new Symbol(sym.ID,"x");
            case 'y': advance(); return new Symbol(sym.ID,"y");
            case 'z': advance(); return new Symbol(sym.ID,"z");
	
	//Syntax for the If <Statement> then ... Else ...
	    case 'i': advance(); advance(); return new Symbol(sym.IF);
	    case 't': advance(); advance(); advance(); advance(); return new Symbol(sym.THEN);
	    case 'e': advance(); advance(); advance(); advance(); advance(); return new Symbol(sym.ENDIF);
	    case '#': advance(); return new Symbol(sym.ELSE);
	   
 
 
            case -1: return new Symbol(sym.EOF);
 
            default:
              /* in this simple scanner we just ignore everything else */
              advance();
            break;
          }
    }
};

