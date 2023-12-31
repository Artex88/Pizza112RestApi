package ru.urfu.pizzaSite.RestApiPizzaApplication.services.Bucket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.Bucket.Bucket;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.Client.Client;
import ru.urfu.pizzaSite.RestApiPizzaApplication.repositories.BucketRepository;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.exceptions.NotFoundException;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;

@Service
public class BucketService {

    private final BucketRepository bucketRepository;
    @Autowired
    public BucketService(BucketRepository bucketRepository) {
        this.bucketRepository = bucketRepository;
    }
    @Transactional
    public void save(Bucket bucket){
        bucketRepository.save(bucket);
    }
    @Transactional
    public void createBucket(Client client) {
        Bucket bucket = new Bucket(client, LocalDateTime.now(), new HashSet<>(),true);
        this.save(bucket);
        client.getBucketList().add(bucket);
    }
    @Transactional
    public Bucket findById(int id){
        Optional<Bucket> optionalBucket = bucketRepository.findById(id);
        if (optionalBucket.isEmpty())
            throw new NotFoundException("Bucket with this id doesn't exist");
        else
            return bucketRepository.findById(id).get();
    }
}
