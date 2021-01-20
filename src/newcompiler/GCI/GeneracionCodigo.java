/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newcompiler.GCI;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import newcompiler.Utils.Util;

/**
 *
 * @author Hugo Luna
 */
public class GeneracionCodigo {

    public static GeneracionCodigo GCI;
    private static BufferedWriter bw;
    

    private GeneracionCodigo() {
        GCI = this;
        bw = null;

    }

    public static void setBuffer(BufferedWriter buffer) {
        bw = buffer;
    }

    /* Escribe una secuencia de caracteres dada en la misma linea actual. */
    public static void write(String msg) {
        try {
            if (msg != null) {
                bw.write(msg);
            } else {
                bw.write("");
            }
        } catch (IOException e) {
            System.out.println("Error al intentar escribir el archivo de salida.");
        }
    }

    /* Escribe un label en una misma linea (ojo, no baja de linea!) */
    public static void label(String lbl) {
        write(lbl + ": ");
    }


    public static void instruccionHeader(String inst) {
        write(inst+"\n");
    }
    
    public static void instruccion(String inst) {
        write("     "+inst+"\n");
    }

    /* Escribe un comentario en una nueva linea */
    public static void comment(String com) {
        write("\n; " + com + "\n");
    }


    /* Cierra el Buffered Writer asociado al generador de codigo */
    public static void close() throws IOException {
        if (bw != null) {
            bw.close();
        }
    }

}
