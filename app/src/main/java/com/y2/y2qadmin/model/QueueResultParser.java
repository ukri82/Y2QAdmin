package com.y2.y2qadmin.model;

import com.y2.y2qadmin.ServerInterface.Endpoints;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Windows on 02-03-2015.
 */
public class QueueResultParser
{

    public static ArrayList<Queue> parse(JSONObject response)
    {
        ArrayList<Queue> listQueues = new ArrayList<>();
        if (response != null && response.length() > 0)
        {
            try
            {
                JSONArray arrayItems = response.getJSONArray("QueueData");
                for (int i = 0; i < arrayItems.length(); i++)
                {
                    JSONObject currentItem = arrayItems.getJSONObject(i);

                    Queue queue = parseOne(currentItem);

                    if(queue != null)
                        listQueues.add(queue);

                }

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

        }
        return listQueues;
    }


    public static Queue parseOne(JSONObject response)
    {
        Queue queue = null;
        if (response != null)
        {
            try
            {
                queue = new Queue();

                JSONObject currentQItem = response.getJSONObject("y2q_queue");

                queue.mId = Utils.get(currentQItem, "id");
                queue.mQName = Utils.get(currentQItem, "m_name");
                queue.mPhone = Utils.get(currentQItem, "m_phone");
                queue.mAddress = Utils.get(currentQItem, "m_address");
                String imgURL = Utils.get(currentQItem, "m_photo_url");
                queue.mPhotoURL = "";
                if (!imgURL.isEmpty())
                {
                    queue.mPhotoURL = Endpoints.getImageDownloadURL(imgURL);
                }

            } catch (JSONException e)
            {
                e.printStackTrace();
            }

        }
        return queue;
    }


}
