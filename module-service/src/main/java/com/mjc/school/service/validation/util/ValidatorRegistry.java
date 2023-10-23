package com.mjc.school.service.validation.util;

import com.mjc.school.service.validation.Validator;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.*;

@Component
public class ValidatorRegistry {
    private final Map<Class<? extends Annotation>, Validator<?, ? extends Annotation>> validatorsMap;

    public ValidatorRegistry(List<Validator<?, ? extends Annotation>> validators) {
        Map<Class<? extends Annotation>, Validator<?, ? extends Annotation>> map = new HashMap<>();
        validators
                .forEach(validator -> map.put(validator.getConstraintType(), validator));
        validatorsMap = Collections.unmodifiableMap(map);
    }

    public Validator<?, ? extends Annotation> resolve(Annotation annotation) {
        Set<Map.Entry<Class<? extends Annotation>, Validator<?, ? extends Annotation>>> entrySet = validatorsMap.entrySet();
        for (Map.Entry<Class<? extends Annotation>, Validator<?, ? extends Annotation>> entry : entrySet) {
            if (entry.getKey().isInstance(annotation)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public boolean isValidationType(Annotation testedAnnotation) {
        return validatorsMap.keySet()
                .stream()
                .anyMatch(validationAnnotation -> validationAnnotation.isInstance(testedAnnotation));
    }
}
