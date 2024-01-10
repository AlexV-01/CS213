package com.example.androidapp;

import java.util.ArrayList;
import java.util.Comparator;

public class Album {
    private String name;
    private ArrayList<Photo> photos = new ArrayList<Photo>();

    public Album(String n) {
        name = n;
    }

    public ArrayList<Photo> getPhotos() {
        return photos;
    }

    public String getName() {
        return name;
    }

    public void setName(String n) {
        name = n;
    }

    public void addPhoto(Photo p) {
        photos.add(p);
        sortPhotos();
    }

    public static class PathComparator implements Comparator<Photo> {
        public int compare(Photo a, Photo b) {
            return a.getPath().compareTo(b.getPath());
        }
    }

    public void sortPhotos() {
        photos.sort(new PathComparator());
    }

    public void removePhoto(String n) {
        photos.removeIf(ph -> ph.getName().equals(n));
        photos.sort(new PathComparator());
    }

    public boolean isEmpty() {
        return photos.isEmpty();
    }

    public boolean containsPhoto(Photo p) {
        return photos.contains(p);
    }

    @Override
    public String toString() {
        return name;
    }
}
