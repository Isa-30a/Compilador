package ciclos;

//DESARROLLADO POR JORGE DAVID SIERRA VEGA

import javax.swing.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class PseudocodeConverter {

    private static final Map<String, String> textPlaceholders = new HashMap<>();
    private static int placeholderCount = 0;

    //=========================================================================
    //EJECUTAMOS LOS CICLOS AL TEXTO CON LOS PSEUDOCODIGOS
    //=========================================================================
    
    public static String convertToCpp(String pseudocode) {
        // Proteger texto entre comillas
        pseudocode = protectQuotedText(pseudocode);

        // Continuar reemplazando ciclos hasta que no haya más coincidencias
        String oldPseudocode;
        do {
            oldPseudocode = pseudocode;
            pseudocode = convertForLoops(pseudocode);
            pseudocode = convertWhileLoops(pseudocode);
            pseudocode = convertDoWhileLoops(pseudocode);
        } while (!pseudocode.equals(oldPseudocode));

        // Restaurar texto entre comillas
        pseudocode = restoreQuotedText(pseudocode);

        return pseudocode;
    }
    
    //=========================================================================
    //PROTEJEMOS EL TEXTO ENTRE COMILLAS EN CASO DE HABER UNA PALABRA RESERVADA
    //ES DECIR, SI HAY "El valor PARA la variable es: ", SE OBSERVA LA PALABRA
    //RESERVADA PARA, ASÍ QUE ESA PALABRA NO ES RESERVADA PORQUE ES TEXTO
    //=========================================================================
    private static String protectQuotedText(String pseudocode) {
        // Reemplazar texto entre comillas dobles por placeholders
        Pattern pattern = Pattern.compile("\"([^\"]*)\"");
        Matcher matcher = pattern.matcher(pseudocode);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String placeholder = "QUOTED_TEXT_" + placeholderCount++;
            textPlaceholders.put(placeholder, matcher.group(0));
            matcher.appendReplacement(sb, placeholder);
        }
        matcher.appendTail(sb);
        pseudocode = sb.toString();

        // Reemplazar texto entre comillas simples por placeholders
        pattern = Pattern.compile("'([^']*)'");
        matcher = pattern.matcher(pseudocode);
        sb = new StringBuffer();
        while (matcher.find()) {
            String placeholder = "QUOTED_TEXT_" + placeholderCount++;
            textPlaceholders.put(placeholder, matcher.group(0));
            matcher.appendReplacement(sb, placeholder);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private static String restoreQuotedText(String pseudocode) {
        for (Map.Entry<String, String> entry : textPlaceholders.entrySet()) {
            pseudocode = pseudocode.replace(entry.getKey(), entry.getValue());
        }
        return pseudocode;
    }

    //=========================================================================
    // PASAR DE PSEUDOCODIGO PARA A LENGUAJE C++ FOR
    //=========================================================================
    private static String convertForLoops(String pseudocode) {
    Pattern pattern = Pattern.compile("(?sm)PARA\\s*\\(([^;]+);\\s*([^;]+);\\s*([^\\)]+)\\)\\s*HACER:\\s*(.*?)\\s*FIN PARA");
        Matcher matcher = pattern.matcher(pseudocode);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String initialization = matcher.group(1).trim();
            String condition = matcher.group(2).trim();
            String update = matcher.group(3).trim();
            // Obtener el cuerpo del bucle y dividirlo en líneas
            String body = modifyDoWhileBody(matcher.group(4).trim());
            matcher.appendReplacement(sb, "for (" + initialization + "; " + condition + "; " + update + ") {\n" + body + "\n}");
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    //=========================================================================
    // PASAR DE PSEUDOCODIGO MIENTRAS A LENGUAJE C++ WHILE
    //=========================================================================
    private static String convertWhileLoops(String pseudocode) {
        Pattern pattern = Pattern.compile("(?s)MIENTRAS\\s*\\(([^\\)]+)\\)\\s*HACER:\\s*(.*?)\\s*FIN MIENTRAS");
        Matcher matcher = pattern.matcher(pseudocode);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String condition = matcher.group(1).trim();
            String body = modifyDoWhileBody(matcher.group(2).trim());
            matcher.appendReplacement(sb, "while (" + condition + ") {\n" + body + "\n}");
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    //=========================================================================
    // PASAR DE PSEUDOCODIGO REPETIR-MIENTRAS A LENGUAJE C++ DO-WHILE
    //=========================================================================
    private static String convertDoWhileLoops(String pseudocode) {
        Pattern pattern = Pattern.compile("(?s)REPETIR:\\s*(.*?)\\s*MIENTRAS\\s*\\(([^\\)]+)\\)");
        Matcher matcher = pattern.matcher(pseudocode);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String body = modifyDoWhileBody(matcher.group(1).trim());
            String condition = matcher.group(2).trim();
            matcher.appendReplacement(sb, "do {\n" + body + "\n} while (" + condition + ");");
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
    
    //=========================================================================
    //CAMBIAR LA SALIDA SIN ESPACIOS INICIALES, PARA PERMITIR FORMATEAR TEXTO
    //=========================================================================
    private static String modifyDoWhileBody(String body) {
        
        String[] lines = body.split("\\r?\\n");

            // Eliminar los espacios al inicio de cada línea y reconstruir el cuerpo del bucle
            StringBuilder trimmedBody = new StringBuilder();
            for (String line : lines) {
                String trimmedLine = line.stripLeading(); // Eliminar espacios al inicio de la línea
                if (!trimmedLine.isEmpty()) { // Agregar la línea solo si no está vacía
                    trimmedBody.append(trimmedLine).append("\n");
                }
            }

            // Eliminar el último salto de línea si existe
            if (trimmedBody.length() > 0) {
                trimmedBody.setLength(trimmedBody.length() - 1);
            }
            // Convertir el cuerpo modificado de nuevo a una cadena
            body = trimmedBody.toString();
        return body;
    }

    //=========================================================================
    // FUNCIÓN PRINCIPAL, SE PIDE UBICACIÓN DEL TXT CON PSEUDOCODIGO Y SU SALIDA
    // LA IDEA ES QUE EL EQUIPO COORDINADOR CAMBIE ESTO PARA PASAR EL PARAMETRO
    // A LA FUNCIÓN DE MIS CICLOS
    //=========================================================================
    public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccione el archivo de entrada");
        int userSelection = fileChooser.showOpenDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File inputFile = fileChooser.getSelectedFile();
            
            // Establecer el nombre de archivo predeterminado para el archivo de salida
            String defaultOutputFileName = "Salida.txt";
            fileChooser.setSelectedFile(new File(defaultOutputFileName));

            fileChooser.setDialogTitle("Seleccione la ubicación del archivo de salida");
            userSelection = fileChooser.showSaveDialog(null);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File outputFile = fileChooser.getSelectedFile();

                try {
                    String pseudocode = new String(Files.readAllBytes(Paths.get(inputFile.getAbsolutePath())));
                    String cppCode = convertToCpp(pseudocode);
                    Files.write(Paths.get(outputFile.getAbsolutePath()), cppCode.getBytes());
                    JOptionPane.showMessageDialog(null, "Conversión completada. Archivo guardado en: " + outputFile.getAbsolutePath());
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Error al leer o escribir los archivos: " + e.getMessage());
                }
            }
        }
    });
}

}