package com.example.jiuquwan.loader;


import java.io.File;

import android.content.Context;

import com.example.jiuquwan.R;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ImageLoaderutils {
private  static	ImageLoader loader;
private  static  ImageLoaderConfiguration.Builder confbuilder;
private   static ImageLoaderConfiguration conf;
private   static DisplayImageOptions.Builder optbuilder;
private  static DisplayImageOptions opt;
//返回注册号的imageloader对象
	public static  ImageLoader getInstance(Context context)
	{
	
		loader=ImageLoader.getInstance();
		confbuilder=new ImageLoaderConfiguration.Builder(context);
		File file=new File("/mnt/sdcard/gather/imageloader");
		//制定sdcard缓存的路径
		confbuilder.discCache(new UnlimitedDiscCache(file));
		//缓存的图片个数
		confbuilder.discCacheFileCount(100);
		//缓存的最大容量
		confbuilder.discCacheSize(1024*1024*10*10);
		conf=confbuilder.build();
		loader.init(conf);
		return loader;
	}
	//返回显示图片使得opt
	/**
	 * 
	 * @param forUriId uri 加载默认图片
	 * @param errorId 图片获取失败 加载的默认图片
	 * @return imageLoader的配置对象
	 */
	public   static DisplayImageOptions getOpt(int forUriId,int errorId)
	{
		optbuilder=new DisplayImageOptions.Builder();
		//uri 加载默认图片
		optbuilder.showImageForEmptyUri(forUriId);
		//图片获取失败 加载的默认图片
		optbuilder.showImageOnFail(errorId);
		optbuilder.cacheInMemory(true);//做内存缓存
		optbuilder.cacheOnDisc(true);//做硬盘缓存
		opt=optbuilder.build();
		return opt;
	}
	
//	//返回显示图片使得opt  ListView中 用户的头像
//		public   static DisplayImageOptions getOpt2()
//		{
//			optbuilder=new DisplayImageOptions.Builder();
//			//uri 加载默认图片
//			optbuilder.showImageForEmptyUri(R.drawable.logo1);
//			//图片获取失败 加载的默认图片
//			optbuilder.showImageOnFail(R.drawable.logo1_error);
//			optbuilder.cacheInMemory(true);//做内存缓存
//			optbuilder.cacheOnDisc(true);//做硬盘缓存
//			opt=optbuilder.build();
//			return opt;
//		}
//		
//		//返回显示图片使得opt   ListView中  活动的图像
//		public   static DisplayImageOptions getOpt3()
//		{
//			optbuilder=new DisplayImageOptions.Builder();
//			//uri 加载默认图片
//			optbuilder.showImageForEmptyUri(R.drawable.default_jpg);
//			//图片获取失败 加载的默认图片
//			optbuilder.showImageOnFail(R.drawable.default_jpg);
//			optbuilder.cacheInMemory(true);//做内存缓存
//			optbuilder.cacheOnDisc(true);//做硬盘缓存
//			opt=optbuilder.build();
//			return opt;
//		}

}
