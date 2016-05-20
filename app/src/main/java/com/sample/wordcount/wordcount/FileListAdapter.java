package com.sample.wordcount.wordcount;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.List;

/**
 * Created by chaitanyaduse on 5/20/2016.
 */
public class FileListAdapter extends BaseAdapter {
    Context context;
    List<File> files;

    public FileListAdapter(Context ctx, List<File> fileList) {
        this.context = ctx;
        files = fileList;
    }

    @Override
    public int getCount() {
        return files.size();
    }

    @Override
    public Object getItem(int position) {
        return files.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.row_header, null);
        }

        TextView tv_string = (TextView) convertView
                .findViewById(R.id.tv_string);
        tv_string.setTypeface(null, Typeface.BOLD);
        tv_string.setText(files.get(position).getName());
        return convertView;
    }
}
