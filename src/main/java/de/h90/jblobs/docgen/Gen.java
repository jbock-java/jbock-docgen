package de.h90.jblobs.docgen;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import net.jbock.CommandLineArguments;
import net.jbock.Parameter;
import net.jbock.coerce.mappers.CoercionFactory;
import net.jbock.coerce.mappers.StandardCoercions;
import net.jbock.compiler.EvaluatingProcessor;

public class Gen {

    private static final String GEN_CLASS_NAME = "JbockAllTypes";
    private static final Comparator<MethodData> COMPARATOR = Comparator
            .comparingInt(MethodData::ordering)
            .thenComparing(data -> data.name);
    private static final List<Class<?>> PRIMITIVE_OPTIONALS = Arrays.asList(
            OptionalInt.class,
            OptionalLong.class,
            OptionalDouble.class);

    private static void notMain(Elements elements) throws NoSuchFieldException, IllegalAccessException, IOException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        Constructor<StandardCoercions> constructor = StandardCoercions.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        StandardCoercions coercions = constructor.newInstance();
        Field coercionsField = coercions.getClass().getDeclaredField("coercions");
        coercionsField.setAccessible(true);
        Map<?, CoercionFactory> map = (Map<?, CoercionFactory>) coercionsField.get(coercions);
        TypeSpec.Builder spec = TypeSpec.classBuilder(GEN_CLASS_NAME);
        List<MethodData> data = new ArrayList<>(map.size());
        for (Object mapMirror : map.keySet()) {
            Method mapperReturnTypeMethod = CoercionFactory.class.getDeclaredMethod("mapperReturnType");
            mapperReturnTypeMethod.setAccessible(true);
            Field typeMirrorField = mapMirror.getClass().getDeclaredField("typeMirror");
            typeMirrorField.setAccessible(true);
            TypeMirror mapperReturnType = (TypeMirror) typeMirrorField.get(mapMirror);
            String name = baseName(TypeName.get(mapperReturnType));
            data.add(createMethodData(name, mapperReturnType));
        }
        PRIMITIVE_OPTIONALS
                .stream()
                .map(Class::getCanonicalName)
                .map(elements::getTypeElement)
                .map(Gen::createOptionalMethodData)
                .forEach(data::add);
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
        EvaluatingProcessor.source().run((elements, types) -> notMain(elements));
    }

    static MethodData createOptionalMethodData(TypeElement element) {
        String name = element.getSimpleName().toString();
        return createMethodData(name, element.asType(), true);
    }

    static MethodData createMethodData(String name, TypeMirror element) {
        return createMethodData(name, element, false);
    }

    static MethodData createMethodData(String name, TypeMirror element, boolean optional) {
        return new MethodData(
                name,
                TypeName.get(element), optional);
    }

    static class MethodData {

        final String name;

        final TypeName type;

        final boolean optional;

        MethodData(String name, TypeName type, boolean optional) {
            this.name = snakeToCamel(name);
            this.type = type;
            this.optional = optional;
        }

        int ordering() {
            if (type.isPrimitive()) {
                return -3;
            }
            if (type.isBoxedPrimitive()) {
                return -2;
            }
            for (Class<?> aClass : PRIMITIVE_OPTIONALS) {
                if (TypeName.get(aClass).equals(type)) {
                    return -1;
                }
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
        return tokens[tokens.length - 1];
    }

    private static MethodSpec createMethod(MethodData datum) {
        AnnotationSpec.Builder spec = AnnotationSpec.builder(Parameter.class)
                .addMember("longName", "$S", datum.name);
        if (datum.type.equals(TypeName.get(Boolean.class)) ||
                datum.type.equals(TypeName.BOOLEAN)) {
            spec.addMember("flag", "true");
        }
        if (datum.optional) {
            spec.addMember("optional", "true");
        }
        StringBuilder name = new StringBuilder();
        name.append("a_");
        if (datum.type.isBoxedPrimitive()) {
            name.append("boxed_");
        }
        return MethodSpec.methodBuilder(snakeToCamel(name.toString()) + datum.name)
                .addModifiers(Modifier.ABSTRACT)
                .returns(datum.type)
                .addAnnotation(spec.build())
                .build();
    }
}
