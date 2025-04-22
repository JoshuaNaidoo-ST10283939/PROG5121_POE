package login;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {
    
    public LoginTest() {
    }

    @Test
    public void testCheckUserName() {
        Login valid = new Login("John", "Doe", "jd_e", "Password1!", "+27831234567");
        assertTrue(valid.checkUserName());

        Login invalid = new Login("John", "Doe", "jdoe", "Password1!", "+27831234567");
        assertFalse(invalid.checkUserName());
    }

    @Test
    public void testCheckPasswordComplexity() {
        Login valid = new Login("Jane", "Smith", "js_m", "Passw0rd!", "+27831234567");
        assertTrue(valid.checkPasswordComplexity());

        Login noSpecialChar = new Login("Jane", "Smith", "js_m", "Passw0rd", "+27831234567");
        assertFalse(noSpecialChar.checkPasswordComplexity());

        Login noNumber = new Login("Jane", "Smith", "js_m", "Password!", "+27831234567");
        assertFalse(noNumber.checkPasswordComplexity());

        Login tooShort = new Login("Jane", "Smith", "js_m", "Pw1!", "+27831234567");
        assertFalse(tooShort.checkPasswordComplexity());
    }

    @Test
    public void testCheckCellPhoneNumber() {
        Login valid = new Login("Mike", "Jones", "mj_1", "Password1!", "+27821234567");
        assertTrue(valid.checkCellPhoneNumber());

        Login invalid = new Login("Mike", "Jones", "mj_1", "Password1!", "0821234567");
        assertFalse(invalid.checkCellPhoneNumber());
    }

    @Test
    public void testRegisterUser() {
        Login valid = new Login("Amy", "Williams", "aw_1", "ValidPass1!", "+27839999999");
        assertEquals("Registration successful!", valid.registerUser());

        Login badUsername = new Login("Amy", "Williams", "amywilliams", "ValidPass1!", "+27839999999");
        assertEquals("Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.", badUsername.registerUser());

        Login badPassword = new Login("Amy", "Williams", "aw_1", "password", "+27839999999");
        assertEquals("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.", badPassword.registerUser());

        Login badCell = new Login("Amy", "Williams", "aw_1", "ValidPass1!", "0831234567");
        assertEquals("Cell phone number incorrectly formatted or does not contain international code.", badCell.registerUser());
    }

    @Test
    public void testLoginUser() {
        Login user = new Login("Bruce", "Wayne", "bw_1", "Batm@n123", "+27831112222");

        assertTrue(user.loginUser("bw_1", "Batm@n123"));
        assertFalse(user.loginUser("bw_1", "WrongPassword"));
        assertFalse(user.loginUser("wrong", "Batm@n123"));
    }

    @Test
    public void testReturnLoginStatus() {
        Login user = new Login("Clark", "Kent", "ck_1", "Superm@n1", "+27830001111");

        assertEquals("Welcome Clark Kent, it is great to see you again.", user.returnLoginStatus(true));
        assertEquals("Username or password incorrect, please try again.", user.returnLoginStatus(false));
    }

}