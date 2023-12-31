package ru.urfu.pizzaSite.RestApiPizzaApplication.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.Bucket.Bucket;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.Bucket.BucketItem;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.Product.ProductVariant;

@Repository
public interface BucketItemRepository extends JpaRepository<BucketItem, Integer> {
    @Modifying
    @Query("UPDATE BucketItem SET quantity = quantity + :newQuantity where productVariant = :productVariant and bucket = :bucket")
    void updatePlusBucketItemByQuantity(@Param("productVariant") ProductVariant productVariant, @Param("bucket") Bucket bucket, int newQuantity);
    @Modifying
    @Query("UPDATE BucketItem SET quantity = quantity - 1 where productVariant = :productVariant and bucket = :bucket")
    void updateMinusBucketItemByQuantity(@Param("productVariant") ProductVariant productVariant, @Param("bucket") Bucket bucket);



}
