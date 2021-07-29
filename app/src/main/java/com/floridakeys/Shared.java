package com.floridakeys;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * @author Adrian
 */
public class Shared {
    public static DisplayImageOptions gImageOption = new DisplayImageOptions.Builder()
            .showImageOnLoading(android.R.color.transparent)
            .showImageForEmptyUri(android.R.color.transparent)
            .showImageOnFail(android.R.color.transparent)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .displayer(new FadeInBitmapDisplayer(500))
            .build();;
}
