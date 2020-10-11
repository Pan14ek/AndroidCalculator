package ua.nure.makieiev.calculator;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigDecimal;

import ua.nure.makieiev.math.Calculator;

/**
 * @author Oleksii Makieiev PZPI-17-1
 */
public class MainActivity extends AppCompatActivity {

    public static final String RESULT = "result";

    private TextView resultField;
    private EditText numberField;
    private Calculator calculator;
    private BigDecimal result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(RESULT, result.toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        result = new BigDecimal(savedInstanceState.getString(RESULT));
        resultField.setText(String.valueOf(result));
    }

    public void onClickElement(View view) {
        Button button = (Button) view;
        numberField.append(button.getText());

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onClickGetResult(View view) {
        String number = numberField.getText().toString();
        calculator.writeLine(number);
        result = calculator.calculate();
        resultField.setText(String.valueOf(result));
    }

    private void init() {
        resultField = findViewById(R.id.resultField);
        numberField = findViewById(R.id.numberField);
        calculator = new Calculator();
        result = new BigDecimal(0);
    }

}