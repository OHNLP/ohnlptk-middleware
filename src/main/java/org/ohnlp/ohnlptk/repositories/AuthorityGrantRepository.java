package org.ohnlp.ohnlptk.repositories;

import org.ohnlp.ohnlptk.entities.authorities.AuthorityGrant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityGrantRepository extends JpaRepository<AuthorityGrant, Long> {
}
