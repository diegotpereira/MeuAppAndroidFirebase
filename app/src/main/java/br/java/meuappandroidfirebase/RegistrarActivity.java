package br.java.meuappandroidfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrarActivity extends AppCompatActivity {

    private static final String TAG = "RegistrarActivity";

    private EditText txtEmailEndereco;
    private EditText EdTxtSetEmailOnLogin;
    private EditText txtSenha;
    private FirebaseAuth firebaseAuth;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        txtEmailEndereco = (EditText) findViewById(R.id.txtEmailRegister);
        txtSenha = (EditText) findViewById(R.id.txtPasswordRegister);
        firebaseAuth = firebaseAuth.getInstance();

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

    }

    public void btnUsuarioRegistro_Clique(View v) {
        if (txtEmailEndereco.length() == 0) {
        txtEmailEndereco.setError("Digite seu endereço de email");

        }
        else if (txtSenha.length() == 0){
            txtSenha.setError("Enter your password");
        }
        else {
            (firebaseAuth.createUserWithEmailAndPassword(txtEmailEndereco.getText().toString(), txtSenha.getText().toString())).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        FirebaseUser user = mAuth.getCurrentUser();
                        String userID = user.getUid();
                        String email = user.getEmail();

                        DadosUsuario newUserProData = new DadosUsuario("", "", "Por favor digite seu nome", "", "");
                        myRef.child("users").child(userID).setValue(newUserProData);
                        myRef.child("users").child(userID).child("email").setValue(email);

                        Toast.makeText(RegistrarActivity.this, "Cadastrado com sucesso", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(RegistrarActivity.this, LoginActivity.class);
                        startActivity(intent);

                    } else {
                        Log.e("Error", task.getException().toString());
                        Toast.makeText(RegistrarActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();

    }
}