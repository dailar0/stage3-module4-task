package com.mjc.school.service.validation;

import java.lang.annotation.Annotation;

public record ValidatableEntity(ValidationContext context, Annotation annotation) {
}
