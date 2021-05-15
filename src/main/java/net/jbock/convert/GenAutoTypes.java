package net.jbock.convert;

import net.jbock.Command;
import net.jbock.Option;
import net.jbock.javapoet.AnnotationSpec;
import net.jbock.javapoet.CodeBlock;
import net.jbock.javapoet.JavaFile;
import net.jbock.javapoet.MethodSpec;
import net.jbock.javapoet.TypeName;
import net.jbock.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

import static net.jbock.convert.GenMyCommandParser.MY_ARGUMENTS_PARSER;

public class GenAutoTypes {

    private static final Comparator<MethodData> COMP = Comparator
            .comparing(methodData -> methodData.type.getCanonicalName());

    static final String AUTO_TYPES_CLASSNAME = "JbockAutoTypes";

    static final String PACKAGE = "com.example.hello";

    static final String AUTO_TYPES = "src/main/java/" +
            PACKAGE.replace('.', '/') +
            "/" + AUTO_TYPES_CLASSNAME + ".java";

    static final String AUTO_TYPES_PARSER = "src/main/java/" +
            PACKAGE.replace('.', '/') +
            "/" + AUTO_TYPES_CLASSNAME + "_Parser.java";

    private static void generate(String version) throws NoSuchFieldException, IllegalAccessException, IOException {
        Field mappers = AutoConverter.class.getDeclaredField("CONVERTERS");
        mappers.setAccessible(true);
        List<Map.Entry<String, CodeBlock>> map = (List<Map.Entry<String, CodeBlock>>) mappers.get(null);
        TypeSpec.Builder spec = TypeSpec.classBuilder(AUTO_TYPES_CLASSNAME);
        List<MethodData> data = new ArrayList<>(map.size());
        for (Map.Entry<String, CodeBlock> entry : map) {
            String type = entry.getKey();
            if (!isBoxedPrimitive(type)) {
                data.add(createMethodData(type, entry.getValue()));
            }
        }
        data.sort(COMP);
        for (MethodData datum : data) {
            spec.addMethod(createMethod(datum));
        }
        spec.addModifiers(Modifier.ABSTRACT);
        spec.addAnnotation(Command.class);
        StringBuilder javadoc = new StringBuilder();
        javadoc.append("<p>This class contains all \"auto types\"\n");
        javadoc.append("that can be used without a custom converter in jbock ");
        javadoc.append(version);
        javadoc.append(":</p>\n");
        javadoc.append("\n");
        javadoc.append("<ul>\n");
        for (MethodData datum : data) {
            javadoc.append("  <li>");
            javadoc.append(datum.type.getCanonicalName());
            javadoc.append("</li>\n");
        }
        javadoc.append("</ul>\n");
        javadoc.append("\n");
        javadoc.append("<p>Primitives and boxed primitives are also auto types, except the booleans.\n" +
                "All enums are auto types. They are converted via their static {@code valueOf} method.\n" +
                "Special rules apply for boolean, java.util.List and java.util.Optional.</p>");
        spec.addJavadoc(javadoc.toString());

        JavaFile javaFile = JavaFile.builder(PACKAGE, spec.build())
                .skipJavaLangImports(true)
                .build();

        javaFile.writeTo(Paths.get("src/main/java"));
    }

    private static boolean isBoxedPrimitive(String type) {
        try {
            return TypeName.get(Class.forName(type)).isBoxedPrimitive();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IllegalAccessException, NoSuchFieldException, IOException {

        Stream.of(AUTO_TYPES, AUTO_TYPES_PARSER, MY_ARGUMENTS_PARSER)
                .map(Paths::get)
                .map(Path::toFile)
                .filter(File::exists)
                .forEach(File::delete);

        String version = net.jbock.compiler.Processor.class.getPackage().getImplementationVersion();
        generate(version);
        GenAutoTypesParser.generate();
        GenMyCommandParser.generate();
    }

    private static MethodData createMethodData(String type, CodeBlock mapExpr) {
        try {
            return new MethodData(Class.forName(type), mapExpr);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static class MethodData {

        final Class<?> type;
        final CodeBlock mapExpr;

        MethodData(Class<?> type, CodeBlock mapExpr) {
            this.type = type;
            this.mapExpr = mapExpr;
        }
    }


    /**
     * Mapped by: <pre>{@code s -> {
     *   java.io.File f = new java.io.File(s);
     *   if (!f.exists()) {
     *     throw new java.lang.IllegalStateException("File does not exist: " + s);
     *   }
     *   if (!f.isFile()) {
     *     throw new java.lang.IllegalStateException("Not a file: " + s);
     *   }
     *   return f;
     * }}</pre>
     */
    private static MethodSpec createMethod(MethodData data) {
        String name = Character.toLowerCase(data.type.getSimpleName().charAt(0)) +
                data.type.getSimpleName().substring(1);
        return MethodSpec.methodBuilder(name)
                .addJavadoc("converter: " + mapExprString(data) + "\n")
                .addModifiers(Modifier.ABSTRACT)
                .returns(data.type)
                .addAnnotation(AnnotationSpec.builder(Option.class)
                        .addMember("names", "$S", "--" + data.type.getSimpleName()
                                .toLowerCase(Locale.US)).build())
                .build();
    }

    private static String mapExprString(MethodData data) {
        String result = data.mapExpr.toString();
        if (result.contains("->")) {
            return "<pre>{@code " + result + "}</pre>";
        }
        return result;
    }
}
