package com.dongframe.demo.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import com.dongframe.demo.infos.ReqFileBean;
import com.dongframe.demo.upgrade.HttpClientUtil;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

public class FileUtil {
	
	private static final String TAG = FileUtil.class.getSimpleName();
	public static final String WIFISW_FILE_ICON = "/dongframe/icon/";
	public static final String WIFISW_WEBVIEW = "/dongframe/webview/";
	public static final String WIFISW_FILE_AVATAR = "/dongframe/avatar/";
	public static final String APK_FILE_PATH = "/dongframe/apk/";
	public static final String APK_FILE_NAME = "upgrade.apk";

	public static File getApkRandomAccessFile(Context context)
			throws IOException {
		File file = null;
		if (isSDCardAvaliable()) {
			String filepath = Environment.getExternalStorageDirectory()
					+ APK_FILE_PATH;
			if (!new File(filepath).exists()) {
				LogUtils.LOGW("getApkFile", "mkdirs");
				new File(filepath).mkdirs();
			}
			file = new File(filepath + APK_FILE_NAME);
		} else {
			file = context.getFileStreamPath(APK_FILE_NAME);
			if (file != null && !file.exists()) {
				FileOutputStream out = null;
				out = context.openFileOutput(APK_FILE_NAME,
						Context.MODE_WORLD_READABLE);
				// TODO Auto-generated catch block
				if (out != null) {
					out.close();
				}
			}
		}
		return file;
	}

