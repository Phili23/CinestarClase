package com.example.g102.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.g102.R;
import com.example.g102.models.User;
import com.example.g102.providers.AuthProviders;
import com.example.g102.providers.UsersProviders;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentSnapshot;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {

    //CREACIÓN DE OBJETOS DEL REGISTER =======================================================================
TextView mTextVierRegister;
    TextInputEditText mTextImputEditTextEmail;
TextInputEditText mTextImputEditTextPassword;
    Button mButtonLogin;
   AuthProviders mAuthProviders;
    SignInButton mbtngoogle;
    UsersProviders mUsersproviders;
    AlertDialog mDialog;

    private GoogleSignInClient mGoogleSignInClient;
    private final int REQUEST_CODE_GOOGLE=1;// este es el de los tokens




@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    //SEARCH ID===========================================================================================
        mTextVierRegister=findViewById(R.id.TextViewRegister);
        mTextImputEditTextEmail=findViewById(R.id.textInputEditTextEmail);
        mTextImputEditTextPassword=findViewById(R.id.textInputEditTextPassword);
        mButtonLogin=findViewById(R.id.btnlogin);
        mbtngoogle=findViewById(R.id.bntnLoginSignInGoogle);

        mAuthProviders=new AuthProviders();
        mUsersproviders=new UsersProviders();

      mDialog=new  SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento...")
                .setCancelable(false)
                .build();

        mbtngoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            signInGoogle();
            }
        });

    //CONFIGURE GOOGLE SIGN IN============================================================================
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build();

    mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


    //SING IN GOOGLE BUTTON================================================================================
        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });


    //TxtRegister INSTANCE================================================================================
        mTextVierRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,RegsiterActivity.class);
                startActivity(intent);
            }
        });
    }
    // [START signin]
private void signInGoogle(){
    Intent signIntent=mGoogleSignInClient.getSignInIntent();
    startActivityForResult(signIntent,REQUEST_CODE_GOOGLE);
}

    // [START onactivityresult]
@Override
public void  onActivityResult(int requesCode, int resulCode, Intent data){

    super.onActivityResult(requesCode,resulCode,data);
    if (requesCode==REQUEST_CODE_GOOGLE){
        Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            GoogleSignInAccount acount=task.getResult(ApiException.class);
            firebaseAuthWithGoogle(acount);
        }catch(ApiException e){
            Log.w("ERROR","Google sign in failed",e);
        }
    }
}

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
    mDialog.show();
        mAuthProviders.googleLogin(account)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            String id=mAuthProviders.getUid();//
                            checkUserExist(id);
                            // Sign in success, update UI with the signed-in user's information

                        } else {
                            mDialog.dismiss();
                            // If sign in fails, display a message to the user.
                            Log.w("ERROR", "signInWithCredential:failure", task.getException());

                        }
                    }
                });
    }

    private void checkUserExist(final String id) {

    mUsersproviders.getUser(id).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
        @Override
        public void onSuccess(DocumentSnapshot documentSnapshot) {
            mDialog.dismiss();
            if (documentSnapshot.exists()){
                Intent intent=new Intent(MainActivity.this,HomeActivity.class);
                startActivity(intent);//inicia la actividad
            }else{
                String email=mAuthProviders.getEmail();
                User user=new  User();
               user.setEmail(email);
               user.setId(id);

                //Map<String,Object> map=new HashMap<>();
               // map.put("email",email);
                mUsersproviders.create(user).
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mDialog.dismiss();
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
    //LOGIN METHOD ===============================================================================================================

    private void login() {
        String email = mTextImputEditTextEmail.getText().toString();
        String password = mTextImputEditTextPassword.getText().toString();
        mDialog.show();
        mAuthProviders.login(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mDialog.dismiss();
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
    }


}