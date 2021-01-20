/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newcompiler.Utils;

/**
 *
 * @author Hugo Luna
 */
public class Error {
    private TipoDeError errorTipo;
    private String lexema;
    private int linea;
    private int columna;
    private String descripcion;

    public Error(TipoDeError tipo, String lexema, int linea, int columna, String descripcion) {
        super();
        this.errorTipo = tipo;
        this.lexema = lexema;
        this.linea = linea;
        this.columna = columna;
        this.descripcion = descripcion;
    }

    public TipoDeError getErroTipo() {
        return errorTipo;
    }

    public void setErroTipo(TipoDeError errorTipo) {
        this.errorTipo = errorTipo;
    }

    public String getLexema() {
        return lexema;
    }

    public void setLexema(String lexema) {
        this.lexema = lexema;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


}
