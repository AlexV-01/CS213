package com.example.androidapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.*;

public class Photos extends AppCompatActivity {

    private ListView listView;
    private static ArrayAdapter<String> adapter;
    private static boolean initialized = false;
    private boolean duplicateAlbum = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // basic initializations
        super.onCreate(savedInstanceState);

        try {
            if (!initialized) {
                Utility.readApp(this);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        initialized = true;
        setContentView(R.layout.photos);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        SpannableString spannableString = new SpannableString("Photo Albums");
        int color = getResources().getColor(R.color.white);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
        spannableString.setSpan(colorSpan, 0, spannableString.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        getSupportActionBar().setTitle(spannableString);
        Utility.initialize();

        // initialize list
        listView = findViewById(R.id.albums);
        adapter = new ArrayAdapter<String>(this, R.layout.album, Utility.albumNames);
        adapter.sort(new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareTo(s2);
            }
        });
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((p, v, pos, id) -> openAlbum(pos));
    }

    private void openAlbum(int pos) {
        Bundle bundle = new Bundle();
        bundle.putInt("album_pos", pos);
        Intent intent = new Intent(this, ShowAlbum.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void addAlbum() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        TextView title = new TextView(this);
        title.setText("Add Album");
        title.setGravity(Gravity.CENTER);
        title.setPadding(10, 40, 10, 10);
        title.setTextSize(24);
        builder.setCustomTitle(title);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Album name");
        input.setPadding(40, 20, 40, 20);
        builder.setView(input);
        Context c = this;
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Utility.albumNames.contains(input.getText().toString())) {
                    Toast.makeText(c, "Please choose a unique album name.", Toast.LENGTH_LONG).show();
                    duplicateAlbum = true;
                    return;
                }
                Utility.albums.add(new Album(input.getText().toString()));
                Utility.albumNames.add(input.getText().toString());
                Utility.sortAll();
                adapter.sort(new Comparator<String>() {
                    @Override
                    public int compare(String s1, String s2) {
                        return s1.compareTo(s2);
                    }
                });
                adapter.notifyDataSetChanged();
                try {
                    Utility.writeApp(c);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
        if (duplicateAlbum) {
            duplicateAlbum = false;
            return;
        }
    }

    private void deleteAlbum() {
        if (Utility.albums.size() == 0) {
            Toast.makeText(this, "No album exists to delete.", Toast.LENGTH_LONG).show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        TextView title = new TextView(this);
        title.setText("Delete Album");
        title.setGravity(Gravity.CENTER);
        title.setPadding(10, 40, 10, 10);
        title.setTextSize(24);
        builder.setCustomTitle(title);
        final Spinner spinner = new Spinner(this);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Utility.albumNames);
        spinner.setAdapter(adapter);
        spinner.setPadding(40, 20, 40, 20);
        builder.setView(spinner);
        Context c = this;
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = (String) spinner.getSelectedItem();
                for (File f : getFilesDir().listFiles()) {
                    if (f.getName().contains(Utility.ALBUM_SEPARATOR) && f.getName().substring(0, f.getName().indexOf(Utility.ALBUM_SEPARATOR)).equals(name)) {
                        f.delete();
                    }
                }
                Utility.albums.remove(Utility.getAlbumByName(name));
                Utility.albumNames.remove(name);
                updateListView();
                try {
                    Utility.writeApp(c);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void search() {
        Intent intent = new Intent(this, Search.class);
        startActivity(intent);
    }

    public static void updateListView() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                try {
                    addAlbum();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return true;
            case R.id.action_delete:
                try {
                    deleteAlbum();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return true;
            case R.id.action_search:
                try {
                    search();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}