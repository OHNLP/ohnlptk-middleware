package org.ohnlp.ohnlptk.repositories;

import org.ohnlp.ohnlptk.entities.authorities.AuthorityGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityGroupRepository extends JpaRepository<AuthorityGroup, Long> {
    AuthorityGroup getAuthorityGroupByName(String name);
}
