package com.y2.y2qadmin;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.y2.y2qadmin.ServerInterface.TaskUpdateQueueSlot;
import com.y2.y2qadmin.model.QueueSlot;
import com.y2.y2qadmin.model.Utils;

import java.text.SimpleDateFormat;

public class QueueSlotActivity extends AppCompatActivity implements TaskUpdateQueueSlot.QueueSlotsUpdateListener
{

    QueueSlot mQueueSlot;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue_slot);

        Intent intent = getIntent();
        mQueueSlot = (QueueSlot)intent.getSerializableExtra(QueueSlotListActivityFragment.QUEUE_SLOT_OBJ);

        updateUI();

        initialeHandler();
    }

    void initialeHandler()
    {

        final TaskUpdateQueueSlot.QueueSlotsUpdateListener listener = this;
        ImageButton button = (ImageButton)findViewById(R.id.start);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                EditText tokenNumText = (EditText)findViewById(R.id.token_number);
                final int tokenNum = Integer.parseInt(tokenNumText.getText().toString());
                new TaskUpdateQueueSlot(listener, mQueueSlot.mId, TaskUpdateQueueSlot.UpdateStatus.Start, tokenNum).execute();
            }
        });

        button = (ImageButton)findViewById(R.id.stop);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                new TaskUpdateQueueSlot(listener, mQueueSlot.mId, TaskUpdateQueueSlot.UpdateStatus.Stop, mQueueSlot.mCurrentTokenNumber).execute();
            }
        });

        button = (ImageButton)findViewById(R.id.pause);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                new TaskUpdateQueueSlot(listener, mQueueSlot.mId, TaskUpdateQueueSlot.UpdateStatus.Pause, mQueueSlot.mCurrentTokenNumber).execute();
            }
        });

        button = (ImageButton)findViewById(R.id.next);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                new TaskUpdateQueueSlot(listener, mQueueSlot.mId, TaskUpdateQueueSlot.UpdateStatus.Set, mQueueSlot.mCurrentTokenNumber + 1).execute();
            }
        });

        button = (ImageButton)findViewById(R.id.prev);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                new TaskUpdateQueueSlot(listener, mQueueSlot.mId, TaskUpdateQueueSlot.UpdateStatus.Set, mQueueSlot.mCurrentTokenNumber - 1).execute();
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
        v.setText(mQueueSlot.mQName);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        if(mQueueSlot.mStartTime != null)
        {
            v = (TextView) findViewById(R.id.start_time);
            v.setText("Start: " + sdf.format(mQueueSlot.mStartTime));
        }

        if(mQueueSlot.mEndTime != null)
        {
            v = (TextView) findViewById(R.id.end_time);
            v.setText("End: " + sdf.format(mQueueSlot.mEndTime));
        }

        EditText tokenNumText = (EditText)findViewById(R.id.token_number);
        tokenNumText.setText(mQueueSlot.mCurrentTokenNumber + "");

        Utils.updateBgColor(this, findViewById(R.id.activity_queue_slot), mQueueSlot.mQState);


    }
}
