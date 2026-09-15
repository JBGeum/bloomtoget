package com.btg.e2e;

import com.btg.core.application.port.in.dailyprogress.GetDailyProgressUseCase;
import com.btg.core.application.port.in.dailyprogress.UpdateDailyProgressUseCase;
import com.btg.core.application.port.in.group.*;
import com.btg.core.application.port.in.task.*;
import com.btg.infrastructure.persistence.user.entity.UserJpaEntity;
import com.btg.infrastructure.persistence.user.repository.UserJpaRepository;
import com.btg.infrastructure.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DisplayName("User Controller E2E Tests (Real Server)")
class UserControllerE2ETest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @MockBean private GetDailyProgressUseCase getDailyProgressUseCase;
    @MockBean private UpdateDailyProgressUseCase updateDailyProgressUseCase;
    @MockBean private CreateTaskUseCase createTaskUseCase;
    @MockBean private GetTaskUseCase getTaskUseCase;
    @MockBean private UpdateTaskUseCase updateTaskUseCase;
    @MockBean private DeleteTaskUseCase deleteTaskUseCase;
    @MockBean private ListTasksUseCase listTasksUseCase;
    @MockBean private CreateGroupUseCase createGroupUseCase;
    @MockBean private GetGroupUseCase getGroupUseCase;
    @MockBean private UpdateGroupUseCase updateGroupUseCase;
    @MockBean private DeleteGroupUseCase deleteGroupUseCase;
    @MockBean private ListGroupsUseCase listGroupsUseCase;
    @MockBean private JoinGroupUseCase joinGroupUseCase;

    private UserJpaEntity testUser;
    private String accessToken;

    @BeforeEach
    void setUp() {
        userJpaRepository.deleteAll();

        // H2 AUTO_INCREMENT 초기화 (ID를 1부터 시작)
        jdbcTemplate.execute("ALTER TABLE users ALTER COLUMN id RESTART WITH 1");


        testUser = new UserJpaEntity(
                "testuser@example.com",
                passwordEncoder.encode("password123"),
                "Test User"
        );
        testUser = userJpaRepository.save(testUser);
        accessToken = jwtTokenProvider.generateAccessToken(testUser.getId(), testUser.getEmail());

    }

    @Test
    @DisplayName("GET /users/me - Success with real database")
    void getMyProfile_Success_WithRealDatabase() {
        // When
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        var response = restTemplate.exchange("/users/me", HttpMethod.GET, new HttpEntity<>(headers), String.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("testuser@example.com");
        assertThat(response.getBody()).contains("Test User");
        assertThat(response.getBody()).contains("\"id\":" + testUser.getId());
    }

    @Test
    @DisplayName("PUT /users/me - Success (Update Name Only)")
    void updateMyProfile_Success_NameOnly() {
        // Given
        String requestBody = """
            {
                "name": "Updated Name"
            }
            """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        // When
        var response = restTemplate.exchange("/users/me", HttpMethod.PUT, request, String.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Updated Name");
        assertThat(response.getBody()).contains("testuser@example.com");


        var updatedUser = userJpaRepository.findById(testUser.getId());
        assertThat(updatedUser).isPresent();
        assertThat(updatedUser.get().getName()).isEqualTo("Updated Name");
        assertThat(updatedUser.get().getEmail()).isEqualTo("testuser@example.com");
    }

    @Test
    @DisplayName("PUT /users/me - Success (Update Password Only)")
    void updateMyProfile_Success_PasswordOnly() {
        // Given
        String requestBody = """
            {
                "password": "newPassword456"
            }
            """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        String originalPasswordHash = testUser.getPassword();

        // When
        var response = restTemplate.exchange("/users/me", HttpMethod.PUT, request, String.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        var updatedUser = userJpaRepository.findById(testUser.getId());
        assertThat(updatedUser).isPresent();
        assertThat(updatedUser.get().getPassword()).isNotEqualTo(originalPasswordHash);
        assertThat(updatedUser.get().getPassword()).isNotEqualTo("newPassword456"); // Should be encrypted
        assertThat(updatedUser.get().getPassword()).startsWith("$2a$"); // BCrypt prefix

        assertThat(passwordEncoder.matches("newPassword456", updatedUser.get().getPassword())).isTrue();
    }

    @Test
    @DisplayName("PUT /users/me - Success (Update Both Name and Password)")
    void updateMyProfile_Success_NameAndPassword() {
        // Given
        String requestBody = """
            {
                "name": "Completely New Name",
                "password": "superSecure999"
            }
            """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        // When
        var response = restTemplate.exchange("/users/me", HttpMethod.PUT, request, String.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Completely New Name");


        var updatedUser = userJpaRepository.findById(testUser.getId());
        assertThat(updatedUser).isPresent();
        assertThat(updatedUser.get().getName()).isEqualTo("Completely New Name");
        assertThat(passwordEncoder.matches("superSecure999", updatedUser.get().getPassword())).isTrue();
    }

    @Test
    @DisplayName("PUT /users/me - Success (Empty Body - No Changes)")
    void updateMyProfile_Success_EmptyBody() {
        // Given
        String requestBody = "{}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        String originalName = testUser.getName();
        String originalPassword = testUser.getPassword();

        // When
        var response = restTemplate.exchange("/users/me", HttpMethod.PUT, request, String.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Verify nothing changed
        var unchangedUser = userJpaRepository.findById(testUser.getId());
        assertThat(unchangedUser).isPresent();
        assertThat(unchangedUser.get().getName()).isEqualTo(originalName);
        assertThat(unchangedUser.get().getPassword()).isEqualTo(originalPassword);
    }

    @Test
    @DisplayName("PUT /users/me - Validation Error (Name Too Short)")
    void updateMyProfile_ValidationError_ShortName() {
        // Given
        String requestBody = """
            {
                "name": "A"
            }
            """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        // When
        var response = restTemplate.exchange("/users/me", HttpMethod.PUT, request, String.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        // Verify database was not updated
        var unchangedUser = userJpaRepository.findById(testUser.getId());
        assertThat(unchangedUser).isPresent();
        assertThat(unchangedUser.get().getName()).isEqualTo("Test User");
    }

    @Test
    @DisplayName("PUT /users/me - Validation Error (Name Too Long)")
    void updateMyProfile_ValidationError_LongName() {
        // Given
        String longName = "A".repeat(51); // 51 characters
        String requestBody = String.format("""
            {
                "name": "%s"
            }
            """, longName);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        // When
        var response = restTemplate.exchange("/users/me", HttpMethod.PUT, request, String.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("PUT /users/me - Validation Error (Password Too Short)")
    void updateMyProfile_ValidationError_ShortPassword() {
        // Given
        String requestBody = """
            {
                "password": "short"
            }
            """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        // When
        var response = restTemplate.exchange("/users/me", HttpMethod.PUT, request, String.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        // Verify password was not changed
        var unchangedUser = userJpaRepository.findById(testUser.getId());
        assertThat(unchangedUser).isPresent();
        assertThat(passwordEncoder.matches("password123", unchangedUser.get().getPassword())).isTrue();
    }

    @Test
    @DisplayName("PUT /users/me - Validation Error (Password Too Long)")
    void updateMyProfile_ValidationError_LongPassword() {
        // Given
        String longPassword = "A".repeat(101); // 101 characters
        String requestBody = String.format("""
            {
                "password": "%s"
            }
            """, longPassword);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        // When
        var response = restTemplate.exchange("/users/me", HttpMethod.PUT, request, String.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("GET /users/me - Returns correct data structure")
    void getMyProfile_CorrectDataStructure() {
        // When
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        var response = restTemplate.exchange("/users/me", HttpMethod.GET, new HttpEntity<>(headers), String.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        // Check JSON structure
        assertThat(response.getBody()).contains("\"id\":");
        assertThat(response.getBody()).contains("\"email\":");
        assertThat(response.getBody()).contains("\"name\":");
        assertThat(response.getBody()).contains("\"createdAt\":");
        // Password should NOT be exposed
        assertThat(response.getBody()).doesNotContain("\"password\":");
        assertThat(response.getBody()).doesNotContain("$2a$"); // No BCrypt hash
    }
}
