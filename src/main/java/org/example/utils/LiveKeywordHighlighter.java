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

    private AttributeSet defaultAttrSet;
    private final Map<String, AttributeSet> keywordAttrMap;

    private final Set<String> keywords;

    
    public LiveKeywordHighlighter(String type) {
        super(); // Llama al constructor de UndoRedo
        StyleContext sc = StyleContext.getDefaultStyleContext();
        //defaultAttrSet = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, DEFAULT_COLOR);

        Map<TextAttribute, Object> attributes = new HashMap<>();
        attributes.put(TextAttribute.TRACKING, 0); // Ajusta este valor para cambiar el espaciado
        Font font = new Font("Roboto Mono", Font.PLAIN, 14).deriveFont(attributes);

        setFont(font);

        // Creamos un mapa de palabras clave y sus respectivos atributos de estilo (colores)
        keywordAttrMap = new HashMap<>();

        

        switch (type) {
            default:
                keywordAttrMap.put("LEER", setStyle(new Color(204, 102, 0), true)); // Naranja
                keywordAttrMap.put("ESCRIBIR", setStyle(new Color(0, 102, 204), true)); // Azul
                keywordAttrMap.put("INICIO", setStyle(new Color(204, 0, 102), true)); // Rosa
                keywordAttrMap.put("FIN INICIO", setStyle(new Color(204, 0, 102), true)); // Rosa
                keywordAttrMap.put("FUNCION", setStyle(new Color(102, 0, 102), true)); // Morado oscuro
                keywordAttrMap.put("RETORNA", setStyle(new Color(102, 0, 102), true)); // Morado oscuro
                keywordAttrMap.put("FIN FUNCION", setStyle(new Color(102, 0, 102), true)); // Morado oscuro
                keywordAttrMap.put("FIN", setStyle(new Color(102, 0, 102), true)); // Morado oscuro
                keywordAttrMap.put("Y", setStyle(new Color(51, 153, 102), false)); // Verde
                keywordAttrMap.put("O", setStyle(new Color(51, 153, 102), false)); // Verde
                keywordAttrMap.put("!", setStyle(new Color(51, 153, 102), false)); // Verde
                keywordAttrMap.put(":", setStyle(new Color(0, 153, 153), false)); // Turquesa
                keywordAttrMap.put(">", setStyle(new Color(0, 153, 153), false)); // Turquesa
                keywordAttrMap.put("<", setStyle(new Color(0, 153, 153), false)); // Turquesa
                keywordAttrMap.put("==", setStyle(new Color(0, 153, 153), false)); // Turquesa
                keywordAttrMap.put("=", setStyle(new Color(0, 153, 153), false)); // Turquesa
                keywordAttrMap.put(">=", setStyle(new Color(0, 153, 153), false)); // Turquesa
                keywordAttrMap.put("<=", setStyle(new Color(0, 153, 153), false)); // Turquesa
                keywordAttrMap.put("!=", setStyle(new Color(0, 153, 153), false)); // Turquesa
                keywordAttrMap.put("SEGUN", setStyle(new Color(0, 102, 204), true)); // Azul
                keywordAttrMap.put("SI", setStyle(new Color(0, 102, 204), true)); // Azul
                keywordAttrMap.put("ENTONCES", setStyle(new Color(0, 102, 204), true)); // Azul
                keywordAttrMap.put("FINSI", setStyle(new Color(0, 102, 204), true)); // Azul
                keywordAttrMap.put("SINO", setStyle(new Color(0, 102, 204), true)); // Azul
                keywordAttrMap.put("HACER", setStyle(new Color(0, 102, 204), true)); // Azul
                keywordAttrMap.put("CASO", setStyle(new Color(0, 102, 204), true)); // Azul
                keywordAttrMap.put("TERMINAR", setStyle(new Color(0, 102, 204), true)); // Azul
                keywordAttrMap.put("DEFECTO", setStyle(new Color(0, 102, 204), true)); // Azul
                keywordAttrMap.put("FIN SEGUN", setStyle(new Color(0, 102, 204), true)); // Azul
                keywordAttrMap.put("REPETIR", setStyle(new Color(204, 102, 0), true)); // Naranja
                keywordAttrMap.put("MIENTRAS", setStyle(new Color(204, 102, 0), true)); // Naranja
                keywordAttrMap.put("PARA", setStyle(new Color(204, 102, 0), true)); // Naranja
                keywordAttrMap.put("HASTA", setStyle(new Color(204, 102, 0), true)); // Naranja
                keywordAttrMap.put("EN", setStyle(new Color(204, 102, 0), true)); // Naranja
                keywordAttrMap.put("FIN MIENTRAS", setStyle(new Color(204, 102, 0), true)); // Naranja
                keywordAttrMap.put("FIN PARA", setStyle(new Color(204, 102, 0), true)); // Naranja
                keywordAttrMap.put("ENTERO", setStyle(new Color(0, 90, 0), false)); // Verde
                keywordAttrMap.put("FLOTANTE", setStyle(new Color(0, 90, 0), false)); // Verde
                keywordAttrMap.put("CARACTER", setStyle(new Color(0, 90, 0), false)); // Verde
                keywordAttrMap.put("MATRIZ", setStyle(new Color(0, 90, 0), false)); // Verde
                keywordAttrMap.put("BOOLEANO", setStyle(new Color(0, 90, 0), false)); // Verde
                keywordAttrMap.put("VERDADERO", setStyle(new Color(0, 90, 0), false)); // Verde
                keywordAttrMap.put("FALSO", setStyle(new Color(0, 90, 0), false)); // Verde
                keywordAttrMap.put("NINGUNO", setStyle(new Color(30, 101, 122), false)); // Azul claro
                defaultAttrSet = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, new Color(45, 45, 45)); // Gris Oscuro

                break;
        
            case "quiet-light":
                keywordAttrMap.put("LEER LÍNEA", setStyle(new Color(71, 107, 143), true)); // Azul Claro
                keywordAttrMap.put("LEER", setStyle(new Color(71, 107, 143), false)); // Azul Claro
                keywordAttrMap.put("ESCRIBIR", setStyle(new Color(71, 107, 143), false)); // Azul Claro
        
                keywordAttrMap.put("INICIO", setStyle(new Color(71, 143, 71), true)); // Verde Claro
                keywordAttrMap.put("FIN INICIO", setStyle(new Color(71, 143, 71), false)); // Verde Claro
        
                keywordAttrMap.put("FUNCION", setStyle(new Color(143, 71, 107), true)); // Rosa Claro
                keywordAttrMap.put("RETORNA", setStyle(new Color(143, 71, 107), false)); // Rosa Claro
                keywordAttrMap.put("FIN FUNCION", setStyle(new Color(143, 71, 107), false)); // Rosa Claro
        
                keywordAttrMap.put("Y", setStyle(new Color(143, 107, 71), true)); // Naranja Claro
                keywordAttrMap.put("O", setStyle(new Color(143, 107, 71), false)); // Naranja Claro
                keywordAttrMap.put("!", setStyle(new Color(143, 107, 71), false)); // Naranja Claro
                keywordAttrMap.put(">", setStyle(new Color(143, 107, 71), false)); // Naranja Claro
                keywordAttrMap.put("<", setStyle(new Color(143, 107, 71), false)); // Naranja Claro
                keywordAttrMap.put("==", setStyle(new Color(143, 107, 71), false)); // Naranja Claro
                keywordAttrMap.put(">=", setStyle(new Color(143, 107, 71), false)); // Naranja Claro
                keywordAttrMap.put("<=", setStyle(new Color(143, 107, 71), false)); // Naranja Claro
                keywordAttrMap.put("!=", setStyle(new Color(143, 107, 71), false)); // Naranja Claro
        
                keywordAttrMap.put("SEGUN", setStyle(new Color(107, 107, 143), true)); // Azul Grisáceo
                keywordAttrMap.put("SI", setStyle(new Color(107, 107, 143), false)); // Azul Grisáceo
                keywordAttrMap.put("ENTONCES", setStyle(new Color(107, 107, 143), false)); // Azul Grisáceo
                keywordAttrMap.put("FINSI", setStyle(new Color(107, 107, 143), false)); // Azul Grisáceo
                keywordAttrMap.put("SINO", setStyle(new Color(107, 107, 143), false)); // Azul Grisáceo
                keywordAttrMap.put("HACER", setStyle(new Color(107, 107, 143), false)); // Azul Grisáceo
                keywordAttrMap.put("CASO", setStyle(new Color(107, 107, 143), false)); // Azul Grisáceo
                keywordAttrMap.put("TERMINAR", setStyle(new Color(107, 107, 143), false)); // Azul Grisáceo
                keywordAttrMap.put("DEFECTO", setStyle(new Color(107, 107, 143), false)); // Azul Grisáceo
                keywordAttrMap.put("FIN SEGUN", setStyle(new Color(107, 107, 143), false)); // Azul Grisáceo
        
                keywordAttrMap.put("REPETIR", setStyle(new Color(107, 143, 107), true)); // Verde Grisáceo
                keywordAttrMap.put("MIENTRAS", setStyle(new Color(107, 143, 107), false)); // Verde Grisáceo
                keywordAttrMap.put("PARA", setStyle(new Color(107, 143, 107), false)); // Verde Grisáceo
                keywordAttrMap.put("HASTA", setStyle(new Color(107, 143, 107), false)); // Verde Grisáceo
                keywordAttrMap.put("HACER", setStyle(new Color(107, 143, 107), false)); // Verde Grisáceo
                keywordAttrMap.put("EN", setStyle(new Color(107, 143, 107), false)); // Verde Grisáceo
                keywordAttrMap.put("FIN MIENTRAS", setStyle(new Color(107, 143, 107), false)); // Verde Grisáceo
                keywordAttrMap.put("FIN PARA", setStyle(new Color(107, 143, 107), false)); // Verde Grisáceo
        
                keywordAttrMap.put("ENTERO", setStyle(new Color(143, 143, 71), true)); // Amarillo Claro
                keywordAttrMap.put("FLOTANTE", setStyle(new Color(143, 143, 71), false)); // Amarillo Claro
                keywordAttrMap.put("CARACTER", setStyle(new Color(143, 143, 71), false)); // Amarillo Claro
                keywordAttrMap.put("]", setStyle(new Color(143, 143, 71), false)); // Amarillo Claro
                keywordAttrMap.put("[", setStyle(new Color(143, 143, 71), false)); // Amarillo Claro
                keywordAttrMap.put("BOOLEANO", setStyle(new Color(143, 143, 71), false)); // Amarillo Claro
                keywordAttrMap.put("VERDADERO", setStyle(new Color(143, 143, 71), false)); // Amarillo Claro
                keywordAttrMap.put("FALSO", setStyle(new Color(143, 143, 71), false)); // Amarillo Claro
        
                defaultAttrSet = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, new Color(45, 45, 45)); // Gris Oscuro
                break;
        
            case "quiet-light-cpp":
                keywordAttrMap.put("int", setStyle(new Color(143, 143, 71), true)); // Amarillo Claro
                keywordAttrMap.put("float", setStyle(new Color(143, 143, 71), false)); // Amarillo Claro
                keywordAttrMap.put("double", setStyle(new Color(143, 143, 71), false)); // Amarillo Claro
                keywordAttrMap.put("char", setStyle(new Color(143, 143, 71), false)); // Amarillo Claro
                keywordAttrMap.put("bool", setStyle(new Color(143, 143, 71), false)); // Amarillo Claro
                keywordAttrMap.put("void", setStyle(new Color(143, 143, 71), false)); // Amarillo Claro
        
                keywordAttrMap.put("if", setStyle(new Color(107, 107, 143), true)); // Azul Grisáceo
                keywordAttrMap.put("else", setStyle(new Color(107, 107, 143), false)); // Azul Grisáceo
                keywordAttrMap.put("switch", setStyle(new Color(107, 107, 143), false)); // Azul Grisáceo
                keywordAttrMap.put("case", setStyle(new Color(107, 107, 143), false)); // Azul Grisáceo
                keywordAttrMap.put("default", setStyle(new Color(107, 107, 143), false)); // Azul Grisáceo
        
                keywordAttrMap.put("while", setStyle(new Color(107, 143, 107), true)); // Verde Grisáceo
                keywordAttrMap.put("for", setStyle(new Color(107, 143, 107), false)); // Verde Grisáceo
                keywordAttrMap.put("do", setStyle(new Color(107, 143, 107), false)); // Verde Grisáceo
                keywordAttrMap.put("return", setStyle(new Color(107, 143, 107), false)); // Verde Grisáceo
        
                keywordAttrMap.put("cout", setStyle(new Color(71, 107, 143), true)); // Azul Claro
                keywordAttrMap.put("cin", setStyle(new Color(71, 107, 143), false)); // Azul Claro
        
                keywordAttrMap.put("and", setStyle(new Color(143, 107, 71), true)); // Naranja Claro
                keywordAttrMap.put("or", setStyle(new Color(143, 107, 71), false)); // Naranja Claro
                keywordAttrMap.put("not", setStyle(new Color(143, 107, 71), false)); // Naranja Claro
                keywordAttrMap.put("==", setStyle(new Color(143, 107, 71), false)); // Naranja Claro
                keywordAttrMap.put("!=", setStyle(new Color(143, 107, 71), false)); // Naranja Claro
                keywordAttrMap.put("<", setStyle(new Color(143, 107, 71), false)); // Naranja Claro
                keywordAttrMap.put(">", setStyle(new Color(143, 107, 71), false)); // Naranja Claro
                keywordAttrMap.put("<=", setStyle(new Color(143, 107, 71), false)); // Naranja Claro
                keywordAttrMap.put(">=", setStyle(new Color(143, 107, 71), false)); // Naranja Claro
        
                defaultAttrSet = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, new Color(45, 45, 45)); // Gris Oscuro
                break;

                case "paper-color-cpp":
                keywordAttrMap.put("int", setStyle(new Color(163, 105, 85), true)); // Coral Claro
                keywordAttrMap.put("float", setStyle(new Color(163, 105, 85), false)); // Coral Claro
                keywordAttrMap.put("double", setStyle(new Color(163, 105, 85), false)); // Coral Claro
                keywordAttrMap.put("char", setStyle(new Color(163, 105, 85), false)); // Coral Claro
                keywordAttrMap.put("bool", setStyle(new Color(163, 105, 85), false)); // Coral Claro
                keywordAttrMap.put("void", setStyle(new Color(163, 105, 85), false)); // Coral Claro
                
                keywordAttrMap.put("if", setStyle(new Color(122, 167, 167), true)); // Cian Pálido
                keywordAttrMap.put("else", setStyle(new Color(122, 167, 167), false)); // Cian Pálido
                keywordAttrMap.put("switch", setStyle(new Color(122, 167, 167), false)); // Cian Pálido
                keywordAttrMap.put("case", setStyle(new Color(122, 167, 167), false)); // Cian Pálido
                keywordAttrMap.put("default", setStyle(new Color(122, 167, 167), false)); // Cian Pálido
                
                keywordAttrMap.put("for", setStyle(new Color(168, 161, 98), true)); // Amarillo Kaki Claro
                keywordAttrMap.put("while", setStyle(new Color(168, 161, 98), false)); // Amarillo Kaki Claro
                keywordAttrMap.put("do", setStyle(new Color(168, 161, 98), false)); // Amarillo Kaki Claro
                keywordAttrMap.put("break", setStyle(new Color(168, 161, 98), false)); // Amarillo Kaki Claro
                keywordAttrMap.put("continue", setStyle(new Color(168, 161, 98), false)); // Amarillo Kaki Claro
                
                keywordAttrMap.put("return", setStyle(new Color(132, 128, 75), true)); // Amarillo Oscuro
                
                keywordAttrMap.put("true", setStyle(new Color(155, 112, 155), false)); // Lavanda
                keywordAttrMap.put("false", setStyle(new Color(155, 112, 155), false)); // Lavanda
                
                keywordAttrMap.put("class", setStyle(new Color(179, 127, 135), true)); // Rosa Claro
                keywordAttrMap.put("struct", setStyle(new Color(179, 127, 135), false)); // Rosa Claro
                keywordAttrMap.put("public", setStyle(new Color(179, 127, 135), false)); // Rosa Claro
                keywordAttrMap.put("private", setStyle(new Color(179, 127, 135), false)); // Rosa Claro
                keywordAttrMap.put("protected", setStyle(new Color(179, 127, 135), false)); // Rosa Claro
                
                keywordAttrMap.put("cout", setStyle(new Color(101, 167, 101), false)); // Verde Claro
                keywordAttrMap.put("cin", setStyle(new Color(101, 167, 101), false)); // Verde Claro
                keywordAttrMap.put("endl", setStyle(new Color(101, 167, 101), false)); // Verde Claro
                
                keywordAttrMap.put("&&", setStyle(new Color(179, 152, 129), false)); // Melocotón Claro
                keywordAttrMap.put("||", setStyle(new Color(179, 152, 129), false)); // Melocotón Claro
                keywordAttrMap.put("!", setStyle(new Color(179, 152, 129), false)); // Melocotón Claro
                keywordAttrMap.put(">", setStyle(new Color(179, 152, 129), false)); // Melocotón Claro
                keywordAttrMap.put("<", setStyle(new Color(179, 152, 129), false)); // Melocotón Claro
                keywordAttrMap.put("==", setStyle(new Color(179, 152, 129), false)); // Melocotón Claro
                keywordAttrMap.put(">=", setStyle(new Color(179, 152, 129), false)); // Melocotón Claro
                keywordAttrMap.put("<=", setStyle(new Color(179, 152, 129), false)); // Melocotón Claro
                keywordAttrMap.put("!=", setStyle(new Color(179, 152, 129), false)); // Melocotón Claro
                
                defaultAttrSet = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, new Color(70, 70, 70)); // Gris Oscuro
                break;
            
            case "paper-color":
                keywordAttrMap.put("LEER LÍNEA", setStyle(new Color(122, 167, 167), true)); // Cian Pálido
                keywordAttrMap.put("LEER", setStyle(new Color(122, 167, 167), false)); // Cian Pálido
                keywordAttrMap.put("ESCRIBIR", setStyle(new Color(122, 167, 167), false)); // Cian Pálido
                
                keywordAttrMap.put("INICIO", setStyle(new Color(179, 127, 135), true)); // Rosa Claro
                keywordAttrMap.put("FIN INICIO", setStyle(new Color(179, 127, 135), false)); // Rosa Claro
                
                keywordAttrMap.put("FUNCION", setStyle(new Color(163, 105, 85), true)); // Coral Claro
                keywordAttrMap.put("RETORNA", setStyle(new Color(163, 105, 85), false)); // Coral Claro
                keywordAttrMap.put("FIN FUNCION", setStyle(new Color(163, 105, 85), false)); // Coral Claro
                
                keywordAttrMap.put("Y", setStyle(new Color(179, 152, 129), true)); // Melocotón Claro
                keywordAttrMap.put("O", setStyle(new Color(179, 152, 129), false)); // Melocotón Claro
                keywordAttrMap.put("!", setStyle(new Color(179, 152, 129), false)); // Melocotón Claro
                keywordAttrMap.put(">", setStyle(new Color(179, 152, 129), false)); // Melocotón Claro
                keywordAttrMap.put("<", setStyle(new Color(179, 152, 129), false)); // Melocotón Claro
                keywordAttrMap.put("==", setStyle(Color.RED, false)); // Melocotón Claro
                keywordAttrMap.put(">=", setStyle(new Color(179, 152, 129), false)); // Melocotón Claro
                keywordAttrMap.put("<=", setStyle(new Color(179, 152, 129), false)); // Melocotón Claro
                keywordAttrMap.put("!=", setStyle(new Color(179, 152, 129), false)); // Melocotón Claro
                
                keywordAttrMap.put("SEGUN", setStyle(new Color(122, 167, 167), true)); // Cian Pálido
                keywordAttrMap.put("SI", setStyle(new Color(122, 167, 167), false)); // Cian Pálido
                keywordAttrMap.put("ENTONCES", setStyle(new Color(122, 167, 167), false)); // Cian Pálido
                keywordAttrMap.put("FINSI", setStyle(new Color(122, 167, 167), false)); // Cian Pálido
                keywordAttrMap.put("SINO", setStyle(new Color(122, 167, 167), false)); // Cian Pálido
                keywordAttrMap.put("HACER", setStyle(new Color(122, 167, 167), false)); // Cian Pálido
                keywordAttrMap.put("CASO", setStyle(new Color(122, 167, 167), false)); // Cian Pálido
                keywordAttrMap.put("TERMINAR", setStyle(new Color(122, 167, 167), false)); // Cian Pálido
                keywordAttrMap.put("DEFECTO", setStyle(new Color(122, 167, 167), false)); // Cian Pálido
                keywordAttrMap.put("FIN SEGUN", setStyle(new Color(122, 167, 167), false)); // Cian Pálido
                
                keywordAttrMap.put("REPETIR", setStyle(new Color(168, 161, 98), true)); // Amarillo Kaki Claro
                keywordAttrMap.put("MIENTRAS", setStyle(new Color(168, 161, 98), false)); // Amarillo Kaki Claro
                keywordAttrMap.put("PARA", setStyle(new Color(168, 161, 98), false)); // Amarillo Kaki Claro
                keywordAttrMap.put("HASTA", setStyle(new Color(168, 161, 98), false)); // Amarillo Kaki Claro
                keywordAttrMap.put("HACER", setStyle(new Color(168, 161, 98), false)); // Amarillo Kaki Claro
                keywordAttrMap.put("EN", setStyle(new Color(168, 161, 98), false)); // Amarillo Kaki Claro
                keywordAttrMap.put("FIN MIENTRAS", setStyle(new Color(168, 161, 98), false)); // Amarillo Kaki Claro
                keywordAttrMap.put("FIN PARA", setStyle(new Color(168, 161, 98), false)); // Amarillo Kaki Claro
                
                keywordAttrMap.put("ENTERO", setStyle(new Color(132, 128, 75), true)); // Amarillo Oscuro
                keywordAttrMap.put("FLOTANTE", setStyle(new Color(132, 128, 75), false)); // Amarillo Oscuro
                keywordAttrMap.put("CARACTER", setStyle(new Color(132, 128, 75), false)); // Amarillo Oscuro
                keywordAttrMap.put("]", setStyle(new Color(132, 128, 75), false)); // Amarillo Oscuro
                keywordAttrMap.put("[", setStyle(new Color(132, 128, 75), false)); // Amarillo Oscuro
                keywordAttrMap.put("BOOLEANO", setStyle(new Color(132, 128, 75), false)); // Amarillo Oscuro
                keywordAttrMap.put("VERDADERO", setStyle(new Color(132, 128, 75), false)); // Amarillo Oscuro
                keywordAttrMap.put("FALSO", setStyle(new Color(132, 128, 75), false)); // Amarillo Oscuro
                
                defaultAttrSet = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, new Color(70, 70, 70)); // Gris Oscuro
                break;
            
        }
        

        
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
        String text = null;

        try {
            text = doc.getText(0, doc.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        if (text == null) {
            return;
        }

        // Limpiar estilos actuales
        doc.setCharacterAttributes(0, text.length(), defaultAttrSet, true);

        // Aplicar estilos para palabras clave
        for (String keyword : keywords) {
            int index = 0;
            while ((index = text.indexOf(keyword, index)) >= 0) {
                // Verificar si la palabra encontrada es una palabra completa
                boolean isCompleteWord = 
                    (index == 0 || !Character.isLetterOrDigit(text.charAt(index - 1))) &&
                    (index + keyword.length() == text.length() || !Character.isLetterOrDigit(text.charAt(index + keyword.length())));

                if (isCompleteWord) {
                    doc.setCharacterAttributes(index, keyword.length(), keywordAttrMap.get(keyword), false);
                }

                index += keyword.length();
            }
        }
    }

    @Override
    public void replaceSelection(String content) {
        getInputAttributes().removeAttribute(StyleConstants.Foreground);
        super.replaceSelection(content);
    }
}

