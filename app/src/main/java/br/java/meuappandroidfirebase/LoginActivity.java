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

public class LoginActivity extends AppCompatActivity {

    private EditText txtEmailLogin;
    private EditText txtSenha;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtEmailLogin = (EditText) findViewById(R.id.txtEmailLogin);
        txtSenha = (EditText) findViewById(R.id.txtPasswordLogin);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void btnUsuarioEntrar_Clique(View v) {
        if (txtEmailLogin.length() == 0) {
            txtEmailLogin.setError("Digite seu endereço de email");

        } else if (txtSenha.length() == 0) {
            txtSenha.setError("Digite sua senha");

        } else {
            try {
                firebaseAuth.signInWithEmailAndPassword(txtEmailLogin.getText().toString(), txtSenha.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            toastMessage("Conectado com sucesso com: " + firebaseAuth.getCurrentUser().getEmail());

                            Intent intent = new Intent(LoginActivity.this, PerfilActivity.class);
                            intent.putExtra("Email", firebaseAuth.getCurrentUser().getEmail());
                            startActivity(intent);

                        } else {
                            Log.e("Error", task.getException().toString());
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (Exception e) {
                Toast.makeText(LoginActivity.this,  e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();

    }
}