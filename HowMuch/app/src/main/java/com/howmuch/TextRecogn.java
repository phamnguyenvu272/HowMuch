package com.howmuch;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.document.FirebaseVisionDocumentText;
import com.google.firebase.ml.vision.document.FirebaseVisionDocumentTextRecognizer;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.google.firebase.ml.vision.text.RecognizedLanguage;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;

public class TextRecogn {

    private final int TEXT_RECO_REQ_CODE = 100;
    private FirebaseVisionImage image;
    FirebaseVisionTextRecognizer detector;
    private OnSuccessListener<FirebaseVisionText> listener;
    private final String TAG = "TextRec-Log";

    public TextRecogn(Bitmap bitmap, OnSuccessListener<FirebaseVisionText> listener) {
        image = FirebaseVisionImage.fromBitmap(bitmap);
        this.listener = listener;
        detector = FirebaseVision.getInstance()
                .getOnDeviceTextRecognizer();
        processImage();
    }

    public void processImage() {
        Task<FirebaseVisionText> result =
                detector.processImage(image)
                        .addOnSuccessListener(listener)
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "Failed!!");
                                    }
                                });
    }

    public ArrayList<String> success(FirebaseVisionText result) {
//        ArrayList<String> string = new ArrayList<>();
//        Log.d(TAG, result.getText());
        ArrayList<String> results = new ArrayList<>();
        for (FirebaseVisionText.TextBlock block : result.getTextBlocks()) {
            results.addAll(parseIntsAndFloats(block.getText()));
        }
        return results;
    }

    private ArrayList<String> parseIntsAndFloats(String raw) {
        ArrayList<String> listBuffer = new ArrayList<String>();

        Pattern pattern = Pattern.compile("[+-]?[0-9]*[.]?[0-9]+");
        //Pattern pattern = Pattern.compile("^\\d+\\.\\d{2}$");

        Matcher m = pattern.matcher(raw);

        while (m.find()) {
            if (m.group().matches("^\\d+\\.\\d{1,2}$")) {
                listBuffer.add(m.group());
            }

        }

        return listBuffer;
    }

}
