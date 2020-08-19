package com.techres.techresfacebook.repository;

import com.techres.techresfacebook.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat,Long> {

    List<Chat> findAllByConversationId(Long conversationId);

}
