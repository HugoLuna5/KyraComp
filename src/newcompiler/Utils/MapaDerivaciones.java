/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newcompiler.Utils;

import newcompiler.Estructura.EstructuraBase;

/**
 *
 * @author Hugo Luna
 */
public class MapaDerivaciones {
 
    
    public EstructuraBase derivacaoS = new EstructuraBase();
    public EstructuraBase derivacaoBloco = new EstructuraBase();
    public EstructuraBase derivacaoCmds = new EstructuraBase();
    public EstructuraBase derivacaoIfflw = new EstructuraBase();
    public EstructuraBase derivacaoIdflw = new EstructuraBase();
    public EstructuraBase derivacaoDcflw = new EstructuraBase();
    public EstructuraBase derivacaoCmd = new EstructuraBase();
    public EstructuraBase derivacaoDecl = new EstructuraBase();
    public EstructuraBase derivacaoCond = new EstructuraBase();
    public EstructuraBase derivacaoCndb = new EstructuraBase();
    public EstructuraBase derivacaoAtrib = new EstructuraBase();
    public EstructuraBase derivacaoExp = new EstructuraBase();
    public EstructuraBase derivacaoExpl = new EstructuraBase();
    public EstructuraBase derivacaoLogflw = new EstructuraBase();
    public EstructuraBase derivacaoGenflw = new EstructuraBase();
    public EstructuraBase derivacaoGenflw1 = new EstructuraBase();
    public EstructuraBase derivacaoGenflw2 = new EstructuraBase();
    public EstructuraBase derivacaoGenflw3 = new EstructuraBase();
    public EstructuraBase derivacaoExpr = new EstructuraBase();
    public EstructuraBase derivacaoExpn = new EstructuraBase();
    public EstructuraBase derivacaoExpn1 = new EstructuraBase();
    public EstructuraBase derivacaoTermon = new EstructuraBase();
    public EstructuraBase derivacaoTermon1 = new EstructuraBase();
    public EstructuraBase derivacaoValn = new EstructuraBase();
    public EstructuraBase derivacaoRep = new EstructuraBase();
    public EstructuraBase derivacaoRepf = new EstructuraBase();
    public EstructuraBase derivacaoRepw = new EstructuraBase();
    public EstructuraBase imprimir = new EstructuraBase();

