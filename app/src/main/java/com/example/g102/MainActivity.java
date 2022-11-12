package com.example.g102;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
TextView mTextVierRegister;
    TextInputEditText mTextImputEditTextEmail;
TextInputEditText mTextImputEditTextPassword;
    Button mButtonLogin;
    FirebaseAuth mAuth;
    Button mbtngoogle;
    FirebaseFirestore mFirestore;


    private GoogleSignInClient mGoogleSignInClient;
    private final int REQUEST_CODE_GOOGLE=1;




@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextVierRegister=findViewById(R.id.TextViewRegister);
        mTextImputEditTextEmail=findViewById(R.id.textInputEditTextEmail);
        mTextImputEditTextPassword=findViewById(R.id.textInputEditTextPassword);
        mButtonLogin=findViewById(R.id.btnlogin);
        mbtngoogle=findViewById(R.id.bntnLoginSignInGoogle);

        mAuth=FirebaseAuth.getInstance();

        mbtngoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build();

    mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });



        mTextVierRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,RegsiterActivity.class);
                startActivity(intent);
            }
        });
    }

private void signInGoogle(){
    Intent signIntent=mGoogleSignInClient.getSignInIntent();
    startActivityForResult(signIntent,REQUEST_CODE_GOOGLE);
}

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String id=mAuth.getCurrentUser().getUid();
                            checkUserExist(id);
                            // Sign in success, update UI with the signed-in user's information

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("ERROR", "signInWithCredential:failure", task.getException());

                        }
                    }
                });
    }

    private void checkUserExist(final String id) {
    mFirestore.collection("Users").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
        @Override
        public void onSuccess(DocumentSnapshot documentSnapshot) {
            if (documentSnapshot.exists()){
                Intent intent=new Intent(MainActivity.this,HomeActivity.class);
                startActivity(intent);//inicia la actividad
            }else{
                String email=mAuth.getCurrentUser().getEmail();
                Map<String,Object> map=new HashMap<>();
                map.put("email",email);
                mFirestore.collection("Users").document(id).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Intent intent=new Intent(MainActivity.this,HomeActivity.class);
                            startActivity(intent);
                        }else {
                            Toast.makeText(MainActivity.this, "No se pudo almacenar el usuario", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                Toast.makeText(MainActivity.this, "No se pudo almacenar, la inforacion del Usuario", Toast.LENGTH_SHORT).show();
            }

        }
    });


    }


    private void login() {
        String email = mTextImputEditTextEmail.getText().toString();
        String password = mTextImputEditTextPassword.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Email y Contraseña incorrectas", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Log.d("campo", "email" + email);
        Log.d("campo", "pasword" + password);
    }}