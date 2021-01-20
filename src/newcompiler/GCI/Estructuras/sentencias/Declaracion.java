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
public class Declaracion extends Sentencia {

   private String tipo;
    private String token;

     public Declaracion(String tipo, String token) {
        this.tipo = tipo;
        this.token = token;
        revisar();
    }
    
    @Override
    public void revisar() {
       

        /**
         * Asignación dinamica de memeoria segun
         * el tipo de dato
         */
        switch (tipo) {
            case "entero":
                GeneracionCodigo.instruccionHeader("_"+token + " resb 4 ; declaración del variable " + token);
                break;
            case "real":
                GeneracionCodigo.instruccionHeader("_"+token + " resq 4 ; declaración del variable " + token);
                break;
            case "texto":
                GeneracionCodigo.instruccionHeader("_"+token + " resw 255 ; declaración del variable " + token);
                break;
            case "logico":
                GeneracionCodigo.instruccionHeader("_"+token + " resb 1 ; declaración del variable " + token);
                break;

        }

    }

}
