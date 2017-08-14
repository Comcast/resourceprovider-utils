package com.xfinity.resourceprovider;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.List;

import javax.lang.model.element.Modifier;

import static com.squareup.javapoet.ClassName.get;
import static com.squareup.javapoet.TypeName.INT;
import static com.squareup.javapoet.TypeSpec.classBuilder;
import static javax.lang.model.element.Modifier.PUBLIC;


final class RpCodeGenerator {
    private List<String> rClassStringVars;
    private List<String> rClassPluralVars;
    private List<String> rDrawableVars;

    RpCodeGenerator(List<String> rClassStringVars, List<String> rClassPluralVars, List<String> rDrawableVars) {
        this.rClassStringVars = rClassStringVars;
        this.rClassPluralVars = rClassPluralVars;
        this.rDrawableVars = rDrawableVars;
    }

    TypeSpec generateClass() {
        ClassName contextClassName = get("android.content", "Context");
        FieldSpec contextField = FieldSpec.builder(contextClassName, "context", Modifier.PRIVATE).build();
        ClassName drawableClassName = get("android.graphics.drawable", "Drawable");
        TypeName contextCompatClassName = get("android.support.v4.content", "ContextCompat");

        AnnotationSpec supressLint = AnnotationSpec.builder(ClassName.get("android.annotation", "SuppressLint"))
                                                   .addMember("value", "$L", "{\"StringFormatInvalid\", \"StringFormatMatches\"}")
                                                   .build();

        MethodSpec constructor = MethodSpec.constructorBuilder()
                                           .addModifiers(PUBLIC)
                                           .addParameter(contextClassName, "context")
                                           .addStatement("this.context = context")
                                           .build();

        TypeSpec.Builder classBuilder = classBuilder("ResourceProvider")
                .addModifiers(PUBLIC)
                .addField(contextField)
                .addMethod(constructor)
                .addAnnotation(supressLint);

        for (String var : rClassStringVars) {
            TypeName objectVarArgsType = ArrayTypeName.get(Object[].class);
            ParameterSpec parameterSpec = ParameterSpec.builder(objectVarArgsType, "formatArgs").build();

            try {
                classBuilder.addMethod(MethodSpec.methodBuilder("get" + getterSuffix(var))
                                                 .addModifiers(Modifier.PUBLIC)
                                                 .addParameter(parameterSpec)
                                                 .returns(String.class)
                                                 .addStatement("return context.getString(R.string." + var + ", formatArgs)")
                                                 .varargs(true)
                                                 .build());
            } catch (IllegalArgumentException e) {
                System.out.println("\n\nResourceProvider Compiler Error: " + e.getMessage() + ".\n\nUnable to generate API for R.string." + var + "\n\n") ;
            }

        }

        for (String var : rClassPluralVars) {
            TypeName objectVarArgsType = ArrayTypeName.get(Object[].class);
            ParameterSpec formatArgsParameterSpec = ParameterSpec.builder(objectVarArgsType, "formatArgs").build();
            ParameterSpec quantityParameterSpec = ParameterSpec.builder(INT, "quantity").build();

            try {
                classBuilder.addMethod(MethodSpec.methodBuilder("get" + getterSuffix(var) + "QuantityString")
                                                 .addModifiers(Modifier.PUBLIC)
                                                 .addParameter(quantityParameterSpec)
                                                 .addParameter(formatArgsParameterSpec)
                                                 .returns(String.class)
                                                 .addStatement("return context.getResources().getQuantityString(R.plurals." + var + ", quantity, formatArgs)")
                                                 .varargs(true)
                                                 .build());
            } catch (IllegalArgumentException e) {
                System.out.println("\n\nResourceProvider Compiler Error: " + e.getMessage() + ".\n\nUnable to generate API for R.plurals." + var + "\n\n") ;
            }

        }

        for (String var : rDrawableVars) {
            try {
                classBuilder.addMethod(MethodSpec.methodBuilder("get" + getterSuffix(var))
                        .addModifiers(Modifier.PUBLIC)
                        .returns(drawableClassName)
                        .addStatement("return $T.getDrawable(context, R.drawable." + var + ")", contextCompatClassName)
                        .varargs(false)
                        .build());
            } catch (IllegalArgumentException e) {
                System.out.println("\n\nResourceProvider Compiler Error: " + e.getMessage() + ".\n\nUnable to generate API for R.drawable." + var + "\n\n") ;
            }

        }


        return classBuilder.build();
    }

    private String getterSuffix(String varName) {
        StringBuilder getterSuffix = new StringBuilder(varName);
        getterSuffix.setCharAt(0, Character.toUpperCase(getterSuffix.charAt(0)));

        int i;
        while ((i = getterSuffix.indexOf("_")) != -1) {
            char old = getterSuffix.charAt(i + 1);
            char newChar = Character.toUpperCase(old);
            getterSuffix.setCharAt(i + 1, newChar);
            getterSuffix.deleteCharAt(i);
        }

        return getterSuffix.toString();
    }
}
