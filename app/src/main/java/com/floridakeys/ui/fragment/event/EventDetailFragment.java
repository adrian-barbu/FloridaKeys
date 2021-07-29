package com.floridakeys.ui.fragment.event;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.floridakeys.R;
import com.floridakeys.model.Event;
import com.floridakeys.ui.fragment.BaseFragment;
import com.floridakeys.util.ActionUtil;
import com.floridakeys.util.ImageUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * @description Event Detail Fragment
 *              This fragment has map-based venues
 *
 * @author      Adrian
 */
public class EventDetailFragment extends BaseFragment implements OnMapReadyCallback
{
    // UI Variable
    ImageView ivImage;
    TextView tvDateTime;
    TextView tvSubject, tvTitle, tvAddress;
    TextView tvDescription;

    // Variables
    Event mEvent;

    int mEventType;
    String mAddress;

    GoogleMap mMap;
    LatLngBounds.Builder mBoundsBuilder;
    LatLngBounds mMapBounds;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_event_details, container, false);
        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvDateTime = (TextView) mRootView.findViewById(R.id.tvDateTime);
        ivImage = (ImageView) mRootView.findViewById(R.id.ivImage);
        tvSubject = (TextView) mRootView.findViewById(R.id.tvSubject);
        tvTitle = (TextView) mRootView.findViewById(R.id.tvTitle);
        tvAddress = (TextView) mRootView.findViewById(R.id.tvAddress);
        tvDescription = (TextView) mRootView.findViewById(R.id.tvDescription);

        // Set datas
        mEvent = getArguments().getParcelable(PARAM_EVENT);
        mEventType = getArguments().getInt(PARAM_EVENT_TYPE);

        tvDateTime.setText(String.format("%s %s", mEvent.getDate(), mEvent.getTime()));

        mAddress = mEvent.getAddress();
        if (!mEvent.getCity().isEmpty()) mAddress += ", " + mEvent.getCity();
        if (!mEvent.getState().isEmpty()) mAddress += ", " + mEvent.getState();

        if (mEventType == EVENT_TYPE_FOR_VENUE) {
            setTitle(mEvent.getArtistName());
            ImageUtil.displayVenueImage(ivImage, mEvent.getArtistImage(), null);
            tvSubject.setText(mEvent.getVenueName());
        }
        else /* if (mEventType == EVENT_TYPE_FOR_ARTIST) */ {
            setTitle(mEvent.getVenueName());
            ImageUtil.displayVenueImage(ivImage, mEvent.getVenueImage(), null);
            tvSubject.setText(mEvent.getArtistName());
        }

        tvTitle.setText(mEvent.getTitle());
        tvAddress.setText(mAddress);
        tvDescription.setText(mEvent.getDescription());

        // Setup buttons
        final String buyTicketLink = mEvent.getBuyTicketLink();
        Button btnBuyTicket = (Button) mRootView.findViewById(R.id.btnBuyTicket);
        btnBuyTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!buyTicketLink.isEmpty())
                    ActionUtil.callBrowser(getActivity(), buyTicketLink);
                else
                    showToast(R.string.error_no_buy_ticket_link);
            }
        });


        Button btnDirections = (Button) mRootView.findViewById(R.id.btnDirections);
        btnDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActionUtil.navigateTo(getActivity(), mEvent.getLatitude(), mEvent.getLongitude());
            }
        });

        // Start to map sync
        mBoundsBuilder = new LatLngBounds.Builder();

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapViewer);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        addLocation(mTitle, mAddress, mEvent.getLatitude(), mEvent.getLongitude());
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
        mMap.addMarker(new MarkerOptions()
                .position(location)
                .title(title)
                .snippet(String.format("%n%s", address))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_mark_filled)));

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
