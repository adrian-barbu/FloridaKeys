package com.floridakeys.model.artist;

import com.floridakeys.model.BaseModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @description     Artist Music Model
 *
 * @author          Adrian
 */
public class ArtistMusic extends BaseModel {

    // Member Variables
    private String mID;     // This is id of song
    private String mName;
    private String mDuration;
    private String mPublish;
    private String mUrl;

    // Member Functions
    public ArtistMusic() {
        super();
    }

    // Getter
    public String getID() { return mID; }
    public String getName() { return mName; }

    public String getDuration() { return mDuration; }
    public String getPublish() { return mPublish; }
    public String getUrl() { return mUrl; }

    // Json Parser
    @Override
    public boolean doParseJson(JSONObject jsonObject) {
        mID = getStringValue(jsonObject, "id");
        mName = getStringValue(jsonObject, "name");
        mUrl = getStringValue(jsonObject, "url");

        mDuration = getStringValue(jsonObject, "duration");
        mPublish = getStringValue(jsonObject, "publish");

        return (mID != null && !mID.isEmpty());
    }

    @Override
    protected void onDestroy() {
        // To do here

        super.onDestroy();
    }
}
