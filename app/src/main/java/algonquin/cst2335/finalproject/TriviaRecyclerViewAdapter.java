package algonquin.cst2335.finalproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_questions, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            JSONObject questionObj = questionArray.getJSONObject(position);
            String question = questionObj.getString("question");
            String correctAnswer = questionObj.getString("correct_answer");

            // Assuming you have a "incorrect_answers" JSONArray in your JSON
            JSONArray incorrectAnswers = questionObj.getJSONArray("incorrect_answers");

            // Set the question to the TextView
            holder.questionTextView.setText(question);

            // Set the choices to the RadioButtons
            holder.choice1.setText(incorrectAnswers.getString(0));
            holder.choice2.setText(incorrectAnswers.getString(1));
            holder.choice3.setText(incorrectAnswers.getString(2));
            holder.choice4.setText(correctAnswer); // This is a simple way, you might want to randomize the placement

            holder.submitButton.setOnClickListener(v -> {
                int selectedId = holder.radioGroup.getCheckedRadioButtonId();

                RadioButton selectedRadioButton = holder.itemView.findViewById(selectedId);
                String userAnswer = selectedRadioButton.getText().toString();

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
        RadioGroup radioGroup;
        RadioButton choice1, choice2, choice3, choice4;
        Button submitButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            questionTextView = itemView.findViewById(R.id.questionTextView);
            radioGroup = itemView.findViewById(R.id.radioGroup);
            choice1 = itemView.findViewById(R.id.choice1);
            choice2 = itemView.findViewById(R.id.choice2);
            choice3 = itemView.findViewById(R.id.choice3);
            choice4 = itemView.findViewById(R.id.choice4);
            submitButton = itemView.findViewById(R.id.submitButton);
        }
    }
}
