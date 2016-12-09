package com.restdude.domain.confirmationtoken.repository;

import com.restdude.domain.base.repository.ModelRepository;
import com.restdude.domain.confirmationtoken.model.ConfirmationToken;
import org.springframework.data.jpa.repository.Query;

public interface ConfirmationTokenReposirory extends ModelRepository<ConfirmationToken, String> {

    @Query("select token from ConfirmationToken token where token.tokenValue = ?1 and token.targetId = ?2")
    ConfirmationToken findByTokenValueAndRTargetId(String tokenValue, String targetId);

    @Query("select token from ConfirmationToken token where token.targetId = ?1")
    ConfirmationToken findByTargetId(String targetId);

}
