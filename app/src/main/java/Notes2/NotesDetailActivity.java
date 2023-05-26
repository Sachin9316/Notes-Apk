package Notes2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notespro2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class NotesDetailActivity extends AppCompatActivity {

    ImageButton save;
    TextView delete, pageTitle;
    EditText titletxt, contenttxt;
    ProgressDialog dialog,dialogDelete;
    String docId;

    boolean isInEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_detail);
        save = findViewById(R.id.savebtn);
        titletxt = findViewById(R.id.title_txt);
        contenttxt = findViewById(R.id.content_text);
        delete = findViewById(R.id.deleteBtn);
        pageTitle = findViewById(R.id.page_title_txt_view);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Saving");
        dialog.setCancelable(false);
        dialog.setIcon(R.drawable.saving);

        dialogDelete = new ProgressDialog(this);
        dialogDelete.setMessage("Deleting");
        dialogDelete.setCancelable(false);

        String title = getIntent().getStringExtra("title");
        String content = getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");

        if (docId != null && !docId.isEmpty()) {
            isInEditMode = true;
        }

        titletxt.setText(title);
        contenttxt.setText(content);

        if (isInEditMode) {
            pageTitle.setText("Edit your note");
            delete.setVisibility(View.VISIBLE);
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titletxt.getText().toString();
                String content = contenttxt.getText().toString();

                if (titletxt == null || title.isEmpty()) {
                    Toast.makeText(NotesDetailActivity.this, "title should not be empty", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    dialog.show();
                }
                savenote(title, content);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (titletxt == null && title.isEmpty()){
                    Toast.makeText(NotesDetailActivity.this, "No data for delete", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    dialogDelete.show();
                }
                deleteNoteFromFirebase();
            }
        });



    }

    private void deleteNoteFromFirebase() {
        DocumentReference reference;
        reference = Utility.collectionReference().document(docId);

        reference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                dialogDelete.dismiss();
                if (task.isSuccessful()){
                    Toast.makeText(NotesDetailActivity.this, "Deleted successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(NotesDetailActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void savenote(String title, String content) {

        if (title == null || title.isEmpty()) {
            titletxt.setError("Title Required");
            return;
        }
        Note note = new Note();
        note.setTitle(title);
        note.setContent(content);
        note.setTimestamp(Timestamp.now());
        saveNoteToFirebase(note);
    }

    private void saveNoteToFirebase(Note note) {
        DocumentReference documentReference;
        if (isInEditMode){
            documentReference = Utility.collectionReference().document(docId);
        }else{
            documentReference = Utility.collectionReference().document();
        }

        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                dialog.dismiss();
                if (task.isComplete()) {
                    Toast.makeText(NotesDetailActivity.this, "Note Saved", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(NotesDetailActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}