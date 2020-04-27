package com.xfinity.rpplugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File
import java.util.StringTokenizer

class ResourceProviderPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create<ResourceProviderPluginExtension>("resourceprovider", ResourceProviderPluginExtension::class.java)
        project.tasks.whenTaskAdded{ task ->
            if(task.name.startsWith("process") && task.name.contains("Resources") && !task.name.contains("Test")){
                val index = task.name.indexOf("Resources")
                val variantName = task.name.substring(7, index)

                project.task("generate${variantName}ResourceProvider") {
                    it.doLast {
                        generateResourceProviderForVariant(project, extension, variantName)
                    }
                }.dependsOn(task)

                println("Variant Name: $variantName , TaskName: $task.name")
            }

            if(task.name.startsWith("compile") && task.name.contains("JavaWithJavac") && !task.name.contains("Test")){
                val index = task.name.indexOf("JavaWithJavac")
                val variantName = task.name.substring(7, index)
                println("Variant Name: $variantName , TaskName: $task.name")

                task.dependsOn("generate${variantName}ResourceProvider")
            }
        }
    }

    fun generateResourceProviderForVariant(project: Project, extension: ResourceProviderPluginExtension, variantName: String) {
        val rClassDir = File(project.buildDir.toString() + "/intermediates/compile_and_runtime_not_namespaced_r_class_jar/$variantName/")
        project.exec {
            it.workingDir = rClassDir
            it.executable = "unzip"
            it.args = listOf("R.jar")
            it.isIgnoreExitValue = true
        }

        project.exec {
            it.workingDir = rClassDir
            it.commandLine = listOf("bash", "-c", "find ${rClassDir.absolutePath} -name '*.class' | xargs javap -p > rclass.txt")
        }

        extension.applicationId?.let {
            val resourceProviderFactory = ResourceProviderFactory()
            resourceProviderFactory.buildResourceProvider(it, variantName, project.buildDir.toString(),
                    "${project.buildDir.toString()}/generated/source/kapt/$variantName")
        }
    }
}

open class ResourceProviderPluginExtension {
    var applicationId: String? = null
}

class ResourceProviderFactory {
    fun buildResourceProvider(packageName: String, variantName: String, buildDirectory: String, outputDirectory: String) {
        val rpCodeGenerator  =  RpCodeGenerator(packageName, parseRClassInfoFile(buildDirectory, variantName), outputDirectory)
        rpCodeGenerator.generateCode()
    }

    private fun parseRClassInfoFile(buildDirectory: String, variantName: String): RClassInfo {
        val rClassInfo = File("$buildDirectory/intermediates/compile_and_runtime_not_namespaced_r_class_jar/${variantName}/rclass.txt").readText()
        val tokenizer = StringTokenizer(rClassInfo, "$")

        val rClassStringVars = mutableListOf<String>()
        val rClassPluralVars = mutableListOf<String>()
        val rClassDrawableVars = mutableListOf<String>()
        val rClassDimenVars = mutableListOf<String>()
        val rClassIntegerVars = mutableListOf<String>()
        val rClassColorVars = mutableListOf<String>()
        val rClassIdVars = mutableListOf<String>()

        while (tokenizer.hasMoreTokens()) {
            val token = tokenizer.nextToken();
            if (token.startsWith(STRING_PREFIX)) {
                parseClass(token, rClassStringVars)
            } else if (token.startsWith(PLURAL_PREFIX)) {
                parseClass(token, rClassPluralVars)
            } else if (token.startsWith(DRAWABLE_PREFIX)) {
                parseClass(token, rClassDrawableVars)
            } else if (token.startsWith(DIMEN_PREFIX)) {
                parseClass(token, rClassDimenVars)
            } else if (token.startsWith(INT_PREFIX)) {
                parseClass(token, rClassIntegerVars)
            } else if (token.startsWith(COLOR_PREFIX)) {
                parseClass(token, rClassColorVars)
            } else if (token.startsWith(ID_PREFIX)) {
                parseClass(token, rClassIdVars)
            }
        }

        return RClassInfo(rClassStringVars, rClassPluralVars, rClassDrawableVars, rClassDimenVars, rClassIntegerVars, rClassColorVars, rClassIdVars)
    }

    private fun parseClass(classString: String, varsList: MutableList<String>){
        val varTokenizer = StringTokenizer(classString, ";")
        val rawVarsList = mutableListOf<String>()
        while (varTokenizer.hasMoreTokens()) {
            val varToken = varTokenizer.nextToken()
            val varName = varToken.substringAfter(VAR_PREFIX, "missing").trim(';')
            if (varName != "missing") {
                rawVarsList.add(varName)
            }
        }
        varsList.addAll(rawVarsList.distinct())
    }

    companion object {
        const val STRING_PREFIX = "string {"
        const val PLURAL_PREFIX = "plurals {"
        const val DRAWABLE_PREFIX = "drawable {"
        const val DIMEN_PREFIX = "dimen {"
        const val INT_PREFIX = "integer {"
        const val COLOR_PREFIX = "color {"
        const val ID_PREFIX = "id {"
        const val VAR_PREFIX = "public static final int "
    }
}

data class RClassInfo(val rClassStringVars: List<String>,
                      val rClassPluralVars: List<String>,
                      val rClassDrawableVars: List<String>,
                      val rClassDimenVars: List<String>,
                      val rClassIntegerVars: List<String>,
                      val rClassColorVars: List<String>,
                      val rClassIdVars: List<String>)