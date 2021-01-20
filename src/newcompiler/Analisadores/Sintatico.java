/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newcompiler.Analisadores;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import newcompiler.Utils.*;
import newcompiler.Utils.Error;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import newcompiler.GCI.Estructuras.sentencias.Declaracion;
import newcompiler.GCI.Estructuras.sentencias.Imprimir;
import newcompiler.GCI.Estructuras.sentencias.Si;
import newcompiler.GCI.GeneracionCodigo;
import newcompiler.Modelo.ListaVariable;

/**
 *
 * @author Hugo Luna
 */
public class Sintatico {

    Lexico analizadorLexico;
    private Token token;
    private MapaDerivaciones mapaDeDerivaciones;
    private ManejadorErrores manejadorErrores;

    private String path; //C:\Users\Hugo Luna\Desktop\Otros\Compilador Ana\prueba.an
    private FileWriter fileWriter;
    private File fileDato;
    private String parent;
    private String name;
    private ArrayList<ListaVariable> listaVariables;
    private ArrayList<ListaVariable> listaConstantes;
    private boolean generateCode = false;

    /**
     * constructor de la clase
     *
     * @param path
     * @throws IOException
     */
    public Sintatico(String path) throws IOException {
        this.path = path;
        this.manejadorErrores = new ManejadorErrores();
        this.analizadorLexico = new Lexico(path, manejadorErrores);
        this.mapaDeDerivaciones = new MapaDerivaciones();
        this.listaVariables = new ArrayList<ListaVariable>();
        this.listaConstantes = new ArrayList<ListaVariable>();
    }

    /**
     * ejecuta los metodos de la clase
     */
    public void ejecutar(boolean generateCode) throws IOException {
        this.generateCode = generateCode;
        verificaEstructuraDelPrograma();
        //  manejadorErrores = new ManejadorErrores();
        manejadorErrores.mostrarErrores();
        

    }

    public void iniciarEstructura() throws IOException {

        fileDato = new File(path);
        parent = fileDato.getParent();
        name = new Util().getExtensionByStringHandling(fileDato.getName());

        String nuevoArchivoASM = parent + "\\" + name + ".asm";
        fileWriter = new FileWriter(nuevoArchivoASM);
        BufferedWriter bw = new BufferedWriter(fileWriter);

        GeneracionCodigo.setBuffer(bw);

        GeneracionCodigo.comment(name.toUpperCase());
        GeneracionCodigo.comment("Compile with: nasm -f elf " + name + ".asm");
        GeneracionCodigo.comment("Link with (64 bit systems require elf_i386 option): ld -m elf_i386 " + name + ".o -o " + name);
        GeneracionCodigo.comment("Run with: ./" + name);
        GeneracionCodigo.instruccionHeader("%include        'C:\\Users\\Hugo Luna\\Documents\\NetBeansProjects\\NewCompiler\\ASMPlantillas\\functions.asm'");
        GeneracionCodigo.instruccionHeader("SECTION .bss");
        GeneracionCodigo.instruccionHeader("dummy    resd 1 ");
        for (int i = 0; i < listaVariables.size(); i++) {
            new Declaracion(listaVariables.get(i).getType(), listaVariables.get(i).getName());
        }
        GeneracionCodigo.instruccionHeader("SECTION .data");
        for (int i = 0; i < listaConstantes.size(); i++) {
             String etq = new Util().generaIdUnico();
        GeneracionCodigo.instruccion("msg"+i+"   db  '"+listaConstantes.get(i).getName()+"', 0Ah");
            
        }

        //cadenas consola
        GeneracionCodigo.instruccionHeader("SECTION .text");
        GeneracionCodigo.instruccionHeader("global  _start");
        GeneracionCodigo.label("_start");
        GeneracionCodigo.instruccion("\n");
        
        
        
     
    }

