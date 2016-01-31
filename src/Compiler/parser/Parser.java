package Compiler.parser;

import Compiler.GrammarSymbol;
import Compiler.Lex.*;
import Compiler.semanticActions.*;
import errors.CompilerError;
import errors.LexicalError;
import errors.ParserError;
import java.util.Stack;

/**
 *
 * @author zacharykondak
 */
public class Parser {

    private RHSTable RHS_TABLE = new RHSTable();
    private int[][] PARSE_TABLE = ParseTable.NEW();
    private Tokenizer t;
    private Stack stack = new Stack();
    private int line = 0;
    private Token currentToken;
    private Token lastToken;
    private TokenType in;
    private GrammarSymbol topStack;
    private SemanticActions actions;
    private boolean end;
    private boolean done = false;
    
    final int ERROR = 999;
    
    public SemanticActions getActions(){
        return this.actions;
    }

    //true iff compilation cannot continue 
    //either accept or unresolvable error
    /**
     * Access done field; true iff compilation cannot continue 
     * either accept or unresolvable error
     * @return boolean done; true iff parse cannot continue
     */
    public boolean getDone() {
        return this.done;
    }

    /**
     * Constructs a new parser that will parse file found in path
     * found in input filename
     * @param filename file path for file to be parsed
     * @throws LexicalError 
     */
    public Parser(String filename) throws LexicalError {

        t = new Tokenizer(filename);
        line = t.getLine();
        stack.push(TokenType.ENDOFFILE);
        stack.push(NonTerminal.Goal);
        currentToken = t.getNextToken();
        in = currentToken.getType();
        topStack = (GrammarSymbol) stack.peek();
        actions = new SemanticActions();
        if (in == TokenType.ENDOFFILE){
            done = true;
        }
        end = false;
    }

    /**
     * Executes parse on a given instance of parser.
     * @throws CompilerError 
     */
    public void parse() throws CompilerError {

        while (!(end)) {
            //execute appropriate action
            while (topStack.isAction()) {
                stack.pop();
                SemanticAction thisAction = (SemanticAction) topStack;
                try{
                actions.Execute(thisAction, lastToken, t.getLine());
                }
                catch (CompilerError e){
                    done = true;
                    throw e;
                }
                
                if (stack.isEmpty()) {
                    break;
                } else {
                    topStack = (GrammarSymbol) stack.peek();
                }
            }

            //top of stack is a terminal
            if (topStack.isToken()) {
                //
                //reached the end of stack
                if (topStack == TokenType.ENDOFFILE) {
                    end = true;
                }//
                //top of stack matches input symbol
                else {
                    if (in == topStack) {
                        stack.pop();
                        if (stack.isEmpty()) {
                            break;
                        } else {
                            topStack = (GrammarSymbol) stack.peek();
                        }
                        line = t.getLine();
                        lastToken = currentToken;
                        currentToken = t.getNextToken();
                        in = currentToken.getType();
                    }//
                    //error
                    else {
                        //print stack
                        //System.out.println(stack);
                        ParserError mismatched = ParserError.mismatchedNonTerminals(
                                (TokenType) topStack, currentToken, line);
                        recover(mismatched);
                    }
                }

            }
            
            //
            //top of stack is a non-terminal
            else {
                //cast topStack to NonTerminal
                NonTerminal nonTerm = (NonTerminal) topStack;
                int trans = PARSE_TABLE[in.getIndex()][nonTerm.getIndex()];

                //pop off top of stack
                stack.pop();

                //find appropriate transition if RHS of production is not empty
                if (trans > 0) {

                    //error
                    if (trans == ERROR) {
                        ParserError invalid = ParserError.invalidTransition(currentToken, line);
                        recover(invalid);
                    } //
                    //valid production
                    else {
                        GrammarSymbol[] currentProduction = 
                                RHS_TABLE.getRule(trans);
                        int index = (currentProduction.length - 1);
                        while (index >= 0) {
                            stack.push(currentProduction[index]);
                            index--;
                        }
                    }
                } //
                //push nothing on stack if RHS of production is empty
                else;
                //get next top item on stack
                if (stack.isEmpty()) {
                    break;
                } else {
                    topStack = (GrammarSymbol) stack.peek();
                }
            }
        }//
        //check if end of file matches end of stack
        if ((in == TokenType.ENDOFFILE)) {
            done = true;
        } else {
            done = true;
            throw ParserError.stackIsEmpty(currentToken, line);
        }
        
    }

    //panic mode
    public void panic() throws LexicalError {

        //pop off of stack until you read a semicolon
        while (true) {

            if (topStack.isToken()) {
                TokenType temp = (TokenType) topStack;
                if (!(temp == TokenType.ENDOFFILE)) {
                    stack.pop();
                    topStack = (GrammarSymbol) stack.peek();
                    if (topStack.isToken()) {
                        temp = (TokenType) topStack;
                        if (temp == TokenType.SEMICOLON) {
                            break;
                        }
                    }
                } else {
                    //end of file
                    break;
                }
            } else {
                stack.pop();
                if (stack.isEmpty()) {
                    break;
                }
                topStack = (GrammarSymbol) stack.peek();
            }
        }

        //read next token until you read a semicolon
        while (in != TokenType.ENDOFFILE) {
            line = t.getLine();
            lastToken = currentToken;
            currentToken = t.getNextToken();
            in = currentToken.getType();
            if (in == TokenType.SEMICOLON) {
                break;
            }
        }

    }

    private boolean isEnd() {
        boolean end = false;
        if (topStack.isToken()) {
            TokenType temp = (TokenType) topStack;
            if (temp == TokenType.ENDOFFILE || in == TokenType.ENDOFFILE) {
                end = true;
            }
        }
        return end;
    }

    /**
     * Resolves simple errors so that a parse may continue; if an error is
     * present after recover has attempted to resolve an issue, recover will
     * invoke panic() method to set the parser into panic mode.
     *
     * @param anError the error that caused the problem to be recovered from
     * @throws CompilerError
     */
    public void recover(ParserError anError) throws CompilerError {

        if (isEnd()) {
            done = true;
        } //if stack is not empty and input is not empty,
        ////ignore topStack and in.
        else {

            stack.pop();
            if (!stack.empty()) {
                topStack = (GrammarSymbol) stack.peek();
                line = t.getLine();
                lastToken = currentToken;
                currentToken = t.getNextToken();
                in = currentToken.getType();

                /*ignore actions
                 while (topStack.isAction()) {
                 stack.pop();
                 topStack = (GrammarSymbol) stack.peek();
                 }
                 */
                //next thing on stack is a non-terminal
                if (topStack.isNonTerminal()) {

                    //cast topStack to NonTerminal
                    NonTerminal nonTerm = (NonTerminal) topStack;
                    int nextTrans = PARSE_TABLE[in.getIndex()][nonTerm.getIndex()];
                    //if next transition is an error, go into panic mode
                    if (nextTrans == ERROR) {
                        panic();
                    }

                }
                //next thing on stack is a token
                if (topStack.isToken()) {
                    TokenType temp = (TokenType) topStack;
                    //if next read does not match topStack, panic
                    if (temp != in) {
                        panic();
                    }
                }
            }
        }

        throw anError;
        
    }
    
   


}
