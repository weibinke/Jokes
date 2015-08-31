package com.kb.jokes.business.core;

import java.io.File;

import android.content.Context;

import com.kb.jokes.R;
import com.kb.jokes.business.manager.JokesDataManager;
import com.kb.jokes.data.network.Communicator;
import com.kb.jokes.data.storage.DbService;
import com.kb.platform.update.PlatformHelper;
import com.kb.platform.update.UIDFactory;
import com.kb.platform.util.MLog;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class AppCore {
	private static final String TAG = "AppCore";
	private static AppCore instance;
	private Communicator communicator;
	private boolean isInited = false;
	private Context mContext;
	private JokesDataManager jokesDataManager;
	private DbService dbService;
	
	public static AppCore getInstance(){
		if(instance == null){
			instance = new AppCore();
		}
		
		return instance;
	}
	
	public boolean init(final Context context){
		if(isInited){
			MLog.e(TAG, "already inited.");
			return false;
		}
		
		mContext = context.getApplicationContext();

		UIDFactory.init(mContext);
		FileConfig.initFilePath(mContext);
		//init db
		dbService = new DbService();
		dbService.init(mContext);
		
		//init network
		communicator = new Communicator();
		
		//init ImageLoader
		initImageLoader();
		
		jokesDataManager = new JokesDataManager();
		jokesDataManager.init();
		
		PlatformHelper.getInstance().init(mContext, mContext.getPackageName(),mContext.getString(R.string.channel_id));
		
		return true;
	}
	
	public Context getContext(){
		return mContext;
	}
	
	private void initImageLoader(){
		File cacheDir = StorageUtils.getCacheDirectory(mContext);
		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext)
		.threadPriority(Thread.NORM_PRIORITY - 2)
		.denyCacheImageMultipleSizesInMemory()
		.diskCacheFileNameGenerator(new Md5FileNameGenerator())
		.diskCacheSize(50 * 1024 * 1024) // 50 Mb
		.tasksProcessingOrder(QueueProcessingType.LIFO)
		.denyCacheImageMultipleSizesInMemory()
        .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
        .memoryCacheSize(2 * 1024 * 1024)
        .memoryCacheSizePercentage(13) // default
        .discCache(new UnlimitedDiskCache(cacheDir)) // default
        .diskCacheSize(100 * 1024 * 1024)
        .diskCacheFileCount(500)
        .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
        .imageDownloader(new BaseImageDownloader(mContext)) // default
        .imageDecoder(new BaseImageDecoder(true)) // default
        .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
        .writeDebugLogs()
		.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
		
	}
	
	public void unInit(){
		MLog.i(TAG,"unInit");
		jokesDataManager.unInit();
		dbService.unInit();
		
		System.exit(0);
	}
	
	public void exitApp(){
		MLog.i(TAG, "exitApp");
		unInit();
		System.exit(0);
	}
	
	public static Communicator getCommunicator(){
		return getInstance().communicator;
	}
	
	public static JokesDataManager getDataManager(){
		return getInstance().jokesDataManager;
	}
	
	public static DbService getDbService(){
		return getInstance().dbService;
	}
	
	public String getDeviceId(){
		return UIDFactory.getUID();
	}
}
