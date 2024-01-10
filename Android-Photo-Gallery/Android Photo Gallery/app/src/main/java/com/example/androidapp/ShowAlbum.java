package com.example.androidapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

public class ShowAlbum extends AppCompatActivity {

    private Album album;
    private GridView photoGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_album);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get info from bundle
        Bundle bundle = getIntent().getExtras();
        album = Utility.albums.get(bundle.getInt("album_pos"));

        // set name of album
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SpannableString spannableString = new SpannableString(album.getName());
        int color = getResources().getColor(R.color.white);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
        spannableString.setSpan(colorSpan, 0, spannableString.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        getSupportActionBar().setTitle(spannableString);

        // load screen with info
        photoGrid = (GridView) findViewById(R.id.gridview);
        List<String> filePaths = new ArrayList<String>();
        for (File f : getFilesDir().listFiles()) {
            if (f.getName().contains(Utility.ALBUM_SEPARATOR) && f.getName().substring(0, f.getName().indexOf(Utility.ALBUM_SEPARATOR)).equals(album.getName())) {
                filePaths.add(f.getAbsolutePath());
            }
        }
        Collections.sort(filePaths);
        photoGrid.setAdapter(new ImageAdapter(this, filePaths));
        photoGrid.setNumColumns(3);
        photoGrid.setOnItemClickListener((p, v, pos, id) -> openPhoto(pos));
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

    private void openPhoto(int pos) {
        Bundle bundle = new Bundle();
        bundle.putInt("album_pos", Utility.albums.indexOf(album));
        bundle.putInt("photo_pos", pos);
        Intent intent = new Intent(this, ShowPhoto.class);
        intent.putExtras(bundle);
        Utility.oldAlbum = this;
        startActivity(intent);
    }

    private void addPhoto() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    public void refreshGrid() {
        List<String> filePaths = new ArrayList<String>();
        for (File f : getFilesDir().listFiles()) {
            if (f.getName().contains(Utility.ALBUM_SEPARATOR) && f.getName().substring(0, f.getName().indexOf(Utility.ALBUM_SEPARATOR)).equals(album.getName())) {
                filePaths.add(f.getAbsolutePath());
            }
        }
        Collections.sort(filePaths);
        photoGrid.setAdapter(new ImageAdapter(this, filePaths));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            InputStream inputStream = null;
            try {
                inputStream = getContentResolver().openInputStream(uri);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            String pName = getFileName(uri);
            if (album.containsPhoto(new Photo(pName, album, getFileName(uri)))) {
                Toast.makeText(this, "Duplicate photo.", Toast.LENGTH_LONG).show();
                return;
            }
            album.addPhoto(new Photo(pName, album, getFilesDir() + "/" + album.getName() + Utility.ALBUM_SEPARATOR + getFileName(uri)));
            String imgName = album.getName() + Utility.ALBUM_SEPARATOR + pName;
            File outputFile = new File(getFilesDir(), imgName);
            OutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(outputFile);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            byte[] buffer = new byte[1024];
            int length;
            while (true) {
                try {
                    if (!((length = inputStream.read(buffer)) > 0)) break;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    outputStream.write(buffer, 0, length);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                outputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            photoGrid = (GridView) findViewById(R.id.gridview);
            List<String> filePaths = new ArrayList<String>();
            for (File f : getFilesDir().listFiles()) {
                if (f.getName().contains(Utility.ALBUM_SEPARATOR) && f.getName().substring(0, f.getName().indexOf(Utility.ALBUM_SEPARATOR)).equals(album.getName())) {
                    filePaths.add(f.getAbsolutePath());
                }
            }
            Collections.sort(filePaths);
            photoGrid.setAdapter(new ImageAdapter(this, filePaths));
        }
    }

    @SuppressLint("Range")
    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void deletePhoto() {
        if (album.isEmpty()) {
            Toast.makeText(this, "No photo exists to delete.", Toast.LENGTH_LONG).show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        TextView title = new TextView(this);
        title.setText("Delete Photo");
        title.setGravity(Gravity.CENTER);
        title.setPadding(10, 40, 10, 10);
        title.setTextSize(24);
        builder.setCustomTitle(title);
        final Spinner spinner = new Spinner(this);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, album.getPhotos().stream().map(p -> p.getName()).toArray(String[]::new));
        spinner.setAdapter(adapter);
        spinner.setPadding(40, 20, 40, 20);
        builder.setView(spinner);
        Context c = this;
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = (String) spinner.getSelectedItem();
                album.removePhoto(name);
                for (File f : getFilesDir().listFiles()) {
                    if (f.getName().contains(Utility.ALBUM_SEPARATOR) && f.getName().substring(0, f.getName().indexOf(Utility.ALBUM_SEPARATOR)).equals(album.getName()) && f.getName().substring(f.getName().indexOf(Utility.ALBUM_SEPARATOR) + 1).equals(name)) {
                        f.delete();
                    }
                }
                List<String> filePaths = new ArrayList<String>();
                for (File f : getFilesDir().listFiles()) {
                    if (f.getName().contains(Utility.ALBUM_SEPARATOR) && f.getName().substring(0, f.getName().indexOf(Utility.ALBUM_SEPARATOR)).equals(album.getName())) {
                        filePaths.add(f.getAbsolutePath());
                    }
                }
                Collections.sort(filePaths);
                photoGrid.setAdapter(new ImageAdapter(c, filePaths));
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

    private void renameAlbum() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        TextView title = new TextView(this);
        title.setText("Rename Album");
        title.setGravity(Gravity.CENTER);
        title.setPadding(10, 40, 10, 10);
        title.setTextSize(24);
        builder.setCustomTitle(title);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(album.getName());
        input.setHint("New album name");
        input.setPadding(40, 20, 40, 20);
        builder.setView(input);
        Context c = this;
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String aName = input.getText().toString();
                if (Utility.albumNames.contains(aName)) {
                    Toast.makeText(c, "Please choose a unique album name.", Toast.LENGTH_LONG).show();
                    return;
                }
                for (File f : getFilesDir().listFiles()) {
                    if (f.getName().contains(Utility.ALBUM_SEPARATOR) && f.getName().substring(0, f.getName().indexOf(Utility.ALBUM_SEPARATOR)).equals(album.getName())) {
                        String parentPath = f.getParent();
                        String newFileName = aName + Utility.ALBUM_SEPARATOR + f.getName().substring(f.getName().indexOf(Utility.ALBUM_SEPARATOR) + 1);
                        File newFile = new File(parentPath + File.separator + newFileName);
                        f.renameTo(newFile);
                    }
                }
                album.setName(aName);
                Toolbar toolbar = findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                getSupportActionBar().setTitle(aName);
                Utility.initialize();
                Photos.updateListView();
                List<String> filePaths = new ArrayList<String>();
                for (File f : getFilesDir().listFiles()) {
                    if (f.getName().contains(Utility.ALBUM_SEPARATOR) && f.getName().substring(0, f.getName().indexOf(Utility.ALBUM_SEPARATOR)).equals(album.getName())) {
                        filePaths.add(f.getAbsolutePath());
                    }
                }
                Collections.sort(filePaths);
                photoGrid.setAdapter(new ImageAdapter(c, filePaths));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.album_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                try {
                    addPhoto();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return true;
            case R.id.action_delete:
                try {
                    deletePhoto();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return true;
            case R.id.action_edit:
                try {
                    renameAlbum();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
