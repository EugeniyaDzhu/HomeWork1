package com.mvc.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Collection;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class GeneratePdfReport {

        private static final Logger logger = LoggerFactory.getLogger(GeneratePdfReport.class);

        public static <T extends PdfReport> ByteArrayInputStream report(Collection<T> collection, String header) {

            Document document = new Document();
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            try {

                PdfPTable table = new PdfPTable(1);
                table.setWidthPercentage(100);

                Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

                PdfPCell hcell;
                hcell = new PdfPCell(new Phrase(header, headFont));
                hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(hcell);

                Iterator<T> iterator = collection.iterator();

                while(iterator.hasNext()) {

                    PdfPCell cell;

                    T next = iterator.next();
                    cell = new PdfPCell(new Phrase(next.toReportString()));
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);
                }

                PdfWriter.getInstance(document, out);
                document.open();
                document.add(table);

                document.close();

            } catch (DocumentException ex) {

                logger.error("Error occurred: {0}", ex);
            }

            return new ByteArrayInputStream(out.toByteArray());
        }
    }
