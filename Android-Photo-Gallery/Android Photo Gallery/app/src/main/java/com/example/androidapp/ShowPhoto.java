package com.example.androidapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ShowPhoto extends AppCompatActivity {

    private Album album;
    private Photo photo;
    private ImageView imageView;
    private TextView photoName;
    private TextView locationTag;
    private HorizontalScrollView peopleTags;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_photo);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get info from bundle
        Bundle bundle = getIntent().getExtras();
        album = Utility.albums.get(bundle.getInt("album_pos"));
        photo = album.getPhotos().get(bundle.getInt("photo_pos"));
        pos = bundle.getInt("photo_pos");

        // setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SpannableString spannableString = new SpannableString("Photo");
        int color = getResources().getColor(R.color.white);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);
        spannableString.setSpan(colorSpan, 0, spannableString.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        getSupportActionBar().setTitle(spannableString);

        iterate(photo, pos);
    }

    public void addTag(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.getMenuInflater().inflate(R.menu.popup_tags, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_person:
                        addPerson();
                        return true;
                    case R.id.action_location:
                        addLocation();
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.show();
    }

    private void addLocation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        TextView title = new TextView(this);
        title.setText("Change Location");
        title.setGravity(Gravity.CENTER);
        title.setPadding(10, 40, 10, 10);
        title.setTextSize(24);
        builder.setCustomTitle(title);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Location");
        input.setPadding(40, 20, 40, 20);
        builder.setView(input);
        Context c = this;
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                photo.addTagLocation(input.getText().toString());
                locationTag.setText("Location: " + photo.getLocation());
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

    private void addPerson() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        TextView title = new TextView(this);
        title.setText("Add Person");
        title.setGravity(Gravity.CENTER);
        title.setPadding(10, 40, 10, 10);
        title.setTextSize(24);
        builder.setCustomTitle(title);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Person");
        input.setPadding(40, 20, 40, 20);
        builder.setView(input);
        Context c = this;
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!photo.addTagPerson(input.getText().toString())) Toast.makeText(c, "This person already exists as a tag.", Toast.LENGTH_LONG).show();
                peopleTags.removeAllViews();
                TextView t = new TextView(c);
                for (int i = 0; i < photo.getPeople().size(); i++) {
                    if (i == photo.getPeople().size() - 1) {
                        t.setText(t.getText() + photo.getPeople().get(i));
                    } else {
                        t.setText(t.getText() + photo.getPeople().get(i) + ", ");
                    }
                }
                peopleTags.addView(t);
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

    public void deleteTag(View v) {
        if (photo.getLocation().length() == 0 && photo.getPeople().size() == 0) {
            Toast.makeText(this, "No tags exist.", Toast.LENGTH_LONG).show();
            return;
        }
        PopupMenu popup = new PopupMenu(this, v);
        popup.getMenuInflater().inflate(R.menu.popup_tags, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_person:
                        deletePerson();
                        return true;
                    case R.id.action_location:
                        deleteLocation();
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.show();
    }

    private void deleteLocation() {
        if (photo.getLocation().length() == 0) {
            Toast.makeText(this, "No location tag exists.", Toast.LENGTH_LONG).show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        TextView title = new TextView(this);
        title.setText("Delete location?");
        title.setGravity(Gravity.CENTER);
        title.setPadding(10, 40, 10, 10);
        title.setTextSize(24);
        builder.setCustomTitle(title);
        Context c = this;
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                photo.deleteTagLocation();
                locationTag.setText("Location: " + photo.getLocation());
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

    private void deletePerson() {
        if (photo.getPeople().size() == 0) {
            Toast.makeText(this, "No people tags exist.", Toast.LENGTH_LONG).show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        TextView title = new TextView(this);
        title.setText("Delete Person");
        title.setGravity(Gravity.CENTER);
        title.setPadding(10, 40, 10, 10);
        title.setTextSize(24);
        builder.setCustomTitle(title);
        final Spinner spinner = new Spinner(this);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, photo.getPeople());
        spinner.setAdapter(adapter);
        spinner.setPadding(40, 20, 40, 20);
        builder.setView(spinner);
        Context c = this;
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String person = (String) spinner.getSelectedItem();
                photo.deleteTagPerson(person);
                peopleTags.removeAllViews();
                TextView t = new TextView(c);
                for (int i = 0; i < photo.getPeople().size(); i++) {
                    if (i == photo.getPeople().size() - 1) {
                        t.setText(t.getText() + photo.getPeople().get(i));
                    } else {
                        t.setText(t.getText() + photo.getPeople().get(i) + ", ");
                    }
                }
                peopleTags.addView(t);
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

    public void movePhoto() {
        if (Utility.albums.size() == 1) {
            Toast.makeText(this, "No possible destination album exists.", Toast.LENGTH_LONG).show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        TextView title = new TextView(this);
        title.setText("Move Photo");
        title.setGravity(Gravity.CENTER);
        title.setPadding(10, 40, 10, 10);
        title.setTextSize(24);
        builder.setCustomTitle(title);
        final Spinner spinner = new Spinner(this);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Utility.albumNames.stream().filter(a -> !a.equals(album.getName())).toArray(String[]::new));
        spinner.setAdapter(adapter);
        spinner.setPadding(40, 20, 40, 20);
        builder.setView(spinner);
        Context c = this;
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = (String) spinner.getSelectedItem();
                Album dest = Utility.albums.stream().filter(a -> a.getName().equals(name)).findAny().orElse(null);
                if (dest == null) throw new RuntimeException("Unable to find album " + name);
                if (dest.getPhotos().stream().map(p -> p.getName()).anyMatch(n -> n.equals(photo.getName()))) {
                    Toast.makeText(c, "This photo already exists in this album.", Toast.LENGTH_LONG).show();
                    return;
                }
                File oldFile = null;
                for (File f : getFilesDir().listFiles()) {
                    if (f.getName().contains(Utility.ALBUM_SEPARATOR) && f.getAbsolutePath().equals(photo.getPath())) {
                        oldFile = f;
                        break;
                    }
                }
                File newFile = new File(oldFile.getParent() + File.separator + dest.getName() + Utility.ALBUM_SEPARATOR + photo.getName());
                oldFile.renameTo(newFile);
                photo.setPath(newFile.getAbsolutePath());
                dest.addPhoto(photo);
                dest.sortPhotos();
                album.removePhoto(photo.getName());
                album.sortPhotos();
                try {
                    Utility.writeApp(c);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                Utility.oldAlbum.refreshGrid();
                finish();
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

    public void previous() {
        if (pos == 0) iterate(album.getPhotos().get(album.getPhotos().size() - 1), album.getPhotos().size() - 1);
        else iterate(album.getPhotos().get(pos - 1), pos - 1);
    }

    public void next() {
        if (pos == album.getPhotos().size() - 1) iterate(album.getPhotos().get(0), 0);
        else iterate(album.getPhotos().get(pos + 1), pos + 1);
    }

    private void iterate(Photo photo, int pos) {
        // set pos
        this.pos = pos;

        // set name of photo
        photoName = findViewById(R.id.photoName);
        if (photo.getName().length() <= 25) {
            photoName.setText(photo.getName());
        } else {
            photoName.setText(photo.getName().substring(0, 22) + "...");
        }

        // set tags
        locationTag = findViewById(R.id.locationTag);
        locationTag.setText("Location: " + photo.getLocation());
        peopleTags = findViewById(R.id.peopleTags);
        peopleTags.removeAllViews();
        TextView t = new TextView(this);
        for (int i = 0; i < photo.getPeople().size(); i++) {
            if (i == photo.getPeople().size() - 1) {
                t.setText(t.getText() + photo.getPeople().get(i));
            } else {
                t.setText(t.getText() + photo.getPeople().get(i) + ", ");
            }
        }
        peopleTags.addView(t);

        // set image
        String filePath = "";
        for (File f : getFilesDir().listFiles()) {
            if (f.getName().contains(Utility.ALBUM_SEPARATOR) && f.getName().substring(0, f.getName().indexOf(Utility.ALBUM_SEPARATOR)).equals(album.getName()) && f.getName().substring(f.getName().indexOf(Utility.ALBUM_SEPARATOR) + 1).equals(photo.getName())) {
                filePath = f.getAbsolutePath();
            }
        }
        imageView = findViewById(R.id.displayImage);
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.photo_menu, menu);
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
            case R.id.action_add:
                try {
                    addTag(findViewById(R.id.action_add));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return true;
            case R.id.action_delete:
                try {
                    deleteTag(findViewById(R.id.action_delete));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return true;
            case R.id.action_move:
                try {
                    movePhoto();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return true;
            case R.id.action_previous:
                try {
                    previous();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return true;
            case R.id.action_next:
                try {
                    next();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}