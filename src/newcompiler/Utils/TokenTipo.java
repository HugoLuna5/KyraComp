/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newcompiler.Utils;

/**
 *
 * @author Hugo Luna
 */
public enum TokenTipo {

    NUM_INT(1), NUM_FLOAT(2), LITERAL(3), ID(4), REL_OP(5), ADDSUB_OP(6), MULTDIV_OP(7), ATTRIB_OP(8), TERM(9), L_PAR(
            10), R_PAR(11), LOGIC_VAL(12), LOGIC_OP(13), TYPE(14), PROGRAM(15), END_PROGRAM(16), BEGIN(17), END(18), IF(
            19), THEN(20), ELSE(21), FOR(22), WHILE(23), DECLARE(24), TO(25), EOF(26), ERROR(27), PRINT(28), READ(29), MODULO(30), POTENCIA(31), COMILLAS(32), TEXTO(33);

    private int id;

    TokenTipo(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
