package br.java.meuappandroidfirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TesteDado extends AppCompatActivity {

    private static final String TAG = "TesteDado";

    private DatabaseReference databaseReference;

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teste_dado);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

    }

    public void inserirNoClique(View v) {
        FirebaseUser user = mAuth.getCurrentUser();
        String usuarioID = user.getUid();
        String email = user.getEmail();

        DadosUsuario dadosUsuario = new DadosUsuario("Atualize seu endreço", "Atualize seu aniversário", "Atualize seu telefone", "Atualize seu cep", "Atualize seu nome");
        myRef.child("users").child(usuarioID).setValue(dadosUsuario);
        myRef.child("users").child(usuarioID).child("email").setValue(email);
    }
}