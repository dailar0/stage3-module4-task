package com.mjc.school.service.validation;

import com.mjc.school.repository.annotation.validation.Length;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

@Component
public class StringLengthValidatorImpl implements Validator<String, Length> {
    private static final String minMessage = "field %s should have length higher or equal than %d.";
    private static final String maxMessage = "field %s should have length from %d.";

    @Override
    public Set<String> validate(Object value, @NonNull Annotation length,String fieldName) {
        HashSet<String> violations = new HashSet<>();

        if (value == null)
            return violations;

        String string;
        Length castedLength;
        try {
            string = (String) value;
            castedLength = (Length) length;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        int max = castedLength.max();
        int min = castedLength.min();

        if (string.length()<min)
            violations.add(String.format(minMessage,fieldName, min));
        if (string.length() > max)
            violations.add(String.format(maxMessage,fieldName, max));

        return violations;
    }


    @Override
    public Class<Length> getConstraintType() {
        return Length.class;
    }
}
