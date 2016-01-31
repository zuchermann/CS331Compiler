

package errors;

import Compiler.Lex.TokenType;

/**
 *
 * @author zacharykondak
 */
public class SemanticError extends CompilerError {
    
     public SemanticError(Type errorNumber, String message) {
        super(errorNumber, message);
    }
     
     public static SemanticError undeclaredVariable(String var, int line){
        return new SemanticError(Type.UNDECLARED_VARIABLE, 
                ">>> ERROR AT LINE " + line + ": Variable " + var + " is not defined.");
    }
     
   public static SemanticError arExpError(int line){
        return new SemanticError(Type.ARITHMATIC_EXPRESSION_ERROR, 
                ">>> ERROR AT LINE " + line + ": Expected arithmatic expression.");
    }
   
   public static SemanticError relExpError(int line){
        return new SemanticError(Type.RELATIONAL_EXPRESSION_ERROR, 
                ">>> ERROR AT LINE " + line + ": Expected relational expression.");
    }
   
   public static SemanticError modProblem(int line){
        return new SemanticError(Type.INVALID_OPERANDS_MOD, 
                ">>> ERROR AT LINE " + line + ": the MOD operator requires integer operands.");
    }
   
   public static SemanticError semanticMismatched(int line, String actual, String expected){
        return new SemanticError(Type.SEMANTIC_EXPECTATIONS_SHATTERED, 
                ">>> ERROR AT LINE " + line + ": expected " + expected + ", read " + actual + ".");
    }
   
   public static SemanticError semanticMismatched(String actual, String expected){
        return new SemanticError(Type.SEMANTIC_EXPECTATIONS_SHATTERED, 
                ">>> ERROR: expected " + expected + ", read " + actual + ".");
    }
   
   public static SemanticError arrayAccessError(int line){
        return new SemanticError(Type.ARRAY_OFFSET_ERROR, 
                ">>> ERROR AT LINE " + line + ": array indices must be integers.");
    }
   
   public static SemanticError wrongNumberOfParams(int line){
        return new SemanticError(Type.PARAM_ERROR, 
                ">>> ERROR AT LINE " + line + ": wrong number of parameters.");
    }
     
     public static SemanticError typeMismatch(int line, String type1, String type2){
        return new SemanticError(Type.TYPE_MISMATCH, 
                ">>> ERROR AT LINE " + line + ": types must match, revieved " + type1 + " and " + type2 + ".");
    }
     
     public static SemanticError alreadyDefined(int line, String thing){
        return new SemanticError(Type.ALREADY_DEFINED, 
                ">>> ERROR AT LINE " + line + ": " + thing + " cannot be redefined.");
    }
     
     public static SemanticError arrayBoundMismatch(int line, Integer actual, Integer expected){
        return new SemanticError(Type.ARRAY_BOUND_MISMATCH, 
                ">>> ERROR AT LINE " + line + ": array bound mismatch, expected " + expected.toString() + ", read " + actual.toString() + ".");
    }
}
