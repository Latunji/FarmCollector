package com.api.farmcollector.repository;

import com.api.farmcollector.model.Farm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FarmRepository extends JpaRepository<Farm, Long> {
}
