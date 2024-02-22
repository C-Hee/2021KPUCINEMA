package termproject1.myapplication;


import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;

public class MovieSit extends MainActivity {

    Button[][] btnList = null;
    int check = 0;
    TextView sit1, sit2, sit3, mvTitle;
    String[] a = {"A","B","C","D","E","F"};
    String[] b = {"1","2","3","4","5","6","7","8"}; //좌석 배열
    String filename;
    int posterID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moviesit);

        Intent zintent = getIntent();
        int syear = zintent.getIntExtra("SYear",1);
        int smonth = zintent.getIntExtra("SMonth",1);
        int sday = zintent.getIntExtra("SDay",1);
        int psID = zintent.getIntExtra("MvPoster",1);
        String title = zintent.getStringExtra("mvName");
        posterID = psID; // 인텐트로 넘겨받기

        ImageView mvImg = (ImageView)findViewById(R.id.reImg);
        mvImg.setImageResource(psID);

        sit1 = (TextView)findViewById(R.id.text1);
        sit2 = (TextView)findViewById(R.id.text2);
        sit3 = (TextView)findViewById(R.id.text3);


        TextView sitArray[] = new TextView[3];
        sitArray[0] = sit1;
        sitArray[1] = sit2;
        sitArray[2] = sit3;

        sitArray[0].setText("");
        sitArray[1].setText("");
        sitArray[2].setText(""); // 좌석선택 초기화

        TextView chDay = (TextView)findViewById(R.id.cDay);
        mvTitle = (TextView)findViewById(R.id.mvTitle);
        mvTitle.setText(title);
        chDay.setText("예매 일자 : "+syear+"년 "+smonth+"월 "+sday+"일");
        TextView peNum = (TextView)findViewById(R.id.peopleNum);
        String[] people = {"1","2","3"};

        Spinner peCount = (Spinner)findViewById(R.id.spPeople);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,people);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        peCount.setAdapter(adapter); // 스피너 인원 목록 생성

        Button bReset = (Button)findViewById(R.id.btnReset);
        Button bFin = (Button)findViewById(R.id.btnFinish);
        btnList = new Button[6][8];

        int[][] btnID = {
                {R.id.btnA1, R.id.btnA2, R.id.btnA3, R.id.btnA4, R.id.btnA5, R.id.btnA6, R.id.btnA7, R.id.btnA8},
                {R.id.btnB1, R.id.btnB2, R.id.btnB3, R.id.btnB4, R.id.btnB5, R.id.btnB6, R.id.btnB7, R.id.btnB8},
                {R.id.btnC1, R.id.btnC2, R.id.btnC3, R.id.btnC4, R.id.btnC5, R.id.btnC6, R.id.btnC7, R.id.btnC8},
                {R.id.btnD1, R.id.btnD2, R.id.btnD3, R.id.btnD4, R.id.btnD5, R.id.btnD6, R.id.btnD7, R.id.btnD8},
                {R.id.btnE1, R.id.btnE2, R.id.btnE3, R.id.btnE4, R.id.btnE5, R.id.btnE6, R.id.btnE7, R.id.btnE8},
                {R.id.btnF1, R.id.btnF2, R.id.btnF3, R.id.btnF4, R.id.btnF5, R.id.btnF6, R.id.btnF7, R.id.btnF8}
        }; // 좌석 버튼 ID 배열에 저장

        peCount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                peNum.setText(people[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                peNum.setText("0");
            }
        }); // 스피너에서 인원 선택시 인원 출력

        for(int i=0; i<6; i++){
            for(int j=0; j<8; j++){
                this.btnList[i][j] = (Button)findViewById(btnID[i][j]);
            }
        }

        View.OnClickListener btnListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i=0; i<6; i++) {
                    for(int j=0; j<8; j++) {
                        if(view.getId() == btnList[i][j].getId()){
                            String num = peCount.getSelectedItem().toString();
                            int peopleNumber = Integer.parseInt(num);
                            if(check==peopleNumber) {
                                Toast.makeText(getApplicationContext(),"더이상 선택할 수 없습니다.",Toast.LENGTH_SHORT).show();
                            } // 선택한 인원수보다 더많은 좌석 선택시 선택불가와 메시지 출력
                            else {
                                btnList[i][j].setEnabled(false);
                                String sit = a[i] + b[j];
                                sitArray[check].setText(sit);
                                check = check + 1;
                            }
                        }
                    }
                }
            }
        }; // 버튼마다 리스너 지정

        for(int i=0; i<6; i++){
            for(int j=0; j<8; j++){
                this.btnList[i][j].setOnClickListener(btnListener);
            }
        } // 버튼마다 리스너 지정


        bReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0; i<6; i++){
                    for(int j=0; j<8; j++){
                        btnList[i][j].setEnabled(true);
                    }
                }
                sitArray[0].setText("");
                sitArray[1].setText("");
                sitArray[2].setText("");
                check=0;
            }
        }); // 상태 초기화

        bFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sitCheck1 = sitArray[0].getText().toString();
                String sitCheck2 = sitArray[1].getText().toString();
                String sitCheck3 = sitArray[2].getText().toString();
                String num = peCount.getSelectedItem().toString();
                Intent intent = getIntent();
                String name = intent.getStringExtra("mvNick");
                int cash = intent.getIntExtra("Cash",1);
                String id= intent.getStringExtra("id");
                int fYear = intent.getIntExtra("SYear",1);
                int fMonth = intent.getIntExtra("SMonth",1);
                int fDay = intent.getIntExtra("SDay",1);
                filename = "("+name+")"+Integer.toString(fYear)+"-"+Integer.toString(fMonth)+"-"+Integer.toString(fDay)+".txt";
                Intent lastintent = new Intent(getApplicationContext(), SecondActivity.class);


                int peopleNumber = Integer.parseInt(num);
                if(peopleNumber==1 && sitCheck1.equals("")) {
                    Toast.makeText(getApplicationContext(),"좌석을 선택해주세요.",Toast.LENGTH_SHORT).show();
                }
                else if(peopleNumber==2 && sitCheck2.equals("")) {
                    Toast.makeText(getApplicationContext(),"좌석을 선택해주세요.",Toast.LENGTH_SHORT).show();
                }
                else if(peopleNumber==3 && sitCheck2.equals("")) {
                    Toast.makeText(getApplicationContext(),"좌석을 선택해주세요.",Toast.LENGTH_SHORT).show();
                }
                else if(peopleNumber==3 && sitCheck3.equals("")) {
                    Toast.makeText(getApplicationContext(),"좌석을 선택해주세요.",Toast.LENGTH_SHORT).show();
                }
                else {
                    switch (peopleNumber) {
                        case 1 :
                            lastintent.putExtra("fSit",sitCheck1);
                            break;
                        case 2 :
                            lastintent.putExtra("fSit",sitCheck1);
                            lastintent.putExtra("sSit",sitCheck2);
                            break;
                        case 3 :
                            lastintent.putExtra("fSit",sitCheck1);
                            lastintent.putExtra("sSit",sitCheck2);
                            lastintent.putExtra("tSit",sitCheck3);
                            break;
                    }
                    int lastcash = cash - 11000*peopleNumber;
                    if(lastcash<0) {
                        Toast.makeText(getApplicationContext(),"잔액이 부족합니다.",Toast.LENGTH_SHORT).show();
                        lastintent.putExtra("gname",name);
                        lastintent.putExtra("gcash",cash);
                        lastintent.putExtra("gid",id);
                        startActivity(lastintent);
                        finish();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"예매가 완료되었습니다.",Toast.LENGTH_SHORT).show();
                        lastintent.putExtra("gname",name);
                        lastintent.putExtra("gcash",lastcash);
                        lastintent.putExtra("gid",id);
                        DBHelper dbHelper = new DBHelper(getApplicationContext());
                        SQLiteDatabase sqlDB=dbHelper.getWritableDatabase();
                        String query = ("UPDATE groupTBL SET gCash = '"+lastcash+"' WHERE gid ='"+id+"';");
                        sqlDB.execSQL(query);
                        sqlDB.close();

                        try {
                            String str = Integer.toString(fYear)+Integer.toString(fMonth)+Integer.toString(fDay) + "_" + title + "_"
                                    + Integer.toString(peopleNumber) + "명 " + sitCheck1 + sitCheck2 + sitCheck3;
                            FileOutputStream outFs = openFileOutput(filename, Context.MODE_PRIVATE);
                            outFs.write(str.getBytes());
                            outFs.close();
                        } catch (IOException e) {}
                        lastintent.putExtra("fName",filename);  //// 예매내역 파일로 저장
                        startActivity(lastintent);
                        finish();
                    }
                }
            }
        });

    }
}