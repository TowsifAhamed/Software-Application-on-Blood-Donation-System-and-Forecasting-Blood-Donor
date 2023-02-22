package com.example.emailsignup;

class User {
    public String email,name,phone,country,city,bday,bgroup,need_bgroup,lastdonday,lastlogin,reg_date;
    public Boolean donor,in_need;
    public Double latitude,longitude;
    public int times_donated,times_requested,rep_point;
    public User() {

    }
    public User(String email,Boolean donor,Boolean in_need,String name,String phone,String country,String city,String bday,
                Double latitude,Double longitude,String bgroup,String need_bgroup, String lastdonday, String lastlogin, String reg_date, int times_donated, int times_requested, int rep_point) {
        this.email = email;
        this.donor = donor;
        this.in_need = in_need;
        this.name = name;
        this.phone = phone;
        this.country = country;
        this.city = city;
        this.bday = bday;
        this.latitude=latitude;
        this.longitude=longitude;
        this.bgroup = bgroup;
        this.need_bgroup = need_bgroup;
        this.lastdonday = lastdonday;
        this.lastlogin = lastlogin;
        this.reg_date = reg_date;
        this.times_donated = times_donated;
        this.times_requested = times_requested;
        this.rep_point = rep_point;
    }
}
