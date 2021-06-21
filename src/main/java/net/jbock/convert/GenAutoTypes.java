package net.jbock.convert;

import net.jbock.Command;
import net.jbock.Option;
import net.jbock.common.SafeElements;
import net.jbock.common.TypeTool;
import net.jbock.convert.matching.AutoConverters;
import net.jbock.convert.matching.MapExpr;
import net.jbock.javapoet.AnnotationSpec;
import net.jbock.javapoet.CodeBlock;
import net.jbock.javapoet.JavaFile;
import net.jbock.javapoet.MethodSpec;
import net.jbock.javapoet.TypeName;
import net.jbock.javapoet.TypeSpec;
import org.mockito.Mockito;

import javax.annotation.processing.Generated;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
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
            "/" + AUTO_TYPES_CLASSNAME + "Parser.java";

    private static void generate(String version) throws IllegalAccessException, IOException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        Constructor<AutoConverters> constructor = AutoConverters.class.getDeclaredConstructor(TypeTool.class, SafeElements.class);
        constructor.setAccessible(true);
        AutoConverters autoConverters = constructor.newInstance(new Object[]{null, mockElements()});
        Method mappers = AutoConverters.class.getDeclaredMethod("autoConverters");
        mappers.setAccessible(true);
        List<Map.Entry<String, MapExpr>> map = (List<Map.Entry<String, MapExpr>>) mappers.invoke(autoConverters);
        TypeSpec.Builder spec = TypeSpec.classBuilder(AUTO_TYPES_CLASSNAME);
        spec.addAnnotation(AnnotationSpec.builder(Generated.class)
                .addMember("value", CodeBlock.of("$S", GenAutoTypes.class.getCanonicalName()))
                .build());
        List<MethodData> data = new ArrayList<>(map.size());
        for (Map.Entry<String, MapExpr> entry : map) {
            String type = entry.getKey();
            if (!isBoxedPrimitive(type)) {
                data.add(createMethodData(type, entry.getValue().code()));
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

    private static SafeElements mockElements() {
        SafeElements mock = Mockito.mock(SafeElements.class);
        TypeElement mockTypeElement = Mockito.mock(TypeElement.class);
        Mockito.when(mock.getTypeElement(Mockito.anyString()))
                .thenReturn(Optional.of(mockTypeElement));
        return mock;
    }

    private static boolean isBoxedPrimitive(String type) {
        try {
            return TypeName.get(Class.forName(type)).isBoxedPrimitive();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IllegalAccessException, IOException, InvocationTargetException, NoSuchMethodException, InstantiationException {

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
