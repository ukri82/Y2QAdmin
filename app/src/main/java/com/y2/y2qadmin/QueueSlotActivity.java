package com.y2.y2qadmin;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.y2.y2qadmin.ServerInterface.TaskUpdateQueueSlot;
import com.y2.y2qadmin.misc.TimeRangeDisplay;
import com.y2.y2qadmin.misc.VolleySingleton;
import com.y2.y2qadmin.model.Queue;
import com.y2.y2qadmin.model.QueueSlot;
import com.y2.y2qadmin.model.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import pl.polak.clicknumberpicker.ClickNumberPickerListener;
import pl.polak.clicknumberpicker.ClickNumberPickerView;
import pl.polak.clicknumberpicker.PickerClickType;

public class QueueSlotActivity extends AppCompatActivity implements TaskUpdateQueueSlot.QueueSlotsUpdateListener
{

    QueueSlot mQueueSlot;
    Queue mQueue;

    private VolleySingleton myVolleySingleton;
    private ImageLoader myImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        myVolleySingleton = VolleySingleton.getInstance(null);
        myImageLoader = myVolleySingleton.getImageLoader();

        setContentView(R.layout.activity_queue_slot);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        mQueueSlot = (QueueSlot)intent.getSerializableExtra(QueueSlot.QUEUE_SLOT_OBJ);
        mQueue = (Queue)intent.getSerializableExtra(Queue.QUEUE_OBJ);
        updateUI();

        initialeHandler();
    }

    void createupdateQSlotTask(TaskUpdateQueueSlot.UpdateStatus taskStatus, int tokenNumber)
    {
        new TaskUpdateQueueSlot(this, mQueueSlot.mId, taskStatus, tokenNumber, Calendar.getInstance().getTime()).execute();
    }

    void initialeHandler()
    {

        ImageButton button = (ImageButton)findViewById(R.id.start);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ClickNumberPickerView pickerView = (ClickNumberPickerView)findViewById(R.id.token_number_picker);
                //EditText tokenNumText = (EditText)findViewById(R.id.token_number);
                //int tokenNum = Integer.parseInt(tokenNumText.getText().toString());
                createupdateQSlotTask(TaskUpdateQueueSlot.UpdateStatus.Start, (int)pickerView.getValue());
            }
        });

        button = (ImageButton)findViewById(R.id.stop);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                createupdateQSlotTask(TaskUpdateQueueSlot.UpdateStatus.Stop, mQueueSlot.mCurrentTokenNumber);
            }
        });

        button = (ImageButton)findViewById(R.id.pause);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                createupdateQSlotTask(TaskUpdateQueueSlot.UpdateStatus.Pause, mQueueSlot.mCurrentTokenNumber);
            }
        });

        /*button = (ImageButton)findViewById(R.id.next);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                createupdateQSlotTask(TaskUpdateQueueSlot.UpdateStatus.Set, mQueueSlot.mCurrentTokenNumber + 1);
            }
        });

        button = (ImageButton)findViewById(R.id.prev);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                createupdateQSlotTask(TaskUpdateQueueSlot.UpdateStatus.Set, mQueueSlot.mCurrentTokenNumber - 1);
            }
        });*/

        final ClickNumberPickerView pickerView = (ClickNumberPickerView)findViewById(R.id.token_number_picker);
        pickerView.setClickNumberPickerListener(new ClickNumberPickerListener()
        {
            @Override
            public void onValueChange(float previousValue, float currentValue, PickerClickType pickerClickType)
            {
                int tokenNumber = (int)currentValue;
                if(mQueueSlot.mCurrentTokenNumber != tokenNumber)
                    createupdateQSlotTask(TaskUpdateQueueSlot.UpdateStatus.Set, tokenNumber);
            }
        });
    }

    @Override
    public void onQueueSlotUpdated(QueueSlot queueSlot)
    {
        mQueueSlot = queueSlot;
        updateUI();

    }

    private void updateUI()
    {
        TextView v = (TextView)findViewById(R.id.queue_name);
        v.setFilters( new InputFilter[] { new InputFilter.LengthFilter(30) } );
        v.setText(mQueue.mQName);


        v = (TextView)findViewById(R.id.queue_address);
        v.setText(mQueue.mAddress);

        ImageView imageView = (ImageView)findViewById(R.id.queue_picture);
        Utils.setImage(myImageLoader, mQueue.mPhotoURL, imageView, R.drawable.ic_person_black_48dp);

        v = (TextView)findViewById(R.id.slot_status);
        String statusText = "Slot Status : ";
        if(mQueueSlot.mQState.compareTo("not_started") == 0)
            statusText += "Not started";
        if(mQueueSlot.mQState.compareTo("started") == 0)
            statusText += "Started";
        if(mQueueSlot.mQState.compareTo("paused") == 0)
            statusText += "Paused";
        if(mQueueSlot.mQState.compareTo("ended") == 0)
            statusText += "Ended";
        v.setText(statusText);

        TimeRangeDisplay timeRangeDisplay = (TimeRangeDisplay) findViewById(R.id.slot_start_time);
        if(mQueueSlot.mStartTime != null)
        {
            timeRangeDisplay.setVisibility(View.VISIBLE);
            timeRangeDisplay.setTime(mQueueSlot.mStartTime);
            timeRangeDisplay.disableTouch(true);
        }
        else
        {
            timeRangeDisplay.setVisibility(View.INVISIBLE);
        }

        timeRangeDisplay = (TimeRangeDisplay) findViewById(R.id.slot_end_time);
        if(mQueueSlot.mEndTime != null)
        {
            timeRangeDisplay.setVisibility(View.VISIBLE);
            timeRangeDisplay.setTime(mQueueSlot.mEndTime);
            timeRangeDisplay.disableTouch(true);
        }
        else
        {
            timeRangeDisplay.setVisibility(View.INVISIBLE);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        if(mQueueSlot.mStartTime != null)
        {
            v = (TextView) findViewById(R.id.slot_date);
            String dateStr = "";
            if(mQueueSlot.mStartTime != null)
                dateStr = dateStr + sdf.format(mQueueSlot.mStartTime);

            v.setText(dateStr);
        }

        //v.setText(mQueueSlot.mQName);

        /*SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        if(mQueueSlot.mStartTime != null)
        {
            v = (TextView) findViewById(R.id.start_time);
            String dateStr = "Start : ";
            if(mQueueSlot.mStartTime != null)
                dateStr = dateStr + sdf.format(mQueueSlot.mStartTime);

            v.setText(dateStr);
        }

        if(mQueueSlot.mEndTime != null)
        {
            v = (TextView) findViewById(R.id.end_time);
            String dateStr = "End : ";
            if(mQueueSlot.mEndTime != null)
                dateStr = dateStr + sdf.format(mQueueSlot.mEndTime);
            v.setText(dateStr);
        }*/

        /*EditText tokenNumText = (EditText)findViewById(R.id.token_number);
        tokenNumText.setText(mQueueSlot.mCurrentTokenNumber + "");*/

        ClickNumberPickerView pickerView = (ClickNumberPickerView)findViewById(R.id.token_number_picker);
        pickerView.setPickerValue(mQueueSlot.mCurrentTokenNumber);

        //Utils.updateBgColor(this, findViewById(R.id.queue_slot_holder), mQueueSlot.mQState);
        Utils.setQueueStatusIcon(findViewById(R.id.queue_status_icon), mQueueSlot.mQState);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home)
        {
            overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
