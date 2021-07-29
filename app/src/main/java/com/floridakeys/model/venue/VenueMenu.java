package com.floridakeys.model.venue;

import android.os.Parcel;
import android.os.Parcelable;

import com.floridakeys.model.BaseModel;

import org.json.JSONObject;

/**
 * @description     Venue Menu Model
 *
 * @author          Adrian
 */
public class VenueMenu extends BaseModel implements Parcelable {

    // Member Variables
    private String mID;
    private String mName;
    private String mDescription;

    // Member Functions
    public VenueMenu() {
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
    public VenueMenu(Parcel in) {
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

    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public VenueMenu createFromParcel(Parcel in) {
                    return new VenueMenu(in);
                }

                public VenueMenu[] newArray(int size) {
                    return new VenueMenu[size];
                }
            };

}
