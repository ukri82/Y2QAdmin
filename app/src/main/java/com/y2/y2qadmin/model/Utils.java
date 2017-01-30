package com.y2.y2qadmin.model;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.y2.y2qadmin.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Windows on 02-03-2015.
 */
public class Utils
{
    public static boolean contains(JSONObject jsonObject, String key)
    {
        return jsonObject != null && jsonObject.has(key) && !jsonObject.isNull(key) ? true : false;
    }

    public static String get(JSONObject jsonObject, String key) throws JSONException
    {
        if (contains(jsonObject, key))
        {
            return jsonObject.getString(key);
        }

        return "N.A";
    }

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    /**
     * Generate a value suitable for use in {@link #setId(int)}.
     * This value will not collide with ID values generated at build time by aapt for R.id.
     *
     * @return a generated ID value
     */
    public static int generateViewId()
    {
        for (; ; )
        {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue))
            {
                return result;
            }
        }
    }

    static DateFormat mInDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public static Date parseDate(String dateString)
    {
        Date dateOut = null;
        try
        {
            if (!dateString.isEmpty() && dateString.compareTo("N.A") != 0)
                dateOut = mInDateFormat.parse(dateString);
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
        return dateOut;
    }

    static DateFormat mOutDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");

    public static String printDate(Date date)
    {
        return mOutDateFormat.format(date);
    }

    static DateFormat mPrintDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public static String printDateToScreen(Date date)
    {
        return mPrintDateFormat.format(date);
    }


    public static void updateBgColor(Activity activity, View view, String qState)
    {
        int colorId = R.color.not_started_slot_color;
        if (qState.compareTo("not_started") == 0)
            colorId = R.color.not_started_slot_color;
        if (qState.compareTo("started") == 0)
            colorId = R.color.started_slot_color;
        if (qState.compareTo("paused") == 0)
            colorId = R.color.paused_slot_color;
        if (qState.compareTo("ended") == 0)
            colorId = R.color.stopped_slot_color;

        view.setBackgroundColor(ContextCompat.getColor(activity, colorId));

    }

    public static void setQueueStatusIcon(View view, String qState)
    {
        int imageId = R.drawable.queue_not_started;
        if (qState.compareTo("started") == 0)
            imageId = R.drawable.queue_started;
        if (qState.compareTo("paused") == 0)
            imageId = R.drawable.queue_paused;
        if (qState.compareTo("ended") == 0)
            imageId = R.drawable.queue_stopped;

        view.setBackgroundResource(imageId);
    }

    public static void setImage(ImageLoader imageLoader, String urlThumbnail, final ImageView imageView, final int defaultResId)
    {

        if (!urlThumbnail.equals("N.A") && !urlThumbnail.equals(""))
        {
            imageLoader.get(urlThumbnail, new ImageLoader.ImageListener()
            {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate)
                {
                    imageView.setImageBitmap(response.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError error)
                {
                    imageView.setImageResource(defaultResId);
                }
            });
        } else
        {
            imageView.setImageResource(defaultResId);
        }
    }

}
