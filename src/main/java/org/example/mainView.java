package org.example;

import javax.swing.*;
import org.example.utils.UndoRedo;

public class mainView extends javax.swing.JFrame {
    NumeroLinea num_linea;
    public UndoRedo undoredo;
    private JScrollPane panelcodigo;

    public mainView() {
        initComponents();
        num_linea = new NumeroLinea(editor);
        panelcodigo.setRowHeaderView(num_linea);
        setLocationRelativeTo(null);
    }

    private void createUIComponents() {
        initComponents();
        num_linea = new NumeroLinea(editor);
        panelcodigo.setRowHeaderView(num_linea);
        setLocationRelativeTo(null);
    }
    private void initComponents() {
        panelcodigo = new javax.swing.JScrollPane();
        editor = new javax.swing.JTextArea();

        undoredo = new UndoRedo();
        undoredo.add(editor);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Compilador IDE");
        setBackground(new java.awt.Color(0, 0, 0));

        editor.setColumns(20);
        editor.setRows(5);
        panelcodigo.setViewportView(editor);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(panelcodigo, javax.swing.GroupLayout.DEFAULT_SIZE, 760, Short.MAX_VALUE)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(42, 42, 42)
                                .addComponent(panelcodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(138, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>
    private javax.swing.JTextArea editor;
}
