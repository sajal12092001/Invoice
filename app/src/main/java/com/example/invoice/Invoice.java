package com.example.invoice;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Invoice extends AppCompatActivity {
    String cname, caddress, cmobile, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        cname = getIntent().getStringExtra("name");
        caddress = getIntent().getStringExtra("address");
        cmobile = getIntent().getStringExtra("mobile");
        date = getIntent().getStringExtra("date&time");

        createpdf();
    }

    private void createpdf() {

        Cursor cursor = new Conn(this).get_shop_details();
        String path = getExternalFilesDir(null) + "/Invoices";


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


            //document close

            document.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}