import static org.junit.jupiter.api.Assertions.*;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LoginTest {

    @BeforeEach
    public void resetMessages() {
        // Reset message count using reflection (since messageCount is private)
        try {
            var field = Login.Message.class.getDeclaredField("messageCount");
            field.setAccessible(true);
            field.setInt(null, 0); // Reset to 0 before each test
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException e) {
            fail("Could not reset message count: " + e.getMessage());
        }
    }

    @Test
    public void testMessageLength_Success() {
        String message = "This is a valid short message.";
        assertTrue(message.length() <= 250, "Message is within the valid length");
    }

    @Test
    public void testMessageLength_Failure() {
        StringBuilder longMessage = new StringBuilder();
        for (int i = 0; i < 260; i++) longMessage.append("a");
        int over = longMessage.length() - 250;
        assertTrue(over > 0, "Message exceeds 250 characters by " + over);
    }

    @Test
    public void testRecipientNumber_Valid() {
        Login.Message msg = new Login.Message("+27718693002", "Hello Mike!");
        assertTrue(msg.checkRecipientCell(), "Valid SA number should pass");
    }

    @Test
    public void testRecipientNumber_Invalid() {
        Login.Message msg = new Login.Message("08575975889", "Hello Keegan!");
        assertFalse(msg.checkRecipientCell(), "Invalid SA number format should fail");
    }

    @Test
    public void testMessageHashGeneration() {
        Login.Message msg = new Login.Message("+27718693002", "Hi Mike, can you join us for dinner tonight");
        String hash;
        hash = msg.createMessageHash();
        assertTrue(hash.contains("HITONIGHT"), "Hash should include first and last words of message");
    }

    @Test
    public void testMessageIDGenerationLength() {
        Login.Message msg = new Login.Message("+27718693002", "Hello");
        JSONObject json = msg.toJSON();
        String id = json.getString("MessageID");
        assertEquals(10, id.length(), "Message ID should be 10 characters long");
    }

    @Test
    public void testSendMessageOption_Send() {
        // Simulate sending by invoking messageList add directly
        Login.Message msg = new Login.Message("+27718693002", "Hi Mike");
        String result;  // Simulate "Send Message"
        result = msg.sendMessageOptionTest(0);
        assertEquals("Message successfully sent.", result);
    }

    @Test
    public void testSendMessageOption_Disregard() {
        Login.Message msg = new Login.Message("+27718693002", "Hi Mike");
        String result;  // Simulate "Disregard Message"
        result = msg.sendMessageOptionTest(1);
        assertEquals("Message disregarded.", result);
    }

    @Test
    public void testSendMessageOption_Store() {
        Login.Message msg = new Login.Message("+27718693002", "Hi Mike");
        String result = msg.sendMessageOptionTest(2);  // Simulate "Store Message"
        assertEquals("Message successfully stored.", result);
    }

    @Test
    public void testTotalMessages() {
        int before = Login.Message.returnTotalMessages();
        Login.Message msg = new Login.Message("+27831234567", "Test message");
        int after = Login.Message.returnTotalMessages();
        assertEquals(before + 1, after, "Message count should increase by 1");
    }
}
