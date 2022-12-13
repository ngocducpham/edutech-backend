package com.edutech.api.controller;

import com.edutech.api.constant.EduTechConstant;
import com.edutech.api.dto.ApiMessageDto;
import com.edutech.api.dto.resource.UploadFileResponse;
import com.edutech.api.exception.NotFoundException;
import com.edutech.api.service.FileStorageService;
import com.edutech.api.storage.model.Class;
import com.edutech.api.storage.model.*;
import com.edutech.api.storage.repository.*;
import com.edutech.api.utils.AESUtils;
import com.edutech.api.utils.FileStorageProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/v1/resource")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class ResourceController extends ABasicController {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);
    @Autowired
    private ClassRepository classRepository;
    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private ChapterRepository chapterRepository;
    @Autowired
    private SyllabusRepository syllabusRepository;
    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private ClassNewsRepository newsRepository;

    public UploadFileResponse studentUploadFileAssignment(MultipartFile file, Long classId, Long assignmentId) {
        Long currentId = getCurrentUserId();
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(()-> new NotFoundException("Assignment not found"));
        Lesson lesson = lessonRepository.findById(assignment.getLesson().getId())
                .orElseThrow(()-> new NotFoundException("Lesson not found"));
        Chapter chapter = chapterRepository.findById(lesson.getChapter().getId())
                .orElseThrow(()-> new NotFoundException("Chapter not found"));
        Syllabus syllabus = syllabusRepository.findById(chapter.getSyllabus().getId())
                .orElseThrow(()-> new NotFoundException("Syllabus not found"));
        Class aClass = classRepository.findById(classId)
                .orElseThrow(()-> new NotFoundException("Class not found"));
        Boolean checkStudent = false;
        for(Student s : aClass.getStudents()){
            if(s.getId()==getCurrentUserId())
                checkStudent = true;
        }
        if(!checkStudent){
            throw new NotFoundException("This student is not in this class");
        }
        String fileUploadPath = "/edutech/"+aClass.getTeacher().getId().toString()+"/"+syllabus.getId().toString()+"/"+classId+"/ASSIGNMENTS/"+chapter.getId().toString()+"/"+lesson.getId().toString()+"/"+assignmentId;
        FileStorageProperties fp = new FileStorageProperties();
        fp.setUploadDir(fileUploadPath);
        FileStorageService studentUploadFileAssignmentService = new FileStorageService(fp);
        String fileName = studentUploadFileAssignmentService.studentStoreFile(file, currentId);
        String fileS = String.join("_", Arrays.copyOfRange(fileName.split("_"),1, fileName.split("_").length));
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("v1/resource/student-download-file-assignment/")
                .path(classId.toString())
                .path("/")
                .path(assignmentId.toString())
                .path("/")
                .path(fileS)
                .toUriString();
        UploadFileResponse uploadFileResponse = new UploadFileResponse(fileS, fileDownloadUri,
                file.getContentType(), file.getSize());

        return uploadFileResponse;
    }

    @PostMapping("/assignment-upload/{classId}/{assignmentId}")
    public ApiMessageDto<List<UploadFileResponse>> studentUploadMultipleFiles(@RequestParam("files") MultipartFile[] files, @PathVariable Long classId, @PathVariable Long assignmentId) {
        List<UploadFileResponse> msgList = new ArrayList<>();
        for(MultipartFile file:files){
            msgList.add(studentUploadFileAssignment(file, classId, assignmentId));
        }
        ApiMessageDto<List<UploadFileResponse>> response = new ApiMessageDto<>();
        response.setData(msgList);
        response.setMessage("Upload file success");
        return response;
    }

    public UploadFileResponse teacherUploadMaterialLesson(MultipartFile file, Long classId, Long lessonId) {
        Long teacherId = getCurrentUserId();
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(()-> new NotFoundException("Failed to post files, only teacher can process"));
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(()-> new NotFoundException("Lesson not found"));
        Chapter chapter = chapterRepository.findById(lesson.getChapter().getId())
                .orElseThrow(()-> new NotFoundException("Chapter not found"));
        Syllabus syllabus = syllabusRepository.findById(chapter.getSyllabus().getId())
                .orElseThrow(()-> new NotFoundException("Syllabus not found"));
        Class aClass = classRepository.findById(classId)
                .orElseThrow(()-> new NotFoundException("Class not found"));
        if(teacherId!=aClass.getTeacher().getId()){
            throw new NotFoundException("You are not the teacher of this class");
        }
        System.out.println(ServletUriComponentsBuilder.fromCurrentContextPath());
        String fileUploadPath = "/edutech/"+teacherId+"/"+syllabus.getId().toString()+"/"+classId+"/POSTLOAD_MATERIALS/"+chapter.getId().toString()+"/"+lesson.getId().toString();
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(teacherId.toString())
                .path("/")
                .path(syllabus.getId().toString())
                .path("/")
                .path(classId.toString())
                .path("/POSTLOAD_MATERIALS/")
                .path(chapter.getId().toString())
                .path("/")
                .path(lesson.getId().toString())
                .path("/")
                .toUriString();

        FileStorageProperties fp = new FileStorageProperties();
        fp.setUploadDir(fileUploadPath);
        FileStorageService teacherUploadLessonFileService = new FileStorageService(fp);
        String fileName = teacherUploadLessonFileService.storeFile(file);
        fileDownloadUri += fileName;

        UploadFileResponse uploadFileResponse = new UploadFileResponse (fileName, fileDownloadUri,
                file.getContentType(), file.getSize());

        return uploadFileResponse;
    }
    @PostMapping("/materials-upload-lesson/{classId}/{lessonId}")
    public ApiMessageDto<List<UploadFileResponse>> teacherUploadMultipleFilesLesson(@RequestParam("files") MultipartFile[] files, @PathVariable Long classId, @PathVariable Long lessonId) {
        List<UploadFileResponse> msgList = new ArrayList<>();
        for(MultipartFile file:files){
            msgList.add(teacherUploadMaterialLesson(file, classId, lessonId));
        }
        ApiMessageDto<List<UploadFileResponse>> response = new ApiMessageDto<>();
        response.setData(msgList);
        response.setMessage("Upload materials success");
        return response;
    }
    public UploadFileResponse teacherUploadMaterialClass(MultipartFile file, Long classId) {
        Long teacherId = getCurrentUserId();
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(()-> new NotFoundException("Failed to post files, only teacher can process"));
        Class aClass = classRepository.findById(classId)
                .orElseThrow(()-> new NotFoundException("Class not found"));
        Syllabus syllabus = syllabusRepository.findById(aClass.getSyllabus().getId())
                .orElseThrow(()-> new NotFoundException("Syllabus not found"));
        if(teacherId!=aClass.getTeacher().getId()){
            throw new NotFoundException("You are not the teacher of this class");
        }
        System.out.println(ServletUriComponentsBuilder.fromCurrentContextPath());
        String fileUploadPath = "/edutech/"+teacherId+"/"+syllabus.getId().toString()+"/"+classId+"/TEMP/";
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(classId.toString())
                .path("/TEMP/")
                .toUriString();

        FileStorageProperties fp = new FileStorageProperties();
        fp.setUploadDir(fileUploadPath);
        FileStorageService teacherUploadLessonFileService = new FileStorageService(fp);
        String fileName = teacherUploadLessonFileService.storeFile(file);
        fileDownloadUri += fileName;

        UploadFileResponse uploadFileResponse = new UploadFileResponse (fileName, fileDownloadUri,
                file.getContentType(), file.getSize());

        return uploadFileResponse;
    }
    @PostMapping("/materials-upload-class/{classId}")
    public ApiMessageDto<List<UploadFileResponse>> teacherUploadMultipleFilesClass(@RequestParam("files") MultipartFile[] files, @PathVariable Long classId) {
        List<UploadFileResponse> msgList = new ArrayList<>();
        for(MultipartFile file:files){
            msgList.add(teacherUploadMaterialClass(file, classId));
        }
        ApiMessageDto<List<UploadFileResponse>> response = new ApiMessageDto<>();
        response.setData(msgList);
        response.setMessage("Upload file success");
        return response;
    }
    @GetMapping("/student-download-file-lesson/{classId}/{lessonId}/{fileName:.+}")
    public ResponseEntity<Resource> studentDownloadFileLesson(
            @CookieValue(name = "edutechuid", defaultValue = "") String uidCookie,
            @PathVariable Long classId, @PathVariable Long lessonId,
            @PathVariable String fileName, HttpServletRequest request) {
        String userId = AESUtils.decrypt(uidCookie, false);
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(()-> new NotFoundException("Lesson not found"));
        Chapter chapter = chapterRepository.findById(lesson.getChapter().getId())
                .orElseThrow(()-> new NotFoundException("Chapter not found"));
        Syllabus syllabus = syllabusRepository.findById(chapter.getSyllabus().getId())
                .orElseThrow(()-> new NotFoundException("Syllabus not found"));
        Class aClass = classRepository.findById(classId)
                .orElseThrow(()-> new NotFoundException("Class not found"));
        String fileUploadPath = String.format("/edutech/%s/%s/%s/POSTLOAD_MATERIALS/%s/%s/%s", aClass.getTeacher().getId(), syllabus.getId(), classId, chapter.getId(), lessonId, fileName);
        FileStorageProperties fp = new FileStorageProperties();
        FileStorageService studentDownloadFileLessonService = new FileStorageService(fp);
        Resource resource = studentDownloadFileLessonService.loadFileAsResource(fileUploadPath);
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
    @GetMapping("/student-download-file-assignment/{classId}/{assignmentId}/{fileName:.+}")
    public ResponseEntity<Resource> studentDownloadFileAssignment(@PathVariable Long classId, @PathVariable Long assignmentId ,@PathVariable String fileName, HttpServletRequest request) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(()-> new NotFoundException("Assignment not found"));
        Lesson lesson = lessonRepository.findById(assignment.getLesson().getId())
                .orElseThrow(()-> new NotFoundException("Lesson not found"));
        Chapter chapter = chapterRepository.findById(lesson.getChapter().getId())
                .orElseThrow(()-> new NotFoundException("Chapter not found"));
        Syllabus syllabus = syllabusRepository.findById(chapter.getSyllabus().getId())
                .orElseThrow(()-> new NotFoundException("Syllabus not found"));
        Class aClass = classRepository.findById(classId)
                .orElseThrow(()-> new NotFoundException("Class not found"));
        String fileUploadPath = String.format("/edutech/%s/%s/%s/ASSIGNMENTS/%s/%s/%s/%s_%s", aClass.getTeacher().getId(), syllabus.getId(), classId, chapter.getId(), lesson.getId(), assignmentId, getCurrentUserId(), fileName);
        FileStorageService studentDownloadFileLessonService = new FileStorageService();
        Resource resource = studentDownloadFileLessonService.loadFileAsResource(fileUploadPath);
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename().split("_")[1] + "\"")
                .body(resource);
    }
    @GetMapping("/teacher_download_assignment_upload/{classId}/{assignmentId}")
    public ApiMessageDto<String> teacherDownloadAssignment(@PathVariable Long classId, @PathVariable Long assignmentId, HttpServletResponse response) {
        Long teacherId = getCurrentUserId();
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(()-> new NotFoundException("Failed to post files, only teacher can process"));
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(()-> new NotFoundException("Assignment not found"));
        Lesson lesson = lessonRepository.findById(assignment.getLesson().getId())
                .orElseThrow(()-> new NotFoundException("Lesson not found"));
        Chapter chapter = chapterRepository.findById(lesson.getChapter().getId())
                .orElseThrow(()-> new NotFoundException("Chapter not found"));
        Syllabus syllabus = syllabusRepository.findById(chapter.getSyllabus().getId())
                .orElseThrow(()-> new NotFoundException("Syllabus not found"));
        Class aClass = classRepository.findById(classId)
                .orElseThrow(()-> new NotFoundException("Class not found"));
        // Try to determine file's content type
        String filePath = "/edutech/"+teacherId+"/"+syllabus.getId().toString()+"/"+classId+"/ASSIGNMENTS/"+chapter.getId().toString()+"/"+lesson.getId().toString()+"/"+assignmentId+"/";
        if(teacherId!=aClass.getTeacher().getId()){
            throw new NotFoundException("You are not the teacher of this class");
        }
        File folder = new File(filePath);
        File[] listOfFiles = folder.listFiles();
        List<String> listOfFileNames = new ArrayList<>();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                listOfFileNames.add(listOfFiles[i].getName());
            }
        }
        downloadZipFile(response, listOfFiles, filePath);
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setMessage("Download all files for assignmentId: "+assignmentId+" successfully");

        return apiMessageDto;
    }
    @GetMapping("/teacher_download_temp_upload/{classId}")
    public ApiMessageDto<String> teacherDownloadTemp(@PathVariable Long classId, HttpServletResponse response) {
        Long teacherId = getCurrentUserId();
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(()-> new NotFoundException("Failed to post files, only teacher can process"));
        Class aClass = classRepository.findById(classId)
                .orElseThrow(()-> new NotFoundException("Class not found"));
        Syllabus syllabus = syllabusRepository.findById(aClass.getSyllabus().getId())
                .orElseThrow(()-> new NotFoundException("Syllabus not found"));
        // Try to determine file's content type
        String filePath = "/edutech/"+teacherId+"/"+syllabus.getId().toString()+"/"+classId+"/TEMP/";
        if(teacherId!=aClass.getTeacher().getId()){
            throw new NotFoundException("You are not the teacher of this class");
        }
        File folder = new File(filePath);
        File[] listOfFiles = folder.listFiles();
        List<String> listOfFileNames = new ArrayList<>();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                listOfFileNames.add(listOfFiles[i].getName());
            }
        }
        downloadZipFile(response, listOfFiles, filePath);
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setMessage("Download all temp files for : "+classId+" successfully");

        return apiMessageDto;
    }
    public void downloadZipFile(HttpServletResponse response, File[] listOfFiles, String path) {
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=download.zip");
        try(ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream())) {

            for(File file : listOfFiles) {
                zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
                FileInputStream fileInputStream = new FileInputStream(file);
                IOUtils.copy(fileInputStream, zipOutputStream);
                fileInputStream.close();
                zipOutputStream.closeEntry();
            }
            zipOutputStream.finish();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @PostMapping("/materials-upload-syllabus/{syllabusId}")
    public ApiMessageDto<List<UploadFileResponse>> teacherUploadMultipleFilesSyllabus(@RequestParam("files") MultipartFile[] files, @PathVariable Long syllabusId, HttpServletRequest request) {
        List<UploadFileResponse> msgList = new ArrayList<>();
        for(MultipartFile file:files){
            msgList.add(teacherUploadSyllabus(file, syllabusId, request));
        }
        ApiMessageDto<List<UploadFileResponse>> response = new ApiMessageDto<>();
        response.setData(msgList);
        response.setMessage("Upload file success");
        return response;

    }


    public UploadFileResponse teacherUploadSyllabus(MultipartFile file, Long syllabusId, HttpServletRequest request) {
        Long teacherId = getCurrentUserId();
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(()-> new NotFoundException("Failed to post files, only teacher can process"));
        Syllabus syllabus = syllabusRepository.findById(syllabusId)
                .orElseThrow(()-> new NotFoundException("Syllabus not found"));
        String fileUploadPath = "/edutech/"+teacherId+"/"+syllabus.getId().toString();

        FileStorageProperties fp = new FileStorageProperties();
        fp.setUploadDir(fileUploadPath);
        FileStorageService teacherUploadSyllabusFileService = new FileStorageService(fp);
        String fileName = teacherUploadSyllabusFileService.storeFile(file);

        String fileDownloadUri = UriComponentsBuilder.newInstance()
                .path("/" + syllabus.getId().toString())
                .path("/" + fileName)
                .encode().toUriString();

        UploadFileResponse uploadFileResponse = new UploadFileResponse (fileName.substring(fileName.indexOf("_") + 1), fileDownloadUri,
                file.getContentType(), file.getSize());

        return uploadFileResponse;
    }

    @GetMapping("/teacher-download-syllabus/{syllabusId}")
    public ApiMessageDto<String> teacherDownloadSyllabus(@PathVariable Long syllabusId, HttpServletResponse response) {
        Long teacherId = getCurrentUserId();
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(()-> new NotFoundException("Failed to post files, only teacher can process"));
        Syllabus syllabus = syllabusRepository.findById(syllabusId)
                .orElseThrow(()-> new NotFoundException("Syllabus not found"));
        // Try to determine file's content type
        String filePath = "/edutech/"+teacherId+"/"+ syllabusId +"/";
        File folder = new File(filePath);
        File[] listOfFiles = folder.listFiles();
        List<String> listOfFileNames = new ArrayList<>();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                listOfFileNames.add(listOfFiles[i].getName());
            }
        }
        downloadZipFile(response, listOfFiles, filePath);
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setMessage("Download syllabus id: "+syllabusId+" successfully");

        return apiMessageDto;
    }

    @DeleteMapping("/teacher-delete-file-syllabus/{syllabusId}/{fileName:.+}")
    public ApiMessageDto<String> teacherDeleteFileSyllabus(@PathVariable Long syllabusId, @PathVariable String fileName, HttpServletRequest request) {
        Long teacherId = getCurrentUserId();
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(()-> new NotFoundException("Failed to post files, only teacher can process"));
        Syllabus syllabus = syllabusRepository.findById(syllabusId)
                .orElseThrow(()-> new NotFoundException("Syllabus not found"));
        String fileUploadPath = String.format("%s/%s/%s/%s", "/edutech", teacherId, syllabusId, fileName);
        FileStorageProperties fp = new FileStorageProperties();
        FileStorageService teacherDownloadFileSyllabusService = new FileStorageService(fp);
        teacherDownloadFileSyllabusService.deleteFile(fileUploadPath);
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setMessage("Delete file "+fileName.split("_")[1]+" successfully");
        return apiMessageDto;
    }

    @GetMapping("/teacher-download-file-syllabus/{syllabusId}/{fileName:.+}")
    public ResponseEntity<Resource> teacherDownloadFileSyllabus(
            @PathVariable Long syllabusId,
            @PathVariable String fileName, HttpServletRequest request) {
        Long teacherId = getCurrentUserId();
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(()-> new NotFoundException("Failed to post files, only teacher can process"));
        Syllabus syllabus = syllabusRepository.findById(syllabusId)
                .orElseThrow(()-> new NotFoundException("Syllabus not found"));
        String fileUploadPath = String.format("%s/%s/%s/%s", "/edutech", teacherId, syllabusId, fileName);
        FileStorageProperties fp = new FileStorageProperties();
        FileStorageService teacherDownloadFileSyllabusService = new FileStorageService(fp);
        Resource resource = teacherDownloadFileSyllabusService.loadFileAsResource(fileUploadPath);
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        String filename = resource.getFilename().substring(resource.getFilename().indexOf("_")+1);

        ResponseEntity.BodyBuilder response = ResponseEntity.ok();
        if(filename.contains("blob")){
            response.cacheControl(CacheControl.maxAge(7776000, TimeUnit.SECONDS));
        }

        return response
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(resource);
    }

    @GetMapping("/teacher-download-file-classnews/{classnewsId}/{fileName:.+}")
    public ResponseEntity<Resource> teacherDownloadFileClassNews(
            @PathVariable Long classnewsId,
            @PathVariable String fileName, HttpServletRequest request) {
        Long teacherId = getCurrentUserId();
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(()-> new NotFoundException("Failed to post files, only teacher can process"));
        ClassNews news = newsRepository.findById(classnewsId)
                .orElseThrow(()-> new NotFoundException("ClassNews not found"));
        String fileUploadPath = String.format("%s/%s/%s/%s", "/edutech", teacherId, news.getAClass().getSyllabus().getId(), fileName);
        FileStorageProperties fp = new FileStorageProperties();
        FileStorageService teacherDownloadFileSyllabusService = new FileStorageService(fp);
        Resource resource = teacherDownloadFileSyllabusService.loadFileAsResource(fileUploadPath);
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        String filename = resource.getFilename().substring(resource.getFilename().indexOf("_")+1);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(resource);
    }

    @GetMapping("/student-download-file-classnews/{classnewsId}/{fileName:.+}")
    public ResponseEntity<Resource> studentDownloadFileClassNews(
            @PathVariable Long classnewsId,
            @PathVariable String fileName, HttpServletRequest request) {
        ClassNews news = newsRepository.findById(classnewsId)
                .orElseThrow(()-> new NotFoundException("ClassNews not found"));
        Class aclass = classRepository.findByIdAndStudentIdAndStatus(news.getAClass().getId(), getCurrentUserId(), EduTechConstant.STATUS_ACTIVE)
                .orElseThrow(()-> new NotFoundException("Class not found"));
        String fileUploadPath = String.format("%s/%s/%s/%s", "/edutech", news.getAClass().getTeacher().getId(), news.getAClass().getSyllabus().getId(), fileName);
        FileStorageProperties fp = new FileStorageProperties();
        FileStorageService teacherDownloadFileSyllabusService = new FileStorageService(fp);
        Resource resource = teacherDownloadFileSyllabusService.loadFileAsResource(fileUploadPath);
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        String filename = resource.getFilename().substring(resource.getFilename().indexOf("_")+1);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(resource);
    }

    @GetMapping("/client-get-file-classnews/{classId}/{fileName:.+}")
    public ResponseEntity<Resource> clientDownloadFileClassNews(
            @PathVariable Long classId,
            @PathVariable String fileName, HttpServletRequest request) {
        Class aClass = null;
        if(isStudent()) {
            aClass = classRepository.findByIdAndStudentIdAndStatus(classId, getCurrentUserId(), EduTechConstant.STATUS_ACTIVE)
                    .orElseThrow(() -> new NotFoundException("Class not found"));
        } else if(isTeacher()) {
            aClass = classRepository.findByIdAndTeacherIdAndStatus(classId, getCurrentUserId(), EduTechConstant.STATUS_ACTIVE)
                    .orElseThrow(() -> new NotFoundException("Class not found"));
        }
        String fileUploadPath ="/edutech/"+aClass.getTeacher().getId()+"/"+aClass.getSyllabus().getId().toString()+"/"+classId+"/TEMP/"+fileName;
        FileStorageProperties fp = new FileStorageProperties();
        FileStorageService teacherDownloadFileSyllabusService = new FileStorageService(fp);
        Resource resource = teacherDownloadFileSyllabusService.loadFileAsResource(fileUploadPath);
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        String filename = resource.getFilename().substring(resource.getFilename().indexOf("_")+1);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(resource);
    }

}
