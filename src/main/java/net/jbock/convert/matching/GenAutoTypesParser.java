package net.jbock.convert.matching;

import com.google.common.collect.ImmutableList;
import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import net.jbock.processor.JbockProcessor;

import javax.tools.JavaFileObject;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.google.testing.compile.Compiler.javac;
import static net.jbock.convert.matching.GenAutoTypes.AUTO_TYPES;
import static net.jbock.convert.matching.GenAutoTypes.AUTO_TYPES_CLASSNAME;
import static net.jbock.convert.matching.GenAutoTypes.AUTO_TYPES_PARSER;
import static net.jbock.convert.matching.GenAutoTypes.PACKAGE;

public class GenAutoTypesParser {

    static void generate() throws IOException {

        JavaFileObject javaFileObject = JavaFileObjects.forSourceLines(PACKAGE + "." + AUTO_TYPES_CLASSNAME,
                Files.readAllLines(Paths.get(AUTO_TYPES)));

        Compilation compilation = javac().withProcessors(new JbockProcessor()).compile(javaFileObject);
        ImmutableList<JavaFileObject> results = compilation.generatedSourceFiles();

        try (InputStream in = results.get(0).openInputStream();
             OutputStream out = new FileOutputStream(AUTO_TYPES_PARSER)) {
            byte[] buffer = new byte[in.available()];
            in.read(buffer);
            out.write(buffer);
        }
    }

    public static void main(String[] args) throws IOException {
        generate();
    }
}
