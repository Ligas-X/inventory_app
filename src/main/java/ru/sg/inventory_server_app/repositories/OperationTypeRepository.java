package ru.sg.inventory_server_app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sg.inventory_server_app.models.OperationType;

@Repository
public interface OperationTypeRepository extends JpaRepository<OperationType, Long> {
}
