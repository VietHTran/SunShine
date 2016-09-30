package com.example.android.sunshine.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v4.view.MenuItemCompat;

public class DetailActivity extends ActionBarActivity {

    private ShareActionProvider mShareActionProvider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        //MenuItem item= menu.findItem(R.id.detail_share);
        //mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        return true;
    }

    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider!=null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent= new Intent(this,SettingsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_share) {
            Intent intent= new Intent(this,DetailActivity.class);
            intent.putExtra("Share",getIntent().getStringExtra(Intent.EXTRA_TEXT));
            setShareIntent(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";
        private String mForecastStr;

        public PlaceholderFragment() {
            setHasOptionsMenu(true);
        }

        @Override
        public void onCreate(Bundle saveInstanceState) {
            setHasOptionsMenu(true);
            super.onCreate(saveInstanceState);
        }

        @Override
        public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.detailfragment, menu);
            super.onCreateOptionsMenu(menu, inflater);
            MenuItem menuItem = menu.findItem(R.id.action_share);
            ShareActionProvider mShareActionProvider =(ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
            if (mShareActionProvider!=null) {
                mShareActionProvider.setShareIntent(createShareForecastIntent());
            }
        }

        private Intent createShareForecastIntent() {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT,
                    mForecastStr + FORECAST_SHARE_HASHTAG);
                    return shareIntent;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id= item.getItemId();
            if (id==R.id.action_settings) {
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            Intent intent= getActivity().getIntent();
            if (intent!=null && intent.hasExtra(Intent.EXTRA_TEXT)) {
                String data=intent.getStringExtra(Intent.EXTRA_TEXT);
                ((TextView)rootView.findViewById(R.id.string_data1)).setText(data);
            }
            return rootView;
        }
    }
}