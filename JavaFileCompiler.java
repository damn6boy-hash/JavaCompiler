import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.tools.*;
import java.util.*;


public class JavaFileCompiler {
    
    public static void main(String[] args) {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select the input file");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showOpenDialog(null);

        if (result != JFileChooser.APPROVE_OPTION) {
            JOptionPane.showMessageDialog(null, "Cancel");
            return;
        }

        File inputFile = fileChooser.getSelectedFile();

        boolean success = compile(inputFile.getAbsolutePath());
        System.exit(success ? 0 : 1);
    }

    public static boolean compile(String filePath) {

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
        String outputDirectory = System.getProperty("user.dir");
        List<String> options = Arrays.asList("-d", outputDirectory);
        File sourceFile = new File(filePath);
        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjects(sourceFile);
        boolean success = compiler.getTask(null, fileManager, diagnostics, options, null, compilationUnits).call();

        if (success) {
            System.out.println("Compilation successful.");
        } else {
            System.err.println("Compilation failed.");
            for ( Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics())
                System.out.format("Error on line %d in %s%n",
                         diagnostic.getLineNumber(),
                         diagnostic.getSource().toUri());
        }
        
        return success;
    }
}
