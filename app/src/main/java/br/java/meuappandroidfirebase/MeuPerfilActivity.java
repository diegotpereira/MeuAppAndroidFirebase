package br.java.meuappandroidfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MeuPerfilActivity extends AppCompatActivity {

    private static final String TAG = "MeuPerfilActivity";

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;

    private String usuarioID;

    private ListView mListaView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meu_perfil);

        mListaView1 = (ListView) findViewById(R.id.myProfileListView1);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        usuarioID =user.getUid();

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
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                exibirDados(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                toastMessage("Erro ao conectar ao banco de dados");
            }
        });
    }

    private void exibirDados(DataSnapshot dataSnapshot) {
        for(DataSnapshot ds : dataSnapshot.getChildren()) {

            InformacoesPerfilUsuario uInfo = new InformacoesPerfilUsuario();
            uInfo.setNome(ds.child(usuarioID).getValue(InformacoesPerfilUsuario.class).getNome());
            uInfo.setIdade(ds.child(usuarioID).getValue(InformacoesPerfilUsuario.class).getIdade());
            uInfo.setFone(ds.child(usuarioID).getValue(InformacoesPerfilUsuario.class).getFone());
            uInfo.setCep(ds.child(usuarioID).getValue(InformacoesPerfilUsuario.class).getCep());
            uInfo.setEmail(ds.child(usuarioID).getValue(InformacoesPerfilUsuario.class).getEmail());

            Log.d(TAG, "exibirDados: nome: " + uInfo.getNome());
            Log.d(TAG, "exibirDados: idade: " + uInfo.getIdade());
            Log.d(TAG, "exibirDados: fone: " + uInfo.getFone());
            Log.d(TAG, "exibirDados: endereco: " + uInfo.getEndereco());
            Log.d(TAG, "exibirDados: cep: " + uInfo.getCep());
            Log.d(TAG, "exibirDados: email: " + uInfo.getEmail());

            ArrayList<String> array = new ArrayList<>();
            array.add("Nome :" + uInfo.getNome());
            array.add("Email :" + uInfo.getEmail());
            array.add("Nascimento :" + uInfo.getIdade());
            array.add("Fone :" + uInfo.getFone());
            array.add("Endereco :" + uInfo.getEndereco());
            array.add("Cep :" + uInfo.getCep());

            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, array);
            mListaView1.setAdapter(adapter);
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message,  Toast.LENGTH_SHORT).show();
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
}