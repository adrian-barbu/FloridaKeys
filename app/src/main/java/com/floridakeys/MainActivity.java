package com.floridakeys;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.floridakeys.ui.fragment.BaseFragment;
import com.floridakeys.ui.fragment.artists.ArtistsFragment;
import com.floridakeys.ui.fragment.event.EventsFragment;
import com.floridakeys.ui.fragment.nearby.NearByFragment;

import com.floridakeys.ui.fragment.search.SearchFragment;
import com.floridakeys.ui.fragment.settings.SettingsFragment;
import com.floridakeys.ui.fragment.venues.VenuesFragment;
import com.floridakeys.util.MyLocation;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * @description     Main Activity
 *
 * @author          Adrian
 */
public class MainActivity extends FragmentActivity {

    // Constant
//    public final int FRAGMENT_NEARBY = 10;
    public final int FRAGMENT_EVENTS = 10;
    public final int FRAGMENT_VENUES = 11;
    public final int FRAGMENT_ARTISTS = 12;
    public final int FRAGMENT_SETTINGS = 13;
    public final int FRAGMENT_SEARCH = 14;

    // UI Members
    TextView tvTitle;
    ImageView ivMenu;
    ImageView ivSearch;

    // Variables
    private SlidingMenu mMenu = null;
    private int mSelectedPage;          // Selected Fragment
    BaseFragment mFragment = null;      // Current Fragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupToolbar();
        setupLeftMenu();
        addFragment(FRAGMENT_EVENTS);
    }

    /**
     * Setup Toolbar Controls
     */
    private void setupToolbar() {
        ivMenu = (ImageView) findViewById(R.id.ivMenu);
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMenu != null)
                    mMenu.toggle();
            }
        });

        tvTitle = (TextView) findViewById(R.id.tvTitle);

        ivSearch = (ImageView) findViewById(R.id.ivSearch);
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragment(FRAGMENT_SEARCH);
            }
        });
    }

    /**
     * Add Sub Fragment
     *
     * @param which  : Fragment ID
     */
    private void addFragment(int which) {
        BaseFragment fragment = null;

        switch (which) {
//            case FRAGMENT_NEARBY:
//                fragment = new NearByFragment();
//                break;

            case FRAGMENT_EVENTS:
                fragment = new EventsFragment();
                break;

            case FRAGMENT_VENUES:
                fragment = new VenuesFragment();
                break;

            case FRAGMENT_ARTISTS:
                fragment = new ArtistsFragment();
                break;

            case FRAGMENT_SETTINGS:
                fragment = new SettingsFragment();
                break;

            case FRAGMENT_SEARCH:
                fragment = new SearchFragment();
                break;
        }

        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (which == FRAGMENT_SEARCH)
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
            transaction.replace(R.id.container, fragment);
            transaction.commit();

            mFragment = fragment;
            mSelectedPage = which;
        }
    }

    @Override
    public void onBackPressed() {
        onBack(null);
    }

    /**
     * Setup Side Menu
     */
    private void setupLeftMenu() {
        mMenu = new SlidingMenu(this);
        mMenu.setMode(SlidingMenu.LEFT);
        mMenu.setBehindOffset((int) (getResources().getDimension(
                R.dimen.sideMenuOffset) / getResources().getDisplayMetrics().density));
        mMenu.setFadeDegree(0.35f);
        mMenu.setFadeEnabled(true);
        mMenu.setSlidingEnabled(true);
        mMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        mMenu.setTouchModeBehind(SlidingMenu.TOUCHMODE_MARGIN);

//        //kadase pritisne na ikonu kliznog menia
//        mMenu.setOnOpenedListener(new OnOpenedListener() {
//
//            @Override
//            public void onOpened() {
//                setupItems();
//            }
//        });

//        //kada se ponovno klikne na ikonu menia onda ga zatvara, da se ne moďż˝e klizno otvarati kada je zatvoren
//        mMenu.setOnClosedListener(new OnClosedListener() {
//            @Override
//            public void onClosed() {
//                mMenu.setSlidingEnabled(false);//da se ne moďż˝e gestom otvoriti
//            }
//        });

        mMenu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
        mMenu.setMenu(R.layout.menu_layout);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        MyLocation.getInstance(this).stopListener();
    }

    /**
     * Side Bar Item Click Event Handler
     */
    public void onSideBarClick(View v) {
        int fragmentId = -1;

        switch (v.getId()) {
//            case R.id.itemNearBy:
//                fragmentId = FRAGMENT_NEARBY;
//                break;

            case R.id.itemEvents:
                fragmentId = FRAGMENT_EVENTS;
                break;

            case R.id.itemVenues:
                fragmentId = FRAGMENT_VENUES;
                break;

            case R.id.itemArtists:
                fragmentId = FRAGMENT_ARTISTS;
                break;

            case R.id.itemSettings:
                fragmentId = FRAGMENT_SETTINGS;
                break;
        }

        if (fragmentId != -1 && fragmentId != mSelectedPage)
            addFragment(fragmentId);

        mMenu.toggle();
    }

    /**
     * Back Button Event Handler
     *
     * @param v
     */
    public void onBack(View v) {
        if (mFragment != null) {
            BaseFragment topFragment = mFragment.getTopFragment();

            if (topFragment != null && !topFragment.isTopParent)
                mFragment.removeChildFragment();
            else
                addFragment(FRAGMENT_EVENTS);
        }
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }
}
