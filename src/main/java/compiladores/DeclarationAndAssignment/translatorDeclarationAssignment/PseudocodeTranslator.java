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
        // Inicialización de las máquinas y autómatas
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
            StringBuilder translatedLine = new StringBuilder();
            int index = 0;

            while (index < line.length()) {
                String remainingLine = line.substring(index).trim();
                String block = findNextBlock(remainingLine);
                if (!block.isEmpty()) {
                    // Agregar el texto anterior al bloque encontrado
                    translatedLine.append(line, index, line.indexOf(block, index)).append(" ");

                    // Traducir el bloque encontrado
                    String translatedBlock = translateSegment(block.trim());
                    translatedLine.append(translatedBlock).append(" ");
                    index += remainingLine.indexOf(block) + block.length();
                } else {
                    // Si no se encontró un bloque, agregar el resto de la línea sin cambios
                    translatedLine.append(line.substring(index));
                    break;
                }
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

    private String findNextBlock(String line) {
        // Buscar y devolver el próximo bloque que coincida con alguno de los AFDs
        for (int i = 0; i < line.length(); i++) {
            String substring = line.substring(0, i + 1).trim();
            if (afdDeclarationAssignment.isValidDeclarationAssignment(substring) ||
                afdDecWithAssignOperations.isValidDeclarationWithAssignment(substring) ||
                afdAssignOperations.isValidAssignment(substring) ||
                afdDeclaration.isValidDeclaration(substring) ||
                afdAssignment.isValidAssignment(substring)) {
                return substring;
            }
        }
        return "";
    }

    private String translateSegment(String segment) {
        // Verificar si es una declaración con asignación
        if (afdDeclarationAssignment.isValidDeclarationAssignment(segment)) {
            return turingDeclarationAssignment.translateDeclarationAndAssignment(segment);
        }

        // Verificar si es una declaración sola
        if (afdDeclaration.isValidDeclaration(segment)) {
            return turingDeclaration.translateDeclaration(segment);
        }

        // Verificar si es una asignación sola
        if (afdAssignment.isValidAssignment(segment)) {
            return turingAssignment.translateAssignment(segment);
        }

        // Verificar si es una declaración con operaciones de asignación
        if (afdDecWithAssignOperations.isValidDeclarationWithAssignment(segment)) {
            return turingDecWithAssignOperations.translateDeclarationAndAssignment(segment);
        }

        // Verificar si es una operación de asignación
        if (afdAssignOperations.isValidAssignment(segment)) {
            return turingAssignOperations.translateAssignment(segment);
        }

        // Si no es ninguna de las anteriores, devolver el segmento sin cambios
        return segment;
    }
}
