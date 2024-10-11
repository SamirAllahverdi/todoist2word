package org.example.tool;

import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.example.client.TodoistAPI;
import org.example.model.Attachment;
import org.example.model.Comment;
import org.example.model.Section;
import org.example.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;


public class WordBuilder {

    private static final Logger log = LoggerFactory.getLogger(WordBuilder.class);
    public static final String BULLET = "   -";
    private final XWPFDocument document;
    private final String wordName;
    private final String outputPath;
    private final TodoistAPI api;

    public WordBuilder(String wordName, String outputPath, String projectName, TodoistAPI api) {
        this.document = new XWPFDocument();
        this.wordName = wordName;
        this.outputPath = outputPath;
        this.api = api;
        createParagraph(projectName, ParagraphAlignment.CENTER, 20, false);
    }

    public void export(Section section) {
        createParagraph(String.format(" - %s - ", section.getName()), ParagraphAlignment.CENTER, 18, true);
    }

    public void export(Task task, List<Comment> comments) {

        log.info("Exporting task = {}", task.getContent());
        createParagraph(String.format("%s - %s", task.getContent(), task.getDescription()), ParagraphAlignment.CENTER, 14, true);

        comments.forEach(c -> {
            log.info("Exporting comment = {}", c.getContent());
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
            log.info("Fetching file - fileUrl = {}", attachment.getFileUrl());
            InputStream imageStream = api.getAttachment(attachment.getFileUrl(), attachment.getFileType());
            log.info("Exporting file - attachment = {} ", attachment);
            if (imageStream == null)
                return;

            if (attachment.getFileType().equals("application/pdf")) {
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
        if (fileType.equals("image/png")) {
            return XWPFDocument.PICTURE_TYPE_PNG;
        } else if (fileType.equals("image/jpeg")) {
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
            log.info("Export finished! word = {}", wordName);
        } catch (Exception ex) {
            log.error("Export failed! word = {}", wordName, ex);
        } finally {
            try {
                document.close();
            } catch (IOException e) {
                log.error("Exception in document close name = {}", wordName, e);
            }
        }
    }

}
