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

public class CuerpoMain {
    private boolean flag;
    private boolean flagInicio = false;

    
    public boolean getFlag(){
        return this.flag;
    }
    
    /**
     *
     * @param flag
     */
    public void setFlag(boolean flag){
        this.flag = flag;
    }
    
    String inicio(String cadena){
        if(!this.flagInicio){
            VerificarFuncion v = new VerificarFuncion();
            boolean error = false;
            String[] cadena_split = v.dividirExpresion(cadena.trim().split("[ \\n]"));
            String result = "";
            String expresion = "INICIO(\\s|\\n)+(RETORNA\\s+((([a-zA-Z_][a-zA-Z0-9_]*|[0-9]+)\\s*(\\s*([+]|[-]|[*]|[/]|[%])\\s*([a-zA-Z_][a-zA-Z0-9_]*|[0-9]+))*)\\s*)(\\s|\\n)+)?FIN INICIO";
            String returnValue = "";
            
            if(Pattern.compile(expresion).matcher(cadena).find()){
                if (cadena_split[0].equals("INICIO")) {
                    cadena_split[0] = "int main(){";
                    for(int x = 0; x < cadena_split.length; x++){
                        error = v.checkEnd(cadena_split[x]);
                    }
                }
                if(!error){
                    List<String> nuevo = new ArrayList<>();
                    for (int i = 0; i < cadena_split.length; i++) {
                        if(cadena_split[i].equals("RETORNA")) {
                            cadena_split[i] = "\n\treturn";
                            int x = i;
                            while(!v.checkEnd(cadena_split[x])) {
                                nuevo.add(" " + cadena_split[x++]);
                            }
                            nuevo.add(";\n");
                            i = x - 1;
                            continue;
                        }
                        
                        if(v.checkEnd(cadena_split[i])) {
                            nuevo.add("}");
                            break;
                        }
                        nuevo.add(cadena_split[i]);
                    }
                    cadena_split = nuevo.toArray(new String[0]);
                    result = String.join("", cadena_split).trim();
                    this.flagInicio = true;
                    this.flag = true;
                    return result;
                }else{
                    return "Syntax Error";
                }
            }else{
                return "Syntax Error";
            }
        }
        return "Syntax Error";
    }
    
}
