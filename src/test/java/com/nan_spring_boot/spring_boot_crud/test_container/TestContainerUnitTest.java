package com.nan_spring_boot.spring_boot_crud.test_container;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.Mock;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nan_spring_boot.spring_boot_crud.SpringBootCrudApplication;
import com.nan_spring_boot.spring_boot_crud.model.Gender;
import com.nan_spring_boot.spring_boot_crud.model.UserProfile;
import org.testcontainers.utility.DockerImageName;
import java.time.LocalDate;

@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SpringBootCrudApplication.class)
@AutoConfigureMockMvc
public class TestContainerUnitTest {

    public static final String API = "/api/v1/user";

    @Autowired
    private MockMvc mockMvc;

    @Container
    private static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:15"))
            .withUsername("admin")
            .withPassword("pass")
            .withInitScript("schema.sql");
        
    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
            registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
            registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
            registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }
        

    @Container
    private static GenericContainer<?> redisContainer = new GenericContainer<>(
            DockerImageName.parse("redis/redis-stack:latest"))
            .withExposedPorts(6379);
    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        String redisHost = redisContainer.getHost();
        Integer redisPort = redisContainer.getMappedPort(6379);
        registry.add("spring.data.redis.host", () -> redisHost);
        registry.add("spring.data.redis.port", () -> redisPort);
    }

    @Test
    @DisplayName("Check the connection for postgres-testcontainer")
    @Order(1)
    void testPostGresContainerIsRunning() {
        assertTrue(postgreSQLContainer.isCreated(), "Postgres is not Created");
        assertTrue(postgreSQLContainer.isRunning(), "Postgres is not running");
    }

    @Test
    @DisplayName("Check the connection for redis-testcontainer")
    @Order(2)
    void testRedisContainerIsRunning() {
        assertTrue(redisContainer.isCreated(), "Redis Container is not Created");
        assertTrue(redisContainer.isRunning(), "Redis Container is not running");
    }

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @AfterEach
    public void cleanUp() {
        jdbcTemplate.getJdbcTemplate().execute("DELETE FROM users_profile");
    }

    @Test
    @DisplayName("Test createTodo")
    @Order(3)
    public void testCreateTodo_whenValidTodo_returnCreatedSuccessfully() throws Exception {
    
        UserProfile userProfile = new UserProfile("John Doe", "Johndoe@email.com", Gender.MALE, "2024/06/11", "Lead", 0);
        
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(API + "/create")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userProfile));

    
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String responseBodyAsString = mvcResult.getResponse().getContentAsString();


        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertTrue(responseBodyAsString.contains("User created successfully"));
    
    }

    @ParameterizedTest
    @DisplayName("Test create user with invalid Birth Date")
    @ValueSource(strings = { "2024-06-11", "2024-2020-2123", "23021-23232" })
    @Order(4)
    public void testCreateTodo_whenRecordDateIsNull_returnBadRequest() throws Exception {
        
        UserProfile userProfile = new UserProfile("John Doe", "Johndoe@email.com", Gender.MALE, "2024-06-11", "Lead", 0);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(API + "/create")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userProfile));

       
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

       
        int status = mvcResult.getResponse().getStatus();
        assertEquals(HttpStatus.BAD_REQUEST.value(), status);
    }

    @Test
    @DisplayName("Test get user by id")
    @Order(5)
    public void testGetUserProfileById_whenValidId_returnUser() throws Exception {
        Integer insertedId = insertData();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(API + "/get/" + insertedId)
            .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String responseBodyAsString = mvcResult.getResponse().getContentAsString();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertTrue(responseBodyAsString.contains("John Doe"));
    }

    @Test
    @DisplayName("update user by id")
    @Order(6)
    public void testUpdateUserProfileById_whenValidId_returnUpdatedSuccessfully() throws Exception {
        Integer insertedId = insertData();

        UserProfile userProfileUpdate = new UserProfile("John Dovis", "Johndovis@email.com", Gender.MALE, "2024/06/11", "Lead", 0);
    
    
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put(API + "/update/" + insertedId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userProfileUpdate));

    
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();  
        String responseBodyAsString = mvcResult.getResponse().getContentAsString();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertTrue(responseBodyAsString.contains("User Profile updated successfully"));
    }

    @Test
    @DisplayName("delete user by id")
    @Order(7)   
    public void testDeleteUserProfileById_whenValidId_returnDeletedSuccessfully() throws Exception {
        Integer insertedId = insertData();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(API + "/delete/" + insertedId)
            .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String responseBodyAsString = mvcResult.getResponse().getContentAsString();

        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertTrue(responseBodyAsString.contains("User deleted successfully"));
    }


    public Integer insertData(){
        final String sql = """
            INSERT INTO users_profile (
            full_name, email_address, gender, birth_date, role_base, age
            ) VALUES (
            :name, :email_address, :gender, :birth_date, :role_base, :age
            )
            RETURNING id
        """;
        MapSqlParameterSource parameterSqlParameterSource = new MapSqlParameterSource()
            .addValue("name", "John Doe")
            .addValue("email_address", "johndoe@gmail.com")
            .addValue("gender", "MALE")
            .addValue("birth_date", LocalDate.of(2024, 6, 11))
            .addValue("role_base", "Lead")
            .addValue("age", 10);
        Integer id = jdbcTemplate.queryForObject(sql, parameterSqlParameterSource, Integer.class);
        return id;
    }
        
    
}
