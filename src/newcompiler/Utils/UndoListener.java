/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newcompiler.Utils;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;

/**
 *
 * @author Hugo Luna
 */
public class UndoListener implements UndoableEditListener {

    private UndoManager undoManager;
    private UndoAction undoAction;
    private RedoAction redoAction;

    public UndoListener(UndoManager undoManager, UndoAction undoAction, RedoAction redoAction) {
        this.undoManager = undoManager;
        this.undoAction = undoAction;
        this.redoAction = redoAction;
    }

    @Override
    public void undoableEditHappened(UndoableEditEvent e) {
        undoManager.addEdit(e.getEdit());
        undoAction.update();
        redoAction.update();
    }

}