    /**
     * verifica quela estructura del programa ests bien compuesta
     */
    public void verificaEstructuraDelPrograma() throws IOException {
        token = analizadorLexico.getNextToken();

        if (!token.getTipoToken().equals(TokenTipo.PROGRAM)) {
            this.guardarErro(TipoDeError.SINTACTICO, token, Mensajes.MSG_FALTA_PROGRAM, true);
        } else {
            token = analizadorLexico.getNextToken();
        }

        if (!token.getTipoToken().equals(TokenTipo.ID)) {
            this.guardarErro(TipoDeError.SINTACTICO, token, Mensajes.MSG_FALTA_PROGRAM, true);
        } else {
            token = analizadorLexico.getNextToken();
        }

        if (!token.getTipoToken().equals(TokenTipo.TERM)) {
            this.guardarErro(TipoDeError.SINTACTICO, token, Mensajes.MSG_FALTA_INSERTAR_NOMBRE_PROGRAMA, true);
            separarPorBloque();
            token = analizadorLexico.getNextToken();
        } else {
            token = analizadorLexico.getNextToken();
            separarPorBloque();
            token = analizadorLexico.getNextToken();
        }

        if (!token.getTipoToken().equals(TokenTipo.END_PROGRAM)) {
            this.guardarErro(TipoDeError.SINTACTICO, token, Mensajes.MSG_FALTA_PUNTOCOMA_FIN_PROGRAMA, true);
        } else {
            token = analizadorLexico.getNextToken();

        }

        if (!token.getTipoToken().equals(TokenTipo.TERM)) {
            this.guardarErro(TipoDeError.SINTACTICO, token, Mensajes.MSG_FALTA_INRODUCIR_TERMINO, true);
        } else {
            token = analizadorLexico.getNextToken();
        }
    }

    /**
     * Verifica si un bloque esta bien compuesto con un inicio y un fin
     */
    public void separarPorBloque() throws IOException {
        if (token.getTipoToken().equals(TokenTipo.BEGIN)) {
            token = analizadorLexico.getNextToken();
            separarPorInstrucciones();
            token = analizadorLexico.getNextToken();

            if (!token.getTipoToken().equals(TokenTipo.END)) {
                this.guardarErro(TipoDeError.SINTACTICO, token, Mensajes.MSG_INSERTAR_FIN, true);
            }
        } else {
            if (!mapaDeDerivaciones.derivacaoCmd.verificaSiContiene(token.getTipoToken())) {
                this.guardarErro(TipoDeError.SINTACTICO, token, Mensajes.MSG_INSERTAR_FIN, true);
            } else {
                separarInstruccion();
            }
        }
    }

    /**
     * separa o verifica que la estructura del if este bien escrito
     */
    public void separarPorInstrucciones() throws IOException {

        //Busca si el token actual es igual a la palabara reservada de declaración de varibales
        if (token.getTipoToken().equals(TokenTipo.DECLARE)) {
            token = analizadorLexico.getNextToken();

            verificarDeclaracionyTipoDeDato();
        } else if (token.getTipoToken().equals(TokenTipo.IF)) {
            token = analizadorLexico.getNextToken();
            sacarContenidoParentesis();
        } else if (mapaDeDerivaciones.derivacaoRepf.verificaSiContiene(token.getTipoToken())) {
            estructuraPara();
            token = analizadorLexico.getNextToken();
            separarPorInstrucciones();
        } else if (token.getTipoToken().equals(TokenTipo.PRINT)) {//Token = imprimir

            token = analizadorLexico.getNextToken(); //Token (
            sacarContenidoAImprimirTexto();

        } else if (token.getTipoToken().equals(TokenTipo.READ)) {  //Token = leer //Leer datos
            token = analizadorLexico.getNextToken(); //Token = (
        } else if (mapaDeDerivaciones.derivacaoRepw.verificaSiContiene(token.getTipoToken())) {
            separarBucle();
            token = analizadorLexico.getNextToken();
            separarPorInstrucciones();
        } else if (token.getTipoToken().equals(TokenTipo.ID)) {

            /*
            Obtener el nombre de la variable donde se 
            guardara el dato
             */
            System.out.println(token.getLexema());

            if (!TablaSimbolos.getInstance().get(token.getLexema()).fueDeclarado()) {
                this.guardarErro(TipoDeError.SEMANTICO, token, Mensajes.MSG_VARIABLE_NO_ENCONTRADA + token.getLexema(),
                        false);
            }

            token = analizadorLexico.getNextToken();
            separarAsignacion();
        } else {
            analizadorLexico.almacenarToken(token);
        }
    }

