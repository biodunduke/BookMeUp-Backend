/**
 * Team LastMinSub:
 * Abiodun Ojo
 * David Uche
 * Elias Sabbagh
 * <p>
 * The purpose of this class is to give the user the ability to add a new listing(book) to
 * the database. The data here is written to the backend
 */


package com.humber.bookmeup.views;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.esafirm.imagepicker.features.ImagePicker;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.humber.bookmeup.R;
import com.humber.bookmeup.models.Book;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;

import static android.app.Activity.RESULT_OK;

public class NewListingFragment extends Fragment {
    // Store instance variables
    private String title;
    private String name, email, uid, providerId = "";
    private int page;
    EditText bookName, bookAuthor, bookCondition, bookP, bookISBN;
    StorageReference storageRef, picsRef, bookPicImageRef;
    UploadTask uploadTask;

    public String api = "http://booktemp.herokuapp.com";

    ImagePicker imagePicker;
    // Download Uri to store the uri for the image when it finishes uploading to cloud storage

    Uri downloadUri;
    //Store uri as string for database write

    String downloadUriStr = "";

    //Define storage for backend

    FirebaseStorage storage = FirebaseStorage.getInstance();

    public NewListingFragment() {

    }

    public static NewListingFragment newInstance(int page, String title) {
        NewListingFragment fragmentFirst = new NewListingFragment();
        Bundle args = new Bundle();
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //Get uri for image selected and replace the default image with it
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1000) {
                //get uri
                Uri returnUri = data.getData();
                try {
                    ImageView image = getView().findViewById(R.id.imageView3);
                    //obtain image from gallery
                    Bitmap bitmapImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), returnUri);
                    //set as new image in the imageview
                    image.setImageBitmap(bitmapImage);
                } catch (Exception e) {
                    System.out.print(e);
                }
            }
        }
        Uri returnUri;
        returnUri = data.getData();
    }


    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_listing, container, false);
        bookName = view.findViewById(R.id.addBookName);
        bookAuthor = view.findViewById(R.id.addBookAuthor);
        bookCondition = view.findViewById(R.id.addCondition);
        bookP = view.findViewById(R.id.addPrice);
        bookISBN = view.findViewById(R.id.addISBN);
        Button imageBtn = view.findViewById(R.id.addImage);
        Button submitBtn = view.findViewById(R.id.submitBtn);
        ImageView image = view.findViewById(R.id.imageView3);

        /**We need to know which user is uploading new information*/
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //TODO:Do validations here
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                uid = profile.getUid();
                name = profile.getDisplayName();
            }
        }

        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getActivity(),
                        android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(
                            new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                            2000);
                } else {
                    startGallery();
                }

            }

        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String error = "";
                // Create a storage reference from our app
                try {
                    if (!textLength(bookP.getText().toString())) {
                        bookP.setError(getString(R.string.invalid));
                        bookP.requestFocus();
                        error = getString(R.string.invalid);
                    }
                    if (!textLength(bookCondition.getText().toString())) {
                        bookCondition.setError(getString(R.string.invalid));
                        bookCondition.requestFocus();
                        error = getString(R.string.invalid);
                    }
                    if (!textLength(bookAuthor.getText().toString())) {
                        bookAuthor.setError(getString(R.string.invalid));
                        bookAuthor.requestFocus();
                        error = getString(R.string.invalid);
                    }
                    if (!textLength(bookISBN.getText().toString())) {
                        bookISBN.setError(getString(R.string.invalid));
                        bookISBN.requestFocus();
                        error = getString(R.string.invalid);
                    }
                    if (!textLength(bookName.getText().toString())) {
                        bookName.setError(getString(R.string.invalid));
                        bookName.requestFocus();
                        error = getString(R.string.invalid);
                    }
                    if (!error.equals(R.string.invalid)) {
                        try {
                            storageRef = storage.getReference();
                            picsRef = storageRef.child(bookISBN.getText().toString());
                            bookPicImageRef = storageRef.child(getString(R.string.pathImages) +
                                    "/" + getString(R.string.pathUsers) + "/" + uid + "/" + bookISBN.getText().toString());
                            // While the file names are the same, the references point to different files
                            picsRef.getName().equals(bookPicImageRef.getName());    // true
                            picsRef.getPath().equals(bookPicImageRef.getPath());    // false
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                        //HERE
                        // Get the data from an ImageView as bytes
                        image.setDrawingCacheEnabled(true);
                        image.buildDrawingCache();
                        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();


                        //upload the image to cloud storage
                        uploadTask = picsRef.putBytes(data);
                    }

                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            //Toast.makeText(getActivity(),exception.toString(),Toast.LENGTH_LONG).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //Toast.makeText(getActivity(),taskSnapshot.toString(),Toast.LENGTH_LONG).show();
                            /**Obtain the download uri to store in the backend so that we can view the images later on.*/
                            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    if (!task.isSuccessful()) {
                                        throw task.getException();
                                    }
                                    //Toast.makeText(getActivity(),storageRef.child(bookISBN.getText().toString()).getDownloadUrl().toString(),Toast.LENGTH_LONG).show();
                                    return storageRef.child(bookISBN.getText().toString()).getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        downloadUri = task.getResult();
                                        downloadUriStr = downloadUri.toString();
                                        Toast.makeText(getActivity(), getString(R.string.uploadSuccess), Toast.LENGTH_LONG).show();
                                        /**Create new book instance to send to the api*/
                                        Book book = new Book();
                                        //Validation
                                        try {
                                            book.bookName = bookName.getText().toString();
                                            book.bookAuthor = bookAuthor.getText().toString();
                                            book.bookISBN = bookISBN.getText().toString();
                                            book.bookCondition = bookCondition.getText().toString();
                                            book.userId = uid;
                                            book.bookPrice = Integer.valueOf(bookP.getText().toString());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        //Clearing the text fields
                                        bookName.setText("");
                                        bookAuthor.setText("");
                                        bookCondition.setText("");
                                        bookP.setText("");
                                        bookISBN.setText("");
                                        book.bookPicUrl = downloadUriStr;

                                        /**Convert book to Json and add it to the database. We also
                                         * store a reference to the storage bucket here for fetching in the listings
                                         * and profile page view*/
                                        AndroidNetworking.post(api + "/api/ads")
                                                .addApplicationJsonBody(book)
                                                .build()
                                                .getAsJSONArray(new JSONArrayRequestListener() {
                                                    @Override
                                                    public void onResponse(JSONArray response) {
                                                        //  Toast.makeText(getActivity(),response.toString(),Toast.LENGTH_LONG).show();
                                                    }

                                                    @Override
                                                    public void onError(ANError error) {
                                                        // handle error
                                                    }
                                                });
                                        //TODO: Reset state here

                                    } else {
                                        //TODO: Add else logic
                                    }
                                }
                            });
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }

    /**
     * Open the andoid gallery to select an image
     */
    private void startGallery() {
        Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        cameraIntent.setType(getString(R.string.image) + "/*");
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(cameraIntent, 1000);
        }
    }

    public boolean textLength(String text) {
        if (text.isEmpty() || text.length() < 3)
            return false;
        else
            return true;
    }

}