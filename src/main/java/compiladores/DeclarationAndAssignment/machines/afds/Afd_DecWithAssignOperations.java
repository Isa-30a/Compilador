
package compiladores.DeclarationAndAssignment.machines.afds;

import java.util.regex.Pattern;

public class Afd_DecWithAssignOperations {
    // Método para verificar si una declaración con asignación es válida
    public boolean isValidDeclarationWithAssignment(String input) {
        // Patrón para la declaración con asignación: tipoDato nombreVariable = cualquierCantidadDeOperaciones
        // Permite cualquier cantidad de operaciones matemáticas básicas con operandos
        String pattern = "^(ENTERO|REAL|CARACTER|CADENA|BOOLEANO|LARGO|DOBLE)\\s+\\w+\\s*=\\s*(?:[\\w.]+|\\(\\s*(?:(?:\\w+|\\(\\s*(?:[\\w.]+|\\s*[+\\-*/]\\s*)*\\s*\\))\\s*[+\\-*/]\\s*)*)$";

        // Verificar si la entrada coincide con el patrón
        return Pattern.matches(pattern, input);
    }
}
