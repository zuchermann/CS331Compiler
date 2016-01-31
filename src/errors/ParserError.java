

package errors;

import Compiler.Lex.Token;
import Compiler.Lex.TokenType;

/**
 *
 * @author zacharykondak
 */
public class ParserError extends CompilerError {

    

    public ParserError(Type errorNumber, String message) {
        super(errorNumber, message);
    }
    
    public static ParserError mismatchedNonTerminals(TokenType expected, Token read, int line){
        return new ParserError(Type.MISMATCHED_TOKEN, 
                ">>> ERROR: at line " + line + ". Expected " + expected.toString() + " read " + read.getType() + ".");
    }
    
    public static ParserError stackIsEmpty(Token currentToken, int line){
         return new ParserError(Type.STACK_EMPTY, 
                ">>> ERROR: at line " + line + ". Expected end of file, instead got " + currentToken.getType() + ".");
      }
    
    public static ParserError invalidTransition(Token currentToken, int line){
         return new ParserError(Type.STACK_EMPTY, 
                ">>> ERROR: at line " + line + ". Did not expect " + currentToken.getType() + ".");
      }


}
