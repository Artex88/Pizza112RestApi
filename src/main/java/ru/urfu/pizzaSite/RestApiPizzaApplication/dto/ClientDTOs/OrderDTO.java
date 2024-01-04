package ru.urfu.pizzaSite.RestApiPizzaApplication.dto.ClientDTOs;

import java.time.LocalDateTime;

public class OrderDTO {

    private LocalDateTime date;
    private int orderId;
    private double sum;

    public OrderDTO(LocalDateTime date, int orderId, double sum) {
        this.date = date;
        this.orderId = orderId;
        this.sum = sum;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }
}
