package com.eros.userorderapi.security;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = true)
class OrderSecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void adminCanAccessAdminEndpoint() throws Exception {
        mockMvc.perform(get("/orders")
                .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk());
    }

    @Test
    void userCannotAccessAdminEndpoint() throws Exception {
        mockMvc.perform(get("/orders")
                .with(user("user").roles("USER")))
                .andExpect(status().isForbidden());
    }

    @Test
    void anonymousCannotAccessProtectedEndpoint() throws Exception {
        mockMvc.perform(get("/orders"))
                .andExpect(status().isUnauthorized());
    }
}
