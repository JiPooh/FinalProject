package algonquin.cst2335.finalproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TriviaRecyclerViewAdapter extends RecyclerView.Adapter<TriviaRecyclerViewAdapter.ViewHolder> {

    private JSONArray questionArray;

    public TriviaRecyclerViewAdapter(JSONArray questionArray) {
        this.questionArray = questionArray;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_trivia, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            JSONObject questionObj = questionArray.getJSONObject(position);
            String question = questionObj.getString("question");
            String correctAnswer = questionObj.getString("correct_answer");

            holder.questionTextView.setText(question);

            holder.submitButton.setOnClickListener(v -> {
                String userAnswer = holder.answerEditText.getText().toString().trim();
                if (userAnswer.equalsIgnoreCase(correctAnswer)) {
                    Toast.makeText(v.getContext(), "Correct!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(v.getContext(), "Wrong answer. Try again.", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return questionArray.length();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView questionTextView;
        EditText answerEditText;
        Button submitButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            questionTextView = itemView.findViewById(R.id.questionText);
            answerEditText = itemView.findViewById(R.id.answerText);
            submitButton = itemView.findViewById(R.id.testButton);
        }
    }
}
