package com.example.invoice;

import static android.content.ContentValues.TAG;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Invoice extends AppCompatActivity {
    String cname, caddress, cmobile, date;
    String getgrandtotal = "";
    File file1;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        SharedPreferences sharedPreferences = getSharedPreferences("customer", MODE_PRIVATE);
        cname = sharedPreferences.getString("name", "");
        caddress = sharedPreferences.getString("address", "");
        cmobile = sharedPreferences.getString("mobile", "");

        getgrandtotal = getIntent().getStringExtra("grandtotal");

        date = getIntent().getStringExtra("date");

        try {
            createpdf();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getDate() {
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
        return myDateObj.format(myFormatObj);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createpdf() throws Exception {

        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Invoices" + "/" + getDate();

        File file = new File(path);
        if (!file.exists())
            file.mkdirs();

        file1 = new File(path + "/" + cname + "-" + date + ".pdf");

        if (!file1.exists()) {
            file1.createNewFile();

            Cursor cursor = new Conn(this).get_shop_details();
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

            --------------------MOBILE NUMBERS-----------------

             */

                Chunk shop_mob = null;
                if (cursor.moveToFirst()) {
                    if (cursor.getString(9).equals("")) {
                        shop_mob = new Chunk(String.valueOf(new Chunk("                                                                                Mob. " + cursor.getString(8))));

                    } else {
                        shop_mob = new Chunk(String.valueOf(new Chunk("                                                          Mob. " + cursor.getString(8) + ", " + cursor.getString(9))));
                    }
                }
                assert shop_mob != null;
                document.add(shop_mob);


                 /*
                 ------------ shop logo ----------
                  */

                if (cursor.getBlob(10).length != 0) {


//                Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.om);
                    Bitmap bm = BitmapFactory.decodeByteArray(cursor.getBlob(10), 0, cursor.getBlob(10).length);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    Image img = null;
                    byte[] byteArray = stream.toByteArray();
                    try {
                        img = Image.getInstance(byteArray);
                    } catch (BadElementException | IOException e) {
                        e.printStackTrace();
                    }
                    assert img != null;
                    img.setWidthPercentage(50);
                    img.scaleToFit(70, 35);
                    img.setAlignment(Element.ALIGN_CENTER);
                    document.add(img);
                }




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
                    shop_name = new Paragraph(cursor.getString(2), new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD));
                }

                assert shop_name != null;
                shop_name.setAlignment(Element.ALIGN_CENTER);
                document.add(shop_name);

             /*

            -------------------------SHOP ADDRESS---------------------

            */

                Paragraph shop_adress = null;

                if (cursor.moveToFirst()) {
                    shop_adress = new Paragraph(cursor.getString(3) + ", " + cursor.getString(4) + ", " + cursor.getString(5) + ", " + cursor.getString(6).toUpperCase() + "-" + cursor.getString(7), new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL));
                }
                assert shop_adress != null;
                shop_adress.setAlignment(Element.ALIGN_CENTER);
                document.add(shop_adress);

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

                Paragraph custname = new Paragraph("     Name      :  " + cname + "                                                    Date    :   " + date, new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL));
                document.add(custname);

            /*

            ---------------CUSTOMER ADDRESS-------------------

             */

                Paragraph custaddress = new Paragraph("     Address   :  " + caddress, new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL));
                document.add(custaddress);

            /*

            ---------------CUSTOMER PHONE-------------------

             */

                Paragraph custmobile = new Paragraph("     Mobile     :  " + cmobile, new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL));
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


                PdfPTable table = new PdfPTable(8);

                table.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.setWidthPercentage(100);
                float[] columnWidths = {0.5f, 2, 1, 1, 0.6f, 1, 1, 1};
                table.setWidths(columnWidths);

                PdfPCell serial_no = new PdfPCell(new Paragraph("SNO"));
                serial_no.setHorizontalAlignment(Element.ALIGN_CENTER);
                serial_no.setVerticalAlignment(Element.ALIGN_CENTER);
                serial_no.setBorder(2);
                table.addCell(serial_no);


                PdfPCell goods_name = new PdfPCell(Phrase.getInstance("Decription of goods"));
                goods_name.setHorizontalAlignment(Element.ALIGN_CENTER);
                goods_name.setVerticalAlignment(Element.ALIGN_CENTER);
                goods_name.setBorder(2);
                table.addCell(goods_name);

                PdfPCell item_size = new PdfPCell(new Paragraph("Size"));
                item_size.setHorizontalAlignment(Element.ALIGN_CENTER);
                item_size.setVerticalAlignment(Element.ALIGN_CENTER);
                item_size.setBorder(2);
                table.addCell(item_size);

                PdfPCell item_rate = new PdfPCell(new Paragraph("Item Rate"));
                item_rate.setHorizontalAlignment(Element.ALIGN_CENTER);
                item_rate.setVerticalAlignment(Element.ALIGN_CENTER);
                item_rate.setBorder(2);
                table.addCell(item_rate);

                PdfPCell quantity = new PdfPCell(new Paragraph("QTY"));
                quantity.setVerticalAlignment(Element.ALIGN_CENTER);
                quantity.setHorizontalAlignment(Element.ALIGN_CENTER);
                quantity.setBorder(2);
                table.addCell(quantity);


                PdfPCell subtotal = new PdfPCell(new Paragraph("Sub Total"));
                subtotal.setVerticalAlignment(Element.ALIGN_CENTER);
                subtotal.setHorizontalAlignment(Element.ALIGN_CENTER);
                subtotal.setBorder(2);
                table.addCell(subtotal);

                PdfPCell discount = new PdfPCell(new Paragraph("Discount"));
                discount.setVerticalAlignment(Element.ALIGN_CENTER);
                discount.setHorizontalAlignment(Element.ALIGN_CENTER);
                discount.setBorder(2);
                table.addCell(discount);

                PdfPCell total = new PdfPCell(new Paragraph("Total"));
                total.setVerticalAlignment(Element.ALIGN_CENTER);
                total.setHorizontalAlignment(Element.ALIGN_CENTER);
                total.setBorder(2);
                table.addCell(total);

                document.add(table);


                Cursor cursor1 = new Conn(this).get_items();

                if (cursor1.moveToFirst()) {
                    PdfPTable itemtable = new PdfPTable(8);

                    itemtable.setHorizontalAlignment(Element.ALIGN_LEFT);
                    itemtable.setWidthPercentage(100);
                    float[] itemcolumnWidths = {0.5f, 2, 1, 1, 0.6f, 1, 1, 1};
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


                    PdfPCell itemitem_size = new PdfPCell(new Paragraph(cursor1.getString(2)));
                    itemitem_size.setHorizontalAlignment(Element.ALIGN_CENTER);
                    itemitem_size.setVerticalAlignment(Element.ALIGN_CENTER);
                    itemitem_size.setBorder(0);
                    itemtable.addCell(itemitem_size);

                    PdfPCell itemitem_rate = new PdfPCell(new Paragraph(cursor1.getString(3)));
                    itemitem_rate.setHorizontalAlignment(Element.ALIGN_CENTER);
                    itemitem_rate.setVerticalAlignment(Element.ALIGN_CENTER);
                    itemitem_rate.setBorder(0);
                    itemtable.addCell(itemitem_rate);

                    PdfPCell itemquantity = new PdfPCell(new Paragraph(cursor1.getString(4)));
                    itemquantity.setVerticalAlignment(Element.ALIGN_CENTER);
                    itemquantity.setBorder(0);
                    itemquantity.setHorizontalAlignment(Element.ALIGN_CENTER);
                    itemtable.addCell(itemquantity);


                    PdfPCell itemsubtotal = new PdfPCell(new Paragraph(cursor1.getString(5)));
                    itemsubtotal.setVerticalAlignment(Element.ALIGN_CENTER);
                    itemsubtotal.setBorder(0);
                    itemsubtotal.setHorizontalAlignment(Element.ALIGN_CENTER);
                    itemtable.addCell(itemsubtotal);

                    PdfPCell itemdiscount = new PdfPCell(new Paragraph(cursor1.getString(6)));
                    itemdiscount.setVerticalAlignment(Element.ALIGN_CENTER);
                    itemdiscount.setBorder(0);
                    itemdiscount.setHorizontalAlignment(Element.ALIGN_CENTER);
                    itemtable.addCell(itemdiscount);

                    PdfPCell itemtotal = new PdfPCell(new Paragraph(cursor1.getString(7)));
                    itemtotal.setVerticalAlignment(Element.ALIGN_CENTER);
                    itemtotal.setBorder(0);
                    itemtotal.setHorizontalAlignment(Element.ALIGN_CENTER);
                    itemtable.addCell(itemtotal);

                    document.add(itemtable);

                }
                while (cursor1.moveToNext()) {
                    PdfPTable itemtable = new PdfPTable(8);

                    itemtable.setHorizontalAlignment(Element.ALIGN_LEFT);
                    itemtable.setWidthPercentage(100);
                    float[] itemcolumnWidths = {0.5f, 2, 1, 1, 0.6f, 1, 1, 1};
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


                    PdfPCell itemitem_size = new PdfPCell(new Paragraph(cursor1.getString(2)));
                    itemitem_size.setHorizontalAlignment(Element.ALIGN_CENTER);
                    itemitem_size.setVerticalAlignment(Element.ALIGN_CENTER);
                    itemitem_size.setBorder(0);
                    itemtable.addCell(itemitem_size);

                    PdfPCell itemitem_rate = new PdfPCell(new Paragraph(cursor1.getString(3)));
                    itemitem_rate.setHorizontalAlignment(Element.ALIGN_CENTER);
                    itemitem_rate.setVerticalAlignment(Element.ALIGN_CENTER);
                    itemitem_rate.setBorder(0);
                    itemtable.addCell(itemitem_rate);

                    PdfPCell itemquantity = new PdfPCell(new Paragraph(cursor1.getString(4)));
                    itemquantity.setVerticalAlignment(Element.ALIGN_CENTER);
                    itemquantity.setBorder(0);
                    itemquantity.setHorizontalAlignment(Element.ALIGN_CENTER);
                    itemtable.addCell(itemquantity);


                    PdfPCell itemsubtotal = new PdfPCell(new Paragraph(cursor1.getString(5)));
                    itemsubtotal.setVerticalAlignment(Element.ALIGN_CENTER);
                    itemsubtotal.setBorder(0);
                    itemsubtotal.setHorizontalAlignment(Element.ALIGN_CENTER);
                    itemtable.addCell(itemsubtotal);

                    PdfPCell itemdiscount = new PdfPCell(new Paragraph(cursor1.getString(6)));
                    itemdiscount.setVerticalAlignment(Element.ALIGN_CENTER);
                    itemdiscount.setBorder(0);
                    itemdiscount.setHorizontalAlignment(Element.ALIGN_CENTER);
                    itemtable.addCell(itemdiscount);

                    PdfPCell itemtotal = new PdfPCell(new Paragraph(cursor1.getString(7)));
                    itemtotal.setVerticalAlignment(Element.ALIGN_CENTER);
                    itemtotal.setBorder(0);
                    itemtotal.setHorizontalAlignment(Element.ALIGN_CENTER);
                    itemtable.addCell(itemtotal);

                    document.add(itemtable);

                }
                LineSeparator lineSeparator = new LineSeparator();
                document.add(lineSeparator);

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

                PdfPCell grandtot = new PdfPCell(new Paragraph("Sub Total: "));
                grandtot.setVerticalAlignment(Element.ALIGN_CENTER);
                grandtot.setHorizontalAlignment(Element.ALIGN_CENTER);
                granddtotal.addCell(grandtot);

                PdfPCell grandtot2 = new PdfPCell(new Paragraph("Rs. " + getgrandtotal));
                grandtot2.setVerticalAlignment(Element.ALIGN_CENTER);
                grandtot2.setHorizontalAlignment(Element.ALIGN_CENTER);
                granddtotal.addCell(grandtot2);

                document.add(granddtotal);


                PdfPTable grandtotal = new PdfPTable(6);
                grandtotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
                float[] columnWidthsdis = {1, 1, 1, 1, 1, 1.1f};
                grandtotal.setWidthPercentage(100);
                grandtotal.setWidths(columnWidthsdis);

                PdfPCell m = new PdfPCell(new Paragraph());
                m.setBorder(0);
                grandtotal.addCell(m);
                PdfPCell n = new PdfPCell(new Paragraph());
                n.setBorder(0);
                grandtotal.addCell(n);
                PdfPCell o = new PdfPCell(new Paragraph());
                o.setBorder(0);
                grandtotal.addCell(o);
                PdfPCell p = new PdfPCell(new Paragraph());
                p.setBorder(0);
                grandtotal.addCell(p);

                PdfPCell grandtota = new PdfPCell(new Paragraph("Grand Total: ", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
                grandtota.setVerticalAlignment(Element.ALIGN_CENTER);
                grandtota.setHorizontalAlignment(Element.ALIGN_CENTER);
                // grandtota.addElement((Element) new Font(Font.FontFamily.HELVETICA,10,Font.BOLD));
                grandtota.setNoWrap(true);
                grandtotal.addCell(grandtota);

                PdfPCell grandtota2 = new PdfPCell(new Paragraph("Rs. " + Math.round(Double.parseDouble(getgrandtotal)), new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
                grandtota2.setVerticalAlignment(Element.ALIGN_CENTER);
                grandtota2.setHorizontalAlignment(Element.ALIGN_CENTER);
                grandtotal.addCell(grandtota2);

                document.add(grandtotal);

                PdfPTable typeinword = new PdfPTable(1);
                typeinword.setWidthPercentage(100);

                PdfPCell typeinwordcell = new PdfPCell(new Paragraph("GRAND TOTAL IN WORDS :\nRs. " + new TypeToWord().typetoword((int) Math.round(Double.parseDouble(getgrandtotal))).toUpperCase() + "ONLY /-", new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD)));
                typeinwordcell.setNoWrap(false);
                typeinwordcell.setHorizontalAlignment(Element.ALIGN_LEFT);
                typeinwordcell.setVerticalAlignment(Element.ALIGN_CENTER);
                typeinword.addCell(typeinwordcell);

                document.add(typeinword);

                Paragraph authsignfor = new Paragraph("\n\n\nFOR OM SHOPPING POINT", new Font(Font.FontFamily.HELVETICA, 8, Font.BOLDITALIC));
                authsignfor.setAlignment(Element.ALIGN_RIGHT);
                document.add(authsignfor);

                Paragraph authsign = new Paragraph("\n\n\n\nAuthorised Signatory", new Font(Font.FontFamily.HELVETICA, 8, Font.BOLDITALIC));
                authsign.setAlignment(Element.ALIGN_RIGHT);
                document.add(authsign);

                Paragraph note = new Paragraph("\n\n...Thank You Visit Again...", new Font(Font.FontFamily.HELVETICA, 13, Font.BOLDITALIC));
                note.setAlignment(Element.ALIGN_CENTER);
                document.add(note);


            /*

            document close

             */

                document.close();
                Toast.makeText(this, file1.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "File path is " + file1.getAbsolutePath());



            /*

            for veiwing created invoice

             */
//           // Get the URI Path of file.
                Uri uriPdfPath = FileProvider.getUriForFile(this, getApplicationContext().getPackageName(), file1);
                Log.d("pdfPath", "" + uriPdfPath);

                // Start Intent to View PDF from the Installed Applications.
                Intent pdfOpenIntent = new Intent(Intent.ACTION_VIEW);
                pdfOpenIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                pdfOpenIntent.setClipData(ClipData.newRawUri("", uriPdfPath));
                pdfOpenIntent.setDataAndType(uriPdfPath, "application/pdf");
                pdfOpenIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                try {
                    startActivity(pdfOpenIntent);
                } catch (ActivityNotFoundException activityNotFoundException) {
                    Toast.makeText(this, "There is no app to load corresponding PDF", Toast.LENGTH_LONG).show();

                }

                SharedPreferences sharedPreferences1 = getSharedPreferences("check_exist_file", MODE_PRIVATE);
                SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                editor1.putString("file_name", file1.getAbsolutePath());
                editor1.apply();

                finish();

            } catch (Exception e) {
                Toast.makeText(this, "hi\n" + e, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "hihihih " + e);
                e.printStackTrace();
            }

        } else {
            Toast.makeText(this, "This invoice is already created in this path -> " + file1.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        }


    }

}


class TypeToWord {
    public String typetoword(int n) {

        String word = "";
        word += type2word((n / 10000000) % 100000, "crore");
        word += type2word((n / 100000) % 100, "lakh");
        word += type2word((n / 1000) % 100, "thousand");
        word += type2word((n / 100) % 10, "hundred");
        word += type2word(n % 100, "");

        return word;
    }

    public String type2word(int n, String s) {

        String[] ones = {"", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten", "eleven"
                , "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"};
        String[] tens = {"", "", "twenty", "thirty", "fourty", "fifty", "sixty", "seventy", "eighty", "ninty"};
        String word = "";
        if (n < 20) {
            word += ones[n];
        } else {
            word += tens[n / 10] + " " + ones[n % 10];
        }

        if (n > 0) {
            word += " " + s + " ";
        }
        return word;
    }


}