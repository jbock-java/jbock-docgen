package net.jbock.coerce;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import javax.lang.model.element.Modifier;
import net.jbock.CommandLineArguments;
import net.jbock.Parameter;

public class GenAutoTypes {

    private static final Comparator<MethodData> COMP = Comparator
            .<MethodData>comparingInt(methodData -> methodData.type.isBoxedPrimitive() ? -1 : 1)
            .thenComparing(methodData -> methodData.name);

    static final String GEN_CLASS_NAME = "JbockAutoTypes";

    private static void notMain(String version) throws NoSuchFieldException, IllegalAccessException, IOException {
        Field mappers = AutoMapper.class.getDeclaredField("MAPPERS");
        mappers.setAccessible(true);
        List<Map.Entry<Class<?>, CodeBlock>> map = (List<Map.Entry<Class<?>, CodeBlock>>) mappers.get(null);
        TypeSpec.Builder spec = TypeSpec.classBuilder(GEN_CLASS_NAME);
        List<MethodData> data = new ArrayList<>(map.size());
        for (Map.Entry<Class<?>, CodeBlock> mapMirror : map) {
            Class<?> mapperReturnType = mapMirror.getKey();
            String name = baseName(TypeName.get(mapperReturnType));
            data.add(createMethodData(name, mapperReturnType));
        }
        data.sort(COMP);
        for (MethodData datum : data) {
            spec.addMethod(createMethod(datum));
        }
        spec.addModifiers(Modifier.ABSTRACT);
        spec.addAnnotation(CommandLineArguments.class);
        spec.addJavadoc("This class contains all the basic parameter types\n" +
                "that can be used without a custom mapper in jbock " + version + ".\n" +
                "Optional and List thereof can also be used.\n" +
                "All non-private enums can also be used directly.\n" +
                "The default mapper will use their {@code static valueOf(String)} method.\n");

        String packageName = "com.example.helloworld";
        JavaFile javaFile = JavaFile.builder(packageName, spec.build())
                .skipJavaLangImports(true)
                .build();

        javaFile.writeTo(Paths.get("src/main/java"));
    }

    public static void main(String[] args) throws IllegalAccessException, NoSuchFieldException, IOException {
        String version = net.jbock.compiler.Processor.class.getPackage().getImplementationVersion();
        notMain(version);
        GenAutoTypesParser.notMain();
    }

    private static MethodData createMethodData(String name, Class<?> element) {
        return new MethodData(name, TypeName.get(element));
    }

    static class MethodData {

        final String name;

        final TypeName type;

        MethodData(String name, TypeName type) {
            this.name = snakeToCamel(name);
            this.type = type;
        }
    }

    private static String snakeToCamel(String s) {
        StringBuilder sb = new StringBuilder();
        boolean upcase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '_') {
                upcase = true;
            } else if (upcase) {
                sb.append(Character.toUpperCase(c));
                upcase = false;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private static String baseName(TypeName type) {
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
        name.append(datum.name);
        return MethodSpec.methodBuilder(name.toString())
                .addModifiers(Modifier.ABSTRACT)
                .returns(datum.type)
                .addAnnotation(spec.build())
                .build();
    }
}
