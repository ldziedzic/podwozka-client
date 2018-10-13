package podwozka.podwozka.Driver;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import podwozka.podwozka.Driver.entity.DriverTravel;
import podwozka.podwozka.PopUpWindows;
import podwozka.podwozka.R;


public class DriverNewTravelActivity extends AppCompatActivity {

    private static TextView pickedTime;
    private static TextView pickedDate;

    // Date dialog
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }


        public void onDateSet(DatePicker view, int year, int month, int day) {
            pickedDate.setText(new StringBuilder().append(day).append("-")
                    .append(month).append("-").append(year));
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }


    // Time dialog
    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            pickedTime.setText(new StringBuilder().append(hourOfDay).append(":")
                    .append(minute));

        }
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_new_travel);
        Button btnNextScreen = (Button) findViewById(R.id.submitNewTravelButton);
        Button reversePlacesButton = (Button) findViewById(R.id.reverseTravelPlaces);
        pickedDate = (TextView) findViewById(R.id.pickedDate);
        pickedTime = (TextView) findViewById(R.id.pickedTime);
        final NumberPicker np = (NumberPicker) findViewById(R.id.passengerCount);

        //Set the minimum value of NumberPicker
        np.setMinValue(0);
        //Specify the maximum value/number of NumberPicker
        np.setMaxValue(10);

        //Set a value change listener for NumberPicker
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                //Display the newly selected number from picker
            }
        });

        btnNextScreen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                PopUpWindows alertWindow = new PopUpWindows();
                boolean noErrors = true;
                int httpResponse;

                //Starting a new Intent
                Intent nextScreen = new Intent(getApplicationContext(), DriverNewTravelActivity.class);

                EditText startTravelPlace = (EditText) findViewById(R.id.startTravelPlace);
                String startTravelPlaceMessage = startTravelPlace.getText().toString();

                EditText endTravelPlace = (EditText) findViewById(R.id.endTravelPlace);
                String endTravelPlaceMessage = endTravelPlace.getText().toString();

                TextView pickUpDate = (TextView) findViewById(R.id.pickedDate);
                String pickUpDateMessage = pickUpDate.getText().toString();

                TextView pickUpTime = (TextView) findViewById(R.id.pickUpTime);
                String pickUpTimeMessage = pickUpTime.getText().toString();

                if (startTravelPlaceMessage.isEmpty())
                {
                    alertWindow.showAlertWindow(DriverNewTravelActivity.this, null, getResources().getString(R.string.start_place_empty));
                    noErrors = false;
                }
                else if (endTravelPlaceMessage.isEmpty())
                {
                    alertWindow.showAlertWindow(DriverNewTravelActivity.this, null, getResources().getString(R.string.end_place_empty));
                    noErrors = false;
                }

                if(noErrors == true) {
                DriverTravel newTravel = new DriverTravel("user", startTravelPlaceMessage, endTravelPlaceMessage, (pickUpDateMessage+"T"+pickUpTimeMessage), Integer.toString(np.getValue()));
                    httpResponse = newTravel.postNewTravel(newTravel);
                    if(httpResponse == 201) {
                        startActivity(nextScreen);
                    }
                }

            }
        });

        reversePlacesButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                //Starting a new Inten
                EditText startTravelPlace = (EditText) findViewById(R.id.startTravelPlace);
                String startTravelPlaceMessage = startTravelPlace.getText().toString();

                EditText endTravelPlace = (EditText) findViewById(R.id.endTravelPlace);
                String endTravelPlaceMessage = endTravelPlace.getText().toString();

                startTravelPlace.setText(endTravelPlaceMessage);
                endTravelPlace.setText(startTravelPlaceMessage);

            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent nextScreen = new Intent(DriverNewTravelActivity.this, DriverMainActivity.class);
        startActivity(nextScreen);
        finish();
    }
}
