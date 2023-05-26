package Notes2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notespro2.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;

import org.w3c.dom.Text;

import java.util.SimpleTimeZone;

import Notes2.Note;
import Notes2.Utility;

public class NoteAdapter extends FirestoreRecyclerAdapter<Note, NoteAdapter.NoteViewHolder> {

    Context context;
    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options,Context ctx) {
        super(options);
        context = ctx;
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Note note) {
        holder.titleTextView.setText(note.title);
        holder.contentTextView.setText(note.content);
        holder.timeStampTextView.setText(Utility.timeStampToString(note.timestamp));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NotesDetailActivity.class);
                intent.putExtra("title",note.title);
                intent.putExtra("content",note.content);
                String docId = getSnapshots().getSnapshot(position).getId();
                intent.putExtra("docId",docId);
                context.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item_view,parent,false);
        return new NoteViewHolder(view);
    }

    class NoteViewHolder extends RecyclerView.ViewHolder{
        TextView titleTextView,contentTextView,timeStampTextView;

        public NoteViewHolder(@NonNull View itemView) {

            super(itemView);

            titleTextView = itemView.findViewById(R.id.note_title_txt_view);
            contentTextView = itemView.findViewById(R.id.note_content_txt_view);
            timeStampTextView = itemView.findViewById(R.id.note_timestamp_txt_view);
        }
    }
}
