
package Compiler.symbolTable;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 *
 * @author zacharykondak
 */
public class SymbolTable {

    Hashtable<String, SymbolTableEntry> table;

    //constructs a new table of a given size
    public SymbolTable(int size) {
        table = new Hashtable<String, SymbolTableEntry>(size);
    }

    //inserts an entry into the table
    public void insert(SymbolTableEntry newEntry) {
        if (table.containsKey(newEntry.getName())) {
            table.remove(newEntry.getName());
        }
        table.put(newEntry.getName(), newEntry);
    }

    //returns true iff entry is in the table
    public boolean lookup(SymbolTableEntry entry) {
        return table.containsKey(entry.getName());
    }
    
    //returns true iff id is in table
    public boolean lookup(String name){
        return table.containsKey(name);
    }
    
    //clears table
    public void clearTable(){
        table.clear();
    }
    
    //gets SymbolTableEntry mapped to string input
    public SymbolTableEntry getEntry(String id){
        return table.get(id);
    }
    
    //helper for dumpTable
    public void printName(SymbolTableEntry entry){
        if (entry != null){
        entry.print();
        }
    }

    //prints the name of all values in table
    public void dumpTable() {
        Enumeration elements = table.elements();
        while (elements.hasMoreElements()){
            SymbolTableEntry printEntry = (SymbolTableEntry) elements.nextElement();
            printName(printEntry);
        }
    }
}
