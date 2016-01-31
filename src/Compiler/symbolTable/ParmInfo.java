/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Compiler.symbolTable;

import Compiler.Lex.TokenType;

/**
 *
 * @author zakondak
 */
public class ParmInfo {
    TokenType type;
    int upperBound;
    int lowerBound;
    boolean array;
    
    public void setType(TokenType type){
        this.type = type;
    }
    
    public void setUpperBound(int upperBound){
        this.upperBound = upperBound;
    }
    
    public void setLowerBound(int lowerBound){
        this.lowerBound = lowerBound;
    }
    
    public void setArray(boolean array){
        this.array = array;
    }
    
    public TokenType getType(){
        return this.type;
    }
    
    public int getUpperBound(){
        return this.upperBound;
    }
    
    public int getLowerBound(){
        return this.lowerBound;
    }
    
    public boolean getArray(){
        return this.array;
    }
}
