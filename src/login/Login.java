 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

package login;

import javax.swing.JOptionPane;
//import java.util.regex.*;

public class Login {
    private final String username;
    private final String password;
    private final String cellNumber;
    private final String firstName;
    private final String lastName;

    // Constructor
    public Login(String firstName, String lastName, String username, String password, String cellNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.cellNumber = cellNumber;
    }

    // Method to check username
    public boolean checkUserName() {
        return username.contains("_") && username.length() <= 5;
    }

    // Method to check password complexity
    public boolean checkPasswordComplexity() {
        return password.matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+=\\-]).{8,}$");

    }

    // Method to check cellphone number
    public boolean checkCellPhoneNumber() {
        // Valid SA number format: +27 followed by 9 digits
        return cellNumber.matches("^\\+27\\d{9}$");
    }

    // Registration logic
    public String registerUser() {
    

        if (!checkUserName()) {
            return "Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.";
        }
        if (!checkPasswordComplexity()) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }
        if (!checkCellPhoneNumber()) {
            return "Cell phone number incorrectly formatted or does not contain international code.";
        }
        else 
            return "Registration successful!";
    }
    
    // Login logic
    public boolean loginUser(String enteredUsername, String enteredPassword) {
        return this.username.equals(enteredUsername) && this.password.equals(enteredPassword);
    }

    // Login feedback
    public String returnLoginStatus(boolean loginSuccess) {
        while(true){
        if (loginSuccess) {
            return "Welcome " + firstName + " " + lastName + ", it is great to see you again.";
            
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
    }
    // Main method
    public static void main(String[] args) {
    String firstName = JOptionPane.showInputDialog("Enter your first name:");
    String lastName = JOptionPane.showInputDialog("Enter your last name:");

    String username;
    while (true) {
        username = JOptionPane.showInputDialog("Enter a username (must contain an underscore and be no more than 5 characters):");
        Login temp = new Login(firstName, lastName, username, "Temp123!", "+27123456789"); // dummy pass/cell for validation
        if (!temp.checkUserName()) {
            JOptionPane.showMessageDialog(null, "Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.");
        } else {
            break;
        }
    }

    String password;
    while (true) {
        password = JOptionPane.showInputDialog("Enter a password (min 8 characters, 1 capital, 1 number, 1 special char):");
        Login temp = new Login(firstName, lastName, username, password, "+27123456789");
        if (!temp.checkPasswordComplexity()) {
            JOptionPane.showMessageDialog(null, "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.");
        } else {
            break;
        }
    }

    String cellNumber;
    while (true) {
        cellNumber = JOptionPane.showInputDialog("Enter your South African cellphone number (e.g. +27831234567):");
        Login temp = new Login(firstName, lastName, username, password, cellNumber);
        if (!temp.checkCellPhoneNumber()) {
            JOptionPane.showMessageDialog(null, "Cell phone number incorrectly formatted or does not contain international code. Please re-enter.");
        } else {
            break;
        }
    }

    // Final user creation after all validations
    Login user = new Login(firstName, lastName, username, password, cellNumber);
    JOptionPane.showMessageDialog(null, user.registerUser());

    // Login loop with 5 attempts
    int attemptsLeft = 5;
    boolean isLoggedIn = false;

    while (attemptsLeft > 0 && !isLoggedIn) {
        String loginUsername = JOptionPane.showInputDialog("Login - Enter your username:");
        String loginPassword = JOptionPane.showInputDialog("Login - Enter your password:");
        isLoggedIn = user.loginUser(loginUsername, loginPassword);

        if (isLoggedIn) {
            JOptionPane.showMessageDialog(null, user.returnLoginStatus(true));
        } else {
            attemptsLeft--;
            if (attemptsLeft > 0) {
                JOptionPane.showMessageDialog(null, "Username or password incorrect. You have " + attemptsLeft + " attempt(s) left.");
            } else {
                JOptionPane.showMessageDialog(null, "Too many failed login attempts. Access denied.");
            }
        }
    }
}
}
