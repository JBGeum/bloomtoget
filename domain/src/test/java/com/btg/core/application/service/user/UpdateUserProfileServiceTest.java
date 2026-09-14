package com.btg.core.application.service.user;

import com.btg.core.application.port.in.user.UpdateUserProfileUseCase;
import com.btg.core.application.port.out.auth.EncodePasswordPort;
import com.btg.core.application.port.out.user.UpdateUserPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
@DisplayName("UpdateUserProfileService 단위 테스트")
class UpdateUserProfileServiceTest {

    @Mock
    private UpdateUserPort updateUserPort;

    @Mock
    private EncodePasswordPort encodePasswordPort;

    @InjectMocks
    private UpdateUserProfileService updateUserProfileService;

    @Test
    @DisplayName("이름만 업데이트 - 성공")
    void updateUserProfile_NameOnly_Success() {
        // Given
        UpdateUserProfileUseCase.UpdateUserProfileCommand command =
                new UpdateUserProfileUseCase.UpdateUserProfileCommand(
                        1L,
                        "Updated Name",
                        null  // password는 null
                );

        UpdateUserPort.User updatedUser = new UpdateUserPort.User(
                1L,
                "test@example.com",
                "Updated Name",
                1732352400000L
        );

        given(updateUserPort.update(1L, "Updated Name", null)).willReturn(updatedUser);

        // When
        UpdateUserProfileUseCase.UserProfileResult result = updateUserProfileService.updateUserProfile(command);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.email()).isEqualTo("test@example.com");
        assertThat(result.name()).isEqualTo("Updated Name");
        assertThat(result.createdAt()).isEqualTo(1732352400000L);

