package ru.urfu.pizzaSite.RestApiPizzaApplication.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.Client.ClientInfo;

import java.util.Optional;
@Repository
public interface ClientInfoRepository extends JpaRepository<ClientInfo, Integer> {
    Optional<ClientInfo> findByClientId (int clientId);
    Optional<ClientInfo> findByPhoneNumber(String phoneNumber);
}
