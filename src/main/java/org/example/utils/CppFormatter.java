package org.example.utils;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

public class CppFormatter {

    public static String formatCode(String code) {
        StringBuilder formattedCode = new StringBuilder();
        String[] lines = code.split("\n");
        int indentLevel = 0;
        String indent = "    "; // 4 spaces for each indent level

        for (String line : lines) {
            line = line.trim();
            if (line.endsWith("}")) {
                indentLevel--;
            }

            for (int i = 0; i < indentLevel; i++) {
                formattedCode.append(indent);
            }

            formattedCode.append(line).append("\n");

            if (line.endsWith("{")) {
                indentLevel++;
            }
        }


        return formattedCode.toString();
    }

}
