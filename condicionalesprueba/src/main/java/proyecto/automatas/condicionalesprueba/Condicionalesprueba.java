package proyecto.automatas.condicionalesprueba;

/**
 *
 * @author Usuario
 */
public class Condicionalesprueba {

    public static void main(String[] args) {
    /*
        String pseudocodevarius = "SI (edad == 2) ENTONCES\n" +
                            "numero = numero + 1\n" +
                            "SINO (edad >= 18 && edad < 65) ENTONCES\n" +
                            "numero = 4\n" +
                            "SINO (edad >= 65 < 80) ENTONCES\n" +
                            "numero = 5\n" +
                            "SINO (edad >= 80) ENTONCES\n" +
                            "numero = 5\n" +
                            "numero = 5\n" +
                            "SINO\n" +
                            "numero = 7\n" +
                            "FINSI";*/
    /*   
        String pseudocodevarius = "SI (x > 0) ENTONCES\n" +
                            "SI (y > 0) ENTONCES\n" +
                            "print('x and y are positive')\n" +
                            "SINO\n" +
                            "print('x is positive but y is not')\n" +
                            "FIN DEL SI\n" +
                            "SINO\n" +
                            "print('x is not positive')\n" +
                            "FINSI";
         */       
        String pseudocodevarius = "SI (num != 4) ENTONCES\nnum=8FINSI";
        
        
        String convertedCode = IfElseConverter.convertToCpp(pseudocodevarius);
        System.out.println(convertedCode );
    }
}
