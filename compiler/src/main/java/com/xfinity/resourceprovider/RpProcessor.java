package com.xfinity.resourceprovider;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import com.sun.tools.javac.code.Symbol;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.squareup.javapoet.JavaFile.builder;
import static com.xfinity.resourceprovider.Utils.getPackageName;
import static javax.lang.model.SourceVersion.latestSupported;

@AutoService(Processor.class)
@SupportedOptions("kapt.kotlin.generated")
public class RpProcessor extends AbstractProcessor {

    private static final String ANNOTATION = "@" + RpApplication.class.getSimpleName();
    private static final String R_CLASS_IDENTIFIER = ".R";
    private static final String STRING = "string";
    private static final String PLURALS = "plurals";
    private static final String DRAWABLE = "drawable";
    private static final String DIMEN = "dimen";
    private static final String INTEGER = "integer";
    private static final String COLOR = "color";
    private static final String ID = "id";
    private static final String ANDROID_APP_CLASS_TYPE = "android.app.Application";

    private final Messager messager = new Messager();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager.init(processingEnv);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new HashSet<>();
        annotations.add(RpApplication.class.getCanonicalName());
        annotations.add(RpTest.class.getCanonicalName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        String useSupportLibraryString = processingEnv.getOptions().get("resourceProvider.useSupportLibrary");
        boolean useJetpack = !"true".equals(useSupportLibraryString);

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
                List<String> rIdVars = new ArrayList<>();

                //lame.  this assumes that the application class is at the top level.  find a better way.
                String packageName = getPackageName(processingEnv.getElementUtils(), annotatedClass);
                String rClassName = packageName + R_CLASS_IDENTIFIER;

                boolean generateIdProvider = annotatedElement.getAnnotation(RpApplication.class).generateIdProvider();
                boolean generateStringProvider = annotatedElement.getAnnotation(RpApplication.class).generateStringProvider();
                boolean generateColorProvider = annotatedElement.getAnnotation(RpApplication.class).generateColorProvider();
                boolean generateDrawableProvider = annotatedElement.getAnnotation(RpApplication.class).generateDrawableProvider();
                boolean generateIntegerProvider = annotatedElement.getAnnotation(RpApplication.class).generateIntegerProvider();
                boolean generateDimensionProvider = annotatedElement.getAnnotation(RpApplication.class).generateDimensionProvider();
                roundEnv.getRootElements().stream().filter(element -> element instanceof TypeElement).forEach(element -> {
                    TypeElement typeElement = (TypeElement) element;
                    if (typeElement.getQualifiedName().toString().equals(rClassName)) {
                        element.getEnclosedElements().stream()
                               .filter(enclosedElement -> enclosedElement instanceof TypeElement)
                               .forEach(enclosedElement -> {
                                   List<? extends Element> enclosedElements = enclosedElement.getEnclosedElements();
                                   if (generateStringProvider) {
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
                                   }

                                   if (generateDrawableProvider) {
                                       if (enclosedElement.getSimpleName().toString().equals(DRAWABLE)) {
                                           enclosedElements.stream()
                                                   .filter(drawableElement -> drawableElement instanceof Symbol.VarSymbol)
                                                   .forEach(drawableElement -> rDrawableVars.add(drawableElement.toString()));
                                       }
                                   }

                                   if (generateDimensionProvider) {
                                       if (enclosedElement.getSimpleName().toString().equals(DIMEN)) {
                                           enclosedElements.stream()
                                                   .filter(dimenElement -> dimenElement instanceof Symbol.VarSymbol)
                                                   .forEach(dimenElement -> rDimenVars.add(dimenElement.toString()));
                                       }
                                   }

                                   if (generateIntegerProvider) {
                                       if (enclosedElement.getSimpleName().toString().equals(INTEGER)) {
                                           enclosedElements.stream()
                                                   .filter(integerElement -> integerElement instanceof Symbol.VarSymbol)
                                                   .forEach(integerElement -> rIntegerVars.add(integerElement.toString()));
                                       }
                                   }

                                   if (generateColorProvider) {
                                       if (enclosedElement.getSimpleName().toString().equals(COLOR)) {
                                           enclosedElements.stream()
                                                   .filter(colorElement -> colorElement instanceof Symbol.VarSymbol)
                                                   .forEach(colorElement -> rColorVars.add(colorElement.toString()));
                                       }
                                   }

                                   if (generateIdProvider) {
                                       if (enclosedElement.getSimpleName().toString().equals(ID)) {
                                           enclosedElements.stream()
                                                           .filter(idElement -> idElement instanceof Symbol.VarSymbol)
                                                           .forEach(idElement -> rIdVars.add(idElement.toString()));
                                       }
                                   }
                               });
                    }
                });

                generateCode(annotatedClass, useJetpack, rStringVars, rPluralVars, rDrawableVars, rDimenVars, rIntegerVars,
                             rColorVars, rIdVars);
            } catch (UnnamedPackageException | IOException e) {
                messager.error(annotatedElement, "Couldn't generate class for %s: %s", annotatedClass,
                               e.getMessage());
            }
        }

        for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(RpTest.class)) {
            PackageElement packageElement = processingEnv.getElementUtils().getPackageOf(annotatedElement);
            String packageName = packageElement.toString();

            boolean generateIdProvider = annotatedElement.getAnnotation(RpTest.class).generateIdProvider();
            new RpKtCodeGenerator().generateTestUtils(packageName, processingEnv, generateIdProvider);
        }

        return true;
    }

    private boolean isValidClass(TypeElement annotatedClass) {
        TypeElement applicationTypeElement = processingEnv.getElementUtils().getTypeElement(ANDROID_APP_CLASS_TYPE);
        return processingEnv.getTypeUtils().isAssignable(annotatedClass.asType(), applicationTypeElement.asType());
    }

    private void generateCode(TypeElement annotatedClass,
                              boolean useJetpack,
                              List<String> rStringVars,
                              List<String> rPluralVars,
                              List<String> rDrawableVars,
                              List<String> rDimenVars,
                              List<String> rIntegerVars,
                              List<String> rColorVars,
                              List<String> rIdVars)
            throws UnnamedPackageException, IOException {
        String packageName = getPackageName(processingEnv.getElementUtils(), annotatedClass);
        RpCodeGenerator codeGenerator = new RpCodeGenerator(packageName, useJetpack, rStringVars, rPluralVars, rDrawableVars, rDimenVars,
                                                            rIntegerVars, rColorVars, rIdVars);

        boolean generateIdProvider = rIdVars.size() > 0;
        if (generateIdProvider) {
            TypeSpec idProviderClass = codeGenerator.generateIdProviderClass();
            JavaFile idProviderJavaFile = builder(packageName, idProviderClass).build();
            idProviderJavaFile.writeTo(processingEnv.getFiler());
        }

        boolean generateIntegerProvider = rIntegerVars.size() > 0;
        if (generateIntegerProvider) {
            TypeSpec integerProviderClass = codeGenerator.generateIntegerProviderClass();
            JavaFile integerProviderJavaFile = builder(packageName, integerProviderClass).build();
            integerProviderJavaFile.writeTo(processingEnv.getFiler());
        }

        boolean generateDimensionProvider = rDimenVars.size() > 0;
        if (generateDimensionProvider) {
            TypeSpec dimensionProviderClass = codeGenerator.generateDimensionProviderClass();
            JavaFile dimensionProviderJavaFile = builder(packageName, dimensionProviderClass).build();
            dimensionProviderJavaFile.writeTo(processingEnv.getFiler());
        }

        boolean generateColorProvider = rColorVars.size() > 0;
        if (generateColorProvider) {
            TypeSpec colorProviderClass = codeGenerator.generateColorProviderClass();
            JavaFile colorProviderJavaFile = builder(packageName, colorProviderClass).build();
            colorProviderJavaFile.writeTo(processingEnv.getFiler());
        }

        boolean generateDrawableProvider = rDrawableVars.size() > 0;
        if (generateDrawableProvider) {
            TypeSpec drawableProviderClass = codeGenerator.generateDrawableProviderClass();
            JavaFile drawableProviderJavaFile = builder(packageName, drawableProviderClass).build();
            drawableProviderJavaFile.writeTo(processingEnv.getFiler());
        }

        boolean generateStringProvider = rDrawableVars.size() > 0;
        if (generateStringProvider) {
            TypeSpec stringProviderClass = codeGenerator.generateStringProviderClass();
            JavaFile stringProviderJavaFile = builder(packageName, stringProviderClass).build();
            stringProviderJavaFile.writeTo(processingEnv.getFiler());
        }

        TypeSpec resourceProviderClass = codeGenerator.generateResourceProviderClass(
                generateIdProvider,
                generateIntegerProvider,
                generateDimensionProvider,
                generateColorProvider,
                generateDrawableProvider,
                generateStringProvider);

        JavaFile resourceProviderJavaFile = builder(packageName, resourceProviderClass).build();
        resourceProviderJavaFile.writeTo(processingEnv.getFiler());
    }
}

