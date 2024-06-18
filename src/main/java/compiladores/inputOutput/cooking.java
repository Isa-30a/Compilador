package compiladores.inputOutput;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class cooking {


    public String inputOutputTraductor(String pseudocodigo)
    {
        StringBuilder resultado = new StringBuilder();
        String expresion = "ESCRIBIR\\s+(\"[^\"]+\"|\\w+)(\\s*\\+\\s*(\"[^\"]+\"|\\w+))*|LEER(LINEA\\s+\\w+|\\s+\\w+)";
        Pattern patron = Pattern.compile(expresion);

        Matcher matcher = patron.matcher(pseudocodigo);
        int ultimaPosicion = 0;
        while (matcher.find()) {
            String instruccionEscribir = matcher.group();
            String codigoEscribir = " ";
            if (instruccionEscribir.startsWith("ESCRIBIR")) {
                codigoEscribir = cambiarEscribir(instruccionEscribir);
            } else if (instruccionEscribir.startsWith("LEERLINEA")) {
                codigoEscribir = cambiarEntradaStrings(instruccionEscribir);
            } else if (instruccionEscribir.startsWith("LEER")) {
                codigoEscribir = cambiarEntradaEstandar(instruccionEscribir);
            }

            resultado.append(pseudocodigo, ultimaPosicion, matcher.start());
            resultado.append(codigoEscribir);
            ultimaPosicion = matcher.end();
        }

        resultado.append(pseudocodigo.substring(ultimaPosicion));
        return resultado.toString();
    }

    public String cambiarEscribir(String instruccionEscribir) {
        String[] partes = instruccionEscribir.split("\\+");
        StringBuilder codigoEscribir = new StringBuilder();
        String primeraLinea = partes[0].replaceFirst("^ESCRIBIR\\s+", "");
        codigoEscribir.append("cout<<").append(primeraLinea);
        for(int i=1; i<partes.length;i++)
        {
            codigoEscribir.append("<<").append(partes[i]);
            System.out.println(partes[i]);
        }
        codigoEscribir.append(";\n");
        return codigoEscribir.toString();
    }

    public String cambiarEntradaEstandar(String instruccionEscribir) {
        String[] partes = instruccionEscribir.split("\\s+");
        StringBuilder codigoEscribir = new StringBuilder();
        codigoEscribir.append("cin>>").append(partes[1]).append(";\n");
        return codigoEscribir.toString();
    }

    public String cambiarEntradaStrings(String instruccionEscribir) {
        String[] partes = instruccionEscribir.split("\\s+");
        StringBuilder codigoEscribir = new StringBuilder();
        codigoEscribir.append("getline(cin, ").append(partes[1]).append(");\nfflush(stdin);\n");
        return codigoEscribir.toString();
    }
}
