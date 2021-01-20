/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newcompiler.Analisadores;

import newcompiler.Exception.CaractereInvalidoException;
import newcompiler.Utils.FileLoader;
import newcompiler.Utils.*;
import newcompiler.Utils.Error;

import java.io.EOFException;
import java.io.IOException;

/**
 *
 * @author Hugo Luna
 */
public class Lexico {

    private FileLoader lectorDeArchivo;
    private StringBuilder lexema = new StringBuilder();
    private int linea;
    private int columna;
    private Token tokenAlmacenado;
    private ManejadorErrores manejadorErrores;

    /**
     * constructor de la clase
     *
     * @param path
     * @throws IOException
     */
    public Lexico(String path, ManejadorErrores manejadorErrores) throws IOException {
        lectorDeArchivo = new FileLoader(path);
        this.manejadorErrores = manejadorErrores;
    }

    /**
     * obtenemos el sig token
     *
     * @return
     */
    public Token getNextToken() {
        Token tk = null;

        if (tokenAlmacenado != null) {
            tk = tokenAlmacenado;
            tokenAlmacenado = null;
            return tk;
        }
        // manejo de errores(xaraxter invalido o error de IO)
        try {
            tk = this.procesar();
        } catch (CaractereInvalidoException e) {
            tk = this.getNextToken();
        } catch (IOException eof) {
            if (lexema.length() > 0) {
                this.guardarErro(TipoDeError.LEXICO, tk, Mensajes.MSG_FIN_INESPERADO_ARCHIVO, false);
            }
            tk = new Token("EndOfFile", TokenTipo.EOF);
        }

        return tk;
    }

    /**
     *
     * @return @throws CaractereInvalidoException analizador de error caracter
     * invalido
     * @throws IOException Mnejador de error IO
     *
     */
    private Token procesar() throws CaractereInvalidoException, IOException {
        Token tk = null;
        char caractere;
        lexema = new StringBuilder();
        do {
            caractere = lectorDeArchivo.getNextChar();
        } while (Character.isWhitespace(caractere));

        lexema.append(caractere);
        linea = lectorDeArchivo.getLine();
        columna = lectorDeArchivo.getColumn();

        if (Util.esLetra(caractere)) {
            tk = this.tokenId();
        } else if (Util.esDigito(caractere)) {
            tk = this.tokenNumInt();
        } else {
            switch (caractere) {
                case ':':
                    caractere = lectorDeArchivo.getNextChar();
                    if (caractere == '[') {
                        lectorDeArchivo.rollbackChar();
                        this.descartarComentario();
                        tk = this.procesar();
                    } else {
                        lectorDeArchivo.rollbackChar();
                        tk = this.tokenAttribOp();
                    }
                    break;
                case '$':
                    tk = this.tokenRelOp();
                    break;
                case '"':
                    tk = new Token(this.lexema.toString(),TokenTipo.COMILLAS, this.linea, this.columna);
                    break;
                case '%':
                    tk = new Token(this.lexema.toString(), TokenTipo.MODULO, this.linea, this.columna);
                    break;
                case '^':
                    tk = new Token(this.lexema.toString(), TokenTipo.POTENCIA, this.linea, this.columna);
                    break;
                case '\'':
                    tk = this.tokenLiteral();
                    break;
                case '+':
                    tk = new Token(this.lexema.toString(), TokenTipo.ADDSUB_OP, this.linea, this.columna);
                    break;
                case '-':
                    tk = new Token(this.lexema.toString(), TokenTipo.ADDSUB_OP, this.linea, this.columna);
                    break;
                case '*':
                    tk = new Token(this.lexema.toString(), TokenTipo.MULTDIV_OP, this.linea, this.columna);
                    break;
                case '/':
                    tk = new Token(this.lexema.toString(), TokenTipo.MULTDIV_OP, this.linea, this.columna);
                    break;
                case ';':
                    tk = new Token(this.lexema.toString(), TokenTipo.TERM, this.linea, this.columna);
                    break;
                case '(':
                    tk = new Token(this.lexema.toString(), TokenTipo.L_PAR, this.linea, this.columna);
                    break;
                case ')':
                    tk = new Token(this.lexema.toString(), TokenTipo.R_PAR, this.linea, this.columna);
                    break;
                default:
                    this.guardarErro(TipoDeError.LEXICO, tk, Mensajes.MSG_LEXICO_CARACTER_INVALIDO, false);
                    throw new CaractereInvalidoException(Mensajes.MSG_LEXICO_CARACTER_INVALIDO);
            }
        }
        return tk;
    }

