package com.example.emailsignup;

class DonationForm {
    public String patient_name, patient_bgroup, in_need_user_id, donor_user_id, hospital_address, don_date, don_city, don_country, donor_des;
    public Boolean is_completed;
    public DonationForm() {

    }
    public DonationForm(String patient_name, String patient_bgroup, String in_need_user_id, String donor_user_id, String hospital_address, String don_date, String don_city, String don_country, String donor_des, Boolean is_completed) {
        this.patient_name = patient_name;
        this.patient_bgroup = patient_bgroup;
        this.in_need_user_id = in_need_user_id;
        this.donor_user_id = donor_user_id;
        this.hospital_address = hospital_address;
        this.don_date = don_date;
        this.don_city = don_city;
        this.don_country = don_country;
        this.donor_des = donor_des;
        this.is_completed = is_completed;
    }
}
