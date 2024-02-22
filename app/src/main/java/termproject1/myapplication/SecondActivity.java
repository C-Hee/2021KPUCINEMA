package termproject1.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentHostCallback;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ActionBar;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SecondActivity extends AppCompatActivity {

    Toolbar toolbar;
    Fragment fr1, fr2, fr3;

    String id;
    String name;
    String pw;
    int cash;
    SQLiteDatabase sqlDB;
    MainActivity.DBHelper dbHelper;
    TextView menuname,menuid,menucash;


    String fName = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_main);
        //네비게이션 관련 https://developer.android.com/guide/navigation/navigation-ui?hl=ko 참조
        //프래그먼트 관련 https://developer.android.com/guide/components/fragments.html?hl=ko
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.seconddrawer);
        NavigationView navigationView = (NavigationView) findViewById(R.id.secondnavi);
        //앱 상단 앱바
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //toolbar.setVisibility(View.INVISIBLE);
        //앱바에 메뉴
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //프래그먼트 관리를 위한 변수
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        fr1 = new Fragment1();
        fr2 = new Fragment2();

        //로그인 화면으로부터 전달받는 정보
        Intent intent = getIntent();
        id=intent.getStringExtra("gid");
        name=intent.getStringExtra("gname");
        pw=intent.getStringExtra("gpw");
        cash=intent.getIntExtra("gcash",1);

        //예매 화면에서 전달받는 정보
        String filename = intent.getStringExtra("fName");
        fName = filename;
        //Frag1로 전달할 정보
        Bundle bundleToFrag1=new Bundle();
        bundleToFrag1.putInt("cash",cash);
        bundleToFrag1.putString("id",id);
        bundleToFrag1.putString("name",name);
        fr1.setArguments(bundleToFrag1);

        //Frag2로 전달할 정보
        Bundle bundleToFrag2 = new Bundle();
        bundleToFrag2.putString("id",id);
        bundleToFrag2.putInt("cash",cash);
        fr2.setArguments(bundleToFrag2);
        ft.add(R.id.framelayout, fr1);
        ft.commit();



        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.open, R.string.close
        )
            //메뉴 오픈할 때 사용자 정보 갱신
        {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                dbHelper=new MainActivity.DBHelper(getApplicationContext());
                sqlDB = dbHelper.getReadableDatabase();

                menuid=(TextView) findViewById(R.id.menuid);
                menuname=(TextView) findViewById(R.id.menuname);
                menucash=(TextView) findViewById(R.id.menucash);

                Cursor cursor = sqlDB.rawQuery("SELECT * FROM groupTBL WHERE gId = '"+id+"';", null);
                while(cursor.moveToNext()){
                    name = cursor.getString(0);
                    pw = cursor.getString(2);
                    cash = cursor.getInt(3);
                }

                menuname.setText("이름:   "+name);
                menuid.setText("아이디: "+id);
                menucash.setText("잔액:   "+String.valueOf(cash));
                cursor.close();
                sqlDB.close();
            }

        };


        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fr;
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.menuitem1:
                        Intent intent = new Intent(SecondActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(getApplicationContext(), "로그아웃 되었습니다", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menuitem2:
                        ft.replace(R.id.framelayout, fr1).commit();
                        break;
                    case R.id.menuitem3:
                        ft.replace(R.id.framelayout, fr2).commit();
                        break;
                    case R.id.menuitem4:
                        String list = null;
                        FileInputStream inFs;
                        try {
                            inFs = openFileInput(filename);
                            byte[] txt = new byte[500];
                            inFs.read(txt);
                            inFs.close();
                            list = (new String(txt)).trim();
                            TextView text= (TextView)findViewById(R.id.textTicket);
                            text.setText(list);
                        } catch (IOException e) {
                            Toast.makeText(getApplicationContext(), "예매 내역이 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                        catch (NullPointerException e)
                        {
                            Toast.makeText(getApplicationContext(), "예매 내역이 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case R.id.menuitem5:
                        cash+=50000;
                        bundleToFrag1.putInt("cash",cash);
                        fr1.setArguments(bundleToFrag1);
                        break;

                }
                DrawerLayout drawer = findViewById(R.id.seconddrawer);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }

        });
    }
}