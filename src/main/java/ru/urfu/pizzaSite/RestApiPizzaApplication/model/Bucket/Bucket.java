package ru.urfu.pizzaSite.RestApiPizzaApplication.model.Bucket;

import jakarta.persistence.*;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.Client.Client;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "bucket")
public class Bucket {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne()
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;

    @Column(name = "created_at")
    private LocalDateTime createdTime;

    @Column
    private String status;

    @OneToMany(mappedBy = "bucket", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private Set<BucketItem> bucketItemSet;

    public Bucket(){

    }

    public Bucket(Client client, LocalDateTime createdTime, HashSet<BucketItem> bucketItemSet, String status) {
        this.client = client;
        this.createdTime = createdTime;
        this.bucketItemSet = bucketItemSet;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
