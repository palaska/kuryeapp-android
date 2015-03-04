package com.mobile.kuryeapp.kuryeappv01.Classes;

public class customJob {
    String _id;
    int index;
    String name;
    String address;
    String addressdesc;
    customLocation location;
    int __v;
    String type;
    String phone;

    public customJob(String _id, int index,String name, String address, String addressdesc, customLocation location, int __v, String type, String phone) {
        this._id = _id;
        this.index = index;
        this.name = name;
        this.address = address;
        this.addressdesc = addressdesc;
        this.location = location;
        this.__v = __v;
        this.type = type;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getAddressdesc() {
        return addressdesc;
    }

    public String getPhone() {
        return phone;
    }

    public String getType() {
        return type;
    }

    public customLocation getLocation() {
        return location;
    }
}
