package ru.urfu.pizzaSite.RestApiPizzaApplication.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.Review;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer>, PagingAndSortingRepository<Review, Integer> {

    Page<Review> findAll (Pageable pageable);
}
