/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lex;

import Compiler.Lex.StateTable;
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
public class StateTableTest {
    
    public StateTableTest() {
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
     * Test of NEW method, of class StateTable.
     */
    @Test
    public void testNEW() {
        System.out.println("NEW");
        int expResult1 = 50;
        int expResult2 = 89;
        int expResult3 = 99;
        int expResult4 = 140;
        int[][] instance = StateTable.NEW();
        int result1 = instance[0][0];
        int result2 = instance[87][21];
        int result3 = instance[97][48];
        int result4 = instance[100][53];
        assertEquals(expResult1, result1);
        assertEquals(expResult2, result2);
        assertEquals(expResult3, result3);
        assertEquals(expResult4, result4);
    }
    
}
