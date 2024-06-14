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
            return "La entrada está vacía.";
        }

        // Validar y traducir el segmento
        if (afdDeclarationAssignment.isValidDeclarationAssignment(toCompile)) {
            return turingDeclarationAssignment.translateDeclarationAndAssignment(toCompile);
        } else if (afdDecWithAssignOperations.isValidDeclarationWithAssignment(toCompile)) {
            return turingDecWithAssignOperations.translateDeclarationAndAssignment(toCompile);
        } else if (afdAssignOperations.isValidAssignment(toCompile)) {
            return turingAssignOperations.translateAssignment(toCompile);
        } else if (afdDeclaration.isValidDeclaration(toCompile)) {
            return turingDeclaration.translateDeclaration(toCompile);
        } else if (afdAssignment.isValidAssignment(toCompile)) {
            return turingAssignment.translateAssignment(toCompile);
        }

        // Si no es ninguna de las anteriores, devolver el segmento sin cambios
        return toCompile;
    }
}
