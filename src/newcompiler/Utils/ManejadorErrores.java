/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newcompiler.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Hugo Luna
 */
public class ManejadorErrores {

    
    private ArrayList<Error> erros ;

    public ManejadorErrores(){
        erros = new ArrayList<Error>();
    }
   

   

    public  void guardarErro(Error erro) {
        this.erros.add(erro);
    }

    public void mostrarErrores() {
        List<String> errosEmString = new ArrayList<String>();

        for (int i = 0; i < erros.size(); i++) {
            switch (erros.get(i).getErroTipo()) {
                case LEXICO:
                    errosEmString.add("(" + erros.get(i).getLinea() + "," + erros.get(i).getColumna() + ")" + " - " + erros.get(i).getDescripcion());
                    break;

                case SINTACTICO:
                case SEMANTICO:
                    errosEmString.add("| " + "Linea: " + erros.get(i).getLinea() + " / " + erros.get(i).getDescripcion());
                    break;
            }
        }

        if (errosEmString.isEmpty()) {
            System.out.println("======================================================================================");
            System.out.println("|                            CÓDIGO COMPILADO COM EXITO                            |");
            System.out.println("======================================================================================");
        } else {
            System.out.println("======================================================================================");
            System.out.println("|                           INFORME DE ERRORES ENCONTRADOS                           |");
            System.out.println("======================================================================================");

            for (int i = 0; i < errosEmString.size(); i++) {
                System.out.println(Util.completaStringCoEspacios(errosEmString.get(i), 86));
            }

            System.out.println("======================================================================================");
        }
    }

}
