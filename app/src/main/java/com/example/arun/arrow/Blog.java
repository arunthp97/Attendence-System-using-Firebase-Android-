package com.example.arun.arrow;

public class Blog {
    String name,roll_no,branch,gender,time;
    public Blog(){}

    public Blog(String name, String roll_no, String branch, String gender,String time) {
        this.name = name;
        this.roll_no = roll_no;
        this.branch = branch;
        this.gender = gender;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoll_no() {
        return roll_no;
    }

    public void setRoll_no(String roll_no) {
        this.roll_no = roll_no;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
