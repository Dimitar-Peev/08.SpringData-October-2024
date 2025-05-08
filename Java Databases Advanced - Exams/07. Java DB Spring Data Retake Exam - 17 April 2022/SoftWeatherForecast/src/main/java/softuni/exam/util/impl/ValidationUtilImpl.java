package softuni.exam.util.impl;

import org.springframework.stereotype.Component;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import softuni.exam.util.ValidationUtil;

@Component
public class ValidationUtilImpl implements ValidationUtil {

    private final Validator validator;

    public ValidationUtilImpl() {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Override
    public <E> boolean isValid(E entity) {
        return this.validator.validate(entity).isEmpty();
    }
}