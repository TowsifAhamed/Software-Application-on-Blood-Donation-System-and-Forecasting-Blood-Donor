package com.example.emailsignup;

class notification {
    public String description, donor_user_id;
    public Boolean is_respond;
    public notification() {

    }
    public notification(String description, String donor_user_id, Boolean is_respond) {
        this.description = description;
        this.donor_user_id = donor_user_id;
        this.is_respond = is_respond;
    }
}
