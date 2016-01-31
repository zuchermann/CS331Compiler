package Compiler.symbolTable;

import Compiler.Lex.TokenType;
import java.util.LinkedList;

/**
 *
 * @author zacharykondak
 */
public class ProcedureEntry extends SymbolTableEntry {

    int numberOfParameters;
    LinkedList<ParmInfo> parameterInfo = new LinkedList<ParmInfo>();

    public int getNumberOfParameters() {
        return this.numberOfParameters;
    }

    public LinkedList getParameterInfo() {
        return this.parameterInfo;
    }
    
    public void setParameterInfo(LinkedList parameterInfo) {
        this.parameterInfo = parameterInfo;
    }

    public ProcedureEntry(String Name, int numberOfParameters,
            LinkedList parameterInfo) {
        super(Name);
        this.numberOfParameters = numberOfParameters;
        this.parameterInfo = parameterInfo;
    }
    
   public ProcedureEntry(String name){
       super(name);
       this.numberOfParams = 0;
   }
   
   public void addParaminfo(ParmInfo info){
       this.parameterInfo.add(info);
   }
   
   public void addParaminfo(LinkedList<ParmInfo> info){
       for(ParmInfo thisParm: info){
           this.parameterInfo.add(thisParm);
       }
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
        return true;
    }

    public boolean isVariable() {
        return false;
    }

    public void print() {

        System.out.println("Procedure Entry:");
        System.out.println("   Name    : " + this.getName());
        System.out.println("   Number of Parameters   : " + this.getNumberOfParameters());

        String paramList = "";
        LinkedList paramInfo = this.getParameterInfo();
        int i = 0;
        while (i < this.getNumberOfParameters()) {
            paramList = paramList + ", " + paramInfo.get(i).toString();
            i++;
        }

        System.out.println("   Parameter Info : " + paramList);
        System.out.println();
    }

}
