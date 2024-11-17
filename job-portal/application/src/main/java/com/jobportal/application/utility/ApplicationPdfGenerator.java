package com.jobportal.application.utility;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.jobportal.application.entity.Application;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Component
public class ApplicationPdfGenerator {

    public byte[] generateApplicationsPdf(List<Application> applications) throws DocumentException {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            // Add title
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("Job Applications Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n"));

            // Create table
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);

            // Add headers
            String[] headers = {"Application ID", "Applicant ID", "Status", "Resume URL", "Cover Letter"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell.setPadding(5);
                table.addCell(cell);
            }

            // Add data
            for (Application application : applications) {
                table.addCell(new Phrase(String.valueOf(application.getId())));
                table.addCell(new Phrase(String.valueOf(application.getApplicantId())));
                table.addCell(new Phrase(application.getStatus().toString()));
                table.addCell(new Phrase(application.getResumeUrl() != null ? application.getResumeUrl() : ""));
                table.addCell(new Phrase(application.getCoverLetter() != null ?
                        application.getCoverLetter().length() > 100 ?
                                application.getCoverLetter().substring(0, 100) + "..." :
                                application.getCoverLetter()
                        : ""));
            }

            document.add(table);

        } finally {
            document.close();
        }

        return baos.toByteArray();
    }
}
