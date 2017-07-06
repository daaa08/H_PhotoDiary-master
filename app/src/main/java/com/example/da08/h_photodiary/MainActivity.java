package com.example.da08.h_photodiary;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.da08.h_photodiary.util.PermissionControl;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements PermissionControl.CallBack{

    EditText editEmail, editPw;
    Button btnSignIn, btnSignUp;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();  // 인증처리를 하기 위한 것
    private FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                Log.d("Auth", "onAuthStateChanged:signed_in:" + user.getUid());
            } else {
                Log.d("Auth", "onAuthStateChanged:signed_out");
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnSignIn = (Button)findViewById(R.id.btnSignIn);
        btnSignUp = (Button)findViewById(R.id.btnSignUp);

        PermissionControl.checkPermission(this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionControl.onResult(this, requestCode, grantResults);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void signUp(View v){

        String email = editEmail.getText().toString();
        String pw = editPw.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, pw)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Auth", "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "사용자 등록이되지 않았습니다",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MainActivity.this, "사용자 등록이되었습니다.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    public void signIn(View v){
        String email = editEmail.getText().toString();
        String pw = editPw.getText().toString();

        mAuth.signInWithEmailAndPassword(email, pw)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Auth", "signInWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.w("Auth", "signInWithEmail:failed", task.getException());
                            Toast.makeText(MainActivity.this, "로그인이 되지 않았습니다",
                                    Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(MainActivity.this, "로그인 되었습니다",
                                    Toast.LENGTH_SHORT).show();

                            goList();
                        }
                    }
                });
    }

    public void goList(){
        Intent intent = new Intent(this, ListActivity.class);
       startActivity(intent);
        finish();
    }

    @Override
    public void init() {
        editEmail = (EditText)findViewById(R.id.editEmail);
        editPw = (EditText)findViewById(R.id.editPw);

    }
}
