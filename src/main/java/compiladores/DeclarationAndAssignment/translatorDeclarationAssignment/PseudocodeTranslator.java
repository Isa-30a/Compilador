
package compiladores.DeclarationAndAssignment.translatorDeclarationAssignment;

import compiladores.DeclarationAndAssignment.machines.afds.*;
import compiladores.DeclarationAndAssignment.machines.turingMachines.*;

public class PseudocodeTranslator {

    private Afd_DeclarationAndAssignment afdDeclarationAssignment;
    private Afd_Declaration afdDeclaration;
    private Afd_Assignment afdAssignment;
    private Afd_AssignOperations afdAssignOperations;
    private Afd_DecWithAssignOperations afdDecWithAssignOperations;

    private TuringMachineAssignment turingAssignment;
    private TuringMachineDeclaration turingDeclaration;
    private TuringMachineDeclarationAndAssignment turingDeclarationAssignment;
    private TuringMachineAssignOperations turingAssignOperations;
    private TuringMachineDecWithAssignOperations turingDecWithAssignOperations;

    public PseudocodeTranslator() {
        this.afdDeclarationAssignment = new Afd_DeclarationAndAssignment();
        this.afdDeclaration = new Afd_Declaration();
        this.afdAssignment = new Afd_Assignment();
        this.afdAssignOperations = new Afd_AssignOperations();
        this.afdDecWithAssignOperations = new Afd_DecWithAssignOperations();

        this.turingAssignment = new TuringMachineAssignment();
        this.turingDeclaration = new TuringMachineDeclaration();
        this.turingDeclarationAssignment = new TuringMachineDeclarationAndAssignment();
        this.turingAssignOperations = new TuringMachineAssignOperations();
        this.turingDecWithAssignOperations = new TuringMachineDecWithAssignOperations();
    }

    public String translate(String pseudocode) {
        if (pseudocode.trim().isEmpty()) {
            // Si la cadena de pseudocódigo está vacía
            return "La entrada está vacía.";
        }

        if (afdDeclarationAssignment.isValidDeclarationAssignment(pseudocode)) {
            // Caso de declaración y asignación simultánea con operaciones
            return turingDecWithAssignOperations.translateDeclarationAndAssignment(pseudocode);
        } else if (afdDeclaration.isValidDeclaration(pseudocode)) {
            // Caso de declaración simple
            return turingDeclaration.translateDeclaration(pseudocode);
        } else if (afdAssignment.isValidAssignment(pseudocode)) {
            // Caso de asignación simple
            return turingAssignment.translateAssignment(pseudocode);
        } else if (afdAssignOperations.isValidAssignment(pseudocode)) {
            // Caso de asignación con operaciones
            return turingAssignOperations.translateAssignment(pseudocode);
        } else if (afdDecWithAssignOperations.isValidDeclarationWithAssignment(pseudocode)) {
            // Caso de declaración con asignación y operaciones
            return turingDecWithAssignOperations.translateDeclarationAndAssignment(pseudocode);
        } else {
            // Caso no válido
            return "El pseudocódigo no es válido.";
        }
    }
}
//status interconection prueba