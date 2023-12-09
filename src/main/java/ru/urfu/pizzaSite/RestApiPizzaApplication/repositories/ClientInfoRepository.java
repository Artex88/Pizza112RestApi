package ru.urfu.pizzaSite.RestApiPizzaApplication.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.ClientInfo;

import java.util.Optional;
@Repository
public interface ClientInfoRepository extends JpaRepository<ClientInfo, Integer> {
    Optional<ClientInfo> findByClientId (int clientId);
    Optional<ClientInfo> findByPhoneNumber(String phoneNumber);
}
