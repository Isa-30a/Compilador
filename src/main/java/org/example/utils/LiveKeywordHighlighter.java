package org.example.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;

public class LiveKeywordHighlighter extends JTextPane implements DocumentListener, Runnable {

    private static final Color DEFAULT_COLOR = Color.black;

    private final AttributeSet defaultAttrSet;
    private final Map<String, AttributeSet> keywordAttrMap;

    private final Set<String> keywords;

    
    public LiveKeywordHighlighter() {
        super(); // Llama al constructor de UndoRedo
        StyleContext sc = StyleContext.getDefaultStyleContext();
        //defaultAttrSet = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, DEFAULT_COLOR);

        Map<TextAttribute, Object> attributes = new HashMap<>();
        attributes.put(TextAttribute.TRACKING, 0); // Ajusta este valor para cambiar el espaciado
        Font font = new Font("Roboto Mono", Font.PLAIN, 14).deriveFont(attributes);

        setFont(font);

        // Creamos un mapa de palabras clave y sus respectivos atributos de estilo (colores)
        keywordAttrMap = new HashMap<>();

        // QUIET LIGHT
        keywordAttrMap.put("ALGORITMO", setStyle(new Color(102, 102, 204), true)); // Azul
        keywordAttrMap.put("ENTERO", setStyle(new Color(0, 128, 0), false)); // Verde
        keywordAttrMap.put("ARREGLO", setStyle(new Color(43, 145, 175), false)); // Azul claro
        // keywordAttrMap.put("MATRIZ", setStyle(new Color(97, 50, 170), false)); // Morado
        keywordAttrMap.put("PARA", setStyle(new Color(102, 102, 204), false)); // Azul
        keywordAttrMap.put("HASTA", setStyle(new Color(102, 102, 204), false)); // Azul
        keywordAttrMap.put("HACER", setStyle(new Color(102, 102, 204), false)); // Azul
        keywordAttrMap.put("EN", setStyle(new Color(102, 102, 204), false)); // Azul
        keywordAttrMap.put("ESCRIBIR", setStyle(new Color(0, 128, 0), false)); // Verde
        keywordAttrMap.put("FIN", setStyle(new Color(102, 102, 204), true)); // Azul
        keywordAttrMap.put("SEGUN", setStyle(new Color(102, 102, 204), false)); // Azul
        keywordAttrMap.put("SI", setStyle(new Color(102, 102, 204), false)); // Azul
        keywordAttrMap.put("NINGUNO", setStyle(new Color(43, 145, 175), false)); // Azul claro
        defaultAttrSet = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, new Color(64, 64, 64));

        // ATOM ONE LIGHT
        // keywordAttrMap.put("ALGORITMO", setStyle(new Color(86, 156, 214), true)); // Azul claro
        // keywordAttrMap.put("ENTERO", setStyle(new Color(86, 182, 194), true)); // Verde azulado
        // keywordAttrMap.put("ARREGLO", setStyle(new Color(198, 120, 221), true)); // Violeta
        // keywordAttrMap.put("MATRIZ", setStyle(new Color(86, 182, 194), true)); // Verde azulado
        // keywordAttrMap.put("PARA", setStyle(new Color(86, 156, 214), true)); // Azul claro
        // keywordAttrMap.put("HASTA", setStyle(new Color(86, 156, 214), true)); // Azul claro
        // keywordAttrMap.put("ESCRIBIR", setStyle(new Color(128, 128, 128), false)); // Gris medio
        // keywordAttrMap.put("FIN", setStyle(new Color(86, 156, 214), true)); // Azul claro
        // keywordAttrMap.put("SEGUN", setStyle(new Color(86, 156, 214), true)); // Azul claro
        // keywordAttrMap.put("SI", setStyle(new Color(86, 156, 214), true)); // Azul claro
        // keywordAttrMap.put("NINGUNO", setStyle(new Color(198, 120, 221), true)); // Violeta
        // keywordAttrMap.put("MALO", setStyle(new Color(204, 0, 0), true)); // Rojo
        // defaultAttrSet = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, new Color(28, 28, 28));

        //SOLARIZED LIGHT
        // keywordAttrMap.put("ALGORITMO", setStyle(new Color(42, 161, 152), true)); // Solarized Light Cyan

        // keywordAttrMap.put("ENTERO", setStyle(new Color(38, 139, 210), true)); // Solarized Light Azul
        // keywordAttrMap.put("ARREGLO", setStyle(new Color(181, 137, 0), true)); // Solarized Light Amarillo
        // keywordAttrMap.put("MATRIZ", setStyle(new Color(38, 139, 210), true)); // Solarized Light Azul
        // keywordAttrMap.put("PARA", setStyle(new Color(42, 161, 152), true)); // Solarized Light Cyan
        // keywordAttrMap.put("HASTA", setStyle(new Color(42, 161, 152), true)); // Solarized Light Cyan
        // keywordAttrMap.put("ESCRIBIR", setStyle(new Color(101, 123, 131), false)); // Solarized Light Gris
        // keywordAttrMap.put("FIN", setStyle(new Color(42, 161, 152), true)); // Solarized Light Cyan
        // keywordAttrMap.put("SEGUN", setStyle(new Color(42, 161, 152), true)); // Solarized Light Cyan
        // keywordAttrMap.put("SI", setStyle(new Color(42, 161, 152), true)); // Solarized Light Cyan
        // keywordAttrMap.put("NINGUNO", setStyle(new Color(181, 137, 0), true)); // Solarized Light Amarillo
        // keywordAttrMap.put("MALO", setStyle(new Color(220, 50, 47), true)); // Solarized Light Rojo
        // defaultAttrSet = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, new Color(131, 148, 150)); 

