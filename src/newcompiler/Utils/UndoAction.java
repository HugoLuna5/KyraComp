/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newcompiler.Utils;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.undo.UndoManager;

/**
 *
 * @author Hugo Luna
 */
public class UndoAction extends AbstractAction {

    private UndoManager undoManager;
    private RedoAction redoAction;
    private UndoAction undoAction;

    public UndoAction(UndoManager undoManager) {
        this.undoManager = undoManager;
        this.undoAction = this;
        this.putValue(Action.NAME, undoManager.getUndoPresentationName());
        this.setEnabled(false);
    }

    public void setRedoAction(RedoAction redoAction) {
        this.redoAction = redoAction;
    }

    public void actionPerformed(ActionEvent e) {
        if (this.isEnabled()) {
            undoManager.undo();
            undoAction.update();
            redoAction.update();
        }
    }

    public void update() {
        this.putValue(Action.NAME, undoManager.getUndoPresentationName());
        this.setEnabled(undoManager.canUndo());
    }

}
