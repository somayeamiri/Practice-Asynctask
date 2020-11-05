package com.example.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import ir.mahdi.mzip.zip.ZipArchive;


public class myasynctask extends AsyncTask<String, Integer, Integer> {


    private ProgressDialog progressDialog;
    private Context context;
    private String NAME;
    private String PATH;

    public myasynctask(Context context, String name) {
        this.context = context;
        this.NAME = name;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        //create progress dialog
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Download File");
        progressDialog.setMessage("downloading ...");
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
    }


    @Override
    protected Integer doInBackground(String... params) {

        String URL_ADDRESS=params[0];
        String PATHNAME=params[1]; // path for download and unzip
        PATH=PATHNAME;
        //check folder exist
        File file = new File(PATHNAME);
        if (!file.exists()) {
            file.mkdirs();
        }


        try {
            // get url and create connection
            URL url = new URL(URL_ADDRESS);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            // get file lenght
            int fileLength = connection.getContentLength();

            // create input stream and output stream
            InputStream input = connection.getInputStream();
            OutputStream output = new FileOutputStream(PATHNAME + NAME);

            // create byte array for read data
            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                // allow canceling with back button
                if (isCancelled()) {
                    input.close();
                    return null;
                }
                total += count;
                // publishing the progress....
                if (fileLength > 0) // only if total length is known
                    publishProgress((int) (total * 100 / fileLength));

                output.write(data, 0, count);
            }

            output.close();
            input.close();
            connection.disconnect();


        } catch (Exception e) {

            Log.e("error",e.getMessage());
        }


        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, "download finished", Toast.LENGTH_SHORT).show();

                progressDialog.dismiss();
                progressDialog = new ProgressDialog(context);
                progressDialog.setTitle("Unzipping");
                progressDialog.show();

            }
        });


        // Unzip
        ZipArchive zipArchive = new ZipArchive();
        zipArchive.unzip(PATHNAME + NAME,PATHNAME, "");





        return 0;
    }


    @Override
    protected void onPostExecute(Integer aVoid) {
        super.onPostExecute(aVoid);
        progressDialog.dismiss();
        Toast.makeText(context, "unzipping finished", Toast.LENGTH_SHORT).show();


    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        progressDialog.setProgress(values[0]);
    }


}
