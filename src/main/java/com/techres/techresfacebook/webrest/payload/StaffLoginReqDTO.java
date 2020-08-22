package com.techres.techresfacebook.webrest.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaffLoginReqDTO {

    private String email;
    private String password;

}
