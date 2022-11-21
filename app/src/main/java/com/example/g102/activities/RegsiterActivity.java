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
import com.google.firebase.auth.AuthResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class RegsiterActivity extends AppCompatActivity {

CircleImageView mcircleImageViewBack;
TextInputEditText mTextInputEdiTextUsername;
TextInputEditText mTextInputEdiTextEmailR;
TextInputEditText mTextInputEdiTextPasswordR;
TextInputEditText mTextInputEdiTextConfirmPassword;
Button mButtonRegister;
//FirebaseAuth mAut;
//FirebaseFirestore mFirestore;
    AuthProviders mAutProvider;
    UsersProviders mUsersProvider;
    AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regsiter);

            //Instancias
        mcircleImageViewBack = findViewById(R.id.circleimageback);
        mTextInputEdiTextUsername=findViewById(R.id.textInputEditTextUsername);
        mTextInputEdiTextEmailR=findViewById(R.id.textInputEditTextEmailR);
        mTextInputEdiTextPasswordR=findViewById(R.id.textInputEditTextPasswordR);
        mTextInputEdiTextConfirmPassword=findViewById(R.id.textInputEditTextConfirmPassword);
        mButtonRegister=findViewById(R.id.btnregister);
        //mAut=FirebaseAuth.getInstance();
        //mFirestore=FirebaseFirestore.getInstance();


        mDialog=new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento...")
                .setCancelable(false)
                .build();

        mAutProvider=new AuthProviders();
        mUsersProvider= new UsersProviders();

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        mcircleImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }

        });

    }
//Metodo de Register
    private void register() {
        String  username=mTextInputEdiTextUsername.getText().toString();
        String email=mTextInputEdiTextEmailR.getText().toString();
        String password=mTextInputEdiTextPasswordR.getText().toString();
        String confirmPassword=mTextInputEdiTextConfirmPassword.getText().toString();

//0.54 min... AHI QUEDE
        if (!username.isEmpty()&& !email.isEmpty()&& !password.isEmpty() && !confirmPassword.isEmpty()){
            if (isEmailValid(email)){
                if(password.equals(confirmPassword)){
                    if(password.length() >=6){
                        createUser(username,email,password);
                    }else {
                        Toast.makeText(this, "Las contraseñas debe tener 6 caracteres", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                }

            }else {
                Toast.makeText(this, "Inserto todos los campos y el correo no es valido", Toast.LENGTH_SHORT).show();
            }


        }else {
            Toast.makeText(this, "para continuar inserta todos los campos", Toast.LENGTH_SHORT).show();
        }

    }

    private void createUser(String username,String email, String password) {
        mDialog.show();
       mAutProvider.register(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    String id=mAutProvider.getUid();
                    //Map<String,Object>  map =new HashMap<>();
                    //map.put("email",email);
                    //map.put("username",username);
                    //map.put("password",password);
                    User user=new User();
                    user.setId(id);
                    user.setEmail(email);
                    user.setUsername(username);
                    user.setPassword(password);
                    mUsersProvider.create(user)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mDialog.dismiss();
                            if (task.isSuccessful()){
                                Toast.makeText(RegsiterActivity.this, "El usuario se almaceno correctamente", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(RegsiterActivity.this,HomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);//metodo de limpiar las banderas
                                startActivity(intent);
                            }else{
                                Toast.makeText(RegsiterActivity.this, "EL usuario no fue almacenado", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    Toast.makeText(RegsiterActivity.this, "El usuario se registro correctamente", Toast.LENGTH_SHORT).show();
                }else{
                    mDialog.dismiss();
                    Toast.makeText(RegsiterActivity.this, "No se pudo hacer el registro", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }



    public boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    }