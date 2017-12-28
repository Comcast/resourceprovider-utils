package com.xfinity.resourceprovider;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.util.Arrays;
import java.util.List;

import static com.squareup.javapoet.ClassName.get;
import static com.squareup.javapoet.TypeName.INT;
import static com.squareup.javapoet.TypeSpec.classBuilder;
import static javax.lang.model.element.Modifier.PUBLIC;


final class RpCodeGenerator {
    private final String packageName;
    private List<String> rClassStringVars;
    private List<String> rClassPluralVars;
    private List<String> rClassDrawableVars;
    private List<String> rClassDimenVars;
    private List<String> rClassIntegerVars;
    private List<String> rClassColorVars;

    private final ClassName contextClassName = get("android.content", "Context");
    private final FieldSpec contextField = FieldSpec.builder(contextClassName, "context", Modifier.PRIVATE).build();
    private final AnnotationSpec suppressLint = AnnotationSpec.builder(ClassName.get("android.annotation", "SuppressLint"))
                                               .addMember("value", "$L", "{\"StringFormatInvalid\", \"StringFormatMatches\"}")
                                               .build();
    private TypeName contextCompatClassName = get("android.support.v4.content", "ContextCompat");

    RpCodeGenerator(String packageName, List<String> rClassStringVars, List<String> rClassPluralVars, List<String> rClassDrawableVars,
                    List<String> rClassDimenVars, List<String> rClassIntegerVars, List<String> rClassColorVars) {
        this.packageName = packageName;
        this.rClassStringVars = rClassStringVars;
        this.rClassPluralVars = rClassPluralVars;
        this.rClassDrawableVars = rClassDrawableVars;
        this.rClassDimenVars = rClassDimenVars;
        this.rClassIntegerVars = rClassIntegerVars;
        this.rClassColorVars = rClassColorVars;
    }

    TypeSpec generateResourceProviderClass() {
        MethodSpec constructor = MethodSpec.constructorBuilder()
                                           .addModifiers(PUBLIC)
                                           .addParameter(contextClassName, "context")
                                           .addStatement("this.context = context")
                                           .addStatement("this.stringProvider = new StringProvider(context)")
                                           .addStatement("this.drawableProvider = new DrawableProvider(context)")
                                           .addStatement("this.colorProvider = new ColorProvider(context)")
                                           .addStatement("this.dimenProvider = new DimensionProvider(context)")
                                           .addStatement("this.integerProvider = new IntegerProvider(context)")
                                           .build();

        ClassName stringProviderClassName = get(packageName, "StringProvider");
        FieldSpec stringProvider = FieldSpec.builder(stringProviderClassName, "stringProvider")
                                            .addModifiers(Modifier.PRIVATE, Modifier.FINAL).build();

        ClassName drawableProviderClassName = get(packageName, "DrawableProvider");
        FieldSpec drawableProvider = FieldSpec.builder(drawableProviderClassName, "drawableProvider")
                                              .addModifiers(Modifier.PRIVATE, Modifier.FINAL).build();

        ClassName colorProviderClassName = get(packageName, "ColorProvider");
        FieldSpec colorProvider = FieldSpec.builder(colorProviderClassName, "colorProvider")
                                           .addModifiers(Modifier.PRIVATE, Modifier.FINAL).build();

        ClassName dimenProviderClassName = get(packageName, "DimensionProvider");
        FieldSpec dimenProvider = FieldSpec.builder(dimenProviderClassName, "dimenProvider")
                                           .addModifiers(Modifier.PRIVATE, Modifier.FINAL).build();

        ClassName integerProviderClassName = get(packageName, "IntegerProvider");
        FieldSpec integerProvider = FieldSpec.builder(integerProviderClassName, "integerProvider")
                                             .addModifiers(Modifier.PRIVATE, Modifier.FINAL).build();

        MethodSpec getStringMethodSpec = MethodSpec.methodBuilder("getStrings")
                                                   .addModifiers(Modifier.PUBLIC)
                                                   .addStatement("return stringProvider")
                                                   .returns(stringProviderClassName)
                                                   .build();
        MethodSpec getColorMethodSpec = MethodSpec.methodBuilder("getColors")
                                                  .addModifiers(Modifier.PUBLIC)
                                                  .addStatement("return colorProvider")
                                                  .returns(colorProviderClassName)
                                                  .build();
        MethodSpec getDrawableMethodSpec = MethodSpec.methodBuilder("getDrawables")
                                                     .addModifiers(Modifier.PUBLIC)
                                                     .addStatement("return drawableProvider")
                                                     .returns(drawableProviderClassName)
                                                     .build();
        MethodSpec getDimenMethodSpec = MethodSpec.methodBuilder("getDimens")
                                                  .addModifiers(Modifier.PUBLIC)
                                                  .addStatement("return dimenProvider")
                                                  .returns(dimenProviderClassName)
                                                  .build();
        MethodSpec getIntegerMethodSpec = MethodSpec.methodBuilder("getIntegers")
                                                    .addModifiers(Modifier.PUBLIC)
                                                    .addStatement("return integerProvider")
                                                    .returns(integerProviderClassName)
                                                    .build();

        TypeSpec.Builder classBuilder = classBuilder("ResourceProvider")
                .addModifiers(PUBLIC)
                .addField(contextField)
                .addMethods(Arrays.asList(constructor,
                                          getStringMethodSpec,
                                          getColorMethodSpec,
                                          getDrawableMethodSpec,
                                          getDimenMethodSpec,
                                          getIntegerMethodSpec))
                .addField(stringProvider)
                .addField(drawableProvider)
                .addField(colorProvider)
                .addField(dimenProvider)
                .addField(integerProvider);

        return classBuilder.build();
    }

