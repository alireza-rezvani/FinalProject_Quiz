package ir.maktab.arf.quiz.dto;

import lombok.Data;


/**
 * transfers account attributes
 * @author Alireza
 */

@Data
public class EditAccountDto {
    private Long id;
    private String nationalCode;
    private String firstName;
    private String lastName;
    private String mobileNumber;
    private String email;
    private String username;
    private String roleTitleName;
    private String statusTitleName;
    // TODO: 2/27/2020 multiple roles
    // TODO: 2/27/2020 privilege management for each account
}
