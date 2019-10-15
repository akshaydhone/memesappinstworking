package Model;

public class Users {

    private String id;
    private String Bio;
    private String UserName;
    private String Name;
    private String Image_Url;
    private String Email;


    public Users() {
    }



    public Users(String id, String bio, String username, String name, String email,String image_url) {
        this.id = id;
        Bio = bio;
        UserName = username;
        Name = name;
        Email = email;
        Image_Url=image_url;
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

    public String getImage_Url() {
        return Image_Url;
    }



    public void setImage_Url(String image_Url) {
        Image_Url = image_Url;
    }





}
