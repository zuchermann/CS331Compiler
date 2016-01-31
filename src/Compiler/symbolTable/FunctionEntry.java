package Compiler.symbolTable;

import Compiler.Lex.TokenType;
import java.util.LinkedList;

/**
 *
 * @author zacharykondak
 */
public class FunctionEntry extends SymbolTableEntry {

    int numberOfParameters;
    TokenType result;
    LinkedList<ParmInfo> parameterInfo = new LinkedList<ParmInfo>();

    public int getNumberOfParameters() {
        return this.numberOfParameters;
    }
    
    public LinkedList<ParmInfo> getParameterInfo() {
        return this.parameterInfo;
    }

    public TokenType getResult() {
        return this.result;
    }
    
    public void addParaminfo(ParmInfo info){
       this.parameterInfo.add(info);
   }
   
   public void addParaminfo(LinkedList<ParmInfo> info){
       for(ParmInfo thisParm: info){
           this.parameterInfo.add(thisParm);
       }
   }

    public FunctionEntry(String Name, int numberOfParameters,
            LinkedList<ParmInfo> parameterInfo, TokenType result) {
        super(Name);
        this.numberOfParameters = numberOfParameters;
        this.parameterInfo = parameterInfo;
        this.result = result;
    }
    

    
    public FunctionEntry(String name){
        super(name);
    }

    public boolean isArray() {
        return false;
    }

    public boolean isConstant() {
        return false;
    }

    public boolean isFunction() {
        return true;
    }

    public boolean isProcedure() {
        return false;
    }

    public boolean isVariable() {
        return false;
    }
    
    public void print() {

        System.out.println("Function Entry:");
        System.out.println("   Name    : " + this.getName());
        System.out.println("   Number of Parameters   : " + this.getNumberOfParameters());

        String paramList = "";
        LinkedList<ParmInfo> paramInfo = this.getParameterInfo();
        int i = 0;
        while (i < this.getNumberOfParameters()) {
            paramList = paramList + ", " + paramInfo.get(i).toString();
            i++;
        }

        System.out.println("   Parameter Info : " + paramList);
        System.out.println("   Result : " + this.getResult().toString());
        System.out.println();
    }

}
