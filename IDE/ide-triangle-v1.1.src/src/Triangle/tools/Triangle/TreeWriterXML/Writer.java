package Triangle.tools.Triangle.TreeWriterXML;
import Triangle.tools.Triangle.AbstractSyntaxTrees.Program;

import java.io.FileWriter;
import java.io.IOException;

/*
* La clase Writer permite crear el archivo xml donde se pueden observar los diferentes AST creados
* al compilar el programa. La clase recibe de parmetro en su constructor el nombre del archivo
* xml.
*/
public class Writer {

    private String fileName;

    public Writer(String fileName) {
        this.fileName = fileName;
    }

    // Draw the AST representing a complete program.
    /*
    * Entradas: el AST Program
    * Proceso: Dado que el AST inicia a partir de Program, este se pasa por par�metro con el fin
    * de recorrer los ASTs generados, e ir escribiendo el respectivo texto xml del nombre de
    * cada AST que se visita. Adem�s, se crea un archivo con el nombre de archivo indicado en el
    * constructor, al mismo se le escribe el respectivo encabezado de XML. Al terminar de escribir
    * el xml, se cierra el archivo.
    * Salidas: ninguna
    */
    public void write(Program ast) {
        // Prepare the file to write
        try {
            FileWriter fileWriter = new FileWriter(fileName);

            //HTML header
            fileWriter.write("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n");

            WriterVisitor layout = new WriterVisitor(fileWriter);
            ast.visit(layout, null);

            fileWriter.close();

        } catch (IOException e) {
            System.err.println("Error while creating file for print the AST");
            e.printStackTrace();
        }
    }

}