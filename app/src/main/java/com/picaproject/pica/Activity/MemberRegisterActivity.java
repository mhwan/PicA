package com.picaproject.pica.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.picaproject.pica.CustomView.CustomBottomButton;
import com.picaproject.pica.R;
import com.picaproject.pica.Util.NetworkItems.DefaultResultItem;
import com.picaproject.pica.Util.NetworkItems.MemberRegisterItem;
import com.picaproject.pica.Util.NetworkUtility;
import com.picaproject.pica.Util.Validator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MemberRegisterActivity extends BaseToolbarActivity {
    private EditText edit_phonenumber, edit_email, edit_nickname, edit_password, edit_rePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_register);

        initView();
    }

    private void initView() {
        edit_email = (EditText) findViewById(R.id.edit_email);
        edit_nickname = (EditText) findViewById(R.id.edit_nickname);
        edit_phonenumber = (EditText) findViewById(R.id.edit_phonenumber);
        edit_password = (EditText) findViewById(R.id.edit_password);
        edit_rePassword = (EditText) findViewById(R.id.edit_re_password);
        CustomBottomButton register = findViewById(R.id.button_register);
        register.getButtonBackground().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValidate()) {
                    registerMemberWork();

                }
            }
        });
    }

    private void registerMemberWork() {
        MemberRegisterItem item = new MemberRegisterItem(edit_email.getText().toString(), edit_password.getText().toString(),
                edit_nickname.getText().toString(), edit_phonenumber.getText().toString());
        NetworkUtility.getInstance().registerMember(item, new Callback<DefaultResultItem>() {
            @Override
            public void onResponse(Call<DefaultResultItem> call, Response<DefaultResultItem> response) {
                if (response.isSuccessful() && response != null) {
                    switch (response.body().getCode()) {
                        case NetworkUtility.APIRESULT.RESULT_SUCCESS:
                            Toast.makeText(getApplicationContext(), "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                            break;
                        case NetworkUtility.APIRESULT.RESULT_REGISTER_ALREADY_EXIST:
                            Toast.makeText(getApplicationContext(), "이미 가입된 회원입니다.", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<DefaultResultItem> call, Throwable t) {
                Log.i("MEMBER REGISTER", "FAILED");
                t.printStackTrace();
            }
        });
    }

    private boolean checkValidate() {

        if (edit_email.getText().toString().length() == 0) {
            Toast.makeText(getApplicationContext(), "이메일주소를 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edit_password.getText().toString().length() == 0 || edit_rePassword.getText().toString().length() == 0) {
            Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edit_nickname.getText().toString().length() == 0) {
            Toast.makeText(getApplicationContext(), "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edit_phonenumber.getText().toString().length() == 0) {
            Toast.makeText(getApplicationContext(), "휴대폰번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!edit_password.getText().toString().equals(edit_rePassword.getText().toString())) {
            Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!Validator.isValidEmail(edit_email.getText().toString())) {
            Toast.makeText(getApplicationContext(), "올바른 형식의 이메일주소를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!Validator.isValidPhoneNumber(edit_phonenumber.getText().toString())) {
            Toast.makeText(getApplicationContext(), "올바른 형식의 휴대폰번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (edit_nickname.getText().toString().length() > 8) {
            Toast.makeText(getApplicationContext(), "8글자 이내로 닉네임을 정해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        setToolbarTitle("회원가입");
    }
}
