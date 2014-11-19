package org.guiceside.support.upload;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.guiceside.commons.FileObject;
import org.guiceside.commons.lang.DateFormatUtil;
import org.guiceside.commons.lang.StringUtils;
import org.guiceside.support.file.FileManager;
import org.guiceside.support.properties.PropertiesConfig;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 该类使用Apache FileUpload组件进行封装，管理文件上传
 * </p>
 *
 * @author zhenjia <a href='mailto:zhenjiaWang@gmail.com'>email</a>
 * @version 1.0 2008-9-17
 * @since JDK1.5
 */
public class FileUploadManager {
    private static final Logger log = Logger.getLogger(FileUploadManager.class);

    private PropertiesConfig propertiesConfig = null;

    private DiskFileItemFactory diskFileItemFactory = null;

    private ServletFileUpload servletFileUpload = null;

    private String path = null;

    private String platform = null;

    private ProgressListener progressListener=null;

    /**
     * 将当前Appliaction绝对路径作为参数实例化组件
     *
     * @param path
     */
    public FileUploadManager(String path) {
        propertiesConfig = new PropertiesConfig("upload.properties");
        if (propertiesConfig.getProperties() == null) {
            log
                    .error(new FileUploadManagerException(
                            "upload Properties not found (did you forget create file upload.properties ?)"));
            throw new FileUploadManagerException(
                    "upload Properties not found (did you forget create file upload.properties ?)");
        }
        platform = propertiesConfig.getString("platform");
        this.path = path;
    }
    public FileUploadManager(String path,ProgressListener progressListener) {
        propertiesConfig = new PropertiesConfig("upload.properties");
        if (propertiesConfig.getProperties() == null) {
            log
                    .error(new FileUploadManagerException(
                            "upload Properties not found (did you forget create file upload.properties ?)"));
            throw new FileUploadManagerException(
                    "upload Properties not found (did you forget create file upload.properties ?)");
        }
        platform = propertiesConfig.getString("platform");
        this.path = path;
        this.progressListener=progressListener;
    }

    public void deleteFile(String uploadKey, int year, int month, int day, String fileName) {
        FileManager.deleteFile(parsePathByDate(parsePath(uploadKey), year, month, day) + "/" + fileName);
    }

    private void init() {
        diskFileItemFactory = new DiskFileItemFactory();
        diskFileItemFactory.setSizeThreshold(propertiesConfig
                .getInt("sizeThreshold"));
        File tempRepository = FileManager.newFloder(parsePath("repository"));
        diskFileItemFactory.setRepository(tempRepository);
        servletFileUpload = new ServletFileUpload(diskFileItemFactory);
        servletFileUpload.setSizeMax(propertiesConfig.getInt("sizeMax"));
        if(this.progressListener!=null){
            servletFileUpload.setProgressListener(progressListener);
        }
    }

    public String parsePath(String uploadKey) {
        String uploadPath = propertiesConfig.getString(uploadKey);
        if (platform.toLowerCase().equals("windows")) {
            if (uploadPath.startsWith("/")) {
                // 相对路径
                uploadPath = path + uploadPath.substring(1);
            }
        } else if (platform.toLowerCase().equals("linux")) {
            if (!uploadPath.startsWith("/")) {
                // 相对路径
                uploadPath = path + uploadPath.substring(1);
            }
        }
        return uploadPath;
    }

