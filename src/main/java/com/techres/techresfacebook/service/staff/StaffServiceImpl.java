package com.techres.techresfacebook.service.staff;

import com.techres.techresfacebook.domain.Participant;
import com.techres.techresfacebook.domain.Staff;
import com.techres.techresfacebook.domain.User;
import com.techres.techresfacebook.domain.enumration.Role;
import com.techres.techresfacebook.repository.ConversationRepository;
import com.techres.techresfacebook.repository.ParticipantRepository;
import com.techres.techresfacebook.repository.StaffRepository;
import com.techres.techresfacebook.repository.UserRepository;
import com.techres.techresfacebook.service.twilio.TwilioClientService;
import com.techres.techresfacebook.service.twilio.data.MemberCreateReqDTO;
import com.techres.techresfacebook.service.twilio.data.UserCreateReqDTO;
import com.techres.techresfacebook.webrest.payload.StaffJoinConversationReqDTO;
import com.techres.techresfacebook.webrest.payload.StaffLoginReqDTO;
import com.techres.techresfacebook.webrest.payload.StaffRegisterReqDTO;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.val;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class StaffServiceImpl implements StaffService {

    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ConversationRepository conversationRepository;
    @Autowired
    private ParticipantRepository participantRepository;
    @Autowired
    private TwilioClientService twilioClientService;

    // - - - - - - - - - - - - - -
    @Override
    public Try<Boolean> register(StaffRegisterReqDTO dto) {

        //TODO: Check duplicated email when register process
        if (staffRepository.findStaffByEmail(dto.getEmail()) != null){
            return Try.failure(new Exception("Email already registered"));
        }

        twilioClientService.initTwilioServiceInstance();
        val user = new User();
        user.setDisplayName(dto.getDisplayName());
        user.setNationality(dto.getNationality());
        user.setRole(Role.STAFF);
        userRepository.save(user);

        val staff = new Staff();
        staff.setEmail(dto.getEmail());
        staff.setPassword(dto.getPassword());
        staff.setUser(user);

        val result = staffRepository.save(staff);
        if (result instanceof Staff){

            val userCreate = new UserCreateReqDTO();
            userCreate.setIndentity(dto.getEmail());
            userCreate.setFriendlyName(dto.getDisplayName());

            return twilioClientService.createUSer(userCreate);
        }
        return Try.of(() -> false);
    }

    @Override
    public Try<Staff> login(StaffLoginReqDTO dto) {
        Staff staff = staffRepository.findStaffByEmail(dto.getEmail());
        if (staff == null){
            return Try.failure(new Exception("Email invalid"));
        }

        return (
                staff.getPassword().equals(dto.getPassword())
            ) ? Try.success(staff) : Try.failure(new Exception("Wrong password, please try again"));
    }

    @Override
    public Try<Boolean> joinConversation(StaffJoinConversationReqDTO dto) {
        val conversation = conversationRepository.findById(dto.getConversationId());
        val staffUserJoin = staffRepository.findById(dto.getStaffId());
        if ((!conversation.isPresent()) || (!staffUserJoin.isPresent())){
            return Try.failure(new Exception("Can find conversation or StaffUser"));
        }
        val participant = new Participant();
        participant.setTimeJoin(Instant.now());
        participant.setConversation(conversation.get());
        participant.setUser(staffUserJoin.get().getUser());
        participantRepository.save(participant);

        conversation.get().setHaveResponsePerson(true);
        conversationRepository.save(conversation.get());

        val dataMember = new MemberCreateReqDTO();
        dataMember.setChannelId("CHATBOX_"+conversation.get().getId());
        dataMember.setIdentity(staffUserJoin.get().getEmail());

        return twilioClientService.createMember(dataMember);

    }


}
