

package Compiler.Lex;

/**
 *
 * @author zacharykondak
 */
public class MyStack {
    
    /**
     * Size of the stack.
     */
    public static final int SIZE = 2;
    
    /**
     * Array that will be implement as a stack.
     */
    char[] chars = new char[SIZE];
    
    /**
     * Current position in stack, either 0 or 1.
     */
    private int top;
    
    /**
     * Constructs a new stack.
     */
    public MyStack(){
        top = 0;
    }
   
    /**
     * Pushes a character onto the stack.
     * @param c character to be pushed onto the stack.
     */
    public void push(char c){
        top = ((top + 1) % 2);
        chars[top] = c;
    }
    
    /**
     * Returns the most recently pushed character
     * (only remembers last two characters).
     * @return most recently pushed character on the stack
     */
    public char pop() {
        char cur = chars[top];
        top = ((top + 1) % 2);
      return cur;
   }
    
    
}
