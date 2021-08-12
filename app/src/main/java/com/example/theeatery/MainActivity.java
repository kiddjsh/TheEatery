package com.example.theeatery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity2();
            }

        });
    }
    public void openMainActivity2(){
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId())
        {
            case R.id.item1:
                Toast.makeText(getApplicationContext(), "About", Toast.LENGTH_SHORT).show();
                Intent item1 = new Intent(this, AboutActivity.class);
                startActivity(item1);
                //About
                break;
            case R.id.item4:
                Toast.makeText(getApplicationContext(), "Exit", Toast.LENGTH_SHORT).show();
                Intent item4 = new Intent(this, MainActivity.class);
                finish();
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