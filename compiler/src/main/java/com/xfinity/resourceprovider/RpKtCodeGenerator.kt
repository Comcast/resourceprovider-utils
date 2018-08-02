package com.xfinity.resourceprovider

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import java.io.File
import javax.annotation.processing.ProcessingEnvironment
import javax.tools.Diagnostic

class RpKtCodeGenerator {
    fun generateTestUtils(receiverPackageName: String, processingEnv: ProcessingEnvironment) {
        val kotlinFile = FileSpec.builder("com.xfinity.resourceprovider", "GenResourceProviderTestUtils")
                .addFunction(FunSpec.builder("mock")
                        .receiver(ClassName(receiverPackageName, "ResourceProvider"))
                        .addStatement("com.nhaarman.mockito_kotlin.whenever(this.strings).thenReturn(Mockito.mock(StringProvider::class.java, StringProviderAnswer()))")
                        .build())
                .build()

        val kaptKotlinGeneratedDir = processingEnv.options["kapt.kotlin.generated"] ?:
        run {
            processingEnv.messager.printMessage(
                    Diagnostic.Kind.ERROR,
                    "Can't find the target directory for generated Kotlin files."
            )
        }

        File("$kaptKotlinGeneratedDir/${kotlinFile.packageName.replace(".", "/")}", kotlinFile.name + ".kt").apply {
            parentFile.mkdirs()
            writeText(kotlinFile.toString())
        }
    }
}