    /**
     *
     * @throws IOException manejador de expecion de entrada y salida
     * @throws CaractereInvalidoException
     */
    private void descartarComentario() throws IOException, CaractereInvalidoException {
        int comentarios = 0;
        char caracter = lectorDeArchivo.getNextChar();

        try {
            lexema.append(caracter);
            //[: :]
            if (caracter == '[') {
                while (true) {
                    caracter = lectorDeArchivo.getNextChar();
                    lexema.append(caracter);

                    if (caracter == ':') {
                        caracter = lectorDeArchivo.getNextChar();
                        lexema.append(caracter);
                        if (caracter == '[') {
                            comentarios++;
                        }
                    }
                    if (caracter == ']') {
                        caracter = lectorDeArchivo.getNextChar();
                        lexema.append(caracter);
                        if (caracter == ':') {
                            if (comentarios == 0) {
                                break;
                            } else {
                                comentarios--;
                            }
                        }
                    }
                }
            } else {
                manejadorErrores.guardarErro(new Error(TipoDeError.LEXICO, lexema.toString(), linea, columna,
                        Mensajes.MSG_LEXICO_FALTA_CERRAR_COMENTARIO));
                throw new CaractereInvalidoException(Mensajes.MSG_LEXICO_FALTA_CERRAR_COMENTARIO);
            }
        } catch (EOFException eof) {
            manejadorErrores.guardarErro(new Error(TipoDeError.LEXICO, lexema.toString(), linea, columna,
                    Mensajes.MSG_LEXICO_COMENTARIO_CERRADO_INCORRECTO));
            throw new CaractereInvalidoException(Mensajes.MSG_LEXICO_COMENTARIO_CERRADO_INCORRECTO);
        }
    }

    /**
     * guarda un token de tipo literal
     *
     * @return
     * @throws IOException
     */
    private Token tokenLiteral() throws IOException {
        Token tkLiteral = null;
        char c;
        boolean encontrado = false;
        try {
            while (true) {
                c = lectorDeArchivo.getNextChar();
                this.lexema.append(c);
                if (c == '\'') {
                    tkLiteral = new Token(this.lexema.toString(), TokenTipo.LITERAL, this.linea, this.columna);
                    encontrado = true;
                    break;
                }
            }
            if (encontrado == false) {
                throw new EOFException();
            }
        } catch (EOFException e) {
            Token tk = new Token(this.lexema.toString(), TokenTipo.LITERAL, this.linea, this.columna);
            this.guardarErro(TipoDeError.LEXICO, tk, Mensajes.MSG_LEXICO_FALTA_COMILLAS, false);
            tkLiteral = new Token(this.lexema.toString(), TokenTipo.ERROR);
        }

        return tkLiteral;
    }

