package ru.urfu.pizzaSite.RestApiPizzaApplication.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "bucket")
public class Bucket {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne()
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;

    @Column(name = "created_at")
    private LocalDateTime createdTime;

    @OneToMany(mappedBy = "bucket")
    private Set<BucketItem> bucketItemSet;

    public Bucket(){

    }

    public Bucket(Client client, LocalDateTime createdTime, Set<BucketItem> bucketItemSet) {
        this.client = client;
        this.createdTime = createdTime;
        this.bucketItemSet = bucketItemSet;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public Set<BucketItem> getBucketItemSet() {
        return bucketItemSet;
    }

    public void setBucketItemSet(Set<BucketItem> bucketItemSet) {
        this.bucketItemSet = bucketItemSet;
    }
}
