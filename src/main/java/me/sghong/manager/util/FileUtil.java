package me.sghong.manager.util;

import lombok.RequiredArgsConstructor;
import me.sghong.manager.app.common.dto.FileDto;
import org.apache.tika.Tika;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class FileUtil {
    private static final Tika tika = new Tika();

    public static boolean validIFileMimetype(MultipartFile file, String valiType) {
        try {
            List<String> notValidTypeList = switch (valiType) {
                case "image" -> Arrays.asList("image/jpeg", "image/pjpeg", "image/png", "image/gif", "image/bmp", "image/x-windows-bmp");
                case "xls" -> Arrays.asList("application/x-tika-msoffice", "application/x-tika-ooxml");
                default -> Arrays.asList("image/jpeg", "image/pjpeg", "image/png", "image/gif", "image/bmp", "image/x-windows-bmp", "application/x-tika-msoffice", "application/x-tika-ooxml", "application/pdf", "application/zip", "application/x-zip-compressed");
            };

            List<String> notValidExtensionList = switch (valiType) {
                case "image" -> Arrays.asList("jpeg", "jpg", "png", "gif", "bmp");
                case "xls" -> Arrays.asList("xls", "xlsx");
                default -> Arrays.asList("jpeg", "jpg", "png", "gif", "bmp", "xls", "xlsx", "ppt", "pptx", "pdf", "zip");
            };

            String mimeType = tika.detect(file.getInputStream());
            String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());

            return notValidTypeList.stream().anyMatch(notValidType -> notValidType.equalsIgnoreCase(mimeType)) && notValidExtensionList.stream().anyMatch(notExt -> notExt.equalsIgnoreCase(ext));
        } catch (IOException ex) {
            return false;
        }
    }

    public static List<FileDto> uploadFiles(final List<MultipartFile> multipartFiles, String subDir) {
        List<FileDto> files = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (multipartFile.isEmpty()) {
                continue;
            }
            files.add(uploadFile(multipartFile, subDir));
        }
        return files;
    }

    /**
     * 단일 파일 업로드
     * @param multipartFile - 파일 객체
     * @return DB에 저장할 파일 정보
     */
    public static FileDto uploadFile(final MultipartFile multipartFile, String subDir) {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String saveName = generateSaveFilename(multipartFile.getOriginalFilename());
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")).toString();
        String uploadPath;
        if (subDir.isEmpty()) {
            uploadPath = getUploadPath(today) + File.separator + saveName;
        } else {
            uploadPath = getUploadPath(subDir.replace("/", File.separator) + File.separator + today) + File.separator + saveName;
        }
        File uploadFile = new File(uploadPath);

        try {
            multipartFile.transferTo(uploadFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return FileDto.builder()
                .originFileName(multipartFile.getOriginalFilename())
                .saveFileName(uploadPath.replace(Constants.uploadrootPath, Constants.fileUploadroot).replace(File.separator, "/"))
                .fileSize(multipartFile.getSize())
                .build();
    }

    /**
     * 저장 파일명 생성
     * @param filename 원본 파일명
     * @return 디스크에 저장할 파일명
     */
    private static String generateSaveFilename(final String filename) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String extension = StringUtils.getFilenameExtension(filename);
        return uuid + "." + extension;
    }

    /**
     * 업로드 경로 반환
     * @return 업로드 경로
     */
    private static String getUploadPath() {
        return makeDirectories(Constants.uploadrootPath);
    }

    /**
     * 업로드 경로 반환
     * @param addPath - 추가 경로
     * @return 업로드 경로
     */
    private static String getUploadPath(final String addPath) {
        return makeDirectories(Constants.uploadrootPath + File.separator + addPath);
    }

    /**
     * 업로드 폴더(디렉터리) 생성
     * @param path - 업로드 경로
     * @return 업로드 경로
     */
    private static String makeDirectories(final String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.getPath();
    }

    public static boolean deleteFile(String fileName) {
        try {
            fileName = fileName.replace(Constants.fileUploadroot, "").replace("/", File.separator);
            File file = new File(Constants.uploadrootPath + fileName);
            return file.delete();
        } catch (Exception e) {
            return false;
        }
    }

    public static String createThumbnail(String fileName, int width, int height) {
        try {
            String path = fileName.substring(0, fileName.lastIndexOf("/"));
            String orgfileName = fileName.substring(fileName.lastIndexOf("/") + 1);

            String thumbnailPath = path + "/thumb/" + orgfileName;

            makeDirectories(Constants.uploadrootPath + path.replace(Constants.fileUploadroot, "").replace("/", File.separator) + File.separator + "thumb");

            File orgFile = new File(Constants.uploadrootPath + path.replace(Constants.fileUploadroot, "").replace("/", File.separator) + File.separator + orgfileName);
            File thumbnail = new File(Constants.uploadrootPath + path.replace(Constants.fileUploadroot, "").replace("/", File.separator) + File.separator + "thumb" + File.separator + orgfileName);

            BufferedImage readImg = ImageIO.read(orgFile);
            BufferedImage thumbImg = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

            Graphics2D graphic = thumbImg.createGraphics();

            graphic.drawImage(readImg, 0, 0, width, height, null);

            ImageIO.write(thumbImg, "jpg", thumbnail);

            return thumbnailPath;
        } catch (IOException ex) {
            return null;
        }
    }

    public static String createProductImage(String fileName, int width, int height) {
        try {
            String path = fileName.substring(0, fileName.lastIndexOf("/"));
            String orgfileName = fileName.substring(fileName.lastIndexOf("/") + 1);

            String productImgFileName = generateSaveFilename(orgfileName);

            File orgFile = new File(Constants.uploadrootPath + path.replace(Constants.fileUploadroot, "").replace("/", File.separator) + File.separator + orgfileName);
            File thumbnail = new File(Constants.uploadrootPath + path.replace(Constants.fileUploadroot, "").replace("/", File.separator) + File.separator + productImgFileName);

            BufferedImage readImg = ImageIO.read(orgFile);
            BufferedImage thumbImg = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

            Graphics2D graphic = thumbImg.createGraphics();

            graphic.drawImage(readImg, 0, 0, width, height, null);

            ImageIO.write(thumbImg, "jpg", thumbnail);

            return path + "/" + productImgFileName;
        } catch (IOException ex) {
            return null;
        }
    }

    public static boolean FileExists(String fileName) {
        String path = fileName.substring(0, fileName.lastIndexOf("/"));
        String orgfileName = fileName.substring(fileName.lastIndexOf("/") + 1);

        File orgFile = new File(Constants.uploadrootPath + path.replace(Constants.fileUploadroot, "").replace("/", File.separator) + File.separator + orgfileName);

        return orgFile.exists();
    }
}
