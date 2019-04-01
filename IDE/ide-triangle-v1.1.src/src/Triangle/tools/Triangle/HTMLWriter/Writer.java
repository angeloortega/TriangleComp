package Triangle.tools.Triangle.HTMLWriter;
import Triangle.tools.Triangle.AbstractSyntaxTrees.Program;


import java.io.FileWriter;
import java.io.IOException;

public class Writer {

    private String fileName;

    public Writer(String fileName) {
        this.fileName = fileName.substring(0,fileName.length()-4)+".html";
    }

    // Draw the AST representing a complete program.
    public void write(String program) {
        // Prepare the file to write
        try {
            FileWriter fileWriter = new FileWriter(fileName);

            //HTML header
            fileWriter.write("<!DOCTYPE html>\n<html>\n<body style = font-family:Courier>\n");

            fileWriter.write(program);
            fileWriter.write("</body>\n</html>");
            fileWriter.close();

        } catch (IOException e) {
            System.err.println("Error while creating file for print the AST");
            e.printStackTrace();
        }
    }

}