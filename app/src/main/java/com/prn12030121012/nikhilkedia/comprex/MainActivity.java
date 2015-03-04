package com.prn12030121012.nikhilkedia.comprex;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.BreakIterator;
import java.util.BitSet;
import java.util.Objects;
import java.util.Random;

import static java.util.Objects.requireNonNull;

public class MainActivity extends Activity {

    private static final int SELECT_PICTURE = 1;

    private ImageView img;
    private Bitmap resized;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the message from the intent
        Intent intent = getIntent();
        String name = getIntent().getStringExtra("fn");
        // Create the text view
        TextView welcome = (TextView) findViewById(R.id.welcome);
        welcome.setText("Welcome " + name);

        // Set the text view as the activity layout


        img = (ImageView)findViewById(R.id.iv);

        findViewById(R.id.si)
                .setOnClickListener(new OnClickListener() {
                    public void onClick(View arg0) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                                SELECT_PICTURE);
                    }
                });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                String selectedImagePath = getPath(selectedImageUri);
                System.out.println("Image Path : " + selectedImagePath);
                img.setImageURI(selectedImageUri);
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public void compress(View view) throws IOException {
        img.buildDrawingCache();
        Bitmap bmap = img.getDrawingCache();
        Toast.makeText(getApplicationContext(),"Converted to Bitmap",Toast.LENGTH_SHORT).show();
        Bitmap resized = Bitmap.createScaledBitmap(bmap, 200, 200, true);
        Toast.makeText(getApplicationContext(),"Compressed",Toast.LENGTH_SHORT).show();
        saveToInternalSorage(resized);
    }

    public String saveToInternalSorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, "profile.jpg");

        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(mypath);

            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(getApplicationContext(), "Saved to Internal Storage !", Toast.LENGTH_SHORT).show();
        return directory.getAbsolutePath();

    }
    }
