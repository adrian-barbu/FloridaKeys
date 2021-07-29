package com.floridakeys.util;

import android.graphics.Bitmap.Config;
import android.widget.ImageView;

import com.floridakeys.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * @description		Image Util Functions Using ImageLoader Library
 * @author 			Adrian
 */
public class ImageUtil {
	
	public static void displayImage(ImageView view, String path, ImageLoadingListener listener) {
		ImageLoader loader = ImageLoader.getInstance();
		try {
			loader.displayImage(path, view, DEFAULT_DISPLAY_IMAGE_OPTIONS, listener);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			loader.clearMemoryCache();
		}
	}

	public static void displayArtistImage(ImageView view, String path, ImageLoadingListener listener) {
		ImageLoader loader = ImageLoader.getInstance();
		try {
			loader.displayImage(path, view, DEFAULT_ARTIST_DISPLAY_IMAGE_OPTIONS_BUIDLER.build(), listener);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			loader.clearMemoryCache();
		}
	}

	public static void displayVenueImage(ImageView view, String path, ImageLoadingListener listener) {
		ImageLoader loader = ImageLoader.getInstance();
		try {
			loader.displayImage(path, view, DEFAULT_VENUE_DISPLAY_IMAGE_OPTIONS_BUIDLER.build(), listener);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			loader.clearMemoryCache();
		}
	}

	public static void displayUserImage(ImageView view, String path, ImageLoadingListener listener) {
		ImageLoader loader = ImageLoader.getInstance();
		try {
			loader.displayImage(path, view, DEFAULT_USER_DISPLAY_IMAGE_OPTIONS_BUIDLER.build(), listener);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			loader.clearMemoryCache();
		}
	}

	public static void displayRoundImage(ImageView view, String path, ImageLoadingListener listener) {
		ImageLoader loader = ImageLoader.getInstance();
		try {
			loader.displayImage(path, view, ROUND_DISPLAY_IMAGE_OPTIONS, listener);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			loader.clearMemoryCache();
		}
	}
	
	public static void loadImage(String path, ImageLoadingListener listener) {
		ImageLoader loader = ImageLoader.getInstance();
		try {
			loader.loadImage(path, DEFAULT_DISPLAY_IMAGE_OPTIONS, listener);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
	}
	
	private static final DisplayImageOptions.Builder DEFAULT_DISPLAY_IMAGE_OPTIONS_BUIDLER = new DisplayImageOptions.Builder()
			.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
			.displayer(new FadeInBitmapDisplayer(500))
			.showImageForEmptyUri(R.drawable.ic_default_artist)
			.showImageOnLoading(R.drawable.ic_default_artist)
			.showImageOnFail(R.drawable.ic_default_artist)
			.cacheOnDisk(true)
			.cacheInMemory(true).bitmapConfig(Config.ARGB_8888);

	private static final DisplayImageOptions.Builder DEFAULT_ARTIST_DISPLAY_IMAGE_OPTIONS_BUIDLER = new DisplayImageOptions.Builder()
			.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
			.displayer(new FadeInBitmapDisplayer(500))
			.showImageForEmptyUri(R.drawable.ic_default_artist)
			.showImageOnLoading(R.drawable.ic_default_artist)
			.showImageOnFail(R.drawable.ic_default_artist)
			.cacheOnDisk(true)
			.cacheInMemory(true).bitmapConfig(Config.ARGB_8888);

	private static final DisplayImageOptions.Builder DEFAULT_VENUE_DISPLAY_IMAGE_OPTIONS_BUIDLER = new DisplayImageOptions.Builder()
			.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
			.displayer(new FadeInBitmapDisplayer(500))
			.showImageForEmptyUri(R.drawable.ic_default_artist)
			.showImageOnLoading(R.drawable.ic_default_artist)
			.showImageOnFail(R.drawable.ic_default_artist)
			.cacheOnDisk(true)
			.cacheInMemory(true).bitmapConfig(Config.ARGB_8888);

	private static final DisplayImageOptions.Builder DEFAULT_USER_DISPLAY_IMAGE_OPTIONS_BUIDLER = new DisplayImageOptions.Builder()
			.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
			.displayer(new RoundedBitmapDisplayer(500))
			.showImageForEmptyUri(R.drawable.ic_profile)
			.showImageOnLoading(R.drawable.ic_profile)
			.showImageOnFail(R.drawable.ic_profile)
			.cacheOnDisk(true)
			.cacheInMemory(true).bitmapConfig(Config.ARGB_8888);

	private static final DisplayImageOptions DEFAULT_DISPLAY_IMAGE_OPTIONS = DEFAULT_DISPLAY_IMAGE_OPTIONS_BUIDLER
			.build();
	private static final DisplayImageOptions ROUND_DISPLAY_IMAGE_OPTIONS = DEFAULT_DISPLAY_IMAGE_OPTIONS_BUIDLER
			.displayer(new RoundedBitmapDisplayer(500)).build();

}
