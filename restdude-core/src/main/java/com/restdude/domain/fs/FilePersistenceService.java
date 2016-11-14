/**
 * calipso-hub-framework - A full stack, high level framework for lazy application hackers.
 * Copyright © 2005 Manos Batsis (manosbatsis gmail)
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.restdude.domain.fs;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.imgscalr.Scalr;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Abstract file persistence
 */
public interface FilePersistenceService {

    public static final String BEAN_ID = "filePersistenceService";
    public static final String[] MIMES_IMAGE = {"image/jpeg", "image/png", "image/gif", "image/x-ms-bmp"};
    public static final Map<String, String> MIME_FORMATS = new HashMap<String, String>();

    public static String getImageIoFormat(String contentType) {
        if (MIME_FORMATS.size() == 0) {
            MIME_FORMATS.put("image/jpeg", "jpeg");
            MIME_FORMATS.put("image/png", "png");
            MIME_FORMATS.put("image/gif", "gif");
        }
        return MIME_FORMATS.get(contentType);
    }

    public static boolean isImage(String contentType) {
        return ArrayUtils.contains(MIMES_IMAGE, contentType.toLowerCase());
    }

    public static void validateContentType(String contentType, FilePersistence config) {
        if (ArrayUtils.isNotEmpty(config.mimeTypeIncludes()) && !ArrayUtils.contains(config.mimeTypeIncludes(), contentType.toLowerCase())) {
            throw new IllegalArgumentException("Unacceptable MIME type: " + contentType);
        }
    }

    public default String saveFile(Field fileField, MultipartFile multipartFile, String filename) {
        FileDTO file;
        try {
            file = new FileDTO.Builder()
                    .contentLength(multipartFile.getSize())
                    .contentType(multipartFile.getContentType())
                    .in(multipartFile.getInputStream())
                    .path(filename).build();

        } catch (IOException e) {
            throw new RuntimeException("Failed persisting file", e);
        }
        return this.saveFile(fileField, file);
    }

    public default void deleteFile(Field fileField, MultipartFile multipartFile, String filename) {
        FileDTO file;
        try {
            file = new FileDTO.Builder()
                    .path(filename).build();

        } catch (Exception e) {
            throw new RuntimeException("Failed deleting file", e);
        }
        this.deleteFile(fileField, file);
    }

    /**
     * The method saves the given multipart file to the path specified, ignoring the original file name.
     * @param fileField
     * @param file
     * @return the URL for the saved file
     */
    public default String saveFile(Field fileField, FileDTO file) {
        String url = null;
        try {


            FilePersistence config = fileField.getAnnotation(FilePersistence.class);
            // ensure accepted content type
            validateContentType(file.getContentType(), config);
            BufferedImage img = ImageIO.read(file.getIn());

            // if image that needs scaling
            if (isImage(file.getContentType()) && (config.maxHeight() > 0 || config.maxWidth() > 0)) {
                url = saveScaled(img, file.getContentType(), config.maxWidth(), config.maxHeight(), file.getPath());
            } else {
                url = saveFile(img, file.getContentLength(), file.getContentType(), file.getPath());
            }

            // generate previews?
            Map<String, FilePersistencePreview> previews = getPreviews(fileField);
            if (isImage(file.getContentType()) && MapUtils.isNotEmpty(previews)) {
                for (String key : previews.keySet()) {
                    FilePersistencePreview previewConfig = previews.get(key);
                    saveScaled(img, file.getContentType(), previewConfig.maxWidth(), previewConfig.maxHeight(), file.getPath() + "_" + key);
                }
            }
            //cleanup
            file.getIn().close();

        } catch (IOException e) {
            throw new RuntimeException("Failed persisting file", e);
        }

        return url;
    }

    /**
     * The method saves the given multipart file to the path specified, ignoring the original file name.
     *
     * @param fileField
     * @param file
     * @return the URL for the saved file
     */
    public default void deleteFile(Field fileField, FileDTO file) {

        FilePersistence config = fileField.getAnnotation(FilePersistence.class);

        // delete file
        List<String> keys = new LinkedList<String>();
        keys.add(file.getPath());

        // generate previews?
        Map<String, FilePersistencePreview> previews = getPreviews(fileField);
        if (isImage(file.getContentType()) && MapUtils.isNotEmpty(previews)) {
            for (String key : previews.keySet()) {
                keys.add(file.getPath() + "_" + key);
            }
        }

        deleteFiles(keys.toArray(new String[keys.size()]));

    }


    public default String saveScaled(BufferedImage file, String contentType, int maxWidth, int maxHeight, String path) throws IOException {
        String url;
        FileDTO tmp = scaleFile(file, contentType, maxWidth, maxHeight);
        url = saveFile(tmp.getIn(), tmp.getContentLength(), tmp.getContentType(), path);

        //cleanup
        tmp.getIn().close();
        return url;
    }

    public default Map<String, FilePersistencePreview> getPreviews(Field fileField) {
        Map<String, FilePersistencePreview> previews = new HashMap<String, FilePersistencePreview>();
        if (fileField.isAnnotationPresent(FilePersistencePreviews.class)) {
            FilePersistencePreview[] tmp = fileField.getAnnotation(FilePersistencePreviews.class).value();
            if (tmp != null) {
                for (int i = 0; i < tmp.length; i++) {
                    FilePersistencePreview preview = tmp[i];
                    previews.put(preview.maxWidth() + "x" + preview.maxHeight(), preview);
                }
            }
        }
        if (fileField.isAnnotationPresent(FilePersistencePreview.class)) {
            FilePersistencePreview[] tmp = fileField.getAnnotationsByType(FilePersistencePreview.class);
            for (int i = 0; i < tmp.length; i++) {
                FilePersistencePreview preview = tmp[i];
                previews.put(preview.maxWidth() + "x" + preview.maxHeight(), preview);
            }
        }
        return previews;
    }

    public default FileDTO scaleFile(BufferedImage img, String contentType, int maxWidth, int maxHeight) throws IOException {
        BufferedImage scaled = Scalr.resize(img,
                Scalr.Method.SPEED,
                Scalr.Mode.FIT_TO_WIDTH,
                maxWidth,
                maxHeight,
                Scalr.OP_ANTIALIAS);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(scaled, getImageIoFormat(contentType), os);
        FileDTO scaledFile = new FileDTO.Builder()
                .contentLength(os.size())
                .contentType(contentType)
                .in(new ByteArrayInputStream(os.toByteArray()))
                .build();
        os.close();
        return scaledFile;
    }


    public default String saveFile(BufferedImage img, long contentLength, String contentType, String path) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(img, getImageIoFormat(contentType), os);
        return saveFile(new ByteArrayInputStream(os.toByteArray()), contentLength, contentType, path);

    }

    public String saveFile(InputStream in, long contentLength, String contentType, String path);

    public void deleteFiles(String... path);

}