package com.xfinity.rpplugin

import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.ArrayTypeName
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterSpec
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import java.io.File
import java.util.ArrayList
import java.util.Arrays
import javax.lang.model.element.Modifier

class RpCodeGenerator(private val packageName: String,
                      private val rClassStringVars: List<String>?,
                      private val rClassPluralVars: List<String>?,
                      private val rClassDrawableVars: List<String>?,
                      private val rClassDimenVars: List<String>?,
                      private val rClassIntegerVars: List<String>?,
                      private val rClassColorVars: List<String>?,
                      private val rClassIdVars: List<String>?) {

    private val contextClassName = ClassName.get("android.content", "Context")
    private val contextField = FieldSpec.builder(contextClassName, "context", Modifier.PRIVATE).build()
    private val suppressLint = AnnotationSpec.builder(ClassName.get("android.annotation", "SuppressLint"))
            .addMember("value", "\$L", "{\"StringFormatInvalid\", \"StringFormatMatches\"}")
            .build()
    private val contextCompatClassName: TypeName = ClassName.get("androidx.core.content", "ContextCompat")

    private fun generateCode(packageName: String, rStringVars: List<String>, rPluralVars: List<String>,
                             rDrawableVars: List<String>, rDimenVars: List<String>, rIntegerVars: List<String>,
                             rColorVars: List<String>, rIdVars: List<String>) {
        val codeGenerator = RpCodeGenerator(packageName, rStringVars, rPluralVars, rDrawableVars, rDimenVars,
                rIntegerVars, rColorVars, rIdVars)
        val generateIdProvider = rIdVars.size > 0
        val idProviderFile = File("IdProvider.java")
        val intProviderFile = File("IntProvider.java")
        val dimenProviderFile = File("DimenProvider.java")
        val colorProviderFile = File("ColorProvider.java")
        val drawableProviderFile = File("DrawableProvider.java")
        val stringProviderFile = File("StringProvider.java")
        val resourceProviderFile = File("ResourceProvider.java")

        if (generateIdProvider) {
            val idProviderClass: TypeSpec = codeGenerator.generateIdProviderClass()
            val idProviderJavaFile = JavaFile.builder(packageName, idProviderClass).build()
            idProviderJavaFile.writeTo(idProviderFile)
        }
        val generateIntegerProvider = rIntegerVars.size > 0
        if (generateIntegerProvider) {
            val integerProviderClass: TypeSpec = codeGenerator.generateIntegerProviderClass()
            val integerProviderJavaFile = JavaFile.builder(packageName, integerProviderClass).build()
            integerProviderJavaFile.writeTo(intProviderFile)
        }
        val generateDimensionProvider = rDimenVars.size > 0
        if (generateDimensionProvider) {
            val dimensionProviderClass: TypeSpec = codeGenerator.generateDimensionProviderClass()
            val dimensionProviderJavaFile = JavaFile.builder(packageName, dimensionProviderClass).build()
            dimensionProviderJavaFile.writeTo(dimenProviderFile)
        }
        val generateColorProvider = rColorVars.size > 0
        if (generateColorProvider) {
            val colorProviderClass: TypeSpec = codeGenerator.generateColorProviderClass()
            val colorProviderJavaFile = JavaFile.builder(packageName, colorProviderClass).build()
            colorProviderJavaFile.writeTo(colorProviderFile)
        }
        val generateDrawableProvider = rDrawableVars.size > 0
        if (generateDrawableProvider) {
            val drawableProviderClass: TypeSpec = codeGenerator.generateDrawableProviderClass()
            val drawableProviderJavaFile = JavaFile.builder(packageName, drawableProviderClass).build()
            drawableProviderJavaFile.writeTo(drawableProviderFile)
        }
        val generateStringProvider = rStringVars.size > 0
        if (generateStringProvider) {
            val stringProviderClass: TypeSpec = codeGenerator.generateStringProviderClass()
            val stringProviderJavaFile = JavaFile.builder(packageName, stringProviderClass).build()
            stringProviderJavaFile.writeTo(stringProviderFile)
        }
        val resourceProviderClass: TypeSpec = codeGenerator.generateResourceProviderClass(
                generateIdProvider,
                generateIntegerProvider,
                generateDimensionProvider,
                generateColorProvider,
                generateDrawableProvider,
                generateStringProvider)

        val resourceProviderJavaFile = JavaFile.builder(packageName, resourceProviderClass).build()
        resourceProviderJavaFile.writeTo(resourceProviderFile)
    }

    fun generateResourceProviderClass(generateIdProvider: Boolean,
                                      generateIntegerProvider: Boolean,
                                      generateDimensionProvider: Boolean,
                                      generateColorProvider: Boolean,
                                      generateDrawableProvider: Boolean,
                                      generateStringProvider: Boolean): TypeSpec {
        val constructorBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(contextClassName, "context")
                .addStatement("this.context = context")
        if (generateIdProvider) {
            constructorBuilder.addStatement("this.idProvider = new IdProvider(context)")
        }
        if (generateIntegerProvider) {
            constructorBuilder.addStatement("this.integerProvider = new IntegerProvider(context)")
        }
        if (generateDrawableProvider) {
            constructorBuilder.addStatement("this.drawableProvider = new DrawableProvider(context)")
        }
        if (generateColorProvider) {
            constructorBuilder.addStatement("this.colorProvider = new ColorProvider(context)")
        }
        if (generateDimensionProvider) {
            constructorBuilder.addStatement("this.dimenProvider = new DimensionProvider(context)")
        }
        if (generateStringProvider) {
            constructorBuilder.addStatement("this.stringProvider = new StringProvider(context)")
        }
        val constructor = constructorBuilder.build()
        val stringProviderClassName = ClassName.get(packageName, "StringProvider")
        val stringProvider = FieldSpec.builder(stringProviderClassName, "stringProvider")
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL).build()
        val drawableProviderClassName = ClassName.get(packageName, "DrawableProvider")
        val drawableProvider = FieldSpec.builder(drawableProviderClassName, "drawableProvider")
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL).build()
        val colorProviderClassName = ClassName.get(packageName, "ColorProvider")
        val colorProvider = FieldSpec.builder(colorProviderClassName, "colorProvider")
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL).build()
        val dimenProviderClassName = ClassName.get(packageName, "DimensionProvider")
        val dimenProvider = FieldSpec.builder(dimenProviderClassName, "dimenProvider")
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL).build()
        val integerProviderClassName = ClassName.get(packageName, "IntegerProvider")
        val integerProvider = FieldSpec.builder(integerProviderClassName, "integerProvider")
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL).build()
        val idProviderClassName = ClassName.get(packageName, "IdProvider")
        val idProvider = FieldSpec.builder(idProviderClassName, "idProvider")
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL).build()
        val getStringMethodSpec = MethodSpec.methodBuilder("getStrings")
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return stringProvider")
                .returns(stringProviderClassName)
                .build()
        val getColorMethodSpec = MethodSpec.methodBuilder("getColors")
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return colorProvider")
                .returns(colorProviderClassName)
                .build()
        val getDrawableMethodSpec = MethodSpec.methodBuilder("getDrawables")
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return drawableProvider")
                .returns(drawableProviderClassName)
                .build()
        val getDimenMethodSpec = MethodSpec.methodBuilder("getDimens")
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return dimenProvider")
                .returns(dimenProviderClassName)
                .build()
        val getIntegerMethodSpec = MethodSpec.methodBuilder("getIntegers")
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return integerProvider")
                .returns(integerProviderClassName)
                .build()
        var getIdMethodSpec: MethodSpec? = null
        if (generateIdProvider) {
            getIdMethodSpec = MethodSpec.methodBuilder("getIds")
                    .addModifiers(Modifier.PUBLIC)
                    .addStatement("return idProvider")
                    .returns(idProviderClassName)
                    .build()
        }
        val classBuilder = TypeSpec.classBuilder("ResourceProvider")
                .addModifiers(Modifier.PUBLIC)
                .addField(contextField)
                .addMethod(constructor)
        if (generateIdProvider) {
            classBuilder.addMethod(getIdMethodSpec)
            classBuilder.addField(idProvider)
        }
        if (generateIntegerProvider) {
            classBuilder.addMethod(getIntegerMethodSpec)
            classBuilder.addField(integerProvider)
        }
        if (generateDrawableProvider) {
            classBuilder.addMethod(getDrawableMethodSpec)
            classBuilder.addField(drawableProvider)
        }
        if (generateColorProvider) {
            classBuilder.addMethod(getColorMethodSpec)
            classBuilder.addField(colorProvider)
        }
        if (generateDimensionProvider) {
            classBuilder.addMethod(getDimenMethodSpec)
            classBuilder.addField(dimenProvider)
        }
        if (generateStringProvider) {
            classBuilder.addMethod(getStringMethodSpec)
            classBuilder.addField(stringProvider)
        }
        return classBuilder.build()
    }

    fun generateStringProviderClass(): TypeSpec {
        val constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(contextClassName, "context")
                .addStatement("this.context = context")
                .build()
        val classBuilder = TypeSpec.classBuilder("StringProvider")
                .addModifiers(Modifier.PUBLIC)
                .addField(contextField)
                .addMethod(constructor)
                .addAnnotation(suppressLint)
        val stringGetterSuffixes: MutableList<String> = ArrayList()
        for (`var` in rClassStringVars!!) {
            val objectVarArgsType = ArrayTypeName.get(Array<Any>::class.java)
            val parameterSpec = ParameterSpec.builder(objectVarArgsType, "formatArgs").build()
            try {
                val getterSuffix = getterSuffix(`var`, stringGetterSuffixes)
                classBuilder.addMethod(MethodSpec.methodBuilder("get$getterSuffix")
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(parameterSpec)
                        .returns(String::class.java)
                        .addStatement("return context.getString(R.string.$`var`, formatArgs)")
                        .varargs(true)
                        .build())
                stringGetterSuffixes.add(getterSuffix)
            } catch (e: IllegalArgumentException) {
                println("\n\nResourceProvider Compiler Error: " + e.message + ".\n\nUnable to generate API for R.string." + `var` + "\n\n")
            }
        }
        val pluralsGetterSuffixes: MutableList<String> = ArrayList()
        for (`var` in rClassPluralVars!!) {
            val objectVarArgsType = ArrayTypeName.get(Array<Any>::class.java)
            val formatArgsParameterSpec = ParameterSpec.builder(objectVarArgsType, "formatArgs").build()
            val quantityParameterSpec = ParameterSpec.builder(TypeName.INT, "quantity").build()
            try {
                val getterSuffix = getterSuffix(`var`, pluralsGetterSuffixes)
                classBuilder.addMethod(MethodSpec.methodBuilder("get" + getterSuffix + "QuantityString")
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(quantityParameterSpec)
                        .addParameter(formatArgsParameterSpec)
                        .returns(String::class.java)
                        .addStatement("return context.getResources().getQuantityString(R.plurals.$`var`, quantity, formatArgs)")
                        .varargs(true)
                        .build())
                pluralsGetterSuffixes.add(getterSuffix)
            } catch (e: IllegalArgumentException) {
                println("\n\nResourceProvider Compiler Error: " + e.message + ".\n\nUnable to generate API for R.plurals." + `var` + "\n\n")
            }
        }
        return classBuilder.build()
    }

    fun generateDrawableProviderClass(): TypeSpec {
        val drawableClassName = ClassName.get("android.graphics.drawable", "Drawable")
        val constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(contextClassName, "context")
                .addStatement("this.context = context")
                .build()
        val classBuilder = TypeSpec.classBuilder("DrawableProvider")
                .addModifiers(Modifier.PUBLIC)
                .addField(contextField)
                .addMethod(constructor)
                .addAnnotation(suppressLint)
        val getterSuffixes: MutableList<String> = ArrayList()
        for (`var` in rClassDrawableVars!!) {
            try {
                val getterSuffix = getterSuffix(`var`, getterSuffixes)
                classBuilder.addMethod(MethodSpec.methodBuilder("get$getterSuffix")
                        .addModifiers(Modifier.PUBLIC)
                        .returns(drawableClassName)
                        .addStatement("return \$T.getDrawable(context, R.drawable.$`var`)", contextCompatClassName)
                        .varargs(false)
                        .build())
                getterSuffixes.add(getterSuffix)
            } catch (e: IllegalArgumentException) {
                println("\n\nResourceProvider Compiler Error: " + e.message + ".\n\nUnable to generate API for R.drawable." + `var` + "\n\n")
            }
        }
        return classBuilder.build()
    }

    fun generateColorProviderClass(): TypeSpec {
        val constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(contextClassName, "context")
                .addStatement("this.context = context")
                .build()
        val classBuilder = TypeSpec.classBuilder("ColorProvider")
                .addModifiers(Modifier.PUBLIC)
                .addField(contextField)
                .addMethod(constructor)
                .addAnnotation(suppressLint)
        val getterSuffixes: MutableList<String> = ArrayList()
        for (`var` in rClassColorVars!!) {
            try {
                val getterSuffix = getterSuffix(`var`, getterSuffixes)
                classBuilder.addMethod(MethodSpec.methodBuilder("get$getterSuffix")
                        .addModifiers(Modifier.PUBLIC)
                        .returns(TypeName.INT)
                        .addStatement("return \$T.getColor(context, R.color.$`var`)", contextCompatClassName)
                        .varargs(false)
                        .build())
                getterSuffixes.add(getterSuffix)
            } catch (e: IllegalArgumentException) {
                println("\n\nResourceProvider Compiler Error: " + e.message + ".\n\nUnable to generate API for R.color." + `var` + "\n\n")
            }
        }
        return classBuilder.build()
    }

    fun generateIntegerProviderClass(): TypeSpec {
        val constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(contextClassName, "context")
                .addStatement("this.context = context")
                .build()
        val classBuilder = TypeSpec.classBuilder("IntegerProvider")
                .addModifiers(Modifier.PUBLIC)
                .addField(contextField)
                .addMethod(constructor)
                .addAnnotation(suppressLint)
        val getterSuffixes: MutableList<String> = ArrayList()
        for (`var` in rClassIntegerVars!!) {
            try {
                val getterSuffix = getterSuffix(`var`, getterSuffixes)
                classBuilder.addMethod(MethodSpec.methodBuilder("get$getterSuffix")
                        .addModifiers(Modifier.PUBLIC)
                        .returns(TypeName.INT)
                        .addStatement("return context.getResources().getInteger(R.integer.$`var`)")
                        .varargs(false)
                        .build())
                getterSuffixes.add(getterSuffix)
            } catch (e: IllegalArgumentException) {
                println("\n\nResourceProvider Compiler Error: " + e.message + ".\n\nUnable to generate API for R.int." + `var` + "\n\n")
            }
        }
        return classBuilder.build()
    }

    fun generateIdProviderClass(): TypeSpec {
        val constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(contextClassName, "context")
                .addStatement("this.context = context")
                .build()
        val classBuilder = TypeSpec.classBuilder("IdProvider")
                .addModifiers(Modifier.PUBLIC)
                .addField(contextField)
                .addMethod(constructor)
                .addAnnotation(suppressLint)
        val idInfoList = Arrays.asList(IdInfo("R.id.", "", rClassIdVars),
                IdInfo("R.string.", "String", rClassStringVars),
                IdInfo("R.plurals.", "Plural", rClassPluralVars),
                IdInfo("R.drawable.", "Drawable", rClassDrawableVars),
                IdInfo("R.dimen.", "Dimen", rClassDimenVars),
                IdInfo("R.integer.", "Integer", rClassIntegerVars),
                IdInfo("R.color.", "Color", rClassColorVars))
        val getterSuffixes: MutableList<String> = ArrayList()
        for (info in idInfoList) {
            for (`var` in info.classVars!!) {
                try {
                    val getterSuffix = getterSuffix(`var`, getterSuffixes)
                    classBuilder.addMethod(MethodSpec.methodBuilder("get" + getterSuffix + info.resType + "Id")
                            .addModifiers(Modifier.PUBLIC)
                            .returns(TypeName.INT)
                            .addStatement("return " + info.idResPrefix + `var`)
                            .varargs(false)
                            .build())
                    getterSuffixes.add(getterSuffix)
                } catch (e: IllegalArgumentException) {
                    println("\n\nResourceProvider Compiler Error: " + e.message + ".\n\nUnable to generate API for " + info.idResPrefix + `var` + "\n\n")
                }
            }
        }
        return classBuilder.build()
    }

    fun generateDimensionProviderClass(): TypeSpec {
        val constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(contextClassName, "context")
                .addStatement("this.context = context")
                .build()
        val classBuilder = TypeSpec.classBuilder("DimensionProvider")
                .addModifiers(Modifier.PUBLIC)
                .addField(contextField)
                .addMethod(constructor)
                .addAnnotation(suppressLint)
        val getterSuffixes: MutableList<String> = ArrayList()
        for (`var` in rClassDimenVars!!) {
            try {
                val getterSuffix = getterSuffix(`var`, getterSuffixes)
                classBuilder.addMethod(MethodSpec.methodBuilder("get" + getterSuffix + "PixelSize")
                        .addModifiers(Modifier.PUBLIC)
                        .returns(TypeName.INT)
                        .addStatement("return context.getResources().getDimensionPixelSize(R.dimen.$`var`)")
                        .varargs(false)
                        .addJavadoc("Returns the dimension R.dimen.$`var` in pixels")
                        .build())
                getterSuffixes.add(getterSuffix)
            } catch (e: IllegalArgumentException) {
                println("\n\nResourceProvider Compiler Error: " + e.message + ".\n\nUnable to generate API for R.dimen." + `var` + "\n\n")
            }
        }
        return classBuilder.build()
    }

    private fun getterSuffix(varName: String, getterSuffixes: List<String>): String {
        val getterSuffix = StringBuilder(varName)
        getterSuffix.setCharAt(0, Character.toUpperCase(getterSuffix[0]))
        var i: Int
        while (getterSuffix.indexOf("_").also { i = it } != -1) {
            val old = getterSuffix[i + 1]
            val newChar = Character.toUpperCase(old)
            getterSuffix.setCharAt(i + 1, newChar)
            getterSuffix.deleteCharAt(i)
        }
        val suffix = getterSuffix.toString()
        var adjustedSuffix = suffix
        var count = 1
        while (getterSuffixes.contains(adjustedSuffix)) {
            adjustedSuffix = suffix + count
            count++
        }
        return adjustedSuffix
    }

    internal class IdInfo(var idResPrefix: String, var resType: String, var classVars: List<String>?)
}