    public void sacarContenidoAImprimirTexto() throws IOException {
        //Token = (
        if (!token.getTipoToken().equals(TokenTipo.L_PAR)) {
            this.guardarErro(TipoDeError.SINTACTICO, token, Mensajes.MSG_PARENTESIS_IMPRIMIIR, true);
        } else {
            token = analizadorLexico.getNextToken(); //Token = "
            comprobarComillas();
        }

    }

    public void comprobarComillas() throws IOException {
        if (!token.getTipoToken().equals(TokenTipo.COMILLAS)) {
            this.guardarErro(TipoDeError.SINTACTICO, token, Mensajes.MSG_COMILLAS_IMPRIMIR, true);
        } else {
            token = analizadorLexico.getNextToken(); //Token = Contenido

            System.out.println(token.getLexema());
            token = analizadorLexico.getNextToken(); //Token = "

            if (!token.getTipoToken().equals(TokenTipo.COMILLAS)) {
                this.guardarErro(TipoDeError.SINTACTICO, token, Mensajes.MSG_COMILLAS_IMPRIMIR, true);
            } else {
                token = analizadorLexico.getNextToken();//Token = )

                if (!token.getTipoToken().equals(TokenTipo.R_PAR)) {
                    this.guardarErro(TipoDeError.SINTACTICO, token, Mensajes.MSG_PARENTESIS_IMPRIMIIR, true);
                } else {
                    token = analizadorLexico.getNextToken(); //Token = ;

                    if (!token.getTipoToken().equals(TokenTipo.TERM)) {
                        this.guardarErro(TipoDeError.SINTACTICO, token, Mensajes.MSG_FALTA_INRODUCIR_TERMINO, true);
                    } else {

                        token = analizadorLexico.getNextToken();
                        separarPorInstrucciones();

                    }

                }

            }

        }
    }

    /**
     * verfica si hay un parentesis Separarc contenido(Expresión) dentro de
     * parentesis en una estructura If por ejemplo
     */
    public void sacarContenidoParentesis() throws IOException {
        if (!token.getTipoToken().equals(TokenTipo.L_PAR)) {
            this.guardarErro(TipoDeError.SINTACTICO, token, Mensajes.MSG_EXPRESION_PARENTESIS_ABIERTO, true);
        } else {
            token = analizadorLexico.getNextToken();
            separaExpresion();
            token = analizadorLexico.getNextToken();
        }

        if (!token.getTipoToken().equals(TokenTipo.R_PAR)) {
            this.guardarErro(TipoDeError.SINTACTICO, token, Mensajes.MSG_EXPRESION_PARENTESIS_CERRADO, true);
        } else {
            token = analizadorLexico.getNextToken();
        }

        if (!token.getTipoToken().equals(TokenTipo.THEN)) {
            this.guardarErro(TipoDeError.SINTACTICO, token, Mensajes.MSG_ENTONCES_DECLARA_IF, true);
        } else {
            token = analizadorLexico.getNextToken();
            separarPorBloque();
            token = analizadorLexico.getNextToken();
            separarPorInstrucciones();
        }
    }

    /**
     * verifica si una operacion esta correctamente formada
     */
    public void separarAsignacion() throws IOException {

        if (!token.getTipoToken().equals(TokenTipo.ATTRIB_OP)) {
            this.guardarErro(TipoDeError.SINTACTICO, token, Mensajes.MSG_VALOR_VARIABLE, true);
        } else {
            token = analizadorLexico.getNextToken();

            separarPorExpresion();

            token = analizadorLexico.getNextToken();
        }

        if (!token.getTipoToken().equals(TokenTipo.TERM)) {
            this.guardarErro(TipoDeError.SINTACTICO, token, Mensajes.MSG_FALTA_INRODUCIR_TERMINO, true);
        } else {
            token = analizadorLexico.getNextToken();
            separarPorInstrucciones();
        }
    }

