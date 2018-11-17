package com.humber.bookmeup;

import android.app.Activity;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
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

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class NewListingFragment extends Fragment {
    // Store instance variables
    private String title;
    private String name,email,uid,providerId = "";
    private int page;
    public String api = "http://bb57afc5.ngrok.io";
    ImagePicker imagePicker;
    Uri downloadUri;
    String downloadUriStr = "";
    FirebaseStorage storage = FirebaseStorage.getInstance();

    // newInstance constructor for creating fragment with arguments
    public static NewListingFragment newInstance(int page, String title) {
        NewListingFragment fragmentFirst = new NewListingFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            if(requestCode == 1000){
                Uri returnUri = data.getData();
                try {
                    ImageView image = getView().findViewById(R.id.imageView3);
                    Bitmap bitmapImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), returnUri);
                    image.setImageBitmap(bitmapImage);

                }catch (Exception e){
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
        EditText bookName = view.findViewById(R.id.addBookName);
        EditText bookAuthor = view.findViewById(R.id.addBookAuthor);
        EditText bookCondition = view.findViewById(R.id.addCondition);
        EditText bookP = view.findViewById(R.id.addPrice);
        EditText bookISBN = view.findViewById(R.id.addISBN);
        Button imageBtn = view.findViewById(R.id.addImage);
        Button submitBtn = view.findViewById(R.id.submitBtn);
        ImageView image = view.findViewById(R.id.imageView3);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                uid = profile.getUid();
                name = profile.getDisplayName();
            }
        }

        //TODO: ADD COMMENTS
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
                //validateFields(){}
                // Create a storage reference from our app
                StorageReference storageRef = storage.getReference();
                StorageReference mountainsRef = storageRef.child(bookISBN.getText().toString());
                StorageReference mountainImagesRef = storageRef.child("images/users/"+uid+"/"+bookISBN.getText().toString());
                // While the file names are the same, the references point to different files
                mountainsRef.getName().equals(mountainImagesRef.getName());    // true
                mountainsRef.getPath().equals(mountainImagesRef.getPath());    // false
                // Get the data from an ImageView as bytes
                image.setDrawingCacheEnabled(true);
                image.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = mountainsRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Toast.makeText(getActivity(),exception.toString(),Toast.LENGTH_LONG).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                      Toast.makeText(getActivity(),taskSnapshot.toString(),Toast.LENGTH_LONG).show();
                        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }
                                Toast.makeText(getActivity(),storageRef.child(bookISBN.getText().toString()).getDownloadUrl().toString(),Toast.LENGTH_LONG).show();
                                return storageRef.child(bookISBN.getText().toString()).getDownloadUrl();

                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    downloadUri = task.getResult();
                                    downloadUriStr=downloadUri.toString();
                                    Toast.makeText(getActivity(),"download UI:"+downloadUri.toString(),Toast.LENGTH_LONG).show();
                                    Book book = new Book();
                                    book.bookName = bookName.getText().toString();
                                    book.bookAuthor = bookAuthor.getText().toString();
                                    book.bookISBN = bookISBN.getText().toString();
                                    book.bookCondition = bookCondition.getText().toString();
                                    book.userId = uid;
                                    book.bookPrice = Integer.valueOf(bookP.getText().toString());
                                    book.bookPicUrl = downloadUriStr;

                                    AndroidNetworking.post(api+"/api/ads")
                                            .addApplicationJsonBody(book)
                                            .build()
                                            .getAsJSONArray(new JSONArrayRequestListener() {
                                                @Override
                                                public void onResponse(JSONArray response) {
                                                    // do anything with response
                                                    //  Toast.makeText(getActivity(),response.toString(),Toast.LENGTH_LONG).show();
                                                }
                                                @Override
                                                public void onError(ANError error) {
                                                    // handle error
                                                    // Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG).show();
                                                }
                                            });

                                } else {}
                            }
                        });
                    }
                });



            }
        });
        return view;
    }




    private void startGallery() {
        Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        cameraIntent.setType("image/*");
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(cameraIntent, 1000);
        }
    }


}