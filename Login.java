import javax.swing.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Random;

public class Login {

    static class UserLogin {
        private final String username;
        private final String password;
        private final String cellNumber;
        private final String firstName;
        private final String lastName;

        public UserLogin(String firstName, String lastName, String username, String password, String cellNumber) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.username = username;
            this.password = password;
            this.cellNumber = cellNumber;
        }

        public boolean checkUserName() {
            return username.contains("_") && username.length() <= 5;
        }

        public boolean checkPasswordComplexity() {
            return password.matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+=\\-]).{8,}$");
        }

        public boolean checkCellPhoneNumber() {
            return cellNumber.matches("^\\+27\\d{9}$");
        }

        public String registerUser() {
            if (!checkUserName()) return "Username is not correctly formatted.";
            if (!checkPasswordComplexity()) return "Password is not correctly formatted.";
            if (!checkCellPhoneNumber()) return "Cell phone number incorrectly formatted.";
            return "Registration successful!";
        }

        public boolean loginUser(String enteredUsername, String enteredPassword) {
            return this.username.equals(enteredUsername) && this.password.equals(enteredPassword);
        }

        public String returnLoginStatus(boolean loginSuccess) {
            if (loginSuccess) {
                return "Welcome " + firstName + " " + lastName + ", it is great to see you again.";
            } else {
                return "Username or password incorrect.";
            }
        }
    }

    static class Message {
        private static int messageCount = 0;
        private static final JSONArray messageList = new JSONArray();

        private final String messageID;
        private final int messageNumber;
        private final String recipient;
        private final String message;
        private final String messageHash;

        public Message(String recipient, String message) {
            this.messageID = generateMessageID();
            this.messageNumber = ++messageCount;
            this.recipient = recipient;
            this.message = message;
            this.messageHash = createMessageHash();
        }

        private String generateMessageID() {
            Random rand = new Random();
            StringBuilder id = new StringBuilder();
            for (int i = 0; i < 10; i++) {
                id.append(rand.nextInt(10));
            }
            return id.toString();
        }

        public boolean checkRecipientCell() {
            return recipient.matches("^\\+27\\d{9}$");
        }

        private String createMessageHash() {
            String[] words = message.trim().split("\\s+");
            if (words.length == 0 || words[0].isEmpty()) {
                return (messageID.substring(0, 2) + ":" + messageNumber + ":EMPTY").toUpperCase();
            }
            String first = words[0];
            String last = words[words.length - 1];
            return (messageID.substring(0, 2) + ":" + messageNumber + ":" + first + last).toUpperCase();
        }

        public String sendMessageOption() {
            String[] options = {"Send Message", "Disregard Message", "Store Message"};
            int choice = JOptionPane.showOptionDialog(null, "Choose an action:", "Message Option",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

            switch (choice) {
                case 0 -> {
                    messageList.put(toJSON());
                    return "Message successfully sent.";
                }
                case 1 -> {
                    return "Message disregarded.";
                }
                case 2 -> {
                    storeMessage();
                    return "Message successfully stored.";
                }
                default -> {
                    return "No action taken.";
                }
            }
        }

        public JSONObject toJSON() {
            JSONObject obj = new JSONObject();
            obj.put("MessageID", messageID);
            obj.put("MessageHash", messageHash);
            obj.put("Recipient", recipient);
            obj.put("Message", message);
            return obj;
        }

        public void storeMessage() {
            try (FileWriter file = new FileWriter("stored_messages.json", true)) {
                file.write(toJSON().toString() + System.lineSeparator());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Failed to store message: " + e.getMessage());
            }
        }

        public static int returnTotalMessages() {
            return messageCount;
        }

        public void displayMessageDetails() {
            String details = "Message ID: " + messageID +
                    "\nMessage Hash: " + messageHash +
                    "\nRecipient: " + recipient +
                    "\nMessage: " + message;
            JOptionPane.showMessageDialog(null, details);
        }

        public static void showStoredMessages() {
            File file = new File("stored_messages.json");
            if (!file.exists()) {
                JOptionPane.showMessageDialog(null, "No stored messages found.");
                return;
            }
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line).append("\n\n");
                }
                JOptionPane.showMessageDialog(null, builder.toString(), "Stored Messages", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error reading messages: " + e.getMessage());
            }
        }

        String sendMessageOptionTest(int i) {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

       

    public static void main(String[] args) {
        // Registration
        String firstName = prompt("Enter your first name:");
        String lastName = prompt("Enter your last name:");

        String username;
        while (true) {
            username = prompt("Enter a username (must contain _ and ≤ 5 chars):");
            UserLogin temp = new UserLogin(firstName, lastName, username, "Temp123!", "+27123456789");
            if (!temp.checkUserName()) {
                JOptionPane.showMessageDialog(null, "Invalid username format.");
            } else break;
        }

        String password;
        while (true) {
            password = prompt("Enter a secure password:");
            UserLogin temp = new UserLogin(firstName, lastName, username, password, "+27123456789");
            if (!temp.checkPasswordComplexity()) {
                JOptionPane.showMessageDialog(null, "Invalid password format.");
            } else break;
        }

        String cellNumber;
        while (true) {
            cellNumber = prompt("Enter your SA phone number (+27...):");
            UserLogin temp = new UserLogin(firstName, lastName, username, password, cellNumber);
            if (!temp.checkCellPhoneNumber()) {
                JOptionPane.showMessageDialog(null, "Invalid phone number.");
            } else break;
        }

        UserLogin user = new UserLogin(firstName, lastName, username, password, cellNumber);
        JOptionPane.showMessageDialog(null, user.registerUser());

        // Login
        boolean isLoggedIn = false;
        int attempts = 5;
        while (attempts > 0 && !isLoggedIn) {
            String u = prompt("Login - Enter username:");
            String p = prompt("Login - Enter password:");
            isLoggedIn = user.loginUser(u, p);
            if (!isLoggedIn) {
                attempts--;
                JOptionPane.showMessageDialog(null, "Incorrect credentials. Attempts left: " + attempts);
            }
        }

        if (!isLoggedIn) {
            JOptionPane.showMessageDialog(null, "Too many failed attempts. Goodbye.");
            return;
        }

        JOptionPane.showMessageDialog(null, user.returnLoginStatus(true) + "\nWelcome to QuickChat!");

        // Messaging
        int numMessages = Integer.parseInt(prompt("How many messages would you like to enter?"));
        int messagesEntered = 0;

        while (true) {
            String menu = prompt("Select an option:\n1) Send Messages\n2) Show Recently Stored Messages\n3) Quit");

            switch (menu) {
                case "1" -> {
                    if (messagesEntered >= numMessages) {
                        JOptionPane.showMessageDialog(null, "You've reached the message limit.");
                        continue;
                    }

                    String recipient = prompt("Enter recipient's phone number (+27...):");
                    String msg = prompt("Enter your message (max 250 characters):");

                    if (msg.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Message cannot be empty.");
                        break;
                    }

                    if (msg.length() > 250) {
                        JOptionPane.showMessageDialog(null, "Message exceeds 250 characters by " + (msg.length() - 250));
                        break;
                    }

                    Message message = new Message(recipient, msg);
                    if (!message.checkRecipientCell()) {
                        JOptionPane.showMessageDialog(null, "Invalid recipient number.");
                        break;
                    }

                    String result = message.sendMessageOption();
                    message.displayMessageDetails();
                    JOptionPane.showMessageDialog(null, result);
                    messagesEntered++;
                }

                case "2" -> Message.showStoredMessages();

                case "3" -> {
                    JOptionPane.showMessageDialog(null,
                            "Total messages sent: " + Message.returnTotalMessages() + "\nGoodbye!");
                    return;
                }

                default -> JOptionPane.showMessageDialog(null, "Invalid selection.");
            }
        }
    }

    // Utility function to handle null input
    private static String prompt(String message) {
        String input = JOptionPane.showInputDialog(message);
        if (input == null) {
            JOptionPane.showMessageDialog(null, "Cancelled. Exiting...");
            System.exit(0);
        }
        return input.trim();
    }
    
}
