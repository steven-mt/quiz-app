package au.edu.jcu.cp3406.quizapp;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsActivity extends AppCompatActivity {

    static SharedPreferences themePreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        themePreferences = getSharedPreferences("config", MODE_PRIVATE);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            Preference.OnPreferenceChangeListener themeChangeListener = (preference, newValue) -> {
                String[] stringArray = getResources().getStringArray(R.array.theme_values);
                if (newValue.equals(stringArray[0]))
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                else if (newValue.equals(stringArray[1]))
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                else if (newValue.equals(stringArray[2]))
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                themePreferences.edit().putInt("default night mode",
                        AppCompatDelegate.getDefaultNightMode()).apply();
                return true;
            };
            ListPreference listPreference = findPreference("theme");
            if (listPreference != null) {
                listPreference.setOnPreferenceChangeListener(themeChangeListener);
            }

            Preference clearHighScores = findPreference("clear_high_scores");
            if (clearHighScores != null) {
                clearHighScores.setOnPreferenceClickListener(preference -> {
                    SharedPreferences highScorePreferences =
                            requireActivity().getSharedPreferences("high_scores", MODE_PRIVATE);
                    highScorePreferences.edit().clear().apply();
                    return true;
                });
            }
        }
    }

}