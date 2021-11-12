package br.java.meuappandroidfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AtualizarPerfilAcrivity extends AppCompatActivity {

    private static final String TAG = "AtualizarPerfilAcrivity";

    private Button btnAtualizarPerfil;
    private EditText txtNome;
    private EditText txtIdade;
    private EditText txtEndereco;
    private EditText txtCep;
    private EditText txtFone;

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atualizar_perfil_acrivity);

        btnAtualizarPerfil = (Button) findViewById(R.id.btnUpdateProfile);

        txtNome = (EditText) findViewById(R.id.txtNameUpdateProfile);
        txtIdade = (EditText) findViewById(R.id.txtAgeUpdateProfile);
        txtEndereco = (EditText) findViewById(R.id.txtCityUpdateProfile);
        txtCep = (EditText) findViewById(R.id.txtPostalCodeUpdateProfile);
        txtFone = (EditText) findViewById(R.id.txtPhoneUpdateProfile);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in" + user.getUid());

                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    toastMessage("Sessão terminada com sucesso.");
                }
            }
        };

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object value = dataSnapshot.getValue();
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Falha ao ler o valor.", error.toException());
            }
        });

        btnAtualizarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick:Tentativa de adicionar objeto ao banco de dados.");

                String nome = txtNome.getText().toString();
                String idade = txtIdade.getText().toString();
                String endereco = txtEndereco.getText().toString();
                String cep = txtCep.getText().toString();
                String fone = txtFone.getText().toString();

                if (!nome.equals("")) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    String usuarioID = user.getUid();

                    myRef.child("users").child(usuarioID).child("nome").setValue(nome);
                    myRef.child("users").child(usuarioID).child("idade").setValue(idade);
                    myRef.child("users").child(usuarioID).child("endereco").setValue(endereco);
                    myRef.child("users").child(usuarioID).child("cep").setValue(cep);
                    myRef.child("users").child(usuarioID).child("fone").setValue(fone);

                    toastMessage("Perfil salvo");

                    txtNome.setText("");
                    txtIdade.setText("");
                    txtEndereco.setText("");
                    txtCep.setText("");
                    txtFone.setText("");
                }
            }
        });
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
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}