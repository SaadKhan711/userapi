package com.example.userapi.validation;

import com.example.userapi.dto.request.SignupRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

@Component
public class UserValidator implements Validator {

    private static final String EMAIL_PATTERN = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
    private static final String USERNAME_PATTERN = "^[a-zA-Z0-9]*$";

    @Override
    public boolean supports(Class<?> clazz) {
        return SignupRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SignupRequest request = (SignupRequest) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "username.empty", "Username is required");
        if (request.getUsername().length() < 3 || request.getUsername().length() > 20) {
            errors.rejectValue("username", "username.size", "Username must be between 3 and 20 characters");
        }
        if (!Pattern.matches(USERNAME_PATTERN, request.getUsername())) {
            errors.rejectValue("username", "username.invalid", "Username can only contain letters and numbers");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "email.empty", "Email is required");
        if (!Pattern.matches(EMAIL_PATTERN, request.getEmail())) {
            errors.rejectValue("email", "email.invalid", "Invalid email format");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "password.empty", "Password is required");
        if (!Pattern.matches(PASSWORD_PATTERN, request.getPassword())) {
            errors.rejectValue("password", "password.weak", "Password must be at least 8 chars, contain 1 uppercase, 1 lowercase, 1 number, and 1 special char");
        }
    }
}