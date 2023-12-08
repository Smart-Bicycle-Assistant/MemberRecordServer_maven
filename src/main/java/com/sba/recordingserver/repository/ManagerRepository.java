package com.sba.recordingserver.repository;

import com.sba.recordingserver.entity.Manager;
import com.sba.recordingserver.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ManagerRepository extends JpaRepository<Manager,String> {
    Optional<Manager> findById(String Id);
    Optional<Manager> deleteMemberById(String id);
}
