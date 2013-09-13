/**
 * 
 */
package tk.sweetvvck.external;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import tk.sweetvvck.customview.MyWebView;
import tk.sweetvvck.shortrendhouse.fragment.WubaHouseListFragment;
import tk.sweetvvck.utils.WebTool;
import android.content.Context;
import android.webkit.JavascriptInterface;

/**
 * @author 程科
 * 
 */
public class External {

	@SuppressWarnings("unused")
	private Context context;
	
	public External() {
	}

	public External(MyWebView webview) {
		this.context = webview.getContext();
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
//					Intent intent = new Intent(context, HouseDetailActivity.class);
//					intent.putExtra("url", url);
//					context.startActivity(intent);
//					String data = remove58(url);
//					/** 使用loaddatawithbaseurl解决中文乱码*/
					((WubaHouseListFragment)WubaHouseListFragment.instance).getmWebView().loadUrl(url);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static String remove58(String url) throws IOException {
		String data = WebTool.getDataFromUrl(url);
		data = data
				.replace("<head>",
						"<head><style type=\"text/css\">.hidden{display: none;} #hidden{display: none;}</style>");// 添加隐藏样式
		data = data.replace("header", "hidden");
		data = data.replace("footer", "hidden");
		data = data.replace("fangico duanxin", "hidden");// 隐藏发送到手机功能
		data = data.replace("btn_Favorite", "hidden");// 隐藏收藏功能
		data = data.replace("fangico bangbang net-online", "hidden");// 隐藏在线聊天功能
		data = data.replace("banner_down", "hidden");
		data = data.replace("adZhiNeng", "hidden");
		data = data.replace("dl_nav", "hidden");
		data = data.replace("58同城", "<strong><font color=red>VV短租</font></strong>");
		return data;
	}
}