    /**
     * verifica si una variable fue declarada
     */
    public void verificarDeclaracionyTipoDeDato() throws IOException {
        if (!token.getTipoToken().equals(TokenTipo.ID)) {
            this.guardarErro(TipoDeError.SINTACTICO, token, Mensajes.MSG_DECLARANOM_DES_VARIABLE, true);
        } else {
            if (token.fueDeclarado()) {
                this.guardarErro(TipoDeError.SEMANTICO, token, Mensajes.MSG_VARIABLE_REDECLARADA + token.getLexema(),
                        false);
            }

            token.setFueDeclarado(true);
            System.out.println(token.getLexema());
            Token var = token;
            token = analizadorLexico.getNextToken();
            String tipo = token.getLexema();
            listaVariables.add(new ListaVariable(var.getLexema(), tipo));
        }

        if (!token.getTipoToken().equals(TokenTipo.TYPE)) {
            this.guardarErro(TipoDeError.SINTACTICO, token, Mensajes.MSG_TIPO_VARIABLE_DECLARADA + token.getLexema(), true);
        } else {
            token = analizadorLexico.getNextToken();
        }

        if (!token.getTipoToken().equals(TokenTipo.TERM)) {
            this.guardarErro(TipoDeError.SINTACTICO, token, Mensajes.MSG_DECLARA_PUNTOYCOMA_TIPVARIABLE, true);
        } else {
            token = analizadorLexico.getNextToken();
            separarPorInstrucciones();
        }
    }

    /**
     * Separar por instruccion (Declaracion, condicion, repeticion, atributos)
     */
    public void separarInstruccion() throws IOException {

        if (mapaDeDerivaciones.derivacaoDecl.verificaSiContiene(token.getTipoToken())) {
            separarDeclaracionVariables();
        } else if (mapaDeDerivaciones.derivacaoCond.verificaSiContiene(token.getTipoToken())) {
            estructuraSi();
        } else if (mapaDeDerivaciones.derivacaoRep.verificaSiContiene(token.getTipoToken())) {
            separarBucle();
        } else if (mapaDeDerivaciones.derivacaoAtrib.verificaSiContiene(token.getTipoToken())) {

            asignarAtributos();
        } else {
            this.guardarErro(TipoDeError.SINTACTICO, token, Mensajes.MSG_CMD_NO_ENCONTRADO, true);
        }
    }

    /**
     * Declaración de variables
     */
    public void separarDeclaracionVariables() {
        if (!token.getTipoToken().equals(TokenTipo.DECLARE)) {
            this.guardarErro(TipoDeError.SINTACTICO, token, Mensajes.MSG_INSERTA__DECLARAR_VARIABLE, true);
        } else {
            token = analizadorLexico.getNextToken();
            System.out.println(token);
        }

        if (!token.getTipoToken().equals(TokenTipo.ID)) {
            this.guardarErro(TipoDeError.SINTACTICO, token, Mensajes.MSG_DECLARANOM_DES_VARIABLE, true);
        } else {
            if (token.fueDeclarado()) {
                this.guardarErro(TipoDeError.SEMANTICO, token, Mensajes.MSG_VARIABLE_REDECLARADA, false);
            }

            token = analizadorLexico.getNextToken();
            token.setFueDeclarado(true);
        }

        if (!token.getTipoToken().equals(TokenTipo.TYPE)) {
            this.guardarErro(TipoDeError.SINTACTICO, token, Mensajes.MSG_TIPO_VARIABLE_DECLARADA, true);
        } else {
            token = analizadorLexico.getNextToken();
        }

        if (!token.getTipoToken().equals(TokenTipo.TERM)) {
            this.guardarErro(TipoDeError.SINTACTICO, token, Mensajes.MSG_DECLARA_PUNTOYCOMA_TIPVARIABLE, true);
        }
    }

    /**
     * verifica si esta bien declarado la condicion
     */
    public void estructuraSi() throws IOException {
        if (!token.getTipoToken().equals(TokenTipo.IF)) {
            this.guardarErro(TipoDeError.SINTACTICO, token, Mensajes.MSG_INICIA_CONDICION_SI, true);
        } else {
            token = analizadorLexico.getNextToken();
        }

        if (!token.getTipoToken().equals(TokenTipo.L_PAR)) {
            this.guardarErro(TipoDeError.SINTACTICO, token, Mensajes.MSG_EXPRESION_PARENTESIS_ABIERTO, true);
        } else {
            token = analizadorLexico.getNextToken();
            System.out.println("Primer valor" + token.getLexema()); //primer valor
            separaExpresion();
            token = analizadorLexico.getNextToken();

        }

        if (!token.getTipoToken().equals(TokenTipo.R_PAR)) {
            this.guardarErro(TipoDeError.SINTACTICO, token, Mensajes.MSG_EXPRESION_PARENTESIS_CERRADO, true);
        } else {
            token = analizadorLexico.getNextToken();
        }

        if (!token.getTipoToken().equals(TokenTipo.THEN)) {
            this.guardarErro(TipoDeError.SINTACTICO, token, Mensajes.MSG_ENTONCES_DECLARA_IF, true);
        } else {
            token = analizadorLexico.getNextToken();
            separarPorBloque();
            token = analizadorLexico.getNextToken();

            separaCondicionSiNo();
        }

        if (generateCode) {
            new Si(token, mapaDeDerivaciones);
        }

    }

