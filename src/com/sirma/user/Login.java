package com.sirma.user;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login {

    private final String[] forbidden = {"select", "insert", "update", "delete", "into", "create", "alter", "drop", "table", "backup", "database", "from", "where", "desc", "exec", "group by", "join", "union", "truncate", "unique"};

    public void signUp(String username, String password, String mobileNumber) {
        if(!isValid(username)){
            throw new IllegalArgumentException ("Please pick another username...");
        }
        if(!isValid(password)){
            throw new IllegalArgumentException("Please pick another password...");
        }

        User user = UserService.readFromFile(username);
        if(user!=null){
            throw new RuntimeException("User with such username "+ user.getUsername() + " already exists!");
        }

        isNumber(mobileNumber);
        user = new User(username,password,mobileNumber);
        UserService.saveToFile(user);
        System.out.println("Successfully signed up!");
    }

    public User login(String username, String password) {
        if(!isValid(username)){
            throw new IllegalArgumentException ("Please pick another username...");
        }
        if(!isValid(password)){
            throw new IllegalArgumentException("Please pick another password...");
        }

        User user = UserService.readFromFile(username);
        if(user==null){
            throw new RuntimeException("User with such username "+ user.getUsername() + " doesn't exists!");
        }
        return user;
    }

    boolean isNumber(String mobileNumber){
        if(mobileNumber.length()!=10){
            throw new IllegalArgumentException("Not a mobile number!");
        }

        if(!isMobile(mobileNumber)){
            throw new IllegalArgumentException("Not a bulgarian Mobile number!");
        }

        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(mobileNumber);

        if(!matcher.find()){
            throw new IllegalArgumentException("number is not valid");
        }
            return true;
    }

    boolean isMobile(String number){
        return number.startsWith("359") || number.startsWith("0892") || number.startsWith("0895") || number.startsWith("0898");
    }

    public boolean isValid(String data) {
        for (String str : forbidden
        ) {
            if (data.contains(str)) {
                return false;
            }

        }
        return true;
    }
}
