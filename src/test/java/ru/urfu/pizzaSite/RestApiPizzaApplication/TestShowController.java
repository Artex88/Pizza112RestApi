package ru.urfu.pizzaSite.RestApiPizzaApplication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.urfu.pizzaSite.RestApiPizzaApplication.controllers.ShowController;
import ru.urfu.pizzaSite.RestApiPizzaApplication.dto.ProductDTOs.ProductDTO;
import ru.urfu.pizzaSite.RestApiPizzaApplication.dto.ProductDTOs.ShowDTO;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class TestShowController {

    private final ShowController showController;

    @Autowired
    public TestShowController(ShowController showController) {
        this.showController = showController;
    }

    @Test
    public void testWrongFrom() throws JsonProcessingException {
        String json = "{\"from\": \"ELECTRONICS\", \"count\": 2, \"sort\": \"id\"}";
        ShowDTO showDTO = new ObjectMapper().readValue(json, ShowDTO.class);
        ResponseEntity<List<ProductDTO>> response = showController.showProducts(showDTO, null);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
