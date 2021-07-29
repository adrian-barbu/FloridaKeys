package com.floridakeys.model.venue;

import com.floridakeys.model.BaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @description     Venue Summary Model
 *
 * @author          Adrian
 */
public class VenueSummary extends BaseModel {

    // Member Variables
    private String mID;
    private String mName;
    private String mOpenTime, mCloseTime;
    private String mOpenDay, mCloseDay;
    private String mAddress, mCity, mState, mZipCode;
    private String mImageUrl;
    private double mLatitude, mLongitude;
    private String mPhoneNumber;
    private String mWebsite;
    private int mReviewCount;
    private float mRating;
    private ArrayList<VenueMenu> mMenus;
    private ArrayList<VenueSpecial> mSpecials;
    private String mEvents;

    // Member Functions
    public VenueSummary() {
        super();
    }

    // Getter
    public String getID() { return mID; }
    public String getName() { return mName; }

    public String getOpenTime() { return mOpenTime; }
    public String getCloseTime() { return mCloseTime; }
    public String getOpenDay() { return mOpenDay; }
    public String getCloseday() { return mCloseDay; }

    public String getAddress() { return mAddress; }
    public String getCity() { return mCity; }
    public String getState() { return mState; }
    public String getZipCode() { return mZipCode; }

    public String getImageUrl() { return mImageUrl; }
    public double getLatitude() { return mLatitude; }
    public double getLongitude() { return mLongitude; }

    public String getPhoneNumber() { return mPhoneNumber; }
    public String getWebsite() { return mWebsite; }
    public int getReviewCount() { return mReviewCount; }
    public float getRating() { return mRating; }

    public ArrayList<VenueMenu> getMenus() { return mMenus; }
    public String getEvents() { return mEvents; }
    public ArrayList<VenueSpecial> getSpecials() { return mSpecials; }

    // Json Parser
    @Override
    public boolean doParseJson(JSONObject jsonObject) {
        mID = getStringValue(jsonObject, "id");
        mName = getStringValue(jsonObject, "name");

        mOpenTime = getStringValue(jsonObject, "openTime");
        mCloseTime = getStringValue(jsonObject, "closeTime");
        mOpenDay = getStringValue(jsonObject, "openDay");
        mCloseDay = getStringValue(jsonObject, "closeDay");

        mAddress = getStringValue(jsonObject, "address");
        mCity = getStringValue(jsonObject, "city");
        mState = getStringValue(jsonObject, "state");
        mZipCode = getStringValue(jsonObject, "zipcode");
        
        mImageUrl = getStringValue(jsonObject, "photo");

        mLatitude = getDoubleValue(jsonObject, "latitude");
        mLongitude = getDoubleValue(jsonObject, "longitude");

        mPhoneNumber = getStringValue(jsonObject, "phone");
        mWebsite = getStringValue(jsonObject, "website");
        mReviewCount = getIntValue(jsonObject, "reviewCount");
        mRating = (float) getDoubleValue(jsonObject, "rating");

        // Parse Venue Menus
        parseVenueMenus(jsonObject);

        // Parse Venue Specials
        parseVenueSpecials(jsonObject);

        return (mID != null && !mID.isEmpty());
    }

    /**
     * Parse Venue Menus
     *
     * @param jsonObject
     */
    private void parseVenueMenus(JSONObject jsonObject) {
        try {
            JSONArray menus = jsonObject.getJSONArray("menus");
            if (menus != null) {
                if (menus.length() > 0)
                    mMenus = new ArrayList<>();

                for (int i = 0; i < menus.length(); i++) {
                    try {
                        JSONObject data = menus.getJSONObject(i);
                        VenueMenu menu = new VenueMenu();
                        if (menu.doParseJson(data))
                            mMenus.add(menu);
                    } catch (Exception e) {

                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parse Venue Specials
     *
     * @param jsonObject
     */
    private void parseVenueSpecials(JSONObject jsonObject) {
        try {
            JSONArray specials = jsonObject.getJSONArray("specials");
            if (specials != null) {
                if (specials.length() > 0)
                    mSpecials = new ArrayList<>();

                for (int i = 0; i < specials.length(); i++) {
                    try {
                        JSONObject data = specials.getJSONObject(i);
                        VenueSpecial special = new VenueSpecial();
                        if (special.doParseJson(data))
                            mSpecials.add(special);
                    } catch (Exception e) {

                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        // To do here

        super.onDestroy();
    }
}
