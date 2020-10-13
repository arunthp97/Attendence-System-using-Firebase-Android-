package com.example.arun.arrow;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.ScrollBar;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;

import java.io.File;

public class PDF_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_);
        //PDFVIEW SHALL DISPLAY OUR PDFS
        PDFView pdfView= (PDFView) findViewById(R.id.pdfView);
        //SCROLLBAR TO ENABLE SCROLLING
        ScrollBar scrollBar = (ScrollBar) findViewById(R.id.scrollBar);
        pdfView.setScrollBar(scrollBar);
        //VERTICAL SCROLLING
        scrollBar.setHorizontal(false);
        //SACRIFICE MEMORY FOR QUALITY
        //pdfView.useBestQuality(true)

        //UNPACK OUR DATA FROM INTENT
        Intent i=this.getIntent();
        String path=i.getExtras().getString("PATH");

        //GET THE PDF FILE
        File file=new File(path);

        if(file.canRead())
        {
            //LOAD IT

            pdfView.fromFile(file).defaultPage(1).onLoad(new OnLoadCompleteListener() {
                @Override
                public void loadComplete(int nbPages) {
                    Toast.makeText(PDF_Activity.this, "PDF have only "+String.valueOf(nbPages)+" Page.", Toast.LENGTH_LONG).show();
                }
            }).load();

        }


    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE)
        {

        }
        else if(newConfig.orientation==Configuration.ORIENTATION_PORTRAIT)
        {

        }
    }
}