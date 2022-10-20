package com.example.invoice;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;

public class Update_shop_details extends AppCompatActivity {

    Conn conn = new Conn(this);
    EditText gstno, shopname, shopaddress, city, district, pincode, mob1, mob2;
    Spinner state;
    Button setbutton, BSelectImage, BdeleteImage;

    // One Preview Image
    ImageView IVPreviewImage;
    // constant to compare
    // the activity result code

    Uri selectedImageUri;

    Bitmap selectedImageBitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_shop_details);
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
        BdeleteImage = findViewById(R.id.BdeleteImage);

        BSelectImage.setOnClickListener(v -> imageChooser());

        BdeleteImage.setOnClickListener(v ->
        {
            selectedImageBitmap=null;
            IVPreviewImage.setImageBitmap(null);
        });

        List<String> your_array = Arrays.asList(getResources().getStringArray(R.array.state));


        Cursor getshopdetails_cursor = conn.get_shop_details();
        if (getshopdetails_cursor.moveToFirst()) {
            gstno.setText(getshopdetails_cursor.getString(1));
            shopname.setText(getshopdetails_cursor.getString(2));
            shopaddress.setText(getshopdetails_cursor.getString(3));
            city.setText(getshopdetails_cursor.getString(4));
            district.setText(getshopdetails_cursor.getString(5));
            state.setSelection(your_array.indexOf(getshopdetails_cursor.getString(6)));
            pincode.setText(getshopdetails_cursor.getString(7));

            mob1.setText(getshopdetails_cursor.getString(8));

            mob2.setText(getshopdetails_cursor.getString(9));

            if (getshopdetails_cursor.getBlob(10).length != 0) {
                getBSelectImage();
            }
        }


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
                conn.update_shop_details(gst, name, address, tcity, tdistrict, tstate, tpincode, tmob1, tmob2, out.toByteArray());
            } else {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                conn.update_shop_details(gst, name, address, tcity, tdistrict, tstate, tpincode, tmob1, tmob2, out.toByteArray());

            }


            Intent intent = new Intent(getApplicationContext(), Items_Recyclerview.class);
            startActivity(intent);
            finish();

        });
    }

    public void getBSelectImage() {
        Cursor cursor = conn.get_shop_details();
        if (cursor.moveToFirst()) {
            Bitmap getImageBitmap = BitmapFactory.decodeByteArray(cursor.getBlob(10), 0, cursor.getBlob(10).length);
            ByteArrayOutputStream bm = new ByteArrayOutputStream();
            getImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, bm);
            IVPreviewImage.setImageBitmap(getImageBitmap);
        }


    }

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