package net.jbock.convert.map;

import net.jbock.Command;
import net.jbock.Option;
import net.jbock.common.TypeTool;
import net.jbock.javapoet.AnnotationSpec;
import net.jbock.javapoet.CodeBlock;
import net.jbock.javapoet.JavaFile;
import net.jbock.javapoet.MethodSpec;
import net.jbock.javapoet.TypeName;
import net.jbock.javapoet.TypeSpec;

import javax.annotation.processing.Generated;
import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import static net.jbock.convert.map.GenMyCommandParser.MY_ARGUMENTS_PARSER;

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
            "/" + AUTO_TYPES_CLASSNAME + "Parser.java";

    private static void generate(String version) throws IllegalAccessException, IOException, NoSuchMethodException, InvocationTargetException, InstantiationException, NoSuchFieldException {
        Constructor<AutoMappings> constructor = AutoMappings.class.getDeclaredConstructor(TypeTool.class);
        constructor.setAccessible(true);
        AutoMappings autoConverters = constructor.newInstance(new Object[]{new TypeTool(null, null)});
        Method mappers = AutoMappings.class.getDeclaredMethod("autoConversions");
        mappers.setAccessible(true);
        List<AutoConversion> map = (List<AutoConversion>) mappers.invoke(autoConverters);
        TypeSpec.Builder spec = TypeSpec.classBuilder(AUTO_TYPES_CLASSNAME);
        spec.addAnnotation(AnnotationSpec.builder(Generated.class)
                .addMember("value", CodeBlock.of("$S", GenAutoTypes.class.getCanonicalName()))
                .build());
        List<MethodData> data = new ArrayList<>(map.size());
        for (AutoConversion entry : map) {
            String type = entry.qualifiedName();
            if (!isBoxedPrimitive(type)) {
                data.add(createMethodData(type, entry));
            }
        }
        data.sort(COMP);
        for (MethodData datum : data) {
            spec.addMethod(createMethod(datum));
        }
        spec.addModifiers(Modifier.ABSTRACT);
        spec.addAnnotation(Command.class);
        StringBuilder javadoc = new StringBuilder();
        List<String> specialTypes = List.of(
                "boolean",
                "java.util.List",
                "java.util.Optional",
                "java.util.OptionalInt",
                "java.util.OptionalLong",
                "java.util.OptionalDouble",
                "io.vavr.control.Option");
        javadoc.append("This class contains all \"auto types\"\n");
        javadoc.append("that can be used without a custom converter in jbock ");
        javadoc.append(version);
        javadoc.append(":\n\n");
        javadoc.append("<ul>\n");
        for (MethodData datum : data) {
            javadoc.append("  <li>{@code ");
            javadoc.append(datum.type.getCanonicalName());
            javadoc.append("}\n");
        }
        javadoc.append("</ul>\n");
        javadoc.append("\n");
        javadoc.append("Primitives and boxed primitives are also auto types, except the booleans.\n" +
                "All enums are auto types. They are converted via their static {@code valueOf} method.\n" +
                "Special rules apply for these types:\n\n");
        javadoc.append("<ul>\n");
        for (String specialType : specialTypes) {
            javadoc.append("  <li>{@code ");
            javadoc.append(specialType);
            javadoc.append("}\n");
        }
        javadoc.append("</ul>\n");
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

    public static void main(String[] args) throws IllegalAccessException, IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, NoSuchFieldException {

        Stream.of(AUTO_TYPES, AUTO_TYPES_PARSER, MY_ARGUMENTS_PARSER)
                .map(Paths::get)
                .map(Path::toFile)
                .filter(File::exists)
                .forEach(File::delete);

        String version = net.jbock.processor.JbockProcessor.class.getPackage().getImplementationVersion();
        generate(version);
        GenAutoTypesParser.generate();
        GenMyCommandParser.generate();
    }

    private static MethodData createMethodData(String type, AutoConversion autoConversion) throws NoSuchFieldException, IllegalAccessException {
        Field mapper = AutoConversion.class.getDeclaredField("mapper");
        mapper.setAccessible(true);
        CodeBlock mapExpr = (CodeBlock) mapper.get(autoConversion);
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
