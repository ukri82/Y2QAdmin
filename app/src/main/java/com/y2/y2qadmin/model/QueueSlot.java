package com.y2.y2qadmin.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by u on 22.01.2017.
 */

public class QueueSlot implements Serializable
{

    public String mId;
    public String mQId;
    public String mQName;
    public String mQState;

    public String mPhotoURL;

    public Date mStartTime;
    public Date mEndTime;
    public int mCurrentTokenNumber;
}
