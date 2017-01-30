package com.y2.y2qadmin.model;

import com.y2.y2qadmin.ServerInterface.Endpoints;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Windows on 02-03-2015.
 */
public class QueueSlotResultParser
{

    public static ArrayList<QueueSlot> parse(JSONObject response)
    {
        ArrayList<QueueSlot> listQueueSlots = new ArrayList<>();
        if (response != null && response.length() > 0)
        {
            try
            {
                JSONArray arrayItems = response.getJSONArray("QueueSlotData");
                for (int i = 0; i < arrayItems.length(); i++)
                {
                    JSONObject currentItem = arrayItems.getJSONObject(i);

                    QueueSlot qSlot = parseOne(currentItem);

                    if(qSlot != null)
                        listQueueSlots.add(qSlot);

                }

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

        }
        return listQueueSlots;
    }


    public static QueueSlot parseOne(JSONObject response)
    {
        QueueSlot qSlot = null;
        if (response != null)
        {
            try
            {
                qSlot = new QueueSlot();

                JSONObject currentQItem = response.getJSONObject("y2q_queue");
                JSONObject currentQSlotItem = response.getJSONObject("y2q_queue_slot");


                qSlot.mId = Utils.get(currentQSlotItem, "id");
                qSlot.mQId = Utils.get(currentQItem, "id");
                qSlot.mQName = Utils.get(currentQItem, "m_name");
                qSlot.mQState = Utils.get(currentQSlotItem, "m_state");
                qSlot.mStartTime = Utils.parseDate(Utils.get(currentQSlotItem, "m_start_time"));
                qSlot.mEndTime = Utils.parseDate(Utils.get(currentQSlotItem, "m_end_time"));
                qSlot.mCreationTime = Utils.parseDate(Utils.get(currentQSlotItem, "m_creation_time"));
                qSlot.mPauseTime = Utils.parseDate(Utils.get(currentQSlotItem, "m_pause_time"));
                qSlot.mCurrentTokenTime = Utils.parseDate(Utils.get(currentQSlotItem, "m_current_token_time"));
                String imgURL = Utils.get(currentQItem, "m_photo_url");
                qSlot.mPhotoURL = "";
                if (!imgURL.isEmpty())
                {
                    qSlot.mPhotoURL = Endpoints.getImageDownloadURL(imgURL);
                }

                String tokenNum = Utils.get(currentQSlotItem, "m_token_number");
                if (tokenNum.compareTo("N.A") == 0)
                {
                    tokenNum = "0";
                }
                qSlot.mCurrentTokenNumber = Integer.parseInt(tokenNum);

                String expTokens = Utils.get(currentQSlotItem, "m_expected_tokens");
                if (expTokens.compareTo("N.A") == 0)
                {
                    expTokens = "0";
                }
                qSlot.mExpectedTokens = Integer.parseInt(expTokens);


            } catch (JSONException e)
            {
                e.printStackTrace();
            }

        }
        return qSlot;
    }


}
