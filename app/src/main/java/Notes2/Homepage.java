package Notes2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.notespro2.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;

public class Homepage extends AppCompatActivity {

    FloatingActionButton floatingActionButton;
    RecyclerView recyclerView;
    ImageButton menu;
    ProgressDialog logoutDialog;
    NoteAdapter noteAdapter;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        floatingActionButton = findViewById(R.id.floatingbtn);
        recyclerView = findViewById(R.id.recyclerView);
        menu = findViewById(R.id.menubtn);
        auth = FirebaseAuth.getInstance();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Homepage.this, NotesDetailActivity.class));
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String logout = "Logout";
                PopupMenu popupMenu = new PopupMenu(getApplicationContext(),menu);
                popupMenu.getMenu().add("Logout");
                popupMenu.show();

                logoutDialog = new ProgressDialog(Homepage.this);
                logoutDialog.setTitle("Logging Out");
                logoutDialog.setMessage("Please wait");
                logoutDialog.setCancelable(false);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        logoutDialog.show();
                        if (item.getTitle() == logout){
                            logoutDialog.dismiss();
                            auth.signOut();
                            startActivity(new Intent(Homepage.this, LoginScreen.class));
                            Toast.makeText(Homepage.this, "Logged-Out successfully", Toast.LENGTH_SHORT).show();
                            finish();
                            return true;
                        }else {
                            Toast.makeText(Homepage.this, "Unable to LogOut", Toast.LENGTH_SHORT).show();
                        }
                        return false;
                    }
                });
            }
        });

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        Query query = Utility.collectionReference().orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>().setQuery(query, Note.class).build();

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        noteAdapter = new NoteAdapter(options,this);
        recyclerView.setAdapter(noteAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        noteAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        noteAdapter.notifyDataSetChanged();
    }
}