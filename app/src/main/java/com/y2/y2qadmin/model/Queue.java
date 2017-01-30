package com.y2.y2qadmin.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by u on 22.01.2017.
 */

public class Queue implements Serializable
{

    public String mId;
    public String mQName;
    public String mPhone;
    public String mAddress;

    public String mPhotoURL;

    public static String QUEUE_OBJ = "Queue";
}
