package com.nuitblanche.whatdidyou.domain.user;

import javax.persistence.EntityManager;

public interface UserCustomRepository {

    EntityManager getEntityManager();

    void setEntityManager(EntityManager entityManager);
}
