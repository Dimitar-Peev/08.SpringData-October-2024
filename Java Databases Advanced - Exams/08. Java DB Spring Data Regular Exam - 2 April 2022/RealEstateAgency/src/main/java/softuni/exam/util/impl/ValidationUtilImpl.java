package softuni.exam.util.impl;

import org.springframework.stereotype.Component;
import softuni.exam.util.ValidationUtil;

import jakarta.validation.Validation;
import jakarta.validation.Validator;

@Component
public class ValidationUtilImpl implements ValidationUtil {

    private final Validator validator;

    public ValidationUtilImpl() {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Override
    public <T> boolean isValid(T entity) {
        return this.validator.validate(entity).isEmpty();
    }

}
