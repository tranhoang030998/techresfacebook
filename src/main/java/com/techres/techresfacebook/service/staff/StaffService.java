package com.techres.techresfacebook.service.staff;

import com.techres.techresfacebook.domain.Staff;
import com.techres.techresfacebook.webrest.payload.StaffJoinConversationReqDTO;
import com.techres.techresfacebook.webrest.payload.StaffLoginReqDTO;
import com.techres.techresfacebook.webrest.payload.StaffRegisterReqDTO;
import io.vavr.control.Try;

public interface StaffService {

    Try<Boolean> register(StaffRegisterReqDTO dto);

    Try<Staff> login(StaffLoginReqDTO dto);

    Try<Boolean> joinConversation(StaffJoinConversationReqDTO dto);

}
