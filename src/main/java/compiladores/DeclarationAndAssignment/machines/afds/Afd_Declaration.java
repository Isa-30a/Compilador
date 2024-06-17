
package compiladores.DeclarationAndAssignment.machines.afds;

import java.util.regex.Pattern;

public class Afd_Declaration {

    // Método para verificar si una declaración es válida
    public boolean isValidDeclaration(String input) {
        // Patrón para las declaraciones: tipo de dato seguido de una variable, incluyendo arreglos y matrices
        String pattern = "^(ENTERO|REAL|CARACTER|CADENA|BOOLEANO|LARGO|DOBLE)(\\[\\d*\\])*(\\[\\d*\\])*\\s+\\w+$";
        // Verificar si la entrada coincide con el patrón
        return Pattern.matches(pattern, input);
    }
}
