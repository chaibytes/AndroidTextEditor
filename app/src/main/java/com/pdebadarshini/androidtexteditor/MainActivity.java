package com.pdebadarshini.androidtexteditor;

import android.content.Context;
import android.os.Debug;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.content.Intent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.util.Log;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.view.ViewGroup;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;


public class MainActivity extends ActionBarActivity {

    static String filenameopened = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // File Spinner
        Spinner spinner_file = (Spinner) findViewById(R.id.file_spinner1);
        ArrayAdapter<CharSequence> adapter_file = ArrayAdapter.createFromResource(this,
                R.array.file_array, R.layout.spinner);
        adapter_file.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_file.setAdapter(adapter_file);

        SpinnerFileListener spl = new SpinnerFileListener();
        spinner_file.setOnItemSelectedListener(spl);

        // Edit Spinner
        Spinner spinner_edit = (Spinner) findViewById(R.id.edit_spinner1);
        ArrayAdapter<CharSequence> adapter_edit = ArrayAdapter.createFromResource(this,
                R.array.edit_array, R.layout.spinner);
        adapter_edit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_edit.setAdapter(adapter_edit);

        // Format Spinner
        Spinner spinner_format = (Spinner) findViewById(R.id.format_spinner1);
        ArrayAdapter<CharSequence> adapter_format = ArrayAdapter.createFromResource(this,
                R.array.format_array, R.layout.spinner);
        adapter_format.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_format.setAdapter(adapter_format);

        // Tools Spinner
        Spinner spinner_tools = (Spinner) findViewById(R.id.tools_spinner1);
        ArrayAdapter<CharSequence> adapter_tools = ArrayAdapter.createFromResource(this,
                R.array.tools_array, R.layout.spinner);
        adapter_tools.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_tools.setAdapter(adapter_tools);

        final EditText et = (EditText) findViewById(R.id.edit_view1);
        et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Log.d("Original text", String.valueOf(et.getText()));
                if (String.valueOf(et.getText()).equals("Type something here...")) {
                    // Log.d("Current Text is now", String.valueOf(et.getText()));
                    et.setText("", null);
                }

            }

        });

        Intent intent = getIntent();
        String message = intent.getStringExtra(FileBrowserActivity.filetext);
        String file = intent.getStringExtra(FileBrowserActivity.filenameabs);
        if (message != null) {
            Log.d("Received Message", message);
            et.setText(null);
            et.setText(message);
        }
        if (file != null) {
            filenameopened = file;
        }

        // Explicitly set all the spinners positions to zero to depict initial states
        spinner_file.setSelection(0);
        spinner_edit.setSelection(0);
        spinner_format.setSelection(0);
        spinner_tools.setSelection(0);

    }


        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private final class SpinnerFileListener implements AdapterView.OnItemSelectedListener {

        public SpinnerFileListener() {

        }

        public void onItemSelected(AdapterView<?> parent, View
                view, int pos, long id) {

            // An item was selected. You can retrieve the selected item using
            // parent.getItemAtPosition(pos)
            String item = (String) parent.getItemAtPosition(pos);
            switch (item) {
                case ("Open"):
                    // Open a new previously stored file
                    Intent fbintent = new Intent(MainActivity.this, FileBrowserActivity.class);
                    startActivity(fbintent);

                    break;

                case ("Save"):
                    // Save onto the file opened
                    String filename = new File(filenameopened).getName();
                    // Log.d("Filename is:", filename);
                    // Log.d("Filepath is:", new File(file).getAbsolutePath());
                    EditText e = (EditText) findViewById(R.id.edit_view1);
                    String data = e.getText().toString();
                    // Log.d("File Contents", data);
                    try {
                        FileOutputStream fos = openFileOutput(filename, MODE_PRIVATE);
                        try {
                            fos.write(data.getBytes());
                            fos.close();
                        } catch (IOException io) {
                            io.printStackTrace();
                        }

                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    }

                    // Make a Toast
                    Toast.makeText(getApplicationContext(), "File Saved",
                            Toast.LENGTH_SHORT).show();
                    break;

                case ("Save As"):
                    // Popup window will contain a input text box and two buttons
                    // Save and Cancel

                    //Toast.makeText(getApplicationContext(), "Save As",
                    //        Toast.LENGTH_SHORT).show();
                    LayoutInflater inflater = (LayoutInflater)
                            MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View layout = inflater.inflate(
                            R.layout.saveaspopuplayout, (ViewGroup) findViewById(R.id.popupwin));
                    final PopupWindow popup = new PopupWindow(layout, LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT, true);
                    popup.setTouchable(true);
                    popup.setFocusable(true);
                    popup.showAtLocation(layout, Gravity.CENTER, 0, 0);

                    final EditText popuptext = (EditText) layout.findViewById(R.id.poptextbox);
                    final Button savebtn = (Button) layout.findViewById(R.id.popsavebtn);
                    final Button cancelbtn = (Button) layout.findViewById(R.id.popcancelbtn);

                    savebtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EditText et = (EditText) findViewById(R.id.edit_view1);
                            String data = et.getText().toString();
                            // Get the file name from the textview
                            String name = popuptext.getText().toString();
                            try {
                                FileOutputStream fos = openFileOutput(name, MODE_PRIVATE);
                                try {
                                    fos.write(data.getBytes());
                                    fos.close();
                                } catch (IOException io) {
                                    io.printStackTrace();
                                }

                            } catch (FileNotFoundException ex) {
                                ex.printStackTrace();
                            }
                        }
                    });

                    cancelbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });

                    break;


            }

        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Another interface callback
        }
    }
}
