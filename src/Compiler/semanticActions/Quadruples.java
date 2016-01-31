
package Compiler.semanticActions;

import java.util.Vector;

/**
 *
 * @author zakondak
 */
public class Quadruples {
    
    private Vector<String[]> quadruple;
    private int nextQuad;
    
    public Quadruples() {
        quadruple = new Vector();
        nextQuad = 0;
    }
    
    public String getField(int quadIndex, int field){
        return quadruple.get(quadIndex)[field];
    }
    
    public void setField(int quadIndex, int fieldIndex, String field){
        quadruple.get(quadIndex)[fieldIndex] = field;
    }
    
    public int getNextQuad(){
        return nextQuad;
    }
    
    public void incrementNextQuad(){
        nextQuad++;
    }
    
    public String[] getQuad(int index){
        return quadruple.get(index);
    }
    
    public void addQuad(String[] quad){
        quadruple.add(quad);
    }
    
    public void print(){
        System.out.println("CODE");
        int i = 1;
        while(i < quadruple.size()){
            String[] thisQuad = quadruple.get(i);
            String quadString = "";
            int j = 1;
            quadString = quadString + thisQuad[0];
            while (j < thisQuad.length - 1) {
                quadString = quadString + " " + thisQuad[j] + ",";
                j++;
            }
            if(thisQuad.length > 1){
            quadString = quadString + " " + thisQuad[thisQuad.length - 1];
            }
            System.out.println(i + ": " + quadString);
            i++;
        }
    }
    
}
