package proyecto.automatas.condicionalesprueba;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Usuario
 */
public class IfElseConverter {

    private static int placeholderCount = 0;
    private static Map<String, String> textPlaceholders = new HashMap<>();

    public static String convertToCpp(String pseudocode) {
        pseudocode = protectQuotedText(pseudocode);

        String oldPseudocode;
        do {
            oldPseudocode = pseudocode;
            
            pseudocode = convertSwitchCase(pseudocode);
            //pseudocode = convertIfElseVarius(pseudocode);
            //pseudocode = convertIfElseNested(pseudocode)
            /*if (containsNestedIf(pseudocode)) {
                pseudocode = convertIfElseNested(pseudocode);
            } else if (containsMultipleElse(pseudocode)) {
                pseudocode = convertIfElseVarius(pseudocode);
            } else {
                pseudocode = convertIfElse(pseudocode);
            }*/
            if (containsSwitchCase(pseudocode)) {
                pseudocode = convertSwitchCase(pseudocode);
            }
            if (containsNestedIf(pseudocode)) {
                pseudocode = convertIfElseNested(pseudocode);
            }
            if (containsMultipleElse(pseudocode)) {
                pseudocode = convertIfElseVarius(pseudocode);
            } else {
                pseudocode = convertIfElse(pseudocode);
            }
        } while (!pseudocode.equals(oldPseudocode));

        pseudocode = restoreQuotedText(pseudocode);
        return pseudocode;
    }

