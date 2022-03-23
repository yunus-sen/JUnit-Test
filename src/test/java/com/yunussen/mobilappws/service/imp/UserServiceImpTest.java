package com.yunussen.mobilappws.service.imp;

import com.yunussen.mobilappws.io.entity.UserEntity;
import com.yunussen.mobilappws.io.repository.PasswordResetTokenRepository;
import com.yunussen.mobilappws.io.repository.RoleRepository;
import com.yunussen.mobilappws.io.repository.UserRepository;
import com.yunussen.mobilappws.shared.Utils;
import com.yunussen.mobilappws.shared.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImpTest {

    @InjectMocks
    private UserServiceImp userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private Utils utils;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Test
    void whenGetUserCalledWithNonValidEmail_itShouldThrowUserNotFoundException() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        assertThrows(UsernameNotFoundException.class,
                () -> userService.getUser(anyString()));
    }


    @Test
    void whenGetUserCalledValidEmail_itShouldReturnValidUSerDto() {
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .userId("12q243sfdsdf")
                .email("yunussen@gmail.com")
                .encryptedPassword("asd1234")
                .firstName("yunus")
                .lastName("şen")
                .build();

        when(userRepository.findByEmail(anyString())).thenReturn(userEntity);
        UserDto userDto=userService.getUser("yunussen@gmail.com");
        assertEquals(userDto.getId(),userDto.getId());
        assertEquals(userDto.getUserId(),userEntity.getUserId());

        verify(userRepository).findByEmail(anyString());
    }

}