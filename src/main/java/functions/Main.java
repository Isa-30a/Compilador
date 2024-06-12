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
        String functionText = "FUNCION function (ENTERO hola): ENTERO\ncout<<\"Hello Planet\";\nfunction(a, b);\nRETORNA b\ncout<<\"Hello Earth\";\nFIN FUNCION";
        String mainText = "INICIO\ncout<<\"Hello World\";\nfunction(b, c);\n\n\nRETORNA 0\nFIN INICIO";

        System.out.println(df.declarar(functionText, cf.getFunctionsNames()));
        System.out.println(cf.cuerpo(mainText));
        System.out.println(cf.cuerpo(functionText));
    }

}