package net.jbock.convert;

import com.google.common.collect.ImmutableList;
import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import net.jbock.compiler.Processor;

import javax.tools.JavaFileObject;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.google.testing.compile.Compiler.javac;
import static net.jbock.convert.GenAutoTypes.PACKAGE;

public class GenMyCommandParser {

    private static final String FULLY_QUALIFIED_NAME = PACKAGE + ".DeleteCommand";
    static final String MY_ARGUMENTS_PARSER = "src/main/java/" +
            FULLY_QUALIFIED_NAME.replace('.', '/') + "_Parser.java";

    static void generate() throws IOException {

        JavaFileObject javaFileObject = JavaFileObjects.forSourceLines(FULLY_QUALIFIED_NAME,
                Files.readAllLines(Paths.get("src/main/java/" +
                        FULLY_QUALIFIED_NAME.replace('.', '/') + ".java")));

        Compilation compilation = javac().withProcessors(new Processor()).compile(javaFileObject);
        ImmutableList<JavaFileObject> results = compilation.generatedSourceFiles();

        try (InputStream in = results.get(0).openInputStream();
             OutputStream out = new FileOutputStream(Paths.get(MY_ARGUMENTS_PARSER).toFile())) {
            byte[] buffer = new byte[in.available()];
            in.read(buffer);
            out.write(buffer);
        }
    }

    public static void main(String[] args) throws IOException {
        generate();
    }
}
