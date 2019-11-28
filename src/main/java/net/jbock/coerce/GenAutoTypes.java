package net.jbock.coerce;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import javax.lang.model.element.Modifier;
import net.jbock.CommandLineArguments;
import net.jbock.Parameter;

import static net.jbock.coerce.GenMyArgumentsParser.MY_ARGUMENTS_PARSER;

public class GenAutoTypes {

    private static final Comparator<MethodData> COMP = Comparator
            .comparing(methodData -> methodData.type.getSimpleName());

    static final String AUTO_TYPES_CLASSNAME = "JbockAutoTypes";

    static final String PACKAGE = "com.example.hello";

    static final String AUTO_TYPES = "src/main/java/" +
            PACKAGE.replace('.', '/') +
            "/" + AUTO_TYPES_CLASSNAME + ".java";

    static final String AUTO_TYPES_PARSER = "src/main/java/" +
            PACKAGE.replace('.', '/') +
            "/" + AUTO_TYPES_CLASSNAME + "_Parser.java";

    private static void generate(String version) throws NoSuchFieldException, IllegalAccessException, IOException {
        Field mappers = AutoMapper.class.getDeclaredField("MAPPERS");
        mappers.setAccessible(true);
        List<Map.Entry<Class<?>, CodeBlock>> map = (List<Map.Entry<Class<?>, CodeBlock>>) mappers.get(null);
        TypeSpec.Builder spec = TypeSpec.classBuilder(AUTO_TYPES_CLASSNAME);
        List<MethodData> data = new ArrayList<>(map.size());
        for (Map.Entry<Class<?>, CodeBlock> entry : map) {
            Class<?> type = entry.getKey();
            if (!isBoxedPrimitive(type)) {
                data.add(createMethodData(type, entry.getValue()));
            }
        }
        data.sort(COMP);
        for (MethodData datum : data) {
            spec.addMethod(createMethod(datum));
        }
        spec.addModifiers(Modifier.ABSTRACT);
        spec.addAnnotation(CommandLineArguments.class);
        spec.addJavadoc("This class contains all the basic parameter types\n" +
                "that can be used without custom mappers or collectors in jbock " + version + ".\n" +
                "Primitives and boxed primitives are omitted here.\n" +
                "All enums can also be used; they are mapped by their {@code valueOf} method.\n");

        JavaFile javaFile = JavaFile.builder(PACKAGE, spec.build())
                .skipJavaLangImports(true)
                .build();

        javaFile.writeTo(Paths.get("src/main/java"));
    }

    private static boolean isBoxedPrimitive(Class<?> type) {
        return TypeName.get(type).isBoxedPrimitive();
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
        GenMyArgumentsParser.generate();
    }

    private static MethodData createMethodData(Class<?> type, CodeBlock mapExpr) {
        return new MethodData(type, mapExpr);
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
        String name = Character.toLowerCase(data.type.getSimpleName().charAt(0)) + data.type.getSimpleName().substring(1);
        return MethodSpec.methodBuilder(name)
                .addJavadoc("Mapped by: " + mapExprString(data) + "\n")
                .addModifiers(Modifier.ABSTRACT)
                .returns(data.type)
                .addAnnotation(AnnotationSpec.builder(Parameter.class)
                        .addMember("value", "$S", data.type.getSimpleName()).build())
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
