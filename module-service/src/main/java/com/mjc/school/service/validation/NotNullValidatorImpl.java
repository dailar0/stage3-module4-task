package com.mjc.school.service.validation;

import com.mjc.school.repository.annotation.validation.NotNull;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;
@Component
public class NotNullValidatorImpl implements Validator<Object, NotNull> {
    private static final String errorMessage = "field %s shouldn't be null.";

    @Override
    public Set<String> validate(Object testedValue, Annotation notNullAnnotation, String fieldName) {
        HashSet<String> violations = new HashSet<>();
        if (testedValue == null)
            violations.add(String.format(errorMessage, fieldName));
        return violations;
    }

    @Override
    public Class<NotNull> getConstraintType() {
        return NotNull.class;
    }
}
