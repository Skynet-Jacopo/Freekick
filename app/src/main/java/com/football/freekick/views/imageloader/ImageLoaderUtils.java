package com.football.freekick.views.imageloader;

import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * Created by wanglin on 2016/5/27.
 *
 */
public class ImageLoaderUtils {

    public static void displayHeadIcon(String url, ImageView target, int defRes)    {
        ImageLoader.getInstance().displayImage(url, target, ImageOption.getCicleOption(defRes));
    }
    
    /**
     * 自定义Option
     */
    public static void displayImage(String url, ImageView target) {
        ImageLoader.getInstance().displayImage(url, target, ImageOption.getDefOption());
    }

    /**
     * 自定义Option
     */
    public static void displayImage(String url, ImageView target, int defRes) {
        ImageLoader.getInstance().displayImage(url, target, ImageOption.getDefOption(defRes));
    }

    /**
     * 自定义Option
     */
    public static void displayImage(String url, ImageView target, DisplayImageOptions options) {
        ImageLoader.getInstance().displayImage(url, target, options);
    }

    /**
     * 图片详情页专用
     */
    public static void displayImage4Detail(String url, ImageView target, SimpleImageLoadingListener loadingListener) {
        ImageLoader.getInstance().displayImage(url, target, ImageOption.get4ExactlyType(), loadingListener);
    }

    /**
     * 图片列表页专用
     */
    public static void displayImageList(String url, ImageView target, int loadingResource, SimpleImageLoadingListener loadingListener, ImageLoadingProgressListener progressListener) {
        ImageLoader.getInstance().displayImage(url, target, ImageOption.get4PictureList(loadingResource), loadingListener, progressListener);
    }

    /**
     * 自定义加载中图片
     */
    public static void displayImageWithLoadingPicture(String url, ImageView target, int loadingResource) {
        ImageLoader.getInstance().displayImage(url, target, ImageOption.get4PictureList(loadingResource));
    }

    /**
     * 当使用WebView加载大图的时候，使用本方法现下载到本地然后再加载
     */
    public static void loadImageFromLocalCache(String url, SimpleImageLoadingListener loadingListener) {
        ImageLoader.getInstance().loadImage(url, ImageOption.get4ExactlyType(), loadingListener);
    }
}
