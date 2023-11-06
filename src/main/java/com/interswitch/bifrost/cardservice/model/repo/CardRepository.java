package com.interswitch.bifrost.cardservice.model.repo;

import com.interswitch.bifrost.cardservice.model.CardAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository  extends JpaRepository<CardAudit, Long> {
}
