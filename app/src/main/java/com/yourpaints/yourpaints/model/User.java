package com.yourpaints.yourpaints.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class User implements Parcelable {
    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
    String userId;
    String userName;
    String emailAddress;
    String mobileNo;
    String gender;
    String company;
    String college;
    String location;
    List<String> friendsArrId;

    public User(String emailAddress, String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
        this.emailAddress = emailAddress;
        this.mobileNo = null;
        this.gender = null;
        this.college = null;
        this.company = null;
        this.location = null;
        this.friendsArrId = null;
    }

    protected User(Parcel in) {
        userId = in.readString();
        userName = in.readString();
        emailAddress = in.readString();
        mobileNo = in.readString();
        gender = in.readString();
        company = in.readString();
        college = in.readString();
        location = in.readString();
        friendsArrId = in.createStringArrayList();
    }

    public List<String> getFriendsArrId() {
        return friendsArrId;
    }

    public void setFriendsArrId(List<String> friendsArrId) {
        this.friendsArrId = friendsArrId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userId);
        parcel.writeString(userName);
        parcel.writeString(emailAddress);
        parcel.writeString(mobileNo);
        parcel.writeString(gender);
        parcel.writeString(company);
        parcel.writeString(college);
        parcel.writeString(location);
        parcel.writeStringList(friendsArrId);
    }
}