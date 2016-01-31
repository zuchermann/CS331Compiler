/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Compiler.Lex;

/**
 *
 * @author zacharykondak
 */
public class Token {
    
    /**
     * The type of the token, represented as a TokenType.
     */
    TokenType type;
    
    /**
     * The value of the token, represented as a String.
     */
    String value;
    
    /**
     * Constructs a token with void type and value.
     */
    public Token(){
    }
    
    /**
     * Constructs a new token of given type and void value.
     * @param type type of token to be constructed.
     */
    public Token(TokenType type){
        this.type = type;
    }
    
    /**
     * Constructs a new token of given type and value.
     * @param type type of new token to be constructed.
     * @param value value of new token to be constructed.
     */
    public Token(TokenType type, String value){
        this.type = type;
        this.value = value;
    }
    
    /**
     * Returns the type of the token object this method is called on. 
     * @return type of token.
     */
    public TokenType getType(){
        return type;
    }
    
    /**
     * Returns the value of the token object this method is called on.
     * @return value of token.
     */
    public String getValue(){
        return value;
    }
    
    
    public int getIntValue(){
        return Integer.parseInt(value);
    }
    
    /**
     * Converts the value stored in the value field to a operator type.
     * @return operator type.
     */
    public String getOpType(){
        String opType = "NOT AN OPERATOR";
        if (type == TokenType.RELOP){
            if (null != value)
                switch (value) {
                case "1":
                    opType = "EQUAL";
                    break;
                case "2":
                    opType = "NOTEQUAL";
                    break;
                case "3":
                    opType = "LESSTHAN";
                    break;
                case "4":
                    opType = "GREATERTHAN";
                    break;
                case "5":
                    opType = "LESSTHANOREQUAL";
                    break;
                case "6":
                    opType = "GREATERTHANOREQUAL";
                    break;
            }
        }
        else if (type == TokenType.ADDOP){
            switch (value) {
                case "1":
                    opType = "PLUS";
                    break;
                case "2":
                    opType = "MINUS";
                    break;
                case "3":
                    opType = "OR";
                    break;
            }
        }
        else if (type == TokenType.MULOP){
            switch (value) {
                case "1":
                    opType = "*";
                    break;
                case "2":
                    opType = "/";
                    break;
                case "3":
                    opType = "DIV";
                    break;
                case "4":
                    opType = "MOD";
                    break;
                case "5":
                    opType = "AND";
                    break;
            }
        }
        return opType;
    }
    public String gettviOp() {
        String opType = "";
        if (type == TokenType.RELOP){
            if (null != value)
                switch (value) {
                case "1":
                    opType = "beq";
                    break;
                case "2":
                    opType = "bne";
                    break;
                case "3":
                    opType = "blt";
                    break;
                case "4":
                    opType = "bgt";
                    break;
                case "5":
                    opType = "ble";
                    break;
                case "6":
                    opType = "bge";
                    break;
            }
        }
        if (type == TokenType.ADDOP){
            switch (value) {
                case "1":
                    opType = "add";
                    break;
                case "2":
                    opType = "sub";
                    break;
            }
        }
        else if (type == TokenType.MULOP){
            switch (value) {
                case "1":
                    opType = "mul";
                    break;
                case "2":
                    opType = "div";
                    break;
                case "3":
                    opType = "div";
                    break;
            }
        }
        return opType;
    }
    
    public String toString(){
        String finalString = "";
        
        if (this.type == TokenType.INTCONSTANT){
            finalString = "INTEGER " + this.value;
        }
        else if (this.type == TokenType.REALCONSTANT){
            finalString = "REAL " + this.value;
        }
        else if (this.type == TokenType.IDENTIFIER){
            finalString = "IDENTIFIER " + this.value;
        }
        else if (this.type == TokenType.ADDOP){
            finalString = "OPERATOR " + this.getOpType();
        }
        else if (this.type == TokenType.MULOP){
            finalString = "OPERATOR " + this.getOpType();
        }
        else if (this.type == TokenType.RELOP){
            finalString = "OPERATOR " + this.getOpType();
        }
        else if (this.type != null){
            finalString = this.type.toString();
        }
        
        return finalString;
    }
    
    
    
}
