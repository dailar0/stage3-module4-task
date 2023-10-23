package com.mjc.school.service.validation;

import java.lang.reflect.Field;

public record ValidationContext(Object value, Field field) {
}
