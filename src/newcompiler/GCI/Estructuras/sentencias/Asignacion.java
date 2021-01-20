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
public class Asignacion extends Sentencia{

    private Token token; 
    private String value;
    
    
    public Asignacion(Token token){
        this.token = token;
        revisar();
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }
    
    
    
    
    @Override
    public void revisar() {
        GeneracionCodigo.comment("Asignacion de valor");
        GeneracionCodigo.instruccion("mov "+token.getLexema()+", "+value);
        GeneracionCodigo.comment("Fin de asignacion de valor");
        
        
    }
    
}
