package com.example.androidapp;

import static java.util.stream.Collectors.toList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Search extends AppCompatActivity {

    private Switch conjunction;
    private GridView resultGrid;
    private Button search;
    private EditText field1;
    private EditText field2;
    private Spinner option1;
    private Spinner option2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // initialize objects
        conjunction = findViewById(R.id.conjunction);
        conjunction.setChecked(false);
        resultGrid = findViewById(R.id.results);
        search = findViewById(R.id.search);
        field1 = findViewById(R.id.field1);
        field2 = findViewById(R.id.field2);
        field1.setEnabled(false);
        field2.setEnabled(false);
        option1 = findViewById(R.id.select1);
        option2 = findViewById(R.id.select2);
        option1.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[] {"(None)", "Location", "Person"}));
        option2.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[] {"(None)", "Location", "Person"}));

        // setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SpannableString spannableString = new SpannableString("Photo Search");
        int color = getResources().getColor(R.color.white);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
        spannableString.setSpan(colorSpan, 0, spannableString.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        getSupportActionBar().setTitle(spannableString);

        // set click listeners
        conjunction.setOnCheckedChangeListener((b, c) -> checkSwitch(c));
        search.setOnClickListener(b -> displayResults());
        option1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (!((String) option1.getSelectedItem()).equals("(None)")) {
                    field1.setEnabled(true);
                } else {
                    field1.setText("");
                    field1.setEnabled(false);
                }
            }
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        option2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (!((String) option2.getSelectedItem()).equals("(None)")) {
                    field2.setEnabled(true);
                } else {
                    field2.setText("");
                    field2.setEnabled(false);
                }
            }
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void checkSwitch(boolean c) {
        if (c) {
            conjunction.setText("AND");
        } else {
            conjunction.setText("OR");
        }
    }

    private ArrayList<Photo> fetchResults() {
        ArrayList<Photo> results = null;
        ArrayList<Photo> all = new ArrayList<Photo>();
        for (Album a : Utility.albums) for (Photo p : a.getPhotos()) all.add(p);
        String f1 = field1.getText().toString();
        String f2 = field2.getText().toString();
        if (option1.getSelectedItem().equals("(None)") && option2.getSelectedItem().equals("(None)")) return null;
        if (option1.getSelectedItem().equals("(None)") && option2.getSelectedItem().equals("Location")) results = (ArrayList<Photo>) all.stream().filter(p -> p.getLocation().startsWith(f2)).collect(toList());
        if (option1.getSelectedItem().equals("(None)") && option2.getSelectedItem().equals("Person")) results = (ArrayList<Photo>) all.stream().filter(p -> p.getPeople().stream().anyMatch(pe -> pe.startsWith(f2))).collect(toList());
        if (option1.getSelectedItem().equals("Location") && option2.getSelectedItem().equals("(None)")) results = (ArrayList<Photo>) all.stream().filter(p -> p.getLocation().startsWith(f1)).collect(toList());
        if (option1.getSelectedItem().equals("Person") && option2.getSelectedItem().equals("(None)")) results = (ArrayList<Photo>) all.stream().filter(p -> p.getPeople().stream().anyMatch(pe -> pe.startsWith(f1))).collect(toList());
        if (conjunction.isChecked()) {
            if (option1.getSelectedItem().equals("Location") && option2.getSelectedItem().equals("Location")) results = (ArrayList<Photo>) all.stream().filter(p -> p.getLocation().startsWith(f1) && p.getLocation().startsWith(f2)).collect(toList());
            if (option1.getSelectedItem().equals("Location") && option2.getSelectedItem().equals("Person")) results = (ArrayList<Photo>) all.stream().filter(p -> p.getLocation().startsWith(f1) && p.getPeople().stream().anyMatch(pe -> pe.startsWith(f2))).collect(toList());
            if (option1.getSelectedItem().equals("Person") && option2.getSelectedItem().equals("Location")) results = (ArrayList<Photo>) all.stream().filter(p -> p.getLocation().startsWith(f2) && p.getPeople().stream().anyMatch(pe -> pe.startsWith(f1))).collect(toList());
            if (option1.getSelectedItem().equals("Person") && option2.getSelectedItem().equals("Person")) results = (ArrayList<Photo>) all.stream().filter(p -> p.getPeople().stream().anyMatch(pe -> pe.startsWith(f1)) && p.getPeople().stream().anyMatch(pe -> pe.startsWith(f2))).collect(toList());
        } else {
            if (option1.getSelectedItem().equals("Location") && option2.getSelectedItem().equals("Location")) results = (ArrayList<Photo>) all.stream().filter(p -> p.getLocation().startsWith(f1) || p.getLocation().startsWith(f2)).collect(toList());
            if (option1.getSelectedItem().equals("Location") && option2.getSelectedItem().equals("Person")) results = (ArrayList<Photo>) all.stream().filter(p -> p.getLocation().startsWith(f1) || p.getPeople().stream().anyMatch(pe -> pe.startsWith(f2))).collect(toList());
            if (option1.getSelectedItem().equals("Person") && option2.getSelectedItem().equals("Location")) results = (ArrayList<Photo>) all.stream().filter(p -> p.getLocation().startsWith(f2) || p.getPeople().stream().anyMatch(pe -> pe.startsWith(f1))).collect(toList());
            if (option1.getSelectedItem().equals("Person") && option2.getSelectedItem().equals("Person")) results = (ArrayList<Photo>) all.stream().filter(p -> p.getPeople().stream().anyMatch(pe -> pe.startsWith(f1)) || p.getPeople().stream().anyMatch(pe -> pe.startsWith(f2))).collect(toList());
        }
        return results;
    }

    private void displayResults() {
        // hide keyboard
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        ArrayList<Photo> results = fetchResults();
        if (results == null) {
            Toast.makeText(this, "Please enter at least one tag value.", Toast.LENGTH_SHORT).show();
            return;
        }
        resultGrid = (GridView) findViewById(R.id.results);
        List<String> filePaths = Arrays.asList(getFilesDir().listFiles()).stream().map(f -> f.getPath()).filter(p -> results.stream().map(r -> r.getPath()).anyMatch(pa -> pa.equals(p))).collect(toList());
        Collections.sort(filePaths);
        resultGrid.setAdapter(new Search.ImageAdapter(this, filePaths));
        resultGrid.setNumColumns(3);
    }

    public class ImageAdapter extends BaseAdapter {
        private Context mContext;
        List<String> mFilePaths;

        public ImageAdapter(Context c, List<String> filePaths) {
            mContext = c;
            mFilePaths = filePaths;
        }

        public int getCount() {
            return mFilePaths.size();
        }

        public Object getItem(int position) {
            return mFilePaths.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(180, 180));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(0, 50, 0, 50);
            } else {
                imageView = (ImageView) convertView;
            }
            String filePath = mFilePaths.get(position);
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            imageView.setImageBitmap(bitmap);
            return imageView;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                try {
                    onBackPressed();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}