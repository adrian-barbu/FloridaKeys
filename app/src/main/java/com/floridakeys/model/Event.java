package com.floridakeys.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

/**
 * @description     Event Model
 *
 * @author          Adrian
 */
public class Event extends BaseModel implements Parcelable {

    // Member Variables
    private String mID;         // This is event id
    private String mDate, mTime;
    private String mTitle, mDescription;

    private String mArtistName, mArtistImage;
    private String mVenueName, mVenueImage;

    private String mAddress, mCity, mState, mZipCode;
    private double mLatitude, mLongitude;

    private String mTickets;
    private String mBuyTicketLink;

    // Member Functions
    public Event() {
        super();
    }

    // Getter
    public String getID() { return mID; }
    public String getDate() { return mDate; }
    public String getTime() { return mTime; }
    public String getTitle() { return mTitle; }
    public String getDescription() { return mDescription; }

    public String getArtistName() { return mArtistName; }
    public String getArtistImage() { return mArtistImage; }

    public String getVenueName() { return mVenueName; }
    public String getVenueImage() { return mVenueImage; }

    public String getAddress() { return mAddress; }
    public String getCity() { return mCity; }
    public String getState() { return mState; }
    public String getZipCode() { return mZipCode; }

    public double getLatitude() { return mLatitude; }
    public double getLongitude() { return mLongitude; }

    public String getTickets() { return mTickets; }
    public String getBuyTicketLink() { return mBuyTicketLink; }

    // Json Parser
    @Override
    public boolean doParseJson(JSONObject jsonObject) {
        mID = getStringValue(jsonObject, "id");
        mDate = getStringValue(jsonObject, "date");
        mTime = getStringValue(jsonObject, "time");
        mTitle = getStringValue(jsonObject, "title");
        mDescription = getStringValue(jsonObject, "desc");

        mArtistName = getStringValue(jsonObject, "artistname");
        mArtistImage = getStringValue(jsonObject, "artistphoto");

        mVenueName = getStringValue(jsonObject, "venuename");
        mVenueImage = getStringValue(jsonObject, "venuephoto");

        mAddress = getStringValue(jsonObject, "address");
        mCity = getStringValue(jsonObject, "city");
        mState = getStringValue(jsonObject, "state");
        mZipCode = getStringValue(jsonObject, "zipcode");

        mLatitude = getDoubleValue(jsonObject, "latitude");
        mLongitude = getDoubleValue(jsonObject, "longitude");

        mTickets = getStringValue(jsonObject, "ticket");
        mBuyTicketLink = getStringValue(jsonObject, "buylink");
        return (mID != null && !mID.isEmpty());
    }

    ////////////// Manage Parcelable //////////////
    public Event(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mID);
        dest.writeString(mDate);
        dest.writeString(mTime);
        dest.writeString(mTitle);
        dest.writeString(mDescription);

        dest.writeString(mArtistName);
        dest.writeString(mArtistImage);

        dest.writeString(mVenueName);
        dest.writeString(mVenueImage);

        dest.writeString(mAddress);
        dest.writeString(mCity);
        dest.writeString(mState);

        dest.writeDouble(mLatitude);
        dest.writeDouble(mLongitude);

        dest.writeString(mTickets);
        dest.writeString(mBuyTicketLink);
    }

    private void readFromParcel(Parcel in) {
        mID = in.readString();
        mDate = in.readString();
        mTime = in.readString();
        mTitle = in.readString();
        mDescription = in.readString();

        mArtistName = in.readString();
        mArtistImage = in.readString();

        mVenueName = in.readString();
        mVenueImage = in.readString();

        mAddress = in.readString();
        mCity = in.readString();
        mState = in.readString();

        mLatitude = in.readDouble();
        mLongitude = in.readDouble();

        mTickets = in.readString();
        mBuyTicketLink = in.readString();
    }

    public static final Creator CREATOR =
            new Creator() {
                public Event createFromParcel(Parcel in) {
                    return new Event(in);
                }

                public Event[] newArray(int size) {
                    return new Event[size];
                }
            };

    @Override
    protected void onDestroy() {
        // To do here

        super.onDestroy();
    }
}
