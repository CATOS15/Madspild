package madspild.Models;

public class User {


    int UserID;
    String Username;
    String Email;
    String Passwords;


    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPasswords() {
        return Passwords;
    }

    public void setPasswords(String passwords) {
        Passwords = passwords;
    }


}
