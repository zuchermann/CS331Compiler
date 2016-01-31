/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Compiler;

import drivers.ParserDriver;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author zacharykondak
 */
public class CS331Compiler {
    
    public static String array = "src/array.pas"; 
    public static String arrayRef = "src/arrayRef.pas";
    public static String bool = "src/bool.pas";
    public static String expressionTest = "src/expressionTest.pas";
    public static String fib = "src/fib.pas";
    public static String func = "src/func.pas";
    public static String ifTest = "src/ifTest.pas";
    public static String noParm = "src/noParm.pas";
    public static String orTest = "src/orTest.pas";
    public static String proc = "src/proc.pas";
    public static String recursion = "src/recursion.pas";
    public static String simple = "src/simple.pas";
    public static String ultimate = "src/ultimate.pas";

    /**
     * Text field to view printed output.
     */
    private JTextArea outField;
    /**
     * Text field for user input.
     */
    private JTextArea inField;

    /**
     * Method to update outField.
     * @param text text to be added to outField.
     */
    private void updateTextArea(final String text) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                outField.append(text);
            }
        });
    }

    /**
     * method to redirect System.out to outField.
     */
    private void redirectSystemStreams() {
        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                updateTextArea(String.valueOf((char) b));
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                updateTextArea(new String(b, off, len));
            }

            @Override
            public void write(byte[] b) throws IOException {
                write(b, 0, b.length);
            }
        };

        System.setOut(new PrintStream(out, true));
        System.setErr(new PrintStream(out, true));
    }

    
    /**
     * Constructs a new MainGUI object; initializes the GUI.
     */
    public CS331Compiler(){
        //redirects System.out to outField.
        this.redirectSystemStreams();
        //build outField.
        JPanel outPanel = new JPanel();
        JPanel inPanel = new JPanel();
        outPanel.setLayout(new BorderLayout());
        outPanel.add(new JLabel("View"), BorderLayout.CENTER);
        inPanel.setLayout(new BorderLayout());
        inPanel.add(new JLabel("View"), BorderLayout.CENTER);
        outField = new JTextArea(25, 25);
        inField = new JTextArea(25, 25);
        outField.setEditable(false);
        inField.setEditable(true);
        JScrollPane sp = new JScrollPane(outField);
        outPanel.add(sp, BorderLayout.CENTER);
        JScrollPane spIn = new JScrollPane(inField);
        inPanel.add(spIn, BorderLayout.CENTER);

        //creation of buttons.
        JButton arrayButton = new JButton("Load \"Array Test\"");
        arrayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    inField.setText(new Scanner(new File(array)).useDelimiter("\\Z").next());
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(CS331Compiler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        JButton arrayRefButton = new JButton("Load \"ArrayRef Test\"");
        arrayRefButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    inField.setText(new Scanner(new File(arrayRef)).useDelimiter("\\Z").next());
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(CS331Compiler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        JButton boolButton = new JButton("Load \"Bool Test\"");
        boolButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    inField.setText(new Scanner(new File(bool)).useDelimiter("\\Z").next());
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(CS331Compiler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        JButton expressionButton = new JButton("Load \"Expression Test\"");
        expressionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    inField.setText(new Scanner(new File(expressionTest)).useDelimiter("\\Z").next());
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(CS331Compiler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        JButton fibButton = new JButton("Load \"Fib Test\"");
        fibButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    inField.setText(new Scanner(new File(fib)).useDelimiter("\\Z").next());
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(CS331Compiler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        JButton funcButton = new JButton("Load \"Func Test\"");
        funcButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    inField.setText(new Scanner(new File(func)).useDelimiter("\\Z").next());
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(CS331Compiler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        JButton ifButton = new JButton("Load \"If Test\"");
        ifButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    inField.setText(new Scanner(new File(ifTest)).useDelimiter("\\Z").next());
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(CS331Compiler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        JButton noParmButton = new JButton("Load \"NoParm Test\"");
        noParmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    inField.setText(new Scanner(new File(noParm)).useDelimiter("\\Z").next());
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(CS331Compiler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        JButton orButton = new JButton("Load \"Or Test\"");
        orButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    inField.setText(new Scanner(new File(orTest)).useDelimiter("\\Z").next());
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(CS331Compiler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        JButton procButton = new JButton("Load \"Proc Test\"");
        procButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    inField.setText(new Scanner(new File(proc)).useDelimiter("\\Z").next());
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(CS331Compiler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        JButton recursionButton = new JButton("Load \"Recursion Test\"");
        recursionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    inField.setText(new Scanner(new File(recursion)).useDelimiter("\\Z").next());
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(CS331Compiler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        JButton simpleButton = new JButton("Load \"Simple Test\"");
        simpleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    inField.setText(new Scanner(new File(simple)).useDelimiter("\\Z").next());
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(CS331Compiler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        JButton ultimateButton = new JButton("Load \"Ultimate Test\"");
        ultimateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    inField.setText(new Scanner(new File(ultimate)).useDelimiter("\\Z").next());
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(CS331Compiler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        JButton compileButton = new JButton("Compile");
        
        compileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                outField.setText("");
                //get temp file
                File temp = new File("./src/tempfile.pas");
                try {

                    //clear it
                    PrintWriter writer = new PrintWriter(temp);
                    writer.print("");
                    writer.close();
                    //write it
                    BufferedWriter bw = new BufferedWriter(new FileWriter(temp));
                    bw.write(inField.getText());
                    bw.close();

                } catch (IOException e) {

                    e.printStackTrace();

                }

                ParserDriver test = new ParserDriver("./src/tempfile.pas");
                test.run();
            }
        });

        JButton loadButton = new JButton("Load from File");
        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {

                    //opens the load dialogue window.
                    FileDialog fd = new FileDialog((java.awt.Frame) null,
                            "Load", FileDialog.LOAD);
                    fd.setVisible(true);
                    String fileName = fd.getFile();
                    String fileLocation = fd.getDirectory();

                    if (!(fileName.endsWith(".pas"))) {
                        System.out.println("input files must end with .pas");
                    } else {
                        String content = new Scanner(new File(fileLocation + fileName)).useDelimiter("\\Z").next();
                        inField.setText(content);
                    }
                } catch (Exception e) {
                    System.out.println("Error loading file.");
                }
            }

        });


        loadButton.setForeground(Color.BLUE);

        JPanel buttonPanel = new JPanel();
        GridLayout grid = new GridLayout(3, 5, -10, 5);
        
        buttonPanel.setLayout(grid);
        
        //first row of buttons
        buttonPanel.add(simpleButton);
        buttonPanel.add(arrayButton);
        buttonPanel.add(arrayRefButton);
        buttonPanel.add(expressionButton);
        //adds blanks space for aesthetic purposes
        buttonPanel.add(new JLabel("")); 
        
        //second row of buttons
        buttonPanel.add(orButton);
        buttonPanel.add(boolButton);
        buttonPanel.add(ifButton);
        buttonPanel.add(fibButton);
        //adds blanks space for aesthetic purposes
        buttonPanel.add(new JLabel(""));
        
        //third row of buttons
        buttonPanel.add(procButton);
        buttonPanel.add(funcButton);
        buttonPanel.add(recursionButton);
        buttonPanel.add(noParmButton);
        buttonPanel.add(ultimateButton);
        
        
        
        
        JPanel buttonPane2 = new JPanel();
        GridLayout grid2 = new GridLayout(1, 2, -10, 5);
        buttonPane2.setLayout(grid2);
    
        buttonPane2.add(loadButton);
        buttonPane2.add(compileButton);

        //Create and show frame.
        JFrame frame = new JFrame("CMPU 331 COMPILE-O-MATIC!");
        frame.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        c.gridwidth = 2;
        c.gridy = 0;
        frame.add(buttonPanel, c);
        c.gridwidth = 1;
        c.gridy = 1;
        c.ipadx = 400;
        c.ipady = 400;
        c.anchor = GridBagConstraints.CENTER;
        frame.add(inPanel, c);
        c.gridx = 1;
        c.anchor = GridBagConstraints.CENTER;
        frame.add(outPanel, c);
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 3;
        c.ipadx = 0;
        c.ipady = 0;
        frame.add(buttonPane2, c);
        
        frame.pack();
        frame.setSize(925, 600);
        frame.setResizable(false);
        frame.setVisible(true);
    }
    
    
    public static void main(String[] args){
        try{
        new CS331Compiler();
        }
        catch(Exception e){
            //do nothing
        }
    }

}
