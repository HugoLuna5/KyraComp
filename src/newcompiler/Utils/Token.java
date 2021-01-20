/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newcompiler.Utils;
import newcompiler.GCI.Estructuras.Modelos.Tipo;

/**
 *
 * @author Hugo Luna
 */
public class Token implements Tipo{
    
    private String lexema;
    private TokenTipo tipoToken;
    private int linea, columna;
    private boolean fueDeclarado;
    private String tipoVar;
    
    public Token(String lexema, TokenTipo tipoToken, int linea, int columna) {
        this.lexema = lexema;
        this.tipoToken = tipoToken;
        this.linea = linea;
        this.columna = columna;
        this.fueDeclarado = false;
    }

    public Token(String lexema, TokenTipo tipoToken) {
        super();
        this.lexema = lexema;
        this.tipoToken = tipoToken;
    }

    public String getLexema() {
        return lexema;
    }

    public void setLexema(String lexema) {
        this.lexema = lexema;
    }

    public TokenTipo getTipoToken() {
        return tipoToken;
    }

    public void setTipoToken(TokenTipo tipoToken) {
        this.tipoToken = tipoToken;
    }

    public int getLinea() {
        return linea;
    }

    public void setLinea(int linea) {
        this.linea = linea;
    }

    public int getColumna() {
        return columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    public boolean fueDeclarado() {
        return fueDeclarado;
    }

    public void setFueDeclarado(boolean foiDeclarado) {
        this.fueDeclarado = foiDeclarado;
    }
    
     public String getTipoVar() {
        return tipoVar;
    }

    public void setTipoVar(String tipoVar) {
        this.tipoVar = tipoVar;
    }
    
     @Override
    public String getNombreClase() {
        return this.tipoVar;
    }

    
}
