package ru.sg.inventory_server_app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.sg.inventory_server_app.models.AccountingObject;
import ru.sg.inventory_server_app.models.Operation;

import java.util.List;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {
    List<Operation> findOperationsByOperationType_Id(Long operation_type_id);
    List<Operation> findOperationsByUserInfo_Id(Long user_id);

    @Query(value = "SELECT opr.id, opr.additional_info, opr.cost, opr.date, opr.quantity, opr.operation_type_id, opr.user_info_id " +
            "FROM operation opr " +
            "LEFT JOIN accounting_object_operation ON accounting_object_operation.operation_id = opr.id " +
            "WHERE accounting_object_operation.accounting_object_id = ?1", nativeQuery = true)
    List<Operation> findByAccountingObject_Id(Long accounting_object_id);
}
