package com.nttdata.accountservice.transactions.infrastructure.jpa;


import com.nttdata.accountservice.accounts.infrastructure.jpa.AccountEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepo extends CrudRepository<TransactionEntity, UUID> {
    Optional<TransactionEntity> findById(UUID id);

    @Query("SELECT t FROM TransactionEntity t " +
            "WHERE t.account.number= :number " +
            "AND t.account.customerIdentification = :identification " +
            "ORDER BY t.ordinal DESC LIMIT 1")
    TransactionEntity findTopByAccountCustomerPersonIdentificationOrderByOrdinalDesc(
            @Param("identification") String identification, @Param("number") String number);

    List<TransactionEntity> findByAccountAndDateBetween(AccountEntity account, LocalDateTime from, LocalDateTime to);
}