    /**
     * guarda un token de tipo operacional
     *
     * @return
     * @throws IOException
     */
    private Token tokenRelOp() throws IOException {
        Token tkRelOp = null;
        char c;
        c = lectorDeArchivo.getNextChar();
        boolean validar = false;

        if (c == '>') {
            this.lexema.append(c);
            c = lectorDeArchivo.getNextChar();
            if (c == '=') {
                this.lexema.append(c);
                c = lectorDeArchivo.getNextChar();
                if (c == '$') {
                    this.lexema.append(c);
                    tkRelOp = new Token(this.lexema.toString(), TokenTipo.REL_OP, this.linea, this.columna);
                    validar = true;
                }
            }
        }

        if (validar == false && c == '<') {
            this.lexema.append(c);
            c = lectorDeArchivo.getNextChar();
            if (c == '=') {
                this.lexema.append(c);
                c = lectorDeArchivo.getNextChar();
                if (c == '$') {
                    this.lexema.append(c);
                    tkRelOp = new Token(this.lexema.toString(), TokenTipo.REL_OP, this.linea, this.columna);
                    validar = true;
                }
            }
        }

        if (validar == false && c == '=') {
            this.lexema.append(c);
            c = lectorDeArchivo.getNextChar();
            if (c == '=') {
                this.lexema.append(c);
                c = lectorDeArchivo.getNextChar();
                if (c == '$') {
                    this.lexema.append(c);
                    tkRelOp = new Token(this.lexema.toString(), TokenTipo.REL_OP, this.linea, this.columna);
                    validar = true;
                }
            }
        }

        if (validar == false && c == '!') {
            this.lexema.append(c);
            c = lectorDeArchivo.getNextChar();
            if (c == '=') {
                this.lexema.append(c);
                c = lectorDeArchivo.getNextChar();
                if (c == '$') {
                    this.lexema.append(c);
                    tkRelOp = new Token(this.lexema.toString(), TokenTipo.REL_OP, this.linea, this.columna);
                    validar = true;
                }
            }
        }

        if (validar == false && c == '|') {

            this.lexema.append(c);
            c = lectorDeArchivo.getNextChar();
            if (c == '|') {
                this.lexema.append(c);
                c = lectorDeArchivo.getNextChar();
                if (c == '$') {
                    this.lexema.append(c);
                    tkRelOp = new Token(this.lexema.toString(), TokenTipo.REL_OP, this.linea, this.columna);
                    validar = true;
                }
            }
        }

        if (validar == false && c == '&') {
            this.lexema.append(c);
            c = lectorDeArchivo.getNextChar();
            if (c == '&') {
                this.lexema.append(c);
                c = lectorDeArchivo.getNextChar();
                if (c == '$') {
                    this.lexema.append(c);
                    tkRelOp = new Token(this.lexema.toString(), TokenTipo.REL_OP, this.linea, this.columna);
                    validar = true;
                }
            }
        }

        if (validar == false) {
            if (!Character.isWhitespace(c)) {
                this.lexema.append(c);
            }

            Token tk = new Token(this.lexema.toString(), TokenTipo.REL_OP, this.linea, this.columna);
            this.guardarErro(TipoDeError.LEXICO, tk, Mensajes.MSG_LEXICO_OPERADOR_RELACIONAL_INVALIDO, false);

            tkRelOp = new Token("Error", TokenTipo.ERROR);
        }

        return tkRelOp;
    }

    /**
     * guardas un token de tipo de asignacion de atributo
     *
     * @return
     * @throws IOException
     */
    private Token tokenAttribOp() throws IOException {
        Token tkAttribOp = null;
        char c;
        c = lectorDeArchivo.getNextChar();
        if (c == '=') {
            this.lexema.append(c);
            tkAttribOp = new Token(this.lexema.toString(), TokenTipo.ATTRIB_OP, this.linea, this.columna);
        } else {
            Token tk = new Token(this.lexema.toString(), TokenTipo.ATTRIB_OP, this.linea, this.columna);
            this.guardarErro(TipoDeError.LEXICO, tk, Mensajes.MSG_LEXICO_CARACTER_INVALIDO, false);

            tkAttribOp = new Token(this.lexema.toString(), TokenTipo.ERROR);
        }

        return tkAttribOp;
    }

    /**
     * token de numero entero
     *
     * @return
     * @throws IOException
     */
    private Token tokenNumInt() throws IOException {
        Token tkNumInt = null;
        char c;

        try {
            while (true) {
                c = lectorDeArchivo.getNextChar();
                if (Util.esDigito(c)) {
                    lexema.append(c);
                } else if (Util.esPunto(c)) {
                    this.lexema.append(c);
                    tkNumInt = this.tokenNumFloat();
                    break;
                } else if (c == 'e' || c == 'E') {
                    this.lexema.append(c);
                    tkNumInt = this.tokenNumCientifico(TokenTipo.NUM_INT);
                    break;
                } else {
                    lectorDeArchivo.rollbackChar();
                    tkNumInt = new Token(this.lexema.toString(), TokenTipo.NUM_INT, this.linea, this.columna);
                    break;
                }
            }
        } catch (Exception e) {
            Token tk = new Token(this.lexema.toString(), TokenTipo.NUM_INT, this.linea, this.columna);
            this.guardarErro(TipoDeError.LEXICO, tk, Mensajes.MSG_LEXICO_ERROR, false);

        } finally {
            this.lexema = new StringBuilder();
        }

        return tkNumInt;
    }

