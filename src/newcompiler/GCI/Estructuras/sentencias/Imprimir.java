/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newcompiler.GCI.Estructuras.sentencias;

import newcompiler.GCI.GeneracionCodigo;
import newcompiler.Utils.Util;

/**
 *
 * @author Hugo Luna
 */
public class Imprimir extends Sentencia{

    
    private String cadena;
    
    public Imprimir(String cadena){
        this.cadena = cadena;
        revisar();
    }
    
    @Override
    public void revisar() {
        GeneracionCodigo.comment("Imprimir texto");
        GeneracionCodigo.instruccionHeader("SECTION .data");
        String etq = new Util().generaIdUnico();
        GeneracionCodigo.instruccion("msg"+etq+"   db  '"+cadena+"', 0Ah");
        GeneracionCodigo.instruccion("mov   eax, msg"+etq);
        GeneracionCodigo.instruccion("call  sprint");
        
    }
    
}
