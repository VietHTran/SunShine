package com.example.android.sunshine.app;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.sunshine.app.data.WeatherContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private ShareActionProvider mShareActionProvider;
    static final String[] DETAIL_COLUMNS = {
            WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
            WeatherContract.WeatherEntry.COLUMN_DEGREES,
            WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
            WeatherContract.WeatherEntry.COLUMN_PRESSURE,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID
    };
    private static final int DETAIL_LOADER = 0;
    private static final int COL_WEATHER_ID = 0;
    private static final int COL_WEATHER_DATE = 1;
    private static final int COL_WEATHER_DESC = 2;
    private static final int COL_WEATHER_MAX_TEMP = 3;
    private static final int COL_WEATHER_MIN_TEMP = 4;
    private static final int COL_WEATHER_WIND_SPEED = 5;
    private static final int COL_WEATHER_DEGREES = 6;
    private static final int COL_WEATHER_HUMIDITY = 7;
    private static final int COL_WEATHER_PRESSURE = 8;
    private static final int COL_WEATHER_WEATHER_ID = 9;
    static final String DETAIL_URI = "URI";
    private static final String LOG_TAG = DetailFragment.class.getSimpleName();


    private final String HASHTAGSUNSHINE="#SunshineApp";
    private String mForecastStr;
    private Uri mUri;
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

    void onLocationChanged( String newLocation ) {
        // replace the uri, since the location has changed
        Uri uri = mUri;
        if (null != uri) {
            long date = WeatherContract.WeatherEntry.getDateFromUri(uri);
            Uri updatedUri = WeatherContract.WeatherEntry.buildWeatherLocationWithDate(newLocation, date);
            mUri = updatedUri;
            getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
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
        Bundle bundle= getArguments();
        if (bundle!=null) {
            mUri=bundle.getParcelable(DETAIL_URI);
        }

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
       if (mUri!=null) {
           // Now create and return a CursorLoader that will take care of
           // creating a Cursor for the data being displayed.
           return new CursorLoader(
                   getActivity(),
                   mUri,
                   DETAIL_COLUMNS,
                   null,
                   null,
                   null
           );
       }
        return null;
    }
    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        if (!cursor.moveToFirst()) {
            return;
        }
        boolean isMetric=Utility.isMetric(getActivity());
        String friendlyDate=Utility.getFriendlyDayString(getActivity(),cursor.getLong(COL_WEATHER_DATE));
        String date=Utility.formatDate(cursor.getLong(COL_WEATHER_DATE));
        String description=cursor.getString(COL_WEATHER_DESC);
        String maxTemp=Utility.formatTemperature(getActivity(),cursor.getDouble(COL_WEATHER_MAX_TEMP),isMetric);
        String minTemp=Utility.formatTemperature(getActivity(),cursor.getDouble(COL_WEATHER_MIN_TEMP),isMetric);
        String wind=Utility.getFormattedWind(getActivity(),cursor.getFloat(COL_WEATHER_WIND_SPEED),cursor.getFloat(COL_WEATHER_DEGREES));
        float pressure=cursor.getFloat(COL_WEATHER_PRESSURE);
        float humidity=cursor.getFloat(COL_WEATHER_HUMIDITY);
        String pressureStr=getActivity().getString(R.string.format_pressure,pressure);
        String humidityStr=getActivity().getString(R.string.format_humidity,humidity);
        int weatherID=cursor.getInt(COL_WEATHER_WEATHER_ID);
        int imageID=Utility.getArtResourceForWeatherCondition(weatherID);

        TextView friendlyDateView=(TextView)getView().findViewById(R.id.list_item_friendly_date_textview);
        TextView dateView=(TextView)getView().findViewById(R.id.list_item_date_textview);
        TextView descriptionView=(TextView)getView().findViewById(R.id.list_item_forecast_textview);
        TextView maxTempView=(TextView)getView().findViewById(R.id.list_item_high_textview);
        TextView minTempView=(TextView)getView().findViewById(R.id.list_item_low_textview);
        TextView windView=(TextView)getView().findViewById(R.id.list_item_wind_textview);
        TextView humidityView=(TextView)getView().findViewById(R.id.list_item_humidity_textview);
        TextView pressureView=(TextView)getView().findViewById(R.id.list_item_pressure_textview);
        ImageView iconView=(ImageView)getView().findViewById(R.id.list_item_icon);

        friendlyDateView.setText(friendlyDate);
        dateView.setText(date);
        descriptionView.setText(description);
        maxTempView.setText(maxTemp);
        minTempView.setText(minTemp);
        windView.setText(wind);
        humidityView.setText(humidityStr);
        pressureView.setText(pressureStr);
        iconView.setImageResource(imageID);

        iconView.setContentDescription(description);

        mForecastStr= String.format("%s - %s - %s/%s", date, description, maxTemp, minTemp);

        setShareIntent();
    }
}
