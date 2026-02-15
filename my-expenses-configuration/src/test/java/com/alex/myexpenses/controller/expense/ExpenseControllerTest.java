package com.alex.myexpenses.controller.expense;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.alex.myexpenses.MyExpensesApplication;
import com.alex.myexpenses.dto.expense.ExpenseSimpleDTO;
import com.alex.myexpenses.dto.user.JwtTokenUtil;
import com.alex.myexpenses.service.expense.ExpenseService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ExpenseController.class)
@ContextConfiguration(classes = MyExpensesApplication.class)
public class ExpenseControllerTest {
	
	@Autowired
    private MockMvc mockMvc;
	
	@MockBean
    private JwtTokenUtil jwtTokenUtil;
	
	@MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private ExpenseService expenseService; 

    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    @WithMockUser
    @DisplayName("Should return 400 Bad Request when amount is negative")
    void updateExpense_WhenAmountIsNegative_ShouldReturnBadRequest() throws Exception {
    	//Given
    	ExpenseSimpleDTO invalidDto = new ExpenseSimpleDTO();
    	invalidDto.setId(1L);
        invalidDto.setName("Cena");
        invalidDto.setAmount(-10.0);
        
        //WHEN & THEN
        mockMvc.perform(put("/expense/update")
        		.with(csrf())
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(invalidDto)))
        .andExpect(status().isBadRequest());
    }
    
    @Test
    @WithMockUser
    @DisplayName("Should return a specific error message for negative amount")
    void updateExpese_ValidationDetails() throws Exception {
    	//Given
    	ExpenseSimpleDTO invalidDto = new ExpenseSimpleDTO();
    	invalidDto.setId(1L);
        invalidDto.setName("Cena");
        invalidDto.setAmount(-10.0);
        
        //WHEN then
        mockMvc.perform(put("/expense/update")
        		.with(csrf())
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(objectMapper.writeValueAsString(invalidDto)))
        .andExpect(status().isBadRequest())
        
        //Check what we have into JSON
        .andExpect(jsonPath("$.message").value("Validation Error"))
        .andExpect(jsonPath("$.fieldErrors.amount").value("Amount must be positive"));
    }

}
