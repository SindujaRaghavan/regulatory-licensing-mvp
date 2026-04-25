package com.gov.sg.licensing.repository;

import com.gov.sg.licensing.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    @Query("select distinct a from Application a left join fetch a.documents left join fetch a.feedbacks where a.id = :id")
    Optional<Application> findDetailedById(@Param("id") Long id);
}
