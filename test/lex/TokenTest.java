/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lex;

import Compiler.Lex.Token;
import Compiler.Lex.TokenType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author zacharykondak
 */
public class TokenTest {
    
    public TokenTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getType method, of class Token.
     */
    @Test
    public void testGetType() {
        System.out.println("getType");
        Token instance = new Token();
        TokenType expResult = null;
        TokenType result = instance.getType();
        assertEquals(expResult, result);
        
        Token instance2 = new Token(TokenType.DO);
        TokenType expResult2 = TokenType.DO;
        TokenType result2 = instance2.getType();
        assertEquals(expResult2, result2);
        
        Token instance3 = new Token(TokenType.DOUBLEDOT);
        TokenType expResult3 = TokenType.DOUBLEDOT;
        TokenType result3 = instance3.getType();
        assertEquals(expResult3, result3);
    }

    /**
     * Test of getValue method, of class Token.
     */
    @Test
    public void testGetValue() {
        System.out.println("getValue");
        Token instance = new Token();
        String expResult = null;
        String result = instance.getValue();
        assertEquals(expResult, result);
        
        Token instance2 = new Token(TokenType.ELSE);
        String expResult2 = null;
        String result2 = instance2.getValue();
        assertEquals(expResult2, result2);
        
        Token instance3 = new Token(TokenType.IDENTIFIER, "foo");
        String expResult3 = "foo";
        String result3 = instance3.getValue();
        assertEquals(expResult3, result3);
        
        Token instance4 = new Token(TokenType.INTCONSTANT, "42");
        String expResult4 = "42";
        String result4 = instance4.getValue();
        assertEquals(expResult4, result4);
    }

    /**
     * Test of getOpType method, of class Token.
     */
    @Test
    public void testGetOpType() {
        System.out.println("getOpType");
        Token instance = new Token();
        String expResult = "NOT AN OPERATOR";
        String result = instance.getOpType();
        assertEquals(expResult, result);
        
        Token instance2 = new Token(TokenType.MULOP, "3");
        String expResult2 = "DIV";
        String result2 = instance2.getOpType();
        assertEquals(expResult2, result2);
        
        Token instance3 = new Token(TokenType.ADDOP, "1");
        String expResult3 = "PLUS";
        String result3 = instance3.getOpType();
        assertEquals(expResult3, result3);
        
        Token instance4 = new Token(TokenType.RELOP, "6");
        String expResult4 = "GREATERTHANOREQUAL";
        String result4 = instance4.getOpType();
        assertEquals(expResult4, result4);
    }
    
}
