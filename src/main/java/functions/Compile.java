/**
 * @authors
 * Wilson Jimenez
 * Kevin Carmona
 * Yurleis Zuluaga
 * Greison Castilla
 * Andrés Quintana
 */
package functions;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.Queue;
import java.util.LinkedList;

public class Compile {
    public String pseudoToCpp(String pseudoCode) {
        FunctionsUtils v = new FunctionsUtils();
        FunctionsBody fb = new FunctionsBody();
        FunctionsDeclaration fd = new FunctionsDeclaration();
        List<String> cppCode = new ArrayList<>();
        Queue<String> declarationsQueue = new LinkedList<>();
        Queue<String> bodiesQueue = new LinkedList<>();
        String mainFunction = "";
        String beginExpresion = "\\s*(FUNCION\\s*(\\w|\\W)*|INICIO)\\s*";
        String endExpresion = "\\s*FIN (FUNCION|INICIO)\\s*";
        boolean flag = false;

        String[] fullPseudo = v.splitExpresion(pseudoCode.toString().trim().split("\\n"));
        int beginIndex = -1;
        int endIndex = -1;
        for (int i = 0; i < fullPseudo.length; i++) {
            if (Pattern.compile(beginExpresion).matcher(fullPseudo[i]).find() && !flag) {
                beginIndex = i;
                flag = true;
            } else if (Pattern.compile(endExpresion).matcher(fullPseudo[i]).find() && flag) {
                endIndex = i;
                flag = false;
                if (Pattern.compile("\\s*(FUNCION\\s*(\\w|\\W)*)\\s*").matcher(fullPseudo[beginIndex]).find()) {
                    declarationsQueue.offer(fd.declare(generatePseudoBlock(fullPseudo, beginIndex, endIndex), fb.getFunctionsNames()));
                    bodiesQueue.offer(fb.body(generatePseudoBlock(fullPseudo, beginIndex, endIndex)));
                } else {
                    if (!fb.isMain()) {
                        mainFunction = fb.body(generatePseudoBlock(fullPseudo, beginIndex, endIndex));
                    } else {
                        bodiesQueue.offer("Syntax Error");
                    }

                }
            } else if (Pattern.compile(endExpresion).matcher(fullPseudo[i]).find()
                    && !Pattern.compile(beginExpresion).matcher(fullPseudo[i]).find() && !flag) {
                cppCode.add("Syntax Error");
            } else if(!flag) {
                cppCode.add(fullPseudo[i]);
            }
        }
        while (!declarationsQueue.isEmpty()) {
            cppCode.add(declarationsQueue.poll());
        }
        cppCode.add(mainFunction);
        while (!bodiesQueue.isEmpty()) {
            cppCode.add(bodiesQueue.poll());
        }
        String cppString = String.join("\n", cppCode.toArray(new String[0])).trim();

        return cppString;
    }

    private String generatePseudoBlock(String[] fullPseudo, int beginIndex, int endIndex) {
        List<String> pseudoLines = new ArrayList<>();
        for (int i = beginIndex; i <= endIndex; i++) {
            pseudoLines.add(fullPseudo[i]);
        }
        return String.join("\n", pseudoLines.toArray(new String[0])).trim();
    }
}
