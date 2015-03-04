package com.mobile.kuryeapp.kuryeappv01.Classes;

public class UserGot {
    String _id;
    int index;
    String email;
    String username;
    String access_token;
    String access_secret;
    int __v;
    String role;
    String phone;

    public UserGot(String _id, int index, String email, String username, String access_token, String access_secret, int __v,String role,String phone) {
        this._id = _id;
        this.index = index;
        this.email = email;
        this.username = username;
        this.access_token = access_token;
        this.access_secret = access_secret;
        this.__v = __v;
        this.role = role;
        this.phone = phone;
    }

    public String getRole() {

        return role;
    }

    public String get_id() {

        return _id;
    }

    public String getAccess_token() {
        return access_token;
    }
}