	public static boolean isSDCardAvaliable() {
		LogUtils.LOGE(TAG, "===isSDCardAvaliable====");
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	public static File getApkFile(Context context) {
		File file = null;
		if (isSDCardAvaliable()) {
			String filepath = Environment.getExternalStorageDirectory()
					+ APK_FILE_PATH;
			if (!new File(filepath).exists()) {
				LogUtils.LOGW("getApkFile", "mkdirs");
				new File(filepath).mkdirs();
			}
			file = new File(filepath + APK_FILE_NAME);
		} else {
			file = context.getFileStreamPath(APK_FILE_NAME);
		}
		return file;
	}

	public static void deleteApkFile(Context context) {
		if (isSDCardAvaliable()) {
			String filepath = Environment.getExternalStorageDirectory()
					+ APK_FILE_PATH;
			File file = new File(filepath + APK_FILE_NAME);
			if (file.exists()) {
				file.delete();
			}
		} else {
			context.deleteFile(APK_FILE_NAME);
		}
	}

	public static boolean isExistApkFile(Context context) {
		File f = FileUtil.getApkFile(context);
		if (f != null && f.length() > 0 && f.exists()) {
			return true;
		}
		SharedUtil.setUpgradeFileDownloaded(context, false);
		return false;
	}

	/**
	 * 验证apk的完整性
	 * 
	 * @param context
	 * @param filepath
	 * @return
	 */
	public static boolean isCompleteApk(Context context, String filepath) {
		boolean result = false;
		// String filepath = null;
		try {
			// if (isSDCardAvaliable()) {
			// filepath = Environment.getExternalStorageDirectory()
			// + APK_FILE_PATH + APK_FILE_NAME;
			// } else {
			// File file = context.getFileStreamPath(APK_FILE_NAME);
			// filepath = file.getPath();
			// }

			PackageManager pm = context.getPackageManager();
			LogUtils.LOGE("TAG", "isCompleteApk===>" + filepath);
			PackageInfo info = pm.getPackageArchiveInfo(filepath,
					PackageManager.GET_ACTIVITIES);
			if (info != null) {
				int versionCode = info.versionCode;
				if (versionCode > WifigxApUtil.getAppVersionCode(context)) {
					result = true;
				}
			}
		} catch (Exception e) {
			result = false;
			LogUtils.LOGE(TAG, "*****  解析未安装的 apk 出现异常 *****" + e.getMessage());
		}
		return result;
	}

	/**
	 * 判断该图片url命名的图片是否已经存在，存在则返回改图片文件 否则：不存在则调用httpclientUtil的downloadImg方法下载图片
	 */
	public static File createImageCacheFile(ReqFileBean bean, String url,
			Context context, String paramPath) {
		String fileName = digest2Str(url);
		File cacheFile = null;
		try {
			String filepath = getFilePath(context, paramPath);
			cacheFile = new File(filepath + fileName);
			LogUtils.LOGI(TAG, " createImageCacheFile filepath: " + filepath
					+ " cacheFile: " + cacheFile);

			if (cacheFile.exists()) {
				return cacheFile;
			} else {
				if (!new File(filepath).exists()) {
					LogUtils.LOGW("loadImageFromUrl", "mkdirs");
					new File(filepath).mkdirs();
				}
				// cacheFile.createNewFile();
				cacheFile = HttpClientUtil.downloadImageFile(context, url,
						cacheFile);

			}
		} catch (Exception e) {
			cacheFile = null;
			LogUtils.LOGE(
					TAG,
					" FileUtil createImageCacheFile Exception "
							+ e.getMessage());
			e.printStackTrace();
		}
		return cacheFile;
	}

	public static File findImageFileByPath(String url, Context context,
			String paramPath) {
		File file = null;
		try {
			String fileName = digest2Str(url);
			String filepath = getFilePath(context, paramPath);
			file = new File(filepath + fileName);
			if (file.exists()) {
				return file;
			}
		} catch (Exception e) {
			LogUtils.LOGE(TAG,
					"findImageFileByPath paramPath Exception " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	public static File findImageFileByPath(String imgFilePath) {
		File file = null;
		try {

			file = new File(imgFilePath);
			if (file.exists()) {
				return file;
			}
		} catch (Exception e) {
			LogUtils.LOGE(
					TAG,
					"findImageFileByPath imgFilePath Exception "
							+ e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	@TargetApi(Build.VERSION_CODES.FROYO)
	public static String getFilePath(Context context, String paramPath) {
		try {
			// modify by wangtao on 2013-1-10 modify filepath start
			String packageName = "/." + context.getPackageName();
			paramPath = packageName + paramPath;
			String filepath = context.getCacheDir().getPath() + paramPath;
			if (Build.VERSION.SDK_INT < 8) {
				if (isSDCardAvaliable()) {
					filepath = Environment.getExternalStorageDirectory()
							+ paramPath;
				}
			} else {
				if (isSDCardAvaliable()) {
					File file = context.getExternalCacheDir();
					if (null != file) {
						filepath = file.getAbsolutePath() + paramPath;
					}
				}
			}
			// modify by wangtao on 2013-1-10 modify filepath end
			return filepath;
		} catch (Exception e) {
			LogUtils.LOGE(TAG, "getFilePath error!");
			return "/";
		}
	}
	
	public static byte[] digest2Bytes(byte[] bytes) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (Exception localNoSuchAlgorithmException) {
		}
		return md.digest(bytes);
	}

	public static String digest2Str(byte[] bytes) {
		return bytes2Hex(digest2Bytes(bytes));
	}

	public static String digest2Str(String str) {
		if (TextUtils.isEmpty(str)) {
			return "";
		}
		return digest2Str(str.getBytes());
	}

	public static String bytes2Hex(byte[] b) {
		char[] chars = new char[b.length * 2];
		int charsIndex = 0;

		for (int bytesIndex = 0; bytesIndex < b.length; ++bytesIndex) {
			int intValue = b[bytesIndex];
			if (intValue < 0) {
				intValue += 256;
			}

			int intValueHi = (intValue & 0xF0) >> 4;
			if (intValueHi > 9)
				chars[charsIndex] = (char) (intValueHi - 10 + 97);
			else {
				chars[charsIndex] = (char) (intValueHi + 48);
			}

			int intValueLo = intValue & 0xF;
			if (intValueLo > 9)
				chars[(charsIndex + 1)] = (char) (intValueLo - 10 + 97);
			else {
				chars[(charsIndex + 1)] = (char) (intValueLo + 48);
			}
			charsIndex += 2;
		}
		return new String(chars, 0, charsIndex);
	}
	
	/**
	 * 获取sd卡路径 双sd卡时，根据”设置“里面的数据存储位置选择，获得的是内置sd卡或外置sd卡
	 * 
	 * @return
	 */
	public static String getNormalSDCardPath() {
		if (isSDCardAvaliable()) {
			return Environment.getExternalStorageDirectory().getPath()+"/";
		}
		return "";
	}

	/**
	 * 获取外置SD卡路径
	 * 
	 * @return 应该就一条记录或空
	 */
	public static List<String> getExtSDCardPath() {
		List<String> lResult = new ArrayList<String>();
		try {
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec("mount");
			InputStream is = proc.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line;
			while ((line = br.readLine()) != null) {
				if (line.contains("extSdCard")) {// 其中，line.contains("extSdCard")判断部分有待进一步验证！
					String[] arr = line.split(" ");
					String path = arr[1];
					File file = new File(path);
					if (file.isDirectory()) {
						lResult.add(path+"/");
					}
				}
			}
			isr.close();
		} catch (Exception e) {
		}
		return lResult;
	}

	/**
	 * 创建目录（不存在则创建）
	 * 
	 * @return
	 */
	public static boolean createDir(File file) {
		boolean isSuccess = false;
		File parentFile = file.getParentFile();
		if (parentFile != null) {
			if (!parentFile.exists()) {
				isSuccess = file.getParentFile().mkdirs();
			} else {
				isSuccess = true;
			}
		}
		return isSuccess;
	}
	
}
