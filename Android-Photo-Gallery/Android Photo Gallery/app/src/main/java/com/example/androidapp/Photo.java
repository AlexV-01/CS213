package com.example.androidapp;

import java.util.ArrayList;

public class Photo {
    private String name;
    private ArrayList<Album> albums = new ArrayList<Album>();
    private String location = "";
    private ArrayList<String> people = new ArrayList<String>();
    private String path;

    public Photo(String n, Album a, String p) {
        name = n;
        albums.add(a);
        path = p;
    }

    public void addTagLocation(String loc) {
        location = loc;
    }

    public boolean addTagPerson(String per) {
        if (!people.contains(per)) {
            people.add(per);
            return true;
        }
        return false;
    }

    public String deleteTagLocation() {
        String ret = location;
        location = "";
        return ret;
    }

    public String deleteTagPerson(String per) {
        people.remove(per);
        return per;
    }

    public void setPath(String p) {
        path = p;
    }

    public String getLocation() {
        return location;
    }

    public ArrayList<String> getPeople() {
        return people;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Photo)) return false;
        Photo p = (Photo) o;
        return p.getPath().substring(p.getPath().indexOf(Utility.ALBUM_SEPARATOR) + 1).equals(path);
    }

    @Override
    public String toString() {
        return path;
    }
}
