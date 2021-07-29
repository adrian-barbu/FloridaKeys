package com.floridakeys.model.venue;

import android.os.Parcel;
import android.os.Parcelable;

import com.floridakeys.model.BaseModel;

import org.json.JSONObject;

/**
 * @description     Venue Special Model
 *
 * @author          Adrian
 */
public class VenueSpecial extends BaseModel implements Parcelable {

    // Member Variables
    private String mID;
    private String mName;
    private String mDescription;

    // Member Functions
    public VenueSpecial() {
        super();
    }

    // Getter
    public String getID() { return mID; }
    public String getName() { return mName; }
    public String getDescription() { return mDescription; }

    // Json Parser
    @Override
    public boolean doParseJson(JSONObject jsonObject) {
        mID = getStringValue(jsonObject, "id");
        mName = getStringValue(jsonObject, "name");
        mDescription = getStringValue(jsonObject, "desc");
        return (mDescription != null && !mDescription.isEmpty());
    }

    @Override
    protected void onDestroy() {
        // To do here

        super.onDestroy();
    }

    ////////////// Manage Parcelable //////////////
    public VenueSpecial(Parcel in) {
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
        dest.writeString(mDescription);
    }

    private void readFromParcel(Parcel in) {
        mID = in.readString();
        mName = in.readString();
        mDescription = in.readString();
    }

    public static final Creator CREATOR =
            new Creator() {
                public VenueSpecial createFromParcel(Parcel in) {
                    return new VenueSpecial(in);
                }

                public VenueSpecial[] newArray(int size) {
                    return new VenueSpecial[size];
                }
            };

}
