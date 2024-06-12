/**
 * @authors
 * Wilson Jimenez
 * Kevin Carmona
 * Yurleis Zuluaga
 * Greison Castilla
 * Andrés Quintana
 */
package functions;

import java.util.List;

public class LlamarFuncion {

    public String llamar(String cadena, List<String> array) {
        if (array.contains(cadena)) {
            return cadena;
        } else {
            return "Syntax Error";
        }
    }
}
