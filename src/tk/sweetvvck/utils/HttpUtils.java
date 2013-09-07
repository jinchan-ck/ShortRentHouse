package tk.sweetvvck.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

public class HttpUtils {
	public static final String BASE_URL = "http://192.168.1.103:8080/vvsrh/";
	public static final String PUBLISH_URL = BASE_URL +  "vvsrh.action";
	public static final String GET_HOUSES_URL = BASE_URL + "getVVHouses.action";

	public static String getData(String url, List<NameValuePair> nameValuePairs) {
		if (nameValuePairs == null) {
			nameValuePairs = new ArrayList<NameValuePair>();
		}
		String data = "";
		// 创建StringBuffer，
		StringBuffer stringBuffer = new StringBuffer();
		try {
			HttpEntity httpEntity = new UrlEncodedFormEntity(nameValuePairs,
					"utf-8");
			System.out.println("访问的接口是： ----》" + url);
			HttpPost post = new HttpPost(url);
			post.setEntity(httpEntity);
			System.out.println("HttpEntity is : "
					+ new BufferedReader(new InputStreamReader(httpEntity
							.getContent())).readLine());
			HttpClient client = getHttpClient();
			HttpResponse response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(response.getEntity().getContent()));
				String line;
				while ((line = reader.readLine()) != null) {
					stringBuffer.append(line);
				}
			} else
				return null;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ConnectTimeoutException e) {
			return null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		data = stringBuffer.toString();
		return data;
	}

	private static HttpClient getHttpClient() {
		HttpClient client = new DefaultHttpClient();
		client.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT,
				200000); // 超时设置
		client.getParams().setIntParameter(
				HttpConnectionParams.CONNECTION_TIMEOUT, 200000);// 连接超时
		return client;
	}
}
