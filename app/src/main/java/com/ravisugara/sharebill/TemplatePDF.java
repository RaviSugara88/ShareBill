package com.ravisugara.sharebill;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by in on 7/10/2018.
 */

public class TemplatePDF {
    private Context context;
    private File pdfFile;
    public String pdfFilePath;
    private String fileName;
    private Document document;
    private PdfWriter pdfWriter;
    private Paragraph paragraph;
    private Font fHead = new Font(Font.FontFamily.TIMES_ROMAN,17,Font.BOLD);
    private Font fAddress = new Font(Font.FontFamily.COURIER,13,Font.NORMAL);
    private Font fSubject = new Font(Font.FontFamily.TIMES_ROMAN,18,Font.UNDEFINED);
    private Font fpera = new Font(Font.FontFamily.TIMES_ROMAN,14,Font.NORMAL);
    private Font fTableHed = new Font(Font.FontFamily.TIMES_ROMAN,18,Font.BOLD);

    public TemplatePDF(Context context) {
        this.context = context;

    }
    public void openDocument(){
        crteateFile();
        try {
            document = new Document(PageSize.A4);
            pdfWriter =PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();

        }catch (Exception e){
            Log.e("openDocumet",e.toString());
        }

    }
    private void crteateFile(){
        File folder = new File(Environment.getExternalStorageDirectory().toString(),"PDF");
        if (!folder.exists()){
            folder.mkdir();
        }
            fileName = "myPdf"+".pdf";

        pdfFile = new File(folder,fileName);//"firstPdf.pdf"
        pdfFilePath = pdfFile.getAbsolutePath();


    }
    public void closeDocument(){
        document.close();
    }
    public void addMetaData(String title, String subject, String author){
        document.addTitle(title);
        document.addSubject(subject);
        document.addAuthor(author);

    }
    public void addTitle(String head, String address, String subjest){
        try {
        paragraph =new Paragraph();
        addChildP(new Paragraph(head,fHead));
        addChildP(new Paragraph(address,fAddress));
        addChildP(new Paragraph(subjest,fSubject));
       // addChildP(new Paragraph(statment,fStetment));
        paragraph.setSpacingAfter(20);//30

            document.add(paragraph);

        } catch (Exception e) {
            Log.e("addTitle",e.toString());
        }

    }
    private void addChildP(Paragraph childParagraph){
        childParagraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.add(childParagraph);

    }
    public void addParagraph(String text){
        try {

            paragraph =new Paragraph(text,fpera);
            paragraph.setSpacingAfter(5);
            paragraph.setSpacingBefore(5);
            document.add(paragraph);
        }catch (Exception e){
            Log.e("addParagraph",e.toString());
        }
    }
    public void createTable(String[]header, ArrayList<String[]> clients){
        try {
        paragraph =new Paragraph();
        paragraph.setFont(fTableHed);
        PdfPTable pdfPTable = new PdfPTable(header.length);
        pdfPTable.setWidthPercentage(100);
        PdfPCell pdfPCell;
        int indexC=0;
//        while (indexC<header.length){
//            pdfPCell = new PdfPCell(new Phrase(header[indexC++],fTableHed));
//            pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//            pdfPCell.setBackgroundColor(BaseColor.GREEN);
//            pdfPTable.addCell(pdfPCell);
//        }
        for (int indexR=0;indexR<clients.size();indexR++){
            String[]row =clients.get(indexR);
            for (indexC=0;indexC<header.length;indexC++){
                pdfPCell= new PdfPCell(new Phrase(row[indexC]));
                pdfPCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfPCell.setPadding(10.0F);
              //  pdfPCell.setFixedHeight(40);
                pdfPCell.setMinimumHeight(40);
                pdfPCell.setBorderColor(BaseColor.BLUE);
                pdfPTable.addCell(pdfPCell);
            }

        }
        paragraph.add(pdfPTable);

            document.add(paragraph);

        } catch (Exception e) {
            Log.e("createTable",e.toString());
        }

    }

}
