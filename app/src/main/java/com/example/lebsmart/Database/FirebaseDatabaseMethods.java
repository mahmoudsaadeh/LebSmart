package com.example.lebsmart.Database;

public class FirebaseDatabaseMethods {
/*
    // loading screen not showing here, so the class will not be used

    static Object objectInsert;
    public static DataSnapshot dataSnapshotFDM;

    public static void insertToDB (String[] children, final Object object, final Context context, String referenceInDB) {

        objectInsert = object;

        Log.i("insertToDB", "entered");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(referenceInDB);

        for (String child : children) {
            reference = reference.child(child);
        }

        reference.setValue(objectInsert).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.i("task insert data", "success");
                    if (objectInsert instanceof Apartment) {
                        Toast.makeText(context, "Apartment added successfully!", Toast.LENGTH_SHORT).show();
                    }
                    else if (objectInsert instanceof Meetings) {
                        Toast.makeText(context, "Meeting scheduled successfully!", Toast.LENGTH_SHORT).show();
                    }
                    else if (objectInsert instanceof Problem) {
                        Toast.makeText(context, "Problem reported successfully!", Toast.LENGTH_SHORT).show();
                    }
                    else if (objectInsert instanceof Thefts) {
                        Toast.makeText(context, "Theft reported successfully!", Toast.LENGTH_SHORT).show();
                    }
                    else if (objectInsert instanceof BSP) {
                        Toast.makeText(context, "Service provider added successfully!", Toast.LENGTH_SHORT).show();
                    }
                    else if (objectInsert instanceof CommitteeDR || objectInsert instanceof LFA) {
                        Toast.makeText(context, "Announcement added successfully!", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(context, "Failed to save your data! Please try again.", Toast.LENGTH_SHORT).show();
                    Log.i("task insert data", "failed");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });

    } // end method insertToDB


    public static DataSnapshot retrieveDataFromDB (String[] children, String refInDB, Context context) {
        // get data, save the in 'object', then return the object

        Log.i("reteivedataDromDb", "entered");
        //objectRetrieve = object;
        dataSnapshotFDM = null;

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(refInDB);

        for (String child : children) {
            reference = reference.child(child);
        }

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    //objectRetrieve = snapshot.getValue();
                    Log.i("snapshot", "exists");
                    dataSnapshotFDM = snapshot;

                    Log.i("snapshotValue", snapshot.getValue().toString());
                }
                else {
                    Log.i("snapshot", "doesn't exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("retrieveError", error.getMessage());
            }
        });


        //Log.i("datasnapshot Value", dataSnapshotFDM.getValue().toString());

        //return objectRetrieve;
        return dataSnapshotFDM;
    } // end method retrieveDataFromDB
*/
}
