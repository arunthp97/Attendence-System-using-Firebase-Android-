package com.example.arun.arrow;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.io.File;
import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {
    private RelativeLayout relativeLayout;
    private android.support.v7.widget.Toolbar toolbar;
    private CustomAdapter adapter = new CustomAdapter(Main2Activity.this, getPDFs());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        YoYo.with(Techniques.Landing).playOn(findViewById(R.id.lay));
        relativeLayout = (RelativeLayout) findViewById(R.id.lay);
        final GridView gv = (GridView) findViewById(R.id.gv);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
        gv.setAdapter(adapter);

    }

    private ArrayList<PDFDoc> getPDFs()

    {
        ArrayList<PDFDoc> pdfDocs = new ArrayList<>();
        //TARGET FOLDER
        File downloadsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);

        PDFDoc pdfDoc;

        if (downloadsFolder.exists()) {
            //GET ALL FILES IN DOWNLOAD FOLDER
            File[] files = downloadsFolder.listFiles();

            //LOOP THRU THOSE FILES GETTING NAME AND URI
            for (int i = 0; i < files.length; i++) {
                File file = files[i];

                if (file.getPath().endsWith("pdf")) {
                    pdfDoc = new PDFDoc();
                    pdfDoc.setName(file.getName());
                    pdfDoc.setPath(file.getAbsolutePath());

                    pdfDocs.add(pdfDoc);
                }

            }
        }

        return pdfDocs;
    }

    public class CustomAdapter extends BaseAdapter {

        Context c;
        ArrayList<PDFDoc> pdfDocs;

        public CustomAdapter(Context c, ArrayList<PDFDoc> pdfDocs) {
            this.c = c;
            this.pdfDocs = pdfDocs;
        }

        @Override
        public int getCount() {
            return pdfDocs.size();
        }

        @Override
        public Object getItem(int i) {
            return pdfDocs.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                //INFLATE CUSTOM LAYOUT
                view = LayoutInflater.from(c).inflate(R.layout.model, viewGroup, false);
            }
            final PDFDoc pdfDoc = (PDFDoc) this.getItem(i);
            final GridView gv = (GridView) view.findViewById(R.id.gv);
            final TextView nameTxt = (TextView) view.findViewById(R.id.nameTxt);
            ImageView img = (ImageView) view.findViewById(R.id.pdfImage);

            //BIND DATA
            nameTxt.setText(pdfDoc.getName());
            img.setImageResource(R.drawable.pdf_icon);

            //VIEW ITEM CLICK
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(Main2Activity.this, view);
                    popupMenu.getMenuInflater().inflate(R.menu.pop_menu, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.open: {
                                    openPDFView(pdfDoc.getPath());
                                    break;
                                }
                                case R.id.delete: {
                                    final AlertDialog.Builder builder = new AlertDialog.Builder(c);
                                    builder.setCancelable(false);
                                    builder.setTitle("Do you Really want to delete this PDF file.");
                                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            File file = new File(pdfDoc.getPath());
                                            String loc = String.valueOf(file);
                                            pdfDocs.remove(i);
                                            notifyDataSetChanged();
                                            file.delete();
                                            Toast.makeText(c, "PDF file is deleted successfully.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    builder.create().show();
                                    break;
                                }
                                case R.id.share: {
                                        File path = new File(pdfDoc.getPath());
                                        Intent intent = new Intent(Intent.ACTION_SEND);
                                        Uri uri=FileProvider.getUriForFile(Main2Activity.this,getApplicationContext().getPackageName()+".provider",path);
                                        intent.setDataAndType(uri,"document/*");
                                        intent.putExtra(Intent.EXTRA_STREAM,uri);
                                        startActivity(Intent.createChooser(intent, "Share PDF"));
                                        break;
                                }
                            }
                            return true;
                        }
                    });

                    popupMenu.show();
                }
            });
            return view;
        }

        //OPEN PDF VIEW
        private void openPDFView(String path) {
            Intent i = new Intent(c, PDF_Activity.class);
            i.putExtra("PATH", path);
            c.startActivity(i);
        }
    }


}

