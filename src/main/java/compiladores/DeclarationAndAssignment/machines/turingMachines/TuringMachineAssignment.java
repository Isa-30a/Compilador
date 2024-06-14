
package compiladores.DeclarationAndAssignment.machines.turingMachines;

import java.util.ArrayList;
import java.util.List;

public class TuringMachineAssignment {

    public String translateAssignment(String pseudocode) {
        String[] tokens = pseudocode.split("\\s*=\\s*");
        if (tokens.length != 2) {
            return "El pseudocódigo no es válido.";
        }
        String variable = tokens[0].trim();
        String value = tokens[1].trim();
        return variable + " = " + translateValue(value) + ";";
    }

    private String translateValue(String value) {
        switch (value) {
            case "NULO":
                return "null";
            case "FALSO":
                return "false";
            case "VERDADERO":
                return "true";
            default:
                // Check if value is an array or matrix
                if (value.startsWith("[") && value.endsWith("]")) {
                    return translateArrayOrMatrix(value);
                }
                return value;
        }
    }

    private String translateArrayOrMatrix(String value) {
        // Remove leading and trailing brackets
        String content = value.substring(1, value.length() - 1).trim();
        // Split by commas that are outside nested brackets
        String[] elements = splitByTopLevelCommas(content);
        StringBuilder result = new StringBuilder("{");
        for (int i = 0; i < elements.length; i++) {
            if (i > 0) {
                result.append(", ");
            }
            // Translate each element recursively
            result.append(translateValue(elements[i].trim()));
        }
        result.append("}");
        return result.toString();
    }

    private String[] splitByTopLevelCommas(String content) {
        int level = 0;
        StringBuilder currentElement = new StringBuilder();
        List<String> elements = new ArrayList<>();
        for (char c : content.toCharArray()) {
            if (c == '[') {
                level++;
            } else if (c == ']') {
                level--;
            } else if (c == ',' && level == 0) {
                elements.add(currentElement.toString().trim());
                currentElement.setLength(0);
                continue;
            }
            currentElement.append(c);
        }
        elements.add(currentElement.toString().trim());
        return elements.toArray(new String[0]);
    }
}








