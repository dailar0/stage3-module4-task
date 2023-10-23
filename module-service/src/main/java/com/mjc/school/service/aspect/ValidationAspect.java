package com.mjc.school.service.aspect;

import com.mjc.school.repository.annotation.validation.Valid;
import com.mjc.school.service.exception.ValidationException;
import com.mjc.school.service.validation.Validator;
import com.mjc.school.service.validation.util.ValidatorRegistry;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

@Aspect
@Component
@RequiredArgsConstructor
public class ValidationAspect {
    private final ValidatorRegistry validatorRegistry;


    @Before("within(com.mjc.school.repository.dao..*) && execution(* *(.., @com.mjc.school.repository.annotation.validation.Valid (*), ..))")
    public void validate(JoinPoint joinPoint) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Object[] validationCandidates = joinPoint.getArgs();
        if (!isValidatable(method))
            return;

        Map<ValueFieldNamePair, Set<Annotation>> validatedObjectsMap = getValidatedObjectsMap(method, validationCandidates);

        for (Map.Entry<ValueFieldNamePair, Set<Annotation>> entry : validatedObjectsMap.entrySet()) {
            for (Annotation annotation : entry.getValue()) {
                Validator<?, ? extends Annotation> validator = validatorRegistry.resolve(annotation);
                Set<String> violations = validator.validate(entry.getKey().value(),
                        annotation,
                        entry.getKey().fieldName());
                if (!violations.isEmpty())
                    throw new ValidationException(Validator.formErrorMessage(violations));
            }
        }
    }

    @SneakyThrows
    private Map<ValueFieldNamePair, Set<Annotation>> getValidatedObjectsMap(Method method, Object[] validationCandidates) {
        Map<ValueFieldNamePair, Set<Annotation>> objectToValidate = new HashMap<>();
        List<Object> objectsToValidate = getValidatedObjects(method, validationCandidates);

        for (Object o : objectsToValidate) {
            for (Field field : o.getClass().getDeclaredFields()) {
                Object validatedValue = null;
                ValueFieldNamePair valueFieldNamePair = null;

                for (Annotation fieldAnnotation : field.getAnnotations()) {
                    if (!validatorRegistry.isValidationType(fieldAnnotation))
                        continue;
                    if (validatedValue == null) {
                        field.trySetAccessible();
                        validatedValue = field.get(o);
                        valueFieldNamePair = new ValueFieldNamePair(validatedValue, field.getName());
                    }

                    objectToValidate.computeIfPresent(valueFieldNamePair,
                            (key, annotations) -> {
                                annotations.add(fieldAnnotation);
                                return annotations;
                            });
                    objectToValidate.putIfAbsent(valueFieldNamePair, new HashSet<>(Set.of(fieldAnnotation)));
                }
            }
        }
        return objectToValidate;
    }

    private record ValueFieldNamePair(Object value, String fieldName) {
    }

    private List<Object> getValidatedObjects(Method method, Object[] validationCandidates) {
        List<Object> objectsToValidate = new ArrayList<>();
        for (int i = 0; i < validationCandidates.length; i++) {
            boolean valid = Arrays.stream(method.getParameters()[i]
                            .getAnnotations())
                    .anyMatch(Valid.class::isInstance);
            if (valid)
                objectsToValidate.add(validationCandidates[i]);
        }
        return objectsToValidate;
    }

    private boolean isValidatable(Method method) {
        return Arrays.stream(method.getParameterAnnotations())
                .flatMap(Arrays::stream)
                .anyMatch(Valid.class::isInstance);
    }
}
