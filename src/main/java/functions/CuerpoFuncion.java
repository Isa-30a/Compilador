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

public class CuerpoFuncion {

    public String cuerpo(String cadena) {
        VerificarFuncion v = new VerificarFuncion();
        boolean error = false;
        String headerExpresion = "\\s*(FUNCION(\\s+[a-zA-Z_][a-zA-Z0-9_]*\\s*)([(]((\\s*)|((\\s*)(BOOLEANO|ENTERO|FLOTANTE|CARACTER)\\s+[a-zA-Z_][a-zA-Z0-9_]*(\\s*\\[\\s*\\]\\s*|\\s*))((\\s*)[,](\\s*)((BOOLEANO|ENTERO|FLOTANTE|CARACTER)\\s+[a-zA-Z_][a-zA-Z0-9_]*(\\s*\\[\\s*\\]\\s*|\\s*)))*)(\\s*)[)])\\s*:\\s*(BOOLEANO|ENTERO|FLOTANTE|CARACTER|VACIO)\\s*)|(INICIO\\s*)";
        String bodyExpresion = "(\\w|\\W)*|(RETORNA\\s+((([a-zA-Z_][a-zA-Z0-9_]*|[0-9]+)\\s*(\\s*([+]|[-]|[*]|[/]|[%])\\s*([a-zA-Z_][a-zA-Z0-9_]*|[0-9]+))*)\\s*))*";
        String footerExpresion = "\\s*FIN (FUNCION|INICIO)\\s*";
        String[] fullString = v.dividirExpresion(cadena.trim().split("\\n"));
        String result = "";
        String[] splitChain = null;
        List<String> newValue = new ArrayList<>();
        boolean flag = false;
        boolean returnFlag = false;

        if (Pattern.compile(headerExpresion).matcher(fullString[0]).find()) {
            splitChain = v.dividirExpresion(fullString[0].trim().split("[ (),]"));

            if (splitChain[0].equals("FUNCION")) {
                error = v.isType(splitChain[splitChain.length - 1]);
                if (error) {
                    flag = v.tipos(splitChain[splitChain.length - 1]) == "void" ? true : false;
                    newValue.add(v.tipos(splitChain[splitChain.length - 1]));
                    newValue.add(" ");
                    newValue.add(splitChain[1] + "(");
                    for (int i = 2; i < splitChain.length; i++) {
                        if (i > 2 && i != splitChain.length && v.isType(splitChain[i])) {
                            newValue.add(", ");
                        }
                        newValue.add(v.tipos(splitChain[i]));
                        if (v.isType(splitChain[i])) {
                            newValue.add(" ");
                        }
                    }
                    newValue.add("){\n");

                    for (int j = 1; j < fullString.length; j++) {
                        if (j == fullString.length - 1) {
                            break;
                        }
                        if (Pattern.compile(bodyExpresion).matcher(fullString[j]).find()) {
                            splitChain = v.dividirExpresion(fullString[j].trim().split(" "));
                            for (int k = 0; k < splitChain.length; k++) {
                                if (k == 0) {
                                    newValue.add("\t");
                                }
                                if (v.checkReturn(splitChain[k])) {
                                    returnFlag = true;
                                    newValue.add("return ");
                                    continue;
                                }
                                if (!returnFlag) {
                                    newValue.add(splitChain[k]);
                                    newValue.add(" ");
                                } else {
                                    newValue.add(splitChain[k]);
                                    if (k == splitChain.length - 1) {
                                        newValue.add(";");
                                    } else {
                                        newValue.add(" ");
                                    }
                                }
                            }
                            newValue.add("\n");
                        } else {
                            return "Syntax Error";
                        }
                    }
                    if (Pattern.compile(footerExpresion).matcher(fullString[fullString.length - 1]).find()) {
                        splitChain = v.dividirExpresion(fullString[fullString.length - 1].trim().split(" "));
                        if (splitChain[splitChain.length - 1].equals("FUNCION")) {
                            newValue.add("}\n");
                        } else {
                            return "Syntax Error";
                        }

                    } else {
                        return "Syntax Error";
                    }
                }
            } else if (splitChain[0].equals("INICIO")) {
                newValue.add("int main(){\n");
                for (int j = 1; j < fullString.length; j++) {
                    if (j == fullString.length - 1) {
                        break;
                    }
                    if (Pattern.compile(bodyExpresion).matcher(fullString[j]).find()) {
                        splitChain = v.dividirExpresion(fullString[j].trim().split(" "));
                        
                        for (int k = 0; k < splitChain.length; k++) {
                            if (k == 0) {
                                newValue.add("\t");
                            }
                            if (v.checkReturn(splitChain[k])) {
                                returnFlag = true;
                                newValue.add("return ");
                                continue;
                            }
                            if (!returnFlag) {
                                newValue.add(splitChain[k]);
                                newValue.add(" ");
                            } else {
                                newValue.add(splitChain[k]);
                                if (k == splitChain.length - 1) {
                                    newValue.add(";");
                                } else {
                                    newValue.add(" ");
                                }
                            }
                        }
                        newValue.add("\n");
                    } else {
                        return "Syntax Error";
                    }
                }
                if (Pattern.compile(footerExpresion).matcher(fullString[fullString.length - 1]).find()) {
                    splitChain = v.dividirExpresion(fullString[fullString.length - 1].trim().split(" "));
                    if (splitChain[splitChain.length - 1].equals("INICIO")) {
                        newValue.add("}\n");
                    } else {
                        return "Syntax Error";
                    }

                } else {
                    return "Syntax Error";
                }
            }
            splitChain = newValue.toArray(new String[0]);
            result = String.join("", splitChain).trim();
            return result;
        } else {
            return "Syntax Error";
        }
    }
}
