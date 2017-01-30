package com.y2.y2qadmin;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.y2.y2qadmin.ServerInterface.ServerIPProvider;
import com.y2.y2qadmin.misc.PermissionChecker;
import com.y2.y2qadmin.misc.VolleySingleton;
import com.y2.y2qadmin.model.DeviceIdentity;
import com.y2.y2qadmin.model.Queue;
import com.y2.y2qadmin.model.QueueSlot;

public class QueueSlotListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{

    ServerIPProvider myServerIPProvider = new ServerIPProvider();
    QueueSlotListActivityFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        setContentView(R.layout.activity_queue_slot_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();*/

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initActivity();

    }

    private void initActivity()
    {
        VolleySingleton.getInstance(this);  //  Just initialize the singleton
        DeviceIdentity.initialize(this);
        myServerIPProvider.create();

        Intent intent = getIntent();
        Queue queue = (Queue)intent.getSerializableExtra(Queue.QUEUE_OBJ);

        mFragment = (QueueSlotListActivityFragment)getFragmentManager().findFragmentByTag("QueueSlotListActivityFragment");
        mFragment.initialize(queue);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        switch (requestCode)
        {
            case PermissionChecker.RequestCode:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    initActivity();
                }
                else
                {
                    Toast.makeText(QueueSlotListActivity.this, "Permission denied to access the internet. The app doesn't work", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onResume()
    {
        if(mFragment != null)
        {
            //overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
            mFragment.refresh();
        }

        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home)
        {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
