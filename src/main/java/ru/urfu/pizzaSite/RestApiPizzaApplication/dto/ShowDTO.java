package ru.urfu.pizzaSite.RestApiPizzaApplication.dto;

import ru.urfu.pizzaSite.RestApiPizzaApplication.util.ProductTypes;

public class ShowDTO {

    private ProductTypes from;

    private int count;

    private String sort;

    public ShowDTO() {
    }

    public ProductTypes getFrom() {
        return from;
    }

    public void setFrom(ProductTypes from) {
        this.from = from;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
