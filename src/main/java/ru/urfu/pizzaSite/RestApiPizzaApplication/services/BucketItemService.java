package ru.urfu.pizzaSite.RestApiPizzaApplication.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.*;
import ru.urfu.pizzaSite.RestApiPizzaApplication.repositories.BucketItemRepository;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.exceptions.NotFoundException;

import java.util.Objects;
import java.util.Optional;

@Service
public class BucketItemService {

    private final BucketItemRepository bucketItemRepository;

    @Autowired
    public BucketItemService(BucketItemRepository bucketItemRepository) {
        this.bucketItemRepository = bucketItemRepository;
    }

    @Transactional
    public void save(BucketItem bucketItem){
        bucketItemRepository.save(bucketItem);
    }

    @Transactional
    public void increaseQuantityWithProductVariant(ProductVariant productVariant, Bucket bucket, int quantity){
        bucketItemRepository.updatePlusBucketItemByQuantity(productVariant, bucket, quantity);
    }

    @Transactional
    public void decreaseQuantityByOneWithProductVariant(ProductVariant productVariant, Bucket bucket){
        bucketItemRepository.updateMinusBucketItemByQuantity(productVariant, bucket);
    }

    @Transactional
    public void addNewBucketItem(Bucket bucket, Product product, ProductVariant productVariant, int quantity) {
        BucketItem bucketItem = new BucketItem(quantity, bucket, product, productVariant);
        this.save(bucketItem);
        bucket.getBucketItemSet().add(bucketItem);
    }
@Transactional(readOnly = true)
    public boolean bucketItemExists(Bucket bucket, Product product, ProductVariant productVariant) {
        return bucket.getBucketItemSet().stream().anyMatch(bucketItem ->
                Objects.equals(bucketItem.getProduct(), product) &&
                        Objects.equals(bucketItem.getProductVariant(), productVariant) && Objects.equals(bucketItem.getBucket(), bucket));
    }
    @Transactional
    public void updateAddIncreaseBucketItem(ProductVariant productVariant, Product product, Bucket bucket, int quantity) {
        if (this.bucketItemExists(bucket, product, productVariant))
            this.increaseQuantityWithProductVariant(productVariant, bucket, quantity);
        else
            this.addNewBucketItem(bucket, product,productVariant, quantity);
    }
    @Transactional
    public void updateDeleteOrDecreaseBucketItem(Bucket bucket, Product product, ProductVariant productVariant) {
            Optional<BucketItem> g = bucket.getBucketItemSet().stream().filter(bucketItem -> bucketItem.getProductVariant() == productVariant && bucketItem.getProduct() == product && bucketItem.getBucket() == bucket).findFirst();
            if (g.isEmpty())
                throw new NotFoundException("There is no such product in the cart");
            else {
                BucketItem bucketItem = g.get();
                if (bucketItem.getQuantity() == 1)
                {
                    bucket.getBucketItemSet().remove(bucketItem);;
                }
                else
                    this.decreaseQuantityByOneWithProductVariant(productVariant, bucket);
            }
        }
}
