package de.h90.jblobs.docgen;

import net.jbock.CommandLineArguments;
import net.jbock.Parameter;
import net.jbock.coerce.Coercion;
import net.jbock.coerce.CoercionProvider;
import net.jbock.com.squareup.javapoet.ArrayTypeName;
import net.jbock.com.squareup.javapoet.ClassName;
import net.jbock.com.squareup.javapoet.MethodSpec;
import net.jbock.com.squareup.javapoet.ParameterizedTypeName;
import net.jbock.com.squareup.javapoet.TypeName;
import net.jbock.com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Gen {

    static Comparator<MethodData> comparator = Comparator.comparingInt(MethodData::type)
            .thenComparing(data -> data.name);

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        Field coercions = CoercionProvider.class.getDeclaredField("coercions");
        coercions.setAccessible(true);
        Map<TypeName, Coercion> map = (Map<TypeName, Coercion>) coercions.get(CoercionProvider.getInstance());
        TypeSpec.Builder spec = TypeSpec.classBuilder("Everything");
        spec.addMethod(createMethod(new MethodData("string_array", ArrayTypeName.get(String.class))));
        ArrayList<MethodData> data = new ArrayList<>(map.size());
        for (Map.Entry<TypeName, Coercion> e : map.entrySet()) {
            TypeName type = e.getKey();
            String[] tokens = type.toString().toLowerCase().split("\\.", -1);
            String name = tokens[tokens.length - 1];
            if (type.isPrimitive()) {
                data.add(new MethodData(name + "_primitive", type));
            } else {
                data.add(new MethodData(name + "_required", type));
            }
            if (!e.getValue().special()) {
                data.add(new MethodData(name + "_opt", ParameterizedTypeName.get(ClassName.get(Optional.class), type)));
                data.add(new MethodData(name + "_list", ParameterizedTypeName.get(ClassName.get(List.class), type)));
            }
        }
        data.sort(comparator);
        for (MethodData datum : data) {
            spec.addMethod(createMethod(datum));
        }
        spec.addModifiers(Modifier.ABSTRACT);
        spec.addAnnotation(CommandLineArguments.class);
        System.out.println(spec.build().toString());
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

    private static MethodSpec createMethod(MethodData datum) {
        return MethodSpec.methodBuilder(datum.name)
                .addModifiers(Modifier.ABSTRACT)
                .returns(datum.type)
                .addAnnotation(Parameter.class)
                .build();
    }
}
