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
        FunctionsBody fb = new FunctionsBody();
        FunctionsDeclaration fd = new FunctionsDeclaration();
        List<String> cppCode = new ArrayList<>();
        Queue<String> declarationsQueue = new LinkedList<>();
        Queue<String> bodiesQueue = new LinkedList<>();
        Queue<String> errorQueue = new LinkedList<>();
        String mainFunction = "";
        String beginExpresion = "\\s*(FUNCION\\s*(\\w|\\W)*|INICIO)\\s*";
        String endExpresion = "\\s*FIN (FUNCION|INICIO)\\s*";
        boolean flag = false;
        int lineCounter = 0;

        String[] fullPseudo = pseudoCode.toString().trim().split("\\n", -1);
        int beginIndex = 0;
        int endIndex;
        for (int i = 0; i < fullPseudo.length; i++) {
            lineCounter++;
                declarationsQueue.offer(fd.declare(fullPseudo[i], fb.getFunctionsNames()));
            bodiesQueue.offer(fb.body(fullPseudo[i], flag));
            while(!fb.getErrorQueue().isEmpty()) {
                errorQueue.offer(fb.getErrorQueue().poll() + "\n");
            }
            if (Pattern.compile(beginExpresion).matcher(fullPseudo[i]).find() && !flag) {
                flag = true;
            } else if (Pattern.compile(endExpresion).matcher(fullPseudo[i]).find() && flag) {
                flag = false;
            }
        }
        while (!declarationsQueue.isEmpty()) {
            cppCode.add(declarationsQueue.poll());
        }
        cppCode.add(mainFunction);
        while (!bodiesQueue.isEmpty()) {
            cppCode.add(bodiesQueue.poll());
        }
        cppCode.add("\n");
        while (!errorQueue.isEmpty()) {
            cppCode.add(errorQueue.poll());
        }
        if(flag) {
            cppCode.add("Last function wasn't closed");
        }
        String cppString = String.join("", cppCode.toArray(new String[0])).trim();

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
