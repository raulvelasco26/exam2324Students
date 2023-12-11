package cat.tecnocampus.notes2324;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
// TODO 5.0: in this exercise you need to implement two tests

@SpringBootTest
@AutoConfigureMockMvc
public class TODO5tests {
    @Autowired
    private MockMvc mockMvc;

    //TODO 5.1: implement the happy path test for getting the user with id 1
    @Test
    void getOneExistingUser() throws Exception {
        assertTrue(false); // delete this line when you implement the test
    }

    //TODO 5.2: implement the test for getting a non existing user. User with id 100
    @Test
    void getOneNonExistingUser() throws Exception {
        assertTrue(false); // delete this line when you implement the test
    }
}
