package net.jbock.coerce;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import net.jbock.CommandLineArguments;
import net.jbock.Parameter;

import javax.lang.model.element.Modifier;
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

public class GenAutoTypes {

    private static final Comparator<MethodData> COMP = Comparator
            .<MethodData>comparingInt(methodData -> methodData.type.isBoxedPrimitive() ? -1 : 1)
            .thenComparing(methodData -> methodData.simpleName);

    private static final String AUTO_TYPES_CLASSNAME = "JbockAutoTypes";

    private static final String PACKAGE = "com.example.hello";

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
        for (Map.Entry<Class<?>, CodeBlock> mapMirror : map) {
            Class<?> type = mapMirror.getKey();
            if (!isBoxedPrimitive(type)) {
                String simpleName = simpleName(TypeName.get(type));
                data.add(createMethodData(simpleName, type));
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
                "All non-private enums can also be used.\n" +
                "For each such type X, Optional<X> defines an optional parameter,\n" +
                "and List<X> defines a repeatable parameter.\n" +
                "boolean or Boolean defines a flag.\n");

        JavaFile javaFile = JavaFile.builder(PACKAGE, spec.build())
                .skipJavaLangImports(true)
                .build();

        javaFile.writeTo(Paths.get("src/main/java"));
    }

    private static boolean isBoxedPrimitive(Class<?> type) {
        return type == Integer.class ||
                type == Long.class ||
                type == Short.class ||
                type == Byte.class ||
                type == Float.class ||
                type == Double.class ||
                type == Boolean.class ||
                type == Character.class;
    }

    public static void main(String[] args) throws IllegalAccessException, NoSuchFieldException, IOException {

        Stream.of(AUTO_TYPES, AUTO_TYPES_PARSER)
                .map(Paths::get)
                .map(Path::toFile)
                .filter(File::exists)
                .forEach(File::delete);

        String version = net.jbock.compiler.Processor.class.getPackage().getImplementationVersion();
        generate(version);
        GenAutoTypesParser.generate();
    }

    private static MethodData createMethodData(String simpleName, Class<?> element) {
        return new MethodData(simpleName, TypeName.get(element));
    }

    static class MethodData {

        final String simpleName;

        final TypeName type;

        MethodData(String simpleName, TypeName type) {
            this.simpleName = simpleName;
            this.type = type;
        }
    }

    private static String simpleName(TypeName type) {
        String[] tokens = type.toString().split("[.]", -1);
        return tokens[tokens.length - 1];
    }

    private static MethodSpec createMethod(MethodData datum) {
        AnnotationSpec.Builder spec = AnnotationSpec.builder(Parameter.class)
                .addMember("longName", "$S", datum.type.toString());
        StringBuilder name = new StringBuilder();
        if (datum.type.isBoxedPrimitive()) {
            name.append("boxed");
        }
        name.append(datum.simpleName);
        return MethodSpec.methodBuilder(name.toString())
                .addModifiers(Modifier.ABSTRACT)
                .returns(datum.type)
                .addAnnotation(spec.build())
                .build();
    }
}
