/**
 * @authors
 * Wilson Jimenez
 * Kevin Carmona
 * Yurleis Zuluaga
 * Greison Castilla
 * Andrés Quintana
 */
package functions;

public class Main {

    public static void main(String[] args) {
        DeclararFuncion df = new DeclararFuncion();
        CuerpoFuncion cf = new CuerpoFuncion();
        FinFuncion ff = new FinFuncion();
        CuerpoMain cm = new CuerpoMain();
        FinInicio fi = new FinInicio();
        String functionText = "FUNCION FLOTANTE function(ENTERO pointer)\nRETORNA a\nRETORNA b\nFIN FUNCION";
        String mainText = "INICIO\n\n\n\n\nRETORNA 1 + 2 + b\nFIN INICIO";

        System.out.println(df.declarar(functionText));
//        System.out.println(cm.inicio(mainText));
//        System.out.println(fi.fin("\nFIN INICIO", fm));
        System.out.println(cf.cuerpo(functionText));
    }

}
