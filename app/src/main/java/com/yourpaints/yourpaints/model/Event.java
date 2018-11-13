package com.yourpaints.yourpaints.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

@Entity(tableName = "events")
public class Event implements Parcelable {

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "event_id")
    String eventId;
    @ColumnInfo(name = "event_desc")
    String eventDescription;
    @ColumnInfo(name = "event_owner")
    String eventOwner;
    @ColumnInfo(name = "location")
    String location;
    @ColumnInfo(name = "event_timing")
    long dateTime;

    public Event(String eventId, String eventDescription, String eventOwner, String location, long dateTime) {
        this.eventId = eventId;
        this.eventDescription = eventDescription;
        this.eventOwner = eventOwner;
        this.location = location;
        this.dateTime = dateTime;
    }

    @Ignore
    protected Event(Parcel in) {
        eventId = in.readString();
        eventDescription = in.readString();
        eventOwner = in.readString();
        location = in.readString();
        dateTime = in.readLong();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventOwner() {
        return eventOwner;
    }

    public void setEventOwner(String eventOwner) {
        this.eventOwner = eventOwner;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(eventId);
        parcel.writeString(eventDescription);
        parcel.writeString(eventOwner);
        parcel.writeString(location);
        parcel.writeLong(dateTime);
    }
}
