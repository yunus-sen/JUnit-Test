package com.yunussen.mobilappws.shared;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UtilsTest {

    @InjectMocks
    private Utils util;

    @Test
    public void whenGenerateUserIdCalled_itShouldReturnString() {
        String userId = util.generateUserId(30);
        String userId2 = util.generateUserId(30);

        assertNotNull(userId);
        assertNotNull(userId2);
        assertFalse(userId.equalsIgnoreCase(userId2));
        assertEquals(userId.length(), 30);
    }

}