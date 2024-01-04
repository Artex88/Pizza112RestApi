package ru.urfu.pizzaSite.RestApiPizzaApplication.services.Bucket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.Bucket.Bucket;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.Client.Client;
import ru.urfu.pizzaSite.RestApiPizzaApplication.repositories.BucketRepository;

import java.time.LocalDateTime;

@Service
public class BucketService {

    private final BucketRepository bucketRepository;
    @Autowired
    public BucketService(BucketRepository bucketRepository) {
        this.bucketRepository = bucketRepository;
    }

    public void save(Bucket bucket){
        bucketRepository.save(bucket);
    }

    public void createBucket(Client client) {
        Bucket bucket = new Bucket(client, LocalDateTime.now(), null,"ACTIVE");
        this.save(bucket);
        client.getBucketList().add(bucket);
    }
}
