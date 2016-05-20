package com.sample.wordcount.wordcount;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<Integer> _listDataHeader; // header titles
    // child data in format of header title, child title
    private Map<Integer, List<WordOccurrenceComparable>> _listDataChild;

    public ExpandableListAdapter(Context context, List<Integer> listDataHeader,
                                 Map<Integer, List<WordOccurrenceComparable>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final WordOccurrenceComparable child = (WordOccurrenceComparable) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.row_contact, parent, false);
        }
        TextView tv_string = (TextView) convertView
                .findViewById(R.id.tv_string);
        TextView tv_occurrence = (TextView) convertView
                .findViewById(R.id.tv_occurrence);

        tv_occurrence.setText(Integer.toString(child.numberOfOccurrence));
        tv_string.setText(child.wordFromFile);


        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        Integer headerTitle = (Integer) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.row_header, null);
        }

        TextView tv_string = (TextView) convertView
                .findViewById(R.id.tv_string);
        tv_string.setTypeface(null, Typeface.BOLD);
        tv_string.setText(getRangeText((headerTitle)));
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private static String getRangeText(int range) {
        String rangeString = "";
        int to = range * 5;
        int from = to - 5 + 1;
        rangeString = "(" + from + "-" + to + ")";
        return rangeString;
    }
}