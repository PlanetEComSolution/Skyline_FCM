package planet.info.skyline.old_activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;

import planet.info.skyline.R;

public class webViewActivity extends BaseActivity {
    WebView webView;
    String pdf;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_web_view);
        webView=(WebView)findViewById(R.id.webView);

        WebSettings webSettings = webView.getSettings();
        Intent in =getIntent();
         pdf=in.getStringExtra("pdf");

        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setJavaScriptEnabled(true);



        webViewActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl("http://docs.google.com/gview?embedded=true&url=http://exhibitpower2.com/upload/" + pdf);
            }
        });

    }

}
