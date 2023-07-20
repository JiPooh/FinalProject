package algonquin.cst2335.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.buttFlight).setOnClickListener(clk->{
            Intent intent = new Intent(MainActivity.this, flightTracker.class);
            startActivity(intent);
        });
        findViewById(R.id.buttTriv).setOnClickListener(clk->{

        });
        findViewById(R.id.buttCurr).setOnClickListener(clk->{

        });
        findViewById(R.id.buttBear).setOnClickListener(clk->{

        });
    }
}