package com.techres.techresfacebook.repository;

import com.techres.techresfacebook.domain.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<Staff,Long> {

    Staff findStaffByEmail(String email);

}
