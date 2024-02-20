package roommate.domain.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import roommate.domain.model.Timespan;

public class TimespanValidator implements ConstraintValidator<ValidTimespan, Timespan> {
    @Override
    public void initialize(ValidTimespan constraintAnnotation) {
    }

    @Override
    public boolean isValid(Timespan timespan, ConstraintValidatorContext context) {
        if (timespan.startTime() == null || timespan.endTime() == null) {
            return false;
        }
        return timespan.endTime().isAfter(timespan.startTime());
    }
}
