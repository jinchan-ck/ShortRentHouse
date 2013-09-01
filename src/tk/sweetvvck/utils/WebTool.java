/**
 * 
 */
package tk.sweetvvck.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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

}
