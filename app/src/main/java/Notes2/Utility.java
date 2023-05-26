package Notes2;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {

    static CollectionReference collectionReference(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("notes").document(firebaseUser.getUid()).collection("my_notes");
    }

    static String timeStampToString(Timestamp timestamp){
        return new SimpleDateFormat("dd/MM/yyyy").format(new Date());
    }

}
