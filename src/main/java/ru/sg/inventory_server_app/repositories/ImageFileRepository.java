package ru.sg.inventory_server_app.repositories;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sg.inventory_server_app.models.ImageFile;
import ru.sg.inventory_server_app.models.InfoLink;

import java.util.List;

@Repository
public interface ImageFileRepository extends JpaRepository<ImageFile, Long> {
    ImageFile findImageFileByAccountingObject_Id(Sort id, Long accounting_object_id);
}
