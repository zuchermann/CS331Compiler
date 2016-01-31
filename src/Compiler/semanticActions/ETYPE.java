package Compiler.semanticActions;


public enum ETYPE {

    ARITHMATIC(0), RELATIONAL(1);
    
    private int n;

    private ETYPE(int i){
        n = i;
    }

    public int getIndex() {
        return n;
    }
}