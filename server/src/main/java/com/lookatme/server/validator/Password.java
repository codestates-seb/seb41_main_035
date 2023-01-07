package com.lookatme.server.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {PasswordValidator.class})
public @interface Password {
    String message() default "비밀번호 형식에 맞지 않습니다 (6자 이상, 숫자 및 문자 포함)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
