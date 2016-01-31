package Compiler.semanticActions;

/**
 *
 * @author zacharykondak
 */
import java.lang.*;
import errors.*;
import java.util.*;
import Compiler.Lex.*;
import Compiler.parser.*;
import Compiler.symbolTable.*;
import drivers.*;

public class SemanticActions {

    ////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////filds/////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    
    private Stack<Object> semanticStack;
    private Stack<Integer> parmCount;
    private Quadruples quads;
    private boolean insert;
    private boolean isArray;
    private boolean global;
    private int globalMemory;
    private int localMemory;
    private int tableSize;
    private int globalStore;
    private int localStore;
    private Integer tempCount;
    private SymbolTable globalTable;
    private SymbolTable localTable;
    private SymbolTable constantTable;
    private SymbolTableEntry currentFunction;
    private NextParm nextParm;
    
    private static final String Q = "\"";
    public static final int ERROR = -999;
    
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////accessors///////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    
    //These accessors are only used for unit testing at the moment. 
    
    public boolean getInsert(){
        return this.insert;
    }
    
    public Stack<Object> getSemanticStack(){
        return this.semanticStack;
    }
    
    public boolean getIsArray(){
        return this.isArray;
    }
    
    public boolean getGlobal(){
        return this.global;
    }
    
    public int getGlobalMemory(){
        return this.globalMemory;
    }
    
    public int getLocalMemory(){
        return this.localMemory;
    }
    
    public int getTableSize(){
        return this.tableSize;
    }
    
    public SymbolTable getGlobalTable(){
        return this.globalTable;
    }
    
    public SymbolTable getLocalTable(){
        return this.localTable;
    }
    
    public SymbolTable getConstantTable(){
        return this.constantTable;
    }
    
