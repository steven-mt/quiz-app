package au.edu.jcu.cp3406.quizapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

public class GameActivity extends AppCompatActivity {

    private static final int MIN_NUMBER = 10;
    private static final int MAX_NUMBER = 20;

    private static final List<Object> answers = new ArrayList<>();
    private static int totalScore;
    private long startTime;
    private boolean isDone;  // Flag for if the Done button has been pressed once.

    private TextView resultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        resultView = findViewById(R.id.result);
        totalScore = 0;

        Button startButton = findViewById(R.id.done_button);
        startButton.setOnClickListener(view -> {
            if (!isDone) {
                long stopTime = System.nanoTime();
                long duration = stopTime - startTime;  // nanoseconds
                float seconds = duration / 1e9f;

                isDone = true;
                int score = getScore();

                resultView.setText(getString(R.string.result_text, score, totalScore, seconds));

                saveResultsToHighScores(score, totalScore, seconds);
            }
        });

        addAdditionSubtractionQuestion();
        addMultiplicationQuestion();
        addDivisionQuestion();
        addAstronomyQuestion();
        addChemistryQuestion();

        startTime = System.nanoTime();
    }

    private void addAdditionSubtractionQuestion() {
        int firstNumber = ThreadLocalRandom.current().nextInt(MIN_NUMBER, MAX_NUMBER + 1);
        int secondNumber = ThreadLocalRandom.current().nextInt(MIN_NUMBER, MAX_NUMBER + 1);
        int answer;
        char operation;
        if (ThreadLocalRandom.current().nextInt(0, 2) == 0) {
            operation = '+';
            answer = firstNumber + secondNumber;
        } else {
            operation = '-';
            answer = firstNumber - secondNumber;
        }
        answers.add(answer);
        ++totalScore;

        ViewGroup gameRows = findViewById(R.id.game_rows);
        getLayoutInflater().inflate(R.layout.question, gameRows);

        View lastRow = gameRows.getChildAt(gameRows.getChildCount() - 1);
        TableRow tableRow = lastRow.findViewById(R.id.question);

        TextView textView = (TextView) tableRow.getChildAt(0);
        textView.setText(getString(R.string.number_question, firstNumber, operation, secondNumber));

        EditText editText = (EditText) tableRow.getChildAt(1);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
    }

    private void addMultiplicationQuestion() {
        int firstNumber = ThreadLocalRandom.current().nextInt(MIN_NUMBER, MAX_NUMBER + 1);
        int secondNumber = ThreadLocalRandom.current().nextInt(MIN_NUMBER, MAX_NUMBER + 1);
        char operation = '*';
        int answer = firstNumber * secondNumber;
        answers.add(answer);
        ++totalScore;

        ViewGroup gameRows = findViewById(R.id.game_rows);
        getLayoutInflater().inflate(R.layout.question, gameRows);

        View lastRow = gameRows.getChildAt(gameRows.getChildCount() - 1);
        TableRow tableRow = lastRow.findViewById(R.id.question);

        TextView textView = (TextView) tableRow.getChildAt(0);
        textView.setText(getString(R.string.number_question, firstNumber, operation, secondNumber));

        EditText editText = (EditText) tableRow.getChildAt(1);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    private void addDivisionQuestion() {
        int smallerNumber = ThreadLocalRandom.current().nextInt(MIN_NUMBER, MAX_NUMBER + 1);
        int answer = ThreadLocalRandom.current().nextInt(2, 11);
        int largerNumber = smallerNumber * answer;
        char operation = '/';
        answers.add(answer);
        ++totalScore;

        ViewGroup gameRows = findViewById(R.id.game_rows);
        getLayoutInflater().inflate(R.layout.question, gameRows);

        View lastRow = gameRows.getChildAt(gameRows.getChildCount() - 1);
        TableRow tableRow = lastRow.findViewById(R.id.question);

        TextView textView = (TextView) tableRow.getChildAt(0);
        textView.setText(getString(R.string.number_question, largerNumber, operation, smallerNumber));

        EditText editText = (EditText) tableRow.getChildAt(1);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    private void addAstronomyQuestion() {
        int position = ThreadLocalRandom.current().nextInt(1, 9);
        String suffix = "th";
        if (position <= 3) {
            switch (position) {
                case 1:
                    suffix = "st";
                    break;
                case 2:
                    suffix = "nd";
                    break;
                case 3:
                    suffix = "rd";
                    break;
            }
        }

        String[] names = getResources().getStringArray(R.array.planets);
        String answer = names[position - 1];
        answers.add(answer);
        ++totalScore;

        ViewGroup gameRows = findViewById(R.id.game_rows);
        getLayoutInflater().inflate(R.layout.question, gameRows);

        View lastRow = gameRows.getChildAt(gameRows.getChildCount() - 1);
        TableRow tableRow = lastRow.findViewById(R.id.question);

        TextView textView = (TextView) tableRow.getChildAt(0);
        textView.setText(getString(R.string.astronomy_question, position + suffix));

        EditText editText = (EditText) tableRow.getChildAt(1);
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
    }

    private void addChemistryQuestion() {
        int atomicNumber = ThreadLocalRandom.current().nextInt(1, 18);
        String[] names = getResources().getStringArray(R.array.element_names);
        String answer = names[atomicNumber - 1];
        answers.add(answer);
        ++totalScore;

        String[] symbols = getResources().getStringArray(R.array.element_symbols);
        String symbol = symbols[atomicNumber - 1];

        ViewGroup gameRows = findViewById(R.id.game_rows);
        getLayoutInflater().inflate(R.layout.question, gameRows);

        View lastRow = gameRows.getChildAt(gameRows.getChildCount() - 1);
        TableRow tableRow = lastRow.findViewById(R.id.question);

        TextView textView = (TextView) tableRow.getChildAt(0);
        textView.setText(getString(R.string.chemistry_question, symbol));

        EditText editText = (EditText) tableRow.getChildAt(1);
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
    }

    private int getScore() {
        int score = 0;
        ViewGroup gameRows = findViewById(R.id.game_rows);

        for (int i = 0; i < gameRows.getChildCount(); ++i) {
            TableRow currentRow = (TableRow) gameRows.getChildAt(i);
            EditText editText = (EditText) currentRow.getChildAt(1);

            String userAnswer = editText.getText().toString().toLowerCase(Locale.ROOT);
            String correctAnswer = answers.get(i).toString().toLowerCase(Locale.ROOT);
            if (userAnswer.equals(correctAnswer))
                ++score;
        }
        return score;
    }

    private void saveResultsToHighScores(int newScore, int newTotalScore, float newTime) {
        SharedPreferences sharedPreferences = getSharedPreferences("high_scores", MODE_PRIVATE);

        for (int i = 1; i < 11; ++i) {
            float percentage = (float) newScore / newTotalScore;

            float percentageRecord = sharedPreferences.getFloat("percentage_record" + i, -1);
            int scoreRecord = sharedPreferences.getInt("score_record" + i, -1);
            int totalScoreRecord = sharedPreferences.getInt("total_score_record" + i, -1);
            float timeRecord = sharedPreferences.getFloat("time_record" + i, -1);

            if ((percentage > percentageRecord)) {
                sharedPreferences.edit().putFloat("percentage_record" + i, percentage).apply();
                sharedPreferences.edit().putInt("score_record" + i, newScore).apply();
                sharedPreferences.edit().putInt("total_score_record" + i, newTotalScore).apply();
                sharedPreferences.edit().putFloat("time_record" + i, newTime).apply();

                if (percentageRecord == -1)
                    // If the previous record was empty, exit out of the loop.
                    break;
                else {
                    /* Otherwise, assign the old variables to the new variables so that they can be
                     * processed in the next iteration. */
                    newScore = scoreRecord;
                    newTotalScore = totalScoreRecord;
                    newTime = timeRecord;
                }
            } else if (percentage == percentageRecord && newTime < timeRecord) {
                // if the percentage is the same but there is a better time

                if (newScore != scoreRecord) {
                    /* If the score is different, this means that the score and total score are
                     * different but make the same percentage. */
                    sharedPreferences.edit().putFloat("score_record" + i, newScore).apply();
                    sharedPreferences.edit().putFloat("total_score_record" + i, newTotalScore).apply();
                }
                sharedPreferences.edit().putFloat("time_record" + i, newTime).apply();

                if (percentageRecord == -1) break;
                else {
                    newScore = scoreRecord;
                    newTotalScore = totalScoreRecord;
                    newTime = timeRecord;
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        isDone = false;
        answers.clear();

        super.onDestroy();
    }
}