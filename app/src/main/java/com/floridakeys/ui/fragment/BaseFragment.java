package com.floridakeys.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.floridakeys.MainActivity;
import com.floridakeys.R;
import com.floridakeys.model.artist.Artist;
import com.floridakeys.network.netConfig;
import com.floridakeys.ui.fragment.nearby.NearByMainFragment;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * @description Base Fragment
  *
 * @author      Adrian
 */

public class BaseFragment extends Fragment
{
    protected String mTitle;        // Title
    protected String getTitle() { return mTitle; }

    protected View mRootView;
    public boolean isTopParent = false;

    // Async Http Request Object
    AsyncHttpClient mHttpClient;

    protected int DELAY_FOR_UI = 2 * 1000;
    protected final static int PAGE_COUNT = 10;

    // Param Constant
    protected final String PARAM_ARTIST = "artist";                 // Artist Param
    protected final String PARAM_ARTIST_BIOGRAPHY = "artist_bio";   // Artist Biography

    protected final String PARAM_VENUE = "venue";                   // Venue Param
    protected final String PARAM_VENUE_OVERAL_RATINGS = "venue_rating";    // Venue Overal Rating Param
    protected final String PARAM_VENUE_MENUS = "venue_menus";       // Venue Menus
    protected final String PARAM_VENUE_SPECIALS = "venue_specials";       // Venue Specials

    protected final String PARAM_EVENT = "event";                   // Event Param
    protected final String PARAM_EVENT_TYPE = "event_type";         // Event Param

    public final static int EVENT_TYPE_FOR_HYBRID = 1000;
    public final static int EVENT_TYPE_FOR_ARTIST = 1001;
    public final static int EVENT_TYPE_FOR_VENUE = 1002;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_base, container, false);
        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    protected void setTitle(String title) {
        mTitle = title;
        setMainTitle(mTitle);
    };

    protected void showToast(int res_id) {
        Toast.makeText(getActivity(), res_id, Toast.LENGTH_SHORT).show();
    }

    /**
     * Add Sub Fragment
     *
     * @param fragment  : Fragment
     * @param useAnimation
     */
    protected void addFragment(BaseFragment fragment, boolean useAnimation) {
        if (fragment != null) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            if (useAnimation)
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
            transaction.add(R.id.root_frame, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    /**
     * Current Fragment
     *
     * @return
     */
    public BaseFragment getTopFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments == null || fragments.size() == 0)
            return null;

        BaseFragment lastFragment = (BaseFragment)fragments.get(fragments.size() - 1);
        return lastFragment;
    }

    /**
     * Remove Fragment
     *
     */
    public void removeChildFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments == null || fragments.size() == 0)
            return;

        BaseFragment lastFragment = (BaseFragment)fragments.get(fragments.size() - 1);
        if (lastFragment != null && !lastFragment.isTopParent) {
            // Get / Set Previous Title
            String prevTitle = "";
            try {
                BaseFragment lastSecondFragment = (BaseFragment) fragments.get(fragments.size() - 2);
                prevTitle = lastSecondFragment.getTitle();
            } catch (Exception e) {}

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
            transaction.remove(lastFragment);
            transaction.commit();

            fragments.remove(lastFragment);

            setMainTitle(prevTitle);
        }
    }

    protected void setMainTitle(String title) {
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null)
            activity.setTitle(title);
    }

    /**
     * Controlling loading layout
     */
    protected void showLoadingLayout() {
        View layoutLoading = (View) mRootView.findViewById(R.id.layoutLoading);
        if (layoutLoading != null)
            layoutLoading.setVisibility(View.VISIBLE);
    }

    protected void hideLoadingLayout() {
        View layoutLoading = (View) mRootView.findViewById(R.id.layoutLoading);
        if (layoutLoading != null)
            layoutLoading.setVisibility(View.INVISIBLE);
    }


    /*************************************************/
    /**************** Network Module *****************/
    /*************************************************/

    /**
     * Call Web Service In Sub Fragments
     *
     * @param url
     * @param responseHandler
     */
    protected void callWebService(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
       if (mHttpClient == null) {
           mHttpClient = new AsyncHttpClient();
           mHttpClient.setTimeout(netConfig.HTTP_REQUEST_TIMEOUT);
           mHttpClient.setConnectTimeout(netConfig.HTTP_REQUEST_TIMEOUT);
           mHttpClient.setResponseTimeout(netConfig.HTTP_RESPONSE_TIMEOUT);
       }

       mHttpClient.post(url, params, responseHandler);
    }

    /**
     * Cancel All Requests
     */
    protected void cancelRequests() {
        if (mHttpClient != null)
            mHttpClient.cancelAllRequests(true);
    }

    /**
     * Get Json Array From Server Data
     *
     * @param responseBody
     * @return
     */
    protected JSONArray getJsonArrayFromResponse(byte[] responseBody) {
        JSONArray jsonArrayData = null;

        try {
            String response = new String(responseBody);
            JSONObject object = new JSONObject(response);
            boolean success = object.getBoolean("success");

            if (success)
                jsonArrayData = object.getJSONArray("data");
        } catch (Exception e) {

        }

        return jsonArrayData;
    }

    /**
     * Get Json Data From Server Data
     *
     * @param responseBody
     * @return
     */
    protected JSONObject getJsonFromResponse(byte[] responseBody) {
        JSONObject jsonObject = null;

        try {
            String response = new String(responseBody);
            JSONObject object = new JSONObject(response);
            boolean success = object.getBoolean("success");

            if (success)
                jsonObject = object.getJSONObject("data");
        } catch (Exception e) {

        }

        return jsonObject;
    }
}
