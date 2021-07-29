package com.floridakeys.model.artist;

import com.floridakeys.model.BaseModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @description     Artist Summary Model
 *
 * @author          Adrian
 */
public class ArtistSummary extends BaseModel {

    // Member Variables
    private String mID;
    private String mName;
    private String mBiography;
    private String mMusicCount;

    private String mAddress;
    private String mCity, mState;
    private String mEmail, mWebsite;
    private String mType, mStyle;

    // Member Functions
    public ArtistSummary() {
        super();
    }

    // Getter
    public String getID() { return mID; }
    public String getName() { return mName; }
    public String getBiography() { return mBiography; }
    public String getMusicCount() { return mMusicCount; }

    public String getEmail() { return mEmail; }
    public String getWebSite() { return mWebsite; }
    public String getType() { return mType; }
    public String getStyle() { return mStyle; }

    // Json Parser
    @Override
    public boolean doParseJson(JSONObject jsonObject) {
        mID = getStringValue(jsonObject, "id");
        mName = getStringValue(jsonObject, "name");
        mBiography = getStringValue(jsonObject, "biography");
        mMusicCount = getStringValue(jsonObject, "musiccount");

        mEmail = getStringValue(jsonObject, "email");
        mWebsite = getStringValue(jsonObject, "website");
        mType = getStringValue(jsonObject, "type");
        mStyle = getStringValue(jsonObject, "style");
        return (mID != null && !mID.isEmpty());
    }

    @Override
    protected void onDestroy() {
        // To do here

        super.onDestroy();
    }
}
