package sample.model;

import java.util.List;

public class User {
    private String id;
    private String name;
    private String username;
    private String email;
    private String password;
    private Group group_id;

    public User(String id, String name, String username, String email, String password, Group group) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.group_id = group;
    }



    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", group_id=" + group_id +
                '}';
    }

    public User() {
    }

    public Group getGroup() {
        return group_id;
    }

    public void setGroup(Group group) {
        this.group_id = group;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