    /**
     * token de numero flotante
     *
     * @return
     * @throws IOException
     */
    private Token tokenNumFloat() throws IOException {
        Token tkNumFloat = null;
        char c = lectorDeArchivo.getNextChar();
        boolean primero = false;
        boolean cient = false;
        try {
            while (Util.esDigito(c)) {
                this.lexema.append(c);
                c = lectorDeArchivo.getNextChar();
                primero = true;
            }
            if (Character.isWhitespace(c) && !primero) {
                throw new EOFException();
            }
            if ((!Util.esDigito(c) && c != 'e' && c != 'E' && !primero) || ((c == 'e' || c == 'E') && primero == false)) {
                if (Util.esLetra(c) || Util.esPunto(c) || Util.esDolar(c) || Util.esComilla(c)) {
                    this.lexema.append(c);
                }
                throw new EOFException();
            }
            if (c == 'e' || c == 'E') {
                this.lexema.append(c);
                tkNumFloat = this.tokenNumCientifico(TokenTipo.NUM_FLOAT);
                cient = true;
            }

        } catch (EOFException e) {
            Token tk = new Token(this.lexema.toString(), TokenTipo.NUM_FLOAT, this.linea, this.columna);
            this.guardarErro(TipoDeError.LEXICO, tk, Mensajes.MSG_LEXICO_CARACTER_INVALIDO_DESPUES_PUNTO, false);

            tkNumFloat = new Token("Erro", TokenTipo.ERROR);
            cient = true;
        }

        if (cient == false) {
            tkNumFloat = new Token(this.lexema.toString(), TokenTipo.NUM_FLOAT, this.linea, this.columna);
            lectorDeArchivo.rollbackChar();
        }
        return tkNumFloat;
    }

    /**
     * se guarda el token de numero cientifico
     *
     * @param tokenTipo
     * @return
     * @throws IOException
     */
    private Token tokenNumCientifico(TokenTipo tokenTipo) throws IOException {
        Token tkNumCientifico = null;
        char c;
        boolean firstTime = true;
        try {
            c = lectorDeArchivo.getNextChar();
            if (c == '+' || c == '-') {
                this.lexema.append(c);
                firstTime = false;
                c = lectorDeArchivo.getNextChar();
            }
            if (!Util.esDigito(c) && !firstTime) {
                if (Util.esLetra(c) || Util.esPunto(c) || Util.esDolar(c) || Util.esComilla(c)) {
                    this.lexema.append(c);
                }
                throw new EOFException();
            }
            while (true) {
                if (Util.esDigito(c)) {
                    this.lexema.append(c);
                    c = lectorDeArchivo.getNextChar();
                } else {
                    break;
                }
            }
            tkNumCientifico = new Token(this.lexema.toString(), tokenTipo, this.linea, this.columna);
        } catch (EOFException e) {
            Token tk = new Token(this.lexema.toString(), tokenTipo, this.linea, this.columna);
            this.guardarErro(TipoDeError.LEXICO, tk, Mensajes.MSG_LEXICO_CARACTER_INVALIDO_DESPUES_E, false);

            tkNumCientifico = new Token("Erro", TokenTipo.ERROR);
        }
        lectorDeArchivo.rollbackChar();
        return tkNumCientifico;
    }

    /**
     * se obtiene el id del token
     *
     * @return
     * @throws IOException
     */
    private Token tokenId() throws IOException {
        Token idtoken = null;
        char c;

        while (true) {
            c = lectorDeArchivo.getNextChar();
            if ((Util.esLetra(c)) || (Util.esDigito(c)) || (c == '_')) {
                this.lexema.append(c);
            } else {
                lectorDeArchivo.rollbackChar();
                idtoken = TablaSimbolos.getInstance().instalaToken(lexema.toString(), linea, columna);
                lexema = new StringBuilder();
                break;
            }
        }
        return idtoken;
    }

    public void guardarErro(TipoDeError tipoDeErro, Token token, String descripcion, boolean guardarToken) {
        Error erro = new Error(tipoDeErro, token.getLexema(), token.getLinea(), token.getColumna(), descripcion);
        manejadorErrores.guardarErro(erro);
    }

    public void almacenarToken(Token t) {
        this.tokenAlmacenado = t;
    }

}
