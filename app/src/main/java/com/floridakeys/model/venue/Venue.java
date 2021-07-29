package com.floridakeys.model.venue;

import android.os.Parcel;
import android.os.Parcelable;

import com.floridakeys.model.BaseModel;

import org.json.JSONObject;

/**
 * @description     Venue Model
 *
 * @author          Adrian
 */
public class Venue extends BaseModel implements Parcelable {

    // Member Variables
    private String mID;
    private String mName;
    private String mAddress, mCity, mState, mZipCode;
    private String mImageUrl;
    private double mLatitude, mLongitude;
    private double mDistance;

    // Member Functions
    public Venue() {
        super();
    }

    // Getter
    public String getID() { return mID; }
    public String getName() { return mName; }
    public String getImageUrl() { return mImageUrl; }

    public String getAddress() { return mAddress; }
    public String getCity() { return mCity; }
    public String getState() { return mState; }
    public String getZipCode() { return mZipCode; }

    public double getLatitude() { return mLatitude; }
    public double getLongitude() { return mLongitude; }
    public double getDistance() { return mDistance; }

    // Json Parser
    @Override
    public boolean doParseJson(JSONObject jsonObject) {
        mID = getStringValue(jsonObject, "id");
        mName = getStringValue(jsonObject, "name");
        mAddress = getStringValue(jsonObject, "address");
        mCity = getStringValue(jsonObject, "city");
        mState = getStringValue(jsonObject, "state");
        mZipCode = getStringValue(jsonObject, "zipcode");
        mImageUrl = getStringValue(jsonObject, "photo");
        mLatitude = getDoubleValue(jsonObject, "latitude");
        mLongitude = getDoubleValue(jsonObject, "longitude");
        mDistance = getDoubleValue(jsonObject, "distance");
        return (mID != null && !mID.isEmpty());
    }

    @Override
    protected void onDestroy() {
        // To do here

        super.onDestroy();
    }

    ////////////// Manage Parcelable //////////////
    public Venue(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mID);
        dest.writeString(mName);
        dest.writeString(mAddress);
        dest.writeString(mCity);
        dest.writeString(mState);
        dest.writeString(mZipCode);
        dest.writeString(mImageUrl);
        dest.writeDouble(mLatitude);
        dest.writeDouble(mLongitude);
        dest.writeDouble(mDistance);
    }

    private void readFromParcel(Parcel in) {
        mID = in.readString();
        mName = in.readString();
        mAddress = in.readString();
        mCity = in.readString();
        mState = in.readString();
        mZipCode = in.readString();
        mImageUrl = in.readString();
        mLatitude = in.readDouble();
        mLongitude = in.readDouble();
        mDistance = in.readDouble();
    }

    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public Venue createFromParcel(Parcel in) {
                    return new Venue(in);
                }

                public Venue[] newArray(int size) {
                    return new Venue[size];
                }
            };
}
