package com.xfinity.resourceprovider;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.lang.model.element.Modifier;

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
    private List<String> rClassIdVars;

    private final ClassName contextClassName = get("android.content", "Context");
    private final FieldSpec contextField = FieldSpec.builder(contextClassName, "context", Modifier.PRIVATE).build();
    private final AnnotationSpec suppressLint = AnnotationSpec.builder(ClassName.get("android.annotation", "SuppressLint"))
                                               .addMember("value", "$L", "{\"StringFormatInvalid\", \"StringFormatMatches\"}")
                                               .build();
    private TypeName contextCompatClassName = get("androidx.core.content", "ContextCompat");

    RpCodeGenerator(String packageName, List<String> rClassStringVars, List<String> rClassPluralVars, List<String> rClassDrawableVars,
                    List<String> rClassDimenVars, List<String> rClassIntegerVars, List<String> rClassColorVars,
                    List<String> rClassIdVars) {
        this.packageName = packageName;
        this.rClassStringVars = rClassStringVars;
        this.rClassPluralVars = rClassPluralVars;
        this.rClassDrawableVars = rClassDrawableVars;
        this.rClassDimenVars = rClassDimenVars;
        this.rClassIntegerVars = rClassIntegerVars;
        this.rClassColorVars = rClassColorVars;
        this.rClassIdVars = rClassIdVars;
    }

    TypeSpec generateResourceProviderClass(boolean generateIdProvider,
                                           boolean generateIntegerProvider,
                                           boolean generateDimensionProvider,
                                           boolean generateColorProvider,
                                           boolean generateDrawableProvider,
                                           boolean generateStringProvider) {
        MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
                                           .addModifiers(PUBLIC)
                                           .addParameter(contextClassName, "context")
                                           .addStatement("this.context = context");

        if (generateIdProvider) {
            constructorBuilder.addStatement("this.idProvider = new IdProvider(context)");
        }

        if (generateIntegerProvider) {
            constructorBuilder.addStatement("this.integerProvider = new IntegerProvider(context)");
        }

        if (generateDrawableProvider) {
            constructorBuilder.addStatement("this.drawableProvider = new DrawableProvider(context)");
        }

        if (generateColorProvider) {
            constructorBuilder.addStatement("this.colorProvider = new ColorProvider(context)");
        }

        if (generateDimensionProvider) {
            constructorBuilder.addStatement("this.dimenProvider = new DimensionProvider(context)");
        }

        if (generateStringProvider) {
            constructorBuilder.addStatement("this.stringProvider = new StringProvider(context)");
        }

        MethodSpec constructor = constructorBuilder.build();

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

        ClassName idProviderClassName = get(packageName, "IdProvider");
        FieldSpec idProvider = FieldSpec.builder(idProviderClassName, "idProvider")
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

        MethodSpec getIdMethodSpec = null;
        if (generateIdProvider) {
            getIdMethodSpec = MethodSpec.methodBuilder("getIds")
                                                        .addModifiers(Modifier.PUBLIC)
                                                        .addStatement("return idProvider")
                                                        .returns(idProviderClassName)
                                                        .build();
        }

        TypeSpec.Builder classBuilder = classBuilder("ResourceProvider")
                .addModifiers(PUBLIC)
                .addField(contextField)
                .addMethod(constructor);

        if (generateIdProvider) {
            classBuilder.addMethod(getIdMethodSpec);
            classBuilder.addField(idProvider);
        }

        if (generateIntegerProvider) {
            classBuilder.addMethod(getIntegerMethodSpec);
            classBuilder.addField(integerProvider);
        }

        if (generateDrawableProvider) {
            classBuilder.addMethod(getDrawableMethodSpec);
            classBuilder.addField(drawableProvider);
        }

        if (generateColorProvider) {
            classBuilder.addMethod(getColorMethodSpec);
            classBuilder.addField(colorProvider);
        }

        if (generateDimensionProvider) {
            classBuilder.addMethod(getDimenMethodSpec);
            classBuilder.addField(dimenProvider);
        }

        if (generateStringProvider) {
            classBuilder.addMethod(getStringMethodSpec);
            classBuilder.addField(stringProvider);
        }

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

        List<String> stringGetterSuffixes = new ArrayList<>();

        for (String var : rClassStringVars) {
            TypeName objectVarArgsType = ArrayTypeName.get(Object[].class);
            ParameterSpec parameterSpec = ParameterSpec.builder(objectVarArgsType, "formatArgs").build();

            try {
                String getterSuffix = getterSuffix(var, stringGetterSuffixes);
                classBuilder.addMethod(MethodSpec.methodBuilder("get" + getterSuffix)
                                                 .addModifiers(Modifier.PUBLIC)
                                                 .addParameter(parameterSpec)
                                                 .returns(String.class)
                                                 .addStatement("return context.getString(R.string." + var + ", formatArgs)")
                                                 .varargs(true)
                                                 .build());
                stringGetterSuffixes.add(getterSuffix);
            } catch (IllegalArgumentException e) {
                System.out.println("\n\nResourceProvider Compiler Error: " + e.getMessage() + ".\n\nUnable to generate API for R.string." + var + "\n\n") ;
            }

        }

        List<String> pluralsGetterSuffixes = new ArrayList<>();
        for (String var : rClassPluralVars) {
            TypeName objectVarArgsType = ArrayTypeName.get(Object[].class);
            ParameterSpec formatArgsParameterSpec = ParameterSpec.builder(objectVarArgsType, "formatArgs").build();
            ParameterSpec quantityParameterSpec = ParameterSpec.builder(INT, "quantity").build();

            try {
                String getterSuffix = getterSuffix(var, pluralsGetterSuffixes);
                classBuilder.addMethod(MethodSpec.methodBuilder("get" + getterSuffix + "QuantityString")
                                                 .addModifiers(Modifier.PUBLIC)
                                                 .addParameter(quantityParameterSpec)
                                                 .addParameter(formatArgsParameterSpec)
                                                 .returns(String.class)
                                                 .addStatement("return context.getResources().getQuantityString(R.plurals." + var + ", quantity, formatArgs)")
                                                 .varargs(true)
                                                 .build());
                pluralsGetterSuffixes.add(getterSuffix);
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

        List<String> getterSuffixes = new ArrayList<>();
        for (String var : rClassDrawableVars) {
            try {
                String getterSuffix = getterSuffix(var, getterSuffixes);
                classBuilder.addMethod(MethodSpec.methodBuilder("get" + getterSuffix)
                                                 .addModifiers(Modifier.PUBLIC)
                                                 .returns(drawableClassName)
                                                 .addStatement("return $T.getDrawable(context, R.drawable." + var + ")", contextCompatClassName)
                                                 .varargs(false)
                                                 .build());
                getterSuffixes.add(getterSuffix);
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

        List<String> getterSuffixes = new ArrayList<>();
        for (String var : rClassColorVars) {
            try {
                String getterSuffix = getterSuffix(var, getterSuffixes);
                classBuilder.addMethod(MethodSpec.methodBuilder("get" + getterSuffix)
                                                 .addModifiers(Modifier.PUBLIC)
                                                 .returns(INT)
                                                 .addStatement("return $T.getColor(context, R.color." + var + ")", contextCompatClassName)
                                                 .varargs(false)
                                                 .build());
                getterSuffixes.add(getterSuffix);
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

        List<String> getterSuffixes = new ArrayList<>();
        for (String var : rClassIntegerVars) {
            try {
                String getterSuffix = getterSuffix(var, getterSuffixes);
                classBuilder.addMethod(MethodSpec.methodBuilder("get" + getterSuffix)
                                                 .addModifiers(Modifier.PUBLIC)
                                                 .returns(INT)
                                                 .addStatement("return context.getResources().getInteger(R.integer." + var + ")")
                                                 .varargs(false)
                                                 .build());
                getterSuffixes.add(getterSuffix);
            } catch (IllegalArgumentException e) {
                System.out.println("\n\nResourceProvider Compiler Error: " + e.getMessage() + ".\n\nUnable to generate API for R.int." + var + "\n\n") ;
            }
        }



        return classBuilder.build();
    }

    TypeSpec generateIdProviderClass() {
        MethodSpec constructor = MethodSpec.constructorBuilder()
                                           .addModifiers(PUBLIC)
                                           .addParameter(contextClassName, "context")
                                           .addStatement("this.context = context")
                                           .build();

        TypeSpec.Builder classBuilder = classBuilder("IdProvider")
                .addModifiers(PUBLIC)
                .addField(contextField)
                .addMethod(constructor)
                .addAnnotation(suppressLint);

        List<IdInfo> idInfoList = Arrays.asList(new IdInfo("R.id.", "", rClassIdVars),
                                                new IdInfo("R.string.", "String", rClassStringVars),
                                                new IdInfo("R.plurals.", "Plural", rClassPluralVars),
                                                new IdInfo("R.drawable.", "Drawable", rClassDrawableVars),
                                                new IdInfo("R.dimen.", "Dimen", rClassDimenVars),
                                                new IdInfo("R.integer.", "Integer", rClassIntegerVars),
                                                new IdInfo("R.color.", "Color", rClassColorVars));

        List<String> getterSuffixes = new ArrayList<>();
        for (IdInfo info : idInfoList) {
            for (String var : info.classVars) {
                try {
                    String getterSuffix = getterSuffix(var, getterSuffixes);
                    classBuilder.addMethod(MethodSpec.methodBuilder("get" + getterSuffix + info.resType + "Id")
                                                     .addModifiers(Modifier.PUBLIC)
                                                     .returns(INT)
                                                     .addStatement("return " + info.idResPrefix + var)
                                                     .varargs(false)
                                                     .build());
                    getterSuffixes.add(getterSuffix);
                } catch (IllegalArgumentException e) {
                    System.out.println("\n\nResourceProvider Compiler Error: " + e.getMessage() + ".\n\nUnable to generate API for " + info.idResPrefix + var + "\n\n");
                }
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

        List<String> getterSuffixes = new ArrayList<>();
        for (String var : rClassDimenVars) {
            try {
                String getterSuffix = getterSuffix(var, getterSuffixes);
                classBuilder.addMethod(MethodSpec.methodBuilder("get" + getterSuffixes + "PixelSize")
                                                 .addModifiers(Modifier.PUBLIC)
                                                 .returns(INT)
                                                 .addStatement("return context.getResources().getDimensionPixelSize(R.dimen." + var + ")")
                                                 .varargs(false)
                                                 .addJavadoc("Returns the dimension R.dimen." + var + " in pixels")
                                                 .build());
                getterSuffixes.add(getterSuffix);
            } catch (IllegalArgumentException e) {
                System.out.println("\n\nResourceProvider Compiler Error: " + e.getMessage() + ".\n\nUnable to generate API for R.dimen." + var + "\n\n") ;
            }

        }

        return classBuilder.build();
    }

    private String getterSuffix(String varName, List<String> getterSuffixes) {
        StringBuilder getterSuffix = new StringBuilder(varName);
        getterSuffix.setCharAt(0, Character.toUpperCase(getterSuffix.charAt(0)));

        int i;
        while ((i = getterSuffix.indexOf("_")) != -1) {
            char old = getterSuffix.charAt(i + 1);
            char newChar = Character.toUpperCase(old);
            getterSuffix.setCharAt(i + 1, newChar);
            getterSuffix.deleteCharAt(i);
        }

        String suffix = getterSuffix.toString();
        String adjustedSuffix = suffix;
        int count = 1;
        while (getterSuffixes.contains(adjustedSuffix)) {
            adjustedSuffix = suffix + count;
            count++;
        }
        return adjustedSuffix;
    }

    class IdInfo {
        String idResPrefix;
        String resType;
        List<String> classVars;

        IdInfo(String idResPrefix, String resType, List<String> classVars) {
            this.idResPrefix = idResPrefix;
            this.resType = resType;
            this.classVars = classVars;
        }
    }
}
