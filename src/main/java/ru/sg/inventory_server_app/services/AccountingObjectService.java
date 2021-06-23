package ru.sg.inventory_server_app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.sg.inventory_server_app.exceptions.FileStorageException;
import ru.sg.inventory_server_app.exceptions.MyFileNotFoundException;
import ru.sg.inventory_server_app.models.AccountingObject;
import ru.sg.inventory_server_app.models.AccountingObjectType;
import ru.sg.inventory_server_app.models.ImageFile;
import ru.sg.inventory_server_app.models.InfoLink;
import ru.sg.inventory_server_app.properties.FileStorageProperties;
import ru.sg.inventory_server_app.repositories.AccountingObjectRepository;
import ru.sg.inventory_server_app.repositories.AccountingObjectTypeRepository;
import ru.sg.inventory_server_app.repositories.ImageFileRepository;
import ru.sg.inventory_server_app.repositories.InfoLinkRepository;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class AccountingObjectService {
    @Autowired
    private AccountingObjectRepository accountingObjectRepository;

    @Autowired
    private AccountingObjectTypeRepository accountingObjectTypeRepository;

    @Autowired
    private InfoLinkRepository infoLinkRepository;

    @Autowired
    private ImageFileRepository imageFileRepository;

    private final Path fileStorageLocation;

    @Autowired
    public AccountingObjectService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    /*
        Методы связанные с AccountingObject
    */

    public AccountingObject createAccountingObject(AccountingObject accountingObject) {
        Optional<AccountingObject> accountingObjectByName =
                accountingObjectRepository.findAccountingObjectByName(accountingObject.getName());
        if (accountingObjectByName.isPresent()) {
            throw new IllegalStateException("The accounting object name already exists.");
        }
        return accountingObjectRepository.save(accountingObject);
    }

    public List<AccountingObject> getAccountingObjects() {
        return accountingObjectRepository.findAll(Sort.by("id"));
    }

    public Optional<AccountingObject> getAccountingObject(Long id) {
        return accountingObjectRepository.findById(id);
    }

    public AccountingObject updateAccountingObject(AccountingObject updatedAccountingObject, Long id) {
        // Примечание: id ОУ в теле запроса не отправляется

        // Находим ОУ по id
        AccountingObject oldAccountingObject = accountingObjectRepository
                .findById(id)
                .orElse(null);

        if (oldAccountingObject == null) {
            throw new IllegalStateException("Accounting object with id "
                    + id
                    + " does not exist!");
        }

        // Проверяем новое название на совпадение с уже существующими названиями
        Optional<AccountingObject> updatedAccountingObjectByName =
                accountingObjectRepository.findAccountingObjectByName(updatedAccountingObject.getName());

        // Если названия нового и старого ОУ равны, то обновляем ОУ (кроме имени)
        if (oldAccountingObject.getName().equals(updatedAccountingObject.getName())) {
            System.out.println("OLD NAME AND NEW NAME ARE EQUAL.");

            System.out.println("OLD NAME: " + oldAccountingObject.getName());
            System.out.println("NEW NAME: " + updatedAccountingObject.getName());
            System.out.println("NAME EQUALS: "
                    + oldAccountingObject.getName().equals(updatedAccountingObject.getName()));

            oldAccountingObject.setBarcode(updatedAccountingObject.getBarcode());
            oldAccountingObject.setCurrentQuantity(updatedAccountingObject.getCurrentQuantity());
            oldAccountingObject.setOldQuantity(updatedAccountingObject.getOldQuantity());
            oldAccountingObject.setQuantityInOperation(updatedAccountingObject.getQuantityInOperation());
            oldAccountingObject.setTechReqs(updatedAccountingObject.getTechReqs());
            oldAccountingObject.setUsePurpose(updatedAccountingObject.getUsePurpose());

            return accountingObjectRepository.save(oldAccountingObject);
        } else {
            // Если новое название совпадает с уже существующим названием другого ОУ, то выводим exception
            if (updatedAccountingObjectByName.isPresent()) {
                throw new IllegalStateException("The accounting object name already exists.");
            }
            else {
                // Обновляем ОУ
                System.out.println("OLD NAME AND NEW NAME ARE NOT EQUAL AND NEW NAME DOES NOT EXIST.");

                System.out.println("OLD NAME: " + oldAccountingObject.getName());
                System.out.println("NEW NAME: " + updatedAccountingObject.getName());
                System.out.println("NAME EQUALS: "
                        + oldAccountingObject.getName().equals(updatedAccountingObject.getName()));

                oldAccountingObject.setName(updatedAccountingObject.getName());
                oldAccountingObject.setBarcode(updatedAccountingObject.getBarcode());
                oldAccountingObject.setCurrentQuantity(updatedAccountingObject.getCurrentQuantity());
                oldAccountingObject.setOldQuantity(updatedAccountingObject.getOldQuantity());
                oldAccountingObject.setQuantityInOperation(updatedAccountingObject.getQuantityInOperation());
                oldAccountingObject.setTechReqs(updatedAccountingObject.getTechReqs());
                oldAccountingObject.setUsePurpose(updatedAccountingObject.getUsePurpose());

                return accountingObjectRepository.save(oldAccountingObject);
            }
        }
    }

    public void deleteAccountingObject(Long id) {
        boolean accountingObjectExist = accountingObjectRepository.existsById(id);
        if (!accountingObjectExist) {
            throw new IllegalStateException("Accounting object with id " + id + " does not exist!");
        }
        accountingObjectRepository.deleteById(id);
    }

    /*
        Методы связанные с AccountingObjectType
    */

    public AccountingObject createAccountingObjectTypeRelation(Long accounting_object_id, Long accounting_object_type_id) {
        AccountingObject accountingObject = accountingObjectRepository
                .findById(accounting_object_id)
                .orElse(null); // .get() replaced by .orElse(null)

        AccountingObjectType accountingObjectType = accountingObjectTypeRepository
                .findById(accounting_object_type_id)
                .orElse(null);

        if (accountingObject == null) {
            throw new IllegalStateException("Accounting object with id "
                    + accounting_object_id
                    + " does not exist!");
        }

        if (accountingObjectType == null) {
            throw new IllegalStateException("Accounting object type with id "
                    + accounting_object_type_id
                    + " does not exist!");
        }

        accountingObject.setAccountingObjectType(accountingObjectType);
        return accountingObjectRepository.save(accountingObject);
    }

    public List<AccountingObject> getAccountingObjectsByType(Long type_id) {
        return accountingObjectRepository.findAccountingObjectsByAccountingObjectType_Id(Sort.by("id"), type_id);
    }

    /*
        Методы связанные с InfoLink
    */

    public InfoLink createInfoLink(InfoLink infoLink) {
        return infoLinkRepository.save(infoLink);
    }

    public AccountingObject createInfoLinkRelation(Long accounting_object_id, Long info_link_id) {
        AccountingObject accountingObject = accountingObjectRepository
                .findById(accounting_object_id)
                .orElse(null);

        InfoLink infoLink = infoLinkRepository
                .findById(info_link_id)
                .orElse(null);

        if (accountingObject == null) {
            throw new IllegalStateException("Accounting object with id "
                    + accounting_object_id
                    + " does not exist!");
        }

        if (infoLink == null) {
            throw new IllegalStateException("Link with id "
                    + info_link_id
                    + " does not exist!");
        }

        AccountingObject accountingObject1 = accountingObjectRepository.findById(accounting_object_id).get();
        InfoLink infoLink1 = infoLinkRepository.findById(info_link_id).get();
        accountingObject1.getInfoLinks().add(infoLink1);

        //System.out.println(infoLink2 + "\n");
        //System.out.println(accountingObject2.getInfoLinks());
        return accountingObjectRepository.save(accountingObject1);
    }

    public List<InfoLink> getAccountingObjectInfoLinks(Long accounting_object_id) {
        return infoLinkRepository.findInfoLinksByAccountingObject_Id(Sort.by("id"), accounting_object_id);
        //return null;
    }

    public InfoLink getAccountingObjectInfoLink(Long accounting_object_id, Long info_link_id) {
        return infoLinkRepository.findInfoLinkByAccountingObject_IdAndId(accounting_object_id, info_link_id);
        //return null;
    }

    public void deleteInfoLink(Long info_link_id) {
        boolean infoLinkExist = infoLinkRepository.existsById(info_link_id);
        if (!infoLinkExist) {
            throw new IllegalStateException("Link with id " + info_link_id + " does not exist!");
        }
        infoLinkRepository.deleteById(info_link_id);
    }

    /*
        Методы связанные с ImageFile
    */

    public ImageFile createImageFile(ImageFile imageFile) {
        return imageFileRepository.save(imageFile);
    }

    public ImageFile createImageFileRelation(Long accounting_object_id, Long image_file_id) {
        AccountingObject accountingObject = accountingObjectRepository
                .findById(accounting_object_id)
                .orElse(null);

        ImageFile imageFile = imageFileRepository
                .findById(image_file_id)
                .orElse(null);

        if (accountingObject == null) {
            throw new IllegalStateException("Accounting object with id "
                    + accounting_object_id
                    + " does not exist!");
        }

        if (imageFile == null) {
            throw new IllegalStateException("File with id "
                    + image_file_id
                    + " does not exist!");
        }

        imageFile.setAccountingObject(accountingObject);
        //accountingObject.setImageFile(imageFile);

        //System.out.println(accountingObject.getImageFile());
        //return accountingObjectRepository.save(accountingObject);
        return imageFileRepository.save(imageFile);
    }

    public ImageFile getAccountingObjectImageFile(Long accounting_object_id) {
        return imageFileRepository.findImageFileByAccountingObject_Id(Sort.by("id"), accounting_object_id);
    }

    public Resource getImageFile(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }

    public void deleteImageFile(Long info_link_id) {
        boolean infoLinkExist = imageFileRepository.existsById(info_link_id);
        if (!infoLinkExist) {
            throw new IllegalStateException("File with id " + info_link_id + " does not exist!");
        }
        imageFileRepository.deleteById(info_link_id);
    }

}
