package com.example.theeatery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_aboutmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId())
        {
            case R.id.item3:
                //Toast.makeText(getApplicationContext(), "About", Toast.LENGTH_SHORT).show();
                Intent item3 = new Intent(this, MainActivity.class);
                startActivity(item3);
                //About
                break;
            default:
                //unknown error
        }
        return super.onOptionsItemSelected(item);
    }
}