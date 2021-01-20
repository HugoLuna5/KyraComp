/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newcompiler.GCI.Estructuras.sentencias;

import newcompiler.GCI.GeneracionCodigo;
import newcompiler.Utils.Token;
import newcompiler.Utils.Util;

/**
 *
 * @author Hugo Luna
 */
public class BuclePara extends Sentencia{

    private Asignacion asignacion;
    private Token literal;
    //private Sentencia
    
    public BuclePara(Asignacion asignacion, Token literal){
        this.asignacion = asignacion;
        this.literal = literal;
        revisar();
    
    }
    
    @Override
    public void revisar() {
        String num = new Util().generaIdUnico();
        GeneracionCodigo.comment("Comienzo de bucle Para_"+num);
        
        GeneracionCodigo.label("Para_"+num);
            
            GeneracionCodigo.instruccion("mov eax, "+asignacion.getToken().getLexema());
            GeneracionCodigo.instruccion("mov ebx, "+literal.getLexema());
            GeneracionCodigo.instruccion("cmp eax, ebx");
            

              //Acciones a ejecutar

            //GeneracionCodigo.instruccion("mov ");
            //i = i + 1;
            GeneracionCodigo.instruccion("jb Para_+num");
            
            
            
        
        
    }
    
    
    
    
}
