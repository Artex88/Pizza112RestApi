package ru.urfu.pizzaSite.RestApiPizzaApplication.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.Client;

import java.util.Optional;
@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    Optional<Client> findByPhoneNumber(String phone_number);
    Optional<Client> findById (int id);
}
