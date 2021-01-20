/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultEditorKit;
import javax.swing.undo.UndoManager;
import newcompiler.Analisadores.Sintatico;
import newcompiler.GCI.GeneracionCodigo;
import newcompiler.Utils.RedoAction;
import newcompiler.Utils.UndoAction;
import newcompiler.Utils.UndoListener;
import newcompiler.Utils.Util;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import view.VistaInicio;

/**
 *
 * @author Hugo Luna
 */
public class VistaInicioControlador {

    private VistaInicio vistaInicio;
    private JFileChooser selectorDeArchivos;
    private File archivoAbierto;
    
     private UndoManager undoManager;
    private UndoAction undoAction;
    private RedoAction redoAction;
    

    public VistaInicioControlador(VistaInicio vistaInicio) {
      this.vistaInicio = vistaInicio;
        configUndoAndRedo();
        vistaInicio.setVisible(true);
        eventos();
    }

    public VistaInicioControlador(VistaInicio vistaInicio, File archivoAbierto, JFileChooser sChooser) {
        this.vistaInicio = vistaInicio;
         configUndoAndRedo();
        this.archivoAbierto = archivoAbierto;
        this.selectorDeArchivos = sChooser;
        vistaInicio.setVisible(true);
        eventos();
    }
    
    private void configUndoAndRedo() {
        undoManager = new UndoManager();
        undoAction = new UndoAction(undoManager);
        redoAction = new RedoAction(undoManager);
        undoAction.setRedoAction(redoAction);
        redoAction.setUndoAction(undoAction);
        vistaInicio.editor.getDocument().addUndoableEditListener(new UndoListener(undoManager, undoAction, redoAction));
    }

    private void eventos() {
        vistaInicio.accionNuevo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                archivoAbierto = crearNuevoDocumento(vistaInicio.editor);
            }
        });

        vistaInicio.accionAbrir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                archivoAbierto = abrirDocumento();
                if (archivoAbierto != null) {
                    leerDocumento(archivoAbierto, vistaInicio.editor);
                }
            }
        });

        vistaInicio.accionGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (archivoAbierto != null) {
                    archivoAbierto = guardarDocumento(vistaInicio.editor, archivoAbierto);
                }
            }
        });

        vistaInicio.accionGuardarComo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (archivoAbierto != null) {
                    archivoAbierto = guardarDocumentoComo(vistaInicio.editor);
                }
            }
        });

        vistaInicio.accionSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                System.exit(0);
            }
        });

         vistaInicio.deshacer.addActionListener(undoAction);
        
        vistaInicio.rehacer.addActionListener(redoAction);
        
        vistaInicio.cortar.addActionListener((ActionEvent ae) -> new DefaultEditorKit.CutAction());
        
        vistaInicio.copiar.addActionListener((ActionEvent ae) -> new DefaultEditorKit.CopyAction());
        
        vistaInicio.pegar.addActionListener((ActionEvent ae) -> new DefaultEditorKit.PasteAction());
        
        
        vistaInicio.accionCompilar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {

                    archivoAbierto = guardarDocumento(vistaInicio.editor, archivoAbierto);
                    new VistaInicioControlador(vistaInicio, archivoAbierto, selectorDeArchivos);
                    leerDocumento(archivoAbierto, vistaInicio.editor);
                    Sintatico analisadorSintatico = new Sintatico(archivoAbierto.getAbsolutePath().toString());
                    analisadorSintatico.ejecutar(false);

                    new Util().setTimeout(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                analisadorSintatico.iniciarEstructura();
                                //analisadorSintatico.ejecutar(true);
                                new Util().setTimeout(() -> {
                                    try {
                                        GeneracionCodigo.close();
                                    } catch (IOException ex) {
                                        Logger.getLogger(Sintatico.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }, 1500);

                            } catch (IOException ex) {
                                Logger.getLogger(VistaInicioControlador.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        }
                    }, 1500);
                } catch (IOException ex) {
                    Logger.getLogger(VistaInicioControlador.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        vistaInicio.acerca.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
           
                
            }
        });
    }

    public File abrirDocumento() {
        File file = null;
        FileNameExtensionFilter filter = new FileNameExtensionFilter("AN", "an");
        selectorDeArchivos = new JFileChooser("Abrir documento fuente");
        selectorDeArchivos.setFileFilter(new FileNameExtensionFilter("AN", "an"));
        selectorDeArchivos.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int state = selectorDeArchivos.showOpenDialog(selectorDeArchivos);
        if (state == JFileChooser.APPROVE_OPTION) {
            file = selectorDeArchivos.getSelectedFile();
        }

        return file;
    }

    public void leerDocumento(File file, RSyntaxTextArea txt) {

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            txt.read(br, null);
            br.close();

        } catch (IOException ex) {    //en caso de que ocurra una excepción
            JOptionPane.showMessageDialog(vistaInicio, ex.getMessage(), ex.toString(), JOptionPane.ERROR_MESSAGE);
        }

    }

    public File crearNuevoDocumento(RSyntaxTextArea textPane) {
        File f = null;

        selectorDeArchivos = new JFileChooser("Crear documento fuente");
        int estado = selectorDeArchivos.showSaveDialog(selectorDeArchivos);
        if (estado == JFileChooser.APPROVE_OPTION) {
            f = selectorDeArchivos.getSelectedFile();

            BufferedWriter bw;

            try {

                bw = new BufferedWriter(new FileWriter(f));
                f = selectorDeArchivos.getSelectedFile();
                textPane.setText("");
                textPane.write(bw);
                bw.close();

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(vistaInicio, ex.getMessage(), ex.toString(), JOptionPane.ERROR_MESSAGE);
            }

        }

        return f;
    }

    public File guardarDocumento(RSyntaxTextArea textPane, File f) {
        BufferedWriter bw;

        try {

            bw = new BufferedWriter(new FileWriter(f));
            f = selectorDeArchivos.getSelectedFile();

            textPane.write(bw);
            bw.close();

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(vistaInicio, ex.getMessage(), ex.toString(), JOptionPane.ERROR_MESSAGE);
        }

        return f;
    }

    public File guardarDocumentoComo(RSyntaxTextArea textPane) {
        File f = null;

        selectorDeArchivos = new JFileChooser("Guardar documento fuente como");
        int estado = selectorDeArchivos.showSaveDialog(selectorDeArchivos);
        if (estado == JFileChooser.APPROVE_OPTION) {
            f = selectorDeArchivos.getSelectedFile();

            BufferedWriter bw;

            try {

                bw = new BufferedWriter(new FileWriter(f));
                f = selectorDeArchivos.getSelectedFile();
                textPane.write(bw);
                bw.close();

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(vistaInicio, ex.getMessage(), ex.toString(), JOptionPane.ERROR_MESSAGE);
            }

        }

        return f;
    }

}
