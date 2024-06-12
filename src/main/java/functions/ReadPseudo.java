/**
 * @authors
 * Wilson Jimenez
 * Kevin Carmona
 * Yurleis Zuluaga
 * Greison Castilla
 * Andrés Quintana
 */
package functions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ReadPseudo {
    public String readFile(String filePath) {
        StringBuilder pseudoCode = new StringBuilder();
        FunctionsUtils v = new FunctionsUtils();
        FunctionsBody fb = new FunctionsBody();
        FunctionsDeclaration fd = new FunctionsDeclaration();
        List<String> cppCode = new ArrayList<>();
        String beginExpresion = "\\s*(FUNCION\\s*(\\w|\\W)*|INICIO)\\s*";
        String endExpresion = "\\s*FIN (FUNCION|INICIO)\\s*";
        boolean flag = false;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                pseudoCode.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] fullPseudo = v.splitExpresion(pseudoCode.toString().trim().split("\\n"));
        String pseudoBlock;
        int beginIndex = -1;
        int endIndex = -1;
        for(int i = 0; i < fullPseudo.length; i++) {
            System.out.println(fullPseudo[i]);
            if(Pattern.compile(beginExpresion).matcher(fullPseudo[i]).find() && !flag) {
                beginIndex = i;
                flag = true;
            }
            
            if(Pattern.compile(endExpresion).matcher(fullPseudo[i]).find() && flag) {
                endIndex = i;
                flag = false;
                //System.out.println("Code Block\n" + fd.declare(generatePseudoBlock(fullPseudo, beginIndex, endIndex), fb.getFunctionsNames()));
                //System.out.println("\n" + fb.body(generatePseudoBlock(fullPseudo, beginIndex, endIndex)));
                //fb.cuerpo(generatePseudoBlock(fullPseudo, beginIndex, endIndex));
            }
        }
        
        return pseudoCode.toString();
    }
    
    private String generatePseudoBlock(String[] fullPseudo, int beginIndex, int endIndex) {
        List<String> pseudoLines = new ArrayList<>();
        for(int i = beginIndex; i <= endIndex; i++) {
            pseudoLines.add(fullPseudo[i]);
        }
        return String.join("\n", pseudoLines.toArray(new String[0])).trim();
    }
    
    
}
