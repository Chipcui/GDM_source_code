package org.gobiiproject.gobiiprocess.machine.builder.processor;

import org.gobiiproject.gobiiprocess.machine.builder.Component;
import org.gobiiproject.gobiiprocess.machine.components.Failure;
import org.gobiiproject.gobiiprocess.machine.components.SideEffect;
import org.gobiiproject.gobiiprocess.machine.components.Transition;
import org.gobiiproject.gobiiprocess.machine.components.Validation;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.LinkedHashSet;
import java.util.Set;

public class ComponentProcessor extends AbstractProcessor {

	private Types typeUtils;
	private Elements elementUtils;
	private Filer filer;
	private Messager messager;
	//private Map<String, FactoryGroupedClasses> factoryClasses = new LinkedHashMap<String, FactoryGroupedClasses>();

	@Override
	public synchronized void init(ProcessingEnvironment processingEnv) {
		super.init(processingEnv);
		typeUtils = processingEnv.getTypeUtils();
		elementUtils = processingEnv.getElementUtils();
		filer = processingEnv.getFiler();
		messager = processingEnv.getMessager();
	}

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		Set<String> annotataions = new LinkedHashSet<String>();
		annotataions.add(Component.class.getCanonicalName());
		return annotataions;
	}

	@Override
	public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.latestSupported();
	}
	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

		ProcessorModel model = new ProcessorModel();

		for (Element component : roundEnv.getElementsAnnotatedWith(Component.class)) {

			TypeMirror componentMirror = component.asType();

			TypeMirror transitionTargetMirror = getImplementationSubtype(componentMirror, Transition.class);
			TypeMirror validationTargetMirror = getImplementationSubtype(componentMirror, Validation.class);
			TypeMirror sideEffectTargetMirror = getImplementationSubtype(componentMirror, SideEffect.class);
			TypeMirror failureTargetMirror    = getImplementationSubtype(componentMirror, Failure.class);

//			model.addComponent(componentMirror, transitionTargetMirror, Transition.class);

			TypeElement componentElement = (TypeElement) typeUtils.asElement(transitionTargetMirror);


		}

		return false;
	}

	private TypeMirror getImplementationSubtype(TypeMirror mirror, Class<?> target) {

		final String targetName = getFullyQualifiedName(target);

		TypeElement typeElement = (TypeElement) typeUtils.asElement(mirror);

		for (TypeMirror interfaceType : typeElement.getInterfaces()) {
			if (interfaceType.toString().equals(targetName)) {
				DeclaredType declaredType = (DeclaredType) interfaceType;
				return declaredType.getTypeArguments().get(0);
			}
		}

		return null;
	}

	private String getFullyQualifiedName(Class <?> c) {
		try {
			return c.getCanonicalName();
		} catch (MirroredTypeException mte) {
			DeclaredType classTypeMirror = (DeclaredType) mte.getTypeMirror();
			TypeElement classTypeElement = (TypeElement) classTypeMirror.asElement();
			return classTypeElement.getQualifiedName().toString();
		}
	}
}
