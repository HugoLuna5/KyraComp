/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newcompiler.Estructura;

import java.util.ArrayList;
import newcompiler.Utils.TokenTipo;

/**
 *
 * @author Hugo Luna
 */
public class EstructuraBase implements ListaEstructurasTokens {
    

    protected final ArrayList<TokenTipo> listaDeTokens;

    public EstructuraBase() {
        listaDeTokens = new ArrayList<>();
    }

    @Override
    public void agrega(TokenTipo token) {
        this.listaDeTokens.add(token);
    }

    @Override
    public boolean verificaSiContiene(TokenTipo token) {
        return this.listaDeTokens.contains(token);
    }

    
}
