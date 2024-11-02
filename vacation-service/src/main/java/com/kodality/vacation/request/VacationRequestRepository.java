package com.kodality.vacation.request;

import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@Singleton
public class VacationRequestRepository {

  @PersistenceContext
  private EntityManager entityManager;

  @Transactional
  public List<VacationRequest> getVacationRequests() {
    return entityManager.createQuery("SELECT vr FROM VacationRequest vr", VacationRequest.class).getResultList();
  }

}
