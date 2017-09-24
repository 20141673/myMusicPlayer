package cn.edu.cqu.mytestapplication;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;

public class WebBrowserActivity extends AppCompatActivity {
    private WebView webView;
    private Button btnBacktoMain;
    private RelativeLayout webLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_browser);
        webView=(WebView)findViewById(R.id.webview);
        btnBacktoMain=(Button)findViewById(R.id.btnBacktoMain);
        webLayout=(RelativeLayout)findViewById(R.id.activity_web_browser);
        final Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        String url=bundle.getString("url");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
        webView.setWebViewClient(new MyWebViewClient());
        btnBacktoMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webLayout.removeView(webView);
                webView.destroy();
                //((Activity)getBaseContext()).finish();

                Intent intent1=new Intent(WebBrowserActivity.this,MainActivity.class);
                WebBrowserActivity.this.startActivity(intent1);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if((keyCode==KeyEvent.KEYCODE_BACK)&&webView.canGoBack()){
            webView.goBack();;
            return  true;
        }
        return false;
    }


    private class MyWebViewClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