        private static boolean containsNestedIf(String pseudocode) {
        Pattern pattern = Pattern.compile("SI\\s*\\(([^\\)]+)\\)\\s*ENTONCES\\s*.*SI\\s*\\(([^\\)]+)\\)", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(pseudocode);
        return matcher.find();
    }

    private static boolean containsMultipleElse(String pseudocode) {
        Pattern pattern = Pattern.compile("SINO\\s*\\(([^\\)]+)\\)", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(pseudocode);
        return matcher.find();
    }

    private static boolean containsSwitchCase(String pseudocode) {
        Pattern pattern = Pattern.compile("SEGUN\\s*(\\w+)\\s*HACER", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(pseudocode);
        return matcher.find();
    }
        
    private static String protectQuotedText(String pseudocode) {
        Pattern pattern = Pattern.compile("\"([^\"]*)\"");
        Matcher matcher = pattern.matcher(pseudocode);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String placeholder = "QUOTED_TEXT_" + placeholderCount++;
            textPlaceholders.put(placeholder, matcher.group(0));
            matcher.appendReplacement(sb, placeholder);
        }
        matcher.appendTail(sb);
        pseudocode = sb.toString();

        pattern = Pattern.compile("'([^']*)'");
        matcher = pattern.matcher(pseudocode);
        sb = new StringBuffer();
        while (matcher.find()) {
            String placeholder = "QUOTED_TEXT_" + placeholderCount++;
            textPlaceholders.put(placeholder, matcher.group(0));
            matcher.appendReplacement(sb, placeholder);
        }
        matcher.appendTail(sb);
        return pseudocode;
    }

    private static String restoreQuotedText(String pseudocode) {
        for (Map.Entry<String, String> entry : textPlaceholders.entrySet()) {
            pseudocode = pseudocode.replace(entry.getKey(), entry.getValue());
        }
        return pseudocode;
    }
//este solo puede hacer if con un solo else
   private static String convertIfElse(String pseudocode) {

        Pattern pattern = Pattern.compile("(?sm)SI\\s*(\\([^\\)]+\\))\\s*ENTONCES:\\s*(.*?)\\s*(SINO:\\s*(.*?)\\s*)?FINSI");
        Matcher matcher = pattern.matcher(pseudocode);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String condition = matcher.group(1).trim();
            String thenBlock = matcher.group(2).trim();
            String elseBlock = matcher.group(4) != null ? matcher.group(4).trim() : null;

            String replacement = "if " + condition + " {\n" + thenBlock + "\n}";
            if (elseBlock != null) {
                replacement += " else {\n" + elseBlock + "\n}";
            }

            matcher.appendReplacement(sb, replacement);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
   
   private static String convertIfElseVarius(String pseudocode) {
        Pattern pattern = Pattern.compile("SI\\s*\\(([^\\)]+)\\)\\s*ENTONCES:\\s*(.*?)\\s*(SINO\\s*\\(([^\\)]+)\\)\\s*ENTONCES\\s*(.*?))*SINO:\\s*(.*?)\\s*FINSI", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(pseudocode);
        StringBuffer sb = new StringBuffer();
        
        while (matcher.find()) {
            String condition = matcher.group(1).trim();
            String thenBlock = matcher.group(2).trim();
            String elseBlock = matcher.group(6).trim();

            // Convertir el bloque THEN
            String replacement = "if (" + condition + ") {\n\t" + thenBlock + "\n}";

            // Convertir los bloques SINO
            String elseIfPatternStr = "SINO\\s*\\(([^\\)]+)\\)\\s*ENTONCES:\\s*(.*?)(?=SINO\\s*\\(|SINO:\\s|FINSI)";
            Pattern elseIfPattern = Pattern.compile(elseIfPatternStr, Pattern.DOTALL);
            Matcher elseIfMatcher = elseIfPattern.matcher(pseudocode.substring(matcher.start(), matcher.end()));
            
            while (elseIfMatcher.find()) {
                String elseifCondition = elseIfMatcher.group(1).trim();
                String elseifBlock = elseIfMatcher.group(2).trim();
                replacement += " else if (" + elseifCondition + ") {\n\t" + elseifBlock + "\n}";
            }
            
            if (!elseBlock.isEmpty()) {
                replacement += " else {\n\t" + elseBlock + "\n}";
            }

            // Reemplazar en el pseudocódigo
            matcher.appendReplacement(sb, replacement);
        }
        
        matcher.appendTail(sb);
        return sb.toString();
    }

    
    private static String convertIfElseNested(String pseudocode) {
        Pattern pattern = Pattern.compile(
            "SI\\s*\\(([^\\)]+)\\)\\s*ENTONCES:\\s*([^SINO])(SINO:\\s([^FINSI]*))?\\s*FINSI", 
            Pattern.DOTALL
        );

        Matcher matcher = pattern.matcher(pseudocode);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String condition = matcher.group(1).trim();
            String thenBlock = matcher.group(2).trim();
            String elseBlock = matcher.group(4) != null ? matcher.group(4).trim() : null;

            thenBlock = convertIfElseNested(thenBlock);
            if (elseBlock != null) {
                elseBlock = convertIfElseNested(elseBlock);
            }

            String replacement = "if (" + condition + ") {\n" + thenBlock + "\n}";
            if (elseBlock != null) {
                replacement += " else {\n" + elseBlock + "\n}";
            }

            matcher.appendReplacement(sb, replacement);
        }
        matcher.appendTail(sb);

        String result = sb.toString();
        result = result.replace("SI ", "if");
        result = result.replace("ENTONCES:", "{");
        result = result.replace("SINO:", "} else {");
        result = result.replace("FINSI", "}");

        return result;
    }


    
    public static String convertSwitchCase(String pseudocode) {
        // Dividir la entrada en líneas
        String[] lines = pseudocode.split("\n");
        
        // Pila para manejar el contexto de los bloques
        Stack<String> stack = new Stack<>();
        StringBuilder convertedCode = new StringBuilder();

        for (String line : lines) {
            line = line.trim();

            if (line.startsWith("SEGUN")) {
                // Convertir SEGUN a switch
                stack.push("switch");
                convertedCode.append("switch (").append(line.substring(6).replace(" HACER:", "")).append(") {\n");
            } else if (line.startsWith("CAS")) {
                // Convertir SI a case
                convertedCode.append("case ").append(line.substring(3).replace(":", "")).append(":\n");
                stack.push("case");
            } else if (line.startsWith("DEFECTO")) {
                // Convertir DEFECTO a default
                convertedCode.append("default:\n");
                stack.push("default");
            } else if (line.startsWith("FIN SEGUN")) {
            while (!stack.isEmpty() && !stack.peek().equals("switch")) {
                String top = stack.pop();
                if (top.equals("case") || top.equals("default")) {
                    convertedCode.append("break;\n");
                }
            }
            if (!stack.isEmpty()) {
                stack.pop();
            }
            convertedCode.append("}\n");
        } else if (line.startsWith("FINCA")) {
            String top = stack.pop();
            if (top.equals("case") || top.equals("default")) {
                convertedCode.append("break;\n");
            }
        } else {
            convertedCode.append(line).append("\n");
        }
    }
    return convertedCode.toString();
}

    
}
