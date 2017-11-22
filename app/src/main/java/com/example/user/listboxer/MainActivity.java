package com.example.user.listboxer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> recordsList = new ArrayList<>();
    private String[] rangeValuesAlpha = {"All", "<none>", "a-m", "n-z"};
    private String[] rangeValuesNum = {"All", "<none>", "0-100", "101-200", "201-300", "301-9999"};
    private String[] rangeValuesAll = {"All", "<none>", "0-100", "101-200", "201-300", "301-9999", "a-m", "n-z"};
    Button add;
    EditText value;
    EditText list;
    Spinner spinner;
    CheckBox alpha;
    CheckBox num;
    RadioButton desc;
    RadioButton asc;
    TextView totalCount;
    TextView shownCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner = findViewById(R.id.range);
        add = findViewById(R.id.add);
        value = findViewById(R.id.value);
        setSymbols("alpha");
        list = findViewById(R.id.list);
        alpha = findViewById(R.id.alpha);
        alpha.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (alpha.isChecked() && !num.isChecked()) {
                    setSymbols("alpha");
                    spinner.setAdapter(getAdapter(rangeValuesAlpha));
                    spinner.setEnabled(true);
                    value.setEnabled(true);
                }
                if (!alpha.isChecked() && num.isChecked()) {
                    setSymbols("num");
                    spinner.setAdapter(getAdapter(rangeValuesNum));
                    spinner.setEnabled(true);
                    value.setEnabled(true);
                }
                if (!alpha.isChecked() && !num.isChecked()) {
                    value.setEnabled(false);
                    //text.setText("");
                    spinner.setEnabled(false);
                }
                if (alpha.isChecked() && num.isChecked()) {
                    setSymbols("all");
                    spinner.setAdapter(getAdapter(rangeValuesAll));
                    spinner.setEnabled(true);
                    value.setEnabled(true);
                }
                displayAllItems();
            }
        });
        num = findViewById(R.id.num);
        num.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (alpha.isChecked() && !num.isChecked()) {
                    setSymbols("alpha");
                    spinner.setAdapter(getAdapter(rangeValuesAlpha));
                    spinner.setEnabled(true);
                    value.setEnabled(true);
                }
                if (!alpha.isChecked() && num.isChecked()) {
                    setSymbols("num");
                    spinner.setAdapter(getAdapter(rangeValuesNum));
                    spinner.setEnabled(true);
                    value.setEnabled(true);
                }
                if (!alpha.isChecked() && !num.isChecked()) {
                    value.setEnabled(false);
                    //text.setText("");
                    spinner.setEnabled(false);
                }
                if (alpha.isChecked() && num.isChecked()) {
                    setSymbols("all");
                    spinner.setAdapter(getAdapter(rangeValuesAll));
                    spinner.setEnabled(true);
                    value.setEnabled(true);
                }
                displayAllItems();
            }
        });
        desc = findViewById(R.id.desc);
        desc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sortDesc();
            }
        });
        asc = findViewById(R.id.asc);
        asc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sortAsc();
            }
        });
        totalCount = findViewById(R.id.totalCount);
        shownCount = findViewById(R.id.shownCount);
        spinner.setAdapter(getAdapter(rangeValuesAlpha));
        // заголовок
        spinner.setPrompt("Title");
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
                                              @Override
                                              public void onItemSelected(AdapterView<?> parent, View view,
                                                                         int position, long id) {
                                                  checkRange(spinner.getSelectedItem().toString());

                                              }

                                              @Override
                                              public void onNothingSelected(AdapterView<?> adapterView) {

                                              }
                                          }
        );
        list.setEnabled(false);
    }

    public void clickAddButton(View view) {
        checkInputValue();
    }

    private void checkInputValue(){
        if (!value.getText().toString().equals("")) {
            if (!alpha.isChecked()) {
                Integer x = Integer.parseInt(value.getText().toString());
                if (x <= 10000) addText();
                else {
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle("Error");
                    alertDialog.setMessage("Value is wrong");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();

                }
            }
            else  addText();

        }
        if (desc.isSelected())  sortDesc();
        if (asc.isSelected())  sortAsc();
    }

    private void sortDesc(){
        String curList = list.getText().toString();
        String[] curListArray = curList.split("\n");
        Arrays.sort(curListArray, Collections.reverseOrder());
        String temp = curListArray[curListArray.length-1];
        curListArray[curListArray.length-1] = curListArray[curListArray.length-2];
        curListArray[curListArray.length-2] = temp;
        StringBuilder sortedText = new StringBuilder();
        for (String x : curListArray) {
            sortedText.append(x).append("\n");
        }
        list.setText(sortedText.toString());
    }

    private void sortAsc(){
        String curList = list.getText().toString();
        String[] curListArray = curList.split("\n");
        Arrays.sort(curListArray);
        StringBuilder sortedText = new StringBuilder();
        for (String x : curListArray) {
            sortedText.append(x).append("\n");
        }
        list.setText(sortedText.toString());
    }

    private void addText(){
        list.append(value.getText() + "\n");
        recordsList.add(value.getText().toString());
        value.setText("");
        totalCount.setText(String.valueOf(recordsList.size()));
        checkRange(spinner.getSelectedItem().toString());
    }

    public void clickClearButton(View view) {
        list.setText(null);
        recordsList = new ArrayList<String>();
        totalCount.setText("0");
        shownCount.setText("0");
    }

    private void checkRange(String item){
        if (item.equals("<none>")) {
            list.setText("");
            shownCount.setText(String.valueOf(list.getLineCount() - 1));
        }
        if (item.equals("All")) {
            displayAllItems();
        }
        if (item.equals("0-100")) {
            applyRangeNum(0, 100);
            return;
        }
        if (item.equals("101-200")) {
            applyRangeNum(101, 200);
            return;
        }
        if (item.equals("201-300")) {
            applyRangeNum(201, 300);
            return;
        }
        if (item.equals("301-9999")) {
            applyRangeNum(301, 9999);
            return;
        }
        if (item.equals("a-m")) {
            Pattern p = Pattern.compile("^[a-mA-M]");
            applyRangeAlpha(p);
            return;
        }
        if (item.equals("n-z")) {
            Pattern p = Pattern.compile("^[n-zN-Z]");
            applyRangeAlpha(p);
        }
    }

    private void displayAllItems(){
        StringBuilder newText = new StringBuilder();
        for (String x : recordsList) {
            newText.append(x).append("\n");
        }
        list.setText(newText.toString());
        shownCount.setText(String.valueOf(list.getLineCount() - 1));
    }

    private void applyRangeNum(int min, int max) {
        StringBuilder newText = new StringBuilder();
        for (String x : recordsList) {
            try {
                if (Integer.parseInt(x) >= min && Integer.parseInt(x) <= max) {
                    newText.append(x).append("\n");
                }
            }
            catch (Exception e){
                System.out.println(e.toString());
            }
        }
        list.setText(newText.toString());
        shownCount.setText(String.valueOf(list.getLineCount() - 1));
    }

    private void applyRangeAlpha(Pattern p) {
        StringBuilder newText = new StringBuilder();
        for (String x : recordsList) {
            Matcher matcher = p.matcher(x);
            if (matcher.find()) newText.append(x).append("\n");
        }
        list.setText(newText.toString());
        shownCount.setText(String.valueOf(list.getLineCount() - 1));
    }

    public void setSymbols(String symbols) {
        if (symbols.equals("num")){
            value.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        }
        if (symbols.equals("alpha")){
            value.setKeyListener(DigitsKeyListener.getInstance("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        }
        if (symbols.equals("all")){
            value.setKeyListener(DigitsKeyListener.getInstance("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"));
        }
     }

     private ArrayAdapter<String> getAdapter(String[] range){
         ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, range);
         adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         return adapter;
     }


}
