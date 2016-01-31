package Compiler.symbolTable;

import Compiler.Lex.TokenType;

/**
 *
 * @author zacharykondak
 */
public class ArrayEntry extends SymbolTableEntry {

    int address;
    int upperBound;
    int lowerBound;

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }
    
    public int getUpperBound(){
        return this.upperBound;
    }
    
    public int getLowerBound(){
        return this.lowerBound;
    }
    
    public void setUpperBound(int upperBound){
        this.upperBound = upperBound;
    }
    
    public void setLowerBound(int lowerBound){
        this.lowerBound = lowerBound;
    }
    
    public ArrayEntry(String Name, int address, TokenType type, int upperBound, int lowerBound){
        super(Name, type);
        this.address = address;
        this.upperBound = upperBound;
        this.lowerBound = lowerBound;
        
    }
    
    public ArrayEntry(String Name, TokenType type){
        super(Name, type);
    }
    
    public boolean isArray() { 
		return true; 
	}
        
        public boolean isConstant() { 
		return false; 
	}
        
         public boolean isFunction() { 
		return false; 
	}
         
         public boolean isProcedure() { 
		return false; 
	}
         
        public boolean isVariable() { 
		return false; 
	}
        
        
        public void print () {
		
		System.out.println("Array Entry:");
		System.out.println("   Name    : " + this.getName());
		System.out.println("   Type    : " + this.getType());
		System.out.println("   Address : " + this.getAddress());
                System.out.println("   Upper Bound : " + this.getUpperBound());
                System.out.println("   Lower Bound : " + this.getLowerBound());
		System.out.println();
	}

}
