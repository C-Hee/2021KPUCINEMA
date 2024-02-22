package termproject1.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btnLogIn, btnSignUp;
    View dlgSignUp, dlgLogin;
    DBHelper dbHelper;
    SQLiteDatabase sqlDB;

    public static class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context) {
            super(context, "groupDB", null, 1);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE groupTBL(gName CHAR(20) , gId CHAR(20)  PRIMARY KEY, gPw CHAR(20),gCash INTEGER);");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {//본래 안드로이드 버전업시 초기화하는 용도이지만 그냥 초기화하는 용도로 사용
            db.execSQL("DROP TABLE IF EXISTS groupTBL");
            onCreate(db);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("KPU CINEMA");

        btnLogIn = (Button) findViewById(R.id.logIn);
        btnSignUp = (Button) findViewById(R.id.signUp);
        dbHelper = new DBHelper(this);



        //회원가입
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            //다이얼로그로 정보입력창을 띄움
            public void onClick(View view) {
                dlgSignUp = (View) View.inflate(MainActivity.this, R.layout.signup, null);
                AlertDialog.Builder dlgBuilder = new AlertDialog.Builder(MainActivity.this);
                dlgBuilder.setTitle("회원가입");
                EditText edtName = (EditText) dlgSignUp.findViewById(R.id.edtName);
                EditText edtId = (EditText) dlgSignUp.findViewById(R.id.edtId);
                EditText edtPw = (EditText) dlgSignUp.findViewById(R.id.edtPw);
                EditText edtPw2 = (EditText) dlgSignUp.findViewById(R.id.edtPw2);

                dlgBuilder.setView(dlgSignUp);
//getButton에서 오버라이드
                dlgBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

                dlgBuilder.setNegativeButton("취소", null);
                AlertDialog dialog = dlgBuilder.create();
                dialog.show();
                //회원가입-확인 버튼 클릭
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override

                    public void onClick(View view) {
                        String inputName=edtName.getText().toString();
                        String inputId=edtId.getText().toString();
                        String inputPw=edtPw.getText().toString();
                        String inputPw2=edtPw2.getText().toString();
                        //입력란에 공백이 있는 경우
                        if (inputName.equals("")|| inputId.equals("") || inputPw.equals("")|| inputPw2.equals(""))
                        {
                            Toast.makeText(MainActivity.this, "빈칸에 정보를 입력해 주십시오", Toast.LENGTH_SHORT).show();
                        }
                        //두 비밀번호가 다른 경우
                        else if (!inputPw.equals(inputPw2))
                        {
                            Toast.makeText(MainActivity.this, "두 비밀번호가 다릅니다. 다시 입력해 주십시오", Toast.LENGTH_SHORT).show();
                        } 
                        //ID, PW,닉네임 길이제한
                        else if(inputName.length()>20||inputId.length()>20||inputPw.length()>20){
                            Toast.makeText(MainActivity.this, "20자를 초과하여 설정할 수 없습니다", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                                sqlDB = dbHelper.getWritableDatabase();//

                                ContentValues cv=new ContentValues();
                                cv.put("gName",inputName);
                                cv.put("gId",inputId);
                                cv.put("gPw",inputPw);
                                cv.put("gCash",100000);
                                //id중복>SQLiteConstraintException
                            try {
                                long id=sqlDB.insertOrThrow("groupTBL",null,cv);
                                Toast.makeText(MainActivity.this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                            catch(SQLiteConstraintException e){
                                Toast.makeText(MainActivity.this, "이미 존재하는 ID입니다.", Toast.LENGTH_SHORT).show();
                            }
                            sqlDB.close();
                        }

                    }
                });

            }
        });

        //로그인
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlgLogin = (View) View.inflate(MainActivity.this, R.layout.login, null);//로그인창 띄우기
                AlertDialog.Builder dlgBuilder = new AlertDialog.Builder(MainActivity.this);
                dlgBuilder.setTitle("로그인");
                dlgBuilder.setView(dlgLogin);

                EditText edtId = (EditText) dlgLogin.findViewById(R.id.edtId);
                EditText edtPw = (EditText) dlgLogin.findViewById(R.id.edtPw);

                dlgBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {//getButton에서 오버라이드
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

                dlgBuilder.setNegativeButton("취소", null);

                AlertDialog dialog = dlgBuilder.create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String inputId=edtId.getText().toString();
                        String inputPw=edtPw.getText().toString();
                        if (inputId.equals("")
                                ||inputPw.equals(""))//입력란에 공백이 있는 경우
                        {
                            Toast.makeText(MainActivity.this, "빈칸을 입력해 주십시오", Toast.LENGTH_SHORT).show();
                        } 
                        else//DB 정보와 입력한 정보 비교
                        {

                            sqlDB = dbHelper.getReadableDatabase();
                            Cursor cursor = sqlDB.rawQuery("SELECT * FROM groupTBL WHERE gId='"+inputId+"';", null);//DB에서 검색 후 데이터

                            cursor.moveToFirst();
                            if(!cursor.isFirst()){//id를 조회할 수 없을 때
                                Toast.makeText(MainActivity.this, "잘못된 아이디 또는 비밀번호 입니다.\n다시 입력해 주십시오", Toast.LENGTH_SHORT).show();
                            }
                            else {

                                if (cursor.getString(2).equals(inputPw)) {//로그인 성공, second액티비티로 전환

                                    Toast.makeText(MainActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();

                                    Intent intent =new Intent(MainActivity.this, SecondActivity.class);
                                    intent.putExtra("gname",cursor.getString(0));//닉네임
                                    intent.putExtra("gid",cursor.getString(1));//ID
                                    intent.putExtra("gpw",cursor.getString(2));//Pw
                                    intent.putExtra("gcash",cursor.getInt(3));//돈
                                    cursor.close();
                                    sqlDB.close();
                                    dialog.dismiss();
                                    startActivity(intent);
                                    finish();
                                } else {//비밀번호 틀렸을 때
                                    cursor.close();
                                    sqlDB.close();
                                    Toast.makeText(MainActivity.this, "잘못된 아이디 또는 비밀번호 입니다.\n다시 입력해 주십시오", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }
                    }
                });
            }
        });
    }
}