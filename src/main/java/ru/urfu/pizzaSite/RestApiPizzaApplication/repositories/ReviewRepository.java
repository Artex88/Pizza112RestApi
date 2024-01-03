package ru.urfu.pizzaSite.RestApiPizzaApplication.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.Review;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer>, PagingAndSortingRepository<Review, Integer> {

    @Query(value = "SELECT * FROM review ORDER BY RANDOM() LIMIT 5", nativeQuery = true)
    List<Review> findRandomReviews();
}
