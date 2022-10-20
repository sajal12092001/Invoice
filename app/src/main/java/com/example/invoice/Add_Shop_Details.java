package com.example.invoice;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.ByteArrayOutputStream;

public class Add_Shop_Details extends AppCompatActivity {
    Conn conn = new Conn(this);
    EditText gstno, shopname, shopaddress, city, district, pincode, mob1, mob2;
    Spinner state;
    Button setbutton, BSelectImage;

    // One Preview Image
    ImageView IVPreviewImage;
    // constant to compare
    // the activity result code

    Uri selectedImageUri;

    Bitmap selectedImageBitmap;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.MANAGE_EXTERNAL_STORAGE},
                PackageManager.PERMISSION_GRANTED);
        Cursor check = conn.get_shop_details();
        if (check.getCount() == 1) {
            Intent intent = new Intent(Add_Shop_Details.this, Add_Customer_Detail.class);
            startActivity(intent);
            finish();
        } else {
            setContentView(R.layout.shop_details);
            setTitle("Shop Details");

            gstno = findViewById(R.id.gstno);
            shopname = findViewById(R.id.shopname);
            shopaddress = findViewById(R.id.shopaddress);
            city = findViewById(R.id.city);
            district = findViewById(R.id.district);
            state = findViewById(R.id.state);
            pincode = findViewById(R.id.pincode);
            mob1 = findViewById(R.id.mob1);
            mob2 = findViewById(R.id.mob2);
            setbutton = findViewById(R.id.setbutton);

            BSelectImage = findViewById(R.id.BSelectImage);
            IVPreviewImage = findViewById(R.id.IVPreviewImage);

            BSelectImage.setOnClickListener(v -> imageChooser());

            setbutton.setOnClickListener(view -> {

                String gst = gstno.getText().toString().trim().toUpperCase();
                String name = shopname.getText().toString().trim().toUpperCase();
                String address = shopaddress.getText().toString().trim().toUpperCase();
                String tcity = city.getText().toString().trim().toUpperCase();
                String tdistrict = district.getText().toString().trim().toUpperCase();
                String tstate = state.getSelectedItem().toString().trim().toUpperCase();
                String tpincode = pincode.getText().toString().trim().toUpperCase();
                String tmob1 = mob1.getText().toString().trim().toUpperCase();
                String tmob2 = mob2.getText().toString().trim().toUpperCase();


                if (selectedImageBitmap != null) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    selectedImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    boolean check1 = conn.add_shop_details(gst, name, address, tcity, tdistrict, tstate, tpincode, tmob1, tmob2, out.toByteArray());
                    if (check1) {
                        Intent intent = new Intent(Add_Shop_Details.this, Add_Customer_Detail.class);
                        startActivity(intent);
                    }
                } else {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();

                    boolean check1 = conn.add_shop_details(gst, name, address, tcity, tdistrict, tstate, tpincode, tmob1, tmob2, out.toByteArray());
                    if (check1) {
                        Intent intent = new Intent(Add_Shop_Details.this, Add_Customer_Detail.class);
                        startActivity(intent);
                    }

                }
            });
        }


    }

//    void imageChooser() {
//
//        // create an instance of the
//        // intent of the type image
//        Intent i = new Intent();
//        i.setType("image/*");
//        i.setAction(Intent.ACTION_GET_CONTENT);
//
//        // pass the constant to compare it
//        // with the returned requestCode
//        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
//    }
//
//    // this function is triggered when user
//    // selects the image from the imageChooser
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == RESULT_OK) {
//
//            // compare the resultCode with the
//            // SELECT_PICTURE constant
//            if (requestCode == SELECT_PICTURE) {
//                // Get the url of the image from data
//                selectedImageUri = data.getData();
//                if (null != selectedImageUri) {
//                    // update the preview image in the layout
//                    IVPreviewImage.setImageURI(selectedImageUri);
//                }
//            }
//        }
//    }


    /*

    Alternative Code: In case: startActivityForResult is deprecated

     */

    private void imageChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        launchSomeActivity.launch(i);
    }

    ActivityResultLauncher<Intent> launchSomeActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            selectedImageBitmap = null;
            // do your operation from here....
            if (data != null && data.getData() != null) {

                selectedImageUri = data.getData();

                try {
                    selectedImageBitmap = (Bitmap) MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                IVPreviewImage.setImageBitmap(
                        selectedImageBitmap);
            }
        }
    });

}