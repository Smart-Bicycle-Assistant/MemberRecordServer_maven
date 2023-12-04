package com.sba.recordingserver.repository;

import com.sba.recordingserver.entity.RidingCoordinate;
import com.sba.recordingserver.entity.RidingLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RidingCoordinateRepository extends JpaRepository<RidingCoordinate,String> {
    @Override
    void deleteById(String s);
}
