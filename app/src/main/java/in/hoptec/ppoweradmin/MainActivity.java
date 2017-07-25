package in.hoptec.ppoweradmin;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ProgressDialog pd;
    public void loading(boolean loading)
    {
        if(pd==null)
        {
            pd=new ProgressDialog(this);
            pd.setCancelable(false);
            pd.setMessage("Loading...");
        }


        if(loading)
        {
            if(!pd.isShowing())
            pd.show();
        }
        else {
            if(pd.isShowing())
            {
                pd.dismiss();
            }
        }

    }
    AdvancedWebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        web=(AdvancedWebView)findViewById(R.id.web);
        web.setListener(this, new AdvancedWebView.Listener() {
            @Override
            public void onPageStarted(String url, Bitmap favicon) {
                if(url.contains(".mp4"))
                {
                    startDownload(url);
                    web.onBackPressed();
                }
                loading(true);
            }

            @Override
            public void onPageFinished(String url) {

                loading(false);
            }

            @Override
            public void onPageError(int errorCode, String description, String failingUrl) {

            }

            @Override
            public void onDownloadRequested(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {

            }

            @Override
            public void onExternalPageRequest(String url) {

            }
        });


        web.loadUrl(Constants.HOST,false);




    }

    public void startDownload(String url)
    {
        Uri uri=Uri.parse(url);
        DownloadManager.Request r = new DownloadManager.Request(uri);

// This put the download in the same Download dir the browser uses
        r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, ""+ SystemClock.currentThreadTimeMillis()+".mp4");

// When downloading music and videos they will be listed in the player
// (Seems to be available since Honeycomb only)
        r.allowScanningByMediaScanner();

// Notify user when download is completed
// (Seems to be available since Honeycomb only)
        r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

// Start download
        DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        dm.enqueue(r);
        utl.snack(this,"Starting Download !");
    }

    @Override
    public void onBackPressed()
    {
        Activity act=this;
        if (web.canGoBack()) {
            web.goBack();
            return;
        }
        else {

        }
        String t="Are you sure you want to Exit ?";

        View rootView = act.getWindow().getDecorView().getRootView();
        //Snackbar snackbar = Snackbar.make(rootView, Html.fromHtml("<font color=\"#fff\">"+t+"</font>" ), Snackbar.LENGTH_LONG);
        Snackbar snackbar = Snackbar.make(rootView, ""+t , Snackbar.LENGTH_LONG);

        snackbar.setActionTextColor(act.getResources().getColor(R.color.blue_100));

        snackbar.setAction("EXIT", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.super.onBackPressed();
            }
        });
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(act.getResources().getColor(R.color.red_300));

// change snackbar text color
        int snackbarTextId = android.support.design.R.id.snackbar_text;
        TextView textView = (TextView)snackbarView.findViewById(snackbarTextId);
        textView.setTextColor(act.getResources().getColor(R.color.white));


        snackbar.show();


        //   utl.snack(this,"You cant go back at this stage !");




    }









}
