package Model;

public class Users {

    private String id;
    private String Bio;
    private String UserName;
    private String Name;
    private String Email;


    public Users() {
    }


    public Users(String id, String bio, String username, String name, String email) {
        this.id = id;
        Bio = bio;
        UserName = username;
        Name = name;
        Email = email;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBio() {
        return Bio;
    }

    public void setBio(String bio) {
        Bio = bio;
    }

    public String getUsername() {
        return UserName;
    }

    public void setUsername(String username) {
        UserName = username;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
