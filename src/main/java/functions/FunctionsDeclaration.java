/**
 * @authors
 * Wilson Jimenez
 * Kevin Carmona
 * Yurleis Zuluaga
 * Greison Castilla
 * Andrés Quintana
 */
package functions;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class FunctionsDeclaration {
    public String declare(String chain, List<String> array) {
        FunctionsUtils v = new FunctionsUtils();
        boolean error = false;
        String headerExpresion = "\\s*FUNCION(\\s+[a-zA-Z_][a-zA-Z0-9_]*\\s*)([(]((\\s*)|((\\s*)(BOOLEANO|ENTERO|FLOTANTE|CARACTER)\\s+[a-zA-Z_][a-zA-Z0-9_]*(\\s*\\[\\s*\\]\\s*|\\s*))((\\s*)[,](\\s*)((BOOLEANO|ENTERO|FLOTANTE|CARACTER)\\s+[a-zA-Z_][a-zA-Z0-9_]*(\\s*\\[\\s*\\]\\s*|\\s*)))*)(\\s*)[)])\\s*:\\s*(BOOLEANO|ENTERO|FLOTANTE|CARACTER|VACIO)\\s*";
        String[] fullString = v.splitExpresion(chain.trim().split("\\n"));
        String result = "";
        String[] splitChain = null;
        List<String> newValue = new ArrayList<>();

        if (Pattern.compile(headerExpresion).matcher(fullString[0]).find()) {
            splitChain = v.splitExpresion(fullString[0].trim().split("[ (),:]"));

            if (splitChain[0].equals("FUNCION")) {
                error = v.isType(splitChain[splitChain.length - 1]);
            }
            if (error) {
                newValue.add(v.types(splitChain[splitChain.length - 1]));
                newValue.add(" ");
                newValue.add(splitChain[1] + "(");
                array.add(splitChain[1]);
                for (int i = 2; i < splitChain.length - 1; i++) {

                    if (i > 2 && i != splitChain.length && v.isType(splitChain[i])) {
                        newValue.add(", ");
                    }

                    newValue.add(v.types(splitChain[i]));
                    if (v.isType(splitChain[i])) {
                        newValue.add(" ");
                    }
                }
                newValue.add(");");
            }
            splitChain = newValue.toArray(new String[0]);
            result = String.join("", splitChain).trim();
            return result;
        } else {
            return "Syntax Error";
        }
    }

}
