package org.example.service;

import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.example.client.TodoistAPI;
import org.example.model.Attachment;
import org.example.model.CommentResponse;
import org.example.model.SectionResponse;
import org.example.model.TaskResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;


public class WordBuilder {

    private static final Logger log = LoggerFactory.getLogger(WordBuilder.class);
    public static final String BULLET = "   -";
    private final XWPFDocument document;
    private final String wordName;
    private final String outputPath;
    private final String code;
    private final TodoistAPI api;

    public WordBuilder(String wordName, String outputPath, String code, String projectName, TodoistAPI api) {
        this.document = new XWPFDocument();
        this.wordName = wordName;
        this.outputPath = outputPath;
        this.api = api;
        this.code = code;
        createParagraph(projectName, ParagraphAlignment.CENTER, 20, false);
    }

    public void export(SectionResponse section) {
        createParagraph(String.format(" - %s - ", section.getName()), ParagraphAlignment.CENTER, 18, true);
    }

    public void export(TaskResponse task, List<CommentResponse> comments) {

        log.debug("Exporting task = {}", task.getContent());
        createParagraph(String.format("%s - %s", task.getContent(), task.getDescription()), ParagraphAlignment.CENTER, 14, true);

        comments.forEach(c -> {
            log.debug("Exporting comment = {}", c.getContent());
            if (c.getAttachment() == null) {
                createParagraph(String.format("%s %s ", BULLET, c.getContent()), ParagraphAlignment.LEFT, 14, false);
            } else {
                createImageParagraph(c.getAttachment());
            }
        });
    }

    private void createImageParagraph(Attachment attachment) {
        if (attachment.getFileUrl() == null || attachment.getFileType() == null)
            return;

        XWPFParagraph image = document.createParagraph();
        image.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun imageRun = image.createRun();
        try {
            log.debug("Fetching file - fileUrl = {}", attachment.getFileUrl());
            InputStream imageStream = api.getAttachment(code, attachment.getFileUrl(), attachment.getFileType());
            log.debug("Exporting file - attachment = {} ", attachment);
            if (attachment.getFileType().equals(MediaType.APPLICATION_PDF_VALUE)) {
                saveFile(attachment, imageStream);
            } else {
                imageRun.addPicture(imageStream,
                        getPictureType(attachment.getFileType()), attachment.getFileName(),
                        Units.toEMU(437), Units.toEMU(111));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void saveFile(Attachment attachment, InputStream imageStream) {
        try (FileOutputStream out = new FileOutputStream(Path.of(outputPath, attachment.getFileName()).toString())) {
            out.write(imageStream.readAllBytes());
        } catch (Exception ex) {
            log.error("Exception while saving file = {}", attachment.getFileName(), ex);
        }
    }

    private int getPictureType(String fileType) {
        if (fileType.equals(MediaType.IMAGE_PNG_VALUE)) {
            return XWPFDocument.PICTURE_TYPE_PNG;
        } else if (fileType.equals(MediaType.IMAGE_JPEG_VALUE)) {
            return XWPFDocument.PICTURE_TYPE_JPEG;
        }

        throw new IllegalArgumentException("Unsupported fileType = " + fileType);
    }

    private void createParagraph(String text, ParagraphAlignment alignment, int fontSize, boolean bold) {
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(alignment);

        XWPFRun run = paragraph.createRun();
        run.setFontFamily("Times New Roman");
        run.setFontSize(fontSize);
        run.setText(text);
        run.setBold(bold);
    }

    public void build() {
        try (FileOutputStream out = new FileOutputStream(wordName)) {
            document.write(out);
            document.close();
            log.info("Export finished! word = {}", wordName);
        } catch (Exception ex) {
            log.error("Export failed! word = {}", wordName, ex);
        }
    }

}
