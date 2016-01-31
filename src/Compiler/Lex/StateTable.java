

package Compiler.Lex;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author zacharykondak
 */
public class StateTable {
    
    /**
     * constructs a new state transition table, 
     * that will in turn drive lexical analysis.
     * @return a new state transition table
     */
    public static int[][] NEW() {
    int[][] states = new int[101][54];
    //file path for my state transition table provided in .csv format
    //in the src file. Change this path to the suitable path 
    //on your native machine.
        String csvFile = "src/LexTable.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        
        try {
            br = new BufferedReader(new FileReader(csvFile));
            int i = 0;
            while (((line = br.readLine()) != null) && i < 101) {
                String[] column = line.split(cvsSplitBy);
                for(int j = 0; j < 54; j++) {
                    states[i][j] = Integer.parseInt(column[j]);
                }
                i++;
            }
            
            
        } catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} finally {
		if (br != null) {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
        
        return states;
}
    
}
