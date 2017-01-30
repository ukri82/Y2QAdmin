package com.y2.y2qadmin;

import android.app.Activity;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.y2.y2qadmin.misc.AnimationUtils;
import com.y2.y2qadmin.misc.VolleySingleton;
import com.y2.y2qadmin.model.Queue;
import com.y2.y2qadmin.model.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by u on 05.12.2016.
 */

public class QueueListAdapter extends RecyclerView.Adapter<QueueListAdapter.ViewHolder>
{
    public interface QueueClickListener
    {
        public void onClick(Queue queue);
    }

    QueueClickListener mListener;

    private ArrayList<Queue> mDataset = new ArrayList<>();


    private VolleySingleton myVolleySingleton;
    private ImageLoader myImageLoader;
    private int myPreviousPosition = 0;
    Activity mActivity;
    RecyclerView mParentView;

    public QueueListAdapter(Activity activity, QueueClickListener listener, RecyclerView parentView)
    {
        mActivity = activity;
        mParentView = parentView;
        myVolleySingleton = VolleySingleton.getInstance(null);
        myImageLoader = myVolleySingleton.getImageLoader();
        mListener = listener;
    }


    public void appendQueueList(List<Queue> data)
    {
        int aNumItems = mDataset.size();
        mDataset.addAll(data);

        notifyItemRangeInserted(aNumItems, data.size());
    }


    public void clear()
    {
        int size = mDataset.size();
        mDataset.clear();
        notifyItemRangeRemoved(0, size);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        // each data item is just a string in this case
        public View mParentView;
        public TextView mTextView;
        public TextView mAddressView;
        public ImageView mPictureView;
        public ViewHolder(View v)
        {
            super(v);
            mParentView = v;
            mTextView = (TextView)v.findViewById(R.id.queue_name);
            mAddressView = (TextView)v.findViewById(R.id.queue_address);
            mPictureView = (ImageView)v.findViewById(R.id.queue_picture);
        }
    }



    @Override
    public QueueListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.queue_card, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    /*private void loadImages(String urlThumbnail, final ImageView imageView)
    {
        if (!urlThumbnail.equals("N.A") && !urlThumbnail.equals(""))
        {
            myImageLoader.get(urlThumbnail, new ImageLoader.ImageListener()
            {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate)
                {
                    imageView.setImageBitmap(response.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError error)
                {
                    imageView.setImageResource(R.drawable.ic_person_black_48dp);
                }
            });
        }
        else
        {
            imageView.setImageResource(R.drawable.ic_person_black_48dp);
        }
    }*/


    @Override
    public void onBindViewHolder(QueueListAdapter.ViewHolder holder, final int position)
    {
        holder.mParentView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mListener.onClick(mDataset.get(position));
            }
        });


        holder.mTextView.setText(mDataset.get(position).mQName);
        if(mDataset.get(position).mAddress != null)
            holder.mAddressView.setText(mDataset.get(position).mAddress);

        //loadImages(mDataset.get(position).mPhotoURL, holder.mPictureView);
        Utils.setImage(myImageLoader, mDataset.get(position).mPhotoURL, holder.mPictureView, R.drawable.ic_person_black_48dp);

        if (position > myPreviousPosition)
        {
            if(Build.VERSION.SDK_INT >= 11)
            {
                AnimationUtils.animateSunblind(holder, true);
            }
        }
        else
        {
            if(Build.VERSION.SDK_INT >= 11)
            {
                AnimationUtils.animateSunblind(holder, false);
            }
        }
    }

    @Override
    public int getItemCount()
    {
        return mDataset.size();
    }


}
