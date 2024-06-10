package proyecto.automatas.condicionalesprueba;

import java.util.HashMap;
import java.util.Map;
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
            
            //pseudocode = convertIfElse(pseudocode);
            pseudocode = convertIfElseVarius(pseudocode);
            //pseudocode = convertIfElseNested(pseudocode);
        } while (!pseudocode.equals(oldPseudocode));

        pseudocode = restoreQuotedText(pseudocode);
        return pseudocode;
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

        Pattern pattern = Pattern.compile("(?sm)SI\\s*(\\([^\\)]+\\))\\s*ENTONCES\\s*(.*?)\\s*(SINO\\s*(.*?)\\s*)?FINSI");
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
        Pattern pattern = Pattern.compile("SI\\s*\\(([^\\)]+)\\)\\s*ENTONCES\\s*(.*?)\\s*(SINO\\s*\\(([^\\)]+)\\)\\s*ENTONCES\\s*(.*?))*SINO\\s*(.*?)\\s*FINSI", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(pseudocode);
        StringBuffer sb = new StringBuffer();
        
        while (matcher.find()) {
            String condition = matcher.group(1).trim();
            String thenBlock = matcher.group(2).trim();
            String elseBlock = matcher.group(6).trim();

            // Convertir el bloque THEN
            String replacement = "if (" + condition + ") {\n\t" + thenBlock + ";\n}";

            // Convertir los bloques SINO
            String elseIfPatternStr = "SINO\\s*\\(([^\\)]+)\\)\\s*ENTONCES\\s*(.*?)(?=SINO\\s*\\(|SINO\\s|FINSI)";
            Pattern elseIfPattern = Pattern.compile(elseIfPatternStr, Pattern.DOTALL);
            Matcher elseIfMatcher = elseIfPattern.matcher(pseudocode.substring(matcher.start(), matcher.end()));
            
            while (elseIfMatcher.find()) {
                String elseifCondition = elseIfMatcher.group(1).trim();
                String elseifBlock = elseIfMatcher.group(2).trim();
                replacement += " else if (" + elseifCondition + ") {\n\t" + elseifBlock + ";\n}";
            }
            
            if (!elseBlock.isEmpty()) {
                replacement += " else {\n\t" + elseBlock + ";\n}";
            }

            // Reemplazar en el pseudocódigo
            matcher.appendReplacement(sb, replacement);
        }
        
        matcher.appendTail(sb);
        return sb.toString();
    }

    
        private static String convertIfElseNested(String pseudocode) {
        // Define a pattern to match the nested structures of SI...ENTONCES...SINO...FIN DEL SI
        Pattern pattern = Pattern.compile("(?sm)SI\\s*(\\([^\\)]+\\))\\s*ENTONCES\\s*(.*?)\\s*(SINO\\s*(.*?)\\s*)?FINSI");
        Matcher matcher = pattern.matcher(pseudocode);
        StringBuffer sb = new StringBuffer();

        // Process all matches found by the matcher
        while (matcher.find()) {
            String condition = matcher.group(1).trim();
            String thenBlock = matcher.group(2).trim();
            String elseBlock = matcher.group(4) != null ? matcher.group(4).trim() : null;

            // Recursively convert nested pseudocode within the thenBlock and elseBlock
            thenBlock = convertIfElseNested(thenBlock);
            if (elseBlock != null) {
                elseBlock = convertIfElseNested(elseBlock);
            }

            // Form the replacement string
            String replacement = "if " + condition + " {\n" + thenBlock + ";\n}";
            if (elseBlock != null) {
                replacement += " else {\n" + elseBlock + ";\n}";
            }

            // Append the replacement to the result buffer
            matcher.appendReplacement(sb, replacement);
        }
        // Append the rest of the pseudocode that was not matched
        matcher.appendTail(sb);
        return sb.toString();
    }


    private static String convertSwitchCase(String pseudocode) {
        Pattern pattern = Pattern.compile("(?sm)SEGUN\\s*(\\w+)\\s*HACER\\s*(.*?)\\s*FIN SEGUN");
        Matcher matcher = pattern.matcher(pseudocode);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String variable = matcher.group(1).trim();
            String casesBlock = matcher.group(2).trim();

            String casePatternStr = "SI\\s*([^:]+):\\s*(.*?)(?=SI|DEFECTO|$)";
            Pattern casePattern = Pattern.compile(casePatternStr, Pattern.DOTALL);
            Matcher caseMatcher = casePattern.matcher(casesBlock);

            StringBuilder switchCode = new StringBuilder("switch(" + variable + ") {\n");

            while (caseMatcher.find()) {
                String caseValue = caseMatcher.group(1).trim();
                String caseBody = caseMatcher.group(2).trim().replaceAll("\\n", "\n    ");
                switchCode.append("    case ").append(caseValue).append(":\n        ").append(caseBody).append(";\n        break;\n");
            }

            Pattern defaultPattern = Pattern.compile("DEFECTO:\\s*(.*)", Pattern.DOTALL);
            Matcher defaultMatcher = defaultPattern.matcher(casesBlock);
            if (defaultMatcher.find()) {
                String defaultBody = defaultMatcher.group(1).trim().replaceAll("\\n", "\n    ");
                switchCode.append("    default:\n        ").append(defaultBody).append(";\n}");
            } else {
                switchCode.append("}");
            }

            matcher.appendReplacement(sb, switchCode.toString());
        }

        matcher.appendTail(sb);
        return sb.toString();
    }
}
