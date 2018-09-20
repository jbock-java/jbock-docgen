package de.h90.jblobs.docgen;

import net.jbock.CommandLineArguments;
import net.jbock.Parameter;
import net.jbock.coerce.Coercion;
import net.jbock.coerce.CoercionProvider;
import net.jbock.com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Gen {

    private static final String GEN_CLASS_NAME = "JbockAllTypes";
    private static final Comparator<MethodData> COMPARATOR = Comparator
            .comparingInt(MethodData::type)
            .thenComparing(data -> data.name);

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, IOException {
        Field coercions = CoercionProvider.class.getDeclaredField("coercions");
        coercions.setAccessible(true);
        Map<TypeName, Coercion> map = (Map<TypeName, Coercion>) coercions.get(CoercionProvider.getInstance());
        TypeSpec.Builder spec = TypeSpec.classBuilder(GEN_CLASS_NAME);
        ArrayList<MethodData> data = new ArrayList<>(map.size());
        for (Map.Entry<TypeName, Coercion> e : map.entrySet()) {
            TypeName type = e.getKey();
            String name = baseName(type);
            if (type.isPrimitive()) {
                data.add(new MethodData("required_" + name + "_primitive", type));
            } else {
                data.add(new MethodData(
                        (name.toLowerCase().contains("optional") ? "" : "required_") + name, type));
            }
        }
        data.sort(COMPARATOR);
        for (MethodData datum : data) {
            spec.addMethod(createMethod(datum));
        }
        spec.addModifiers(Modifier.ABSTRACT);
        spec.addJavadoc("Lists represent repeatable arguments.\n");
        spec.addJavadoc("Optionals represent optional arguments.\n");
        spec.addJavadoc("Booleans represent flags, unless there's a mapper.\n");
        spec.addJavadoc("Everything else represents a required argument.\n");
        spec.addAnnotation(CommandLineArguments.class);


        String packageName = "com.example.helloworld";
        JavaFile javaFile = JavaFile.builder(packageName, spec.build())
                .build();

        javaFile.writeTo(Paths.get("src/main/java"));

    }


    static class MethodData {

        final String name;
        final TypeName type;

        MethodData(String name, TypeName type) {
            this.name = snakeToCamel(name);
            this.type = type;
        }

        int type() {
            if (type.isPrimitive()) {
                return -1;
            }
            if (type instanceof ParameterizedTypeName) {
                return ((ParameterizedTypeName) type).rawType.equals(ClassName.get(List.class)) ?
                        1 : 2;
            }
            return 0;
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
        String[] tokens = type.toString().split("\\.", -1);
        String name = tokens[tokens.length - 1];
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append(Character.toLowerCase(c));
            } else {
                return sb.toString() + name.substring(i);
            }
        }
        return sb.toString();
    }

    private static MethodSpec createMethod(MethodData datum) {
        return MethodSpec.methodBuilder(datum.name)
                .addModifiers(Modifier.ABSTRACT)
                .returns(datum.type)
                .addAnnotation(Parameter.class)
                .build();
    }
}
