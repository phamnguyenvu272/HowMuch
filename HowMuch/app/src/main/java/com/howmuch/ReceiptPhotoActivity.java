package com.howmuch;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.ml.vision.text.FirebaseVisionText;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

public class ReceiptPhotoActivity extends AppCompatActivity  implements DatePickerDialog.OnDateSetListener {

    private int year;
    private int monthOfYear;
    private int dayOfMonth;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap imageBitmap;
    private String pictureImagePath = "";
    private boolean newPhoto;
    private EditText txtReceiptPhotoTotal;
    private EditText txtReceiptPhotoDescription;
    private Button btnSave;
    private Button btnRetake;
    private Spinner spnCategories;
    private Manager dm;
    private TextInputEditText txtDate;
    private DatePickerDialog datePicker;

    private TextRecogn txtRec;

    private ArrayList<String> results = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_photo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dm = Manager.getManager();
        newPhoto = false;
        dispatchTakePictureIntent();
        txtReceiptPhotoTotal = findViewById(R.id.txtReceiptPhotoTotal);
        txtReceiptPhotoDescription = findViewById(R.id.txtReceiptPhotoDescription);
        btnRetake = findViewById(R.id.btnRetakePhoto);
        btnRetake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        btnSave = findViewById(R.id.btnSaveTransaction);
        spnCategories = findViewById(R.id.spnReceiptPhotoCategories);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, DataHandler.CATEGORIES);
        spnCategories.setAdapter(adapter);

        Calendar newDate = Calendar.getInstance();
        year = newDate.get(Calendar.YEAR);
        monthOfYear = newDate.get(Calendar.MONTH);
        dayOfMonth = newDate.get(Calendar.DAY_OF_MONTH);
        datePicker = new DatePickerDialog(
                this, ReceiptPhotoActivity.this, year, monthOfYear, dayOfMonth);

        txtDate = findViewById(R.id.txtNewTransactionDate);
        txtDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    datePicker.show();
                }
            }
        });

    }

    public void ibtnPhotoOnClick(View view) {
        dispatchTakePictureIntent();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(getApplicationContext(),
                        "Cannot create a file to save image",
                        Toast.LENGTH_LONG).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.howmuch.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        pictureImagePath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            File imgFile = new File(pictureImagePath);
            if (imgFile.exists()) {
                newPhoto = true;
                imageBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                ExifInterface ei = null;
                try {
                    ei = new ExifInterface(pictureImagePath);

                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_UNDEFINED);

                    Bitmap rotatedBitmap = null;
                    switch (orientation) {

                        case ExifInterface.ORIENTATION_ROTATE_90:
                            rotatedBitmap = rotateImage(imageBitmap, 90);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_180:
                            rotatedBitmap = rotateImage(imageBitmap, 180);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_270:
                            rotatedBitmap = rotateImage(imageBitmap, 270);
                            break;

                        case ExifInterface.ORIENTATION_NORMAL:
                        default:
                            rotatedBitmap = imageBitmap;
                    }
                    txtRec = new TextRecogn(rotatedBitmap, new OnSuccessListener<FirebaseVisionText>() {
                        @Override
                        public void onSuccess(FirebaseVisionText firebaseVisionText) {
                            results = txtRec.success(firebaseVisionText);
                            txtReceiptPhotoTotal.setText(String.valueOf(findMax(results)));
                            Log.d("MYLOG", results.toString());
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public double findMax(ArrayList<String> strings) {
        double max = -1;
        for (String str : strings) {
            double current = Double.valueOf(str);
            if (current > max) {
                max = current;
            }
        }
        return max;
    }

    public void btnSaveOnClick(View view) {
        Transaction transaction = new Transaction();
        transaction.setDescription(txtReceiptPhotoDescription.getText().toString());
        transaction.setTotal(Double.valueOf(txtReceiptPhotoTotal.getText().toString()));
        transaction.setCategory(spnCategories.getSelectedItemPosition());
        String date = txtDate.getText().toString();

        transaction.setDate(date);
        dm.addTransaction(transaction);
        Toast.makeText(this, "Added transaction!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        this.year = year;
        this.monthOfYear = month;
        this.dayOfMonth = dayOfMonth;
        txtDate.setText((month + 1) +"/" +dayOfMonth + "/" + year);
    }
}

