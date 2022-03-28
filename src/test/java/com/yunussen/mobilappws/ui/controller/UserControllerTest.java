package com.yunussen.mobilappws.ui.controller;

import com.yunussen.mobilappws.service.imp.UserServiceImp;
import com.yunussen.mobilappws.shared.dto.AdressDto;
import com.yunussen.mobilappws.shared.dto.UserDto;
import com.yunussen.mobilappws.ui.model.response.UserRest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    UserController userController;

    @Mock
    UserServiceImp userService;

    UserDto userDto;

    final String USER_ID = "bfhry47fhdjd7463gdh";

    @BeforeEach
    void setUp() throws Exception {

        userDto = new UserDto();
        userDto.setFirstName("Sergey");
        userDto.setLastName("Kargopolov");
        userDto.setEmail("test@test.com");
        userDto.setEmailVerificationStatus(Boolean.FALSE);
        userDto.setEmailVerificationToken(null);
        userDto.setUserId(USER_ID);
        //userDto.setAddresses(getAddressesDto());
        userDto.setEncryptedPassword("xcf58tugh47");

    }
    @Test
    final void whenGetUserCalledWithUserId_itShouldReturnUserRest() {
        when(userService.findUserByid(anyString())).thenReturn(userDto);

        UserRest userRest = userController.getUser(USER_ID);

        assertNotNull(userRest);
        assertEquals(USER_ID, userRest.getUserId());
        assertEquals(userDto.getFirstName(), userRest.getFirstName());
        assertEquals(userDto.getLastName(), userRest.getLastName());
        assertTrue(userDto.getAddresses().size() == userRest.getAddresses().size());
    }

    private List<AdressDto> getAddressesDto() {
        AdressDto addressDto = new AdressDto();
        addressDto.setType("shipping");
        addressDto.setCity("Vancouver");
        addressDto.setCountry("Canada");
        addressDto.setPostalCode("ABC123");
        addressDto.setStreetName("123 Street name");

        AdressDto billingAddressDto = new AdressDto();
        billingAddressDto.setType("billling");
        billingAddressDto.setCity("Vancouver");
        billingAddressDto.setCountry("Canada");
        billingAddressDto.setPostalCode("ABC123");
        billingAddressDto.setStreetName("123 Street name");

        List<AdressDto> addresses = new ArrayList<>();
        addresses.add(addressDto);
        addresses.add(billingAddressDto);

        return addresses;

    }


}