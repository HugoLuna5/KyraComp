/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newcompiler.GCI.Estructuras.sentencias;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import newcompiler.GCI.GeneracionCodigo;
import newcompiler.Utils.MapaDerivaciones;
import newcompiler.Utils.Token;
import newcompiler.Utils.Util;

/**
 *
 * @author Hugo Luna
 */
public class Si extends Sentencia{
    
    private Token token;
    //expresion
    private MapaDerivaciones mapaDeDerivaciones;

    public Si(Token token, MapaDerivaciones mapaDeDerivaciones) {
        this.token = token;
        this.mapaDeDerivaciones = mapaDeDerivaciones;
        revisar();
    }

    

    @Override
    public void revisar() {
      
        String num = new Util().generaIdUnico();
            
            GeneracionCodigo.comment("Comienzo del SI_"+num);            
            GeneracionCodigo.instruccion("cmp 10 > 20");
            
            GeneracionCodigo.instruccion("   jmp SI_"+num);
            GeneracionCodigo.instruccion("jmp SINO_"+num);
            
            GeneracionCodigo.instruccion("Label SI_"+num);
            GeneracionCodigo.instruccion("   acciones del si se cumple");
            
            GeneracionCodigo.instruccion("Label SINO_"+num);
            GeneracionCodigo.instruccion("   acciones del si no se cumple");
            //generacionCodigo.escribir(contenido);
           
       
    }
    
    
    
    
    
    
    
}
