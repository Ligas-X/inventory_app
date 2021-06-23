package ru.sg.inventory_server_app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sg.inventory_server_app.models.AccountingObject;
import ru.sg.inventory_server_app.models.AccountingObjectType;

import java.util.Optional;

@Repository
public interface AccountingObjectTypeRepository extends JpaRepository<AccountingObjectType, Long> {
}
