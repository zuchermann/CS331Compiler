/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Compiler.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author zacharykondak
 */
public class ParseTable {
    
    /**
     * Construct a new parse table to be used by Parser.
     * @return int[][]; the parse table as a 2-dimensional array
     */
    public static int[][] NEW() {
        int[][] states = new int[35][38];
        File datFile = new File("src/parsetable-2const.dat");
        Scanner sc;
        try {
            sc = new Scanner(datFile);
            int row = 0;
            int column = 0;
            while (sc.hasNextInt()) {
                if (column == 38) {
                    column = 0;
                    row++;
                }
                states[row][column] = sc.nextInt();
                column++;
            }
            return states;
        } catch (FileNotFoundException ex) {
            System.out.println("Did not fine parse table file");
            return null;
        }
    }
}
