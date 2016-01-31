/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lex;

import Compiler.Lex.MyStack;
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
public class MyStackTest {
    
    public MyStackTest() {
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
     * Test of pop and push methods, of class MyStack.
     */
    @Test
    public void testPush() {
        System.out.println("push");
        char a = 'a';
        MyStack instance = new MyStack();
        instance.push(a);
        assertEquals(instance.pop(), 'a');
        instance.push(a);
        char b = 'b';
        instance.push(b);
        assertEquals(instance.pop(), 'b');
        assertEquals(instance.pop(), 'a');
        instance.push(a);
        instance.push(b);
        char c = 'c';
        instance.push(c);
        assertEquals(instance.pop(), 'c');
        assertEquals(instance.pop(), 'b');
        assertEquals(instance.pop(), 'c');
        instance.push(a);
        assertEquals(instance.pop(), 'a');
    }

    
    
}
