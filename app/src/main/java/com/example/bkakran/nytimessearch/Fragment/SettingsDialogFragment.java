package com.example.bkakran.nytimessearch.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.bkakran.nytimessearch.R;

public class SettingsDialogFragment extends DialogFragment{

    EditText dateField;
    Spinner sortOrder;
    CheckBox cbArts;
    CheckBox cbFashion;
    CheckBox cbSports;
    Button save;

    public SettingsDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static SettingsDialogFragment newInstance(String title) {
        SettingsDialogFragment frag = new SettingsDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_name, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        dateField = (EditText) view.findViewById(R.id.fetDate);
        dateField = (EditText) view.findViewById(R.id.fetDate);
        sortOrder = (Spinner) view.findViewById(R.id.fsvDropdown);
        cbArts = (CheckBox) view.findViewById(R.id.fcbArts);
        cbFashion = (CheckBox) view.findViewById(R.id.fcbFashion);
        cbSports = (CheckBox) view.findViewById(R.id.fcbSports);
        save = (Button) view.findViewById(R.id.fbtSave);
        //request = (Request) .getIntent().getParcelableExtra("request");
        //prepopulateScreen(request);
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    }


}