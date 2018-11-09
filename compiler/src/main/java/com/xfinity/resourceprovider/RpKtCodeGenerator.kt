package com.xfinity.resourceprovider

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import java.io.File
import javax.annotation.processing.ProcessingEnvironment

class RpKtCodeGenerator {
    fun generateTestUtils(receiverPackageName: String, processingEnv: ProcessingEnvironment, generateIdsMock: Boolean) {
        val mockStringsFunSpec = FunSpec.builder("mockStrings")
                .receiver(ClassName(receiverPackageName, "ResourceProvider"))
                .addStatement(
                        "com.nhaarman.mockito_kotlin.whenever(this.strings).thenReturn(" +
                                "org.mockito.Mockito.mock($receiverPackageName.StringProvider::class.java, " +
                                "com.xfinity.resourceprovider.testutils.StringProviderAnswer()))").build()

        val mockDrawablesFunSpec = FunSpec.builder("mockDrawables")
                .receiver(ClassName(receiverPackageName, "ResourceProvider"))
                .addStatement(
                        "com.nhaarman.mockito_kotlin.whenever(this.drawables).thenReturn(" +
                                "org.mockito.Mockito.mock($receiverPackageName.DrawableProvider::class.java, " +
                                "com.xfinity.resourceprovider.testutils.DrawableProviderAnswer()))").build()

        val mockColorsFunSpec = FunSpec.builder("mockColors")
                .receiver(ClassName(receiverPackageName, "ResourceProvider"))
                .addStatement(
                        "com.nhaarman.mockito_kotlin.whenever(this.colors).thenReturn(" +
                                "org.mockito.Mockito.mock($receiverPackageName.ColorProvider::class.java, " +
                                "com.xfinity.resourceprovider.testutils.IntegerProviderAnswer()))").build()

        val mockDimensFunSpec = FunSpec.builder("mockDimens")
                .receiver(ClassName(receiverPackageName, "ResourceProvider"))
                .addStatement(
                        "com.nhaarman.mockito_kotlin.whenever(this.dimens).thenReturn(" +
                                "org.mockito.Mockito.mock($receiverPackageName.DimensionProvider::class.java, " +
                                "com.xfinity.resourceprovider.testutils.IntegerProviderAnswer()))").build()

        val mockIntegersFunSpec = FunSpec.builder("mockIntegers")
                .receiver(ClassName(receiverPackageName, "ResourceProvider"))
                .addStatement(
                        "com.nhaarman.mockito_kotlin.whenever(this.integers).thenReturn(" +
                                "org.mockito.Mockito.mock($receiverPackageName.IntegerProvider::class.java, " +
                                "com.xfinity.resourceprovider.testutils.IntegerProviderAnswer()))")
                .build()

        val mockIdsFunSpec = FunSpec.builder("mockIds")
                .receiver(ClassName(receiverPackageName, "ResourceProvider"))
                .addStatement(
                        "com.nhaarman.mockito_kotlin.whenever(this.ids).thenReturn(" +
                                "org.mockito.Mockito.mock($receiverPackageName.IdProvider::class.java, " +
                                "com.xfinity.resourceprovider.testutils.IntegerProviderAnswer()))")
                .build()

        val mockFunSpecBuilder = FunSpec.builder("mock")
                .receiver(ClassName(receiverPackageName, "ResourceProvider"))
                .addStatement("this.mockStrings()")
                .addStatement("this.mockDrawables()")
                .addStatement("this.mockColors()")
                .addStatement("this.mockDimens()")
                .addStatement("this.mockIntegers()")

        if (generateIdsMock) {
            mockFunSpecBuilder.addStatement("this.mockIds()")
        }

        val mockFunSpec = mockFunSpecBuilder.build()

        val kotlinFileBuilder = FileSpec.builder("com.xfinity.resourceprovider.testutils", "GeneratedResourceProviderTestUtils")
                .addFunction(mockStringsFunSpec)
                .addFunction(mockColorsFunSpec)
                .addFunction(mockDrawablesFunSpec)
                .addFunction(mockDimensFunSpec)
                .addFunction(mockIntegersFunSpec)

        if (generateIdsMock) {
            kotlinFileBuilder.addFunction(mockIdsFunSpec)
        }

        kotlinFileBuilder.addFunction(mockFunSpec)
        val kotlinFile = kotlinFileBuilder.build()

        val kaptKotlinGeneratedDir = processingEnv.options["kapt.kotlin.generated"]
        kotlinFile.writeTo(File(kaptKotlinGeneratedDir))
    }
}