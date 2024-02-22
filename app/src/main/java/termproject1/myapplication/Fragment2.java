package termproject1.myapplication;

import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Fragment2 extends Fragment {
    Button btnChangeName,btnChangePw;
    TextView edtId, edtCash;
    EditText edtName,edtPw,edtPw2;
    MainActivity.DBHelper dbHelper;
    SQLiteDatabase sqlDB;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view2 = inflater.inflate(R.layout.fragment2,container,false);
        edtId = (TextView) view2.findViewById(R.id.edtId);
        edtName= (EditText)view2.findViewById(R.id.edtName);
        edtPw = (EditText)view2.findViewById(R.id.edtPw);
        edtPw2 = (EditText)view2.findViewById(R.id.edtPw2);
        btnChangeName = (Button)view2.findViewById(R.id.btnChangeName);
        btnChangePw = (Button)view2.findViewById(R.id.btnChangePw);
        edtCash = (TextView) view2.findViewById(R.id.edtCash);

        edtId.setText(getArguments().getString("id"));
        edtCash.setText(String.valueOf(getArguments().getInt("cash",0)));
        dbHelper = new MainActivity.DBHelper(getActivity());

        btnChangeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputName=edtName.getText().toString();
                String inputId=getArguments().getString("id");

                if (inputName.equals(""))
                {
                    Toast.makeText(getActivity(), "빈칸에 정보를 입력해 주십시오", Toast.LENGTH_SHORT).show();
                }
                else if(inputName.length()>20) {
                    Toast.makeText(getActivity(), "20자를 초과하여 설정할 수 없습니다", Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        sqlDB = dbHelper.getWritableDatabase();
                        String query = ("UPDATE groupTBL SET gName = '"+inputName+"'WHERE gid ='"+inputId+"';");
                        sqlDB.execSQL(query);
                        Toast.makeText(getActivity(), "이름 변경 성공!", Toast.LENGTH_SHORT).show();
                    } catch (SQLiteConstraintException e) {
                        Toast.makeText(getActivity(), "변경에 실패했습니다 다시 시도해주십시오", Toast.LENGTH_SHORT).show();
                    }

                    sqlDB.close();
                }

            }
        });
        btnChangePw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputId=getArguments().getString("id");
                String inputPw=edtPw.getText().toString();
                String inputPw2=edtPw2.getText().toString();
                //빈칸이 있을 경우
                if (inputPw.equals("")|| inputPw2.equals(""))
                {
                    Toast.makeText(getActivity(), "빈칸에 정보를 입력해 주십시오", Toast.LENGTH_SHORT).show();
                }
                //두 비밀번호가 다른 경우
                else if (!inputPw.equals(inputPw2))
                {
                    Toast.makeText(getActivity(), "두 비밀번호가 다릅니다. 다시 입력해 주십시오", Toast.LENGTH_SHORT).show();
                }
                //비밀번호 길이제한
                else if(inputPw.length()>20){
                    Toast.makeText(getActivity(), "20자를 초과하여 설정할 수 없습니다", Toast.LENGTH_SHORT).show();
                }
                else{
                    try {
                        sqlDB = dbHelper.getWritableDatabase();
                        String query = ("UPDATE groupTBL SET gPw = '"+inputPw+"'WHERE gid ='"+inputId+"'");
                        sqlDB.execSQL(query);
                        Toast.makeText(getActivity(), "비밀번호 변경 성공!", Toast.LENGTH_SHORT).show();
                    } catch (SQLiteConstraintException e) {
                        Toast.makeText(getActivity(), "변경에 실패했습니다 다시 시도해주십시오", Toast.LENGTH_SHORT).show();
                    }
                    sqlDB.close();

                }
            }
        });
        return view2;
    }
}
