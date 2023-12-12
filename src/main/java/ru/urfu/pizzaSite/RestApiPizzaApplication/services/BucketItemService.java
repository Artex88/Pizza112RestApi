package ru.urfu.pizzaSite.RestApiPizzaApplication.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.urfu.pizzaSite.RestApiPizzaApplication.dto.BucketItemAddDTO;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.*;
import ru.urfu.pizzaSite.RestApiPizzaApplication.repositories.BucketItemRepository;

import java.util.Objects;

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
    public void increaseQuantityByOneWithProductVariant(ProductVariant productVariant, Bucket bucket){
        bucketItemRepository.updateBucketItemByQuantity(productVariant, bucket);
    }

    @Transactional
    public void increaseQuantityByOneWithoutProductVariant(Product product, Bucket bucket){
        bucketItemRepository.updateBucketItemByQuantity(product, bucket);
    }

    @Transactional
    public void addNewBucketItem(BucketItemAddDTO bucketItemAddDTO, Client client, Product product, ProductVariant productVariant) {
        BucketItem bucketItem = new BucketItem(bucketItemAddDTO.getQuantity(), client.getBucket(), product, productVariant);
        this.save(bucketItem);
        client.getBucket().getBucketItemSet().add(bucketItem);
    }

    public boolean bucketItemExists(Bucket bucket, Product product, ProductVariant productVariant) {
        return bucket.getBucketItemSet().stream().anyMatch(buketItem ->
                Objects.equals(buketItem.getProduct(), product) &&
                        Objects.equals(buketItem.getProductVariant(), productVariant));
    }
@Transactional
    public void updateBucketItemByProductVariant(ProductVariant productVariant, BucketItemAddDTO bucketItemAddDTO, Product product, Client client) {
        if (this.bucketItemExists(client.getBucket(), product, productVariant))
            this.increaseQuantityByOneWithProductVariant(productVariant, client.getBucket());
        else
            this.addNewBucketItem(bucketItemAddDTO, client, product,productVariant);
    }

    @Transactional
    public void updateBucketItemByProduct(BucketItemAddDTO bucketItemAddDTO, Client client, Product product) {
        if (this.bucketItemExists(client.getBucket(), product, null)){
            this.increaseQuantityByOneWithoutProductVariant(product, client.getBucket());
        }
        else
            this.addNewBucketItem(bucketItemAddDTO, client, product,null);
    }

}
