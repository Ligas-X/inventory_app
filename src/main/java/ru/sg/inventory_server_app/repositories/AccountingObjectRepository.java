package ru.sg.inventory_server_app.repositories;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.sg.inventory_server_app.models.AccountingObject;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountingObjectRepository extends JpaRepository<AccountingObject, Long> {
    Optional<AccountingObject> findAccountingObjectByName(String name);
    List<AccountingObject> findAccountingObjectsByAccountingObjectType_Id(Sort id, Long type_id);
}
