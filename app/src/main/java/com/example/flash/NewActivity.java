package com.example.flash;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.M)
public class NewActivity<historyBtn> extends AppCompatActivity {
    WebView wv;
    EditText et;
    ImageButton imageButton;
    ProgressBar progressBar;
    RelativeLayout menu;
    static String historyAddress="null";
    static long donwloadID;
    SwipeRefreshLayout swipeRefreshLayout;
    private static final int PERMISION_STORAGE_CODE=1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_new);
        Button historyBtn=(Button)findViewById(R.id.History);
        Button DownloadBtn=(Button)findViewById(R.id.Download);
        menu=(RelativeLayout)findViewById(R.id.menu);
        menu.setVisibility(View.GONE);
        et=(EditText)findViewById(R.id.url) ;
        progressBar= (ProgressBar)findViewById(R.id.progressBar);
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.SwipeToRefresh);
        progressBar.setMax(100);
        progressBar.setProgress(0);
        wv=(WebView)findViewById(R.id.webView);
        wv.setWebViewClient(new WebViewClient());
        wv.getSettings().getBuiltInZoomControls();
        et.setText(getIntent().getStringExtra("url"));
        wv.setWebChromeClient(new WebChromeClient()
        {
            @Override
            public void onProgressChanged(WebView view, int Progress) {
                progressBar.setProgress(Progress);
                if(Progress==100){
                    progressBar.setVisibility(view.GONE);
                    et.setText(wv.getUrl().trim());
                }
                else {
                    progressBar.setVisibility(view.VISIBLE);
                }
                super.onProgressChanged(view,Progress);

            }
        });
        WebSettings javascript =wv.getSettings();
        wv.getSettings().setJavaScriptEnabled(true);
        wv.loadUrl("https://" + et.getText().toString().trim() + "/");
        if(historyAddress=="null") {
            wv.loadUrl("https://" + et.getText().toString().trim() + "/");
        }
        else {
            et.setText(historyAddress);
            wv.loadUrl(historyAddress);
        }

        imageButton=(ImageButton)findViewById(R.id.imageButton3);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager keyboard=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.hideSoftInputFromWindow(et.getWindowToken(),0);
                wv.setWebViewClient(new WebViewClient());
                wv.getSettings().getBuiltInZoomControls();
                wv.loadUrl("https://"+et.getText().toString().trim()+"/");
                WebSettings javascript =wv.getSettings();
                wv.getSettings().setJavaScriptEnabled(true);
            }
        });
        ImageButton home= (ImageButton)findViewById(R.id.imageButton);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loadnewActivity= new Intent(NewActivity.this,Home.class);
                startActivity(loadnewActivity);
            }
        });
        ImageButton backbutton =(ImageButton)findViewById(R.id.BackButton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(wv.canGoBack())
                {
                    wv.goBack();
                }
            }
        });
        ImageButton forwardbutton =(ImageButton)findViewById(R.id.ForwardButton);
        forwardbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(wv.canGoForward()){
                    wv.goForward();
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                wv.reload();
            }
        });
        wv.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent historyload=new Intent(NewActivity.this,history.class);
                startActivity(historyload);
            }
        });
        DownloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Downloads=new Intent(NewActivity.this,Downloads.class);
                startActivity(Downloads);
            }
        });
        wv.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long contentLength) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        Log.d("permission", "permission denied to WRITE_EXTERNAL_STORAGE - requesting it");
                        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permissions, 1);
                    }
                    else{
                       //startDownloading(url);
                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

                        request.setMimeType(mimeType);
                        //------------------------COOKIE!!------------------------
                        String cookies = CookieManager.getInstance().getCookie(url);
                        request.addRequestHeader("cookie", cookies);
                        //------------------------COOKIE!!------------------------
                        request.addRequestHeader("User-Agent", userAgent);
                        request.setDescription("Downloading file...");
                        request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimeType));
                        request.allowScanningByMediaScanner();
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimeType));
                        DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                        dm.enqueue(request);
                        Toast.makeText(getApplicationContext(), "Downloading File", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    //startDownloading(url);
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                    request.setMimeType(mimeType);
                    String cookies = CookieManager.getInstance().getCookie(url);
                    request.addRequestHeader("cookie", cookies);
                    request.addRequestHeader("User-Agent", userAgent);
                    request.setDescription("Downloading file...");
                    request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimeType));
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimeType));
                    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    dm.enqueue(request);
                    Toast.makeText(getApplicationContext(), "Downloading File", Toast.LENGTH_LONG).show();
                }
            }
        });



        ImageButton Menu =(ImageButton) findViewById(R.id.MenuButton);
        Menu.setOnClickListener(new View.OnClickListener() {
            boolean click=true;
            @Override
            public void onClick(View v) {
                if(click) {
                    menu.setVisibility(View.VISIBLE);
                    click = false;
                }
                else{
                    menu.setVisibility(View.GONE);
                    click=true;
                }
            }
        });

    }

    private void startDownloading(String url) {
        String fileName ="my.mp3";//getFilename(url)
        DownloadManager.Request myRequest= new DownloadManager.Request(Uri.parse(url));
        myRequest.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI|DownloadManager.Request.NETWORK_MOBILE);
        myRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        myRequest.setTitle(fileName);
        myRequest.setAllowedOverMetered(true);
        myRequest.setAllowedOverRoaming(true);
        myRequest.setMimeType(getMimeType(Uri.parse(url)));
        myRequest.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,fileName);
        DownloadManager downloadManager=(DownloadManager)getSystemService(DOWNLOAD_SERVICE);
        donwloadID=downloadManager.enqueue(myRequest);
        Toast.makeText(NewActivity.this, "Your file is downloading....",Toast.LENGTH_SHORT).show();
        DownloadManager.Query query =new DownloadManager.Query();
        query.setFilterById(donwloadID);
        Cursor cursor =downloadManager.query(query);
        if(cursor.moveToFirst())
        {
            int sizeIndex=cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
            int downloadedIndex=cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
            long size =cursor.getInt(sizeIndex);
            long downloaded =cursor.getInt(downloadedIndex);
            double progress=0.0;
            if(size!=-1)
            {
                progress=downloaded*100.0/size;
                progressBar.setProgress((int)progress);
            }
        }

    }
    private String getMimeType(Uri uri){

        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private String getFilename(String url)
    {
        String name =url.substring(url.lastIndexOf('/') + 1, url.lastIndexOf('.'));
        String extention="null";
        char[] array1 =new char[url.length()];
        array1=url.toCharArray();
        for(int x=0;x<=url.length()-1;x++)
        {
            if(array1[x]=='?')
            {
                extention= url.substring(url.lastIndexOf('.')+1,url.lastIndexOf('?'));
            }

        }
        if(extention=="null")
        {
            extention= url.substring(url.lastIndexOf('.')+1);
        }
        return (name+"."+extention);


    }



}


