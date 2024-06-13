
package compiladores.DeclarationAndAssignment.machines.afds;

import java.util.regex.Pattern;

public class Afd_DeclarationAndAssignment {
    // Método para verificar si una declaración con asignación es válida
    public boolean isValidDeclarationAssignment(String input) {
        // Patrón para las declaraciones con asignaciones, incluyendo arreglos y matrices
        String pattern = "^(ENTERO|REAL|CARACTER|CADENA|BOOLEANO|LARGO|DOBLE)(\\[\\d*\\])*\\s+\\w+\\s*=\\s*(NULO|FALSO|VERDADERO|\\d+|\\d+\\.\\d+|'.'|\".*\"|\\[(\\d+(,\\d+)*)*\\]|\\[(\\[(\\d+(,\\d+)*)\\])(,\\[(\\d+(,\\d+)*)\\])*\\])$";
        
        // Verificar si la entrada coincide con el patrón
        return Pattern.matches(pattern, input);
    }
}
