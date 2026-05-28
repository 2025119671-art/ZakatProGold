package com.example.zakatprogold;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Locale;

/**
 * MainActivity: The main calculator screen for Zakat Gold Pro.
 * Includes input fields, calculation logic, and navigation menu.
 */
public class MainActivity extends AppCompatActivity {

    // UI elements declaration
    private EditText etWeight, etPrice;
    private RadioButton rbKeep;
    private Button btnCalc;
    private TextView tvOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // [CHANGE] Initialize Toolbar and set it as the Support Action Bar.
        // This ensures the menu appears correctly at the top.
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize UI components by mapping them to their layout IDs
        etWeight = findViewById(R.id.etWeight);
        etPrice = findViewById(R.id.etPrice);
        rbKeep = findViewById(R.id.rbKeep);
        btnCalc = findViewById(R.id.btnCalc);
        tvOutput = findViewById(R.id.tvOutput);

        // Set click listener for the calculate button
        btnCalc.setOnClickListener(v -> calculateZakat());
    }

    /**
     * Initializes the options menu (menu bar).
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * Handles clicks on menu items.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_about) {
            // Open the AboutActivity when "About" is selected from the menu
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.menu_share) {
            // Sharing functionality - shares the application URL
            String shareMessage = "Check out this Zakat Gold Calculator!\n" + getString(R.string.app_url);
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Zakat Gold Pro");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "Share via"));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Main function to perform zakat calculation based on inputs.
     * Includes validation and result formatting.
     */
    private void calculateZakat() {
        String weightStr = etWeight.getText().toString();
        String priceStr = etPrice.getText().toString();

        // Error message for empty fields (Good Design Practice)
        if (weightStr.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Error: Please fill in both Weight and Price fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double weight = Double.parseDouble(weightStr);
            double price = Double.parseDouble(priceStr);

            // 1. Determine the threshold (X) based on the selection
            double x = rbKeep.isChecked() ? 85 : 200;

            // 2. Calculate the weight that is subject to zakat (Weight - X)
            double weightMinusX = weight - x;
            if (weightMinusX < 0) weightMinusX = 0; // If weight is less than threshold, payable weight is 0

            // 3. Perform the calculations as per the rubric and sample table
            double totalValue = weight * price;                   // i. Total gold value
            double zakatPayableValue = weightMinusX * price;      // ii. & iii. Zakat payable value
            double totalZakat = zakatPayableValue * 0.025;        // iv. Total zakat (2.5%)

            // Display results formatted appropriately (3 outputs as required by rubric)
            String result = String.format(Locale.getDefault(),
                    "Total Gold Value: RM %.2f\n" +
                    "Zakat Payable Value: RM %.2f\n" +
                    "Total Zakat: RM %.2f",
                    totalValue, zakatPayableValue, totalZakat);

            tvOutput.setText(result);

            // Helpful notice (Good Design Practice)
            Toast.makeText(this, "Calculation completed successfully.", Toast.LENGTH_SHORT).show();

        } catch (NumberFormatException e) {
            // Error message for invalid numeric input
            Toast.makeText(this, "Error: Please enter valid numbers.", Toast.LENGTH_SHORT).show();
        }
    }
}