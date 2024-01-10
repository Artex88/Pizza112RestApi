package ru.urfu.pizzaSite.RestApiPizzaApplication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
@Schema(description = "Сущность, в которой указываются подробности заказа при отправке.")
public class OrderConfirmDTO {

    @Column(name = "address")
    @NotEmpty
    private String address;

    @Column(name = "pay_type")
    @NotEmpty
    private String payType;

    public OrderConfirmDTO() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }
}
