package com.example.a101;

public class LoginData {
    // Member variables
    public String username;
    public String password;

    // Constructor to initialize username and password
    public LoginData(String username, String password){
        this.username = username;
        this.password = password;
    }

    // Getter method to retrieve the username
    public String getUsername(){
        return username;
    }

    // Getter method to retrieve the password
    public String getPassword(){
        return password;
    }
}
