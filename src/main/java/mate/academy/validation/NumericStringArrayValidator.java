package mate.academy.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class NumericStringArrayValidator implements ConstraintValidator<ValidNumericStringArray,
        String[]> {
    @Override
    public void initialize(ValidNumericStringArray constraintAnnotation) {
    }

    @Override
    public boolean isValid(String[] value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true;
        }
        if (value.length != 2) {
            return false;
        }
        for (String str : value) {
            try {
                BigDecimal number = new BigDecimal(str);
                if (number.compareTo(BigDecimal.ZERO) < 0) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }
}
