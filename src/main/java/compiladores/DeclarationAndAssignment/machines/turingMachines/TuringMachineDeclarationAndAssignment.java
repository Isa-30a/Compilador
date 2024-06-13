
package compiladores.DeclarationAndAssignment.machines.turingMachines;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TuringMachineDeclarationAndAssignment {

    private Map<String, String> typeTranslationMap;

    public TuringMachineDeclarationAndAssignment() {
        typeTranslationMap = new HashMap<>();
        typeTranslationMap.put("ENTERO", "int");
        typeTranslationMap.put("REAL", "float");
        typeTranslationMap.put("CARACTER", "char");
        typeTranslationMap.put("CADENA", "string");
        typeTranslationMap.put("BOOLEANO", "bool");
        typeTranslationMap.put("LARGO", "long");
        typeTranslationMap.put("DOBLE", "double");
    }

    public String translateDeclarationAndAssignment(String pseudocode) {
        // Eliminar espacios extra y dividir por el signo igual
        String[] tokens = pseudocode.trim().split("\\s*=\\s*", 2);
        if (tokens.length != 2) {
            return "El pseudocódigo no es válido.";
        }
        
        String declarationPart = tokens[0].trim();
        String assignmentPart = tokens[1].trim();
        
        // Revisar si es una declaración
        if (isDeclaration(declarationPart)) {
            return translateDeclaration(declarationPart) + " = " + translateAssignment(assignmentPart) + ";";
        } else {
            return "El pseudocódigo no es válido.";
        }
    }

    private boolean isDeclaration(String part) {
        // Dividir por espacios para verificar si es una declaración válida
        String[] tokens = part.trim().split("\\s+");
        if (tokens.length != 2) {
            return false;
        }
        // Verificar si el primer token es un tipo de dato conocido
        return typeTranslationMap.containsKey(tokens[0].trim());
    }

    private String translateDeclaration(String declaration) {
        String[] tokens = declaration.trim().split("\\s+");
        if (tokens.length != 2) {
            return "El pseudocódigo no es válido.";
        }
        String type = tokens[0].trim();
        String variable = tokens[1].trim();
        String cPlusPlusType = translateType(type);

        // Check if it's an array or matrix declaration
        if (type.contains("[")) {
            // Extract the size of the array or matrix
            String arraySizes = type.substring(type.indexOf("[")).replaceAll("\\]", "]");
            return cPlusPlusType + " " + variable + arraySizes;
        }

        return cPlusPlusType + " " + variable;
    }

    private String translateAssignment(String assignment) {
        if (assignment.startsWith("[") && assignment.endsWith("]")) {
            // It's an array or matrix assignment
            return translateArrayOrMatrix(assignment);
        } else {
            // It's a simple value assignment
            return translateValue(assignment);
        }
    }

    private String translateType(String type) {
        // Remove any array size to get the base type
        String baseType = type.replaceAll("\\[.*\\]", "");
        return typeTranslationMap.getOrDefault(baseType, "Error de traducción");
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
                return value;
        }
    }

    private String translateArrayOrMatrix(String value) {
        String content = value.substring(1, value.length() - 1).trim();
        String[] elements = splitByTopLevelCommas(content);
        StringBuilder result = new StringBuilder("{");
        for (int i = 0; i < elements.length; i++) {
            if (i > 0) {
                result.append(", ");
            }
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


