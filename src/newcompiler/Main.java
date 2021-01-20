/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newcompiler;

import controller.VistaInicioControlador;
import java.io.IOException;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import mdlaf.MaterialLookAndFeel;
import newcompiler.Analisadores.Sintatico;
import view.VistaInicio;

/**
 *
 * @author Hugo Luna
 */
public class Main {
    
    public Main() throws UnsupportedLookAndFeelException{
        establecerNuevoDiseño();
         new VistaInicioControlador(new VistaInicio());
    }

    public static void main(String[] args) throws UnsupportedLookAndFeelException {
        new Main();
       
    }
    
    
    public void establecerNuevoDiseño() throws UnsupportedLookAndFeelException{
          try {
           UIManager.setLookAndFeel(new MaterialLookAndFeel());
        } catch (NullPointerException nullP) {
            System.err.println("Error: " + nullP.getMessage());
        }
          
    }
    
}
