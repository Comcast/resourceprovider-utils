package com.xfinity.resourceprovider;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import com.sun.tools.javac.code.Symbol;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import static com.squareup.javapoet.JavaFile.builder;
import static com.xfinity.resourceprovider.Utils.getPackageName;
import static java.util.Collections.singleton;
import static javax.lang.model.SourceVersion.latestSupported;

@AutoService(Processor.class)
public class RpProcessor extends AbstractProcessor {

    private static final String ANNOTATION = "@" + RpApplication.class.getSimpleName();
    private static final String R_CLASS_IDENTIFIER = ".R";
    private static final String STRING = "string";
    private static final String PLURALS = "plurals";
    private static final String DRAWABLE = "drawable";
    private static final String DIMEN = "dimen";
    private static final String INTEGER = "integer";
    private static final String COLOR = "color";
    private static final String ANDROID_APP_CLASS_TYPE = "android.app.Application";

    private final Messager messager = new Messager();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager.init(processingEnv);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return singleton(RpApplication.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(RpApplication.class)) {

            // annotation is only allowed on classes, so we can safely cast here
            TypeElement annotatedClass = (TypeElement) annotatedElement;
            if (!isValidClass(annotatedClass)) {
                continue;
            }

            try {
                List<String> rStringVars = new ArrayList<>();
                List<String> rPluralVars = new ArrayList<>();
                List<String> rDrawableVars = new ArrayList<>();
                List<String> rDimenVars = new ArrayList<>();
                List<String> rIntegerVars = new ArrayList<>();
                List<String> rColorVars = new ArrayList<>();

                //lame.  this assumes that the application class is at the top level.  find a better way.
                String packageName = getPackageName(processingEnv.getElementUtils(), annotatedClass);
                String rClassName = packageName + R_CLASS_IDENTIFIER;

                roundEnv.getRootElements().stream().filter(element -> element instanceof TypeElement).forEach(element -> {
                    TypeElement typeElement = (TypeElement) element;
                    if (typeElement.getQualifiedName().toString().equals(rClassName)) {
                        element.getEnclosedElements().stream()
                               .filter(enclosedElement -> enclosedElement instanceof TypeElement)
                               .forEach(enclosedElement -> {
                                   List<? extends Element> enclosedElements = enclosedElement.getEnclosedElements();
                                   if (enclosedElement.getSimpleName().toString().equals(STRING)) {
                                       enclosedElements.stream()
                                                       .filter(stringElement -> stringElement instanceof Symbol.VarSymbol)
                                                       .forEach(stringElement -> rStringVars.add(stringElement.toString()));
                                   }

                                   if (enclosedElement.getSimpleName().toString().equals(PLURALS)) {
                                       enclosedElements.stream()
                                                       .filter(pluralsElement -> pluralsElement instanceof Symbol.VarSymbol)
                                                       .forEach(pluralsElement -> rPluralVars.add(pluralsElement.toString()));
                                   }

                                   if (enclosedElement.getSimpleName().toString().equals(DRAWABLE)) {
                                       enclosedElements.stream()
                                               .filter(drawableElement -> drawableElement instanceof Symbol.VarSymbol)
                                               .forEach(drawableElement -> rDrawableVars.add(drawableElement.toString()));
                                   }

                                   if (enclosedElement.getSimpleName().toString().equals(DIMEN)) {
                                       enclosedElements.stream()
                                                       .filter(dimenElement -> dimenElement instanceof Symbol.VarSymbol)
                                                       .forEach(dimenElement -> rDimenVars.add(dimenElement.toString()));
                                   }

                                   if (enclosedElement.getSimpleName().toString().equals(INTEGER)) {
                                       enclosedElements.stream()
                                                       .filter(integerElement -> integerElement instanceof Symbol.VarSymbol)
                                                       .forEach(integerElement -> rIntegerVars.add(integerElement.toString()));
                                   }

                                   if (enclosedElement.getSimpleName().toString().equals(COLOR)) {
                                       enclosedElements.stream()
                                                       .filter(colorElement -> colorElement instanceof Symbol.VarSymbol)
                                                       .forEach(colorElement -> rColorVars.add(colorElement.toString()));
                                   }
                               });
                    }
                });

                generateCode(annotatedClass, rStringVars, rPluralVars, rDrawableVars, rDimenVars, rIntegerVars, rColorVars);
            } catch (UnnamedPackageException | IOException e) {
                messager.error(annotatedElement, "Couldn't generate class for %s: %s", annotatedClass,
                               e.getMessage());
            }
        }

        return true;
    }

    private boolean isValidClass(TypeElement annotatedClass) {
        TypeElement applicationTypeElement = processingEnv.getElementUtils().getTypeElement(ANDROID_APP_CLASS_TYPE);
        return processingEnv.getTypeUtils().isAssignable(annotatedClass.asType(), applicationTypeElement.asType());
    }

    private void generateCode(TypeElement annotatedClass, List<String> rStringVars, List<String> rPluralVars,
                              List<String> rDrawableVars, List<String> rDimenVars, List<String> rIntegerVars,
                              List<String> rColorVars)
            throws UnnamedPackageException, IOException {
        String packageName = getPackageName(processingEnv.getElementUtils(), annotatedClass);
        RpCodeGenerator codeGenerator = new RpCodeGenerator(rStringVars, rPluralVars, rDrawableVars, rDimenVars,
                                                            rIntegerVars, rColorVars);
        TypeSpec generatedClass = codeGenerator.generateClass();
        JavaFile javaFile = builder(packageName, generatedClass).build();
        javaFile.writeTo(processingEnv.getFiler());
    }
}

