/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package errors;

/**
 * Base class for errors generated by the parts of the compiler.
 */
public abstract class CompilerError extends Exception
{
   /** The type of error.  New types should be added to the enumeration
    * as the compiler generates new errors.
    */
   public enum Type {BAD_COMMENT, ILLEGAL_CHARACTER, UNTERMINATED_COMMENT,
                     IDENTIFIER_TOO_LONG, CONSTANT_TOO_LONG, MISMATCHED_TOKEN,
                     STACK_EMPTY, UNDECLARED_VARIABLE,
                     ARITHMATIC_EXPRESSION_ERROR, RELATIONAL_EXPRESSION_ERROR,
                     INVALID_OPERANDS_MOD, SEMANTIC_EXPECTATIONS_SHATTERED,
                     ARRAY_OFFSET_ERROR, PARAM_ERROR, TYPE_MISMATCH,
                     ALREADY_DEFINED, ARRAY_BOUND_MISMATCH};

   /** The type of error represented by this object.  This field is declared
    * as final and must be set in the constructor.
    */
   protected final Type errorType;

   public CompilerError(Type errorType)
   {
      super("Unknown error");
      this.errorType = errorType;
   }

   public CompilerError(Type errorType, String message)
   {
      super(message);
      this.errorType = errorType;
   }

}
