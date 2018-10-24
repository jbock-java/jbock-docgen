package de.h90.jblobs.docgen;

import com.squareup.javapoet.*;
import net.jbock.CommandLineArguments;
import net.jbock.Parameter;
import net.jbock.coerce.mappers.CoercionFactory;
import net.jbock.coerce.mappers.StandardCoercions;
import net.jbock.compiler.EvaluatingProcessor;

import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Paths;
import java.util.*;

public class Gen {

    private static final String GEN_CLASS_NAME = "JbockAllTypes";
    private static final Comparator<MethodData> COMPARATOR = Comparator
            .comparingInt(MethodData::type)
            .thenComparing(data -> data.name);

    private static void notMain(Elements elements, Types types) throws NoSuchFieldException, IllegalAccessException, IOException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        Constructor<StandardCoercions> constructor = StandardCoercions.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        StandardCoercions coercions = constructor.newInstance();
        Field coercionsField = coercions.getClass().getDeclaredField("coercions");
        coercionsField.setAccessible(true);
        Map<?, CoercionFactory> map = (Map<?, CoercionFactory>) coercionsField.get(coercions);
        TypeSpec.Builder spec = TypeSpec.classBuilder(GEN_CLASS_NAME);
        List<MethodData> data = new ArrayList<>(map.size());
        for (CoercionFactory coercion : map.values()) {
            Method mapperReturnTypeMethod = CoercionFactory.class.getDeclaredMethod("mapperReturnType");
            mapperReturnTypeMethod.setAccessible(true);
            TypeMirror mapperReturnType = (TypeMirror) mapperReturnTypeMethod.invoke(coercion);
            TypeName type = TypeName.get(mapperReturnType);
            String name = baseName(type);
            if (type.isPrimitive()) {
                data.add(new MethodData(name + "_primitive", type));
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
        spec.addAnnotation(CommandLineArguments.class);


        String packageName = "com.example.helloworld";
        JavaFile javaFile = JavaFile.builder(packageName, spec.build())
                .build();

        javaFile.writeTo(Paths.get("src/main/java"));

    }

    public static void main(String[] args) {
        EvaluatingProcessor.source().run((elements, types) -> notMain(elements, types));
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
        AnnotationSpec.Builder spec = AnnotationSpec.builder(Parameter.class)
                .addMember("longName", "$S", datum.name);
        if (datum.type.equals(TypeName.get(Boolean.class)) ||
                datum.type.equals(TypeName.BOOLEAN)) {
            spec.addMember("flag", "true");
        }
        if (datum.type.equals(TypeName.get(OptionalInt.class)) ||
                datum.type.equals(TypeName.get(OptionalLong.class)) ||
                datum.type.equals(TypeName.get(OptionalDouble.class))) {
            spec.addMember("optional", "true");
        }
        return MethodSpec.methodBuilder(datum.name)
                .addModifiers(Modifier.ABSTRACT)
                .returns(datum.type)
                .addAnnotation(spec.build())
                .build();
    }
}
