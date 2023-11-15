package com.sba.recordingserver.repository;

import com.sba.recordingserver.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.beans.JavaBean;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,String> {
    Optional<Member> findById(String Id);
    Optional<Member> deleteMemberById(String id);
}
