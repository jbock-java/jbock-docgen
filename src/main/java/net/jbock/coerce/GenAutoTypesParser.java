package net.jbock.coerce;

import com.google.common.collect.ImmutableList;
import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.tools.JavaFileObject;
import net.jbock.compiler.Processor;

import static com.google.testing.compile.Compiler.javac;
import static net.jbock.coerce.GenAutoTypes.GEN_CLASS_NAME;
import static net.jbock.coerce.GenAutoTypes.PACKAGE;

public class GenAutoTypesParser {

    static void generate() throws IOException {

        String sourceFile = "src/main/java/" +
                PACKAGE.replace('.', '/') +
                "/" + GEN_CLASS_NAME + ".java";
        String targetFile = "src/main/java/" +
                PACKAGE.replace('.', '/') +
                "/" + GEN_CLASS_NAME + "_Parser.java";

        JavaFileObject javaFileObject = JavaFileObjects.forSourceLines("com.example.helloworld.JbockAutoTypes", Files.readAllLines(Paths.get(sourceFile)));

        Compilation compilation = javac().withProcessors(new Processor()).compile(javaFileObject);
        ImmutableList<JavaFileObject> results = compilation.generatedSourceFiles();

        try (InputStream in = results.get(0).openInputStream();
             OutputStream out = new FileOutputStream(targetFile)) {
            byte[] buffer = new byte[in.available()];
            in.read(buffer);
            out.write(buffer);
        }
    }

    public static void main(String[] args) throws IOException {
        generate();
    }
}
