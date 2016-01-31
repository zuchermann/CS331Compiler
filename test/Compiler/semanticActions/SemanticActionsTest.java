/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Compiler.semanticActions;

import Compiler.Lex.Token;
import Compiler.Lex.TokenType;
import Compiler.parser.SemanticAction;
import Compiler.symbolTable.SymbolTable;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author zakondak
 */
public class SemanticActionsTest {
    
    public SemanticActionsTest() {
    }

    /**
     * Test of constructor method, of class SemanticActions.
     */
    @Test
    public void testConstructor() {
        
        System.out.println();
        System.out.println("*******************************************");
        System.out.println("TEST CONSTRUCTOR");
        System.out.println("*******************************************");
        System.out.println();
        
        SemanticActions instance = new SemanticActions();
       
        boolean gt = instance.getGlobal();
        assertTrue(gt);
        
        int gm = instance.getGlobalMemory();
        assertEquals(gm, 3);
        
        boolean ins = instance.getInsert();
        assertTrue(!ins);
        
        boolean ia = instance.getIsArray();
        assertTrue(!ia);
        
        int lm = instance.getLocalMemory();
        assertEquals(lm, 0);
        
        int ts = instance.getTableSize();
        assertEquals(ts, 37);
        
        boolean emptyStack = instance.getSemanticStack().empty();
        assertTrue(emptyStack);
        
        System.out.println();
        System.out.println("-------------------");
        System.out.println("Dump global table");
        System.out.println();
        instance.getGlobalTable().dumpTable();
        System.out.println("-------------------");
        System.out.println();
        
        System.out.println();
        System.out.println("-------------------");
        System.out.println("Dump constant table");
        System.out.println();
        instance.getConstantTable().dumpTable();
        System.out.println("-------------------");
        System.out.println();
        
        
    }

    /**
     * Test of Action 1, of class SemanticActions.
     */
    @Test
    public void testAction1() throws Exception {
        SemanticActions instance = new SemanticActions();
        boolean ins = instance.getInsert();
        assertTrue(!ins);
        
        //make sure action doesnt modify anything else
        assertTrue(instance.getGlobal());
        assertTrue(!instance.getIsArray());
        
        instance.Execute(SemanticAction.action1, new Token(TokenType.ADDOP), 1);
        boolean ins2 = instance.getInsert();
        assertTrue(ins2);
        
        //make sure action doesnt modify anything else
        assertTrue(instance.getGlobal());
        assertTrue(!instance.getIsArray());
                
    }
    
    /**
     * Test of Action 2, of class SemanticActions.
     */
    @Test
    public void testAction2() throws Exception {
        
        SemanticActions instance = new SemanticActions();
        boolean ins = instance.getInsert();
        assertTrue(!ins);
        
        //make sure action doesnt modify anything else
        assertTrue(instance.getGlobal());
        assertTrue(!instance.getIsArray());
        
        instance.Execute(SemanticAction.action1, new Token(TokenType.ADDOP), 1);
        boolean ins2 = instance.getInsert();
        assertTrue(ins2);
        
        //make sure action doesnt modify anything else
        assertTrue(instance.getGlobal());
        assertTrue(!instance.getIsArray());
        
        instance.Execute(SemanticAction.action2, new Token(TokenType.ADDOP), 1);
        boolean ins3 = instance.getInsert();
        assertTrue(!ins3);
        
        //make sure action doesnt modify anything else
        assertTrue(instance.getGlobal());
        assertTrue(!instance.getIsArray());
        
    }
    
    /**
     * Test of Action 3, of class SemanticActions.
     */
    @Test
    public void testAction3() throws Exception {
        
        System.out.println();
        System.out.println("*******************************************");
        System.out.println("TEST ACTION 3");
        System.out.println("*******************************************");
        System.out.println();
        
        SemanticActions instance = new SemanticActions();
        
        System.out.println();
        System.out.println("__________");
        System.out.println("SIMPLE");
        System.out.println("__________");
        System.out.println();
        
        //insert as simple entry
        instance.Execute(SemanticAction.action13, new Token(TokenType.IDENTIFIER, "Hello"), 1);
        instance.Execute(SemanticAction.action13, new Token(TokenType.IDENTIFIER, "world"), 1);
        instance.Execute(SemanticAction.action4, new Token(TokenType.INTEGER), 1);
        
        
        instance.Execute(SemanticAction.action3, new Token(TokenType.DO), 1);
        
        
        System.out.println();
        System.out.println("-------------------");
        System.out.println("Dump global table");
        System.out.println();
        instance.getGlobalTable().dumpTable();
        System.out.println("-------------------");
        System.out.println();
        
        instance = new SemanticActions();
        
        System.out.println();
        System.out.println("__________");
        System.out.println("ARRAY");
        System.out.println("__________");
        System.out.println();
        
        //insert as array entry
        instance.Execute(SemanticAction.action13, new Token(TokenType.IDENTIFIER, "Hello"), 1);
        instance.Execute(SemanticAction.action13, new Token(TokenType.IDENTIFIER, "world"), 1);
        instance.Execute(SemanticAction.action13, new Token(TokenType.INTCONSTANT, "3"), 1);
        instance.Execute(SemanticAction.action13, new Token(TokenType.INTCONSTANT, "5"), 1);
        instance.Execute(SemanticAction.action4, new Token(TokenType.INTEGER), 1);
        
        instance.Execute(SemanticAction.action6, new Token(TokenType.DO), 1);
        
        instance.Execute(SemanticAction.action3, new Token(TokenType.DO), 1);
        
        
        System.out.println();
        System.out.println("-------------------");
        System.out.println("Dump global table");
        System.out.println();
        instance.getGlobalTable().dumpTable();
        System.out.println("-------------------");
        System.out.println();
        
        
    }
    
    /**
     * Test of Action 4, of class SemanticActions.
     */
    @Test
    public void testAction4() throws Exception {
        
        System.out.println();
        System.out.println("*******************************************");
        System.out.println("TEST ACTION 4");
        System.out.println("*******************************************");
        System.out.println();
        
        SemanticActions instance = new SemanticActions();
        boolean empty = instance.getSemanticStack().empty();
        assertTrue(empty);
        
        instance.Execute(SemanticAction.action4, new Token(TokenType.ADDOP, "PLUS"), 1);
        boolean empty2 = instance.getSemanticStack().empty();
        assertTrue(!empty2);
        
        instance.dump();
    }
    
    /**
     * Test of Action 6, of class SemanticActions.
     */
    @Test
    public void testAction6() throws Exception {
        
        SemanticActions instance = new SemanticActions();
        boolean ia = instance.getIsArray();
        assertTrue(!ia);
        
        //make sure action doesnt modify anything else
        assertTrue(instance.getGlobal());
        assertTrue(!instance.getInsert());
        
        instance.Execute(SemanticAction.action6, new Token(TokenType.ADDOP), 1);
        boolean ia2 = instance.getIsArray();
        assertTrue(ia2);
        
        //make sure action doesnt modify anything else
        assertTrue(instance.getGlobal());
        assertTrue(!instance.getInsert());
        
    }
    
    /**
     * Test of Action 7, of class SemanticActions.
     */
    @Test
    public void testAction7() throws Exception {
        //same code as action 4, no need to test
    }
    
    /**
     * Test of Action 13, of class SemanticActions.
     */
    @Test
    public void testAction13() throws Exception {
        //same code as action 4, no need to test
    }

}