    /**
     * 通过解析HttpServletRequest对象获取当前上传文件集合
     *
     * @param httpServletRequest
     * @return 当前上传文件集合
     */
    @SuppressWarnings("unchecked")
    public List<FileObject> getFiles(HttpServletRequest httpServletRequest) {
        init();
        try {
            if (!ServletFileUpload.isMultipartContent(httpServletRequest)) {
                //html5;
                try {
                    String fileName = httpServletRequest.getHeader("x-file-name");
                    if (StringUtils.isBlank(fileName)) {
                        fileName = httpServletRequest.getHeader("content-disposition");
                        int start = fileName.indexOf("filename=");

                        fileName = fileName.substring(start + "filename=".length());
                        if (StringUtils.isNotBlank(fileName)) {
                            if(fileName.startsWith("\"")){
                                fileName=fileName.substring(1,fileName.length());
                            }
                            if(fileName.endsWith("\"")){
                                fileName=fileName.substring(0,fileName.length()-1);
                            }
                        }
                    }
                    if (StringUtils.isNotBlank(fileName)) {
                        fileName = URLDecoder.decode(fileName, "UTF-8");
                        if (StringUtils.isNotBlank(fileName)) {
                            int size = httpServletRequest.getContentLength();
                            if (size > 0) {
                                InputStream is = new BufferedInputStream(httpServletRequest.getInputStream());
                                if (is != null) {
                                    List<FileObject> fileObjects = new ArrayList<FileObject>();
                                    FileObject fileObject = new FileObject();
                                    fileObject.setName(fileName);
                                    fileObject.setFileName(getFileName(fileName));
                                    fileObject.setPostfix(getPostfix(fileName));
                                    fileObject.setSize(size);
                                    fileObject.setInputStream(is);
                                    fileObjects.add(fileObject);
                                    return fileObjects;
                                }

                            }
                        }
                    }

                } catch (Exception e) {

                }

                if (log.isDebugEnabled()) {
                    log
                            .debug("upload failed ! must set form attribute enctype :multipart/form-data");
                }
                return null;
            }
            List<FileItem> items = servletFileUpload
                    .parseRequest(httpServletRequest);
            if (items != null && !items.isEmpty()) {
                List<FileObject> fileObjects = new ArrayList<FileObject>();
                for (FileItem fileItem : items) {
                    if (fileItem.isFormField()) {
                        continue;
                    }
                    if (StringUtils.isBlank(fileItem.getName())) {
                        continue;
                    }
                    FileObject fileObject = new FileObject();
                    fileObject.setName(fileItem.getName());
                    fileObject.setFieldName(fileItem.getFieldName());
                    fileObject.setFileName(getFileName(fileObject.getName()));
                    fileObject.setPostfix(getPostfix(fileObject.getName()));
                    fileObject.setContentType(fileItem.getContentType());
                    fileObject.setSize(fileItem.getSize());
                    fileObject.setFileItem(fileItem);
                    fileObjects.add(fileObject);
                }
                return fileObjects;
            }
        } catch (FileUploadException e) {
            log.error(new FileUploadManagerException(e));
            throw new FileUploadManagerException(e);
        }
        return null;
    }

    /**
     * 通过指定uploadKey,fileItem filename进行文件写入
     * 按年月日进行分割文件夹
     *
     * @param uploadKey
     * @param fileItem
     * @param fileName
     */
    public void write(String uploadKey, FileItem fileItem, String fileName, Date currentDate) {
        String uploadPath = parsePathByDate(parsePath(uploadKey), currentDate);
        FileManager.newFloder(uploadPath);
        File toFile = FileManager
                .getFile(uploadPath + "/" + fileName);
        try {
            fileItem.write(toFile);
            if (log.isDebugEnabled()) {
                log.debug("Upload Successful ! Uploaded to :"
                        + toFile.getPath());
            }
        } catch (Exception e) {
            log.error(new FileUploadManagerException(e));
            throw new FileUploadManagerException(e);
        } finally {
            fileItem.delete();
            if (log.isDebugEnabled()) {
                log.debug("upload cleanUp done !");
            }
        }
    }

    public String parsePathByDate(String uploadKey, Date currentDate) {
        int month = DateFormatUtil.getDayInMonth(currentDate);
        month++;
        return uploadKey + "/" + DateFormatUtil.getDayInYear(currentDate) + "/"
                + month + "/"
                + DateFormatUtil.getDayInDay(currentDate);
    }

    public String parsePathByDate(String uploadKey, int year, int month, int day) {
        return uploadKey + "/" + year + "/"
                + month + "/"
                + day;
    }

    public File getFile(String uploadKey, int year, int month, int day, String fileName) {
        return FileManager.getFile(parsePathByDate(parsePath(uploadKey), year, month, day) + "/" + fileName);
    }

    public String getFileFloder(String uploadKey, Date currentDate) {
        return parsePathByDate(parsePath(uploadKey), currentDate);
    }

    public String getFilePath(String uploadKey, int year, int month, int day, String fileName) {
        return parsePathByDate(parsePath(uploadKey), year, month, day) + "/" + fileName;
    }
    private String getFileName(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            throw new FileUploadManagerException("fileName Cant not be empty");
        }
        fileName = fileName.replaceAll("\\\\", "/");
        int beginIndex = fileName.lastIndexOf("/");
        if (beginIndex != -1) {
            fileName = fileName.substring(beginIndex + 1);
        }
        beginIndex = fileName.lastIndexOf(".");
        if (beginIndex != -1) {
            fileName = fileName.substring(0, beginIndex);
        }
        return fileName;
    }

    private String getPostfix(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            throw new FileUploadManagerException("fileName Cant not be empty");
        }
        int beginIndex = fileName.lastIndexOf(".");
        if (beginIndex != -1) {
            fileName = fileName.substring(beginIndex + 1);
        } else {
            fileName = "";
        }
        return fileName;
    }
}
