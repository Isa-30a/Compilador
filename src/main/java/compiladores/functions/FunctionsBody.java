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

public class FunctionsBody {

    private List<String> functionsNames = new ArrayList<>();
    private static boolean mainFlag = false;

    public String body(String chain) {
        FunctionsUtils v = new FunctionsUtils();
        CallFunction c = new CallFunction();
        boolean error = false;
        String headerExpresion = "\\s*(FUNCION(\\s+[a-zA-Z_][a-zA-Z0-9_]*\\s*)([(]((\\s*)|((\\s*)(BOOLEANO|ENTERO|FLOTANTE|CARACTER)\\s+[a-zA-Z_][a-zA-Z0-9_]*(\\s*\\[\\s*\\]\\s*|\\s*))((\\s*)[,](\\s*)((BOOLEANO|ENTERO|FLOTANTE|CARACTER)\\s+[a-zA-Z_][a-zA-Z0-9_]*(\\s*\\[\\s*\\]\\s*|\\s*)))*)(\\s*)[)])\\s*:\\s*(BOOLEANO|ENTERO|FLOTANTE|CARACTER|VACIO)\\s*)|(INICIO\\s*)";
        String bodyExpresion = "(\\w|\\W)*|(RETORNA\\s+((([a-zA-Z_][a-zA-Z0-9_]*|[0-9]+)\\s*(\\s*([\\||]|[\\&&]|[+]|[-]|[*]|[/]|[%])\\s*([a-zA-Z_][a-zA-Z0-9_]*|[0-9]+))*)\\s*))*";
        String footerExpresion = "\\s*FIN (FUNCION|INICIO)\\s*";
        String[] fullString = v.splitExpresion(chain.trim().split("\\n"));
        String result = "";
        String[] splitChain = null;
        List<String> newValue = new ArrayList<>();
        boolean flag = false;
        boolean returnFlag = false;

        if (Pattern.compile(headerExpresion).matcher(fullString[0]).find()) {
            splitChain = v.splitExpresion(fullString[0].trim().split("[ (),:]"));

            if (splitChain[0].equals("FUNCION")) {
                error = v.isType(splitChain[splitChain.length - 1]);
                if (error) {
                    flag = v.types(splitChain[splitChain.length - 1]) == "void" ? true : false;
                    newValue.add(v.types(splitChain[splitChain.length - 1]));
                    newValue.add(" ");
                    newValue.add(splitChain[1] + "(");

                    for (int i = 2; i < splitChain.length; i++) {
                        if (v.isType(splitChain[i]) && i == splitChain.length - 1) {
                            break;
                        }
                        if (i > 2 && i != splitChain.length && v.isType(splitChain[i])) {
                            newValue.add(", ");
                        }
                        newValue.add(v.types(splitChain[i]));
                        if (v.isType(splitChain[i])) {
                            newValue.add(" ");
                        }
                    }
                    newValue.add("){\n");

                    for (int j = 1; j < fullString.length - 1; j++) {
                        if (Pattern.compile("FUNCION").matcher(fullString[j]).find()) {

                        }
                        if (Pattern.compile(bodyExpresion).matcher(fullString[j]).find()) {
                            splitChain = v.splitExpresion(fullString[j].trim().split(" "));

                            for (int k = 0; k < splitChain.length; k++) {
                                if (k == 0) {
                                    newValue.add("\t");
                                }
                                if (splitChain[k].equals("FUNCION")) {
                                    newValue.add("Syntax Error");
                                    continue;
                                }
                                if (splitChain[k].indexOf("(") == 0) {
                                    if (c.call(splitChain[k - 1], functionsNames).equals("Syntax Error")) {
                                        newValue.remove(newValue.size() - 1);
                                        newValue.remove(newValue.size() - 1);
                                        newValue.add("Syntax Error");
                                        break;
                                    }
                                } else if (splitChain[k].indexOf("(") != -1) {
                                    String auxChain = splitChain[k].substring(0, splitChain[k].indexOf("("));
                                    if (c.call(auxChain, functionsNames).equals("Syntax Error")) {
                                        newValue.add("Syntax Error");
                                        break;
                                    }
                                }

                                if (v.checkReturn(splitChain[k])) {
                                    if (!flag) {
                                        returnFlag = true;
                                        newValue.add("return ");
                                        continue;
                                    } else {
                                        newValue.add("Syntax Error");
                                        break;
                                    }

                                }
                                if (!returnFlag) {
                                    newValue.add(splitChain[k]);
                                    if (splitChain[k].charAt(splitChain[k].length() - 1) == ')' && k == splitChain.length - 1) {
                                        newValue.add(";");
                                    } else {
                                        newValue.add(" ");
                                    }
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
                        splitChain = v.splitExpresion(fullString[fullString.length - 1].trim().split(" "));
                        if (splitChain[splitChain.length - 1].equals("FUNCION")) {
                            newValue.add("}");
                        } else {
                            return "Syntax Error";
                        }

                    } else {
                        return "Syntax Error";
                    }
                }
            } else if (splitChain[0].equals("INICIO")) {
                newValue.add("\nint main(){\n");
                mainFlag = true;
                for (int j = 1; j < fullString.length; j++) {
                    if (j == fullString.length - 1) {
                        break;
                    }
                    if (Pattern.compile(bodyExpresion).matcher(fullString[j]).find()) {
                        splitChain = v.splitExpresion(fullString[j].trim().split(" "));

                        for (int k = 0; k < splitChain.length; k++) {
                            if (k == 0) {
                                newValue.add("\t");
                            }
                            if (splitChain[k].equals("FUNCION")) {
                                newValue.add("Syntax Error");
                                continue;
                            }
                            if (splitChain[k].indexOf("(") == 0) {
                                if (c.call(splitChain[k - 1], functionsNames).equals("Syntax Error")) {
                                    newValue.remove(newValue.size() - 1);
                                    newValue.remove(newValue.size() - 1);
                                    newValue.add("Syntax Error");
                                    break;
                                }
                            } else if (splitChain[k].indexOf("(") != -1) {
                                String auxChain = splitChain[k].substring(0, splitChain[k].indexOf("("));
                                if (c.call(auxChain, functionsNames).equals("Syntax Error")) {
                                    newValue.add("Syntax Error");
                                    break;
                                }
                            }
                            if (v.checkReturn(splitChain[k])) {
                                returnFlag = true;
                                newValue.add("return ");
                                continue;
                            }
                            if (!returnFlag) {
                                newValue.add(splitChain[k]);
                                if (splitChain[k].charAt(splitChain[k].length() - 1) == ')' && k == splitChain.length - 1) {
                                    newValue.add(";");
                                } else {
                                    newValue.add(" ");
                                }
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
                    splitChain = v.splitExpresion(fullString[fullString.length - 1].trim().split(" "));
                    if (splitChain[splitChain.length - 1].equals("INICIO")) {
                        newValue.add("}");
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

    public List<String> getFunctionsNames() {
        return functionsNames;
    }

    public static boolean isMain() {
        return mainFlag;
    }
}
