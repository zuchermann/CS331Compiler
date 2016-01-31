package Compiler.Lex;

import errors.LexicalError;
import java.text.DecimalFormat;

/**
 *
 * @author zacharykondak
 */
public class Tokenizer {

    /**
     * State transition table.
     */
    public static final int[][] TABLE = StateTable.NEW();

    /**
     * New CharStream to provide chars to Tokenizer.
     */
    public static CharStream stream = null;

    /**
     * Field to store the last token returned by tokenizer.
     */
    private TokenType prev = null;
    
    public static final int MAX = 40;

    /**
     * Creates a new tokenizer.
     *
     * @param fileName file path of program to be tokenized.
     */
    public Tokenizer(String fileName) {
        stream = new CharStream(fileName);
    }

    /**
     * Converts an input character to an integer that can be used as an index
     * into TABLE.
     *
     * @param c input character.
     * @return integer to be used as an index into TABLE.
     */
    public int charIndex(char c) {
        if (c == 'a' || c == 'A') {
            return 0;
        } else {
        }
        if (c == 'b' || c == 'B') {
            return 1;
        } else {
        }
        if (c == 'c' || c == 'C') {
            return 2;
        }
        if (c == 'd' || c == 'D') {
            return 3;
        }
        if (c == 'e' || c == 'E') {
            return 4;
        }
        if (c == 'f' || c == 'F') {
            return 5;
        }
        if (c == 'g' || c == 'G') {
            return 6;
        }
        if (c == 'h' || c == 'H') {
            return 7;
        }
        if (c == 'i' || c == 'I') {
            return 8;
        }
        if (c == 'j' || c == 'J') {
            return 9;
        }
        if (c == 'k' || c == 'K') {
            return 10;
        }
        if (c == 'l' || c == 'L') {
            return 11;
        }
        if (c == 'm' || c == 'M') {
            return 12;
        }
        if (c == 'n' || c == 'N') {
            return 13;
        }
        if (c == 'o' || c == 'O') {
            return 14;
        }
        if (c == 'p' || c == 'P') {
            return 15;
        }
        if (c == 'q' || c == 'Q') {
            return 16;
        }
        if (c == 'r' || c == 'R') {
            return 17;
        }
        if (c == 's' || c == 'S') {
            return 18;
        }
        if (c == 't' || c == 'T') {
            return 19;
        }
        if (c == 'u' || c == 'U') {
            return 20;
        }
        if (c == 'v' || c == 'V') {
            return 21;
        }
        if (c == 'w' || c == 'W') {
            return 22;
        }
        if (c == 'x' || c == 'X') {
            return 23;
        }
        if (c == 'y' || c == 'Y') {
            return 24;
        }
        if (c == 'z' || c == 'Z') {
            return 25;
        }
        if (c == '1') {
            return 26;
        }
        if (c == '2') {
            return 27;
        }
        if (c == '3') {
            return 28;
        }
        if (c == '4') {
            return 29;
        }
        if (c == '5') {
            return 30;
        }
        if (c == '6') {
            return 31;
        }
        if (c == '7') {
            return 32;
        }
        if (c == '8') {
            return 33;
        }
        if (c == '9') {
            return 34;
        }
        if (c == '0') {
            return 35;
        }
        if (c == '.') {
            return 36;
        }
        if (c == ',') {
            return 37;
        }
        if (c == ';') {
            return 38;
        }
        if (c == ':') {
            return 39;
        }
        if (c == '<') {
            return 40;
        }
        if (c == '>') {
            return 41;
        }
        if (c == '/') {
            return 42;
        }
        if (c == '*') {
            return 43;
        }
        if (c == '[') {
            return 44;
        }
        if (c == ']') {
            return 45;
        }
        if (c == '+') {
            return 46;
        }
        if (c == '-') {
            return 47;
        }
        if (c == '=') {
            return 48;
        }
        if (c == '(') {
            return 49;
        }
        if (c == ')') {
            return 50;
        }
        //if (c == '/t') return 51;
        if (c == CharStream.BLANK) {
            return 52;
        }
        if (c == CharStream.EOF) {
            return 53;
        }
        return 53;
    }

