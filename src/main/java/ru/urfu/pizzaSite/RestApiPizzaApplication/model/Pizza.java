package ru.urfu.pizzaSite.RestApiPizzaApplication.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pizza")
public class Pizza {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne()
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;


    @ManyToOne()
    @JoinColumn(name = "variant_name", referencedColumnName = "name")
    private PizzaVariant pizzaVariant;

    public Pizza() {
    }

    public PizzaVariant getPizzaVariant() {
        return pizzaVariant;
    }

    public void setPizzaVariant(PizzaVariant pizzaVariant) {
        this.pizzaVariant = pizzaVariant;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
