package com.example.g102.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.g102.R;
import com.example.g102.models.User;
import com.example.g102.providers.AuthProviders;
import com.example.g102.providers.UsersProviders;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

import dmax.dialog.SpotsDialog;

public class CompleteProfileActivity extends AppCompatActivity {
    TextInputEditText mTextInputUsername;
    Button mButtonRegister;
    //FirebaseAuth mAuth;
    //FirebaseFirestore mFirestore;
    AuthProviders mAuthProviders;
    UsersProviders mUsersProviders;
    AlertDialog mDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile);
        mTextInputUsername=findViewById(R.id.TextInputEditTextUsernameC);
        mButtonRegister=findViewById(R.id.btnConfirmar);
       // mAuth=FirebaseAuth.getInstance();
       // mFirestore=FirebaseFirestore.getInstance();
        mAuthProviders=new AuthProviders();
        mUsersProviders=new UsersProviders();


        mDialog=new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento...")
                .setCancelable(false)
                .build();

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }

    private void register() {
        String username=mTextInputUsername.getText().toString();
        if (!username.isEmpty()){
            updateUser(username);
        }else{
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUser(final String username) {
        String id=mAuthProviders.getUid();
      //  Map<String,Object> map=new HashMap<>();
    //    map.put("Username", username);
        User user=new User();
        user.setUsername(username);
        user.setId(id);
        mDialog.show();
        mUsersProviders.update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mDialog.dismiss();
                if (task.isSuccessful()) {
                    Intent intent = new Intent(CompleteProfileActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);//metodo de limpiar las banderas
                    startActivity(intent);
                }else{
                    Toast.makeText(CompleteProfileActivity.this, "", Toast.LENGTH_SHORT).show();
                }

                }


        });
    }
}