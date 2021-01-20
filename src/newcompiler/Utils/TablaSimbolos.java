/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newcompiler.Utils;

import java.util.HashMap;

/**
 *
 * @author Hugo Luna
 */
public class TablaSimbolos extends HashMap<String, Token> {
    private static final long serialVersionUID = 1L;
    private static TablaSimbolos uniqueInstance;

    private TablaSimbolos() {
        this.configTablaSimbolos(new Token("verdadero", TokenTipo.LOGIC_VAL));
        this.configTablaSimbolos(new Token("falso", TokenTipo.LOGIC_VAL));
        this.configTablaSimbolos(new Token("no", TokenTipo.LOGIC_OP));
        this.configTablaSimbolos(new Token("y", TokenTipo.LOGIC_OP));
        this.configTablaSimbolos(new Token("o", TokenTipo.LOGIC_OP));
        this.configTablaSimbolos(new Token("logico", TokenTipo.TYPE));
        this.configTablaSimbolos(new Token("texto", TokenTipo.TYPE));
        this.configTablaSimbolos(new Token("entero", TokenTipo.TYPE));
        this.configTablaSimbolos(new Token("real", TokenTipo.TYPE));
        this.configTablaSimbolos(new Token("programa", TokenTipo.PROGRAM));
        this.configTablaSimbolos(new Token("fin_programa", TokenTipo.END_PROGRAM));
        this.configTablaSimbolos(new Token("comienza", TokenTipo.BEGIN));
        this.configTablaSimbolos(new Token("fin", TokenTipo.END));
        this.configTablaSimbolos(new Token("si", TokenTipo.IF));
        this.configTablaSimbolos(new Token("entonces", TokenTipo.THEN));
        this.configTablaSimbolos(new Token("siNo", TokenTipo.ELSE));
        this.configTablaSimbolos(new Token("para", TokenTipo.FOR));
        this.configTablaSimbolos(new Token("mientras", TokenTipo.WHILE));
        this.configTablaSimbolos(new Token("declarar", TokenTipo.DECLARE));
        this.configTablaSimbolos(new Token("hacia", TokenTipo.TO));
        this.configTablaSimbolos(new Token("imprimir", TokenTipo.PRINT));

    }

    public static synchronized TablaSimbolos getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new TablaSimbolos();
        }
        return uniqueInstance;
    }

    public void configTablaSimbolos(Token token) {
        this.put(token.getLexema(), token);
    }

    public Token instalaToken(String lexema, int linea, int columna) {
        Token token = null;

        if (containsKey(lexema)) {
            token = get(lexema);
            token.setLinea(linea);
            token.setColumna(columna);
        } else {
            token = new Token(lexema, TokenTipo.ID, linea, columna);
            configTablaSimbolos(token);
        }
        return token;
    }

    public void printToken() {
        System.out.println();
        System.out.println("##############################");
        System.out.println("      Tabla de Simbolos        ");
        System.out.println("##############################");
        for (Entry<String, Token> entry : entrySet()) {
            if (entry.getValue().getLinea() != 0 && entry.getValue().getColumna() != 0) {
                System.out.println("Lexema: " + entry.getValue().getLexema());
                System.out.println("Tipo Token: " + entry.getValue().getTipoToken());
                System.out.println("Linea: " + entry.getValue().getLinea());
                System.out.println("Columna: " + entry.getValue().getColumna());
                System.out.println("##############################");
            }
        }
    }
    
}
