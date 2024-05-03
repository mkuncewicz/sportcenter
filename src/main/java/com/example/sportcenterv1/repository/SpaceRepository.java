package com.example.sportcenterv1.repository;

import com.example.sportcenterv1.entity.space.Space;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpaceRepository extends JpaRepository<Space,Long> {
    @Query(value = "SELECT * FROM spaces WHERE space_type LIKE :spaceType", nativeQuery = true)
    List<Space> findSpacesByType(@Param("spaceType") String spaceType);
}
