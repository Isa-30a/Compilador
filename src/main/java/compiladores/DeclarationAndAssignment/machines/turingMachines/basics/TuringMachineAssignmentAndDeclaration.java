
package compiladores.DeclarationAndAssignment.machines.turingMachines.basics;

import compiladores.DeclarationAndAssignment.machines.afds.*;
import compiladores.DeclarationAndAssignment.semantics.Esthetic;

public class TuringMachineAssignmentAndDeclaration {

    Afd_Assignment afdAssignment = new Afd_Assignment();
    Afd_Declaration afdDeclaration = new Afd_Declaration();

    TuringMachineAssignment turingAssignment = new TuringMachineAssignment();
    TuringMachineDeclaration turingDeclaration = new TuringMachineDeclaration();
    Esthetic estetica = new Esthetic();

    public String translateAssignmentDeclaration(String toCompile) {

        String particion = respectiveDivisions(toCompile);

        String[] texto = particion.split("#");
        String declarationPart = texto[0];
        String asignacioncompleta = texto[1];
        String retorno = "no tradujo declaracion y asignacion sencilla";

        if (afdDeclaration.isValidDeclaration(declarationPart) && afdAssignment.isValidAssignment(asignacioncompleta)) {

            String asigcompletaTraducida = turingAssignment.translateAssignment(asignacioncompleta);

            String despuesDELigual = estetica.obtenerSubcadenaDespuesDeIgual(asigcompletaTraducida);

            String declaracion = turingDeclaration.translateDeclaration(declarationPart);
            String sinpuntoycoma = estetica.eliminarUltimoCaracter(declaracion);

            retorno = sinpuntoycoma + " = " + despuesDELigual;

            return retorno;
        }

        return retorno;
    }

    private String respectiveDivisions(String toCompile) {

        String[] tokens = toCompile.trim().split("\\s*=\\s*", 2);

        String declarationPart = tokens[0].trim();
        String[] Dato_nombre = declarationPart.split(" ");
        String nombre = Dato_nombre[1];

        String assignmentPart = tokens[1].trim();
        String asignacioncompleta = nombre + " = " + assignmentPart;

        return declarationPart + "#" + asignacioncompleta;
    }

}