    /**
     * Condición else
     */
    public void separaCondicionSiNo() throws IOException {
        if (!token.getTipoToken().equals(TokenTipo.ELSE)) {
            analizadorLexico.almacenarToken(token);
        } else {
            token = analizadorLexico.getNextToken();
            separarPorBloque();
        }
        //crear estructura Si
    }

    /**
     * Asignar atributos
     */
    public void asignarAtributos() throws IOException {
        System.out.println("XD");
        System.out.println(token.getLexema());
        if (!token.getTipoToken().equals(TokenTipo.ID)) {
            this.guardarErro(TipoDeError.SINTACTICO, token, Mensajes.MSG_DECLARA_VARIABLE, true);
        } else {
            if (!TablaSimbolos.getInstance().get(token.getLexema()).fueDeclarado()) {
                this.guardarErro(TipoDeError.SEMANTICO, token, Mensajes.MSG_VARIABLE_NO_ENCONTRADA + token.getLexema(),
                        false);
            }
            token = analizadorLexico.getNextToken();
        }

        if (!token.getTipoToken().equals(TokenTipo.ATTRIB_OP)) {
            this.guardarErro(TipoDeError.SINTACTICO, token, Mensajes.MSG_VALOR_VARIABLE, true);
        } else {
            token = analizadorLexico.getNextToken();
            separarPorExpresion();
            token = analizadorLexico.getNextToken();
        }

        if (!token.getTipoToken().equals(TokenTipo.TERM)) {
            this.guardarErro(TipoDeError.SINTACTICO, token, Mensajes.MSG_FALTA_PUNTOCOMA_FIN_PROGRAMA, true);
        }
    }

    /**
     * verifica el valor del token
     */
    public void separaExpresion() throws IOException {
        if (token.getTipoToken().equals(TokenTipo.LOGIC_VAL)) {
            token = analizadorLexico.getNextToken();
            separarValorLogico();
        } else if (token.getTipoToken().equals(TokenTipo.ID)) {
            if (!TablaSimbolos.getInstance().get(token.getLexema()).fueDeclarado()) {
                this.guardarErro(TipoDeError.SEMANTICO, token, Mensajes.MSG_VARIABLE_NO_ENCONTRADA + token.getLexema(),
                        false);
            }
            token = analizadorLexico.getNextToken();
            derivaGenflw();
        } else if (token.getTipoToken().equals(TokenTipo.NUM_INT)) {
            token = analizadorLexico.getNextToken();
            derivaGenflw1();
        } else if (token.getTipoToken().equals(TokenTipo.NUM_FLOAT)) {
            derivaGenflw1();
        } else if (token.getTipoToken().equals(TokenTipo.L_PAR)) {
            token = analizadorLexico.getNextToken();
            separarPorExpresion();
            token = analizadorLexico.getNextToken();
            if (token.getTipoToken().equals(TokenTipo.R_PAR)) {
                token = analizadorLexico.getNextToken();
                derivaGenflw1();
            } else {
                this.guardarErro(TipoDeError.SINTACTICO, token, Mensajes.MSG_EXPRESION_PARENTESIS_CERRADO, true);
            }
        } else {
            this.guardarErro(TipoDeError.SINTACTICO, token, Mensajes.MSG_EXP_FALTAN, true);
        }
    }

