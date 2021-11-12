package br.java.meuappandroidfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PerfilActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "PerfilActivity";

    public TextView textViewEmail;
    public TextView txtEmailP;
    public TextView txtNomeP;

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;

    private String usuarioID;
    private ListView mListaViewP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        mListaViewP = (ListView) findViewById(R.id.ProfileListViewP);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        usuarioID = user.getUid();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                } else {
                    Log.d(TAG, "onAuthStateChanged: signed_out");
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "In development mode :| Contact RILEYGHOST", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        textViewEmail = (TextView) findViewById(R.id.userEmailProfile);
        textViewEmail.setText(getIntent().getExtras().getString("Email"));
    }

    private void exibirDados(DataSnapshot dataSnapshot) {
        for(DataSnapshot ds : dataSnapshot.getChildren()) {
            InformacoesPerfilUsuario uInfo = new InformacoesPerfilUsuario();
            uInfo.setNome(ds.child(usuarioID).getValue(InformacoesPerfilUsuario.class).getNome());
            uInfo.setIdade(ds.child(usuarioID).getValue(InformacoesPerfilUsuario.class).getIdade());
            uInfo.setFone(ds.child(usuarioID).getValue(InformacoesPerfilUsuario.class).getFone());
            uInfo.setCidade(ds.child(usuarioID).getValue(InformacoesPerfilUsuario.class).getEndereco());
            uInfo.setCep(ds.child(usuarioID).getValue(InformacoesPerfilUsuario.class).getCep());

            Log.d(TAG, "exibirDados: nome: " + uInfo.getNome());
            Log.d(TAG, "exibirDados: idade: " + uInfo.getIdade());
            Log.d(TAG, "exibirDados: fone: " + uInfo.getFone());
            Log.d(TAG, "exibirDados: endereco: " + uInfo.getEndereco());
            Log.d(TAG, "exibirDados: cep: " + uInfo.getCep());

            ArrayList<String> array = new ArrayList<>();
            array.add("Nome : " + uInfo.getNome());
            array.add("Aniversario : " + uInfo.getIdade());
            array.add("Telefone : " + uInfo.getFone());
            array.add("Endereco : " + uInfo.getEndereco());
            array.add("Cep : " + uInfo.getCep());

            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, array);
            mListaViewP.setAdapter(adapter);

            txtNomeP = (TextView) findViewById(R.id.txtNomeP);
            txtNomeP.setText(uInfo.getNome());
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);

        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        txtEmailP = (TextView) findViewById(R.id.txtEmailP);
        txtEmailP.setText(getIntent().getExtras().getString("Email"));

        getMenuInflater().inflate(R.menu.perfil, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent intent = new Intent(PerfilActivity.this, MeuPerfilActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(PerfilActivity.this, AtualizarPerfilAcrivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            Intent intent = new Intent(PerfilActivity.this, TesteDado.class);
            startActivity(intent);
        }
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
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