
package compiladores.DeclarationAndAssignment.machines.afds;

import java.util.regex.Pattern;

public class Afd_Assignment {

    // Método para verificar si una asignación es válida
    public boolean isValidAssignment(String input) {
        // Patrón para las asignaciones: variable = valor, incluyendo arreglos y matrices
        String pattern = "^\\w+(\\[\\d*\\])*\\s*=\\s*(NULO|FALSO|VERDADERO|\\d+|\\d+\\.\\d+|'.'|\".*\"|\\[.*\\]|\\[.*\\]\\[.*\\])$";

        // Verificar si la entrada coincide con el patrón
        return Pattern.matches(pattern, input);
    }
}
