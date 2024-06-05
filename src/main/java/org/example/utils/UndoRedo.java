package org.example.utils;
import javax.swing.JTextPane;
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

    public void add(JTextPane textPane) {
        // Add the UndoableEditListener to the textPane's Document
        textPane.getDocument().addUndoableEditListener(new UndoableEditListener() {
            @Override
            public void undoableEditHappened(UndoableEditEvent e) {
                System.out.println("Edit Happend: " + e.getEdit().getPresentationName());
                undoManager.addEdit(e.getEdit());
            }
        });

        // Map CTRL+Z to Undo action
        textPane.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK), "Undo");
        textPane.getActionMap().put("Undo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (undoManager.canUndo()) {
                    undoManager.undo();
                    System.out.println("Undo action: " + undoManager.getUndoPresentationName());
                }
            }
        });

        // Map CTRL+Y to Redo action
        textPane.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK), "Redo");
        textPane.getActionMap().put("Redo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (undoManager.canRedo()) {
                    undoManager.redo();
                    System.out.println("Redo action: " + undoManager.getRedoPresentationName());
                }
            }
        });
    }
}
