package compiladores.ciclos;

import javax.swing.*;
import java.io.*;
import java.nio.file.*;


public class PseudocodeConverter {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Seleccione el archivo de entrada");
            int userSelection = fileChooser.showOpenDialog(null);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File inputFile = fileChooser.getSelectedFile();
                
                PseudocodeProcessorCiclos processor = new PseudocodeProcessorCiclos();
                
                String defaultOutputFileName = "Salida.txt";
                fileChooser.setSelectedFile(new File(defaultOutputFileName));

                fileChooser.setDialogTitle("Seleccione la ubicación del archivo de salida");
                userSelection = fileChooser.showSaveDialog(null);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File outputFile = fileChooser.getSelectedFile();

                    try {
                        String pseudocode = new String(Files.readAllBytes(Paths.get(inputFile.getAbsolutePath())));
                        
                        String cppCode = processor.convertToCpp(pseudocode);
                        
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
