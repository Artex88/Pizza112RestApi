package ru.urfu.pizzaSite.RestApiPizzaApplication.model.Pizza;

import jakarta.persistence.*;

@Entity
public class PizzaIngredient {

    @EmbeddedId
    private PizzaIngredientKey pizzaIngredientKey = new PizzaIngredientKey();

    @ManyToOne()
    @MapsId("pizzaId")
    @JoinColumn(name = "pizza_id")
    private CustomPizza customPizza;

    @ManyToOne
    @MapsId("ingredientId")
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    private int count;

    public PizzaIngredient() {
    }

    public PizzaIngredientKey getPizzaIngredientKey() {
        return pizzaIngredientKey;
    }

    public void setPizzaIngredientKey(PizzaIngredientKey pizzaIngredientKey) {
        this.pizzaIngredientKey = pizzaIngredientKey;
    }

    public CustomPizza getCustomPizza() {
        return customPizza;
    }

    public void setCustomPizza(CustomPizza customPizza) {
        this.customPizza = customPizza;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
