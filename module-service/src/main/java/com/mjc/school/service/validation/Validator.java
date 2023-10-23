package com.mjc.school.service.validation;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.StringJoiner;

public interface Validator<T, A extends Annotation> {
    //set of violations
    Set<String> validate(Object testedValue, Annotation constraintAnnotation, String parameterName);

    Class<A> getConstraintType();

    static String formErrorMessage(Set<String> violations) {
        StringJoiner message = new StringJoiner("; ");
        violations.forEach(message::add);
        return message.toString();
    }

}
