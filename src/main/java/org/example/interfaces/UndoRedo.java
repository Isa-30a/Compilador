package org.example.interfaces;

import javax.swing.JTextArea;
import javax.swing.JComponent;
import javax.swing.AbstractAction;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;


public class UndoRedo {
    private UndoManager undoManager;

    public UndoRedo() {
        undoManager = new UndoManager();
    }

    public void add(JTextArea textArea) {
        // Add the UndoableEditListener to the textArea's Document
        textArea.getDocument().addUndoableEditListener(new UndoableEditListener() {
            @Override
            public void undoableEditHappened(UndoableEditEvent e) {
                if (shouldAddEdit(e.getEdit())) {
                    undoManager.addEdit(e.getEdit());
                }
            }
        });

        // Map CTRL+Z to Undo action
        textArea.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK), "Undo");
        textArea.getActionMap().put("Undo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (undoManager.canUndo()) {
                    undoManager.undo();
                }
            }
        });

        // Map CTRL+Y to Redo action
        textArea.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK), "Redo");
        textArea.getActionMap().put("Redo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (undoManager.canRedo()) {
                    undoManager.redo();
                }
            }
        });
    }

    
  private boolean shouldAddEdit(javax.swing.undo.UndoableEdit edit) {
    try {
        String textBeforeEdit = edit.getUndoPresentationName();
        String textAfterEdit = edit.getRedoPresentationName();
        
        // Verificar si la acción es una inserción o un pegado
        return !textBeforeEdit.equals(textAfterEdit) && !textBeforeEdit.isEmpty();
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
  }

}
