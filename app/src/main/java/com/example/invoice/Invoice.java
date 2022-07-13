package com.example.invoice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Invoice extends AppCompatActivity {
    String cname, caddress, cmobile, date;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        SharedPreferences sharedPreferences = getSharedPreferences("customer", MODE_PRIVATE);
        cname = sharedPreferences.getString("name", "");
        caddress = sharedPreferences.getString("address", "");
        cmobile = sharedPreferences.getString("mobile", "");

        date = getIntent().getStringExtra("date&time");

        createpdf();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getDate()
    {
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
        return myDateObj.format(myFormatObj);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createpdf() {



        Cursor cursor = new Conn(this).get_shop_details();
        String path = getExternalFilesDir(null) + "/Invoices/"+getDate();


        File file = new File(path);
        if (!file.exists())
            file.mkdirs();
        File file1 = new File(file.getAbsolutePath() + "/" + cname + "-" + date + ".pdf");
        if (!file1.exists()) {
            try {
                file1.createNewFile();
            } catch (IOException e) {
                Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show();
            }
        }


        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file1));
            document.open();
            document.setPageSize(PageSize.A4);


            /*

            ------------------SHOP GST NUMBER-----------------

             */


            Chunk gstLabel = new Chunk("GSTIN : ");
            Chunk gstno = null;
            if (cursor.moveToFirst()) {
                gstno = new Chunk(cursor.getString(1));
            }
            document.add(gstLabel);
            document.add(gstno);


            /*

            -------------------------DATE----------------------

             */


            Chunk dateLabel = new Chunk("                                                   Date" + ": " + date);
            document.add(dateLabel);


            /*

              -------------------BILL OF SUPPLY--------------------

             */


            Paragraph bill_of_supply_title = new Paragraph("Bill of supply", new Font(Font.FontFamily.UNDEFINED));
            bill_of_supply_title.setAlignment(Element.ALIGN_CENTER);
            document.add(bill_of_supply_title);


             /*

            -----------------------SHOP NAME-------------------------

            */

            Paragraph shop_name = null;

            if (cursor.moveToFirst()) {
                shop_name = new Paragraph(cursor.getString(2), new Font(Font.FontFamily.HELVETICA, 22, Font.BOLD));
            }

            assert shop_name != null;
            shop_name.setAlignment(Element.ALIGN_CENTER);
            document.add(shop_name);



             /*

            -------------------------SHOP ADDRESS---------------------

            */

            Paragraph shop_adress = null;

            if (cursor.moveToFirst()) {
                shop_adress = new Paragraph(cursor.getString(3) + ", " + cursor.getString(4) + ", " + cursor.getString(5) + ", " + cursor.getString(6) + "-" + cursor.getString(7), new Font(Font.FontFamily.HELVETICA, 15, Font.NORMAL));
            }
            assert shop_adress != null;
            shop_adress.setAlignment(Element.ALIGN_CENTER);
            document.add(shop_adress);



            /*

            --------------------MOBILE NUMBERS-----------------

             */

            Paragraph shop_mob = null;
            if (cursor.moveToFirst()) {
                if (cursor.getString(9).equals("")) {
                    shop_mob = new Paragraph(String.valueOf(new Chunk("Mob. " + cursor.getString(8))), new Font(Font.FontFamily.HELVETICA, 15, Font.NORMAL));

                } else {
                    shop_mob = new Paragraph(String.valueOf(new Chunk("Mob. " + cursor.getString(8) + ", " + cursor.getString(9))), new Font(Font.FontFamily.HELVETICA, 15, Font.NORMAL));
                }
            }
            assert shop_mob != null;
            shop_mob.setAlignment(Element.ALIGN_CENTER);
            document.add(shop_mob);


            /*

            ----------------LINE SEPARATOR---------------

             */

            Paragraph blankline = new Paragraph("\n");
            LineSeparator blackline = new LineSeparator();
            document.add(blankline);
            document.add(blackline);



             /*

              -------------------BILL to--------------------

             */


            Paragraph bill_to = new Paragraph("Bill to-", new Font(Font.FontFamily.UNDEFINED));
            document.add(bill_to);


            /*

            ---------------CUSTOMER NAME-------------------

             */

            Paragraph custname = new Paragraph("     Name      :  " + cname);
            document.add(custname);


            /*

            ---------------CUSTOMER ADDRESS-------------------

             */

            Paragraph custaddress = new Paragraph("     Address  :  " + caddress);
            document.add(custaddress);

            /*

            ---------------CUSTOMER PHONE-------------------

             */

            Paragraph custmobile = new Paragraph("     Mobile     :  " + cmobile);
            document.add(custmobile);



            /*

            ----------------LINE SEPARATOR---------------

             */

            Paragraph blankline1 = new Paragraph("\n");
            LineSeparator blackline1 = new LineSeparator();
            document.add(blankline1);
            document.add(blackline1);



            /*

            -----------------GOODS DETAILS---------------

             */


            PdfPTable table = new PdfPTable(9);

            table.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.setWidthPercentage(100);
            float[] columnWidths = {1, 1, 1, 0.5f, 0.8f, 0.8f, 1,1, 1};

            table.setWidths(columnWidths);

//            PdfPCell serial_no = new PdfPCell(new Paragraph("SNO"));
//            //serial_no.setBorder(1);
//            serial_no.setHorizontalAlignment(Element.ALIGN_CENTER);
//            serial_no.setMinimumHeight(0.1f);
//            serial_no.setVerticalAlignment(Element.ALIGN_CENTER);
//
//            table.addCell(serial_no);


            PdfPCell goods_name = new PdfPCell(Phrase.getInstance("Decription of goods"));
            // goods_name.setBorder(500);
            goods_name.setHorizontalAlignment(Element.ALIGN_CENTER);
            goods_name.setNoWrap(true);
            goods_name.setColspan(2);
            goods_name.setVerticalAlignment(Element.ALIGN_CENTER);
            table.addCell(goods_name);


            PdfPCell item_rate = new PdfPCell(new Paragraph("Item Rate"));
            // item_rate.setBorder(1);
            item_rate.setHorizontalAlignment(Element.ALIGN_CENTER);
            item_rate.setVerticalAlignment(Element.ALIGN_CENTER);
            table.addCell(item_rate);

            PdfPCell quantity = new PdfPCell(new Paragraph("QTY"));
            //quantity.setBorder(1);
            quantity.setVerticalAlignment(Element.ALIGN_CENTER);
            quantity.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(quantity);

            PdfPCell cgstrate = new PdfPCell(new Paragraph("CGST%"));
            //quantity.setBorder(1);
            cgstrate.setVerticalAlignment(Element.ALIGN_CENTER);
            cgstrate.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cgstrate);

            PdfPCell sgstrate = new PdfPCell(new Paragraph("SGST%"));
            //quantity.setBorder(1);
            sgstrate.setVerticalAlignment(Element.ALIGN_CENTER);
            sgstrate.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(sgstrate);

            PdfPCell cgstamount = new PdfPCell(new Paragraph("CGST Amount"));
            //quantity.setBorder(1);
            cgstamount.setVerticalAlignment(Element.ALIGN_CENTER);
            cgstamount.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cgstamount);

            PdfPCell sgstamount = new PdfPCell(new Paragraph("SGST Amount"));
            //quantity.setBorder(1);
            sgstamount.setVerticalAlignment(Element.ALIGN_CENTER);
            sgstamount.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(sgstamount);

            PdfPCell subtotal = new PdfPCell(new Paragraph("Sub Total"));
            //subtotal.setBorder(1);
            subtotal.setVerticalAlignment(Element.ALIGN_CENTER);
            subtotal.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(subtotal);
            PdfPCell span = new PdfPCell(new Paragraph(""));
            table.addCell(span);


            document.add(table);


            //document close

            document.close();

            startActivity(new Intent(Invoice.this, Add_Items.class));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}