    /**
     * verifica el tipo de variable agregado
     */
    public void separarPorExpresion() throws IOException {

        //Espera un valor logico ; booleano
        if (token.getTipoToken().equals(TokenTipo.LOGIC_VAL)) {
            token = analizadorLexico.getNextToken();
            separarValorLogico();
        } else if (token.getTipoToken().equals(TokenTipo.COMILLAS)) {
            //contenido dentro de comillas
            token = analizadorLexico.getNextToken();//Token = contenido
            separarValorCadena();
        } else if (token.getTipoToken().equals(TokenTipo.ID)) {
            if (!TablaSimbolos.getInstance().get(token.getLexema()).fueDeclarado()) {
                this.guardarErro(TipoDeError.SEMANTICO, token, Mensajes.MSG_VARIABLE_NO_ENCONTRADA + token.getLexema(),
                        false);
            }
            token = analizadorLexico.getNextToken();
            derivaGenflw();
        } else if (token.getTipoToken().equals(TokenTipo.NUM_INT)) {
            token = analizadorLexico.getNextToken();
            //simbolo de la operacion
            derivaGenflw1();
        } else if (token.getTipoToken().equals(TokenTipo.NUM_FLOAT)) {
            token = analizadorLexico.getNextToken();
            derivaGenflw1();
        } else if (token.getTipoToken().equals(TokenTipo.LOGIC_VAL)) {
            separarValorLogico();

        } else if (token.getTipoToken().equals(TokenTipo.L_PAR)) {
            token = analizadorLexico.getNextToken();
            separarPorExpresion();
            token = analizadorLexico.getNextToken();
            if (token.getTipoToken().equals(TokenTipo.R_PAR)) {
                token = analizadorLexico.getNextToken();
                System.out.println(token.getLexema());
                derivaGenflw1();
            } else {
                this.guardarErro(TipoDeError.SINTACTICO, token, Mensajes.MSG_EXPRESION_PARENTESIS_CERRADO, true);
            }
        } else {
            this.guardarErro(TipoDeError.SINTACTICO, token, Mensajes.MSG_EXPL_FALTA, true);
        }
    }

    public void separarValorCadena() throws IOException {

        if (generateCode) {
            new Imprimir(token.getLexema());
        }
        listaConstantes.add(new ListaVariable(token.getLexema(), "imprimir"));
        token = analizadorLexico.getNextToken();

        if (!token.getTipoToken().equals(TokenTipo.COMILLAS)) {
            this.guardarErro(TipoDeError.SINTACTICO, token, Mensajes.MSG_COMILLAS_ATTR, true);

        } else {
            token = analizadorLexico.getNextToken();
            if (!token.getTipoToken().equals(TokenTipo.TERM)) {
                this.guardarErro(TipoDeError.SINTACTICO, token, Mensajes.MSG_FALTA_INRODUCIR_TERMINO, true);
            } else {
                separarPorInstrucciones();
            }

        }
    }

    /**
     * alamcena token de tipo logico
     */
    public void separarValorLogico() throws IOException {
        if (!token.getTipoToken().equals(TokenTipo.LOGIC_OP)) {
            analizadorLexico.almacenarToken(token);
        } else {
            token = analizadorLexico.getNextToken();
            separaExpresion();
        }
    }

    public void derivaGenflw() throws IOException {
        if (token.getTipoToken().equals(TokenTipo.LOGIC_OP)) {
            token = analizadorLexico.getNextToken();
            System.out.println(token.getLexema());
            separaExpresion();
        } else if (mapaDeDerivaciones.derivacaoGenflw1.verificaSiContiene(token.getTipoToken())) {
            derivaGenflw1();
        } else {
            this.guardarErro(TipoDeError.SINTACTICO, token, Mensajes.MSG_GENFLW_FALTA, true);
        }
    }

    /**
     * verifica si el token termino fue declarado para disparar el metodo
     * correspondiente a la operación
     */
    public void derivaGenflw1() {
        if (!mapaDeDerivaciones.derivacaoTermon1.verificaSiContiene(token.getTipoToken())) {
            this.guardarErro(TipoDeError.SINTACTICO, token, Mensajes.MSG_TERMN1_FALTA, true);
        } else {
            separarTermino1();
        }

        if (!mapaDeDerivaciones.derivacaoExpn1.verificaSiContiene(token.getTipoToken())) {
            this.guardarErro(TipoDeError.SINTACTICO, token, Mensajes.MSG_EXPN1_FALTA, true);
        } else {
            token = analizadorLexico.getNextToken();
            derivaExpn1();
        }

        if (!mapaDeDerivaciones.derivacaoGenflw2.verificaSiContiene(token.getTipoToken())) {
            this.guardarErro(TipoDeError.SINTACTICO, token, Mensajes.MSG_GENFLW2_FALTANDO, true);
        } else {
            token = analizadorLexico.getNextToken();
            derivaGenflw2();
        }
    }

