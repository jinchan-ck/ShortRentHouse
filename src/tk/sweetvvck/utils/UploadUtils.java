package tk.sweetvvck.utils;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class UploadUtils {
	public static Bitmap stringtoBitmap(String string) {
		// 将字符串转换成Bitmap类型
		Bitmap bitmap = null;
		try {
			byte[] bitmapArray;
			bitmapArray = Base64.decode(string, Base64.DEFAULT);
			bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
					bitmapArray.length);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bitmap;
	}

	public static String bitmaptoString(Bitmap bitmap, String fix) {
		CompressFormat format = CompressFormat.JPEG;
		if(fix != null){
			format = fix.equalsIgnoreCase("png") ? CompressFormat.PNG : CompressFormat.JPEG;
		}
		// 将Bitmap转换成字符串
		String string = null;
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		bitmap.compress(format, 100, bStream);
		byte[] bytes = bStream.toByteArray();
		string = Base64.encodeToString(bytes, Base64.DEFAULT);
		return string;
	}

	// 计算图片的缩放值
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	// 根据路径获得图片并压缩，返回bitmap用于显示
	public static Bitmap getSmallBitmap(String filePath) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, 320, 480);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(filePath, options);
	}
}
