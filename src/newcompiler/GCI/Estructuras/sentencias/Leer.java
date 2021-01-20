/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newcompiler.GCI.Estructuras.sentencias;

import newcompiler.GCI.GeneracionCodigo;
import newcompiler.Utils.Token;

/**
 *
 * @author Hugo Luna
 */
public class Leer extends Sentencia{
    
    private Token token;//necesita el token leer
    private Token literal;

    public Leer(Token token, Token literal) {
        this.token = token;
        this.literal = literal;
    }

    
    
    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public Token getLiteral() {
        return literal;
    }

    public void setLiteral(Token literal) {
        this.literal = literal;
    }

    @Override
    public void revisar() {
         GeneracionCodigo.comment("Leer dato");
    }

    
  
    
    
}
