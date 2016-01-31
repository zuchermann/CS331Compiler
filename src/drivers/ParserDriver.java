package drivers;

import Compiler.parser.*;
import errors.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class ParserDriver {
    
    /**
     * Text field to view printed output.
     */
    private JTextArea outField;

    Parser parser;

    public ParserDriver(String filename) {
        try {
            parser = new Parser(filename);
        } catch (Exception ex) {
            if (ex instanceof CompilerError) {
                System.out.println(ex);
            }
        }
    }

    public void run() {
        while (!(parser.getDone())) {
            try {
                parser.parse();
            } catch (Exception ex) {
                if (ex instanceof CompilerError) {
                    System.out.println(ex);
                } else {
                    break;
                }
            }
        }
        //System.out.println("Compilation successful.");
        parser.getActions().dump();
        parser.getActions().getQuads().print();
    }


}
