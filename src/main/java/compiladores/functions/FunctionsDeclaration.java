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
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Pattern;

public class FunctionsDeclaration {


    public String declare(String chain, List<String> array) {
        FunctionsUtils v = new FunctionsUtils();
        boolean error = false;
        String headerExpresion = "\\s*FUNCION(\\s+[a-zA-Z_][a-zA-Z0-9_]*\\s*)([(]((\\s*)|((\\s*)(BOOLEANO|ENTERO|FLOTANTE|CARACTER)\\s+[a-zA-Z_][a-zA-Z0-9_]*(\\s*\\[\\s*([a-zA-Z_][a-zA-Z0-9_]*|[0-9]+)*\\s*\\]\\s*)*)((\\s*)[,](\\s*)((BOOLEANO|ENTERO|FLOTANTE|CARACTER)\\s+[a-zA-Z_][a-zA-Z0-9_]*(\\s*\\[\\s*([a-zA-Z_][a-zA-Z0-9_]*|[0-9]+)*\\s*\\]\\s*|\\s*)))*)(\\s*)[)])\\s*:\\s*(BOOLEANO|ENTERO|FLOTANTE|CARACTER|VACIO)\\s*";
        String result = "";
        String[] splitChain = null;
        List<String> newValue = new ArrayList<>();
        boolean paramFlag = false;
        
        if(!chain.isBlank()) {
            if (Pattern.compile(headerExpresion).matcher(chain).find()) {
                splitChain = chain.trim().split("[ (),:]");
                if (splitChain[0].equals("FUNCION")) {
                    error = v.isType(splitChain[splitChain.length - 1]);
                    if (error) {
                        newValue.add(v.types(splitChain[splitChain.length - 1]));
                        newValue.add(" ");
                        newValue.add(splitChain[1] + "(");
                        array.add(splitChain[1]);
                        for (int i = 2; i < splitChain.length; i++) {
                            if (v.isType(splitChain[i]) && i == splitChain.length - 1) {
                                break;
                            }
                            if (i != splitChain.length && v.isType(splitChain[i]) && paramFlag) {
                                newValue.add(", ");
                            }
                            newValue.add(v.types(splitChain[i]));
                            if (v.isType(splitChain[i])) {
                                newValue.add(" ");
                                paramFlag = true;
                            }
                        }
                        newValue.add(");\n");
                    }
                }
            }
        }
        if (splitChain == null) {
            return "";
        } else {
            result = String.join("", newValue.toArray(new String[0]));
            return result;
        }
    }
    
}
