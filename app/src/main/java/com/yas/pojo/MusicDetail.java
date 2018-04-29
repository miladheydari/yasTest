package com.yas.pojo;

import android.net.Uri;
import android.os.Parcelable;

import org.parceler.Parcel;

import java.io.Serializable;

/**
 * Created by milad on 4/29/2018.
 */
//@Parcel
public class MusicDetail implements Serializable {
    public String title;
    public String artist;
    public String imagePath;
    public String musicPath;
}
