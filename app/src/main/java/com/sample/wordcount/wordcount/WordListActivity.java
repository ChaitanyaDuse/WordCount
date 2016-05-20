package com.sample.wordcount.wordcount;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WordListActivity extends AppCompatActivity {
    private static final int IMPORT_DATA = 55;
    private Button btn_import_data;
    private ProgressDialog progressDialog;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    private List<WordOccurrenceComparable> myTopOccurrence;
    private List<Integer> headerList;


    private Map<Integer, List<WordOccurrenceComparable>> range = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);
        myTopOccurrence = new ArrayList<>();
        headerList = new ArrayList<>();
        range = new HashMap<>();
        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        listAdapter = new ExpandableListAdapter(WordListActivity.this, headerList, range);
        // setting list adapter
        expListView.setAdapter(listAdapter);


        progressDialog = new ProgressDialog(WordListActivity.this);

        btn_import_data = (Button) findViewById(R.id.btn_import_data);
        btn_import_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent intent = new Intent();
                // intent.setType("*/*");
                // intent.setAction(Intent.ACTION_GET_CONTENT);
                // intent.addCategory(Intent.CATEGORY_OPENABLE);
                // startActivityForResult(intent, IMPORT_DATA);

                Intent i = new Intent(WordListActivity.this, AndroidListFilesActivity.class);
                startActivityForResult(i, IMPORT_DATA);
            }
        });
    }


    private List<WordOccurrenceComparable> readFromUri(Object obj) throws IOException {
        BufferedReader bufferedReader = null;
        String inputLine = null;
        if (obj instanceof Uri) {
            InputStream is = getContentResolver().openInputStream((Uri) obj);
            bufferedReader = new BufferedReader(new InputStreamReader(is));
        } else if (obj instanceof String) {
            bufferedReader = new BufferedReader(new FileReader(new File(obj.toString())));
        }


        Map<String, Integer> wordOccurrenceMap = new HashMap<>();
        while ((inputLine = bufferedReader.readLine()) != null) {
            String[] words = inputLine.split("[ \n\t\r.,;:!?(){}]");

            for (int counter = 0; counter < words.length; counter++) {
                String key = words[counter].toLowerCase(); // remove .toLowerCase for Case Sensitive result.
                if (key.length() > 0) {
                    if (wordOccurrenceMap.get(key) == null) {
                        wordOccurrenceMap.put(key, 1);
                    } else {
                        int value = wordOccurrenceMap.get(key).intValue();
                        value++;
                        wordOccurrenceMap.put(key, value);
                    }
                }
            }
        }
        Set<Map.Entry<String, Integer>> entrySet = wordOccurrenceMap.entrySet();
        System.out.println("Words" + "\t\t" + "# of Occurances");
        for (Map.Entry<String, Integer> entry : entrySet) {
            System.out.println(entry.getKey() + "\t\t" + entry.getValue());
        }

        if (bufferedReader != null)
            bufferedReader.close();

        List<WordOccurrenceComparable> l = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : wordOccurrenceMap.entrySet())
            l.add(new WordOccurrenceComparable(entry.getKey(), entry.getValue()));

        Collections.sort(l);

        return l;


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
//            Log.d("File Info", data.toString());
            try {
                new AsyncTask<Object, Void, List<WordOccurrenceComparable>>() {
                    @Override
                    protected void onPreExecute() {
                        progressDialog.show();
                    }

                    @Override
                    protected List<WordOccurrenceComparable> doInBackground(Object[] params) {
                        if (params != null && params.length > 0) {
                            try {
                                return readFromUri(params[0]);
                            } catch (IOException e) {
                                e.printStackTrace();
                                return null;
                            }
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(List<WordOccurrenceComparable> wordOccurrenceComparables) {
                        progressDialog.dismiss();
                        if (wordOccurrenceComparables != null && wordOccurrenceComparables.size() > 0) {
                            myTopOccurrence.clear();
                            range.clear();
                            headerList.clear();
                            myTopOccurrence.addAll(wordOccurrenceComparables);


                            for (int i = 0; i < myTopOccurrence.size(); i++) {

                                int section = getSection(myTopOccurrence.get(i));
                                if (range.get(section) == null) {
                                    range.put(section, new ArrayList<WordOccurrenceComparable>());
                                    headerList.add(section);
                                }
                                range.get(section).add(myTopOccurrence.get(i));
                            }

                            listAdapter.notifyDataSetChanged();
                        }
                    }
                }.execute(data.getStringExtra("FILE"));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static int getSection(WordOccurrenceComparable num) {
        int section = num.numberOfOccurrence / 5;
        if (num.numberOfOccurrence % 5 > 0) {
            section++;
        }
        return section;
    }

}
