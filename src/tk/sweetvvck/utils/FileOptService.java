package tk.sweetvvck.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

/**
 * 
 * @author sweetvvck
 *
 */
public class FileOptService {
	private static Context mContext;

	public static void init(Context context){
		mContext = context;
	}
	
	/**
	 * 保存文件
	 * @param fileName
	 * @param fileContent
	 * @throws IOException
	 */
	public static void saveFile(String fileName, String fileContent) throws IOException {
		FileOutputStream out = mContext.openFileOutput(fileName, Context.MODE_PRIVATE);
		out.write(fileContent.getBytes());
		out.close();
	}
	/**
	 * 保存文件
	 * @param fileName
	 * @param fileContent
	 * @throws IOException
	 */
	public static void saveImage(String fileName, Drawable drawable) throws IOException {
		FileOutputStream out = mContext.openFileOutput(fileName, Context.MODE_PRIVATE);
		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
		out.close();
	}
	
	/**
	 * 读取文件中的内容
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static String readFile(String fileName) throws IOException{
		FileInputStream in = mContext.openFileInput(fileName);
		ByteArrayOutputStream out = new ByteArrayOutputStream(); 
		byte[] buffer = new byte[1024];
		int len = 0;
		while((len = in.read(buffer)) != -1){
			out.write(buffer, 0, len);
		}
		byte[] data = out.toByteArray();
		in.close();
		out.close();
		return new String(data);
	}
	/**
	 * 读取文件中的内容
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static Bitmap readImage(String fileName) throws IOException{
		FileInputStream in = mContext.openFileInput(fileName);
		Bitmap bitmap = BitmapFactory.decodeStream(in);
		in.close();
		return bitmap;
	}
	
}
