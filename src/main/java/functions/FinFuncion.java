/**
 * @authors 
 * Wilson Jimenez
 * Kevin Carmona
 * Yurleis Zuluaga
 * Greison Castilla
 * Andrés Quintana
 */

//USELESS CLASS

package functions;

import java.util.Arrays;
import java.util.regex.Pattern;

public class FinFuncion {
    String finFun(String cadena, boolean iniciofun){
        VerificarFuncion v = new VerificarFuncion();
        String[] cadena_split = v.dividirExpresion(cadena.trim().split("\n"));
        System.out.println(Arrays.toString(cadena_split)) ;
        String expresion = "(\\n*FIN\\sFUNCION)";
        
        if(Pattern.compile(expresion).matcher(cadena).find()){
            if(!iniciofun){
                return "Syntax Error";
            }
            if (verificar(cadena_split[0])) {
                return "}";
            }
        }else{
            return "Syntax Error";
        }
        return "Syntax Error";
    }
    
    boolean verificar( String s){
        return Pattern.compile("\\n*FIN\\sFUNCION").matcher(s).find();
    }
    
}
