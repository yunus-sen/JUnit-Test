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
    private Utils utils;

    @Test
    public void whenGenerateUserIdCalled_itShouldReturnString() {
        String userId = utils.generateUserId(30);
        String userId2 = utils.generateUserId(30);

        assertNotNull(userId);
        assertNotNull(userId2);
        assertFalse(userId.equalsIgnoreCase(userId2));
        assertEquals(userId.length(), 30);
    }

    @Test
    public void whenHasTokenExpiredCalledWithNotExpiredToken_itShouldReturnFalse() {
        String token = utils.generateEmailVerificationToken("asdf123");
        assertNotNull(token);

        boolean hasTokenNotExpired = Utils.hasTokenExpired(token);
        assertFalse(hasTokenNotExpired);
    }

    @Test
    public void testHasTokenExpired() {
        String expiredToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhc2RmMTIzIiwiZXhwIjoxNjQ4NDU3MTA2fQ.5feADtmP3actU7j_ozlv75-0bbBVcaMx97Fipe7VJgi-QEv5gAmqJ_bFH2w_ZDIfqn1RwvPTJDQjh1YTY_oJVg";
        boolean hasTokenExpired = Utils.hasTokenExpired(expiredToken);

        assertTrue(hasTokenExpired);

    }

}