package com.example.vidiic.proyecto_music.fragments.social.chat;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.vidiic.proyecto_music.R;
import com.example.vidiic.proyecto_music.adapters.AddUserAdapter;
import com.example.vidiic.proyecto_music.classes.UserApp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import at.markushi.ui.CircleButton;

public class AddFriendActivity extends AppCompatActivity {

    private CircleButton search_user_btn;
    private EditText user_key;
    private RecyclerView rv_show_user;
    private AddUserAdapter addUserAdapter;
    private List<UserApp> requested_users;
    private FirebaseFirestore firebaseFirestore;
    private UserApp userAux;
    private String current_user_id;
    private FirebaseAuth firebaseAuth;
    private Toolbar toolbar_add_friend;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);


        search_user_btn = findViewById(R.id.search_user_btn);
        user_key = findViewById(R.id.username_et);
        rv_show_user = findViewById(R.id.recycler_view_add_user);
        requested_users = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        relativeLayout = findViewById(R.id.relative_edit_text_user);
        toolbar_add_friend = findViewById(R.id.toolbar_add_friend);

        setSupportActionBar(toolbar_add_friend);

        getSupportActionBar().setTitle(R.string.AddFriendToolbarText);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        current_user_id = firebaseAuth.getCurrentUser().getUid();


        firebaseFirestore = FirebaseFirestore.getInstance();

        relativeLayout.setVisibility(View.VISIBLE);


        user_key.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0 && addUserAdapter != null) {
                    requested_users.clear();
                    rv_show_user.setAdapter(null);
                    Log.d("add_friend", "sin texto");
                    addUserAdapter.notifyDataSetChanged();
                    getRequestUser();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                //la variable que recibimos en esta funcino es lo que el usuario va escribienodo
                //lo pasamos a una funcion que devolvera los resultados que coinciden con la busqueda del usuario

            }
        });

        getRequestUser();


        search_user_btn.setOnClickListener(v -> {

            if (!user_key.getText().toString().equals("")) {

                //obtenemos los nombres de usuarios que empiecen por lo que ha introducido el usuario y los pasamos al adapatador

                addUserAdapter = new AddUserAdapter(getUsersByKey(requested_users, user_key.getText().toString()));

                RecyclerView.LayoutManager layout = new LinearLayoutManager(this);

                rv_show_user.setLayoutManager(layout);

                rv_show_user.setAdapter(addUserAdapter);
            } else {
                Toast.makeText(this, "Escribe algo.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }

    private void getRequestUser() {
        firebaseFirestore.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot snapshot : task.getResult()) {
                        userAux = snapshot.toObject(UserApp.class);
                        if (!userAux.getUserid().equals(current_user_id)) {
                            Log.d("request_users", "nombre user" + userAux.getUserName());
                            requested_users.add(userAux);
                        }
                    }
                }
            }
        });
    }

    private List<UserApp> filtered_user_list;

    private List<UserApp> getUsersByKey(List<UserApp> user_list, String key) {

        filtered_user_list = new ArrayList<>();

        for (UserApp u : user_list) {
            if (u.getUserName().contains(key)) filtered_user_list.add(u);
        }

        return filtered_user_list;
    }
}
