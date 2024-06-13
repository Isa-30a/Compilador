
package compiladores.DeclarationAndAssignment.machines.turingMachines;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class TuringMachineDecWithAssignOperations {
    private Map<String, String> typeTranslationMap;

    public TuringMachineDecWithAssignOperations() {
        typeTranslationMap = new HashMap<>();
        typeTranslationMap.put("ENTERO", "int");
        typeTranslationMap.put("REAL", "float");
        typeTranslationMap.put("CARACTER", "char");
        typeTranslationMap.put("CADENA", "string");
        typeTranslationMap.put("BOOLEANO", "bool");
        typeTranslationMap.put("LARGO", "long");
        typeTranslationMap.put("DOBLE", "double");
    }

    public String translateDeclarationAndAssignment(String input) {
        String[] tokens = input.split("\\s*=\\s*");

        if (tokens.length != 2) {
            return "El pseudocódigo no es válido.";
        }

        String declaration = tokens[0].trim();
        String expression = tokens[1].trim();

        // Validar que sea una declaración con asignación válida (usando el AFD correspondiente)
        if (!new compiladores.DeclarationAndAssignment.machines.afds.Afd_DecWithAssignOperations().isValidDeclarationWithAssignment(input)) {
            return "El pseudocódigo no es válido.";
        }

        // Separar la declaración en tipo de dato y nombre de variable
        String[] declarationTokens = declaration.split("\\s+");
        if (declarationTokens.length != 3) {
            return "El pseudocódigo no es válido.";
        }
        String type = declarationTokens[0].trim();
        String variable = declarationTokens[1].trim();
        String cPlusPlusType = translateType(type);

        return cPlusPlusType + " " + variable + " = " + translateExpression(expression) + ";";
    }

    private String translateType(String type) {
        // Obtener el tipo de dato correspondiente en C++
        return typeTranslationMap.getOrDefault(type, "Error de traducción");
    }

    private String translateExpression(String expression) {
        // Utilizaremos un enfoque básico para traducir la expresión matemática
        StringBuilder translatedExpression = new StringBuilder();

        // Stack para manejar los paréntesis
        Stack<Character> stack = new Stack<>();

        int i = 0;
        while (i < expression.length()) {
            char c = expression.charAt(i);

            if (Character.isLetterOrDigit(c) || c == ' ' || c == '.') {
                translatedExpression.append(c);
            } else if (c == '(') {
                stack.push(c);
                translatedExpression.append('(');
            } else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    translatedExpression.append(stack.pop());
                }
                stack.pop(); // Eliminar el '(' del stack
                translatedExpression.append(')');
            } else {
                // Es un operador +, -, *, /
                while (!stack.isEmpty() && precedence(stack.peek()) >= precedence(c)) {
                    translatedExpression.append(stack.pop());
                }
                stack.push(c);
            }

            i++;
        }

        // Agregar el resto de operadores en el stack
        while (!stack.isEmpty()) {
            translatedExpression.append(stack.pop());
        }

        return translatedExpression.toString();
    }

    // Función para determinar la precedencia de los operadores
    private int precedence(char c) {
        if (c == '+' || c == '-') {
            return 1;
        } else if (c == '*' || c == '/') {
            return 2;
        }
        return 0; // En caso de paréntesis '(' y ')'
    }
}



