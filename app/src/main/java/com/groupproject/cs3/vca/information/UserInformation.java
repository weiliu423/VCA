package com.groupproject.cs3.vca.information;

/**
 * Created by User on 2/8/2017.
 */

public class UserInformation {

    private String name;
    private String phone_num;
    private String type;
    private String uid;



    public UserInformation(String name, String phone_num, String type, String uid) {
        this.name = name;
        this.phone_num = phone_num;
        this.type = type;
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    public String gettype() {
        return type;
    }

    public void settype(String type) {
        this.type = type;
    }

    public String getuid() {
        return uid;
    }

    public void setuid(String uid) {
        this.uid = uid;
    }
}
