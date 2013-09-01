/**
 * 
 */
package tk.sweetvvck.external;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import tk.sweetvvck.customview.MyWebView;
import tk.sweetvvck.shortrendhouse.fragment.MailListFragment;
import tk.sweetvvck.utils.WebTool;
import android.webkit.JavascriptInterface;

/**
 * @author 程科
 * 
 */
public class External {

	public External() {
	}

	public External(MyWebView webview) {

	}

	@JavascriptInterface
	public void jsCallMethod(String paramString) {
		System.out.println("json:" + paramString);
		String url = null;
		try {
			JSONObject jso = new JSONObject(paramString);
			if (jso.has("action")) {
				String action = jso.getString("action");
				if ("loadpage".equalsIgnoreCase(action)) {
					url = jso.getString("url");
					if (url.startsWith("http://i.webapp."))
						url = url.replace("webapp", "m");// 调用58 m.58.com
					url += "&refrom=wap";// 使用触屏版
					String data = remove58(url);
					/** 使用loaddatawithbaseurl解决中文乱码*/
					((MailListFragment)MailListFragment.instance).getmWebView().loadDataWithBaseURL("",
							data, "text/html", "utf-8", "");
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param url
	 * @return
	 * @throws IOException
	 */
	private String remove58(String url) throws IOException {
		String data = WebTool.getDataFromUrl(url);
		data = data
				.replace("<head>",
						"<head><style type=\"text/css\">.hidden{display: none;}</style>");// 添加隐藏样式
		data = data.replace("header", "hidden");
		data = data.replace("footer", "hidden");
		data = data.replace("fangico duanxin", "hidden");// 隐藏发送到手机功能
		data = data.replace("btn_Favorite", "hidden");// 隐藏收藏功能
		data = data.replace("fangico bangbang net-online", "hidden");// 隐藏在线聊天功能
		data = data.replace("联系我时，请说是在58同城上看到的，谢谢！", "");
		return data;
	}
}
