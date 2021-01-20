/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newcompiler.Exception;

/**
 *
 * @author Hugo Luna
 */
public class CaractereInvalidoException extends Exception {

    private static final long serialVersionUID = 1L;

    public CaractereInvalidoException(String mensagem){
        super(mensagem);
    }
    
}
