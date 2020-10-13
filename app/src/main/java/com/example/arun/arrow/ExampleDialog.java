package com.example.arun.arrow;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class ExampleDialog extends AppCompatDialogFragment {
    private EditText editText;
    private File pdfFile;
    private DatabaseReference mRef;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        final View view=inflater.inflate(R.layout.pdf_generator,null);
        builder.setView(view)
                .setCancelable(false)
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Generate", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String pdfs=editText.getText().toString().trim();
                        if (pdfs.isEmpty())
                        {
                            editText.setError("Must hava a PDF Title.");
                            editText.requestFocus();
                            return;
                        }
                        else
                        {
                            try {
                                createPdfWrapper();
                                Toast.makeText(getActivity(),"Pdf is Successfully created.",Toast.LENGTH_SHORT).show();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (DocumentException e) {
                                e.printStackTrace();
                            }
                        }


                    }
                });
        editText=view.findViewById(R.id.editview);
        String pdfs=editText.getText().toString().trim();
        if (pdfs.isEmpty())
        {
            editText.setError("Must hava a PDF Title.");
            editText.requestFocus();
        }


        return builder.create();
    }
    private void createPdfWrapper() throws FileNotFoundException,DocumentException {

        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                    showMessageOKCancel("You need to allow access to Storage",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                REQUEST_CODE_ASK_PERMISSIONS);
                                    }
                                }
                            });
                    return;
                }

                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
            }
            return;
        }else {
            createPdf();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    try {
                        createPdfWrapper();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Permission Denied
                    Toast.makeText(getActivity(), "WRITE_EXTERNAL Permission Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    private void createPdf() throws FileNotFoundException, DocumentException {

        final File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
        }
        mRef=FirebaseDatabase.getInstance().getReference().child("Data");
        pdfFile = new File(docsFolder.getAbsolutePath(),editText.getText().append(".pdf").toString());
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String ss="";
                int i=1;
                for (DataSnapshot ds:dataSnapshot.getChildren())
                {
                    Map<String,Object> map=(Map<String, Object>)ds.getValue();
                    Object names=map.get("name");
                    String fname=String.valueOf(names);
                    OutputStream output = null;
                    try {
                        output = new FileOutputStream(pdfFile);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    final Document document = new Document();
                    try {
                        PdfWriter.getInstance(document, output);
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                    document.open();
                    try {
                        Date currentTime = Calendar.getInstance().getTime();
                        System.out.println(document.add(new Paragraph("Today's Attendence List"+"       ["+currentTime+"]")));
                        System.out.println(document.add(new Paragraph(ss=ss+(i++)+"   "+fname+"\n")));
                        System.out.println(document.add(new Paragraph("\n\nTotal student's present today out of 63 is "+(i-1)+" only.")));
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                    document.close();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
