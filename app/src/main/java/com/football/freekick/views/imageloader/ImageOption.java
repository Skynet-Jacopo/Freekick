package com.football.freekick.views.imageloader;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * Created by wanglin on 2016/5/27.
 */
public class ImageOption {
    /**
     *  默认的图片参数
     *
     * @return
     */
    public static DisplayImageOptions getCicleOption(int defRes) {
        return new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading(true)
                .considerExifParams(true)           
                 .showImageOnLoading(defRes)
                 .showImageForEmptyUri(defRes)
                 .showImageOnFail(defRes)
                .displayer(new CircleDisplayer())
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .build();
    }
    
    /**
     *  默认的图片参数
     *
     * @return
     */
    public static DisplayImageOptions getDefOption(int defRes) {
        return new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading(true)
                .considerExifParams(true)
                .showImageOnLoading(defRes)
                .showImageForEmptyUri(defRes)
                .showImageOnFail(defRes)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .build();
    }
    
    /**
     * 默认的图片参数
     *
     * @return
     */
    public static DisplayImageOptions getDefOption() {
        return new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading(true)
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .build();
    }

    /**
     * 设置图片放缩类型为模式EXACTLY，用于图片详情页的缩放
     *
     * @return
     */
    public static DisplayImageOptions get4ExactlyType() {
        return new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading(true)
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();
    }

    /**
     * 加载图片列表专用，加载前会重置View
     * {@link DisplayImageOptions.Builder#resetViewBeforeLoading} = true
     *
     * @param loadingResource
     * @return
     */
    public static DisplayImageOptions get4PictureList(int loadingResource) {
        return new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading(true)
                .showImageOnLoading(loadingResource)
                .showImageForEmptyUri(loadingResource)
                .showImageOnFail(loadingResource)
                .build();
    }

}
