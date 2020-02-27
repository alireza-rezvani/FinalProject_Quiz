package ir.maktab.arf.quiz.dto;

import lombok.Data;

@Data
public class SignUpInfoDto {
    private String nationalCode;
    private String firstName;
    private String lastName;
    private String mobileNumber;
    private String email;
    private String username;
    private String password;
    private String passwordConfirm;
    private String roleTitleName;
}
