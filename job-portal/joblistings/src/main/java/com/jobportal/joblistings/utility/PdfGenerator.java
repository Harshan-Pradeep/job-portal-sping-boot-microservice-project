package com.jobportal.joblistings.utility;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.jobportal.joblistings.entity.Listing;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Component
public class PdfGenerator {

    public byte[] generateListingsPdf(List<Listing> listings) throws DocumentException {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            // Add title
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("Job Listings Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n"));

            // Create table
            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);

            // Add headers
            String[] headers = {"Title", "Company", "Location", "Salary Range", "Experience", "Job Type"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell.setPadding(5);
                table.addCell(cell);
            }

            // Add data
            for (Listing listing : listings) {
                table.addCell(new Phrase(listing.getTitle()));
                table.addCell(new Phrase(listing.getCompany()));
                table.addCell(new Phrase(listing.getLocation() != null ? listing.getLocation() : ""));
                table.addCell(new Phrase(listing.getSalaryRange() != null ? listing.getSalaryRange() : ""));
                table.addCell(new Phrase(listing.getExperienceLevel() != null ? listing.getExperienceLevel() : ""));
                table.addCell(new Phrase(listing.getJobType().toString()));
            }

            document.add(table);

        } finally {
            document.close();
        }

        return baos.toByteArray();
    }
}
