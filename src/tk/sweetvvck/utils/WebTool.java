/**
 * 
 */
package tk.sweetvvck.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * @author 程科
 *
 */
public class WebTool {

	/**
	 * @param url
	 * @throws IOException 
	 */
	public static String getDataFromUrl(String url) throws IOException {
		String result = "";
		URL uri = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
		if(conn.getResponseCode() == 200){
			String encode = conn.getContentEncoding();
			InputStream in = conn.getInputStream();
			result = getStrFromInput(in, encode);
		}
		return result;
	}

	/**
	 * @param in
	 * @param encode 
	 * @return
	 * @throws IOException 
	 */
	private static String getStrFromInput(InputStream in, String encode) throws IOException {
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		byte[] buffer = new byte[1024];
		
		int len = 0;
		
		while((len = in.read(buffer)) != -1){
			out.write(buffer, 0, len);
		}
		out.flush();
		out.close();
		return new String(out.toByteArray(), "UTF-8");
	}

	public static Drawable loadImageFromNetwork(String imageUrl) {

		Drawable drawable = null;
		try {
			// 可以在这里通过文件名来判断，是否本地有此图片
			drawable = Drawable.createFromStream(
					new URL(imageUrl).openStream(), "image.jpg");
		} catch (IOException e) {
			Log.d("test", e.getMessage());
		}
		if (drawable == null) {
			Log.d("test", "null drawable");
		} else {
			Log.d("test", "not null drawable");
		}

		return drawable;
	}
	
}
