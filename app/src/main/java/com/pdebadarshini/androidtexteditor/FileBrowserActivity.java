package com.pdebadarshini.androidtexteditor;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.util.Log;
import android.content.Intent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.FileInputStream;


import android.widget.Button;

import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ListView;

/**
 * Created by pdebadarshini on 4/12/15.
 */

// Not being used class. Just keeping it for future modifications and learning

public final class FileBrowserActivity extends Activity {

    private static final String cachefilepath =
            "/data/data/com.pdebadarshini.androidtexteditor/files";

    public static final String filetext = "com.pdebadarshini.androidtexteditor.MESSAGE";
    public static final String filenameabs = "com.pdebadarshini.androidtexteditor.FILENAME";

    File f = new File(cachefilepath);
    File file[] = f.listFiles();
    String[] fileNames = new String[file.length];
    String filename_selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.filebrowser);

        Debug.startMethodTracing("Openfile");

        final ListView listview = (ListView) findViewById(R.id.listview);

        // Log.d("Files", "Path: " + cachefilepath);
        // Log.d("Files", "Size: "+ file.length);
        for (int i=0; i < file.length; i++)
        {
            // Log.d("Files", "FileName:" + file[i].getName());
            fileNames[i] = file[i].getName();
        }

        final ArrayAdapter adapter =
                new ArrayAdapter(this, android.R.layout.simple_list_item_1, fileNames);

        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                filename_selected = returnFileNameSelected(position);
                // Log.d("File Name selected is:", filenameselected);

            }
        });

        final StringBuilder line = new StringBuilder();
        /*
         * Click event for the Select button
         */
        final Button selectBtn = (Button) findViewById(R.id.select);
        selectBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Pass the filename selected and open the file in the EditView.
                try{
                    FileInputStream fis = openFileInput(filename_selected);
                    try {
                        int data = fis.read();
                        while (data != -1) {
                            // System.out.println((char) data);
                            line.append((char)data);
                            data = fis.read();
                        }
                        fis.close();
                    } catch (IOException io) {
                        io.printStackTrace();
                    }

                } catch (FileNotFoundException ex){
                    ex.printStackTrace();
                }
                Intent in = new Intent (FileBrowserActivity.this, MainActivity.class);
                in.putExtra(filetext, line.toString());
                in.putExtra(filenameabs, cachefilepath + '/' + filename_selected);
                startActivity(in);
                finish();
            }
        });

        Debug.stopMethodTracing();

    }
    // Give button option Open to open a selected file in the editor screen or Cancel
    // Be able to return back to the main screen
    // choose spinner_file.setPosition(0) to return the state back.

    public String returnFileNameSelected(int pos) {
        return fileNames[pos];
    }



    @Override
    protected void onResume() {
        super.onResume();

    }
}
