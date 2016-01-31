
package Compiler.symbolTable;

import Compiler.Lex.Token;
import Compiler.Lex.TokenType;
import java.util.LinkedList;

/**
 *
 * @author zacharykondak
 */
public class SymbolTableEntry {

    String Name;
    TokenType type;
    boolean reserved;
    int address;
    SymbolTableEntry functionResult;
    int numberOfParams;
    boolean isParm;
    boolean isFuncRes;
    LinkedList<ParmInfo> parameterInfo = new LinkedList<ParmInfo>();
    
    public void setIsFunctionResult(boolean isFuncRes){
        this.isFuncRes = isFuncRes;
    }
    
    public boolean isFunctionResult() {
        return this.isFuncRes;
    }

    public void addParaminfo(ParmInfo info) {
        this.parameterInfo.add(info);
    }

    public void addParaminfo(LinkedList<ParmInfo> info) {
        for (ParmInfo thisParm : info) {
            this.parameterInfo.add(thisParm);
        }
    }

    public SymbolTableEntry() {
    }

    public SymbolTableEntry(String Name) {
        this.Name = Name;
    }

    public SymbolTableEntry(String Name, TokenType type) {
        this.Name = Name;
        this.type = type;
    }
    
    public void setIsParm(boolean isParm){
        this.isParm = isParm;
    }
    
    public void setNumberOfParams(int numberOfParams){
        this.numberOfParams = numberOfParams;
    }
    
    public void setFunctionResult(SymbolTableEntry result){
        this.functionResult = result;
    }
    
    public void setType(TokenType type){
        this.type = type;
    }
    
    public void setAddress(int address){
        this.address = address;
    }
    
    public LinkedList<ParmInfo> getParameterInfo() {
        return this.parameterInfo;
    }
    
    public boolean getIsParm(){
        return this.isParm;
    }
    
    public SymbolTableEntry getFunctionResult(){
        return this.functionResult;
    }
            
    public int getNumberOfParams(){
        return this.numberOfParams;
    }
    
    public int getAddress() {
        return this.address;
    }

    public String getName() {
        return this.Name;
    }

    public TokenType getType() {
        return this.type;
    }
    
    public void setReserved(){
        reserved = true;
    }
    
    public boolean getReserved(){
            return this.reserved;
    }
    
    public boolean isArray() {
        return false;
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
		
		System.out.println("Symbol Table Entry:");
		System.out.println("   Name    : " + this.getName());
		System.out.println();
	}
    
    public String toString (){
        String finalString = "";
        if (this.Name.startsWith("$$")){
            finalString = this.type.toString();
        }
        else{
            Token tok = new Token(this.type, this.Name);
            finalString = tok.toString();
        }
        return finalString;
    }
}
