package podwozka.podwozka.Driver;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;


import podwozka.podwozka.Driver.entity.DriverTravel;
import podwozka.podwozka.Passenger.entity.PassangerTravel;
import podwozka.podwozka.R;

public class DriverBrowseWaitingPassengers extends AppCompatActivity {
    private List<PassangerTravel> travelList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TextView route, date;
    private DriverBrowseWaitingPassengersAdapter mAdapter;
    private static DriverTravel travel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_browse_waiting_passengers_list);

        Intent i = getIntent();
        travel = i.getParcelableExtra("TRAVEL");
        recyclerView = findViewById(R.id.recycler_view);
        date = findViewById(R.id.date);
        route = findViewById(R.id.route);

        mAdapter = new DriverBrowseWaitingPassengersAdapter(travelList, getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        String travelsFound = new DriverTravel().findMatchingPassengerTravels(travel);
        prepareTravelData(travelsFound);

        String routeString = travel.getStartPlace() + " - " + travel.getEndPlace();
        route.setText(routeString);

        String dateString = changeDateFormat(travel.getStartDatetime());
        date.setText(dateString);
    }

    @Override
    public void onBackPressed() {
        Intent nextScreen = new Intent(DriverBrowseWaitingPassengers.this, DriverTravelEditor.class);
        nextScreen.putExtra("TRAVEL", (Parcelable)travel);
        startActivity(nextScreen);
        finish();
    }

    private void prepareTravelData (String travelsJSON) {
        JSONParser parser = new JSONParser();
        try {
            JSONArray travelsObjects = (JSONArray)parser.parse(travelsJSON);

            for (Object obj : travelsObjects) {
                JSONObject jsonObj = (JSONObject) obj;
                travelList.add(new PassangerTravel(
                        (Long)jsonObj.get("id"),
                        (String)jsonObj.get("login"),
                        (String)jsonObj.get("firstName"),
                        (String)jsonObj.get("lastName"),
                        (String)jsonObj.get("startPlace"),
                        (String)jsonObj.get("endPlace"),
                        (String)jsonObj.get("startDatetime"),
                        null,
                        (Long)jsonObj.get("driverId")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        mAdapter.notifyDataSetChanged();
    }

    private String changeDateFormat(String strDate){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = dateFormat.parse(strDate);

            dateFormat = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
            return dateFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return strDate;
    }
}