    /**
     * verifica si es un token de comparacion
     */
    public void derivaGenflw2() {
        if (!token.getTipoToken().equals(TokenTipo.REL_OP)) {
            analizadorLexico.almacenarToken(token);
        } else {
            token = analizadorLexico.getNextToken();
            derivaExpn();
            token = analizadorLexico.getNextToken();
            derivaGenflw3();
        }
    }

    /**
     * verifica si es token es de tipo logico
     */
    public void derivaGenflw3() {
        if (!token.getTipoToken().equals(TokenTipo.LOGIC_OP)) {
            analizadorLexico.almacenarToken(token);
        } else {
            token = analizadorLexico.getNextToken();
            separaExpresionVerificaRELOP();
        }
    }

    /**
     * verifica que la experswion ya haya sido declarada y pasa al sig token
     */
    public void separaExpresionVerificaRELOP() {
        if (!mapaDeDerivaciones.derivacaoExpn.verificaSiContiene(token.getTipoToken())) {
            this.guardarErro(TipoDeError.SINTACTICO, token, Mensajes.MSG_EXPN_FALTA, true);
        } else {
            derivaExpn();
            token = analizadorLexico.getNextToken();
        }

        if (!token.getTipoToken().equals(TokenTipo.REL_OP)) {
            this.guardarErro(TipoDeError.SINTACTICO, token, Mensajes.MSG_OPERADOR_RELACIONADO, true);
        } else {
            token = analizadorLexico.getNextToken();
            derivaExpn();
        }
    }

    /**
     * verifica si falta algun termino
     */
    public void derivaExpn() {
        if (!mapaDeDerivaciones.derivacaoTermon.verificaSiContiene(token.getTipoToken())) {
            this.guardarErro(TipoDeError.SINTACTICO, token, Mensajes.MSG_TERMON_FALTA, true);
        } else {
            separarTermino();
            derivaExpn1();
        }
    }

    /**
     * verifica si hace falta un dato
     */
    public void derivaExpn1() {
        if (!token.getTipoToken().equals(TokenTipo.ADDSUB_OP)) {
            analizadorLexico.almacenarToken(token);
        } else {
            token = analizadorLexico.getNextToken();
            separarTermino();
            derivaExpn1();
        }
    }

    /**
     * verifica si no hace falta un valor
     */
    public void separarTermino() {
        if (!mapaDeDerivaciones.derivacaoValn.verificaSiContiene(token.getTipoToken())) {
            this.guardarErro(TipoDeError.SINTACTICO, token, Mensajes.MSG_VALN_FALTA, true);
        } else {
            derivaValn();
            token = analizadorLexico.getNextToken();
            separarTermino1();
        }
    }

    /**
     * comprueba si es de tipo mult y div
     */
    public void separarTermino1() {
        if (!token.getTipoToken().equals(TokenTipo.MULTDIV_OP)) {
            analizadorLexico.almacenarToken(token);
        } else {
            token = analizadorLexico.getNextToken();
            derivaValn();
            token = analizadorLexico.getNextToken();
            separarTermino1();
        }
    }

    /**
     * verifica el contenido dentro de los parentesis
     */
    public void derivaValn() {
        if (token.getTipoToken().equals(TokenTipo.ID)) {
            if (!TablaSimbolos.getInstance().get(token.getLexema()).fueDeclarado()) {
                this.guardarErro(TipoDeError.SEMANTICO, token, Mensajes.MSG_VARIABLE_NO_ENCONTRADA + token.getLexema(),
                        false);
            }
        } else if (token.getTipoToken().equals(TokenTipo.L_PAR)) {
            derivaExpn();
            token = analizadorLexico.getNextToken();
            if (!token.getTipoToken().equals(TokenTipo.R_PAR)) {
                this.guardarErro(TipoDeError.SINTACTICO, token, Mensajes.MSG_EXPRESION_PARENTESIS_CERRADO, true);
            }
        } else if ((!token.getTipoToken().equals(TokenTipo.NUM_INT))
                && (!token.getTipoToken().equals(TokenTipo.NUM_FLOAT))) {
            this.guardarErro(TipoDeError.SINTACTICO, token, Mensajes.MSG_VALN_FALTA, true);
        }
    }

