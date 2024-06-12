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
    }
}
