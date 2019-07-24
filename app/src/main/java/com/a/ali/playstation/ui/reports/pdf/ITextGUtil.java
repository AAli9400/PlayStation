package com.a.ali.playstation.ui.reports.pdf;

import android.app.Application;
import android.content.Intent;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ITextGUtil {
    private ITextGUtil() {
    }

    public static void createPdfFile(int columnNumber, @NonNull Application application, @NonNull Helper helper, @NonNull Helper2 helper2) {
        // set up table
        PdfPTable table = new PdfPTable(columnNumber);

        //Support arabic
        Font arialFont = FontFactory.getFont("assets/arial.ttf", BaseFont.IDENTITY_H, 14, Font.NORMAL);
        table.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

        if (helper.setHeader(table, arialFont)) {
            helper.setData(table, arialFont);
        }

        File path = new File(Environment.getExternalStorageDirectory(), "Playstation");
        if (!path.exists()) {
            path.mkdir();
        }
        File file = new File(path, System.currentTimeMillis() + ".pdf");

        Document layoutDocument = null;
        try {
            PdfDocument pdfDocument = new PdfDocument();
            pdfDocument.writeTo(new FileOutputStream(file));

            layoutDocument = new Document();

            PdfWriter.getInstance(layoutDocument, new FileOutputStream(file));
            layoutDocument.open();

            table.setWidthPercentage(100f);


            // write the document content
            // add table to document
            layoutDocument.add(table);

        } catch (IOException | DocumentException e) {
            Log.e("Exceptionn", e.getMessage());
        } finally {
            // close
            if (layoutDocument != null) {
                layoutDocument.close();

                helper2.openFile(file);
            }
        }
    }

    public interface Helper {
        void setData(PdfPTable pdfPTable, Font font);

        boolean setHeader(PdfPTable pdfPTable, Font font);
    }

    public interface Helper2 {
        void openFile(@NonNull File file);

    }
}