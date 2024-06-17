package compiladores.DeclarationAndAssignment.translatorDeclarationAssignment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConectToStringTxt {

    private PseudocodeTranslator translator;

    public ConectToStringTxt() {
        this.translator = new PseudocodeTranslator();
    }

    public String processString(String input) {
        StringBuilder output = new StringBuilder();
        String[] lines = input.split("\n");

        // Regular expressions for different cases
        String[] patterns = {
            // Declaration with assignment
            "\\b(ENTERO|REAL|CARACTER|CADENA|BOOLEANO|LARGO|DOBLE)\\b(?:\\[\\d*\\]\\[\\d*\\]|\\[\\d*\\]|\\[\\d*\\]\\[\\d*\\]|\\[\\d*\\])?(\\s+[a-zA-Z_][a-zA-Z0-9_]*)\\s*=\\s*[^;]+",
            // Assignment
            "\\b([a-zA-Z_][a-zA-Z0-9_]*)\\b\\s*=\\s*[^;]+",
            // Declarations
            "\\b(ENTERO|REAL|CARACTER|CADENA|BOOLEANO|LARGO|DOBLE)\\b(?:\\[\\d*\\]\\[\\d*\\]|\\[\\d*\\]|\\[\\d*\\]\\[\\d*\\]|\\[\\d*\\])?(\\s+[a-zA-Z_][a-zA-Z0-9_]*)\\b",};

        for (String line : lines) {
            // Trim the line to remove leading and trailing spaces
            line = line.trim();

            // Check if the line is empty or only contains pseudocode not to translate
            if (line.isEmpty() || line.startsWith("//")) {
                output.append(line).append("\n");
                continue;
            }

            // Debugging message to show the line before processing
            System.out.println("Processing line: " + line);

            // Check if the line contains "==", which we should skip
            if (line.contains("==")) {
                output.append(line).append("\n");
                continue;
            }

            boolean translated = false;
            StringBuilder processedLine = new StringBuilder(line);

            // Check if the line starts with "MIENTRAS" and contains a declaration with assignment
            if ((line.startsWith("MIENTRAS") || line.contains("while")) && containsDeclarationWithAssignment(line)) {
                // Find the declaration with assignment
                String matchedPart = findDeclarationWithAssignment(line);
                if (matchedPart != null) {
                    String formattedPart = formatForTranslation(matchedPart);

                    // Debugging message to show the part being translated and its translation
                    System.out.println("parte que se debe traducir: " + formattedPart);
                    String translatedText = translator.translate(formattedPart);
                    System.out.println("traduccion:|" + translatedText + "|\n");

                    // Remove double semicolons (;;)
                    translatedText = translatedText.replace(";", "");

                    // Replace the original matched part with the translated text
                    processedLine.replace(processedLine.indexOf(matchedPart), processedLine.indexOf(matchedPart) + matchedPart.length(), translatedText);
                    translated = true;
                }
            } else {
                // Handle other types of lines (PARA, etc.)
                // Iterate through patterns, but stop after the first successful translation
                for (String pattern : patterns) {
                    Pattern regex = Pattern.compile(pattern);
                    Matcher matcher = regex.matcher(processedLine);

                    while (matcher.find()) {
                        // Check if the match is within quotes
                        if (isWithinQuotes(processedLine, matcher.start())) {
                            continue;
                        }

                        String matchedPart = matcher.group();
                        String formattedPart = formatForTranslation(matchedPart);

                        // Debugging message to show the part being translated and its translation
                        System.out.println("parte que se debe traducir: " + formattedPart);
                        String translatedText = translator.translate(formattedPart);
                        System.out.println("traduccion:|" + translatedText + "|\n");

                        // Replace only the matched part in the processedLine
                        processedLine.replace(matcher.start(), matcher.end(), translatedText);
                        translated = true;

                        // Break out of inner while loop to only translate the first match
                        break;
                    }

                    // Break out of outer for loop if we have translated a part
                    if (translated) {
                        break;
                    }
                }
            }

            // Handle specific modifications for PARA lines
            if ((line.startsWith("PARA") || line.contains("for")) && translated) {
                // Add "; " before "HASTA" and remove trailing ";"
                if (line.contains("HASTA")) {
                    int hastaIndex = processedLine.indexOf("HASTA");
                    if (hastaIndex != -1) {
                        processedLine.insert(hastaIndex, "; ");
                    }
                } else {
                    String[] palabras = line.split(" ");
                    for (int i = 0; i < palabras.length; i++) {
                        if (palabras[i].equals("<") || palabras[i].contains("<")) {
                            // Find the position of the first occurrence of '<'
                            int lessThanIndex = line.indexOf('<');
                            if (lessThanIndex != -1 && lessThanIndex >= 2) {
                                // Insert ';' two positions to the left of '<'
                                processedLine.insert(lessThanIndex - 2, "; ");
                            }
                            break;
                        }
                    }
                }

                // Remove trailing ";"
                if (processedLine.charAt(processedLine.length() - 1) == ';') {
                    processedLine.deleteCharAt(processedLine.length() - 1);
                }
            }

            // Append the modified line after translation, only if it's not empty
            if (processedLine.length() > 0) {
                output.append(processedLine.toString()).append("\n");
                // Debugging message to show the modified line
                System.out.println("linea traducida: " + processedLine.toString() + "\n");
            }
        }

        return output.toString();
    }

    private boolean containsDeclarationWithAssignment(String line) {
        // Check if the line contains a declaration with assignment
        String pattern = "\\b(ENTERO|REAL|CARACTER|CADENA|BOOLEANO|LARGO|DOBLE)\\b(?:\\[\\d*\\]\\[\\d*\\]|\\[\\d*\\]|\\[\\d*\\]\\[\\d*\\]|\\[\\d*\\])?(\\s+[a-zA-Z_][a-zA-Z0-9_]*)\\s*=\\s*[^;]+";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(line);
        return matcher.find();
    }

    private String findDeclarationWithAssignment(String line) {
        // Find and return the declaration with assignment from the line
        String pattern = "\\b(ENTERO|REAL|CARACTER|CADENA|BOOLEANO|LARGO|DOBLE)\\b(?:\\[\\d*\\]\\[\\d*\\]|\\[\\d*\\]|\\[\\d*\\]\\[\\d*\\]|\\[\\d*\\])?(\\s+[a-zA-Z_][a-zA-Z0-9_]*)\\s*=\\s*[^;]+";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(line);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    private String formatForTranslation(String code) {
        // Ensure that there are spaces around '=' and ','
        code = code.replaceAll("\\s*=\\s*", " = ");
        code = code.replaceAll("\\s*,\\s*", ", ");
        code = code.replaceAll("\\[\\s*", "[");
        code = code.replaceAll("\\s*\\]", "]");
        return code.trim();
    }

    private boolean isWithinQuotes(StringBuilder line, int index) {
        boolean insideQuotes = false;
        for (int i = 0; i < index; i++) {
            if (line.charAt(i) == '"') {
                insideQuotes = !insideQuotes;
            }
        }
        return insideQuotes;
    }
}
