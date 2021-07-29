package com.floridakeys.model.artist;

import android.os.Parcel;
import android.os.Parcelable;

import com.floridakeys.model.BaseModel;

import org.json.JSONObject;

/**
 * @description     Artist Model
 *
 * @author          Adrian
 */
public class Artist extends BaseModel implements Parcelable {

    // Member Variables
    private String mID;
    private String mFirstName, mLastName;
    private String mImageUrl;

    // Member Functions
    public Artist() {
        super();
    }

    // Getter
    public String getID() { return mID; }
    public String getFirstName() { return mFirstName; }
    public String getLastName() { return mLastName; }
    public String getFullName() { return String.format("%s %s", mFirstName, mLastName); }
    public String getImageUrl() { return mImageUrl; }

    // Json Parser
    @Override
    public boolean doParseJson(JSONObject jsonObject) {
        mID = getStringValue(jsonObject, "id");
        mFirstName = getStringValue(jsonObject, "firstname");
        mLastName = getStringValue(jsonObject, "lastname");
        mImageUrl = getStringValue(jsonObject, "photo");
        return (mID != null && !mID.isEmpty());
    }

    @Override
    protected void onDestroy() {
        // To do here

        super.onDestroy();
    }

    ////////////// Manage Parcelable //////////////
    public Artist(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mID);
        dest.writeString(mFirstName);
        dest.writeString(mLastName);
        dest.writeString(mImageUrl);
    }

    private void readFromParcel(Parcel in) {
        mID = in.readString();
        mFirstName = in.readString();
        mLastName = in.readString();
        mImageUrl = in.readString();
    }

    public static final Creator CREATOR =
            new Creator() {
                public Artist createFromParcel(Parcel in) {
                    return new Artist(in);
                }

                public Artist[] newArray(int size) {
                    return new Artist[size];
                }
            };
}
