package ru.urfu.pizzaSite.RestApiPizzaApplication.model.Pizza;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class PizzaIngredientKey implements Serializable {
    @Column(name = "pizza_id")
    private int pizzaId;

    @Column(name = "ingredient_id")
    private int ingredientId;

    public PizzaIngredientKey() {
    }

    public PizzaIngredientKey(int pizzaId, int ingredientId) {
        this.pizzaId = pizzaId;
        this.ingredientId = ingredientId;
    }

    public int getPizzaId() {
        return pizzaId;
    }

    public void setPizzaId(int pizzaId) {
        this.pizzaId = pizzaId;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PizzaIngredientKey that = (PizzaIngredientKey) o;

        if (pizzaId != that.pizzaId) return false;
        return ingredientId == that.ingredientId;
    }

    @Override
    public int hashCode() {
        int result = pizzaId;
        result = 31 * result + ingredientId;
        return result;
    }
}
