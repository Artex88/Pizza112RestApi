package ru.urfu.pizzaSite.RestApiPizzaApplication.model.Pizza;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "size")
public class Size {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "weight")
    private Double weight;

    @Column(name = "price")
    private Double price;

    @OneToMany(mappedBy = "size")
    private List<CustomPizza> customPizzaList;

    public Size() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<CustomPizza> getCustomPizzaList() {
        return customPizzaList;
    }

    public void setCustomPizzaList(List<CustomPizza> customPizzaList) {
        this.customPizzaList = customPizzaList;
    }
}
