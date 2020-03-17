package ir.maktab.arf.quiz.dto;

import lombok.Data;


/**
 * transfers user's required attributes for sign up
 * @author Alireza
 */

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