    public MapaDerivaciones(){
        derivacaoS.agrega(TokenTipo.PROGRAM);

        derivacaoBloco.agrega(TokenTipo.WHILE);
        derivacaoBloco.agrega(TokenTipo.ID);
        derivacaoBloco.agrega(TokenTipo.FOR);
        derivacaoBloco.agrega(TokenTipo.IF);
        derivacaoBloco.agrega(TokenTipo.DECLARE);
        derivacaoBloco.agrega(TokenTipo.BEGIN);

        derivacaoCmds.agrega(TokenTipo.WHILE);
        derivacaoCmds.agrega(TokenTipo.ID);
        derivacaoCmds.agrega(TokenTipo.FOR);
        derivacaoCmds.agrega(TokenTipo.IF);
        derivacaoCmds.agrega(TokenTipo.DECLARE);
        derivacaoCmds.agrega(TokenTipo.END);

        derivacaoIfflw.agrega(TokenTipo.L_PAR);
        derivacaoIfflw.agrega(TokenTipo.WHILE);
        derivacaoIfflw.agrega(TokenTipo.FOR);
        derivacaoIfflw.agrega(TokenTipo.ID);

        derivacaoIdflw.agrega(TokenTipo.ATTRIB_OP);

        derivacaoDcflw.agrega(TokenTipo.ID);

        derivacaoCmd.agrega(TokenTipo.WHILE);
        derivacaoCmd.agrega(TokenTipo.ID);
        derivacaoCmd.agrega(TokenTipo.FOR);
        derivacaoCmd.agrega(TokenTipo.IF);
        derivacaoCmd.agrega(TokenTipo.DECLARE);

        derivacaoDecl.agrega(TokenTipo.DECLARE);

        derivacaoCond.agrega(TokenTipo.IF);

        derivacaoCndb.agrega(TokenTipo.WHILE);
        derivacaoCndb.agrega(TokenTipo.ID);
        derivacaoCndb.agrega(TokenTipo.FOR);
        derivacaoCndb.agrega(TokenTipo.ELSE);
        derivacaoCndb.agrega(TokenTipo.IF);
        derivacaoCndb.agrega(TokenTipo.DECLARE);
        derivacaoCndb.agrega(TokenTipo.END);
        derivacaoCndb.agrega(TokenTipo.END_PROGRAM);

        derivacaoAtrib.agrega(TokenTipo.ID);

        derivacaoExp.agrega(TokenTipo.L_PAR);
        derivacaoExp.agrega(TokenTipo.ID);
        derivacaoExp.agrega(TokenTipo.NUM_FLOAT);
        derivacaoExp.agrega(TokenTipo.NUM_INT);
        derivacaoExp.agrega(TokenTipo.LOGIC_VAL);
        derivacaoExp.agrega(TokenTipo.LITERAL);

        derivacaoExpl.agrega(TokenTipo.L_PAR);
        derivacaoExpl.agrega(TokenTipo.ID);
        derivacaoExpl.agrega(TokenTipo.NUM_FLOAT);
        derivacaoExpl.agrega(TokenTipo.NUM_INT);
        derivacaoExpl.agrega(TokenTipo.LOGIC_VAL);

        derivacaoLogflw.agrega(TokenTipo.R_PAR);
        derivacaoLogflw.agrega(TokenTipo.TERM);
        derivacaoLogflw.agrega(TokenTipo.LOGIC_OP);

        derivacaoGenflw.agrega(TokenTipo.R_PAR);
        derivacaoGenflw.agrega(TokenTipo.TERM);
        derivacaoGenflw.agrega(TokenTipo.LOGIC_OP);

        derivacaoGenflw1.agrega(TokenTipo.R_PAR);
        derivacaoGenflw1.agrega(TokenTipo.WHILE);
        derivacaoGenflw1.agrega(TokenTipo.TO);
        derivacaoGenflw1.agrega(TokenTipo.ID);
        derivacaoGenflw1.agrega(TokenTipo.MULTDIV_OP);
        derivacaoGenflw1.agrega(TokenTipo.ADDSUB_OP);
        derivacaoGenflw1.agrega(TokenTipo.REL_OP);
        derivacaoGenflw1.agrega(TokenTipo.TERM);
        derivacaoGenflw1.agrega(TokenTipo.IF);
        derivacaoGenflw1.agrega(TokenTipo.DECLARE);
        derivacaoGenflw1.agrega(TokenTipo.BEGIN);
        derivacaoGenflw1.agrega(TokenTipo.LOGIC_OP);

        derivacaoGenflw2.agrega(TokenTipo.R_PAR);
        derivacaoGenflw2.agrega(TokenTipo.REL_OP);
        derivacaoGenflw2.agrega(TokenTipo.TERM);

        derivacaoGenflw3.agrega(TokenTipo.R_PAR);
        derivacaoGenflw3.agrega(TokenTipo.LOGIC_OP);
        derivacaoGenflw3.agrega(TokenTipo.TERM);

        derivacaoExpr.agrega(TokenTipo.L_PAR);
        derivacaoExpr.agrega(TokenTipo.ID);
        derivacaoExpr.agrega(TokenTipo.NUM_FLOAT);
        derivacaoExpr.agrega(TokenTipo.NUM_INT);

        derivacaoExpn.agrega(TokenTipo.L_PAR);
        derivacaoExpn.agrega(TokenTipo.ID);
        derivacaoExpn.agrega(TokenTipo.NUM_FLOAT);
        derivacaoExpn.agrega(TokenTipo.NUM_INT);

        derivacaoExpn1.agrega(TokenTipo.R_PAR);
        derivacaoExpn1.agrega(TokenTipo.ID);
        derivacaoExpn1.agrega(TokenTipo.WHILE);
        derivacaoExpn1.agrega(TokenTipo.TO);
        derivacaoExpn1.agrega(TokenTipo.FOR);
        derivacaoExpn1.agrega(TokenTipo.ADDSUB_OP);
        derivacaoExpn1.agrega(TokenTipo.REL_OP);
        derivacaoExpn1.agrega(TokenTipo.TERM);
        derivacaoExpn1.agrega(TokenTipo.IF);
        derivacaoExpn1.agrega(TokenTipo.DECLARE);
        derivacaoExpn1.agrega(TokenTipo.BEGIN);
        derivacaoExpn1.agrega(TokenTipo.LOGIC_OP);

        derivacaoTermon.agrega(TokenTipo.L_PAR);
        derivacaoTermon.agrega(TokenTipo.ID);
        derivacaoTermon.agrega(TokenTipo.NUM_FLOAT);
        derivacaoTermon.agrega(TokenTipo.NUM_INT);

        derivacaoTermon1.agrega(TokenTipo.R_PAR);
        derivacaoTermon1.agrega(TokenTipo.ID);
        derivacaoTermon1.agrega(TokenTipo.WHILE);
        derivacaoTermon1.agrega(TokenTipo.TO);
        derivacaoTermon1.agrega(TokenTipo.FOR);
        derivacaoTermon1.agrega(TokenTipo.MULTDIV_OP);
        derivacaoTermon1.agrega(TokenTipo.ADDSUB_OP);
        derivacaoTermon1.agrega(TokenTipo.REL_OP);
        derivacaoTermon1.agrega(TokenTipo.IF);
        derivacaoTermon1.agrega(TokenTipo.TERM);
        derivacaoTermon1.agrega(TokenTipo.BEGIN);
        derivacaoTermon1.agrega(TokenTipo.DECLARE);
        derivacaoTermon1.agrega(TokenTipo.LOGIC_OP);

        derivacaoValn.agrega(TokenTipo.L_PAR);
        derivacaoValn.agrega(TokenTipo.ID);
        derivacaoValn.agrega(TokenTipo.NUM_FLOAT);
        derivacaoValn.agrega(TokenTipo.NUM_INT);

        derivacaoRep.agrega(TokenTipo.WHILE);
        derivacaoRep.agrega(TokenTipo.FOR);

        derivacaoRepf.agrega(TokenTipo.FOR);

        derivacaoRepw.agrega(TokenTipo.WHILE);
        
        imprimir.agrega(TokenTipo.PRINT);
        imprimir.agrega(TokenTipo.L_PAR);
        imprimir.agrega(TokenTipo.COMILLAS);
        imprimir.agrega(TokenTipo.TEXTO);
        imprimir.agrega(TokenTipo.R_PAR);
    }


    
}
