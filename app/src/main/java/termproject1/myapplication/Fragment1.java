package termproject1.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Fragment1 extends Fragment{
    Button btnmovie;
    CalendarView calview1;
    int selectYear, selectMonth, selectDay;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view1 = inflater.inflate(R.layout.fragment1,container,false);
        btnmovie = (Button)view1.findViewById(R.id.BtnMovie);
        calview1 = (CalendarView)view1.findViewById(R.id.CalView);
        btnmovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!calview1.isSelected())
                {
                    Toast.makeText(getActivity(),"날짜를 선택하십시오",Toast.LENGTH_SHORT).show();
                }
                else if(DayCount()>6) {
                    Toast.makeText(getActivity(),"선택한 날짜에 상영되는 영화가 없습니다.",Toast.LENGTH_SHORT).show();
                }
                else {
                    int hCash=getArguments().getInt("cash",1);
                    String id=getArguments().getString("id");
                    String lastname=getArguments().getString("name");;
                    Intent intent = new Intent(getActivity(), MovieList.class);
                    intent.putExtra("SelectYear", selectYear);
                    intent.putExtra("SelectMonth", selectMonth);
                    intent.putExtra("SelectDay", selectDay); // 예매날짜 전달
                    intent.putExtra("Index",DayCount()); // 날짜별로 지정
                    intent.putExtra("id",id);
                    intent.putExtra("mvNick",lastname);
                    intent.putExtra("Cash",hCash); //잔액 전달
                    startActivity(intent);
                    getActivity().finish();

                }
            }
        });

        calview1.setMinDate(System.currentTimeMillis()); // 과거날짜 선택 불가
        calview1.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                selectYear=year;
                selectMonth=month+1;
                selectDay=dayOfMonth;
                calendarView.setSelected(true);
            }
        });
        return view1;
    }

    public int DayCount () {

        try {
            SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
            String current = simple.format(System.currentTimeMillis()); // 현재 날짜 가져오기
            String select = selectYear + "-" + selectMonth + "-" + selectDay;
            Date currentDate = simple.parse(current);
            Date selectDate = simple.parse(select);

            long gettime = selectDate.getTime() - currentDate.getTime(); // 선택한 날짜와 일 수 비교
            long result = gettime / 86400000;
            return (int)result;
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
