package com.techres.techresfacebook.repository;

import com.techres.techresfacebook.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client,Long> {

    Client findByCredentialPlatform(String credentialPlatform);

}
