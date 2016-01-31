

package Compiler.symbolTable;

import Compiler.Lex.TokenType;

/**
 *
 * @author zacharykondak
 */
public class ConstantEntry extends SymbolTableEntry{
    
    public ConstantEntry(String Name, TokenType type) {
		super(Name, type);	
	}
    
    public boolean isArray() { 
		return false; 
	}
        
        public boolean isConstant() { 
		return true; 
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
		
		System.out.println("Constant Entry:");
		System.out.println("   Name    : " + this.getName());
		System.out.println("   Type    : " + this.getType());
		System.out.println();
	}
    
}
