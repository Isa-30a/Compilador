/**
 * @authors
 * Wilson Jimenez
 * Kevin Carmona
 * Yurleis Zuluaga
 * Greison Castilla
 * Andrés Quintana
 */
package functions;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        ReadPseudo reader = new ReadPseudo();
        File file = new File("../pseudoFile.txt");
        reader.readFile(file);
    }

}