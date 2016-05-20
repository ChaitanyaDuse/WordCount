package com.sample.wordcount.wordcount;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AndroidListFilesActivity extends AppCompatActivity {

    private List<File> fileList = new ArrayList<>();
    private ListView lv_file_list;
    private FileListAdapter fileListAdapter;
    private ProgressDialog progressDialog;
    private TextView tv_root_directory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);
        tv_root_directory = (TextView) findViewById(R.id.tv_root_directory);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching .txt files");
        lv_file_list = (ListView) findViewById(R.id.lv_file_list);
        lv_file_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent data = new Intent();
                data.putExtra("FILE", fileList.get(position).getAbsolutePath());
                setResult(Activity.RESULT_OK, data);
                finish();

            }
        });

        tv_root_directory.setText(Environment.getExternalStorageDirectory().getAbsolutePath());

        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog.show();
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    File root = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
                    fileList = getListFiles(root);
                } catch (Exception ex) {
                    return false;
                }
                if (fileList != null && fileList.size() > 0) {
                    return true;
                }

                return false;


            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                progressDialog.dismiss();

                if (aBoolean) {
                    fileListAdapter = new FileListAdapter(AndroidListFilesActivity.this, fileList);
                    lv_file_list.setAdapter(fileListAdapter);
                } else {
                    Toast.makeText(getApplicationContext(), "Sorry problem loading text files", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }.execute();


    }

    private List<File> getListFiles(File parentDir) {
        ArrayList<File> inFiles = new ArrayList<>();
        File[] files = parentDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                inFiles.addAll(getListFiles(file));
            } else {
                if (file.getName().endsWith(".txt")) {
                    inFiles.add(file);
                }
            }
        }
        return inFiles;
    }


}