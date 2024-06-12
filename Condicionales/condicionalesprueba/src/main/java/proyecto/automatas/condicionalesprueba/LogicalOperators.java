package proyecto.automatas.condicionalesprueba;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogicalOperators {

     private static int placeholderCount = 0;
    private static Map<String, String> textPlaceholders = new HashMap<>();

    public static String convertToCpp(String pseudocode) {
        pseudocode = protectQuotedText(pseudocode);

        String oldPseudocode;
        do {
            oldPseudocode = pseudocode;
            pseudocode = convertAND(pseudocode);
            pseudocode = convertOR(pseudocode);
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

    private static String convertAND(String p){
        // Defines a pattern to match all Y that have at least one space before and after the Y   
        Pattern pattern = Pattern.compile("\s+Y\s+");
        Matcher matcher = pattern.matcher(p);
        StringBuffer sb= new StringBuffer();

        //Processes all matches found by the matcher
        while(matcher.find()){   
            //Appends the replacement to the result buffer
            matcher.appendReplacement(sb, " && ");
        }
        //Append the rest of the pseudocode that was not matched
        matcher.appendTail(sb);
        return sb.toString();
    }
    
    private static String convertOR(String p){
        // Defines a pattern to match all "O"s that have at least one space before and after the O  
        Pattern pattern = Pattern.compile("\s+O\s+");
        Matcher matcher = pattern.matcher(p);
        StringBuffer sb= new StringBuffer();

        //Processes all matches found by the matcher
        while (matcher.find()) {
            matcher.appendReplacement(sb, " || ");
        }
        //Append the rest of the pseudocode that was not matched
        matcher.appendTail(sb);
        return sb.toString();
    }
}