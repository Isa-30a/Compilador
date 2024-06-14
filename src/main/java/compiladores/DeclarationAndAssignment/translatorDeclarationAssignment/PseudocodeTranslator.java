package compiladores.DeclarationAndAssignment.translatorDeclarationAssignment;

import compiladores.DeclarationAndAssignment.machines.afds.*;
import compiladores.DeclarationAndAssignment.machines.turingMachines.*;

public class PseudocodeTranslator {

    private Afd_Declaration afdDeclaration;
    private Afd_Assignment afdAssignment;

    private TuringMachineDeclaration turingDeclaration;
    private TuringMachineAssignment turingAssignment;

    public PseudocodeTranslator() {
        // Inicialización de las máquinas y autómatas

        this.afdDeclaration = new Afd_Declaration();
        this.afdAssignment = new Afd_Assignment();

        this.turingDeclaration = new TuringMachineDeclaration();
        this.turingAssignment = new TuringMachineAssignment();
    }

    public String translate(String toCompile) {
        if (toCompile.trim().isEmpty()) {
            return "La entrada está vacía.";
        }

        String[] palabras = toCompile.trim().split("\\s+");

        String declarationPart = "";
        String assignmentPart;
        String asignacioncompleta = "";

        if (toCompile.contains("/") || toCompile.contains("+") || toCompile.contains("*") || toCompile.contains("-")
                || toCompile.contains("(") || toCompile.contains(")")) {

            String[] division = toCompile.trim().split("\\s*=\\s*", 2);

            if (palabras[1].equals("=")) {

                return palabras[0] + " " + palabras[1] + " " + division[1] + ";";

            } else if (palabras[2].equals("=")) {

                declarationPart = division[0].trim();

                assignmentPart = division[1].trim();

                String declaracion = turingDeclaration.translateDeclaration(declarationPart);
                String sinpuntoycoma = eliminarUltimoCaracter(declaracion);
                
                return sinpuntoycoma + " = " + assignmentPart + ";";
            }
        } else {

            if (palabras.length > 3 && !palabras[1].equals("=")) {

                String[] tokens = toCompile.trim().split("\\s*=\\s*", 2);

                declarationPart = tokens[0].trim();
                String[] Dato_nombre = declarationPart.split(" ");
                String nombre = Dato_nombre[1];

                assignmentPart = tokens[1].trim();
                asignacioncompleta = nombre + " = " + assignmentPart;

            }
        }

        // Validar y traducir el segmento
        if (afdDeclaration.isValidDeclaration(declarationPart) && afdAssignment.isValidAssignment(asignacioncompleta)) {

            String asigcompletaTraducida = turingAssignment.translateAssignment(asignacioncompleta);

            String despuesDELigual = obtenerSubcadenaDespuesDeIgual(asigcompletaTraducida);

            String declaracion = turingDeclaration.translateDeclaration(declarationPart);
            String sinpuntoycoma = eliminarUltimoCaracter(declaracion);

            String retorno = sinpuntoycoma + " = " + despuesDELigual;
            
            return retorno;

        } else if (afdDeclaration.isValidDeclaration(toCompile)) {
            return turingDeclaration.translateDeclaration(toCompile);
        } else if (afdAssignment.isValidAssignment(toCompile)) {
            return turingAssignment.translateAssignment(toCompile);
        }

        // Si no es ninguna de las anteriores, devolver el segmento sin cambios
        return toCompile;
    }

    public String eliminarUltimoCaracter(String str) {
        if (str == null || str.isEmpty()) {
            return str; // Devolver cadena vacía o null si es necesario
        }
        return str.substring(0, str.length() - 1);
    }

    public static String obtenerSubcadenaDespuesDeIgual(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        int index = str.indexOf("=");
        if (index == -1) {
            return str; // Devolver la cadena original si no contiene "="
        }
        return str.substring(index + 1).trim(); // Obtener subcadena después de "=" y eliminar espacios adicionales
    }
}
//listos para el enlace con proyecto completo
 