    /**
     * verifica el tipo de token para redireccionar a metodo correspondiente
     */
    public void separarBucle() throws IOException {
        if (mapaDeDerivaciones.derivacaoRepf.verificaSiContiene(token.getTipoToken())) {
            estructuraPara();
            token = analizadorLexico.getNextToken();
            separarTermino1();
        } else if (mapaDeDerivaciones.derivacaoRepw.verificaSiContiene(token.getTipoToken())) {
            estructuraMientras();
        } else {
            this.guardarErro(TipoDeError.SINTACTICO, token, Mensajes.MSG_REP_FALTA, true);
        }
    }

    /**
     * verifica la estructura del ciclo FOR
     */
    public void estructuraPara() throws IOException {
        if (!token.getTipoToken().equals(TokenTipo.FOR)) {
            this.guardarErro(TipoDeError.SINTACTICO, token, Mensajes.MSG_AGREGA_EXPRESION_FOR, true);
        } else {
            token = analizadorLexico.getNextToken();
        }

        if (!token.getTipoToken().equals(TokenTipo.ID)) {
            this.guardarErro(TipoDeError.SINTACTICO, token, Mensajes.MSG_CMD_NO_ENCONTRADO, true);
        } else {
            if (!TablaSimbolos.getInstance().get(token.getLexema()).fueDeclarado()) {
                this.guardarErro(TipoDeError.SEMANTICO, token, Mensajes.MSG_CMD_NO_ENCONTRADO, false);
            }
            token = analizadorLexico.getNextToken();
        }

        if (!token.getTipoToken().equals(TokenTipo.ATTRIB_OP)) {
            this.guardarErro(TipoDeError.SINTACTICO, token, Mensajes.MSG_ASIGNA_ATRIBUTO, true);
        } else {
            token = analizadorLexico.getNextToken();
            derivaExpn();
            token = analizadorLexico.getNextToken();
        }

        if (!token.getTipoToken().equals(TokenTipo.TO)) {
            this.guardarErro(TipoDeError.SINTACTICO, token, Mensajes.MSG_EXPR_HACIA, true);
        } else {
            token = analizadorLexico.getNextToken();
            derivaExpn();
            token = analizadorLexico.getNextToken();
            separarPorBloque();
        }
    }

    /**
     * verifica la estructura del ciclo while
     */
    public void estructuraMientras() throws IOException {
        if (!token.getTipoToken().equals(TokenTipo.WHILE)) {
            this.guardarErro(TipoDeError.SINTACTICO, token, Mensajes.MSG_EXPR_MIENTRAS_FALTA, true);
        } else {
            token = analizadorLexico.getNextToken();
        }

        if (!token.getTipoToken().equals(TokenTipo.L_PAR)) {
            this.guardarErro(TipoDeError.SINTACTICO, token, Mensajes.MSG_EXPRESION_PARENTESIS_ABIERTO, true);
        } else {
            token = analizadorLexico.getNextToken();
            separaExpresion();
            token = analizadorLexico.getNextToken();
        }

        if (!token.getTipoToken().equals(TokenTipo.R_PAR)) {
            this.guardarErro(TipoDeError.SINTACTICO, token, Mensajes.MSG_EXPRESION_PARENTESIS_CERRADO, true);
        } else {
            token = analizadorLexico.getNextToken();
            separarPorBloque();
        }
    }

    /**
     * se guardan los errores
     *
     * @param tipoDeErro
     * @param token
     * @param descricao
     * @param guardarToken
     */
    public void guardarErro(TipoDeError tipoDeErro, Token token, String descricao, boolean guardarToken) {
        Error erro = new Error(tipoDeErro, token.getLexema(), token.getLinea(), token.getColumna(), descricao);
        manejadorErrores.guardarErro(erro);
    }

}
