package com.naimpozo.sustantivos;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Insets;
import android.os.Build;
import android.view.View;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings;
public class MainActivity extends Activity {
  private WebView web;
  @Override public void onCreate(Bundle b) {
    super.onCreate(b);
    web = new NoSuggestionsWebView(this);
    web.setWebViewClient(new WebViewClient());
    WebSettings s = web.getSettings();
    s.setJavaScriptEnabled(true);
    s.setDomStorageEnabled(true);
    s.setAllowFileAccess(true);
    web.loadUrl("file:///android_asset/index.html");
    FrameLayout root = new FrameLayout(this);
    root.setBackgroundColor(Color.rgb(247, 247, 253));
    root.addView(web, new FrameLayout.LayoutParams(-1, -1));
    getWindow().getDecorView().setSystemUiVisibility(
        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR |
        (Build.VERSION.SDK_INT >= 26 ? View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR : 0));
    if (Build.VERSION.SDK_INT >= 30) {
      getWindow().setDecorFitsSystemWindows(false);
      root.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
      @Override public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
        Insets safe = windowInsets.getInsets(WindowInsets.Type.systemBars()
            | WindowInsets.Type.displayCutout() | WindowInsets.Type.ime());
        view.setPadding(safe.left, safe.top, safe.right, safe.bottom);
        return windowInsets;
      }
      });
    } else {
      getWindow().setStatusBarColor(Color.rgb(247, 247, 253));
      getWindow().setNavigationBarColor(Color.rgb(247, 247, 253));
    }
    setContentView(root);
    root.requestApplyInsets();
    if (Build.VERSION.SDK_INT >= 33) {
      getOnBackInvokedDispatcher().registerOnBackInvokedCallback(
          android.window.OnBackInvokedDispatcher.PRIORITY_DEFAULT,
          new android.window.OnBackInvokedCallback() {
            @Override public void onBackInvoked() {
              if (web.canGoBack()) web.goBack(); else finish();
            }
          });
    }
  }
  @Override public void onBackPressed() { if(web.canGoBack()) web.goBack(); else super.onBackPressed(); }
}
