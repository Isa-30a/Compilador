
package compiladores.DeclarationAndAssignment.machines.turingMachines.operations;

public class TuringMachineAssignOperations {

    public String translateAssignmentOperations(String toCompile) {

        String[] division = toCompile.trim().split("\\s*=\\s*", 2);
        String[] palabras = toCompile.trim().split("\\s+");

        return palabras[0] + " " + palabras[1] + " " + division[1] + ";";
    }
}
