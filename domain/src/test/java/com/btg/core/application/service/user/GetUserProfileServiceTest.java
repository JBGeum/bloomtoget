package com.btg.core.application.service.user;

import com.btg.core.application.port.in.user.GetUserProfileUseCase;
import com.btg.core.application.port.out.user.LoadUserPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
@DisplayName("GetUserProfileService 단위 테스트")
class GetUserProfileServiceTest {

    @Mock
    private LoadUserPort loadUserPort;

    @InjectMocks
    private GetUserProfileService getUserProfileService;

    @Test
    @DisplayName("정상적인 프로필 조회 - 성공")
    void getUserProfile_Success() {
        // Given
        Long userId = 1L;
        LoadUserPort.User mockUser = new LoadUserPort.User(
                1L,
                "test@example.com",
                "$2a$10$encodedPassword",
                "Test User",
                1732352400000L
        );

        given(loadUserPort.loadById(userId)).willReturn(Optional.of(mockUser));

        // When
        GetUserProfileUseCase.UserProfileResult result = getUserProfileService.getUserProfile(userId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.email()).isEqualTo("test@example.com");
        assertThat(result.name()).isEqualTo("Test User");
        assertThat(result.createdAt()).isEqualTo(1732352400000L);

        then(loadUserPort).should().loadById(userId);
    }

    @Test
    @DisplayName("존재하지 않는 사용자 조회 - IllegalArgumentException 발생")
    void getUserProfile_UserNotFound_ThrowsException() {
        // Given
        Long nonExistentUserId = 999L;
        given(loadUserPort.loadById(nonExistentUserId)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> getUserProfileService.getUserProfile(nonExistentUserId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User not found with id: 999");

        then(loadUserPort).should().loadById(nonExistentUserId);
    }

    @Test
    @DisplayName("반환값의 모든 필드가 올바르게 매핑되는지 검증 - 성공")
    void getUserProfile_AllFieldsMappedCorrectly_Success() {
        // Given
        Long userId = 42L;
        LoadUserPort.User mockUser = new LoadUserPort.User(
                42L,
                "complete@example.com",
                "hashedPasswordValue",
                "Complete User Name",
                1732372245000L
        );

        given(loadUserPort.loadById(userId)).willReturn(Optional.of(mockUser));

        // When
        GetUserProfileUseCase.UserProfileResult result = getUserProfileService.getUserProfile(userId);

        // Then
        assertThat(result.id()).isEqualTo(mockUser.id());
        assertThat(result.email()).isEqualTo(mockUser.email());
        assertThat(result.name()).isEqualTo(mockUser.name());
        assertThat(result.createdAt()).isEqualTo(mockUser.createdAt());
        // 비밀번호는 반환값에 포함되지 않음 (보안)
    }

    @Test
    @DisplayName("LoadUserPort가 올바른 userId로 호출되는지 검증 - 성공")
    void getUserProfile_LoadUserPortCalledWithCorrectUserId_Success() {
        // Given
        Long specificUserId = 123L;
        LoadUserPort.User mockUser = new LoadUserPort.User(
                123L,
                "user123@example.com",
                "password",
                "User 123",
                1732359600000L
        );

        given(loadUserPort.loadById(specificUserId)).willReturn(Optional.of(mockUser));

        // When
        getUserProfileService.getUserProfile(specificUserId);

        // Then
        then(loadUserPort).should().loadById(specificUserId);
    }

    @Test
    @DisplayName("여러 사용자 조회 시 각각 독립적으로 처리 - 성공")
    void getUserProfile_MultipleUsers_IndependentlyProcessed() {
        // Given
        Long userId1 = 1L;
        Long userId2 = 2L;

        LoadUserPort.User user1 = new LoadUserPort.User(
                1L,
                "user1@example.com",
                "password1",
                "User One",
                1732352400000L
        );

        LoadUserPort.User user2 = new LoadUserPort.User(
                2L,
                "user2@example.com",
                "password2",
                "User Two",
                1732356000000L
        );

        given(loadUserPort.loadById(userId1)).willReturn(Optional.of(user1));
        given(loadUserPort.loadById(userId2)).willReturn(Optional.of(user2));

        // When
        GetUserProfileUseCase.UserProfileResult result1 = getUserProfileService.getUserProfile(userId1);
        GetUserProfileUseCase.UserProfileResult result2 = getUserProfileService.getUserProfile(userId2);

        // Then
        assertThat(result1.id()).isEqualTo(1L);
        assertThat(result1.email()).isEqualTo("user1@example.com");

        assertThat(result2.id()).isEqualTo(2L);
        assertThat(result2.email()).isEqualTo("user2@example.com");

        then(loadUserPort).should().loadById(userId1);
        then(loadUserPort).should().loadById(userId2);
    }
}
