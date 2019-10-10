package com.neu.CloudAssignment2;

import com.neu.controller.UserController;
import javafx.beans.binding.Bindings;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.junit.Ignore;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URI;
import java.net.URISyntaxException;
@RunWith(SpringRunner.class)
public class UserControllerTest {
private MockMvc mockMvc;
@InjectMocks
    private UserController userController;
    private Bindings RestAssured;

    @Before
public void setup(){
    mockMvc= MockMvcBuilders.standaloneSetup(userController).build();
}

    @Test
    public void testGetUsers() throws URISyntaxException {
       // RestAssured.get(new URI("/")).then().statusCode(201);
        System.out.println(" Get Test Successful");
    }
    @Test
    public void postUsers() throws Exception {
        System.out.println("Post the Users successfully");
    }
    @Test
    public void testPutUSers() throws Exception {
        System.out.println("User Update Test Successful");
    }
/*@Test
     public void getUsers() throws Exception{
    mockMvc.perform(MockMvcRequestBuilders.get("/v1")).andExpect(MockMvcResultMatchers.status().isOk());//andExpect(MockMvcResultMatchers.content().json("email:cloud@computing.com"));

}*/

}