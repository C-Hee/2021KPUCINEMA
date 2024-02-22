package termproject1.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AppComponentFactory;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MovieList extends AppCompatActivity {

    TextView editdate;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movielist);
        setTitle("통합 영화 예약 시스템");

        final GridView gv = (GridView) findViewById(R.id.gridView1);
        MyGridAdapter gAdapter = new MyGridAdapter(this);
        gv.setAdapter(gAdapter);

        Intent intent = getIntent();
        int selectyear = intent.getIntExtra("SelectYear",1);
        int selectmonth = intent.getIntExtra("SelectMonth",1);
        int selectday = intent.getIntExtra("SelectDay",1); // 전달받음
        editdate = (TextView) findViewById(R.id.editDay);
        editdate.setText("예매일자 : "+selectyear+"년 "+selectmonth+"월 "+selectday+"일");
    }

    public class MyGridAdapter extends BaseAdapter {
        Context context;

        public MyGridAdapter(Context c) {
            context = c;
        }

        public Integer[] changeMovie () {
            Intent intent = getIntent();
            int movieIndex = intent.getIntExtra("Index",1); // 전달받음
            Integer[] posterID = {};
            switch (movieIndex) { //날짜에 따라 다른 영화 출력
                case 0 :
                    posterID = new Integer[]{
                            R.drawable.movie1, R.drawable.movie2, R.drawable.movie3
                            , R.drawable.movie4, R.drawable.movie5, R.drawable.movie6
                    };
                    break;
                case 1 :
                    posterID = new Integer[]{
                            R.drawable.movie1, R.drawable.movie2, R.drawable.movie6
                            , R.drawable.movie7, R.drawable.movie8, R.drawable.movie9
                    };
                    break;
                case 2 :
                    posterID = new Integer[]{
                            R.drawable.movie1, R.drawable.movie6, R.drawable.movie7
                            , R.drawable.movie9, R.drawable.movie10, R.drawable.movie11
                    };
                    break;
                case 3 :
                    posterID = new Integer[]{
                            R.drawable.movie2, R.drawable.movie3, R.drawable.movie5
                            , R.drawable.movie9, R.drawable.movie10, R.drawable.movie11
                    };
                    break;
                case 4 :
                    posterID = new Integer[]{
                            R.drawable.movie3, R.drawable.movie4, R.drawable.movie5
                            , R.drawable.movie7, R.drawable.movie9, R.drawable.movie10
                    };
                    break;
                case 5 :
                    posterID = new Integer[]{
                            R.drawable.movie4, R.drawable.movie5, R.drawable.movie8
                            , R.drawable.movie9, R.drawable.movie10, R.drawable.movie11
                    };
                    break;
                case 6 :
                    posterID = new Integer[]{
                            R.drawable.movie5, R.drawable.movie6, R.drawable.movie8
                            ,R.drawable.movie9, R.drawable.movie10, R.drawable.movie11
                    };
                    break;
            }
            return posterID;
        }

        Integer[] posterID = changeMovie();

        @Override
        public int getCount() {
            return posterID.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageview = new ImageView(context);
            imageview.setLayoutParams(new GridView.LayoutParams(500,700));
            imageview.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageview.setPadding(5,5,5,5);

            imageview.setImageResource(posterID[position]);

            final int pos = position;
            imageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    View dialogView = (View) View.inflate(MovieList.this, R.layout.moviedlg, null);
                    AlertDialog.Builder dlg = new AlertDialog.Builder(MovieList.this);
                    TextView movieName = (TextView) dialogView.findViewById(R.id.MovieName);
                    TextView mvDir = (TextView) dialogView.findViewById(R.id.MovieDirector);
                    TextView mvAct = (TextView) dialogView.findViewById(R.id.MovieActor);
                    TextView mvGen = (TextView) dialogView.findViewById(R.id.MovieGenre);
                    TextView mvNor = (TextView) dialogView.findViewById(R.id.MovieNormal);
                    TextView mvSyn = (TextView) dialogView.findViewById(R.id.MovieSynop);
                    Button btnTr = (Button) dialogView.findViewById(R.id.btnTra);
                    mvSyn.setMovementMethod(new ScrollingMovementMethod()); // 스크롤 기능 추가
                    ImageView mvPoster = (ImageView) dialogView.findViewById(R.id.MoviePoster);
                    mvPoster.setImageResource(posterID[pos]);

                    String[] movie1All = getResources().getStringArray(R.array.movie1string);
                    String[] movie2All = getResources().getStringArray(R.array.movie2string);
                    String[] movie3All = getResources().getStringArray(R.array.movie3string);
                    String[] movie4All = getResources().getStringArray(R.array.movie4string);
                    String[] movie5All = getResources().getStringArray(R.array.movie5string);
                    String[] movie6All = getResources().getStringArray(R.array.movie6string);
                    String[] movie7All = getResources().getStringArray(R.array.movie7string);
                    String[] movie8All = getResources().getStringArray(R.array.movie8string);
                    String[] movie9All = getResources().getStringArray(R.array.movie9string);
                    String[] movie10All = getResources().getStringArray(R.array.movie10string);
                    String[] movie11All = getResources().getStringArray(R.array.movie11string); // 영화 정보들 배열에 저장

                    String[] traLink = {movie1All[6],movie2All[6],movie3All[6],movie4All[6],movie5All[6],
                            movie6All[6],movie7All[6],movie8All[6],movie9All[6],movie10All[6],movie11All[6]};
                    int i = 0;

                    switch (posterID[pos]) {
                        case R.drawable.movie1 :
                            SetMovieInfo(movieName,mvDir,mvAct,mvGen,mvNor,mvSyn,movie1All);
                            i=0;
                            break;
                        case R.drawable.movie2 :
                            SetMovieInfo(movieName,mvDir,mvAct,mvGen,mvNor,mvSyn,movie2All);
                            i=1;
                            break;
                        case R.drawable.movie3 :
                            SetMovieInfo(movieName,mvDir,mvAct,mvGen,mvNor,mvSyn,movie3All);
                            i=2;
                            break;
                        case R.drawable.movie4 :
                            SetMovieInfo(movieName,mvDir,mvAct,mvGen,mvNor,mvSyn,movie4All);
                            i=3;
                            break;
                        case R.drawable.movie5 :
                            SetMovieInfo(movieName,mvDir,mvAct,mvGen,mvNor,mvSyn,movie5All);
                            i=4;
                            break;
                        case R.drawable.movie6 :
                            SetMovieInfo(movieName,mvDir,mvAct,mvGen,mvNor,mvSyn,movie6All);
                            i=5;
                            break;
                        case R.drawable.movie7 :
                            SetMovieInfo(movieName,mvDir,mvAct,mvGen,mvNor,mvSyn,movie7All);
                            i=6;
                            break;
                        case R.drawable.movie8 :
                            SetMovieInfo(movieName,mvDir,mvAct,mvGen,mvNor,mvSyn,movie8All);
                            i=7;
                            break;
                        case R.drawable.movie9 :
                            SetMovieInfo(movieName,mvDir,mvAct,mvGen,mvNor,mvSyn,movie9All);
                            i=8;
                            break;
                        case R.drawable.movie10 :
                            SetMovieInfo(movieName,mvDir,mvAct,mvGen,mvNor,mvSyn,movie10All);
                            i=9;
                            break;
                        case R.drawable.movie11 :
                            SetMovieInfo(movieName,mvDir,mvAct,mvGen,mvNor,mvSyn,movie11All);
                            i=10;
                            break;
                    } // 선택된 영화에 맞는 정보 입력

                    final int index = i;

                    dlg.setTitle("영화 정보");
                    dlg.setView(dialogView);
                    dlg.setNegativeButton("닫기", null);
                    dlg.setPositiveButton("예매하기",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = getIntent();
                                    int selectyear = intent.getIntExtra("SelectYear",1);
                                    int selectmonth = intent.getIntExtra("SelectMonth",1);
                                    int selectday = intent.getIntExtra("SelectDay",1);
                                    String ID = intent.getStringExtra("mvID");
                                    String name = intent.getStringExtra("mvNick");
                                    int cash = intent.getIntExtra("Cash",1);
                                    String id=intent.getStringExtra("id");
                                    Intent kintent = new Intent(getApplicationContext(), MovieSit.class);
                                    kintent.putExtra("SYear",selectyear);
                                    kintent.putExtra("SMonth",selectmonth);
                                    kintent.putExtra("SDay",selectday);
                                    kintent.putExtra("MvPoster",posterID[pos]);
                                    kintent.putExtra("Cash",cash);
                                    kintent.putExtra("mvNick",name);
                                    kintent.putExtra("mvName",movieName.getText().toString());
                                    kintent.putExtra("id",id);
                                    startActivity(kintent);
                                    finish();
                                }
                            });
                    dlg.show();

                    btnTr.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent trintent = new Intent(Intent.ACTION_VIEW, Uri.parse(SetMovieLink(index,traLink)));
                            startActivity(trintent);
                        }
                    });
                }
            });
            return imageview;
        }
        public void SetMovieInfo(TextView name, TextView dir, TextView act
                ,TextView gen, TextView normal, TextView synop, String[] array) {
            name.setText(array[0]);
            dir.setText(array[1]);
            act.setText(array[2]);
            gen.setText(array[3]);
            normal.setText(array[4]);
            synop.setText(array[5]);
        } // 영화 정보 입력 메소드

        public String SetMovieLink(int i, String[] array) {
            String Link = array[i];
            return  Link;
        } // 영화 트레일러 주소 입력 메소트
    }
}
