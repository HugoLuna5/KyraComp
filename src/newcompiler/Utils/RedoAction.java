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
public class RedoAction extends AbstractAction {

    private UndoManager undoManager;
    private RedoAction redoAction;
    private UndoAction undoAction;

    public RedoAction(UndoManager undoManager) {
        this.undoManager = undoManager;
        this.redoAction = this;
        this.putValue(Action.NAME, undoManager.getRedoPresentationName());
        this.setEnabled(false);
    }

    public void setUndoAction(UndoAction undoAction) {
        this.undoAction = undoAction;
    }

    public void actionPerformed(ActionEvent e) {
        if (this.isEnabled()) {
            undoManager.redo();
            undoAction.update();
            redoAction.update();
        }
    }

    public void update() {
        this.putValue(Action.NAME, undoManager.getRedoPresentationName());
        this.setEnabled(undoManager.canRedo());
    }
}
