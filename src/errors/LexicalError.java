

package errors;


/** Exception class thrown when a lexical error is encountered. */
public class LexicalError extends CompilerError
{
   public LexicalError(Type errorNumber, String message)
   {
      super(errorNumber, message);
   }

   // Factory methods to generate the lexical exception types.

   public static LexicalError BadComment(int line)
   {
      return new LexicalError(Type.BAD_COMMENT,
                              ">>> ERROR: Bad comment at line " + line + ". Cannont include { inside a comment.");
   }

   public static LexicalError IllegalCharacter(char c, int line)
   {
      return new LexicalError(Type.ILLEGAL_CHARACTER,
                              ">>> ERROR: Illegal character at line " + line + ": " + c);
   }

   public static LexicalError UnterminatedComment(int line)
   {
      return new LexicalError(Type.UNTERMINATED_COMMENT,
                              ">>> ERROR: Unterminated comment at line " + line + ".");
   }
   
   public static LexicalError IdentifierTooLong()
   {
       return new LexicalError(Type.IDENTIFIER_TOO_LONG,
                               ">>> ERROR: Identifier name too long.");
   }
   
   public static LexicalError ConstantTooLong()
   {
       return new LexicalError(Type.CONSTANT_TOO_LONG,
                               ">>> ERROR: Constant value too long.");
   }
}