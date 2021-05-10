package org.ohnlp.ohnlptk.repositories;

import org.ohnlp.ohnlptk.entities.APIKey;
import org.ohnlp.ohnlptk.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface APIKeyRepository extends JpaRepository<APIKey, String> {
    Collection<APIKey> findAPIKeysByUser(User user);
    APIKey findAPIKeyByToken(String value);
}
