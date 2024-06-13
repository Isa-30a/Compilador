package org.example.utils;

import java.awt.*;
import java.io.*;

public class FontLoader{ //CHATGPT GOD
    
    public static void loadFont(String fontFileName) {
        try {
            System.out.println(" Load Font resource: " + fontFileName);
            // Cargar la fuente desde el archivo de recursos
            File file = new File("resources/font/" + fontFileName);
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, file);

            System.out.print(" Sucess: ");
            System.out.println(customFont.getName());

            // Registrar la fuente personalizada en el GraphicsEnvironment
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);

        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            
        }
    }
}

