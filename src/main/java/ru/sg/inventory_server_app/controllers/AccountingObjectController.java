package ru.sg.inventory_server_app.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.sg.inventory_server_app.models.AccountingObject;
import ru.sg.inventory_server_app.models.ImageFile;
import ru.sg.inventory_server_app.models.InfoLink;
import ru.sg.inventory_server_app.services.AccountingObjectService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/accounting_object")
public class AccountingObjectController {
    private static final Logger logger = LoggerFactory.getLogger(AccountingObjectController.class);

    private final AccountingObjectService accountingObjectService;

    @Autowired
    public AccountingObjectController(AccountingObjectService accountingObjectService) {
        this.accountingObjectService = accountingObjectService;
    }

    /*
        AccountingObject
    */

    @GetMapping
    @ResponseBody
    public List<AccountingObject> getAccountingObjects() {
        return accountingObjectService.getAccountingObjects();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Optional<AccountingObject> getAccountingObject(@PathVariable("id") Long id) {
        return accountingObjectService.getAccountingObject(id);
    }

    @PostMapping
    public ResponseEntity<AccountingObject> addAccountingObject(@RequestBody AccountingObject accountingObject) {
        return ResponseEntity.ok(accountingObjectService.createAccountingObject(accountingObject));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountingObject> updateAccountingObject(@RequestBody AccountingObject accountingObject,
                                                                   @PathVariable Long id) {
        return ResponseEntity.ok(accountingObjectService.updateAccountingObject(accountingObject, id));
    }

    @DeleteMapping("/{id}")
    public void deleteAccountingObject(@PathVariable("id") Long id) {
        accountingObjectService.deleteAccountingObject(id);
    }

    /*
        AccountingObjectType
    */

    @GetMapping("/type/{type_id}")
    @ResponseBody
    public List<AccountingObject> getAccountingObjectsByType(@PathVariable Long type_id) {
        return accountingObjectService.getAccountingObjectsByType(type_id);
    }

    @PutMapping("/{accounting_object_id}/type/{accounting_object_type_id}")
    public ResponseEntity<AccountingObject> addAccountingObjectTypeRelation(@PathVariable Long accounting_object_id,
                                                                            @PathVariable Long accounting_object_type_id) {
        return ResponseEntity.ok(accountingObjectService.createAccountingObjectTypeRelation(accounting_object_id, accounting_object_type_id));
    }

    /*
        InfoLink
    */

    @GetMapping("/{accounting_object_id}/link")
    @ResponseBody
    public List<InfoLink> getInfoLinks(@PathVariable Long accounting_object_id) {
        return accountingObjectService.getAccountingObjectInfoLinks(accounting_object_id);
    }

    @GetMapping("/{accounting_object_id}/link/{info_link_id}")
    @ResponseBody
    public InfoLink getInfoLink(@PathVariable Long accounting_object_id,
                                @PathVariable Long info_link_id) {
        return accountingObjectService.getAccountingObjectInfoLink(accounting_object_id, info_link_id);
    }

    @PostMapping("/link")
    public ResponseEntity<InfoLink> addInfoLink(@RequestBody InfoLink infoLink) {
        return ResponseEntity.ok(accountingObjectService.createInfoLink(infoLink));
    }

    @PutMapping("/{accounting_object_id}/link/{info_link_id}")
    public ResponseEntity<AccountingObject> addInfoLinkRelation(@PathVariable Long accounting_object_id,
                                                                @PathVariable Long info_link_id) {
        return ResponseEntity.ok(accountingObjectService.createInfoLinkRelation(accounting_object_id, info_link_id));
    }

    @DeleteMapping("/link/{info_link_id}")
    public void deleteInfoLink(@PathVariable Long info_link_id) {
        accountingObjectService.deleteInfoLink(info_link_id);
    }

    /*
        ImageFile
    */

    @Value("${upload.file.path}")
    private String uploadPathStr;

    @GetMapping("/{accounting_object_id}/image")
    @ResponseBody
    public ImageFile getImageFile(@PathVariable Long accounting_object_id) {
        return accountingObjectService.getAccountingObjectImageFile(accounting_object_id);
    }

    @GetMapping("/image/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        Resource resource = accountingObjectService.getImageFile(fileName);

        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping("/image")
    public ResponseEntity<ImageFile> addImageFile(@RequestParam("file") MultipartFile file, @RequestParam String filename) {

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/accounting_object/image/")
                .path(filename)
                .toUriString();

        if (file == null || file.isEmpty() || filename == null || filename.isEmpty())
            System.out.println("File of file name is null!");

        try {
            assert file != null;
            try (InputStream inputStream = file.getInputStream()) {
                Path uploadPath = Paths.get(uploadPathStr);

                if (!uploadPath.toFile().exists()) {
                    uploadPath.toFile().mkdirs();
                }

                Files.copy(inputStream, Paths.get(uploadPathStr).resolve(filename), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("File " + filename + " uploaded!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        ImageFile imageFile = new ImageFile(filename, fileDownloadUri, file.getContentType(), file.getSize());
        return ResponseEntity.ok(accountingObjectService.createImageFile(imageFile));
    }

    @PutMapping("/{accounting_object_id}/image/{image_file_id}")
    public ResponseEntity<ImageFile> addImageFileRelation(@PathVariable Long accounting_object_id,
                                                                 @PathVariable Long image_file_id) {
        return ResponseEntity.ok(accountingObjectService.createImageFileRelation(accounting_object_id, image_file_id));
    }

    @DeleteMapping("/image/{image_file_id}")
    public void deleteImageFile(@PathVariable Long image_file_id) {
        accountingObjectService.deleteImageFile(image_file_id);
    }


}
