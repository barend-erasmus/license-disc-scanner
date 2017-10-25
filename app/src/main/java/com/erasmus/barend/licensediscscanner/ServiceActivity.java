package com.erasmus.barend.licensediscscanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;

import java.io.InputStream;

/**
 * Created by Barend Erasmus on 10/23/2017.
 */


public abstract class ServiceActivity extends Activity {

    private ProgressDialog progress;

    public void Get(final String url, final int resultCode) {
        progress = progress.show(ServiceActivity.this, null, "Please wait...");

        Thread t = new Thread() {

            public void run() {

                HttpClient client = new DefaultHttpClient();
                HttpConnectionParams.setConnectionTimeout(client.getParams(),
                        5000); // Timeout Limit
                HttpResponse response;
                try {
                    HttpGet get = new HttpGet(url);

                    response = client.execute(get);

                    if (response != null) {
                        InputStream in = response.getEntity().getContent();
                        final String content = ConvertStreamToString(in);

                        Handler handler2 = new Handler(Looper.getMainLooper());
                        handler2.post(new Runnable() {
                            public void run() {
                                progress.dismiss();
                                onSuccess(content, resultCode);
                            }
                        });
                    }

                } catch (final Exception e) {

                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        public void run() {
                            progress.dismiss();

                            AlertDialog.Builder builder = new AlertDialog.Builder(
                                    ServiceActivity.this);
                            builder.setTitle("Uh oh!");
                            builder.setMessage("An error occurred, please try again later.");
                            builder.setCancelable(true);
                            builder.setNeutralButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    });

                    e.printStackTrace();
                }
            }
        };

        t.start();

    }

    public void Post(final String json, final String url, final int resultCode) {
        progress = progress.show(ServiceActivity.this, null, "Please wait...");

        Thread t = new Thread() {

            public void run() {

                HttpClient client = new DefaultHttpClient();
                HttpConnectionParams.setConnectionTimeout(client.getParams(),
                        5000); // Timeout Limit
                HttpResponse response;
                try {
                    HttpPost post = new HttpPost(url);

                    StringEntity se = new StringEntity(json);
                    se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
                            "application/json"));
                    post.setEntity(se);
                    response = client.execute(post);

                    if (response != null) {
                        InputStream in = response.getEntity().getContent();
                        final String content = ConvertStreamToString(in);

                        Handler handler2 = new Handler(Looper.getMainLooper());
                        handler2.post(new Runnable() {
                            public void run() {
                                progress.dismiss();
                                onSuccess(content, resultCode);
                            }
                        });
                    }

                } catch (final Exception e) {

                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        public void run() {
                            progress.dismiss();

                            AlertDialog.Builder builder = new AlertDialog.Builder(
                                    ServiceActivity.this);
                            builder.setTitle("Uh oh!");
                            builder.setMessage("An error occurred, please try again later.");
                            builder.setCancelable(true);
                            builder.setNeutralButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    });

                    e.printStackTrace();
                }
            }
        };

        t.start();
    }

    public String ConvertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public void ShowDialog(String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                ServiceActivity.this);
        builder.setTitle("Error");
        builder.setMessage(Message);
        builder.setCancelable(true);
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public abstract void onSuccess(String content, int resultCode);

}

