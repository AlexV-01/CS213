package com.example.androidapp;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

public class Utility {
    public static final String ALBUM_SEPARATOR = "\u058F";
    public static ArrayList<Album> albums = new ArrayList<Album>();
    public static ArrayList<String> albumNames = new ArrayList<String>();
    public static ShowAlbum oldAlbum;

    public static void initialize() {
        albumNames.clear();
        for (Album a : albums) albumNames.add(a.getName());
        sortAll();
    }

    public static class AlbumComparator implements Comparator<Album> {
        public int compare(Album a, Album b) {
            return a.getName().compareTo(b.getName());
        }
    }

    public static void sortAll() {
        Collections.sort(albumNames);
        Collections.sort(albums, new AlbumComparator());
    }

    public static Album getAlbumByName(String name) {
        for (Album a : albums) {
            if (a.getName().equals(name)) return a;
        }
        return null;
    }

    public static void readApp(Context c) throws Exception {
        HashSet<String> aNames = new HashSet<String>();
        for (File f : c.getFilesDir().listFiles()) {
            if (f.getName().contains(ALBUM_SEPARATOR)) {
                aNames.add(f.getName().substring(0, f.getName().indexOf(Utility.ALBUM_SEPARATOR)));
            }
        }
        for (String name : aNames) {
            Album a = new Album(name);
            for (File f : c.getFilesDir().listFiles()) {
                if (f.getName().contains(ALBUM_SEPARATOR) && f.getName().substring(0, f.getName().indexOf(ALBUM_SEPARATOR)).equals(name)) {
                    a.addPhoto(new Photo(f.getName().substring(f.getName().indexOf(Utility.ALBUM_SEPARATOR) + 1), a, f.getAbsolutePath()));
                }
            }
            albums.add(a);
        }
        initialize();

        File info = null;
        for (File f : c.getFilesDir().listFiles()) {
            if (f.getName().equals("info.txt")) info = f;
        }
        if (info == null) {
            info = new File(c.getFilesDir().getAbsolutePath() + File.separator + "info.txt");
            info.getParentFile().mkdirs();
            info.createNewFile();
        }
        Scanner scanner = new Scanner(info);
        while (scanner.hasNextLine() && scanner.hasNext("-----") && scanner.nextLine().equals("-----")) {
            String aName = scanner.nextLine();
            Album aCurr = null;
            for (Album a : albums) {
                if (a.getName().equals(aName)) {
                    aCurr = a;
                    break;
                }
            }
            if (aCurr == null) {
                albums.add(new Album(aName));
                albumNames.add(aName);
                initialize();
            } else {
                while (scanner.hasNext("---") && scanner.nextLine().equals("---")) {
                    String pName = scanner.nextLine();
                    Photo pCurr = null;
                    for (Photo p : aCurr.getPhotos()) {
                        System.out.println(p.getName());
                        if (p.getName().equals(pName)) {
                            pCurr = p;
                            break;
                        }
                    }
                    pCurr.addTagLocation(scanner.nextLine());
                    String per = "";
                    while (!per.equals("-")) {
                        per = scanner.nextLine();
                        if (per.equals("-")) break;
                        pCurr.addTagPerson(per);
                    }
                }
            }
        }
        scanner.close();
    }

    public static void writeApp(Context c) throws Exception {
        File info = null;
        for (File f : c.getFilesDir().listFiles()) {
            if (f.getName().equals("info.txt")) info = f;
        }
        if (info == null) throw new FileNotFoundException("File info.txt does not exist.");
        FileWriter writer = new FileWriter(info);
        new FileWriter(info, false).close();
        for (Album a : albums) {
            writer.write("-----\n");
            writer.write(a.getName() + "\n");
            for (Photo p : a.getPhotos()) {
                writer.write("---\n");
                writer.write(p.getName() + "\n");
                writer.write(p.getLocation() + "\n");
                if (p.getPeople() != null) {
                    for (String person : p.getPeople()) {
                        writer.write(person + "\n");
                    }
                }
                writer.write("-\n");
            }
        }
        writer.close();
    }
}
