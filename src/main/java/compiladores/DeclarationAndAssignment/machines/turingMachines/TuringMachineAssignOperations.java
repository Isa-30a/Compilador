/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package compiladores.DeclarationAndAssignment.machines.turingMachines;

import java.util.Stack;

public class TuringMachineAssignOperations {

    public String translateAssignment(String input) {
        String[] tokens = input.split("\\s*=\\s*");

        if (tokens.length != 2) {
            return "El pseudocódigo no es válido.";
        }

        String variable = tokens[0].trim();
        String expression = tokens[1].trim();

        // Validar que sea una asignación válida (aquí se asume que ya fue validada por el AFD)
        if (!new compiladores.DeclarationAndAssignment.machines.afds.Afd_AssignOperations().isValidAssignment(input)) {
            return "El pseudocódigo no es válido.";
        }

        // Traducir la expresión matemática
        String translatedExpression = translateExpression(expression);

        return variable + " = " + translatedExpression + ";";
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