    public Quadruples getQuads(){
        return this.quads;
    }
    
    
    
    
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////constructors////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    
    /**
     * Constructs a new SemanticActions object
     * to be used for a parse.
     */
    public SemanticActions() {
        quads = new Quadruples();
        semanticStack = new Stack<Object>();
//		quads = new Quadruples();
        insert = false;
        isArray = false;
//		isParm = false;
        global = true;
        globalMemory = 0;
        localMemory = 0;
        globalStore = 0;
        localStore = 0;
        tempCount = 0;
        tableSize = 37;
        globalTable = new SymbolTable(tableSize);
        localTable = new SymbolTable(tableSize);
        constantTable = new SymbolTable(tableSize);
        currentFunction = null;
        InstallBuiltins(globalTable);
        parmCount = new Stack<Integer>();
        nextParm = new NextParm();
        
        String[] CODE = {"CODE"};
        
        this.quads.addQuad(CODE);
        this.quads.incrementNextQuad();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////methods/////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    
    public String tempPrefix(){
        return this.tempCount.toString();
    }
    
    /**
     * Installs a set of built-in functions by inserting
     * them into the input table.
     * @param table a symbol table to be initialized.
     */
    public final void InstallBuiltins(SymbolTable table) {
        //make new entries
        ProcedureEntry read = new ProcedureEntry("read");
        ProcedureEntry write = new ProcedureEntry("write");
        ProcedureEntry main = new ProcedureEntry("main");
        
        //set reserved
        read.setReserved();
        write.setReserved();
        main.setReserved();
        
        //add to table
        table.insert(read);
        table.insert(write);
        table.insert(main);
    }

    /**
     * Prints out all objects in the 
     * semantic stack in a readable format.
     */
    public void dump() {
        //go until stack is empty
        while (!this.semanticStack.empty()) {
            //top of stack is a Token
            if (this.semanticStack.peek() instanceof Token) {
                Token thisTok = (Token) this.semanticStack.peek();
                System.out.print("Token:  " + thisTok.getType());
                //token is id or constant
                if ((thisTok.getType() == TokenType.IDENTIFIER)
                        || (thisTok.getType() == TokenType.REALCONSTANT)
                        || (thisTok.getType() == TokenType.INTCONSTANT)) {
                    System.out.print(" Value : " + thisTok.getValue());
                    //tpken is operation
                } else if ((thisTok.getType() == TokenType.RELOP)
                        || (thisTok.getType() == TokenType.ADDOP)
                        || (thisTok.getType() == TokenType.MULOP)) {
                    System.out.print(" OpType : " + thisTok.getOpType());
                }
                System.out.println();
                //top of stack is not a token
            } else {
                //quad maybe?
            }
            this.semanticStack.pop();
        }
    }
    
    //////////////////////////////findEntry/////////////////////////////////////
    public SymbolTableEntry findEntry(String name){
        if (this.global){
            return this.globalTable.getEntry(name);
        }
        else {
            if (this.localTable.lookup(name)){
                return this.localTable.getEntry(name);
            }
            else return this.globalTable.getEntry(name);
        }
    }
    
    
    
    ////////////////////////////isnulloffset////////////////////////////////////
    public boolean isNullOffset(Object o){
        boolean bool = false;
        if(o instanceof SymbolTableEntry){
            SymbolTableEntry entry = (SymbolTableEntry) o;
            if (entry.getName().equals("__NULLOFFSET")){
                bool = true;
            }
        }
        return bool;
    }

    ////////////////////////////////makeList////////////////////////////////////
    public LinkedList makeList(int eVal) {
        LinkedList list = new LinkedList();
        list.add(eVal);
        return list;
    }
    
    /////////////////////////////////merge//////////////////////////////////////
    public LinkedList merge(LinkedList eList1, LinkedList eList2){
        LinkedList newList = new LinkedList();
        for(Object i : eList1){
            newList.add(i);
        }
        for(Object j : eList2){
            newList.add(j);
        }
        return newList;
    }
    
    ////////////////////////////////getTable////////////////////////////////////
    public SymbolTable getTable(){
        if(global){
            return globalTable;
        }
        else{
            return localTable;
        }
    }
    
    
    ///////////////////////////////////parmCheck///////////////////////////////
    
    public boolean parmTypeCheck(TokenType typ1, TokenType typ2){
        TokenType type1 = typ1;
        TokenType type2 = typ2;
        
        if (typ1 == TokenType.INTCONSTANT){
            type1 = TokenType.INTEGER;
        }
        if (typ2 == TokenType.INTCONSTANT){
            type2 = TokenType.INTEGER;
        }
        if (typ1 == TokenType.REALCONSTANT){
            type1 = TokenType.REAL;
        }
        if (typ2 == TokenType.REALCONSTANT){
            type2 = TokenType.REAL;
        }
        return type1 == type2;
    }
    
    
    //////////////////////////////////bottomStack///////////////////////////////
    
    public Object bottomStack(){
        return semanticStack.toArray()[0];
    }
    
    
    //////////////////////////////bottomStackArray//////////////////////////////
    
    public Object bottomStackArray(){
        Object[] stackArray = this.semanticStack.toArray();
        Object entry = null;
        for(int i = 0; i <stackArray.length; i++){
            if(stackArray[i] instanceof ArrayEntry){
                entry = stackArray[i];
                break;
            }
        }
        return entry;
    }
    
    
    /////////////////////////////////procOrFun//////////////////////////////////
    public Object procOrFun(){
        Object[] stackArray = this.semanticStack.toArray();
        Object entry = null;
        for(int i = 0; i <stackArray.length; i++){
            if(stackArray[i] instanceof ProcedureEntry || stackArray[i] instanceof FunctionEntry){
                entry = stackArray[i];
                break;
            }
        }
        return entry;
    }
    
    
    /////////////////////////////////BackPatch//////////////////////////////////
    public void backPatch(int p, int i){
        Integer newField = new Integer(i);
        String[] thisQuad = quads.getQuad(p);
        int quadLength = thisQuad.length;
        for(int j = 1; j < quadLength; j++){
            if (thisQuad[j].equals("_")){
                quads.setField(p, j, newField.toString());
            }
        }
    }

    public void backPatch(LinkedList ll, int i) {
        Integer newField = new Integer(i);
        for (Object op : ll) {
            int p = (int) op;
            String[] thisQuad = quads.getQuad(p);
            int quadLength = thisQuad.length;
            for (int j = 1; j < quadLength; j++) {
                if (thisQuad[j].equals("_")) {
                    quads.setField(p, j, newField.toString());
                }
            }
        }
    }

    ///////////////////////////////typecheck////////////////////////////////////
    public int typeCheck(SymbolTableEntry id1, SymbolTableEntry id2){
        
        TokenType type1 = id1.getType();
        TokenType type2 = id2.getType();
        
        if(type1 == TokenType.INTCONSTANT){
            type1 = TokenType.INTEGER;
        }
        if(type1 == TokenType.REALCONSTANT){
            type1 = TokenType.REAL;
        }
        if(type2 == TokenType.INTCONSTANT){
            type2 = TokenType.INTEGER;
        }
        if(type2 == TokenType.REALCONSTANT){
            type2 = TokenType.REAL;
        }
        
        if (type1 == type2){
            if (type1 == TokenType.INTEGER){
                return 0;
            }
            else return 1;
        }
        else{
            if (type1 == TokenType.INTEGER){
                return 3;
            }
            else{
                return 2;
            }
        }
    }
    
    public int typeCheck(Token id1, Token id2){
        TokenType type1 = id1.getType();
        TokenType type2 = id2.getType();
        
        if(type1 == TokenType.INTCONSTANT){
            type1 = TokenType.INTEGER;
        }
        if(type1 == TokenType.REALCONSTANT){
            type1 = TokenType.REAL;
        }
        if(type2 == TokenType.INTCONSTANT){
            type2 = TokenType.INTEGER;
        }
        if(type2 == TokenType.REALCONSTANT){
            type2 = TokenType.REAL;
        }
        
        if (type1 == type2){
            if (type1 == TokenType.INTEGER){
                return 0;
            }
            else return 1;
        }
        else{
            if (type1 == TokenType.INTEGER){
                return 3;
            } else {
                return 2;
            }
        }
    }

    /////////////////////////////////create/////////////////////////////////////
    public Integer create(String name, TokenType type) {
        String newName = "$$" + name;
        VariableEntry newEntry = new VariableEntry(newName, type);
        if (global) {
            newEntry.setAddress(globalMemory);
            globalTable.insert(newEntry);
            globalMemory++;
        } else {
            newEntry.setAddress(localMemory);
            localTable.insert(newEntry);
            localMemory++;
        }
        return new Integer(newEntry.getAddress());
    }

    ///////////////////////////////////Help for gen/////////////////////////////
    public String getStringPrefix(SymbolTableEntry entry, String tvi) {
        String finalString = "ERROR_";
        String paramString = "ERROR_";
        String globalLocalString = "ERROR_";

        if (tvi.equals("param")) {
            if (entry.getIsParm()) {
                paramString = "";
            }
            else {
                paramString = "@";
            }
        } else {
            if (entry.getIsParm()) {
                paramString = "^";
            }
            else {
                paramString = "";
            }
        }

        if (global) {
            if (this.globalTable.lookup(entry.getName())) {
                globalLocalString = "_";
            }
        } else {
            if (this.localTable.lookup(entry.getName())) {
                globalLocalString = "%";
            } else if (this.globalTable.getEntry(entry.getName()) != null) {
                globalLocalString = "_";
            }
        }

        finalString = paramString + globalLocalString;

        return finalString;
    }

    public String getEntryString(SymbolTableEntry entry, String tvi) {
        String finalString = "";
        if (entry.isFunction() || entry.isProcedure()) {
            finalString = finalString + entry.getName();
        } else {
            if (entry.isConstant()) {
                this.tempCount++;
                String tempName = this.tempPrefix() + "TEMP";
                String $$tempName = "$$" + tempName;
                
                create(tempName, entry.getType());
                gen("move", entry.getName(), this.getTable().getEntry($$tempName));
                Integer addr = new Integer(this.getTable().getEntry($$tempName).getAddress());
                String addrString = addr.toString();
                finalString = finalString + this.getStringPrefix(this.getTable().getEntry($$tempName), tvi) + addrString;
            } else {
                Integer addr = new Integer(entry.getAddress());
                String addrString = addr.toString();
                finalString = finalString + this.getStringPrefix(entry, tvi) + addrString;
            }
        }
        return finalString;
    }

    ////////////////////////////////////GEN/////////////////////////////////////
    
    
    //TVI codes of lenth 1
    public void gen(String tvi) {
        String[] finalTVI = {tvi};
        //add quadruple to quads
        this.quads.addQuad(finalTVI);
        this.quads.incrementNextQuad();
    }
    
    //TVI codes of length 2
    public void gen(String tvi, String op1) {

        String[] finalTVI = {tvi, op1};
        
        //add quadruple to quads
        this.quads.addQuad(finalTVI);
        this.quads.incrementNextQuad();
    }
    
    //gen string, entry
    public void gen(String tvi, SymbolTableEntry op1) {

        String newOp1 = getEntryString(op1, tvi);
        String[] finalTVI = {tvi, newOp1};
        
        //add quadruple to quads
        this.quads.addQuad(finalTVI);
        this.quads.incrementNextQuad();
    }
    
    
    //gen for string, symTabEnt, int
    public void gen(String tvi, SymbolTableEntry op1, int op2){
        Integer p = new Integer(op2);
        String pString = p.toString();
        String newOp1 = this.getEntryString(op1, tvi);
        String[] newQuad = {tvi, newOp1, pString};
        quads.addQuad(newQuad);
        quads.incrementNextQuad();
    }
    
    //gen for string, string, enry
    public void gen(String tvi, String op1, SymbolTableEntry op2){
        
        String newOp2 = this.getEntryString(op2, tvi);
        
        String[] newQuad = {tvi, op1, newOp2};
        quads.addQuad(newQuad);
        quads.incrementNextQuad();
    }
    
    
    
    //gen for string, entry, entry, entry
    public void gen(String tvi, SymbolTableEntry op1, SymbolTableEntry op2, SymbolTableEntry op3){
        
        String aString1 = this.getEntryString(op1, tvi);
        String aString2 = this.getEntryString(op2, tvi);
        String aString3 = this.getEntryString(op3, tvi);
        
        String[] newQuad = 
        {tvi, aString1, aString2, aString3};
        
        quads.addQuad(newQuad);
        quads.incrementNextQuad();
    }
    
    //gen for string, entry, entry, int
    public void gen(String tvi, SymbolTableEntry op1, SymbolTableEntry op2, int op3){
        Integer address3 = new Integer(op3);
        
        String aString1 = this.getEntryString(op1, tvi);
        String aString2 = this.getEntryString(op2, tvi);
        String aString3 = address3.toString();
        
        String[] newQuad = 
        {tvi, aString1, aString2, aString3};
        
        quads.addQuad(newQuad);
        quads.incrementNextQuad();
    }
    
    //gen for string, entry, entry, string
    public void gen(String tvi, SymbolTableEntry op1, SymbolTableEntry op2, String op3){
        
        String aString1 = this.getEntryString(op1, tvi);
        String aString2 = this.getEntryString(op2, tvi);
        String aString3 = op3;
        
        String[] newQuad = 
        {tvi, aString1, aString2, aString3};
        
        quads.addQuad(newQuad);
        quads.incrementNextQuad();
    }
    
   
    
    //gen for string, entry, entry
    public void gen(String tvi, SymbolTableEntry op1, SymbolTableEntry op2){
        
        String aString1 = getEntryString(op1, tvi);
        String aString2 = getEntryString(op2, tvi);
        
        String[] newQuad = 
        {tvi, aString1, aString2};
        
        quads.addQuad(newQuad);
        quads.incrementNextQuad();
    }
    
    /////////////////////////////////dumpStack/////////////////////////////////
    public void dumpStack(){
        Stack stackObj = semanticStack;
        String finalString = "";
        for(Object o : stackObj){
            String objString = "";
            if(o == null) objString = "null";
            else objString = o.toString();
            if(o instanceof Token){
                Token temp = (Token) o;
                objString = ("TOKEN: type = "  + temp.getType().name()+ " value = " + temp.getValue());
            }
            else if (o instanceof SymbolTableEntry){
                SymbolTableEntry temp = (SymbolTableEntry) o;
                if (temp.isArray()){
                    objString = ("ARRAY_ENTRY: name = " + temp.getName());
                }
                else if (temp.isConstant()){
                    objString = ("CONSTANT_ENTRY: name = " + temp.getName());
                }
                else if (temp.isFunction()){
                    objString = ("FUNCTION_ENTRY: name = " + temp.getName());
                }
                else if (temp.isProcedure()){
                    objString = ("PROCEDURE_ENTRY: name = " + temp.getName());
                }
                else if (temp.isVariable()){
                    objString = ("VARIABLE_ENTRY: name = " + temp.getName());
                }
                else objString = ("ENTRY: name = " + temp.getName());
            }
            else if (o instanceof ETYPE){
                ETYPE temp = (ETYPE) o;
                objString = ("ETYPE: " + temp.name());
            }
            finalString = finalString + ("| " + objString + " ");
        }
        System.out.println();
        finalString = finalString + "|";
        for(int i = 0; i < finalString.length(); i++) System.out.print("=");
        System.out.println();
        System.out.println(finalString);
        for(int i = 0; i < finalString.length(); i++) System.out.print("=");
        System.out.println();
        System.out.println();
    }

    ////////////////////////////////////51READ//////////////////////////////////
    public void action51read(Token token) {
        //System.out.println("calling 51READ");
        
        if (semanticStack.peek() instanceof SymbolTableEntry) {
            SymbolTableEntry id = (SymbolTableEntry) this.semanticStack.peek();
            Stack<SymbolTableEntry> newStack = new Stack<SymbolTableEntry>();
            while (true) {
                newStack.push(id);
                this.semanticStack.pop();
                if (this.semanticStack.peek() instanceof SymbolTableEntry) {
                    id = (SymbolTableEntry) this.semanticStack.peek();
                } else {
                    break;
                }
            }
            while (!(newStack.empty())) {
                SymbolTableEntry entry = newStack.pop();
                if (entry.getType() == TokenType.REAL) {
                    gen("finp", entry);
                } else {
                    gen("inp", entry);
                }
            }
            this.parmCount.pop();
            
            //pop ETYPE
            if (this.semanticStack.peek() instanceof ETYPE) {
                this.semanticStack.pop();
            }
            
            //pop proc entry
            ProcedureEntry proc = (ProcedureEntry) this.semanticStack.pop();
        }
    }
    
    ///////////////////////////////////51WRITE//////////////////////////////////
    public void action51write(Token token){
        //System.out.println("calling 51WRITE");
        
        if (semanticStack.peek() instanceof SymbolTableEntry) {
            SymbolTableEntry id = (SymbolTableEntry) this.semanticStack.peek();
            Stack<SymbolTableEntry> newStack = new Stack<SymbolTableEntry>();
            while (true) {
                newStack.push(id);
                this.semanticStack.pop();
                if (this.semanticStack.peek() instanceof SymbolTableEntry) {
                    id = (SymbolTableEntry) this.semanticStack.peek();
                } else {
                    break;
                }
            }
            while (!(newStack.empty())) {
                SymbolTableEntry entry = newStack.pop();
                
                if (!(entry.getName().startsWith("$$"))) {
                    String tempString = (Q + entry.getName() + " = " + Q);
                    gen("print", tempString);
                }

                if (entry.getType() == TokenType.REAL) {
                    gen("foutp", this.getTable().getEntry(entry.getName()));
                }
                else{
                    gen("outp", entry);
                }
                gen("newl");
            }
            this.parmCount.pop();
            
            //pop ETYPE
            if (this.semanticStack.peek() instanceof ETYPE) {
                this.semanticStack.pop();
            }
            
            //pop proc entry
            ProcedureEntry proc = (ProcedureEntry) this.semanticStack.pop();
        }
    }
    

    /**
     * Executes the appropriate code that corresponds with the input
     * action; execute()'s behavior depends on the current state of the
     * SemanticActions instance it is called upon, and the input token.
     * @param action action to be called; passed by parser.
     * @param token current token in input; passed by parser.
     * @throws SemanticError 
     */
    public void Execute(SemanticAction action, Token token, int line) throws SemanticError {

        int actionNumber = action.getIndex();

        //uncomment for debugging: prints out action being called along with 
        //the token type of the input token.
        //dumpStack();
        //System.out.println("Line " + line + ". Calling action : " + actionNumber + 
        //        " with token " + token.getType());
        
        switch (actionNumber) {
            
            ////////////////////////////////////////////////////////////////////
            //////////////////semantic actions start here///////////////////////
            ////////////////////////////////////////////////////////////////////
            
            case 1: {
                this.insert = true;
                break;
            }
            case 2: {
                this.insert = false;
                break;
            }

            case 3: {
                Token toke = (Token) this.semanticStack.pop();
                TokenType TYP = toke.getType();
                //array entry
                if (this.isArray) {
                    
                    Token upper = (Token) this.semanticStack.pop();
                    Token lower = (Token) this.semanticStack.pop();
                    int UB = Integer.parseInt(upper.getValue());
                    int LB = Integer.parseInt(lower.getValue());
                    int MSIZE = (UB - LB) + 1;
                    Token ID = (Token) this.semanticStack.peek();
                    //put each id in memory as array
                    while (ID.getType() == TokenType.IDENTIFIER) {
                        
                        //global entry
                        if (this.global) {
                            ArrayEntry globalEntry = new ArrayEntry(
                                    ID.getValue(),
                                    this.globalMemory,
                                    TYP,
                                    UB,
                                    LB);
                            this.globalTable.insert(globalEntry);
                            this.globalMemory = this.globalMemory + MSIZE;
                        } //
                        //local entry
                        else {
                            ArrayEntry entry = new ArrayEntry(
                                    ID.getValue(),
                                    this.localMemory,
                                    TYP,
                                    UB,
                                    LB);
                            this.localTable.insert(entry);
                            this.localMemory = this.localMemory + MSIZE;
                        }
                        this.semanticStack.pop();
                        if (this.semanticStack.empty()) break;
                        else if (this.semanticStack.peek() instanceof Token) {
                            ID = (Token) this.semanticStack.peek();
                        } else break;
                    }
                } //
                // put each id in memory as simple entry
                else {
                    Token ID = (Token) this.semanticStack.peek();
                    while (ID.getType() == TokenType.IDENTIFIER) {
                        //global entry
                        if (this.global) {
                            VariableEntry globalEntry = new VariableEntry(
                                    ID.getValue(),
                                    TYP,
                                    globalMemory);
                            this.globalTable.insert(globalEntry);
                            globalMemory++;
                        } //local entry
                        else {
                            VariableEntry entry = new VariableEntry(
                                    ID.getValue(),
                                    TYP,
                                    localMemory);
                            this.localTable.insert(entry);
                            this.localMemory = this.localMemory + 1;
                        }
                        if (this.semanticStack.empty()) {
                            break;
                        }
                        this.semanticStack.pop();
                        if (this.semanticStack.empty()) break;
                        else if (this.semanticStack.peek() instanceof Token) {
                            ID = (Token) this.semanticStack.peek();
                        } else {
                            break;
                        }
                    }
                }
                this.isArray = false;
                break;
            }
            case 4: {
                this.semanticStack.push(token);
                break;
            }
                
            case 5: {
                this.insert = false;
                SymbolTableEntry id = (SymbolTableEntry) this.semanticStack.pop();
                gen("PROCBEGIN", id);
                this.localStore = quads.getNextQuad();
                gen("alloc", "_");
                break;
            }
                
            case 6: {
                this.isArray = true;
                break;
            }

            case 7: {
                this.semanticStack.push(token);
                break;
            }
            case 9: {
                Token nextToken = (Token) this.semanticStack.peek();
                //for all id's on stack
                while (nextToken.getType() == TokenType.IDENTIFIER) {
                        this.semanticStack.pop();
                        VariableEntry newEntry = new VariableEntry(nextToken.getValue() , nextToken.getType(), globalMemory);
                        newEntry.setReserved();
                        globalTable.insert(newEntry);
                        if(semanticStack.empty()){
                            break;
                        }
                        else{
                        nextToken = (Token) this.semanticStack.peek();
                        }
                }
                this.insert = false;
                gen("call", globalTable.getEntry("main"), 0);
                gen("exit");
                break;
            }
            case 11: {
                this.global = true;
                this.localTable.clearTable();
                this.currentFunction = null;
                this.backPatch(this.localStore, this.localMemory);
                Integer mem = new Integer(localMemory);
                gen("free", mem.toString());
                gen("PROCEND");
                break;
            }
                
            case 13: {
                this.semanticStack.push(token);
                break;
            }
                
            case 15: {
                FunctionEntry id = new FunctionEntry(token.getValue());
                
                create(token.getValue(), TokenType.INTEGER);
                SymbolTableEntry funcRes = this.findEntry("$$" + token.getValue());
                funcRes.setIsFunctionResult(true);
                id.setFunctionResult(funcRes);
                this.getTable().insert(id);
                this.global = false;
                this.localMemory = 0;
                semanticStack.push(id);
                break;
            }
                
            case 16: {
                Token tok = (Token) semanticStack.pop();
                TokenType type = tok.getType();
                SymbolTableEntry id = (SymbolTableEntry) this.semanticStack.peek();
                id.setType(type);
                this.currentFunction = id;
                this.findEntry("$$" + this.currentFunction.getName()).setType(type);
                break;
            }
                
            case 17: {
                ProcedureEntry id = new ProcedureEntry(token.getValue());
                this.getTable().insert(id);
                this.semanticStack.push(id);
                this.global = false;
                this.localMemory = 0;
                break;
            }
                
            case 19: {
                this.parmCount.push(0);
                break;
            }
                
            case 20: {
                SymbolTableEntry id = (SymbolTableEntry) this.semanticStack.peek();
                int count = this.parmCount.pop();
                id.setNumberOfParams(count);
                break;
            }
                
            case 21: {
                Token tok = (Token)this.semanticStack.pop();
                TokenType type = tok.getType();
                int count = this.parmCount.pop();
                int ub = ERROR;
                int lb = ERROR;
                if (this.isArray){
                    Token uBound = (Token) semanticStack.pop();
                    Token lBound = (Token) semanticStack.pop();
                    ub = Integer.parseInt(uBound.getValue());
                    lb = Integer.parseInt(lBound.getValue());
                }
                
                LinkedList<ParmInfo> info = new LinkedList();
                
                //for each id (parameter) on stack:
                if (this.semanticStack.peek() instanceof Token) {
                    Token id = (Token) this.semanticStack.peek();
                    while (id.getType() == TokenType.IDENTIFIER){
                        ParmInfo thisParm = new ParmInfo();
                        
                        if(this.isArray){
                            ArrayEntry entry = new ArrayEntry(id.getValue(), type);
                            entry.setIsParm(true);
                            entry.setUpperBound(ub);
                            entry.setLowerBound(lb);
                            thisParm.setUpperBound(ub);
                            thisParm.setLowerBound(lb);
                            thisParm.setArray(true);
                            this.getTable().insert(entry);
                        }
                        
                        else{
                            VariableEntry entry = new VariableEntry(id.getValue(), type);
                            entry.setIsParm(true);
                            thisParm.setArray(false);
                            this.getTable().insert(entry);
                        }
                        
                        SymbolTableEntry entry = this.findEntry(id.getValue());
                        entry.setAddress(this.localMemory);
                        this.localMemory++;
                        entry.setType(type);
                        thisParm.setType(type);
                        count++;
                        
                        info.add(thisParm);
                        
                        this.semanticStack.pop();
                        if (this.semanticStack.peek() instanceof Token){
                            id = (Token) this.semanticStack.peek();
                        }
                        else break;
                    }
                    this.isArray = false;
                }
                SymbolTableEntry thisProc = (SymbolTableEntry) this.semanticStack.peek();
                thisProc.addParaminfo(info);
                this.parmCount.push(count);
                break;
            }
                
            case 22: {
                ETYPE eType = (ETYPE) semanticStack.pop();
                if(eType != ETYPE.RELATIONAL){
                    throw SemanticError.relExpError(line);
                }
                LinkedList efalse = (LinkedList) semanticStack.pop();
                LinkedList etrue = (LinkedList) semanticStack.peek();
                this.semanticStack.push(efalse);
                backPatch(etrue, quads.getNextQuad());
                break;
            }
            case 24: {
                //: push BEGINLOOP
                semanticStack.push(quads.getNextQuad());
                break;
            }
            case 25: {
                ETYPE etype = (ETYPE) semanticStack.pop();
                if(etype != etype.RELATIONAL){
                    throw SemanticError.relExpError(line);
                }
                LinkedList efalse = (LinkedList) semanticStack.pop();
                LinkedList etrue = (LinkedList) semanticStack.peek();
                semanticStack.push(efalse);
                
                backPatch(etrue, quads.getNextQuad());
                break;
            }
            case 26: {
                LinkedList efalse = (LinkedList) semanticStack.pop();
                //pop etrue
                semanticStack.pop();
                
                int beginloop = (int) semanticStack.pop();
                
                Integer bl = new Integer(beginloop);
                
                gen("goto", bl.toString());
                backPatch(efalse, quads.getNextQuad());
                break;
            }
            case 27: {
                LinkedList skipElse = makeList(quads.getNextQuad());
                gen("goto", "_");
                
                LinkedList efalse = (LinkedList) semanticStack.peek();
                backPatch(efalse, quads.getNextQuad());
                semanticStack.push(skipElse);
                break;
            }
            case 28: {
                LinkedList skipElse = (LinkedList) semanticStack.pop();
                semanticStack.pop();
                semanticStack.pop();
                
                backPatch(skipElse, quads.getNextQuad());
                break;
            }
            case 29: {
                LinkedList efalse = (LinkedList) semanticStack.pop();
                semanticStack.pop();
                
                backPatch(efalse, quads.getNextQuad());
                break;
            }
            case 30: {
                if (this.findEntry(token.getValue()) != null) {
                    semanticStack.push(this.findEntry(token.getValue()));
                } else {
                    //identifier not defined
                    throw SemanticError.undeclaredVariable(token.getValue(), line);
                }
                semanticStack.push(ETYPE.ARITHMATIC);
                break;
            }
            case 31: {
                ETYPE eType1 = (ETYPE) semanticStack.pop();
                SymbolTableEntry id2 = (SymbolTableEntry) semanticStack.pop();
                SymbolTableEntry offset = (SymbolTableEntry) semanticStack.pop();
                if(semanticStack.peek() instanceof ETYPE){
                    semanticStack.pop();
                }
                SymbolTableEntry id1 = (SymbolTableEntry) semanticStack.pop();
                
                
                if(eType1 != ETYPE.ARITHMATIC){
                    throw SemanticError.arExpError(line);
                }

                int check = typeCheck(id1, id2);

                if (check == 3) {
                    this.tempCount++;
                    String tempName = this.tempPrefix() + "TEMP";
                    String $$tempName = "$$" + tempName;

                    create(tempName, TokenType.INTEGER);
                    gen("ftol", id2, this.getTable().getEntry($$tempName));
                    if(offset.getName().equals("__NULLOFFSET")){
                        gen("move", this.getTable().getEntry($$tempName), id1);
                    }
                    else{
                       gen("stor", this.getTable().getEntry($$tempName), offset, id1); 
                    }
                }
                if(check == 2) {
                    this.tempCount++;
                    String tempName = this.tempPrefix() + "TEMP";
                    String $$tempName = "$$" + tempName;
                    
                    create(tempName, TokenType.REAL);
                    gen("ltof", id2, this.getTable().getEntry($$tempName));
                    if(offset.getName().equals("__NULLOFFSET")){
                        gen("move", this.getTable().getEntry($$tempName), id1);
                    }
                    else{
                       gen("stor",this.getTable().getEntry($$tempName), offset, id1); 
                    }
                }
                else {
                    if (offset.getName().equals("__NULLOFFSET")){
                        gen("move", id2, id1);
                    }
                    else{
                        gen("stor", id2, offset, id1);
                    }
                }
                break;
            }
            case 32: {
                ETYPE eType = (ETYPE) semanticStack.pop();
                if(eType != ETYPE.ARITHMATIC){
                    throw SemanticError.arExpError(line);
                }
                if(semanticStack.peek() instanceof SymbolTableEntry){
                    SymbolTableEntry entry = (SymbolTableEntry) semanticStack.peek();
                    if(!(entry.isArray())){
                        throw SemanticError.semanticMismatched(line, entry.toString(), "ARRAY");
                    }
                }
                if(semanticStack.peek() instanceof Token){
                    Token tok = (Token) semanticStack.peek();
                    SymbolTableEntry entry = this.findEntry(tok.getValue());
                    if(!(entry.isArray())){
                        throw SemanticError.semanticMismatched(line, tok.toString(), "ARRAY");
                    }
                }
                break;
            }
            case 33: {
                ETYPE eType = (ETYPE) semanticStack.pop();
                if (eType != ETYPE.ARITHMATIC) {
                    throw SemanticError.arExpError(line);
                }
                SymbolTableEntry entry = (SymbolTableEntry) semanticStack.pop();
                if (entry.getType() != TokenType.INTEGER && entry.getType() != TokenType.INTCONSTANT) {
                    if (entry.getType() == TokenType.REAL || entry.getType() == TokenType.REALCONSTANT) {

                        this.tempCount++;
                        String tempName1 = this.tempPrefix() + "TEMP1";
                        String $$tempName1 = "$$" + tempName1;
                        
                        this.tempCount++;
                        String tempName2 = this.tempPrefix() + "TEMP2";
                        String $$tempName2 = "$$" + tempName2;


                        //convert entry from real to int
                        create(tempName1, TokenType.INTEGER);
                        gen("ftol", entry, this.getTable().getEntry($$tempName1));
                        
                        create(tempName2, TokenType.INTEGER);
                        ArrayEntry arrayName = (ArrayEntry) this.bottomStackArray();
                        Integer lBound = new Integer(arrayName.getLowerBound());
                        ConstantEntry lBoundEntry = new ConstantEntry(lBound.toString(), TokenType.INTCONSTANT);
                        this.constantTable.insert(lBoundEntry);
                        gen("sub", this.getTable().getEntry($$tempName1), lBoundEntry, this.getTable().getEntry($$tempName2));
                        semanticStack.push(this.getTable().getEntry($$tempName2));
                    } else {
                        throw SemanticError.semanticMismatched(line, entry.toString(), "INTEGER");
                    }
                } else {
                    this.tempCount++;
                    String tempName = this.tempPrefix() + "TEMP";
                    String $$tempName = "$$" + tempName;

                    create(tempName, TokenType.INTEGER);
                    ArrayEntry arrayName = (ArrayEntry) this.bottomStackArray();
                    Integer lBound = new Integer(arrayName.getLowerBound());
                    ConstantEntry lBoundEntry = new ConstantEntry(lBound.toString(), TokenType.INTCONSTANT);
                    this.constantTable.insert(lBoundEntry);
                    gen("sub", entry, lBoundEntry, this.getTable().getEntry($$tempName));
                    semanticStack.push(this.getTable().getEntry($$tempName));
                }
                
                break;
            }
            case 34: {
                //pop ETYPE
                ETYPE etype = (ETYPE) this.semanticStack.pop();
                
                if (semanticStack.peek() instanceof SymbolTableEntry) {
                    SymbolTableEntry entry = (SymbolTableEntry) semanticStack.peek();
                    if (entry.isFunction()) {
                        //System.out.println("CALLING ACTION 52 from ACTION 34 WITH TOKEN: " + token.getValue());
                        this.semanticStack.push(etype);
                        this.Execute(SemanticAction.action52, token, line);
                    } else {
                        semanticStack.push(new VariableEntry("__NULLOFFSET"));
                    }
                } else {
                    semanticStack.push(new VariableEntry("__NULLOFFSET"));
                }
                break;
            }
                
            case 35: {
                this.parmCount.push(0);
                ETYPE etype = (ETYPE) this.semanticStack.pop();
                ProcedureEntry entry = (ProcedureEntry) this.semanticStack.peek();
                this.semanticStack.push(etype);
                this.nextParm.push(entry.getParameterInfo());
                break;
            }
                
            case 36: {
                if (semanticStack.peek() instanceof ETYPE) {
                    this.semanticStack.pop();
                }
                ProcedureEntry id = (ProcedureEntry) this.semanticStack.pop();
                if(id.getNumberOfParameters() != 0){
                    throw SemanticError.alreadyDefined(line, id.toString());
                }
                gen("call", id, 0);
                break;
            }
                
            case 37: {
                ETYPE etype = (ETYPE) this.semanticStack.pop();
                if(etype != ETYPE.ARITHMATIC){
                    throw SemanticError.arExpError(line);
                }
                
                if (this.semanticStack.peek() instanceof ETYPE){
                    this.semanticStack.pop();
                }
                
                SymbolTableEntry id = (SymbolTableEntry) this.semanticStack.peek();
                if(!(id.isVariable() || id.isConstant() || id.isFunctionResult() || id.isArray())){
                    throw SemanticError.semanticMismatched(id.toString(), "VALUE");
                }
                int newCount = this.parmCount.pop();
                this.parmCount.push(newCount + 1);
                SymbolTableEntry procOrFun = (SymbolTableEntry) this.procOrFun();
                if(!(procOrFun.getName().equals("read") || procOrFun.getName().equals("write"))){
                    if(this.parmCount.peek() > procOrFun.getNumberOfParams()){
                        throw SemanticError.wrongNumberOfParams(line);
                    }
                    ParmInfo info = nextParm.getNextParm();
                    if(!(this.parmTypeCheck(id.getType(), info.getType()))){
                        throw SemanticError.typeMismatch(line, id.getType().toString(), info.getType().toString());
                    }
                    if(info.getArray()){
                        ArrayEntry ArrayID = (ArrayEntry) this.bottomStackArray();
                        if(ArrayID.getLowerBound() != info.getLowerBound()){
                            throw SemanticError.arrayBoundMismatch(line, ArrayID.getLowerBound(), info.getLowerBound());
                        }
                        if(ArrayID.getUpperBound() != info.getUpperBound()) {
                            throw SemanticError.arrayBoundMismatch(line, ArrayID.getUpperBound(), info.getUpperBound());
                        }
                    }
                    nextParm.increment();
                }
                break;
            }

            case 38: {
                ETYPE eType = (ETYPE) semanticStack.pop();
                if (eType != ETYPE.ARITHMATIC) {
                    throw SemanticError.arExpError(line);
                }
                //push token
                this.semanticStack.push(token);
                break;
            }
            case 39: {
                ETYPE etype = (ETYPE) semanticStack.pop();
                if(etype != ETYPE.ARITHMATIC){
                    throw SemanticError.arExpError(line);
                }
                SymbolTableEntry id2 = (SymbolTableEntry) semanticStack.pop();
                Token operator = (Token) semanticStack.pop();
                SymbolTableEntry id1 = (SymbolTableEntry) semanticStack.pop();
                
                int check = typeCheck(id1, id2);
                
                if(check == 2){
                    this.tempCount++;
                    String tempName = this.tempPrefix() + "TEMP";
                    String $$tempName = "$$" + tempName;
                    
                    create(tempName, TokenType.REAL);
                    gen("ltof", id2, this.getTable().getEntry($$tempName));
                    gen(operator.gettviOp(), id1, this.getTable().getEntry($$tempName), "_");
                }
                
                else if(check == 3){
                    this.tempCount++;
                    String tempName = this.tempPrefix() + "TEMP";
                    String $$tempName = "$$" + tempName;
                    
                    create(tempName, TokenType.REAL);
                    gen("ltof", id1, this.getTable().getEntry($$tempName));
                    gen(operator.gettviOp(), this.getTable().getEntry($$tempName), id2, "_");
                }
                
                else{
                    gen(operator.gettviOp(), id1, id2, "_");
                }
                gen("goto", "_");
                LinkedList etrue = makeList(quads.getNextQuad() - 2);
                LinkedList efalse = makeList(quads.getNextQuad() - 1);
                semanticStack.push(etrue);
                semanticStack.push(efalse);
                semanticStack.push(ETYPE.RELATIONAL);
                break;
            }
            case 40: {
                //push sign
                semanticStack.push(token);
                break;
            }
            
            case 41: {
                ETYPE etype = (ETYPE) this.semanticStack.pop();
                if(etype != ETYPE.ARITHMATIC){
                    throw SemanticError.arExpError(line);
                }
                
                SymbolTableEntry id = (SymbolTableEntry) this.semanticStack.pop();
                Token sign = (Token) this.semanticStack.pop();
                
                if (sign.getType() == TokenType.UNARYMINUS){
                    
                    this.tempCount++;
                    String tempName = this.tempPrefix() + "TEMP";
                    String $$tempName = "$$" + tempName;
                    
                    create(tempName, id.getType());
                    //System.out.println(id.getType());
                    gen("uminus", id, this.findEntry($$tempName));
                    this.semanticStack.push(this.findEntry($$tempName));
                }
                else{
                    this.semanticStack.push(id);
                }
                
                this.semanticStack.push(ETYPE.ARITHMATIC);
                break;
            }    
            
            case 42: {
                if (token.getOpType().equals("OR")) {
                    ETYPE eType = (ETYPE) semanticStack.pop();
                    if (eType != ETYPE.RELATIONAL) {
                        throw SemanticError.relExpError(line);
                    }
                    LinkedList eFalse = (LinkedList) semanticStack.peek();
                    backPatch(eFalse, quads.getNextQuad());
                } else {
                    ETYPE eType = (ETYPE) semanticStack.pop();
                    if (eType != ETYPE.ARITHMATIC) {
                        throw SemanticError.arExpError(line);
                    }
                }
                //push operator
                semanticStack.push(token);
                break;
            }
            case 43: {
                ETYPE eType = (ETYPE) semanticStack.pop();
                if (eType == ETYPE.RELATIONAL) {


                    LinkedList e2False = (LinkedList) semanticStack.pop();
                    LinkedList e2True = (LinkedList) semanticStack.pop();
                    Token operator = (Token) semanticStack.pop();
                    if (operator.getOpType().equals("OR")) {
                        LinkedList e1False = (LinkedList) semanticStack.pop();
                        LinkedList e1True = (LinkedList) semanticStack.pop();


                        LinkedList eTrue = merge(e1True, e2True);
                        LinkedList eFalse = e2False;

                        semanticStack.push(eTrue);
                        semanticStack.push(eFalse);
                        semanticStack.push(ETYPE.RELATIONAL);
                    }
                    else{
                        this.semanticStack.push(operator);
                        this.semanticStack.push(e2True);
                        this.semanticStack.push(e2False);
                    }
                }
                else if (eType == ETYPE.ARITHMATIC) {
                    SymbolTableEntry id2 = (SymbolTableEntry) semanticStack.pop();
                    Token op = (Token) semanticStack.pop();
                    SymbolTableEntry id1 = (SymbolTableEntry) semanticStack.pop();


                    if (global) {
                        int check = typeCheck(id1, id2);
                        switch (check) {
                            case 0: {
                                this.tempCount++;
                                String tempName = this.tempPrefix() + "TEMP";
                                String $$tempName = "$$" + tempName;

                                create(tempName, TokenType.INTEGER);
                                gen(op.gettviOp(),
                                        id1,
                                        id2,
                                        globalTable.getEntry($$tempName));
                                semanticStack.push(globalTable.getEntry($$tempName));
                                break;
                            }
                            case 1: {
                                this.tempCount++;
                                String tempName = this.tempPrefix() + "TEMP";
                                String $$tempName = "$$" + tempName;
                                
                                create(tempName, TokenType.REAL);
                                gen(("f" + op.gettviOp()),
                                        id1,
                                        id2,
                                        globalTable.getEntry($$tempName));
                                semanticStack.push(globalTable.getEntry($$tempName));
                                break;
                            }
                            case 2: {
                                this.tempCount++;
                                String tempName1 = this.tempPrefix() + "TEMP1";
                                String $$tempName1 = "$$" + tempName1;
                                
                                this.tempCount++;
                                String tempName2 = this.tempPrefix() + "TEMP2";
                                String $$tempName2 = "$$" + tempName2;
                                
                                create(tempName1, TokenType.REAL);
                                gen("ltof",
                                        id2,
                                        globalTable.getEntry($$tempName1));
                                create(tempName2, TokenType.REAL);
                                gen(("f" + op.gettviOp()),
                                        id1,
                                        globalTable.getEntry($$tempName1),
                                        globalTable.getEntry($$tempName2));
                                semanticStack.push(globalTable.getEntry($$tempName2));
                                break;
                            }
                            case 3: {
                                this.tempCount++;
                                String tempName1 = this.tempPrefix() + "TEMP1";
                                String $$tempName1 = "$$" + tempName1;
                                
                                this.tempCount++;
                                String tempName2 = this.tempPrefix() + "TEMP2";
                                String $$tempName2 = "$$" + tempName2;
                                
                                create(tempName1, TokenType.REAL);
                                gen("ltof",
                                        id1,
                                        globalTable.getEntry($$tempName1));
                                create(tempName2, TokenType.REAL);
                                gen(("f" + op.gettviOp()),
                                        globalTable.getEntry($$tempName1),
                                        id2,
                                        globalTable.getEntry($$tempName2));
                                semanticStack.push(globalTable.getEntry($$tempName2));
                                break;
                            }
                        }
                    } ///local
                    else {
                        int check = typeCheck(id1, id2);
                        switch (check) {
                            case 0: {
                                this.tempCount++;
                                String tempName = this.tempPrefix() + "TEMP";
                                String $$tempName = "$$" + tempName;
                                
                                create(tempName, TokenType.INTEGER);
                                gen(op.gettviOp(),
                                        id1,
                                        id2,
                                        localTable.getEntry($$tempName));
                                semanticStack.push(localTable.getEntry($$tempName));
                                break;
                            }
                            case 1: {
                                this.tempCount++;
                                String tempName = this.tempPrefix() + "TEMP";
                                String $$tempName = "$$" + tempName;
                                
                                create(tempName, TokenType.REAL);
                                gen(("f" + op.gettviOp()),
                                        id1,
                                        id2,
                                        localTable.getEntry($$tempName));
                                semanticStack.push(localTable.getEntry($$tempName));
                                break;
                            }
                            case 2: {
                                this.tempCount++;
                                String tempName1 = this.tempPrefix() + "TEMP1";
                                String $$tempName1 = "$$" + tempName1;
                                
                                this.tempCount++;
                                String tempName2 = this.tempPrefix() + "TEMP2";
                                String $$tempName2 = "$$" + tempName2;
                                
                                create(tempName1, TokenType.REAL);
                                gen("ltof",
                                        id2,
                                        localTable.getEntry($$tempName1));
                                create(tempName2, TokenType.REAL);
                                gen(("f" + op.gettviOp()),
                                        id1,
                                        localTable.getEntry($$tempName1),
                                        localTable.getEntry($$tempName2));
                                semanticStack.push(localTable.getEntry($$tempName2));
                                break;
                            }
                            case 3: {
                                this.tempCount++;
                                String tempName1 = this.tempPrefix() + "TEMP1";
                                String $$tempName1 = "$$" + tempName1;
                                
                                this.tempCount++;
                                String tempName2 = this.tempPrefix() + "TEMP2";
                                String $$tempName2 = "$$" + tempName2;
                                
                                create(tempName1, TokenType.REAL);
                                gen("ltof",
                                        id1,
                                        localTable.getEntry($$tempName1));
                                create(tempName2, TokenType.REAL);
                                gen(("f" + op.gettviOp()),
                                        localTable.getEntry($$tempName1),
                                        id2,
                                        localTable.getEntry($$tempName2));
                                semanticStack.push(localTable.getEntry($$tempName2));
                                break;
                            }
                        }
                    }
                    semanticStack.push(ETYPE.ARITHMATIC);
                } else {
                    throw SemanticError.arExpError(line);
                }
                
                break;
            }
            case 44: {
                //pop ETYPE
                ETYPE eType = (ETYPE) semanticStack.pop();
                if (eType == ETYPE.RELATIONAL){
                    if(token.getOpType().equals("AND")){
                        LinkedList eFalse = (LinkedList) semanticStack.pop();
                        LinkedList eTrue = (LinkedList) semanticStack.peek();
                        this.semanticStack.push(eFalse);
                        backPatch(eTrue, quads.getNextQuad());
                    }
                }
                //push operator
                semanticStack.push(token);
                break;
            }
            case 45: {
                ETYPE eType = (ETYPE) semanticStack.pop();
                if (eType == ETYPE.RELATIONAL) {
                    
                    LinkedList e2False = (LinkedList) semanticStack.pop();
                    LinkedList e2True = (LinkedList) semanticStack.pop();
                    Token operator = (Token) semanticStack.pop();
                    LinkedList e1False = (LinkedList) semanticStack.pop();
                    LinkedList e1True = (LinkedList) semanticStack.pop();
                    
                    if (!(operator.getOpType().equals("AND"))) {
                        throw SemanticError.semanticMismatched(line, operator.toString(), "OPERATOR AND");
                    }
                    
                    LinkedList eTrue = e2True;
                    LinkedList eFalse = merge(e1False, e2False);
                    
                    semanticStack.push(eTrue);
                    semanticStack.push(eFalse);
                    semanticStack.push(ETYPE.RELATIONAL);
                } else if(eType == ETYPE.ARITHMATIC) {
                        
                        Object thing2 = semanticStack.pop();
                        Token otoken = (Token) semanticStack.pop();
                        Object thing1 = semanticStack.pop();
                        
                        
                        if(thing1 instanceof Token){
                            Token thisID = (Token) thing1;
                            SymbolTableEntry newThing = this.getTable().getEntry(thisID.getValue());
                            thing1 = newThing;
                        }
                        
                        if(thing2 instanceof Token){
                            Token thisID = (Token) thing2;
                            SymbolTableEntry newThing = this.getTable().getEntry(thisID.getValue());
                            thing2 = newThing;
                        }
                        
                        SymbolTableEntry id1 = (SymbolTableEntry)thing1;
                        SymbolTableEntry id2 = (SymbolTableEntry)thing2;
                        
                        int check = typeCheck(id1, id2);
                        
                        
                        if (check != 0 && "MOD".equals(otoken.getOpType())){
                            throw SemanticError.modProblem(line);
                        }
                        if (check == 0) {
                            
                            if (otoken.getOpType().equals("MOD")) {
                                this.tempCount++;
                                String tempName1 = this.tempPrefix() + "TEMP1";
                                String $$tempName1 = "$$" + tempName1;

                                this.tempCount++;
                                String tempName2 = this.tempPrefix() + "TEMP2";
                                String $$tempName2 = "$$" + tempName2;

                                create(tempName1, TokenType.INTEGER);
                                gen("move",
                                        id1,
                                        this.getTable().getEntry($$tempName1));
                                create(tempName2, TokenType.INTEGER);
                                gen("move",
                                        this.getTable().getEntry($$tempName1),
                                        this.getTable().getEntry($$tempName2));
                                gen("sub",
                                        this.getTable().getEntry($$tempName2),
                                        id2,
                                        this.getTable().getEntry($$tempName1));
                                gen("bge",
                                        this.getTable().getEntry($$tempName1),
                                        id2,
                                        (quads.getNextQuad() - 2));
                                semanticStack.push(this.getTable().getEntry($$tempName1));
                            }
                            else {
                                if (otoken.getOpType().equals("/")) {
                                    this.tempCount++;
                                    String tempName1 = this.tempPrefix() + "TEMP1";
                                    String $$tempName1 = "$$" + tempName1;

                                    this.tempCount++;
                                    String tempName2 = this.tempPrefix() + "TEMP2";
                                    String $$tempName2 = "$$" + tempName2;

                                    this.tempCount++;
                                    String tempName3 = this.tempPrefix() + "TEMP3";
                                    String $$tempName3 = "$$" + tempName3;
                                    
                                    create(tempName1, TokenType.REAL);
                                    gen("ltof",
                                            id1,
                                            this.getTable().getEntry($$tempName1));
                                    create(tempName2, TokenType.REAL);
                                    gen("ltof",
                                            id2,
                                            this.getTable().getEntry($$tempName2));
                                    create(tempName3, TokenType.REAL);
                                    gen("fdiv",
                                            this.getTable().getEntry($$tempName1),
                                            this.getTable().getEntry($$tempName2),
                                            this.getTable().getEntry($$tempName3));
                                    semanticStack.push(this.getTable().getEntry($$tempName3));
                                }
                                else{
                                    this.tempCount++;
                                    String tempName = this.tempPrefix() + "TEMP";
                                    String $$tempName = "$$" + tempName;
                                    
                                    create(tempName, TokenType.INTEGER);
                                    gen(otoken.gettviOp(), 
                                            id1,
                                            id2,
                                            this.getTable().getEntry($$tempName));
                                    semanticStack.push(this.getTable().getEntry($$tempName));
                                }
                            }
                        }
                        if(check == 1){
                            if (otoken.getOpType().equals("DIV")) {

                                this.tempCount++;
                                String tempName1 = this.tempPrefix() + "TEMP1";
                                String $$tempName1 = "$$" + tempName1;

                                this.tempCount++;
                                String tempName2 = this.tempPrefix() + "TEMP2";
                                String $$tempName2 = "$$" + tempName2;

                                this.tempCount++;
                                String tempName3 = this.tempPrefix() + "TEMP3";
                                String $$tempName3 = "$$" + tempName3;

                                create(tempName1, TokenType.INTEGER);
                                gen("ftol",
                                        id1,
                                        this.getTable().getEntry($$tempName1));
                                create(tempName2, TokenType.INTEGER);
                                gen("ftol",
                                        id2,
                                        this.getTable().getEntry($$tempName2));
                                create(tempName3, TokenType.INTEGER);
                                gen("div",
                                        this.getTable().getEntry($$tempName1),
                                        this.getTable().getEntry($$tempName2),
                                        this.getTable().getEntry($$tempName3));
                                semanticStack.push(this.getTable().getEntry($$tempName3));
                            }
                            else{
                                this.tempCount++;
                                String tempName = this.tempPrefix() + "TEMP";
                                String $$tempName = "$$" + tempName;
                                
                                create(tempName, TokenType.REAL);
                                gen(("f" + otoken.gettviOp()),
                                        id1,
                                        id2,
                                        this.getTable().getEntry($$tempName));
                                semanticStack.push(this.getTable().getEntry($$tempName));
                            }
                        }
                        if (check == 2){
                            if(otoken.getOpType().equals("DIV")){
                                
                                this.tempCount++;
                                String tempName1 = this.tempPrefix() + "TEMP1";
                                String $$tempName1 = "$$" + tempName1;

                                this.tempCount++;
                                String tempName2 = this.tempPrefix() + "TEMP2";
                                String $$tempName2 = "$$" + tempName2;
                                
                                create(tempName1, TokenType.INTEGER);
                                gen("ftol",
                                        id1,
                                        this.getTable().getEntry($$tempName1));
                                create(tempName2, TokenType.INTEGER);
                                gen("div",
                                        this.getTable().getEntry($$tempName1),
                                        id2,
                                        this.getTable().getEntry($$tempName2));
                                semanticStack.push(this.getTable().getEntry($$tempName2));
                            }
                            else{
                                
                                this.tempCount++;
                                String tempName1 = this.tempPrefix() + "TEMP1";
                                String $$tempName1 = "$$" + tempName1;

                                this.tempCount++;
                                String tempName2 = this.tempPrefix() + "TEMP2";
                                String $$tempName2 = "$$" + tempName2;
                                
                                create(tempName1, TokenType.REAL);
                                gen("ltof",
                                        id2,
                                        this.getTable().getEntry($$tempName1));
                                create(tempName2, TokenType.REAL);
                                gen(("f" + otoken.gettviOp()),
                                        id1,
                                        this.getTable().getEntry($$tempName1),
                                        this.getTable().getEntry($$tempName2));
                                semanticStack.push(this.getTable().getEntry($$tempName2));
                            }
                        }
                        if (check == 3) {
                            
                            if(otoken.getOpType().equals("DIV")){
                                
                                this.tempCount++;
                                String tempName1 = this.tempPrefix() + "TEMP1";
                                String $$tempName1 = "$$" + tempName1;

                                this.tempCount++;
                                String tempName2 = this.tempPrefix() + "TEMP2";
                                String $$tempName2 = "$$" + tempName2;
                                
                                create(tempName1, TokenType.INTEGER);
                                gen("ftol",
                                        id2,
                                        this.getTable().getEntry($$tempName1));
                                create(tempName2, TokenType.INTEGER);
                                gen("div",
                                        id1,
                                        this.getTable().getEntry($$tempName1),
                                        this.getTable().getEntry($$tempName2));
                                semanticStack.push(this.getTable().getEntry($$tempName2));
                            } else {
                                this.tempCount++;
                                String tempName1 = this.tempPrefix() + "TEMP1";
                                String $$tempName1 = "$$" + tempName1;

                                this.tempCount++;
                                String tempName2 = this.tempPrefix() + "TEMP2";
                                String $$tempName2 = "$$" + tempName2;
                                
                                create(tempName1, TokenType.REAL);
                                gen("ltof",
                                        id1,
                                        this.getTable().getEntry($$tempName1));
                                create(tempName2, TokenType.REAL);
                                gen(("f" + otoken.gettviOp()),
                                        this.getTable().getEntry($$tempName1),
                                        id2,
                                        this.getTable().getEntry($$tempName2));
                                semanticStack.push(this.getTable().getEntry($$tempName2));
                            }
                        }
                        semanticStack.push(ETYPE.ARITHMATIC);
                    } else {
                        throw SemanticError.arExpError(line);
                    }
                break;
            }
            case 46: {
                if (token.getType() == TokenType.IDENTIFIER) {

                    if (this.findEntry(token.getValue()) != null) {
                        semanticStack.push(this.findEntry(token.getValue()));
                    } else {
                        throw SemanticError.undeclaredVariable(token.getValue(), line);
                    }
                }
                if (token.getType() == TokenType.INTCONSTANT || token.getType() == TokenType.REALCONSTANT) {
                    if (!constantTable.lookup(token.getValue())) {
                        //constant not in table
                        ConstantEntry newEntry = new ConstantEntry(token.getValue(), token.getType());
                        constantTable.insert(newEntry);
                    }
                    semanticStack.push(constantTable.getEntry(token.getValue()));
                }
                semanticStack.push(ETYPE.ARITHMATIC);
                break;
            }
                
            case 47: {
                ETYPE etype = (ETYPE) this.semanticStack.pop();
                if (etype != ETYPE.RELATIONAL){
                    throw SemanticError.relExpError(line);
                }
                
                LinkedList efalse = (LinkedList) this.semanticStack.pop();
                LinkedList etrue = (LinkedList) this.semanticStack.pop();
                
                this.semanticStack.push(efalse);
                this.semanticStack.push(etrue);
                this.semanticStack.push(etype);
                
                break;
            }
                
            case 48: {
                if (this.semanticStack.peek() instanceof ETYPE){
                    this.semanticStack.pop();
                }
                if (this.isNullOffset(semanticStack.peek())) {
                    semanticStack.pop();
                } else {
                    SymbolTableEntry offset = (SymbolTableEntry) semanticStack.pop();

                    //pop id
                    SymbolTableEntry id = (SymbolTableEntry) semanticStack.pop();

                    if (offset.getType() != TokenType.INTEGER && offset.getType() != TokenType.INTCONSTANT) {
                        throw SemanticError.arrayAccessError(line);
                    } else {

                        this.tempCount++;
                        String tempName = this.tempPrefix() + "TEMP";
                        String $$tempName = "$$" + tempName;
                        
                        create(tempName, id.getType());
                        gen("load", id, offset, this.getTable().getEntry($$tempName));
                        semanticStack.push(this.getTable().getEntry($$tempName));
                    }
                }
                    semanticStack.push(ETYPE.ARITHMATIC);
                break;
            }

            case 49: {
                ETYPE etype = (ETYPE) this.semanticStack.pop();
                SymbolTableEntry id = (SymbolTableEntry) this.semanticStack.peek();
                this.semanticStack.push(etype);
                if (etype != ETYPE.ARITHMATIC) {
                    throw SemanticError.arExpError(line);
                }
                if (!(id.isFunction())) {
                    throw SemanticError.semanticMismatched(id.toString(), "FUNCTION");
                }
                parmCount.push(0);
                FunctionEntry funID = (FunctionEntry) id;
                this.nextParm.push(funID.getParameterInfo());
                break;
            }
                
            case 50: {
                if(this.semanticStack.peek() instanceof SymbolTableEntry){
                    SymbolTableEntry id = (SymbolTableEntry) this.semanticStack.peek();
                    Stack<SymbolTableEntry> newStack = new Stack<SymbolTableEntry>();
                    while(true){
                        newStack.push(id);
                        this.semanticStack.pop();
                        if(this.semanticStack.peek() instanceof SymbolTableEntry){
                            id = (SymbolTableEntry) this.semanticStack.peek();
                        }
                        else break;
                    }
                    while(!(newStack.empty())){
                        SymbolTableEntry tok = newStack.pop();
                        gen("param", tok);
                        this.localMemory++;
                    }
                    int count = this.parmCount.pop();
                    
                    //pop ETYPE
                    
                    ETYPE etype = (ETYPE) this.semanticStack.pop();
                    SymbolTableEntry entry = (SymbolTableEntry) this.semanticStack.pop();
                    
                    if(count > entry.getNumberOfParams()){
                        throw SemanticError.wrongNumberOfParams(line);
                    }

                    this.tempCount++;
                    String tempName = this.tempPrefix() + "TEMP";
                    String $$tempName = "$$" + tempName;

                    gen("call", entry, count);
                    this.nextParm.pop();
                    create(tempName, entry.getType());
                    SymbolTableEntry temp = this.getTable().getEntry($$tempName);
                    gen("move", entry.getFunctionResult(), temp);
                    this.semanticStack.push(temp);
                    this.semanticStack.push(ETYPE.ARITHMATIC);
                }
                break;
            }
                
            case 51: {
                ProcedureEntry entry = (ProcedureEntry) this.procOrFun();
                if(entry.getName().equals("read")){
                    action51read(token);
                } else if (entry.getName().equals("write")) {
                    action51write(token);
                } else {
                    if (this.parmCount.peek() != entry.getParameterInfo().size()) {
                        throw SemanticError.wrongNumberOfParams(line);
                    }
                    Stack<SymbolTableEntry> tempStack = new Stack();
                    if (this.semanticStack.peek() instanceof SymbolTableEntry) {
                        SymbolTableEntry id = (SymbolTableEntry) semanticStack.peek();
                        while (true) {
                            tempStack.push(id);
                            this.semanticStack.pop();
                            if (semanticStack.peek() instanceof SymbolTableEntry) {
                                id = (SymbolTableEntry) semanticStack.peek();
                            } else {
                                break;
                            }
                        }
                    }
                    while (!tempStack.empty()) {
                        SymbolTableEntry ent = tempStack.pop();
                        gen("param", ent);
                        this.localMemory++;
                    }
                    gen("call", entry, this.parmCount.pop());

                    this.nextParm.pop();
                    
                    
                    //pop ETYPE
                    if(this.semanticStack.peek() instanceof ETYPE){
                        this.semanticStack.pop();
                    }
                    
                    
                    
                    //pop procedureEntry
                    //wrong?
                    this.semanticStack.pop();
                }
                break;
            }

            case 52: {
                //pop ETYPE
                this.semanticStack.pop();
                SymbolTableEntry id = (SymbolTableEntry) this.semanticStack.pop();
                if(!(id.isFunction())){
                    throw SemanticError.semanticMismatched(id.toString(), "FUNCTION");
                }
                if (id.getParameterInfo().size() > 0) {
                    throw SemanticError.alreadyDefined(line, id.toString());
                }

                this.tempCount++;
                String tempName = this.tempPrefix() + "TEMP";
                String $$tempName = "$$" + tempName;
               
                gen("call", id, 0);
                create(tempName, id.getType());
                SymbolTableEntry temp = this.getTable().getEntry($$tempName);
                gen("move", id.getFunctionResult(), temp);
                this.semanticStack.push(temp);
                semanticStack.push(new VariableEntry("__NULLOFFSET"));
                break;
            }
                
            case 53: {
                Object putThisback = semanticStack.pop();
                SymbolTableEntry entry = (SymbolTableEntry) semanticStack.peek();
                if(entry.isFunction()){
                    FunctionEntry fEntry = (FunctionEntry) semanticStack.pop();
                    semanticStack.push(fEntry.getFunctionResult());
                }
                semanticStack.push(putThisback);
                break;
            }
            case 54: {
                Object topStack = this.semanticStack.peek();
                if (this.semanticStack.peek() instanceof ETYPE) {
                    ETYPE etype = (ETYPE) this.semanticStack.pop();
                    topStack = this.semanticStack.peek();
                    this.semanticStack.push(etype);
                }
                SymbolTableEntry entry = (SymbolTableEntry) topStack;
                if (!(entry.isProcedure())) {
                    throw SemanticError.semanticMismatched(entry.toString(), "PROCEDURE");
                }
                break;
            }
            case 55: {
                backPatch(globalStore, globalMemory);
                Integer gMem = new Integer(globalMemory);
                gen("free", gMem.toString());
                gen("PROCEND");
                break;
            }
            case 56: {
                gen("PROCBEGIN", "main");
                globalStore = quads.getNextQuad();
                gen("alloc", "_");
                break;
            }
            default: {
                break;
            }
        }
    }

}
