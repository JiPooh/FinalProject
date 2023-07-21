package algonquin.cst2335.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class TriviaActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_trivia);


        Button test = findViewById(R.id.testButton) ;
       RecyclerView triviaList = findViewById(R.id.triviaList);

        Toast.makeText(this,"Welecome to trivia game", Toast.LENGTH_LONG).show();

//        findViewById(R.id.backButton).setOnClickListener(clk->{
//            Intent MainIntent = new Intent(TriviaActivity.this , MainActivity.class);
//
//            startActivity(MainIntent) ;
//
//        });

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showAlertDialog();
                showSnackbar(v);
            }
        });





    }
    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert Dialog")
                .setMessage("This is an AlertDialog!")
                .setPositiveButton("OK", null)
                .show();
    }

    private void showSnackbar(View view) {
        Snackbar.make(view, "This is a Snackbar!", Snackbar.LENGTH_SHORT).show();
    }
}