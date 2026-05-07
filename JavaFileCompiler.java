import java.io.File;

import javax.tools.*;
import java.util.*;


public class JavaFileCompiler {
    
    public static void main(String[] args) {

        System.out.print("Enter the input .java file: ");
        Scanner scanner = new Scanner(System.in);
        String inputFileName = scanner.nextLine().trim();

        if (!inputFileName.toLowerCase().endsWith(".java")) {
            System.err.println("Invalid format");
            return;
        }
        
        String workingDirectory = System.getProperty("user.dir");
        File inputFile = new File(workingDirectory, inputFileName);

        if (!inputFile.exists()) {
            System.err.println("File not found in working directory");
            return;
        }

        boolean success = compile(inputFile.getAbsolutePath(), workingDirectory);
        System.exit(success ? 0 : 1);
    }

    public static boolean compile(String filePath, String workingDirectory) {

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
        List<String> options = Arrays.asList("-d", workingDirectory);
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
