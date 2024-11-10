package com.burgers.burgers.burger;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = BurgerController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class BurgerControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BurgerRepository burgerRepository;
    @MockBean
    private BurgerModelAssembler burgerModelAssembler;

    private Burger burger;

    @BeforeEach
    public void setup() {
        ArrayList<String> ingredientsTestBurger = new ArrayList<>();
        ingredientsTestBurger.addAll(Arrays.asList("cheddar", "steak"));
        burger = new Burger("testBurger", ingredientsTestBurger);
    }

    @Test
    public void burgerController_createBurger_returnCreatedBurger() throws Exception {
        given(burgerRepository.save(burger)).willReturn(burger);
        EntityModel<Burger> entityModel = EntityModel.of(burger,
            linkTo(BurgerController.class).slash(burger.getId()).withSelfRel());

        given(burgerModelAssembler.toModel(burger)).willReturn(entityModel);
        ResultActions response = mockMvc.perform(post("/burgers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(burger)));
        
        response.andExpect(MockMvcResultMatchers.status().isCreated());
    }
}