        keywords = keywordAttrMap.keySet();

       // Crear un nuevo Keymap basado en el Keymap por defecto
       Keymap keymap = JTextComponent.addKeymap("CustomKeymap", getKeymap());

       // Asignar la tecla Tab al nuevo Keymap
       KeyStroke tab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
       keymap.addActionForKeyStroke(tab, new TabAction());

       // Establecer el nuevo Keymap en el JTextPane
       setKeymap(keymap);

        getDocument().addDocumentListener(this);
    }
    
    class TabAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (getSelectedText() != null) { // Si hay texto seleccionado
                // Obtener el documento del JTextPane
                StyledDocument doc = (StyledDocument) getDocument();
    
                // Obtener las posiciones de inicio y fin de la selección
                int start = getSelectionStart();
                int end = getSelectionEnd();
    
                // Obtener el texto seleccionado
                String selectedText = getText().substring(start, end);
    
                // Dividir el texto seleccionado en líneas
                String[] lines = selectedText.split("\n");

                //TODO: REVISAR ESTE BLOQUE

                // Construir el texto con tab aplicado a cada línea
                StringBuilder modifiedText = new StringBuilder();
                for (String line : lines) {
                    modifiedText.append("    ").append(line).append("\n");
                }
    
                // Reemplazar la selección con el texto modificado
                replaceSelection(modifiedText.toString());
                System.out.println("??: " + lines.length);
                // Establecer la nueva posición de inicio de la selección
                setSelectionStart(start + 4);
                setSelectionEnd(end + 4 * lines.length);

                //HASTA ACA


            } else { // Si no hay texto seleccionado, insertar tab normalmente
                replaceSelection("    "); // Reemplazar la selección con cuatro espacios
            }
        }
    }
    
    public static SimpleAttributeSet setStyle(Color color, boolean italic){
         // Crear un conjunto de atributos para la palabra clave
         SimpleAttributeSet attrSet = new SimpleAttributeSet();
         // Agregar atributo de color
         StyleConstants.setForeground(attrSet, color);
         // Agregar atributo de cursiva
         StyleConstants.setItalic(attrSet, italic);


        return attrSet;
    }

    // public static void main(String[] args) {
    //     JFrame frame = new JFrame("LiveKeywordHighlighter");
    //     frame.setContentPane(new JScrollPane(new LiveKeywordHighlighter()));
    //     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //     frame.setSize(300, 200);
    //     frame.setVisible(true);
    // }

    @Override
    public void changedUpdate(DocumentEvent de) {
        // No hacemos nada en caso de actualización
    }

    @Override
    public void insertUpdate(DocumentEvent de) {
        SwingUtilities.invokeLater(this); // Ejecutamos el resaltado en otro hilo
    }

    @Override
    public void removeUpdate(DocumentEvent de) {
        SwingUtilities.invokeLater(this); // Ejecutamos el resaltado en otro hilo
    }

    @Override
    public void run() {
        StyledDocument doc = getStyledDocument();
        String text = "";
        int len = doc.getLength();
        try {
            text = doc.getText(0, len);
        } catch (BadLocationException ble) {
            ble.printStackTrace();
        }

        doc.setCharacterAttributes(0, len, defaultAttrSet, true);

        int wordStart = -1;

        for (int j = 0; j < text.length(); j++) {
            char ch = text.charAt(j);

            if (Character.isLetter(ch)) {
                if (wordStart == -1) {
                    wordStart = j;
                }
            } else {
                if (wordStart != -1) {
                    String word = text.substring(wordStart, j).toUpperCase();
                    if (keywords.contains(word)) {
                        AttributeSet attrSet = keywordAttrMap.get(word);
                        doc.setCharacterAttributes(wordStart, word.length(), attrSet, false);
                    }
                    wordStart = -1;
                }
            }
        }

        if (wordStart != -1) {
            String word = text.substring(wordStart).toUpperCase();
            if (keywords.contains(word)) {
                AttributeSet attrSet = keywordAttrMap.get(word);
                doc.setCharacterAttributes(wordStart, word.length(), attrSet, false);
            }
        }
    }

    @Override
    public void replaceSelection(String content) {
        getInputAttributes().removeAttribute(StyleConstants.Foreground);
        super.replaceSelection(content);
    }
}

