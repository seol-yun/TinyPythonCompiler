import java.io.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.ParseTree;

public class Main {
    public static void main(String[] args) throws IOException {
        // Create a PrintStream to write to the file
        PrintStream fileStream = new PrintStream(new FileOutputStream("Test.j"));

        // Save the current System.out
        PrintStream originalOut = System.out;

        try {
            // Set System.out to the file stream
            System.setOut(fileStream);

            tinyPythonLexer lexer = new tinyPythonLexer(CharStreams.fromFileName("test.tpy"));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            tinyPythonParser parser = new tinyPythonParser(tokens);
            ParseTree tree = parser.program();

            ParseTreeWalker walker = new ParseTreeWalker();
            walker.walk(new tinyPythonPrintListener(), tree);
        } finally {
            // Restore the original System.out
            System.setOut(originalOut);

            // Close the file stream
            fileStream.close();
        }
    }
}