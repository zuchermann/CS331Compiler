
package Compiler.semanticActions;

import Compiler.symbolTable.ParmInfo;
import java.util.LinkedList;
import java.util.Stack;

/**
 *
 * @author zakondak
 */
public class NextParm {
    Stack<LinkedList<ParmInfo>> parmLists;
    Stack<Integer> parmPointers;
    
    public NextParm(){
        parmLists = new Stack<LinkedList<ParmInfo>>();
        parmPointers = new Stack<Integer>();
    }
    
    public void push(LinkedList<ParmInfo> parmList){
        this.parmLists.push(parmList);
        this.parmPointers.push(0);
    }
    
    public ParmInfo getNextParm(){
        return this.parmLists.peek().get(this.parmPointers.peek());
    }
    
    public LinkedList<ParmInfo> pop(){
        this.parmPointers.pop();
        return this.parmLists.pop();
    }
    
    public void increment(){
        int temp = this.parmPointers.pop();
        this.parmPointers.push(temp + 1);
    }
}
