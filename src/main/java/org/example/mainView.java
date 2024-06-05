package org.example;
import javax.swing.*;

import org.example.utils.LiveKeywordHighlighter;
import org.example.utils.UndoRedo;
import java.awt.*;

public class mainView extends javax.swing.JFrame {

    private NumeroLinea num_linea;
    private UndoRedo undoredo;
    private JScrollPane panelcodigo;

    public mainView() {
        initComponents();
        num_linea = new NumeroLinea(editor);
        panelcodigo.setRowHeaderView(num_linea);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLookAndFeel();
    }

    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    private void initComponents() {


        panelcodigo = new JScrollPane();
        editor = new  LiveKeywordHighlighter(); // Cambio: Inicializar como JTextPane

        // undoredo = new UndoRedo(); //Está bug, toca arreglarlo
        // undoredo.add(editor);

        setTitle("Tovar IDE");
        setBackground(new java.awt.Color(0, 0, 0));

        editor.setFont(new Font("JetBrainsMono NF", Font.BOLD, 12)); //TODO: Cargar dinamicamente la fuente
        editor.setMargin(new java.awt.Insets(5, 10,5,10));
        // editor.setEditorKit(new StyledEditorKit()); // Necesario para establecer el estilo del texto
        // editor.setFont(new Font("Monospaced", Font.PLAIN, 12)); // Esta línea no es necesaria, ya que el estilo se establece mediante StyledEditorKit
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
  
    private JTextPane editor; // Cambio: Declarar como JTextPane
}
