package algonquin.cst2335.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import algonquin.cst2335.finalproject.bear.Bear;
import algonquin.cst2335.finalproject.databinding.ActivityBearBinding;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.buttFlight).setOnClickListener(clk->{

        });
        findViewById(R.id.buttTriv).setOnClickListener(clk->{

        });
        findViewById(R.id.buttCurr).setOnClickListener(clk->{

        });
        findViewById(R.id.buttBear).setOnClickListener(clk->{
            Intent bearInt = new Intent(MainActivity.this, Bear.class);
            startActivity(bearInt);
        });
    }
}