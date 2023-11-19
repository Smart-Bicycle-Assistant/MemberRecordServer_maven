package com.sba.recordingserver.repository;

import com.sba.recordingserver.entity.RidingSpeed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RidingSpeedRepository extends JpaRepository<RidingSpeed,String> {
}
