/**
 * @authors
 * Wilson Jimenez
 * Kevin Carmona
 * Yurleis Zuluaga
 * Greison Castilla
 * Andrés Quintana
 */
package compiladores.functions;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class VerificarFuncion {

    String[] dividirExpresion(String[] s) {
        int i = 0;
        List<String> nuevo = new ArrayList<>();

        do {
            int old = i;
            
            if (!s[i].isBlank()) {
                nuevo.add(s[i]);
            }

            i++;
        } while (i < s.length);

        return nuevo.toArray(new String[0]);
    }

    String tipos(String s) {
        if (Pattern.compile("ENTERO").matcher(s).find()) {
            return "int";
        }
        if (Pattern.compile("FLOTANTE").matcher(s).find()) {
            return "float";
        }
        if (Pattern.compile("BOOLEANO").matcher(s).find()) {
            return "bool";
        }
        if (Pattern.compile("CARACTER").matcher(s).find()) {
            return "char";
        }
        if (Pattern.compile("VACIO").matcher(s).find()) {
            return "void";
        }

        return s;
    }
    
    boolean checkReturn(String s) {
        return Pattern.compile("RETORNA").matcher(s).find();
    }
    
    boolean checkEnd(String s) {
        return Pattern.compile("FIN").matcher(s).find();
    }

    boolean isType(String s) {
        boolean error = false;

        if (Pattern.compile("ENTERO").matcher(s).find()) {
            error = true;
        } else if (Pattern.compile("FLOTANTE").matcher(s).find()) {
            error = true;
        } else if (Pattern.compile("BOOLEANO").matcher(s).find()) {
            error = true;
        } else if (Pattern.compile("CARACTER").matcher(s).find()) {
            error = true;
        } else if (Pattern.compile("VACIO").matcher(s).find()) {
            error = true;
        }
        return error;
    }
}