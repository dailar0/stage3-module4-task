package com.mjc.school.service.aspect;

import com.mjc.school.repository.annotation.validation.Valid;
import com.mjc.school.service.exception.ValidationException;
import com.mjc.school.service.validation.ValidatableEntity;
import com.mjc.school.service.validation.ValidationContext;
import com.mjc.school.service.validation.Validator;
import com.mjc.school.service.validation.util.ValidatorRegistry;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Aspect
@Component
@Order(0)
@RequiredArgsConstructor
public class newValidationAspect {
    private final ValidatorRegistry validatorRegistry;

    @Before("within(com.mjc.school.repository.dao..*) && execution(* *(.., @com.mjc.school.repository.annotation.validation.Valid (*), ..))")
    public void validate(JoinPoint joinPoint) {


        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Object[] args = joinPoint.getArgs();
        List<Object> validatableArgs = filterArgs(method, args);
        List<ValidatableEntity> entities = createValidationEntities(validatableArgs);
        for (ValidatableEntity entity : entities) {
            Validator<?, ? extends Annotation> validator = validatorRegistry.resolve(entity.annotation());
            Set<String> validate = validator.validate(entity.context().value(),
                    entity.annotation(),
                    entity.context().field().getName());
            if (!validate.isEmpty()) {
                throw new ValidationException(Validator.formErrorMessage(validate));
            }
        }
    }

    private List<ValidatableEntity> createValidationEntities(List<Object> validatableArgs) {
        List<ValidationContext> contexts = new ArrayList<>();
        for (Object validatableArg : validatableArgs) {
            List<ValidationContext> currentFilteredEntities = Arrays.stream(validatableArg.getClass().getDeclaredFields())
                    .filter(field ->
                            Arrays.stream(field.getDeclaredAnnotations())
                                    .anyMatch(validatorRegistry::isValidationType))
                    .map(field -> new ValidationContext(extractFieldValue(field, validatableArg), field))
                    .toList();
            contexts.addAll(currentFilteredEntities);
        }
        return contexts.stream()
                .flatMap(context -> Arrays.stream(context.field().getDeclaredAnnotations())
                                            .map(annotation -> new ValidatableEntity(context, annotation)))
                .collect(Collectors.toList());

    }

    private List<Object> filterArgs(Method method, Object[] args) {
        List<Object> validAnnotatedObjects = new ArrayList<>();
        for (int i = 0; i < method.getParameters().length; i++) {
            boolean valid = Arrays.stream(method.getParameters()[i]
                            .getAnnotations())
                    .anyMatch(Valid.class::isInstance);
            if (valid)
                validAnnotatedObjects.add(args[i]);
        }
        return validAnnotatedObjects;
    }

    @SneakyThrows
    private Object extractFieldValue(Field field, Object entity) {
        field.trySetAccessible();
        return field.get(entity);
    }
}
