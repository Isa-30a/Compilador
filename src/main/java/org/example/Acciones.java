package org.example;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.mycompany.mavenproject1.Mavenproject1;

import compiladores.functions.FunctionsCompiler;
import compiladores.ciclos.PseudocodeProcessorCiclos;
import compiladores.condicionales.IfElseConverter;
import compiladores.condicionales.LogicalOperators;

public class Acciones {

    static {
        try {
            String path = System.getProperty("user.dir") + "/native/CompiladorC++.dll";
            System.load(path);
        } catch (UnsatisfiedLinkError e) {
            System.err.println("No se pudo cargar la biblioteca nativa: " + e.getMessage());
        }
    }
    private native static void CompilarYEjecutar(String RutaArchivo);

    public static void ejecutar(File TempFile)
    {
        // a este metodo tienes que mandarle el un archivo txt ya con el codigo en c++
        // obligatoriamente debes pasarlo en formato .txt, este genera el cpp en la carpeta 
        // donde esta el pom.xml por si quieres saber donde se guarda el ultimo cpp generado
        // att. Nestor
        CompilarYEjecutar(TempFile.getPath());
    }

    private static String lastDirectoryPath = "";

    public static void guardar(File TempFile)
    {
        // Esta funcion ya recibe el input del usuario en un .txt llamado TempFile
        // hazle las modificaciones que vayas a hacerle al archivo
        // para volverlo un .tovar y guardarlo en la maquina del usuario
        // Aqui tienes una funcion para verificar que toma el input del usuario bien
        // att. Nestor

        //guardar listo 
        //att. Mario

        // Si tenemos una última ruta válida, establecerla como directorio inicial
        
        try (BufferedReader reader = new BufferedReader(new FileReader(TempFile))) 
        {
            JFileChooser fileChooser = new JFileChooser();

            if (!lastDirectoryPath.isEmpty()) {
                fileChooser.setCurrentDirectory(new File(lastDirectoryPath));
            }
            
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos Tovar (*.tovar)", "tovar");
                fileChooser.setFileFilter(filter);

            String linea;
            fileChooser.setDialogTitle("Seleccione la ubicación del archivo de salida");
            int userSelection = fileChooser.showSaveDialog(null);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File outputFile = fileChooser.getSelectedFile();

                String fileName = outputFile.getName();
                if (!fileName.toLowerCase().endsWith(".tovar")) {
                    outputFile = new File(outputFile.getParentFile(), fileName + ".tovar");
                }

                try {
                    StringBuilder content = new StringBuilder();
                    while ((linea = reader.readLine()) != null){
                        content.append(linea + '\n');
                    }
                    Files.write(Paths.get(outputFile.getAbsolutePath()), content.toString().getBytes());
                    JOptionPane.showMessageDialog(null, "Archivo guardado en: " +outputFile.getAbsolutePath());
                    
                    Mavenproject1.archive.setText(outputFile.getName());

                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Error al leer o escribir los archivos: " + e.getMessage());
                }
            
            }
        } 
        catch (IOException ex) 
        {
            System.out.println(ex);
        }
    }

    public static void abrirArchivo(JTextPane area)
    {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) 
            {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".tovar");
            }
            @Override
            public String getDescription() 
            {
                return "Archivos de texto (*.tovar)";
            }
        });

        // Establecer el directorio inicial basado en la última ubicación abierta
        if (!lastDirectoryPath.isEmpty()) {
            fileChooser.setCurrentDirectory(new File(lastDirectoryPath));
        }
        int result = fileChooser.showOpenDialog(null);
        
        if (result == JFileChooser.APPROVE_OPTION) 
        {
            File archivo = fileChooser.getSelectedFile();
            String line;
            try(BufferedReader reader = new BufferedReader(new FileReader(archivo))){
                while ((line = reader.readLine()) != null){
                    area.setText(area.getText() + line + '\n');
                }
            }catch (IOException exception){
                JOptionPane.showMessageDialog(null, "Error al leer el archivo: " + exception.getMessage());
            }
            Mavenproject1.archive.setText(archivo.getName());
        } 
        else 
        {
            JOptionPane.showMessageDialog(null, "No se seleccionó ningún archivo.");
        }

        // Aqui aplicale toda la logica para extraer todo el contenido
        // Del archivo del usuario para copiarlo dentro del IDE de programacion
        // En este punto tienes un archivo .txt llamado archivo, lo puedes cambiar facil 
        // Para cuando hagas el .tovar y pasarlo a txt
        // Cualquier cosa que puedas necesitar me haces saber
        // att. Nestor
        

        //Abrir listo
        //att. Mario
    }

    public static void compilar(File TempFile)
    {
        // Aqui haz la tarea de mandarle a todos los compiladores las partes
        // Que les corresponde, como salida haz un archivo .txt que tenga el codigo
        // En c++ ya traducido para poder enviarlo al metodo de ejecutar
        // El metodo ejecutar va a compilar en c++ el archivo .txt que le pases
        // Para que funcione mi metodo solo mandale el archivo que aqui generaste
        // att. Nestor
        try(BufferedReader reader = new BufferedReader(new FileReader(TempFile))){
            StringBuilder builder = new StringBuilder();
            FunctionsCompiler functionsComplier = new FunctionsCompiler();
            PseudocodeProcessorCiclos ciclesCompiler = new PseudocodeProcessorCiclos();
            //IfElseConverter conditionalStructuresCompiler = new IfElseConverter();
            //LogicalOperators logicalOperatorsCompiler = new LogicalOperators();
            String linea;
            String toComplie;
            while((linea = reader.readLine()) != null){
                builder.append(linea + '\n');
            }
            toComplie = functionsComplier.pseudoToCpp(builder.toString());
            toComplie = IfElseConverter.convertSwitchCase(toComplie);
            toComplie = IfElseConverter.convertToCpp(toComplie);
            toComplie = LogicalOperators.convertToCpp(toComplie);
            toComplie = ciclesCompiler.convertToCpp(toComplie);
            System.out.println(toComplie);
        }catch (IOException exception){
            JOptionPane.showMessageDialog(null, "Error al traducir el archivo: " + exception.getMessage());
        }
        


    }
}
