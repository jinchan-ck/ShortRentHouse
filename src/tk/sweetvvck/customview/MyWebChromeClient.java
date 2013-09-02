package tk.sweetvvck.customview;

import tk.sweetvvck.shortrendhouse.activity.MainActivity;
import android.content.Context;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class MyWebChromeClient extends WebChromeClient {
	
	private Context context;
	/**
	 * @param context
	 */
	public MyWebChromeClient(Context context) {
		this.context = context;
	}

	public final void onCloseWindow(WebView paramWebView) {
		super.onCloseWindow(paramWebView);
	}

	public final void onConsoleMessage(String paramString1, int paramInt,
			String paramString2) {
		 System.out.println("WebPageClient---->" + "--onConsoleMessage message:" + paramString1 + ",lineNumber:" + paramInt + ",sourceId:" + paramString2);
	}

	public final boolean onJsAlert(WebView paramWebView, String paramString1,
			String paramString2, JsResult paramJsResult) {
		System.out.println(paramJsResult);
		return super.onJsAlert(paramWebView, paramString1, paramString2,
				paramJsResult);
	}

	public final boolean onJsConfirm(WebView paramWebView, String paramString1,
			String paramString2, JsResult paramJsResult) {
		return super.onJsConfirm(paramWebView, paramString1, paramString2,
				paramJsResult);
	}

	public final boolean onJsPrompt(WebView paramWebView, String paramString1,
			String paramString2, String paramString3,
			JsPromptResult paramJsPromptResult) {
		return super.onJsPrompt(paramWebView, paramString1, paramString2,
				paramString3, paramJsPromptResult);
	}

	public final void onProgressChanged(WebView paramWebView, int paramInt) {
		super.onProgressChanged(paramWebView, paramInt);
		System.out.println("paramInt ______________________" + paramInt);
		((MainActivity)context).houseListFragment.getProgressbar().setProgress(paramInt/100f);
	}
}