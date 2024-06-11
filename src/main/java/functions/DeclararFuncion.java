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

public class DeclararFuncion {

    private boolean flag = false;

    public String declarar(String cadena) {
        VerificarFuncion v = new VerificarFuncion();
        boolean error = false;
        String[] cadena_split = v.dividirExpresion(cadena.trim().split("[ (),\\n]"));
        String result = "";
        String expresion = "FUNCION\\s+(BOOLEANO|ENTERO|FLOTANTE|CARACTER|VACIO)(\\s+[a-zA-Z_][a-zA-Z0-9_]*\\s*)([(]((\\s*)|((\\s*)(BOOLEANO|ENTERO|FLOTANTE|CARACTER)\\s+[a-zA-Z_][a-zA-Z0-9_]*(\\s*\\[\\s*\\]\\s*|\\s*))((\\s*)[,](\\s*)((BOOLEANO|ENTERO|FLOTANTE|CARACTER)\\s+[a-zA-Z_][a-zA-Z0-9_]*(\\s*\\[\\s*\\]\\s*|\\s*)))*)(\\s*)[)])(\\s|\\n)+(RETORNA\\s+[a-zA-Z_][a-zA-Z0-9_]*(\\s|\\n)+)*FIN FUNCION";

        if (Pattern.compile(expresion).matcher(cadena).find()) {
            //si entra
            if (cadena_split[0].equals("FUNCION")) {
                cadena_split[0] = "";
                error = v.isType(cadena_split[2]);
            }
            if (!error) {
                List<String> nuevo = new ArrayList<>();
                flag = v.tipos(cadena_split[1]) == "void" ? true : false;
                for (int i = 0; i < cadena_split.length; i++) {
                    if (i > 3 && i != cadena_split.length && v.isType(cadena_split[i])) {
                        nuevo.add(", ");
                    }

                    if (cadena_split[i].equals("RETORNA")) {
                        if (flag) {
                            return "Syntax Error";
                        }
                        break;
                    }

                    if (v.checkEnd(cadena_split[i])) {
                        break;
                    }

                    nuevo.add(v.tipos(cadena_split[i]));
                    if (v.isType(cadena_split[i])) {
                        nuevo.add(" ");
                    }

                    if (i == 2) {
                        nuevo.add("(");
                    }
                }
                nuevo.add(");");
                cadena_split = nuevo.toArray(new String[0]);
                result = String.join("", cadena_split).trim();
                return result;

            } else {
                return "Syntax Error";
            }
        } else {
            return "Syntax Error";
        }
    }

}
