package com.techres.techresfacebook.repository;

import com.techres.techresfacebook.domain.Conversation;
import io.vavr.control.Try;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation,Long> {

    Try<Conversation> findByClientId(Long id);

}
