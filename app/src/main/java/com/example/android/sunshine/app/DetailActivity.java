/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.sunshine.app;

import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.sunshine.app.data.WeatherContract;

import java.net.URI;

public class DetailActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

        private ShareActionProvider mShareActionProvider;
        static final String[] FORECAST_COLUMNS = {
                WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
                WeatherContract.WeatherEntry.COLUMN_DATE,
                WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
                WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
                WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
        };
        private static final int DETAIL_LOADER = 0;
        private static final int COL_WEATHER_ID = 0;
        private static final int COL_WEATHER_DATE = 1;
        private static final int COL_WEATHER_DESC = 2;
        private static final int COL_WEATHER_MAX_TEMP = 3;
        private static final int COL_WEATHER_MIN_TEMP = 4;

        private final String HASHTAGSUNSHINE="#SunshineApp";
        private String mForecastStr;
        public DetailFragment() {
            setHasOptionsMenu(true);
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            // Inflate the menu; this adds items to the action bar if it is present.
            super.onCreateOptionsMenu(menu,inflater);
            inflater.inflate(R.menu.detailfragment,menu);
            mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menu.findItem(R.id.action_share));
            setShareIntent();
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id=item.getItemId();
            if (id==R.id.action_share) {

            }
            return true;
        }

        private void setShareIntent() {
            if (mShareActionProvider != null && mForecastStr!=null) {
                Intent intent= new Intent(android.content.Intent.ACTION_SEND);
                //Prevent activity sharing to from being placed on the activity stack
                //=> If jump out during share --> Resume --> Switch to this activity
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                intent.putExtra(Intent.EXTRA_TEXT,mForecastStr+" "+HASHTAGSUNSHINE);
                //Let android know you only share text
                intent.setType("text/plain");
                mShareActionProvider.setShareIntent(intent);
            }
        }

        private String getExtraTextFromMain() {
            Intent intent = getActivity().getIntent();
            if (intent != null) {
                return intent.getDataString();
            }
            return  "";
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            return rootView;
        }
        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            getLoaderManager().initLoader(DETAIL_LOADER, null,this);
            super.onActivityCreated(savedInstanceState);
        }
        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            Intent intent = getActivity().getIntent();
            if (intent == null) {
                return null;
            }
            return new CursorLoader(getActivity(),intent.getData(),FORECAST_COLUMNS,null,null,null);
        }
        @Override
        public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
            if (!cursor.moveToFirst()) {
                return;
            }
            boolean isMetric=Utility.isMetric(getActivity());
            String date=Utility.formatDate(cursor.getLong(COL_WEATHER_DATE));
            String description=cursor.getString(COL_WEATHER_DESC);
            String maxTemp=Utility.formatTemperature(cursor.getDouble(COL_WEATHER_MAX_TEMP),isMetric);
            String minTemp=Utility.formatTemperature(cursor.getDouble(COL_WEATHER_MIN_TEMP),isMetric);
            mForecastStr=date+" - "+description+" - "+maxTemp+"/"+minTemp;
            TextView textView=(TextView)getView().findViewById(R.id.detail_text);
            textView.setText(mForecastStr);
            setShareIntent();
        }
    }
}
