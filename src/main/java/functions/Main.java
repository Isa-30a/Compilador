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
        ReadPseudo reader = new ReadPseudo();
        String filePath = "../pseudoFile.txt";
        reader.readFile(filePath);
    }

}