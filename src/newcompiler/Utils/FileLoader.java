/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newcompiler.Utils;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileReader;
import java.io.IOException;
import newcompiler.Utils.Util;

/**
 *
 * @author Hugo Luna
 */
public class FileLoader {

    public BufferedReader buffer;

    private int linea;
    private int columna;
    private int lineBreak;

    private boolean finalLinea = false;

    public final int EOF_CHAR = -1;

    private static char ultimoCaracter;

    /**
     * constructor de la clase fileloader
     *
     * @param path ruta del archivo a leer
     * @throws IOException manejador de error de buffer
     */
    public FileLoader(String path) throws IOException {
        this.linea = 1;
        this.columna = 0;
        this.buffer = new BufferedReader(new FileReader(path));
    }

    /**
     * obtener caracter por caracter
     *
     * @return retorna un caracter
     * @throws IOException manejador de errores de entrada y salida
     */
    public char getNextChar() throws IOException {
        this.buffer.mark(1);
        int aux = this.buffer.read();

        if (aux == EOF_CHAR && !Util.esDigito(FileLoader.getultimoCaracter()) && !Util.esLetra(FileLoader.getultimoCaracter())
                && !Util.esPunto(FileLoader.getultimoCaracter()) && ultimoCaracter != '\n' && ultimoCaracter != '\r'
                && !Character.isWhitespace(ultimoCaracter)) {
            throw new EOFException();
        }

        char result = (char) aux;

        this.controlLineColumn(result);

        FileLoader.setultimoCaracter(result);

        return result;
    }

    /**
     *verifica el termino de cada linea 
     * @param c 
     */
    public void controlLineColumn(char c) {
        if (this.finalLinea) {
            this.linea++;
            this.columna = 0;
            this.finalLinea = false;
        }

        this.setUltimaColumna(this.columna);

        if (c == '\n') {
            this.finalLinea = true;
        }
        this.columna++;
    }

    /**
     * obtenemos la variable linea 
     * @return 
     */
    public int getLine() {
        return linea;
    }

    /**
     * obtenemos la variable columna 
     * @return 
     */
    public int getColumn() {
        return columna;
    }

    /**
     * obtenemos una cadena compuesta de la linea y la columna
     * @return 
     */
    public String getElement() {
        return "[" + this.getLine() + "," + this.getColumn() + "]";
    }

    /**
     * obtenenmos un entero con la ultima columna 
     * @return 
     */
    public int getUltimaColumna() {
        return lineBreak;
    }

    /**
     * establecemos la ultima columna
     * @param ultimaColumna 
     */
    public void setUltimaColumna(int ultimaColumna) {
        this.lineBreak = ultimaColumna;
    }

    /**
     * volvemos al ultimo caracter y recetiamos el buffer
     * @throws IOException 
     */
    public void rollbackChar() throws IOException {
        buffer.reset();
    }

    /**
     * obtenemos el ultimo caracter
     * @return 
     */
    public static char getultimoCaracter() {
        return ultimoCaracter;
    }

    /**
     * establecemos el ultimo caracter
     * @param ultimoCaracter 
     */
    public static void setultimoCaracter(char ultimoCaracter) {
        FileLoader.ultimoCaracter = ultimoCaracter;
    }

}
