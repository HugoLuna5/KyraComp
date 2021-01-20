/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newcompiler.Utils;

import java.nio.charset.Charset;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Pattern;

/**
 *
 * @author Hugo Luna
 */
public class Util {

    /**
     * verifica si es letra
     *
     * @param c
     * @return
     */
    public static boolean esLetra(char c) {
        int dec = (int) c;
        
        return (dec > 64 && dec < 91) || (dec > 96 && dec < 123) || (dec == 95);
    }

    /**
     * verifica si el caracter es digito
     *
     * @param c
     * @return
     */
    public static boolean esDigito(char c) {
        int dec = (int) c;
        
        return (dec > 47 && dec < 58);
    }

    /**
     * verifica el caracter que recibio es un punto
     *
     * @param c
     * @return
     */
    public static boolean esPunto(char c) {
        int dec = (int) c;
        return dec == 46;
    }

    /**
     * verifica si el caracter es comilla
     *
     * @param c
     * @return
     */
    public static boolean esComilla(char c) {
        int dec = (int) c;
        return dec == 39;
    }

    /**
     * verifica si el caracter es dolar
     *
     * @param c
     * @return
     */
    public static boolean esDolar(char c) {
        int dec = (int) c;
        return dec == 36;
    }

    /**
     * verfica si desoues de un punto los siguientes caracteres son numeros
     *
     * @param lexema
     * @return
     */
    public static boolean isAllNumbersAfterDot(StringBuilder lexema) {
        String s = lexema.toString();
        char[] c = s.toCharArray();
        int dot = 0;
        boolean verif = true;
        boolean entrou = false;
        for (int i = 0; i < c.length; i++) {
            if (c[i] == '.') {
                dot = i;
            }
        }
        
        for (int i = dot + 1; i < c.length; i++) {
            if ((!Util.esDigito(c[i]))) {
                verif = false;
                entrou = true;
            }
        }
        
        if (entrou == false) {
            verif = false;
        }
        return verif;
    }

    /**
     * une una cadena cuando se encuebtra en espacios
     *
     * @param original
     * @param tamanDeseado
     * @return
     */
    public static String completaStringCoEspacios(String original, int tamanDeseado) {
        String nuevoString = original;
        int tamanhoOriginal = original.length();
        
        for (int i = tamanhoOriginal; i < tamanDeseado - 1; i++) {
            nuevoString += " ";
        }
        
        nuevoString += "|";
        return nuevoString;
    }
    
    public String obtenerSistemaOActual() {
        return System.getProperty("os.name").toLowerCase();
    }
    
    public String getExtensionByStringHandling(String filename) {
        
        String[] parts = filename.split(Pattern.quote("."));
        
        return parts[0];
    }
    
    public void setTimeout(Runnable runnable, int delay) {
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            } catch (Exception e) {
                System.err.println(e);
            }
        }).start();
    }
    
    public String generaIdUnico() {
        Random rand = new Random();        
        int rand_int1 = rand.nextInt(1000);        
        return String.valueOf(rand_int1);
    }
}
