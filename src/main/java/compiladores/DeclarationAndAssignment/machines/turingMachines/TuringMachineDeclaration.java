
package compiladores.DeclarationAndAssignment.machines.turingMachines;

import java.util.HashMap;
import java.util.Map;

public class TuringMachineDeclaration {

    private Map<String, String> typeTranslationMap;

    public TuringMachineDeclaration() {
        typeTranslationMap = new HashMap<>();
        typeTranslationMap.put("ENTERO", "int");
        typeTranslationMap.put("REAL", "float");
        typeTranslationMap.put("CARACTER", "char");
        typeTranslationMap.put("CADENA", "string");
        typeTranslationMap.put("BOOLEANO", "bool");
        typeTranslationMap.put("LARGO", "long");
        typeTranslationMap.put("DOBLE", "double");
    }

    public String translateDeclaration(String pseudocode) {
        String[] tokens = pseudocode.split("\\s+");
        if (tokens.length != 2) {
            return "El pseudocódigo no es válido.";
        }
        String type = tokens[0].trim();
        String variable = tokens[1].trim();
        String cPlusPlusType = translateType(type);

        // Verificar si es un arreglo o una matriz
        if (type.contains("[")) {
            // Extraer el tamaño del arreglo o matriz
            String arraySizes = type.substring(type.indexOf("[")).replaceAll("\\]", "]");
            return cPlusPlusType + " " + variable + arraySizes + ";";
        }

        return cPlusPlusType + " " + variable + ";";
    }

    private String translateType(String type) {
        // Quitar cualquier tamaño de arreglo para obtener el tipo base
        String baseType = type.replaceAll("\\[.*\\]", "");
        return typeTranslationMap.getOrDefault(baseType, "Error de traducción");
    }
}