    /**
     * Gets next char from CharStream.
     *
     * @return next char in input stream.
     */
    protected char getChar() {
        char ch = (char) CharStream.EOF;
        boolean done = false;
        while (!done) {
            try {
                ch = stream.currentChar();
                done = true;
            } catch (LexicalError ex) {
                System.out.println(ex);
            }
        }
        return ch;
    }

    /**
     * Looks at input stream and determines the next token by observing one char
     * at a time. Does this via a DFA represented as the state transition table
     * TABLE.
     *
     * @return next token in input stream.
     * @throws LexicalError
     */
    public Token getNextToken() throws LexicalError {
        MyStack s = new MyStack();
        String newString = "";
        char nextChar = getChar();
        s.push(nextChar);
        int charCode = charIndex(nextChar);
        int nextState = TABLE[0][charCode];
        newString = newString + nextChar;
        int i = 1;
        //while not in a final state, get the next state.
        while (nextState <= 101) {
            nextChar = getChar();
            s.push(nextChar);
            charCode = charIndex(nextChar);
            nextState = TABLE[nextState - 1][charCode];
            newString = newString + nextChar;
            i = i + 1;
        }
        //all states above state 101 are final states.
        //statemens to check which final state, then take appropriate action
        //i.e. pushback, errors, and token type/value.
        if (nextState == 102) {
            stream.pushBack((int) s.pop());
            stream.pushBack((int) s.pop());
            if (i > MAX + 1) {
                throw LexicalError.ConstantTooLong();
            }
            int length = newString.length();
            String val = newString.substring(0, (length - 2));
            Token newToken = new Token(TokenType.INTCONSTANT, val.trim().toLowerCase());
            prev = TokenType.INTCONSTANT;
            return newToken;
        }
        if (nextState == 103) {
            stream.pushBack((int) s.pop());
            if (i > MAX) {
                throw LexicalError.IdentifierTooLong();
            }
            int length = newString.length();
            String val = newString.substring(0, (length - 1));
            Token newToken = new Token(TokenType.IDENTIFIER, val.trim().toLowerCase());
            prev = TokenType.IDENTIFIER;
            return newToken;
        }
        if (nextState == 104) {
            stream.pushBack((int) s.pop());
            Token newToken = new Token(TokenType.PROGRAM);
            prev = TokenType.PROGRAM;
            return newToken;
        }
        if (nextState == 105) {
            stream.pushBack((int) s.pop());
            Token newToken = new Token(TokenType.BEGIN);
            prev = TokenType.BEGIN;
            return newToken;
        }
        if (nextState == 106) {
            stream.pushBack((int) s.pop());
            Token newToken = new Token(TokenType.END);
            prev = TokenType.END;
            return newToken;
        }
        if (nextState == 107) {
            stream.pushBack((int) s.pop());
            Token newToken = new Token(TokenType.VAR);
            prev = TokenType.VAR;
            return newToken;
        }
        if (nextState == 108) {
            stream.pushBack((int) s.pop());
            Token newToken = new Token(TokenType.FUNCTION);
            prev = TokenType.FUNCTION;
            return newToken;
        }
        if (nextState == 109) {
            stream.pushBack((int) s.pop());
            Token newToken = new Token(TokenType.PROCEDURE);
            prev = TokenType.PROCEDURE;
            return newToken;
        }
        if (nextState == 110) {
            stream.pushBack((int) s.pop());
            Token newToken = new Token(TokenType.RESULT);
            prev = TokenType.RESULT;
            return newToken;
        }
        if (nextState == 111) {
            stream.pushBack((int) s.pop());
            Token newToken = new Token(TokenType.INTEGER);
            prev = TokenType.INTEGER;
            return newToken;
        }
        if (nextState == 112) {
            stream.pushBack((int) s.pop());
            Token newToken = new Token(TokenType.REAL);
            prev = TokenType.REAL;
            return newToken;
        }
        if (nextState == 113) {
            stream.pushBack((int) s.pop());
            Token newToken = new Token(TokenType.ARRAY);
            prev = TokenType.ARRAY;
            return newToken;
        }
        if (nextState == 114) {
            stream.pushBack((int) s.pop());
            Token newToken = new Token(TokenType.OF);
            prev = TokenType.OF;
            return newToken;
        }
        if (nextState == 115) {
            stream.pushBack((int) s.pop());
            Token newToken = new Token(TokenType.IF);
            prev = TokenType.IF;
            return newToken;
        }
        if (nextState == 116) {
            stream.pushBack((int) s.pop());
            Token newToken = new Token(TokenType.THEN);
            prev = TokenType.THEN;
            return newToken;
        }
        if (nextState == 117) {
            stream.pushBack((int) s.pop());
            Token newToken = new Token(TokenType.ELSE);
            prev = TokenType.ELSE;
            return newToken;
        }
        if (nextState == 118) {
            stream.pushBack((int) s.pop());
            Token newToken = new Token(TokenType.WHILE);
            prev = TokenType.WHILE;
            return newToken;
        }
        if (nextState == 119) {
            stream.pushBack((int) s.pop());
            Token newToken = new Token(TokenType.DO);
            prev = TokenType.DO;
            return newToken;
        }
        if (nextState == 120) {
            stream.pushBack((int) s.pop());
            Token newToken = new Token(TokenType.NOT);
            prev = TokenType.NOT;
            return newToken;
        }
        if (nextState == 121) {
            stream.pushBack((int) s.pop());
            if (i > 20) {
                throw LexicalError.ConstantTooLong();
            }
            int length = newString.length();
            String val = newString.substring(0, (length - 1));
            Token newToken = new Token(TokenType.INTCONSTANT, val.trim().toLowerCase());
            prev = TokenType.INTCONSTANT;
            return newToken;
        }
        if (nextState == 122) {
            stream.pushBack((int) s.pop());
            if (i > 20) {
                throw LexicalError.ConstantTooLong();
            }
            int length = newString.length();
            String val = newString.substring(0, (length - 1));
            
            String value = "";
            if(val.contains("e") || val.contains("E")){
                DecimalFormat df = new DecimalFormat("#");
                
                val = val.trim().toLowerCase();
                String[] splitString = val.split("e");
                
                Integer base = Integer.parseInt(splitString[0]);
                Integer exp = Integer.parseInt(splitString[1]);
                
                Double newValue = base * Math.pow(10, exp);
                value = df.format(newValue);
                if(newValue > 1){
                    value = value + ".0";
                }
            }
            
            else{
                value = val.trim().toLowerCase();
            }
            
            Token newToken = new Token(TokenType.REALCONSTANT, value);
            prev = TokenType.REALCONSTANT;
            return newToken;
        }
        if (nextState == 123) {
            stream.pushBack((int) s.pop());
            Token newToken = new Token(TokenType.RELOP, "1");
            prev = TokenType.RELOP;
            return newToken;
        }
        if (nextState == 124) {
            stream.pushBack((int) s.pop());
            Token newToken = new Token(TokenType.RELOP, "3");
            prev = TokenType.RELOP;
            return newToken;
        }
        if (nextState == 125) {
            stream.pushBack((int) s.pop());
            Token newToken = new Token(TokenType.RELOP, "2");
            prev = TokenType.RELOP;
            return newToken;
        }
        if (nextState == 126) {
            stream.pushBack((int) s.pop());
            Token newToken = new Token(TokenType.RELOP, "5");
            prev = TokenType.RELOP;
            return newToken;
        }
        if (nextState == 127) {
            stream.pushBack((int) s.pop());
            Token newToken = new Token(TokenType.RELOP, "4");
            prev = TokenType.RELOP;
            return newToken;
        }
        if (nextState == 128) {
            stream.pushBack((int) s.pop());
            Token newToken = new Token(TokenType.RELOP, "6");
            prev = TokenType.RELOP;
            return newToken;
        }
        if (nextState == 129) {
            stream.pushBack((int) s.pop());
            Token newToken = new Token(TokenType.MULOP, "1");
            prev = TokenType.MULOP;
            return newToken;
        }
        if (nextState == 130) {
            stream.pushBack((int) s.pop());
            Token newToken = new Token(TokenType.MULOP, "2");
            prev = TokenType.MULOP;
            return newToken;
        }
        if (nextState == 131) {
            stream.pushBack((int) s.pop());
            Token newToken = new Token(TokenType.MULOP, "3");
            prev = TokenType.MULOP;
            return newToken;
        }
        if (nextState == 132) {
            stream.pushBack((int) s.pop());
            Token newToken = new Token(TokenType.MULOP, "4");
            prev = TokenType.MULOP;
            return newToken;
        }
        if (nextState == 133) {
            stream.pushBack((int) s.pop());
            Token newToken = new Token(TokenType.MULOP, "5");
            prev = TokenType.MULOP;
            return newToken;
        }
        if (nextState == 134) {
            stream.pushBack((int) s.pop());
            if (prev == TokenType.RIGHTPAREN
                    || prev == TokenType.RIGHTBRACKET
                    || prev == TokenType.IDENTIFIER
                    || prev == TokenType.INTCONSTANT
                    || prev == TokenType.REALCONSTANT) {
                Token newToken = new Token(TokenType.ADDOP, "1");
                prev = TokenType.ADDOP;
                return newToken;
            } else {
                prev = TokenType.UNARYPLUS;
                return new Token(TokenType.UNARYPLUS);
            }
        }
        if (nextState == 135) {
            stream.pushBack((int) s.pop());
            if (prev == TokenType.RIGHTPAREN
                    || prev == TokenType.RIGHTBRACKET
                    || prev == TokenType.IDENTIFIER
                    || prev == TokenType.INTCONSTANT
                    || prev == TokenType.REALCONSTANT) {
                Token newToken = new Token(TokenType.ADDOP, "2");
                prev = TokenType.ADDOP;
                return newToken;
            } else {
                prev = TokenType.UNARYMINUS;
                return new Token(TokenType.UNARYMINUS);
            }
        }
        if (nextState == 136) {
            stream.pushBack((int) s.pop());
            Token newToken = new Token(TokenType.ADDOP, "3");
            prev = TokenType.ADDOP;
            return newToken;
        }
        if (nextState == 137) {
            stream.pushBack((int) s.pop());
            Token newToken = new Token(TokenType.COLON);
            prev = TokenType.COLON;
            return newToken;
        }
        if (nextState == 138) {
            stream.pushBack((int) s.pop());
            Token newToken = new Token(TokenType.ASSIGNOP);
            prev = TokenType.ASSIGNOP;
            return newToken;
        }
        if (nextState == 139) {
            stream.pushBack((int) s.pop());
            Token newToken = new Token(TokenType.ENDMARKER);
            prev = TokenType.ENDMARKER;
            return newToken;
        }
        if (nextState == 140) {
            stream.pushBack((int) s.pop());
            Token newToken = new Token(TokenType.DOUBLEDOT);
            prev = TokenType.DOUBLEDOT;
            return newToken;
        }
        if (nextState == 141) {
            Token newToken = new Token(TokenType.COMMA);
            prev = TokenType.COMMA;
            return newToken;
        }
        if (nextState == 142) {
            Token newToken = new Token(TokenType.SEMICOLON);
            prev = TokenType.SEMICOLON;
            return newToken;
        }
        if (nextState == 143) {
            Token newToken = new Token(TokenType.RIGHTPAREN);
            prev = TokenType.RIGHTPAREN;
            return newToken;
        }
        if (nextState == 144) {
            Token newToken = new Token(TokenType.LEFTPAREN);
            prev = TokenType.LEFTPAREN;
            return newToken;
        }
        if (nextState == 145) {
            Token newToken = new Token(TokenType.RIGHTBRACKET);
            prev = TokenType.RIGHTBRACKET;
            return newToken;
        }
        if (nextState == 146) {
            Token newToken = new Token(TokenType.LEFTBRACKET);
            prev = TokenType.LEFTBRACKET;
            return newToken;
        }
        if (nextState == 147) {
            Token newToken = new Token(TokenType.ENDOFFILE);
            prev = TokenType.ENDOFFILE;
            return newToken;
        }

        return new Token();

    }
    
    public int getLine() {
        return stream.lineNumber();
    }
    

}
