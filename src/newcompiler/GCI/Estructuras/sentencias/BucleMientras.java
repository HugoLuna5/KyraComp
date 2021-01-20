/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newcompiler.GCI.Estructuras.sentencias;

import java.util.ArrayList;
import newcompiler.GCI.GeneracionCodigo;
import newcompiler.Utils.Token;
import newcompiler.Utils.Util;

/**
 *
 * @author Hugo Luna
 */
public class BucleMientras extends Sentencia {

    private Token token;
    private ArrayList<Token> tokens;
    //valor 1
    //simbolo
    //valor 2

    //valor booleano
    public BucleMientras(Token token, ArrayList<Token> tokens) {
        this.token = token;
        this.tokens = tokens;

        revisar();
    }

    @Override
    public void revisar() {

        String num = new Util().generaIdUnico();
        GeneracionCodigo.comment("Comienzo del ciclo Mientras_" + num);
        GeneracionCodigo.label("mientras_" + num);

        if (tokens.size() <= 0) {//Valor de tipo booleano

            String valor = "verdadero";

            if (valor.equals("verdadero")) {
                GeneracionCodigo.instruccion("mov eax, 1");
                GeneracionCodigo.instruccion("mov ebx, 1");
                GeneracionCodigo.instruccion("cmp byte eax, ebx");
                    GeneracionCodigo.instruccion("jne mientras_"+num);
            } else {//Falso
                GeneracionCodigo.instruccion("mov eax, 0");
                GeneracionCodigo.instruccion("mov ebx, 1");
                GeneracionCodigo.instruccion("cmp byte eax, ebx");
                    GeneracionCodigo.instruccion("jne mientras_"+num);
            }
            
            /**
             * Instrucciones
             */
            
            GeneracionCodigo.comment("Sentencias dentro del mientras");

           
        } else {
            GeneracionCodigo.instruccion("mov eax, 22");
            GeneracionCodigo.instruccion("mov ebx, 2");
            
            //Comparacion o simbolo
        }

    }

}
