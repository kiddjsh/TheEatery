package com.example.theeatery;

// The Activity Base class

// Used for saving state information
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

//Android UI Classes
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Button;

// Android UI event listener
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.view.View.OnClickListener;
import android.widget.Toast;

// Number formatting
import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;
import java.util.ArrayList;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class MainActivity2 extends AppCompatActivity {

    private static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    private static final NumberFormat percentFormat = NumberFormat.getPercentInstance();
    private static final int ROUND_NONE = 0;

    // define variables for the widgets
    private TextView amountTextView;
    private StringBuilder userAmountInput = new StringBuilder();
    private TextView percentTextView;
    private TextView tipTextView;
    private TextView totalTextView;
    private ViewGroup mainView;
    private ArrayList<Button> buttons = new ArrayList<Button>();

    // define instance variables that should be saved
    private double billAmount = 0.0;
    private double percent = .15f;

    // set up preferences
    private SharedPreferences prefs;
    private boolean rememberTipPercent = true;
    //private int rounding = ROUND_NONE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mainView = findViewById(R.id.mainLayout);

        //Get the GUI text widgets
        amountTextView = findViewById(R.id.amountTextView);
        amountTextView.setText("Enter Amount");
        percentTextView = findViewById(R.id.percentTextView);
        tipTextView = findViewById(R.id.tipTextView);
        totalTextView = findViewById(R.id.totalTextView);
        tipTextView.setText(currencyFormat.format(0));
        totalTextView.setText(currencyFormat.format(0));

        SeekBar percentSeekBar = findViewById(R.id.percentSeekBar);
        percentSeekBar.setOnSeekBarChangeListener(seekBarListener);

        for (int i = 0; i < mainView.getChildCount(); i++) {
            if (mainView.getChildAt(i) instanceof Button) {
                buttons.add((Button) mainView.getChildAt(i));
            }
        }

        for (Button button : buttons) {
            button.setOnClickListener(buttonPressed);
        }

        // set the default values for the preferences
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        // get default SharedPreferences object
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
    }

    private void calculateBill() {
        percentTextView.setText(percentFormat.format(percent));

        double tip = billAmount * percent;
        double total = billAmount + tip;

        tipTextView.setText(currencyFormat.format(tip).toString());
        totalTextView.setText(currencyFormat.format(total).toString());
    }

    private void resetBill() {
        tipTextView.setText(currencyFormat.format(0));
        totalTextView.setText(currencyFormat.format(0));
    }

    private void updateInputView(String amount) {
        try {
            userAmountInput.append(amount);
            if (userAmountInput.toString().matches("(\\d*)")) {
                amountTextView.setText(userAmountInput.toString());
                billAmount = Double.parseDouble(userAmountInput.toString());
                calculateBill();
            } else if (userAmountInput.toString().matches("(\\d*)(\\.?)")) {
                amountTextView.setText(userAmountInput.toString());
                billAmount = Double.parseDouble(userAmountInput.toString().replace(".", "").trim());
                calculateBill();
            } else if (userAmountInput.toString().matches("(\\d*)(\\.?)(\\d{0,2})")) {
                amountTextView.setText(userAmountInput.toString());
                billAmount = Double.parseDouble(userAmountInput.toString());
                calculateBill();
            } else {
                Toast.makeText(getApplicationContext(), "Invalid Amount", Toast.LENGTH_SHORT).show();
                deleteInputView();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteInputView() {
        userAmountInput.deleteCharAt(userAmountInput.length() - 1);
        amountTextView.setText(userAmountInput.toString());
        if (userAmountInput.toString().matches("(\\d*)(\\.?)(\\d{0,2})")) {
            billAmount = Double.parseDouble(userAmountInput.toString());
            calculateBill();
        }
    }

    private void clearInputView() {
        userAmountInput.delete(0, userAmountInput.length());
        amountTextView.setText("Enter Amount");
        resetBill();
    }

    private final OnClickListener buttonPressed = new OnClickListener() {
        @Override
        public void onClick(View view) {
            Button b = (Button) view;
            if (b.getText().toString().equalsIgnoreCase("DEL")) {
                if (userAmountInput.length() > 1) {
                    deleteInputView();
                } else {
                    clearInputView();
                }
            } else if (b.getText().toString().equalsIgnoreCase("CLR")) {
                clearInputView();
            } else {
                updateInputView(b.getText().toString());
            }
        }
    };

    private final OnSeekBarChangeListener seekBarListener = new OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
            percent = progress / 100.0;
            calculateBill();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_menu2, menu);
        return true;
    }

    @Override
    public void onPause() {
        // save the instance variables
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("billAmountString", String.valueOf(billAmount));
        editor.putFloat("percent", (float) percent);
        editor.commit();

        super.onPause();
    }

    public void onResume() {
        super.onResume();

        // get preferences
        //rememberTipPercent = prefs.getBoolean("pref_forget_percent", true);
        rememberTipPercent = prefs.getBoolean("pref_remember_percent", true);

        // get the instance variables
        //billAmount = Double.parseDouble(prefs.getString("billAmount", "1.0"));
        String amountFound = prefs.getString("billAmount", "Not Found");
        if( amountFound.equals("Not Found")){
            //handle not finding amount
        } else {
            billAmount = Double.parseDouble(amountFound);
        }
        if (rememberTipPercent) {
            percent = prefs.getFloat("tipPercent", 0.15f);
        } else {
            percent = 0.15f;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId())
        {
            case R.id.item2:
                Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                Intent item2 = new Intent(this, SettingsActivity.class);
                startActivity(item2);
                //Settings
                break;
            case R.id.item4:
                Toast.makeText(getApplicationContext(), "Exit", Toast.LENGTH_SHORT).show();
                Intent item4 = new Intent(this,  MainActivity2.class);
                //finish();
                int status = 0;
                System.exit( status);
                //Exit
                break;
            default:
                //unknown error
        }
        return super.onOptionsItemSelected(item);
    }
}
