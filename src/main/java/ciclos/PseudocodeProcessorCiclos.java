package ciclos;

import java.util.*;
import java.util.regex.*;

public class PseudocodeProcessorCiclos {

    private final Map<String, String> textPlaceholders = new HashMap<>();
    private int placeholderCount = 0;

    public String convertToCpp(String pseudocode) {
        pseudocode = protectQuotedText(pseudocode);

        String oldPseudocode;
        do {
            oldPseudocode = pseudocode;
            pseudocode = convertForLoops(pseudocode);
            pseudocode = convertWhileLoops(pseudocode);
            pseudocode = convertDoWhileLoops(pseudocode);
        } while (!pseudocode.equals(oldPseudocode));

        pseudocode = restoreQuotedText(pseudocode);

        return pseudocode;
    }

    private String protectQuotedText(String pseudocode) {
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
        return sb.toString();
    }

    private String restoreQuotedText(String pseudocode) {
        for (Map.Entry<String, String> entry : textPlaceholders.entrySet()) {
            pseudocode = pseudocode.replace(entry.getKey(), entry.getValue());
        }
        return pseudocode;
    }

    private String convertForLoops(String pseudocode) {
        Pattern pattern = Pattern.compile("(?sm)PARA\\s*([^HAS]+)HASTA\\s*([^EN]+)EN\\s*([^\\s]+)\\s*HACER:\\s*(.*?)\\s*FIN PARA");
        Matcher matcher = pattern.matcher(pseudocode);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String initialization = matcher.group(1).trim();
            String condition = matcher.group(2).trim();
            String update = matcher.group(3).trim();
            String body = modifyDoWhileBody(matcher.group(4).trim());
            matcher.appendReplacement(sb, "for (" + initialization + "; " + condition + "; " + update + ") {\n" + body + "\n}");
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private String convertWhileLoops(String pseudocode) {
        Pattern pattern = Pattern.compile("(?s)MIENTRAS\\s*([^HAC)]+)HACER:\\s*(.*?)\\s*FIN MIENTRAS");
        Matcher matcher = pattern.matcher(pseudocode);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String condition = matcher.group(1).trim();
            String body = modifyDoWhileBody(matcher.group(2).trim());
            matcher.appendReplacement(sb, "while (" + condition + ") {\n" + body + "\n}");
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private String convertDoWhileLoops(String pseudocode) {
        Pattern pattern = Pattern.compile("(?s)REPETIR:\\s*(.*?)\\s*MIENTRAS\\s*([^\\n]+)");
        Matcher matcher = pattern.matcher(pseudocode);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String body = modifyDoWhileBody(matcher.group(1).trim());
            String condition = matcher.group(2).trim();
            matcher.appendReplacement(sb, "do {\n" + body + "\n} while (" + condition + ");");
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private String modifyDoWhileBody(String body) {
        String[] lines = body.split("\\r?\\n");
        StringBuilder trimmedBody = new StringBuilder();
        for (String line : lines) {
            String trimmedLine = line.stripLeading();
            if (!trimmedLine.isEmpty()) {
                trimmedBody.append(trimmedLine).append("\n");
            }
        }
        if (trimmedBody.length() > 0) {
            trimmedBody.setLength(trimmedBody.length() - 1);
        }
        return trimmedBody.toString();
    }
}
