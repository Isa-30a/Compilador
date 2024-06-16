
package compiladores.DeclarationAndAssignment.machines.turingMachines.operations;

import compiladores.DeclarationAndAssignment.machines.turingMachines.basics.TuringMachineDeclaration;
import compiladores.DeclarationAndAssignment.semantics.Esthetic;

public class TuringMachineAssignAndDecOperations {

    Esthetic estetica = new Esthetic();
    TuringMachineDeclaration turingDeclaration = new TuringMachineDeclaration();

    String declarationPart;
    String assignmentPart;

    public String translateAssignAndDecOperations(String toCompile) {

        String[] division = toCompile.trim().split("\\s*=\\s*", 2);

        declarationPart = division[0].trim();
        assignmentPart = division[1].trim();

        String declaracion = turingDeclaration.translateDeclaration(declarationPart);

        String sinpuntoycoma = estetica.eliminarUltimoCaracter(declaracion);

        return sinpuntoycoma + " = " + assignmentPart + ";";
    }
}
