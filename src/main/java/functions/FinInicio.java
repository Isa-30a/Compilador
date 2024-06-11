/**
 * @authors 
 * Wilson Jimenez
 * Kevin Carmona
 * Yurleis Zuluaga
 * Greison Castilla
 * Andrés Quintana
 */
package functions;

import java.util.regex.Pattern;

public class FinInicio {
    
    String fin(String cadena, CuerpoMain main){
        if(main.getFlag()){
            VerificarFuncion v = new VerificarFuncion();
            String[] cadena_split = v.dividirExpresion(cadena.trim().split("\n"));
            String expresion = "(\\n*FIN\\sINICIO)";

            if(Pattern.compile(expresion).matcher(cadena).find()){
                if (verificar(cadena_split[0])) {
                    main.setFlag(false);
                    return "}";
                }
            }else{
                return "Syntax Error";
            }
            return "Syntax Error";
        }
        return "Syntax Error";
    }
    
    boolean verificar( String s){
        return Pattern.compile("\\n*FIN\\sINICIO").matcher(s).find();
    }
}
