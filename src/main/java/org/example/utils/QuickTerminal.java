package org.example.utils;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import org.example.Acciones;

public class QuickTerminal {

    public QuickTerminal() {}

    public void compilar(File nombreArchivoCpp) {
        // Obtener la ruta del archivo y el nombre sin la extensión
        String ruta = nombreArchivoCpp.getParent();
        String nombreSinExtension = nombreArchivoCpp.getName().replaceFirst("[.][^.]+$", "");
    
        // Construir la ruta del archivo ejecutable de salida
        String salidaPath = ruta + File.separator + nombreSinExtension + ".exe";
    
        // Reemplazar las barras inclinadas con barras invertidas dobles
        salidaPath = salidaPath.replace("/", "\\\\");
    
        String comandoCompilacion = "g++ -o " + salidaPath + " " + nombreArchivoCpp;
    
        try {
            // Compilar el archivo .cpp
            Process procesoCompilacion = Runtime.getRuntime().exec(comandoCompilacion);
            procesoCompilacion.waitFor();
    
            // Verificar si se generó el archivo ejecutable
            File ejecutable = new File(salidaPath);
            if (ejecutable.exists()) {
                System.out.println("Archivo compilado correctamente: " + salidaPath);
            } else {
                System.err.println("No se encontró el archivo ejecutable generado. y/o hubo un error");
            }
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    
    public void ejecutarCpp(String rutaEjecutable) {
        Command cmd = console.cmd;
    
        if (!cmd.isRunning()) {
            cmd.execute(rutaEjecutable);
        } else {
            try {
                cmd.send(rutaEjecutable + "\n");
            } catch (IOException ex) {
                console.appendText("!! Failed to send command to process: " + ex.getMessage() + "\n");
            }
        }
    }
    
    public void compilarYEjecutar(File nombreArchivoCpp) {

        compilar(nombreArchivoCpp);

        // Obtener la ruta del archivo y el nombre sin la extensión
        String ruta = nombreArchivoCpp.getParent();
        String nombreSinExtension = nombreArchivoCpp.getName().replaceFirst("[.][^.]+$", "");
    
        // Construir la ruta del archivo ejecutable generado
        String salidaPath = ruta + File.separator + nombreSinExtension + ".exe";
        salidaPath = salidaPath.replace("/", "\\\\");
    
        ejecutarCpp(salidaPath);
    }

    public void ejecutar(File nombreArchivoCpp) {
        // String rutaArchivo = new File("").getAbsolutePath() + "/resources/img/mi_archivo.cpp";
        // File archivoPrueba = new File(rutaArchivo);
        // compilarYEjecutar(archivoPrueba);
        compilarYEjecutar(nombreArchivoCpp);
    }

    ConsolePane console;

    public JPanel getJPanelTerminal() {
        console = new ConsolePane();
        return console;
    }

    public void clearTextArea() {
        console.textArea.setText("");
    }

    public interface CommandListener {

        public void commandOutput(String text);

        public void commandCompleted(String cmd, int result);

        public void commandFailed(Exception exp);
    }

    public class ConsolePane extends JPanel implements CommandListener, Terminal {

        public JTextArea textArea;
        private int userInputStart = 0;
        public Command cmd;

        public ConsolePane() {

            cmd = new Command(this);

            setLayout(new BorderLayout());
            textArea = new JTextArea(20, 30);
            ((AbstractDocument) textArea.getDocument()).setDocumentFilter(new ProtectedDocumentFilter(this));

            JScrollPane scroll = new JScrollPane();
            scroll.setBorder(null);
            scroll.getVerticalScrollBar().setBorder(null);
            scroll.getHorizontalScrollBar().setBorder(null);

            // Opcional: Eliminar el borde del JTextArea si es necesario
            textArea.setBorder(null);

            add(new JScrollPane(textArea));

            InputMap im = textArea.getInputMap(WHEN_FOCUSED);
            ActionMap am = textArea.getActionMap();

            Action oldAction = am.get("insert-break");
            am.put("insert-break", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    int range = textArea.getCaretPosition() - userInputStart;
                    try {
                        String text = textArea.getText(userInputStart, range).trim().replace("/", "\\\\");
                        System.out.println("[" + text + "]");
                        userInputStart += range;
                        if (!cmd.isRunning()) {
                            cmd.execute(text);
                        } else {
                            try {
                                cmd.send(text + "\n");
                            } catch (IOException ex) {
                                appendText("!! Failed to send command to process: " + ex.getMessage() + "\n");
                            }
                        }
                    } catch (BadLocationException ex) {
                        Logger.getLogger(QuickTerminal.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    oldAction.actionPerformed(e);
                }
            });

        }

        @Override
        public void commandOutput(String text) {
            SwingUtilities.invokeLater(new AppendTask(this, text));
        }

        @Override
        public void commandFailed(Exception exp) {
            SwingUtilities.invokeLater(new AppendTask(this, "Command failed - " + exp.getMessage()));
        }

        @Override
        public void commandCompleted(String cmd, int result) {
            Timer timer = new Timer(1000, e -> {
                appendText("\n> " + cmd + " exited with " + result + "\n");
                appendText("\n");
            });
            timer.setRepeats(false); // Solo se ejecutará una vez
            timer.start();
        }

        protected void updateUserInputPos() {
            int pos = textArea.getCaretPosition();
            textArea.setCaretPosition(textArea.getText().length());
            userInputStart = pos;

        }

        @Override
        public int getUserInputStart() {
            return userInputStart;
        }

        @Override
        public void appendText(String text) {
            textArea.append(text);
            updateUserInputPos();
        }
    }

    public interface UserInput {

        public int getUserInputStart();
    }

    public interface Terminal extends UserInput {
        public void appendText(String text);
    }

    public class AppendTask implements Runnable {

        public Terminal terminal;
        private String text;

        public AppendTask(Terminal textArea, String text) {
            this.terminal = textArea;
            this.text = text;
        }

        @Override
        public void run() {
            terminal.appendText(text);
        }
    }

    public class Command {

        private CommandListener listener;
        private ProcessExecutor executor;

        public Command(CommandListener listener) {
            this.listener = listener;
        }

        public boolean isRunning() {

            return executor != null && executor.isAlive();

        }

        public void execute(String cmd) {

            if (!cmd.trim().isEmpty()) {

                List<String> values = new ArrayList<>(25);
                if (cmd.contains("\"")) {

                    while (cmd.contains("\"")) {

                        String start = cmd.substring(0, cmd.indexOf("\""));
                        cmd = cmd.substring(start.length());
                        String quote = cmd.substring(cmd.indexOf("\"") + 1);
                        cmd = cmd.substring(cmd.indexOf("\"") + 1);
                        quote = quote.substring(0, cmd.indexOf("\""));
                        cmd = cmd.substring(cmd.indexOf("\"") + 1);

                        if (!start.trim().isEmpty()) {
                            String parts[] = start.trim().split(" ");
                            values.addAll(Arrays.asList(parts));
                        }
                        values.add(quote.trim());

                    }

                    if (!cmd.trim().isEmpty()) {
                        String parts[] = cmd.trim().split(" ");
                        values.addAll(Arrays.asList(parts));
                    }

                    for (String value : values) {
                        System.out.println("[" + value + "]");
                    }

                } else {

                    if (!cmd.trim().isEmpty()) {
                        String parts[] = cmd.trim().split(" ");
                        values.addAll(Arrays.asList(parts));
                    }

                }

                executor = new ProcessExecutor(listener, values);

            }

        }

        public void send(String cmd) throws IOException {
            executor.write(cmd);
        }
    }

    // Detecta el sistema operativo y determina el shell adecuado
    private String getShellCommand() {

        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            return "cmd.exe";
        } else {
            return "/bin/bash";
        }
    }

    // Implementación actualizada de ProcessExecutor
    public class ProcessExecutor extends Thread {

        private List<String> cmds;
        private CommandListener listener;

        private Process process;

        public ProcessExecutor(CommandListener listener, List<String> cmds) {
            this.listener = listener;
            String shell = getShellCommand();
            this.cmds = new ArrayList<>();
            this.cmds.add(shell);
            if (shell.equals("cmd.exe")) {
                this.cmds.add("/c");
            }
            this.cmds.addAll(cmds);
            start();
        }

        @Override
        public void run() {
            try {
                System.out.println("cmds = " + cmds);
                ProcessBuilder pb = new ProcessBuilder(cmds);
                pb.redirectErrorStream(true);
                process = pb.start();
                StreamReader reader = new StreamReader(listener, process.getInputStream());

                int result = process.waitFor();

                reader.join();

                StringJoiner sj = new StringJoiner(" ");
                cmds.stream().forEach((cmd) -> {
                    sj.add(cmd);
                });

                listener.commandCompleted(sj.toString(), result);
            } catch (Exception exp) {
                exp.printStackTrace();
                listener.commandFailed(exp);
            }
        }

        public void write(String text) throws IOException {
            if (process != null && process.isAlive()) {
                process.getOutputStream().write(text.getBytes());
                process.getOutputStream().flush();
            }
        }
    }

    public class StreamReader extends Thread {

        private InputStream is;
        private CommandListener listener;

        public StreamReader(CommandListener listener, InputStream is) {
            this.is = is;
            this.listener = listener;
            start();
        }

        @Override
        public void run() {
            try {
                int value = -1;
                while ((value = is.read()) != -1) {
                    listener.commandOutput(Character.toString((char) value));
                }
            } catch (IOException exp) {
                exp.printStackTrace();
            }
        }
    }

    public class ProtectedDocumentFilter extends DocumentFilter {

        private UserInput userInput;

        public ProtectedDocumentFilter(UserInput userInput) {
            this.userInput = userInput;
        }

        public UserInput getUserInput() {
            return userInput;
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (offset >= getUserInput().getUserInputStart()) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
            if (offset >= getUserInput().getUserInputStart()) {
                super.remove(fb, offset, length); // To change body of generated methods, choose Tools | Templates.
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (offset >= getUserInput().getUserInputStart()) {
                super.replace(fb, offset, length, text, attrs); // To change body of generated methods, choose Tools | Templates.
            }
        }
    }
}
