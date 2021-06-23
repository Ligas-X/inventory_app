package ru.sg.inventory_server_app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.sg.inventory_server_app.models.*;
import ru.sg.inventory_server_app.repositories.*;

import java.util.List;
import java.util.Optional;

@Service
public class OperationService {
    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private OperationTypeRepository operationTypeRepository;

    @Autowired
    private AccountingObjectRepository accountingObjectRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    /*
        Методы связанные с Operation
    */

    public Operation createOperation(Operation operation) {
        return operationRepository.save(operation);
    }

    public List<Operation> getOperations() {
        return operationRepository.findAll(Sort.by("id"));
    }

    public Optional<Operation> getOperation(Long operation_id) {
        return operationRepository.findById(operation_id);
    }

    public Operation updateOperation(Operation updatedOperation, Long operation_id) {
        boolean operationExist = operationRepository.existsById(operation_id);
        if (!operationExist) {
            throw new IllegalStateException("Operation with id " + operation_id + " does not exist!");
        }

        Optional<Operation> optionalOperation = operationRepository.findById(operation_id);
        Operation oldOperation = optionalOperation.get();
        //System.out.println(oldAccountingObject);

        oldOperation.setDate(updatedOperation.getDate());
        oldOperation.setQuantity(updatedOperation.getQuantity());
        oldOperation.setCost(updatedOperation.getCost());
        oldOperation.setAdditionalInfo(updatedOperation.getAdditionalInfo());

        return operationRepository.save(oldOperation);
    }

    public void deleteOperation(Long operation_id) {
        boolean operationExist = operationRepository.existsById(operation_id);
        if (!operationExist) {
            throw new IllegalStateException("Operation with id " + operation_id + " does not exist!");
        }
        operationRepository.deleteById(operation_id);
    }

    /*
        Методы связанные с OperationType
    */

    public Operation createOperationTypeRelation(Long operation_id, Long operation_type_id) {
        Operation operation = operationRepository
                .findById(operation_id)
                .orElse(null); // .get() replaced by .orElse(null)
        OperationType operationType = operationTypeRepository
                .findById(operation_type_id)
                .orElse(null);
        if (operation == null) {
            throw new IllegalStateException("Operation with id "
                    + operation_id
                    + " does not exist!");
        }
        if (operationType == null) {
            throw new IllegalStateException("Operation type with id "
                    + operation_type_id
                    + " does not exist!");
        }
        operation.setOperationType(operationType);
        return operationRepository.save(operation);
    }

    public List<Operation> getOperationsByType(Long operation_type_id) {
        return operationRepository.findOperationsByOperationType_Id(operation_type_id);
    }

    /*
        Методы связанные с AccountingObject
    */

    public Operation createAccountingObjectRelation(Long operation_id, Long accounting_object_id) {
        Operation operation = operationRepository
                .findById(operation_id)
                .orElse(null); // .get() replaced by .orElse(null)
        AccountingObject accountingObject = accountingObjectRepository
                .findById(accounting_object_id)
                .orElse(null);
        if (operation == null) {
            throw new IllegalStateException("Operation with id "
                    + operation_id
                    + " does not exist!");
        }
        if (accountingObject == null) {
            throw new IllegalStateException("Accounting object with id "
                    + accounting_object_id
                    + " does not exist!");
        }
        operation.addAccountingObject(accountingObject);
        return operationRepository.save(operation);
    }

    public List<Operation> getOperationsByAccountingObject(Long accounting_object_id) {
        return operationRepository.findByAccountingObject_Id(accounting_object_id);
    }

    /*
        Методы связанные с UserInfo
    */

    public Operation createUserInfoRelation(Long operation_id, Long user_id) {
        Operation operation = operationRepository
                .findById(operation_id)
                .orElse(null); // .get() replaced by .orElse(null)
        UserInfo userInfo = userInfoRepository
                .findById(user_id)
                .orElse(null);
        if (operation == null) {
            throw new IllegalStateException("Operation with id "
                    + operation_id
                    + " does not exist!");
        }
        if (userInfo == null) {
            throw new IllegalStateException("User with id "
                    + user_id
                    + " does not exist!");
        }
        operation.setUserInfo(userInfo);
        return operationRepository.save(operation);
    }

    public List<Operation> getOperationsByUser(Long user_id) {
        return operationRepository.findOperationsByUserInfo_Id(user_id);
    }
}
