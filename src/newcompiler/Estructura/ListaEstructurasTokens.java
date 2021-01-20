/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newcompiler.Estructura;

import newcompiler.Utils.TokenTipo;

/**
 *
 * @author Hugo Luna
 */
public interface ListaEstructurasTokens {
    public void agrega(TokenTipo token);
    public boolean verificaSiContiene(TokenTipo token);
}

