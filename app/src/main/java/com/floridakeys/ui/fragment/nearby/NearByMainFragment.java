package com.floridakeys.ui.fragment.nearby;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;

import com.floridakeys.R;
import com.floridakeys.model.venue.Venue;
import com.floridakeys.network.netConfig;
import com.floridakeys.ui.fragment.BaseFragment;
import com.floridakeys.ui.fragment.event.EventDetailFragment;
import com.floridakeys.util.MyLocation;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


/**
 * @description NearBy Main Fragment
 *              This fragment has map-based venues
 *
 * @author      Adrian
 */

public class NearByMainFragment extends BaseFragment implements OnMapReadyCallback, GoogleMap.OnCameraChangeListener
{
    // UI Members
    Button btnDetail;

    // Variables
    GoogleMap mMap;
    LatLngBounds.Builder mBoundsBuilder;
    LatLngBounds mMapBounds;

    ArrayList<Venue> mVenues;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_nearby, container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setTitle(getString(R.string.sidebar_nearby));

        isTopParent = true;

        btnDetail = (Button) mRootView.findViewById(R.id.btnDetail);
        btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseFragment fragment = new EventDetailFragment();
                Bundle bundle = new Bundle();
                //bundle.putString("name", cateName);
                fragment.setArguments(bundle);

                addFragment(fragment, true);
            }
        });

        mVenues = new ArrayList<>();

        // Start to map sync
        mBoundsBuilder = new LatLngBounds.Builder();

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapViewer);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnCameraChangeListener(this);

        doRequestLoadMore();
    }

    /**
     * Request load more data
     */
    private void doRequestLoadMore() {
        double latitude, longitude;
        latitude = MyLocation.getInstance(getActivity()).getLatitude();
        longitude = MyLocation.getInstance(getActivity()).getLongitude();

        // Send Request to Server
        RequestParams params = new RequestParams();
        params.add("lat", String.valueOf(latitude));
        params.add("lng", String.valueOf(longitude));
        //params.add("distance", lastDistance);
        params.add("count", String.valueOf(PAGE_COUNT));

        callWebService(netConfig.SERVICE_NEARBY_VENUES, params, mGetVenuesResponseHandler);
    }

    /**
     * Response Handler
     */
    private AsyncHttpResponseHandler mGetVenuesResponseHandler = new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            //hideLoadingLayout();

            JSONArray datas = getJsonArrayFromResponse(responseBody);
            if (datas != null) {
                for (int i = 0; i < datas.length(); i++) {
                    try {
                        JSONObject data = datas.getJSONObject(i);
                        Venue venue = new Venue();
                        if (venue.doParseJson(data)) {
                            mVenues.add(venue);
                            addLocation(venue.getName(), venue.getAddress(), venue.getLatitude(), venue.getLongitude());
                        }
                    } catch (Exception e) {

                    }
                }

                // Update ui
                //if (datas.length() > 0)
                //    updateUI();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
           // hideLoadingLayout();
        }
    };

    @Override
    public void onCameraChange(CameraPosition cameraPosition)
    {
        // Listener of zooming;
        float zoomLevel = cameraPosition.zoom;
        VisibleRegion visibleRegion = mMap.getProjection().getVisibleRegion();
        LatLng nearLeft = visibleRegion.nearLeft;
        LatLng nearRight = visibleRegion.nearRight;
        LatLng farLeft = visibleRegion.farLeft;
        LatLng farRight = visibleRegion.farRight;
        //double dist_w = distanceFrom(nearLeft.latitude, nearLeft.longitude, nearRight.latitude, nearRight.longitude);
        //double dist_h = distanceFrom(farLeft.latitude, farLeft.longitude, farRight.latitude, farRight.longitude);
        //Log.d("DISTANCE: ", "DISTANCE WIDTH: " + dist_w + " DISTANCE HEIGHT: " + dist_h);
    }

    /**
     * Add Location on Map
     *
     * @param address
     * @param latitude
     * @param longitude
     */
    protected void addLocation(String title, String address, double latitude, double longitude) {
        LatLng location = new LatLng(latitude, longitude);
        MarkerOptions mo = new MarkerOptions();
        mMap.addMarker(new MarkerOptions()
                .position(location)
                .title(title)
                .snippet(String.format("%n%s", address))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker_unselected)));

        mBoundsBuilder.include(location);

        moveCameraWithZoom(14);
    }

    /**
     * Move Camera To Special Location
     */
    protected void moveCameraWithZoom(int zoomLevel) {
        final View mapView = getChildFragmentManager().findFragmentById(R.id.mapViewer).getView();
        final CameraUpdate zoom = CameraUpdateFactory.zoomTo(zoomLevel);

        try {
            mMapBounds = mBoundsBuilder.build();
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(mMapBounds, 0));
            mMap.animateCamera(zoom, 1000, null);
        } catch (Exception e) {
            if (mapView.getViewTreeObserver().isAlive()) {
                mapView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @SuppressWarnings("deprecation") // We use the new method when supported
                    @SuppressLint("NewApi") // We check which build version we are using.
                    @Override
                    public void onGlobalLayout() {
                    try {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            mapView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        } else {
                            mapView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }

                        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(mMapBounds, 0));
                        mMap.animateCamera(zoom, 1000, null);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    }
                });

            }
        }
    }
}
