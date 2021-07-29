package com.floridakeys.model.venue;

import android.os.Parcel;
import android.os.Parcelable;

import com.floridakeys.model.BaseModel;

import org.json.JSONObject;

/**
 * @description     Venue Review Model
 *
 * @author          Adrian
 */
public class VenueReview extends BaseModel {

    // Member Variables
    private String mID;
    private String mUserID;
    private String mUserName;
    private String mComment;
    private float mRating;
    private String mImageUrl;

    // Member Functions
    public VenueReview() {
        super();
    }

    // Getter
    public String getID() { return mID; }
    public String getUserID() { return mUserID; }
    public String getUserName() { return mUserName; }
    public String getComment() { return mComment; }
    public float getRating() { return mRating; }
    public String getImageUrl() { return mImageUrl; }

    // Json Parser
    @Override
    public boolean doParseJson(JSONObject jsonObject) {
        mID = getStringValue(jsonObject, "id");
        mUserID = getStringValue(jsonObject, "user");
        mUserName = getStringValue(jsonObject, "username");
        mComment = getStringValue(jsonObject, "comment");
        mRating = (float)getDoubleValue(jsonObject, "rating");
        mImageUrl = getStringValue(jsonObject, "photo");
        return (mUserName != null && !mUserName.isEmpty());
    }

    @Override
    protected void onDestroy() {
        // To do here

        super.onDestroy();
    }
}
