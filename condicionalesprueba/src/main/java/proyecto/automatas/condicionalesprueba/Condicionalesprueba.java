package proyecto.automatas.condicionalesprueba;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
/**
 *
 * @author Usuario
 */
public class Condicionalesprueba {

    public static void main(String[] args) {
        final JFileChooser fc= new JFileChooser();
       int response = fc.showOpenDialog(null);
       if (response ==JFileChooser.APPROVE_OPTION){
        try{
        File file = new File(fc.getSelectedFile().getAbsolutePath());
        Scanner scanner = new Scanner(file);
        String contents="";
        while (scanner.hasNextLine()) {
            contents= contents + scanner.nextLine();
            contents+= "\n";
        }
        String convertedCode = IfElseConverter.convertToCpp(LogicalOperators.convertToCpp(contents));
        scanner.close();
        System.out.println(convertedCode );
        } catch(FileNotFoundException e){ 
            JOptionPane.showMessageDialog(null,"ERROR");
            e.printStackTrace();
        }
       }
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
        //String pseudocodevarius = "SI (num != 4) ENTONCES\nnum=8FINSI";
        
        
        
    }
}
