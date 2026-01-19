package com.example.userapi.validation;

import com.example.userapi.dto.ResultInputDto;
import com.example.userapi.dto.StudentRegistrationDto;
import com.example.userapi.dto.request.SignupRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

@Component
public class AppValidator implements Validator {

    private static final String EMAIL_PATTERN = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    private static final String PHONE_PATTERN = "^\\d{10}$";
    private static final String NAME_PATTERN = "^[a-zA-Z\\s]*$";
    private static final String USERNAME_PATTERN = "^[a-zA-Z0-9]*$";
    private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

    @Override
    public boolean supports(Class<?> clazz) {
        return StudentRegistrationDto.class.equals(clazz) 
            || ResultInputDto.class.equals(clazz)
            || SignupRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        
        if (target instanceof StudentRegistrationDto) {
            validateStudent((StudentRegistrationDto) target, errors);
        }
        
        else if (target instanceof ResultInputDto) {
            validateResult((ResultInputDto) target, errors);
        }

        else if (target instanceof SignupRequest) {
            validateUser((SignupRequest) target, errors);
        }
    }

    private void validateStudent(StudentRegistrationDto request, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fname", "field.required", "First name is required");
        if (!Pattern.matches(NAME_PATTERN, request.getFname())) {
            errors.rejectValue("fname", "field.invalid", "First name contains invalid characters");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lname", "field.required", "Last name is required");

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "field.required", "Email is required");
        if (!Pattern.matches(EMAIL_PATTERN, request.getEmail())) {
            errors.rejectValue("email", "field.invalid", "Invalid email format");
        }

        if (request.getContactNumber() != null && !Pattern.matches(PHONE_PATTERN, request.getContactNumber())) {
            errors.rejectValue("contactNumber", "field.invalid", "Phone number must be exactly 10 digits");
        }
        
        if (request.getGender() != null) {
            String g = request.getGender().toLowerCase();
            if (!g.equals("male") && !g.equals("female") && !g.equals("other")) {
                errors.rejectValue("gender", "field.invalid", "Gender must be Male, Female, or Other");
            }
        }
    }

    private void validateResult(ResultInputDto request, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "registrationId", "field.required", "Registration ID is required");
        if (request.getRegistrationId() != null && !request.getRegistrationId().startsWith("REG-")) {
            errors.rejectValue("registrationId", "field.invalid", "Registration ID must start with 'REG-'");
        }

        if (request.getSubjects() == null || request.getSubjects().isEmpty()) {
            errors.rejectValue("subjects", "field.required", "Subject list cannot be empty");
        } else {
            for (ResultInputDto.SubjectMarkDto subject : request.getSubjects()) {
                if (subject.getMarks() < 0 || subject.getMarks() > 100) {
                    errors.rejectValue("subjects", "field.invalid", 
                        "Marks for Subject ID " + subject.getSubjectId() + " must be between 0 and 100");
                    break;
                }
            }
        }
    }

    private void validateUser(SignupRequest request, Errors errors) {
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
            errors.rejectValue("password", "password.weak", "Password must be strong (8+ chars, 1 Up, 1 Low, 1 Num, 1 Special)");
        }
    }
}