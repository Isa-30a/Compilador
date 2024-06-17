package compiladores.DeclarationAndAssignment.translatorDeclarationAssignment;

import compiladores.DeclarationAndAssignment.machines.turingMachines.basics.*;
import compiladores.DeclarationAndAssignment.machines.turingMachines.operations.*;
import compiladores.DeclarationAndAssignment.machines.afds.*;

public class PseudocodeTranslator {

    private Afd_Declaration afdDeclaration;
    private Afd_Assignment afdAssignment;

    private TuringMachineDeclaration turingDeclaration;
    private TuringMachineAssignment turingAssignment;
    private TuringMachineAssignOperations turingAssignOperations;
    private TuringMachineAssignmentAndDeclaration turingAssignmentDeclaration;
    private TuringMachineAssignAndDecOperations turingAssignDecOperations;

    public PseudocodeTranslator() {
        // Inicialización de las máquinas y autómatas

        this.afdDeclaration = new Afd_Declaration();
        this.afdAssignment = new Afd_Assignment();

        this.turingDeclaration = new TuringMachineDeclaration();
        this.turingAssignment = new TuringMachineAssignment();
        this.turingAssignOperations = new TuringMachineAssignOperations();
        this.turingAssignDecOperations = new TuringMachineAssignAndDecOperations();
        this.turingAssignmentDeclaration = new TuringMachineAssignmentAndDeclaration();
    }

    public String translate(String toCompile) {

        if (toCompile.trim().isEmpty()) {
            return "La entrada está vacía.";
        }

        String[] palabras = toCompile.trim().split("\\s+");

        if (toCompile.contains("/") || toCompile.contains("+") || toCompile.contains("*")
                || toCompile.contains("-") || toCompile.contains("(") || toCompile.contains(")")
                || toCompile.contains("&&") || toCompile.contains("||") || toCompile.contains("and")
                || toCompile.contains("or") || toCompile.contains("(") || toCompile.contains("<")
                || toCompile.contains(">")) {

            // si hay operaciones aritmeticas y/o logicas
            if (palabras[1].equals("=")) {
                //ASIGNACIONES CON OPERACIONES
                return turingAssignOperations.translateAssignmentOperations(toCompile);
            } else if (palabras[2].equals("=")) {
                //DECLARACION Y ASIGNACION CON OPERACIONES
                return turingAssignDecOperations.translateAssignAndDecOperations(toCompile);
            }
        } else if (palabras.length > 3 && !palabras[1].equals("=")) {
            //DECLARACION CON ASIGNACION SENCILLA
            return turingAssignmentDeclaration.translateAssignmentDeclaration(toCompile);

        } else if (afdDeclaration.isValidDeclaration(toCompile)) {
            //DECLARACION SENCILLA
            return turingDeclaration.translateDeclaration(toCompile);

        } else if (afdAssignment.isValidAssignment(toCompile)) {
            //ASIGNACION SENCILLA
            return turingAssignment.translateAssignment(toCompile);

        }
        // Si no es ninguna de las anteriores, devolver el segmento sin cambios
        return "formato incorrecto o incompleto";
    }

}
