package com.example.bkakran.nytimessearch.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.bkakran.nytimessearch.Fragment.DatePickerFragment;
import com.example.bkakran.nytimessearch.R;
import com.example.bkakran.nytimessearch.model.Request;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RequestActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy");
    EditText dateField;
    Spinner sortOrder;
    CheckBox cbArts;
    CheckBox cbFashion;
    CheckBox cbSports;

    private Request request;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dateField = (EditText) findViewById(R.id.fetDate);
        sortOrder = (Spinner) findViewById(R.id.fsvDropdown);
        cbArts = (CheckBox) findViewById(R.id.fcbArts);
        cbFashion = (CheckBox) findViewById(R.id.fcbFashion);
        cbSports = (CheckBox) findViewById(R.id.fcbSports);
        request = (Request) getIntent().getParcelableExtra("request");
        prepopulateScreen(request);
    }

    private void prepopulateScreen(Request request) {
        if (request.getBeginDate() != null)
            dateField.setText(request.getBeginDate());
        if (request.getSortOrder() != null)
            switch (request.getSortOrder()) {
                case "Oldest":
                    sortOrder.setSelection(0);
                case "Newest":
                    sortOrder.setSelection(1);
            }
        if (request.getNewsDesk() != null && request.getNewsDesk().size() == 3) {
            cbArts.setChecked(request.getNewsDesk().get(0));
            cbFashion.setChecked(request.getNewsDesk().get(1));
            cbSports.setChecked(request.getNewsDesk().get(2));

        }

    }

    // attach to an onclick handler to show the date picker
    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    // handle the date selected
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // store the values selected into a Calendar instance
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        dateField.setText(simpleDate.format(c.getTime()));
    }

    public void getDate(View view) {
        showDatePickerDialog(view);
    }

    public void save(View view) {
        Intent intent = new Intent();
        String dateString = dateField.getText().toString();
       /*
        if (TextUtils.isEmpty(dateString)) {
            Toast.makeText(this, "Select a date and proceed", Toast.LENGTH_LONG).show();
            return;
        }*/
        request.setSortOrder(sortOrder.getSelectedItem().toString());
        request.setBeginDate(dateString);
        populateNewsDeskVals(request);
        intent.putExtra("request", request);
        setResult(RESULT_OK, intent);
        // closes the activity and returns to first screen
        this.finish();
    }

    private void populateNewsDeskVals(Request request) {
        List<Boolean> newsdesk = new ArrayList<>();
        if (cbArts.isChecked()) {
            newsdesk.add(true);
        } else {
            newsdesk.add(false);
        }
        if (cbFashion.isChecked()) {
            newsdesk.add(true);
        } else {
            newsdesk.add(false);
        }
        if (cbSports.isChecked()) {
            newsdesk.add(true);
        } else {
            newsdesk.add(false);
        }
        request.setNewsDesk(newsdesk);
    }
}
