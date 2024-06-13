package compiladores.DeclarationAndAssignment.translatorDeclarationAssignment;

import compiladores.DeclarationAndAssignment.machines.afds.*;
import compiladores.DeclarationAndAssignment.machines.turingMachines.*;

import java.util.ArrayList;
import java.util.List;

public class PseudocodeTranslator {

    private Afd_DeclarationAndAssignment afdDeclarationAssignment;
    private Afd_DecWithAssignOperations afdDecWithAssignOperations;
    private Afd_AssignOperations afdAssignOperations;
    private Afd_Declaration afdDeclaration;
    private Afd_Assignment afdAssignment;

    private TuringMachineDeclarationAndAssignment turingDeclarationAssignment;
    private TuringMachineDecWithAssignOperations turingDecWithAssignOperations;
    private TuringMachineAssignOperations turingAssignOperations;
    private TuringMachineDeclaration turingDeclaration;
    private TuringMachineAssignment turingAssignment;

    public PseudocodeTranslator() {
        this.afdDeclarationAssignment = new Afd_DeclarationAndAssignment();
        this.afdDecWithAssignOperations = new Afd_DecWithAssignOperations();
        this.afdAssignOperations = new Afd_AssignOperations();
        this.afdDeclaration = new Afd_Declaration();
        this.afdAssignment = new Afd_Assignment();

        this.turingDeclarationAssignment = new TuringMachineDeclarationAndAssignment();
        this.turingDecWithAssignOperations = new TuringMachineDecWithAssignOperations();
        this.turingAssignOperations = new TuringMachineAssignOperations();
        this.turingDeclaration = new TuringMachineDeclaration();
        this.turingAssignment = new TuringMachineAssignment();
    }

    public String translate(String toCompile) {
        if (toCompile.trim().isEmpty()) {
            // Si la entrada está vacía
            return "La entrada está vacía.";
        }

        // Dividir el pseudocódigo en líneas
        String[] lines = toCompile.split("\n");
        List<String> translatedLines = new ArrayList<>();

        // Iterar sobre cada línea
        for (String line : lines) {
            // Dividir la línea en segmentos basados en espacios
            String[] segments = line.trim().split("\\s+");

            // Traducir cada segmento y reconstruir la línea
            StringBuilder translatedLine = new StringBuilder();
            for (String segment : segments) {
                String translatedSegment = translateSegment(segment.trim());
                translatedLine.append(translatedSegment).append(" ");
            }

            // Agregar la línea traducida al resultado final
            translatedLines.add(translatedLine.toString().trim());
        }

        // Reconstruir el pseudocódigo traducido
        StringBuilder result = new StringBuilder();
        for (String translatedLine : translatedLines) {
            result.append(translatedLine).append("\n");
        }

        return result.toString();
    }

    private String translateSegment(String segment) {
        // Verificar si es una declaración
        if (isDeclaration(segment)) {
            return turingDeclaration.translateDeclaration(segment);
        }

        // Verificar si es una asignación
        if (segment.contains("=") && segment.indexOf("=") == segment.lastIndexOf("=")) {
            return turingAssignment.translateAssignment(segment);
        }

        // Verificar si es una declaración con asignación
        if (afdDecWithAssignOperations.isValidDeclarationWithAssignment(segment)) {
            return turingDecWithAssignOperations.translateDeclarationAndAssignment(segment);
        }

        // Verificar si es una asignación simple
        if (afdAssignOperations.isValidAssignment(segment)) {
            return turingAssignOperations.translateAssignment(segment);
        }

        // Si no es ninguna de las anteriores, devolver el segmento sin cambios
        return segment;
    }

    private boolean isDeclaration(String segment) {
        // Verificar si el segmento es una declaración válida según las reglas especificadas
        // Debe comenzar con un tipo de dato en español y en mayúsculas
        // Puede tener dimensiones de arreglo como [] o [numero]
        return afdDeclaration.isValidDeclaration(segment);
    }
}