        then(updateUserPort).should().update(1L, "Updated Name", null);
        then(encodePasswordPort).should(never()).encode(anyString());
    }

    @Test
    @DisplayName("비밀번호만 업데이트 - 암호화 검증 성공")
    void updateUserProfile_PasswordOnly_Success() {
        // Given
        String rawPassword = "newPassword123";
        String encodedPassword = "$2a$10$encodedNewPassword";

        UpdateUserProfileUseCase.UpdateUserProfileCommand command =
                new UpdateUserProfileUseCase.UpdateUserProfileCommand(
                        1L,
                        null,  // name은 null
                        rawPassword
                );

        given(encodePasswordPort.encode(rawPassword)).willReturn(encodedPassword);

        UpdateUserPort.User updatedUser = new UpdateUserPort.User(
                1L,
                "test@example.com",
                "Original Name",
                1732352400000L
        );

        given(updateUserPort.update(1L, null, encodedPassword)).willReturn(updatedUser);

        // When
        UpdateUserProfileUseCase.UserProfileResult result = updateUserProfileService.updateUserProfile(command);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("Original Name");  // 이름은 변경 안 됨

        then(encodePasswordPort).should().encode(rawPassword);
        then(updateUserPort).should().update(1L, null, encodedPassword);
    }

    @Test
    @DisplayName("이름과 비밀번호 모두 업데이트 - 성공")
    void updateUserProfile_NameAndPassword_Success() {
        // Given
        String rawPassword = "strongPassword456";
        String encodedPassword = "$2a$10$strongEncodedPassword";

        UpdateUserProfileUseCase.UpdateUserProfileCommand command =
                new UpdateUserProfileUseCase.UpdateUserProfileCommand(
                        1L,
                        "Completely New Name",
                        rawPassword
                );

        given(encodePasswordPort.encode(rawPassword)).willReturn(encodedPassword);

        UpdateUserPort.User updatedUser = new UpdateUserPort.User(
                1L,
                "test@example.com",
                "Completely New Name",
                1732352400000L
        );

        given(updateUserPort.update(1L, "Completely New Name", encodedPassword)).willReturn(updatedUser);

        // When
        UpdateUserProfileUseCase.UserProfileResult result = updateUserProfileService.updateUserProfile(command);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("Completely New Name");

        then(encodePasswordPort).should().encode(rawPassword);
        then(updateUserPort).should().update(1L, "Completely New Name", encodedPassword);
    }

    @Test
    @DisplayName("아무것도 업데이트하지 않음 (null 값) - 성공")
    void updateUserProfile_NoChanges_Success() {
        // Given
        UpdateUserProfileUseCase.UpdateUserProfileCommand command =
                new UpdateUserProfileUseCase.UpdateUserProfileCommand(
                        1L,
                        null,
                        null
                );

        UpdateUserPort.User unchangedUser = new UpdateUserPort.User(
                1L,
                "test@example.com",
                "Original Name",
                1732352400000L
        );

        given(updateUserPort.update(1L, null, null)).willReturn(unchangedUser);

        // When
        UpdateUserProfileUseCase.UserProfileResult result = updateUserProfileService.updateUserProfile(command);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("Original Name");

        then(encodePasswordPort).should(never()).encode(anyString());
        then(updateUserPort).should().update(1L, null, null);
    }

    @Test
    @DisplayName("빈 문자열(blank) 비밀번호 - 암호화하지 않음")
    void updateUserProfile_BlankPassword_NoEncoding() {
        // Given
        UpdateUserProfileUseCase.UpdateUserProfileCommand command =
                new UpdateUserProfileUseCase.UpdateUserProfileCommand(
                        1L,
                        "New Name",
                        "   "  // blank string
                );

        UpdateUserPort.User updatedUser = new UpdateUserPort.User(
                1L,
                "test@example.com",
                "New Name",
                1732352400000L
        );

        given(updateUserPort.update(1L, "New Name", null)).willReturn(updatedUser);

        // When
        updateUserProfileService.updateUserProfile(command);

        // Then
        // blank 비밀번호는 암호화하지 않고 null로 처리
        then(encodePasswordPort).should(never()).encode(anyString());
        then(updateUserPort).should().update(1L, "New Name", null);
    }

    @Test
    @DisplayName("비밀번호 암호화가 UpdateUserPort 호출 전에 수행되는지 검증 - 성공")
    void updateUserProfile_PasswordEncodedBeforeUpdate_Success() {
        // Given
        String rawPassword = "testPassword";
        String encodedPassword = "$2a$10$testEncodedPassword";

        UpdateUserProfileUseCase.UpdateUserProfileCommand command =
                new UpdateUserProfileUseCase.UpdateUserProfileCommand(
                        1L,
                        null,
                        rawPassword
                );

        given(encodePasswordPort.encode(rawPassword)).willReturn(encodedPassword);

        UpdateUserPort.User updatedUser = new UpdateUserPort.User(
                1L,
                "test@example.com",
                "Original Name",
                1732352400000L
        );

        given(updateUserPort.update(1L, null, encodedPassword)).willReturn(updatedUser);

        // When
        updateUserProfileService.updateUserProfile(command);

        // Then
        // 암호화가 먼저 호출되고
        then(encodePasswordPort).should().encode(rawPassword);
        // 암호화된 비밀번호가 UpdateUserPort에 전달됨
        then(updateUserPort).should().update(1L, null, encodedPassword);
    }

    @Test
    @DisplayName("UpdateUserPort에 올바른 값들이 전달되는지 검증 - 성공")
    void updateUserProfile_CorrectValuesPassedToPort_Success() {
        // Given
        Long userId = 42L;
        String newName = "Verified Name";
        String rawPassword = "verifiedPassword";
        String encodedPassword = "$2a$10$verifiedEncoded";

        UpdateUserProfileUseCase.UpdateUserProfileCommand command =
                new UpdateUserProfileUseCase.UpdateUserProfileCommand(
                        userId,
                        newName,
                        rawPassword
                );

        given(encodePasswordPort.encode(rawPassword)).willReturn(encodedPassword);

        UpdateUserPort.User updatedUser = new UpdateUserPort.User(
                userId,
                "verified@example.com",
                newName,
                1732352400000L
        );

        given(updateUserPort.update(userId, newName, encodedPassword)).willReturn(updatedUser);

        // When
        updateUserProfileService.updateUserProfile(command);

        // Then
        then(updateUserPort).should().update(
                userId,           // 정확한 userId
                newName,          // 정확한 name
                encodedPassword   // 암호화된 password
        );
    }

    @Test
    @DisplayName("반환값의 모든 필드가 올바르게 매핑되는지 검증 - 성공")
    void updateUserProfile_AllFieldsMappedCorrectly_Success() {
        // Given
        UpdateUserProfileUseCase.UpdateUserProfileCommand command =
                new UpdateUserProfileUseCase.UpdateUserProfileCommand(
                        1L,
                        "Mapped Name",
                        null
                );

        UpdateUserPort.User updatedUser = new UpdateUserPort.User(
                1L,
                "mapped@example.com",
                "Mapped Name",
                1732372245000L
        );

        given(updateUserPort.update(1L, "Mapped Name", null)).willReturn(updatedUser);

        // When
        UpdateUserProfileUseCase.UserProfileResult result = updateUserProfileService.updateUserProfile(command);

        // Then
        assertThat(result.id()).isEqualTo(updatedUser.id());
        assertThat(result.email()).isEqualTo(updatedUser.email());
        assertThat(result.name()).isEqualTo(updatedUser.name());
        assertThat(result.createdAt()).isEqualTo(updatedUser.createdAt());
    }

    @Test
    @DisplayName("빈 문자열(blank) 이름 처리 - 성공")
    void updateUserProfile_BlankName_Success() {
        // Given
        UpdateUserProfileUseCase.UpdateUserProfileCommand command =
                new UpdateUserProfileUseCase.UpdateUserProfileCommand(
                        1L,
                        "   ",  // blank name
                        null
                );

        UpdateUserPort.User updatedUser = new UpdateUserPort.User(
                1L,
                "test@example.com",
                "Original Name",
                1732352400000L
        );

        // blank name은 그대로 전달됨 (검증은 Command에서 수행)
        given(updateUserPort.update(1L, "   ", null)).willReturn(updatedUser);

        // When
        updateUserProfileService.updateUserProfile(command);

        // Then
        then(updateUserPort).should().update(1L, "   ", null);
    }
}
