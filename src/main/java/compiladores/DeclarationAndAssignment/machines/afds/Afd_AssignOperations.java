
package compiladores.DeclarationAndAssignment.machines.afds;

import java.util.regex.Pattern;

public class Afd_AssignOperations {
    // Método para verificar si una asignación es válida
    public boolean isValidAssignment(String input) {
        // Patrón para la asignación: nombreVariable = cualquierCantidadDeOperaciones
        // Permite cualquier cantidad de operaciones matemáticas básicas con operandos
        String pattern = "^\\w+\\s*=\\s*(?:[\\w.]+|\\(\\s*(?:(?:\\w+|\\(\\s*(?:[\\w.]+|\\s*(?:[+\\-*/]\\s*)*)*\\s*\\))\\s*[+\\-*/]\\s*)*)$";

        // Verificar si la entrada coincide con el patrón
        return Pattern.matches(pattern, input);
    }
}
