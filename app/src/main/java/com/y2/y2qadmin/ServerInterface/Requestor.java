package com.y2.y2qadmin.ServerInterface;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Windows on 02-03-2015.
 */
public class Requestor
{

    public static JSONObject request(RequestQueue requestQueue, String url)
    {
        JSONObject response = null;
        RequestFuture<JSONObject> requestFuture = RequestFuture.newFuture();

        Log.d("Requestor", "URL requested is :" + url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, (String)null, requestFuture, requestFuture);

        requestQueue.add(request);

        try
        {
            response = requestFuture.get(30000, TimeUnit.MILLISECONDS);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        } catch (ExecutionException e)
        {
            e.printStackTrace();
        } catch (TimeoutException e)
        {
            e.printStackTrace();
        }
        return response;
    }
}
