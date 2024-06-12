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

public class CallFunction {

    public String call(String chain, List<String> array) {
        if (array.contains(chain)) {
            return chain;
        } else {
            return "Syntax Error";
        }
    }
}
