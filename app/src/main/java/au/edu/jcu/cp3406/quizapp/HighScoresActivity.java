package au.edu.jcu.cp3406.quizapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class HighScoresActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);

        SharedPreferences sharedPreferences = getSharedPreferences("high_scores", MODE_PRIVATE);

        for (int i = 1; i < 11; ++i) {
            ViewGroup highScoreTable = findViewById(R.id.high_score_table);
            getLayoutInflater().inflate(R.layout.high_score, highScoreTable);

            TableRow lastRow = (TableRow) highScoreTable.getChildAt(highScoreTable.getChildCount() - 1);

            TextView number = (TextView) lastRow.getChildAt(0);
            number.setText(String.valueOf(i));

            float percentageRecord = sharedPreferences.getFloat("percentage_record" + i, -1);
            int scoreRecord = sharedPreferences.getInt("score_record" + i, -1);
            int totalScoreRecord = sharedPreferences.getInt("total_score_record" + i, -1);
            float timeRecord = sharedPreferences.getFloat("time_record" + i, -1);

            TextView scoreDisplay = (TextView) lastRow.getChildAt(1);
            TextView timeDisplay = (TextView) lastRow.getChildAt(2);

            if (percentageRecord != -1) {
                scoreDisplay.setText(String.format(Locale.getDefault(), "%d/%d (%.2f%%)",
                        scoreRecord, totalScoreRecord, percentageRecord * 100));
                timeDisplay.setText(String.format(Locale.getDefault(), "%.2f", timeRecord));
            } else {
                scoreDisplay.setText("-");
                timeDisplay.setText("-");
            }
        }
    }
}