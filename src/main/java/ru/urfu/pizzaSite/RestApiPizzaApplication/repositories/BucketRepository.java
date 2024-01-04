package ru.urfu.pizzaSite.RestApiPizzaApplication.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.Bucket.Bucket;

import java.util.Optional;

@Repository
public interface BucketRepository extends JpaRepository<Bucket, Integer> {

    Optional<Bucket> findById (int id);
}
