package nl.fhict.s3.sharedmodel;

public class User {

    private int id;
    private String name;
    private String password;
    private String email;
    private String lastname;

    public User() {
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public String getLastname() {return lastname;}

    public void setLastname(String lastname) {this.lastname = lastname;}
}
