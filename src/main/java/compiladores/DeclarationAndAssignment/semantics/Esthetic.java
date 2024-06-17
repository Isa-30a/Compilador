
package compiladores.DeclarationAndAssignment.semantics;

public class Esthetic {

    public String eliminarUltimoCaracter(String str) {
        if (str == null || str.isEmpty()) {
            return str; // Devolver cadena vacía o null si es necesario
        }
        return str.substring(0, str.length() - 1);
    }

    public String obtenerSubcadenaDespuesDeIgual(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        int index = str.indexOf("=");
        if (index == -1) {
            return str; // Devolver la cadena original si no contiene "="
        }
        return str.substring(index + 1).trim(); // Obtener subcadena después de "=" y eliminar espacios adicionales
    }
}
