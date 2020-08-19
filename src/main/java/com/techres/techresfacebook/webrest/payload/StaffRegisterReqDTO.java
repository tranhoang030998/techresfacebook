package com.techres.techresfacebook.webrest.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaffRegisterReqDTO {

    private String displayName;
    private String nationality;

    private String email;
    private String password;

}