    TypeSpec generateStringProviderClass() {
        MethodSpec constructor = MethodSpec.constructorBuilder()
                                           .addModifiers(PUBLIC)
                                           .addParameter(contextClassName, "context")
                                           .addStatement("this.context = context")
                                           .build();

        TypeSpec.Builder classBuilder = classBuilder("StringProvider")
                .addModifiers(PUBLIC)
                .addField(contextField)
                .addMethod(constructor)
                .addAnnotation(suppressLint);

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

        return classBuilder.build();
    }

    TypeSpec generateDrawableProviderClass() {
        ClassName drawableClassName = get("android.graphics.drawable", "Drawable");
        MethodSpec constructor = MethodSpec.constructorBuilder()
                                           .addModifiers(PUBLIC)
                                           .addParameter(contextClassName, "context")
                                           .addStatement("this.context = context")
                                           .build();

        TypeSpec.Builder classBuilder = classBuilder("DrawableProvider")
                .addModifiers(PUBLIC)
                .addField(contextField)
                .addMethod(constructor)
                .addAnnotation(suppressLint);

        for (String var : rClassDrawableVars) {
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

    TypeSpec generateColorProviderClass() {
        MethodSpec constructor = MethodSpec.constructorBuilder()
                                           .addModifiers(PUBLIC)
                                           .addParameter(contextClassName, "context")
                                           .addStatement("this.context = context")
                                           .build();

        TypeSpec.Builder classBuilder = classBuilder("ColorProvider")
                .addModifiers(PUBLIC)
                .addField(contextField)
                .addMethod(constructor)
                .addAnnotation(suppressLint);

        for (String var : rClassColorVars) {
            try {
                classBuilder.addMethod(MethodSpec.methodBuilder("get" + getterSuffix(var))
                                                 .addModifiers(Modifier.PUBLIC)
                                                 .returns(INT)
                                                 .addStatement("return $T.getColor(context, R.color." + var + ")", contextCompatClassName)
                                                 .varargs(false)
                                                 .build());
            } catch (IllegalArgumentException e) {
                System.out.println("\n\nResourceProvider Compiler Error: " + e.getMessage() + ".\n\nUnable to generate API for R.color." + var + "\n\n") ;
            }
        }

        return classBuilder.build();
    }

    TypeSpec generateIntegerProviderClass() {
        MethodSpec constructor = MethodSpec.constructorBuilder()
                                           .addModifiers(PUBLIC)
                                           .addParameter(contextClassName, "context")
                                           .addStatement("this.context = context")
                                           .build();

        TypeSpec.Builder classBuilder = classBuilder("IntegerProvider")
                .addModifiers(PUBLIC)
                .addField(contextField)
                .addMethod(constructor)
                .addAnnotation(suppressLint);


        for (String var : rClassIntegerVars) {
            try {
                classBuilder.addMethod(MethodSpec.methodBuilder("get" + getterSuffix(var))
                                                 .addModifiers(Modifier.PUBLIC)
                                                 .returns(INT)
                                                 .addStatement("return context.getResources().getInteger(R.integer." + var + ")")
                                                 .varargs(false)
                                                 .build());
            } catch (IllegalArgumentException e) {
                System.out.println("\n\nResourceProvider Compiler Error: " + e.getMessage() + ".\n\nUnable to generate API for R.int." + var + "\n\n") ;
            }
        }



        return classBuilder.build();
    }

    TypeSpec generateDimensionProviderClass() {
        MethodSpec constructor = MethodSpec.constructorBuilder()
                                           .addModifiers(PUBLIC)
                                           .addParameter(contextClassName, "context")
                                           .addStatement("this.context = context")
                                           .build();

        TypeSpec.Builder classBuilder = classBuilder("DimensionProvider")
                .addModifiers(PUBLIC)
                .addField(contextField)
                .addMethod(constructor)
                .addAnnotation(suppressLint);


        for (String var : rClassDimenVars) {
            try {
                classBuilder.addMethod(MethodSpec.methodBuilder("get" + getterSuffix(var) + "PixelSize")
                                                 .addModifiers(Modifier.PUBLIC)
                                                 .returns(INT)
                                                 .addStatement("return context.getResources().getDimensionPixelSize(R.dimen." + var + ")")
                                                 .varargs(false)
                                                 .addJavadoc("Returns the dimension R.dimen." + var + " in pixels")
                                                 .build());
            } catch (IllegalArgumentException e) {
                System.out.println("\n\nResourceProvider Compiler Error: " + e.getMessage() + ".\n\nUnable to generate API for R.dimen." + var + "\n\n") ;
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
