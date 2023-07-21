package algonquin.cst2335.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class TriviaActivity extends AppCompatActivity {






    AlertDialog.Builder builder = new AlertDialog.Builder( TriviaActivity.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_trivia);
        Button test = findViewById(R.id.testButton) ;
        RecyclerView triviaList = findViewById(R.id.triviaList);
        Toast.makeText(this,"Welecome to trivia game", Toast.LENGTH_LONG).show();

        findViewById(R.id.backButton).setOnClickListener(clk->{
            Intent MainIntent = new Intent(TriviaActivity.this , MainActivity.class);

            startActivity(MainIntent) ;

        });
        findViewById(R.id.randomButton).setOnClickListener(clk->{

        });


       test.setOnClickListener(clk -> {
            builder.setMessage("Do you want to choose this topic?")
                    .setTitle("Topic")
                    .setNegativeButton("No", (dialog, which) -> {})
                    .setPositiveButton("Yes", (dialog, which) -> {
                        View rootView = findViewById(R.id.testView);
                        Snackbar.make(rootView , "You are playing the sport topic", Snackbar.LENGTH_LONG)
                                .setAction("Undo", clk2 -> {

                                }).show();
                    }).create().show();
        });

//        triviaList.setAdapter(new RecyclerView.Adapter<MyRowHolder>() {
//            @NonNull
//            @Override
//            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                return null;
//            }
//
//            @Override
//            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
//
//            }
//
//            @Override
//            public int getItemCount() {
//                return 0;
//            }
//        });




    }


}