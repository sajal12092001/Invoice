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
    String getdiscount, gettotal, getgrandtotal, getdiscountrate;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        SharedPreferences sharedPreferences = getSharedPreferences("customer", MODE_PRIVATE);
        cname = sharedPreferences.getString("name", "");
        caddress = sharedPreferences.getString("address", "");
        cmobile = sharedPreferences.getString("mobile", "");

        SharedPreferences sharedPreference = this.getSharedPreferences("itemtotal", MODE_PRIVATE);
        getdiscount = sharedPreference.getString("discount", "");
        gettotal = sharedPreference.getString("grandtotal", "");
        getdiscountrate = sharedPreference.getString("disrate", "");
        getgrandtotal = sharedPreference.getString("discountTtot", "");

        date = getIntent().getStringExtra("date");

        try {
            createpdf();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getDate() {
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
        return myDateObj.format(myFormatObj);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createpdf() throws IOException {


        Cursor cursor = new Conn(this).get_shop_details();
        String path = getExternalFilesDir(null) + "/Invoices/" + getDate();
        // String path = Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS) + "/Invoices" + "/" + getDate();


        File file = new File(path);
        if (!file.exists())
            file.mkdirs();

        File file1 = new File(file.getAbsolutePath() + "/" + cname + "-" + date + ".pdf");
        //file1.createNewFile();
        if (!file1.exists()) {
            try {
                file1.createNewFile();
            } catch (IOException e) {
                Toast.makeText(this, "" + e + "\n" + file1.exists(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "file not created", Toast.LENGTH_SHORT).show();
        }


        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file1, false));
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


            PdfPTable table = new PdfPTable(5);

            table.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.setWidthPercentage(100);
            float[] columnWidths = {0.4f, 3.5f, 1, 1, 1};
            table.setWidths(columnWidths);

            PdfPCell serial_no = new PdfPCell(new Paragraph("SNO"));
            serial_no.setHorizontalAlignment(Element.ALIGN_CENTER);
            serial_no.setVerticalAlignment(Element.ALIGN_CENTER);
            table.addCell(serial_no);


            PdfPCell goods_name = new PdfPCell(Phrase.getInstance("Decription of goods"));
            goods_name.setHorizontalAlignment(Element.ALIGN_CENTER);
            goods_name.setVerticalAlignment(Element.ALIGN_CENTER);
            table.addCell(goods_name);


            PdfPCell item_rate = new PdfPCell(new Paragraph("Item Rate"));
            item_rate.setHorizontalAlignment(Element.ALIGN_CENTER);
            item_rate.setVerticalAlignment(Element.ALIGN_CENTER);
            table.addCell(item_rate);

            PdfPCell quantity = new PdfPCell(new Paragraph("QTY"));
            quantity.setVerticalAlignment(Element.ALIGN_CENTER);
            quantity.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(quantity);


            PdfPCell subtotal = new PdfPCell(new Paragraph("Total"));
            subtotal.setVerticalAlignment(Element.ALIGN_CENTER);
            subtotal.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(subtotal);

            document.add(table);


            Cursor cursor1 = new Conn(this).get_items();

            if (cursor1.moveToFirst()) {
                PdfPTable itemtable = new PdfPTable(5);

                itemtable.setHorizontalAlignment(Element.ALIGN_LEFT);
                itemtable.setWidthPercentage(100);
                float[] itemcolumnWidths = {0.4f, 3.5f, 1, 1, 1};
                itemtable.setWidths(itemcolumnWidths);

                PdfPCell itemserial_no = new PdfPCell(new Paragraph(cursor1.getString(0)));
                itemserial_no.setBorder(0);
                itemserial_no.setHorizontalAlignment(Element.ALIGN_CENTER);
                itemserial_no.setVerticalAlignment(Element.ALIGN_CENTER);
                itemtable.addCell(itemserial_no);


                PdfPCell itemgoods_name = new PdfPCell(Phrase.getInstance(cursor1.getString(1)));
                itemgoods_name.setHorizontalAlignment(Element.ALIGN_CENTER);
                itemgoods_name.setBorder(0);
                itemgoods_name.setNoWrap(false);
                itemgoods_name.setVerticalAlignment(Element.ALIGN_CENTER);
                itemtable.addCell(itemgoods_name);


                PdfPCell itemitem_rate = new PdfPCell(new Paragraph(cursor1.getString(2)));
                itemitem_rate.setHorizontalAlignment(Element.ALIGN_CENTER);
                itemitem_rate.setVerticalAlignment(Element.ALIGN_CENTER);
                itemitem_rate.setBorder(0);
                itemtable.addCell(itemitem_rate);

                PdfPCell itemquantity = new PdfPCell(new Paragraph(cursor1.getString(3)));
                itemquantity.setVerticalAlignment(Element.ALIGN_CENTER);
                itemquantity.setBorder(0);
                itemquantity.setHorizontalAlignment(Element.ALIGN_CENTER);
                itemtable.addCell(itemquantity);


                PdfPCell itemsubtotal = new PdfPCell(new Paragraph(cursor1.getString(4)));
                itemsubtotal.setVerticalAlignment(Element.ALIGN_CENTER);
                itemsubtotal.setBorder(0);
                itemsubtotal.setHorizontalAlignment(Element.ALIGN_CENTER);
                itemtable.addCell(itemsubtotal);

                document.add(itemtable);

            }
            while (cursor1.moveToNext()) {
                PdfPTable itemtable = new PdfPTable(5);

                itemtable.setHorizontalAlignment(Element.ALIGN_LEFT);
                itemtable.setWidthPercentage(100);
                float[] itemcolumnWidths = {0.4f, 3.5f, 1, 1, 1};
                itemtable.setWidths(itemcolumnWidths);

                PdfPCell itemserial_no = new PdfPCell(new Paragraph(cursor1.getString(0)));
                itemserial_no.setBorder(0);
                itemserial_no.setHorizontalAlignment(Element.ALIGN_CENTER);
                itemserial_no.setVerticalAlignment(Element.ALIGN_CENTER);
                itemtable.addCell(itemserial_no);


                PdfPCell itemgoods_name = new PdfPCell(Phrase.getInstance(cursor1.getString(1)));
                itemgoods_name.setHorizontalAlignment(Element.ALIGN_CENTER);
                itemgoods_name.setBorder(0);
                itemgoods_name.setNoWrap(false);
                itemgoods_name.setVerticalAlignment(Element.ALIGN_CENTER);
                itemtable.addCell(itemgoods_name);


                PdfPCell itemitem_rate = new PdfPCell(new Paragraph(cursor1.getString(2)));
                itemitem_rate.setHorizontalAlignment(Element.ALIGN_CENTER);
                itemitem_rate.setVerticalAlignment(Element.ALIGN_CENTER);
                itemitem_rate.setBorder(0);
                itemtable.addCell(itemitem_rate);

                PdfPCell itemquantity = new PdfPCell(new Paragraph(cursor1.getString(3)));
                itemquantity.setVerticalAlignment(Element.ALIGN_CENTER);
                itemquantity.setBorder(0);
                itemquantity.setHorizontalAlignment(Element.ALIGN_CENTER);
                itemtable.addCell(itemquantity);


                PdfPCell itemsubtotal = new PdfPCell(new Paragraph(cursor1.getString(4)));
                itemsubtotal.setVerticalAlignment(Element.ALIGN_CENTER);
                itemsubtotal.setBorder(0);
                itemsubtotal.setHorizontalAlignment(Element.ALIGN_CENTER);
                itemtable.addCell(itemsubtotal);

                document.add(itemtable);

            }
            LineSeparator lineSeparator = new LineSeparator();
            document.add(lineSeparator);

            PdfPTable subtotaltable = new PdfPTable(6);
            subtotaltable.setHorizontalAlignment(Element.ALIGN_RIGHT);
            //subtotaltable.setWidthPercentage(100);
            float[] itemcolumnWidths = {1, 1, 1, 1, 1, 1.1f};
            subtotaltable.setWidths(itemcolumnWidths);

            PdfPCell a = new PdfPCell(new Paragraph());
            a.setBorder(0);
            subtotaltable.addCell(a);
            PdfPCell b = new PdfPCell(new Paragraph());
            b.setBorder(0);
            subtotaltable.addCell(b);
            PdfPCell c = new PdfPCell(new Paragraph());
            c.setBorder(0);
            subtotaltable.addCell(c);
            PdfPCell d = new PdfPCell(new Paragraph());
            d.setBorder(0);
            subtotaltable.addCell(d);

            PdfPCell subtotal1 = new PdfPCell(new Paragraph("Sub Total:"));
            subtotal1.setVerticalAlignment(Element.ALIGN_CENTER);
            subtotal1.setHorizontalAlignment(Element.ALIGN_CENTER);
            subtotaltable.addCell(subtotal1);

            PdfPCell subtotal2 = new PdfPCell(new Paragraph(gettotal));
            subtotal2.setVerticalAlignment(Element.ALIGN_CENTER);
            subtotal2.setHorizontalAlignment(Element.ALIGN_CENTER);
            subtotaltable.addCell(subtotal2);

            document.add(subtotaltable);


            PdfPTable discounttable = new PdfPTable(6);
            discounttable.setHorizontalAlignment(Element.ALIGN_RIGHT);
            float[] columnWidthsdis = {1, 1, 1, 1, 1.4f, 1.2f};
            discounttable.setWidths(columnWidthsdis);

            PdfPCell e = new PdfPCell(new Paragraph());
            e.setBorder(0);
            discounttable.addCell(e);
            PdfPCell f = new PdfPCell(new Paragraph());
            f.setBorder(0);
            discounttable.addCell(f);
            PdfPCell g = new PdfPCell(new Paragraph());
            g.setBorder(0);
            discounttable.addCell(g);
            PdfPCell h = new PdfPCell(new Paragraph());
            h.setBorder(0);
            discounttable.addCell(h);

            PdfPCell discount = new PdfPCell(new Paragraph("Discount: " + getdiscount + "%"));
            discount.setVerticalAlignment(Element.ALIGN_CENTER);
            discount.setNoWrap(true);
            discount.setHorizontalAlignment(Element.ALIGN_CENTER);
            discounttable.addCell(discount);

            PdfPCell discount1 = new PdfPCell(new Paragraph("- " + getdiscountrate));
            discount1.setVerticalAlignment(Element.ALIGN_CENTER);
            discount1.setHorizontalAlignment(Element.ALIGN_CENTER);
            discounttable.addCell(discount1);

            document.add(discounttable);

            LineSeparator lineSeparator1 = new LineSeparator();
            document.add(lineSeparator1);


            PdfPTable granddtotal = new PdfPTable(6);
            granddtotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
            float[] columnWidthsdiss = {1, 1, 1, 1, 1, 1.1f};
            granddtotal.setWidths(columnWidthsdiss);

            PdfPCell i = new PdfPCell(new Paragraph());
            i.setBorder(0);
            granddtotal.addCell(i);
            PdfPCell j = new PdfPCell(new Paragraph());
            j.setBorder(0);
            granddtotal.addCell(j);
            PdfPCell k = new PdfPCell(new Paragraph());
            k.setBorder(0);
            granddtotal.addCell(k);
            PdfPCell l = new PdfPCell(new Paragraph());
            l.setBorder(0);
            granddtotal.addCell(l);

            PdfPCell grandtot = new PdfPCell(new Paragraph("Grand Total: "));
            grandtot.setVerticalAlignment(Element.ALIGN_CENTER);
            grandtot.setNoWrap(true);
            grandtot.setHorizontalAlignment(Element.ALIGN_CENTER);
            granddtotal.addCell(grandtot);

            PdfPCell grandtot2 = new PdfPCell(new Paragraph("Rs. " + getgrandtotal));
            grandtot2.setVerticalAlignment(Element.ALIGN_CENTER);
            grandtot2.setHorizontalAlignment(Element.ALIGN_CENTER);
            granddtotal.addCell(grandtot2);

            document.add(granddtotal);


            //document close

            document.close();
            Toast.makeText(this, file1.getAbsolutePath(), Toast.LENGTH_SHORT).show();

            startActivity(new Intent(Invoice.this, Add_Items.class));

            new Conn(this).delete_create_item_table();
            //finish();


        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}