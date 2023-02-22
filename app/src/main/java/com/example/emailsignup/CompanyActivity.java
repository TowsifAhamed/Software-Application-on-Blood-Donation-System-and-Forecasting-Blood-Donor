package com.example.emailsignup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class CompanyActivity extends AppCompatActivity {
    private TextView choose_file1,choose_file2;
    private Button pick_file1,pick_file2,model,web;
    private TextView result;
    Intent file_intent1,file_intent2;
    String trainpath,testpath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);
        model = findViewById(R.id.model);
        result = findViewById(R.id.result);
        web = findViewById(R.id.web);
        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CompanyActivity.this, WebActivity.class));
            }
        });
        choose_file1 = findViewById(R.id.choose_file1);
        pick_file1 = findViewById(R.id.pick_file1);
        pick_file1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                file_intent1 = new Intent(Intent.ACTION_GET_CONTENT);
                file_intent1.setType("*/*");
                startActivityForResult(file_intent1,10);
            }
        });
        choose_file2 = findViewById(R.id.choose_file2);
        pick_file2 = findViewById(R.id.pick_file2);
        pick_file2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                file_intent2 = new Intent(Intent.ACTION_GET_CONTENT);
                file_intent2.setType("*/*");
                startActivityForResult(file_intent2,20);
            }
        });
        model.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model(choose_file1.getText().toString(),choose_file2.getText().toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK) {
                    trainpath = data.getData().getPath();
                    choose_file1.setText(trainpath.substring(15));
                }
                break;
            case 20:
                if (resultCode == RESULT_OK) {
                    testpath = data.getData().getPath();
                    choose_file2.setText(testpath.substring(15));
                }
                break;
        }
    }

    private void model(String trainpath, String testpath) {
        final String[][] traindata,testdata;
        int[] count = new int[]{0,0,0,0,0,0,0,0,0,0,0};
        int[] truecount = new int[]{0,0,0,0,0,0,0,0,0,0,0};
        int[] testcount = new int[]{0,0,0,0,0,0,0,0,0,0,0};
        double res = 0.0;
        try (CSVReader trainreader = new CSVReader(new BufferedReader(
                new FileReader(trainpath)))) {
            List<String[]> lines1;
            lines1 = trainreader.readAll();
            traindata = lines1.toArray(new String[lines1.size()][]);
            for (int i=1;i<traindata.length;i++) {
                double prob = Math.round(Double.parseDouble(traindata[i][2])*10)/10.0;
                if (prob==0.0) count[0]++;
                if (prob==0.1) count[1]++;
                if (prob==0.2) count[2]++;
                if (prob==0.3) count[3]++;
                if (prob==0.4) count[4]++;
                if (prob==0.5) count[5]++;
                if (prob==0.6) count[6]++;
                if (prob==0.7) count[7]++;
                if (prob==0.8) count[8]++;
                if (prob==0.9) count[9]++;
                if (prob==1.0) count[10]++;
                if (Integer.parseInt(traindata[i][1])==1) {
                    if (prob==0.0) truecount[0]++;
                    if (prob==0.1) truecount[1]++;
                    if (prob==0.2) truecount[2]++;
                    if (prob==0.3) truecount[3]++;
                    if (prob==0.4) truecount[4]++;
                    if (prob==0.5) truecount[5]++;
                    if (prob==0.6) truecount[6]++;
                    if (prob==0.7) truecount[7]++;
                    if (prob==0.8) truecount[8]++;
                    if (prob==0.9) truecount[9]++;
                    if (prob==1.0) truecount[10]++;
                }
            }
            try (CSVReader testreader = new CSVReader(new BufferedReader(
                    new FileReader(testpath)))) {
                List<String[]> lines2;
                lines2 = testreader.readAll();
                testdata = lines2.toArray(new String[lines2.size()][]);
                for (int i=1;i<testdata.length;i++) {
                    double prob = Math.round(Double.parseDouble(testdata[i][2])*10)/10.0;
                    if (prob==0.0) testcount[0]++;
                    if (prob==0.1) testcount[1]++;
                    if (prob==0.2) testcount[2]++;
                    if (prob==0.3) testcount[3]++;
                    if (prob==0.4) testcount[4]++;
                    if (prob==0.5) testcount[5]++;
                    if (prob==0.6) testcount[6]++;
                    if (prob==0.7) testcount[7]++;
                    if (prob==0.8) testcount[8]++;
                    if (prob==0.9) testcount[9]++;
                    if (prob==1.0) testcount[10]++;
                }
                for (int j=0;j<=0;j++){
                    if(count[j]!=0) res+=testcount[j]*truecount[j]/count[j];
                }
                int resultant = (int) Math.ceil(res);
                result.setText(String.valueOf(resultant));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}