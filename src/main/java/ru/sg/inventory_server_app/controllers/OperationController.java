package ru.sg.inventory_server_app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sg.inventory_server_app.models.Operation;
import ru.sg.inventory_server_app.services.OperationService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/operation")
public class OperationController {
    private final OperationService operationService;

    @Autowired
    public OperationController(OperationService operationService) {
        this.operationService = operationService;
    }

    /*
        Operation
        /operation - вывод операций
        /operation/{operation_id} - вывод определенной операции
        /operation/add - создание операции
        /operation - обновление определенной операции (put method / update)
     	/operation/{operation_id} - удаление определенной операции (delete method / delete)
    */

    @GetMapping
    @ResponseBody
    public List<Operation> getOperations() {
        return operationService.getOperations();
    }

    @GetMapping("/{operation_id}")
    @ResponseBody
    public Optional<Operation> getOperation(@PathVariable Long operation_id) {
        return operationService.getOperation(operation_id);
    }

    @PostMapping
    public ResponseEntity<Operation> addOperation(@RequestBody Operation operation) {
        return ResponseEntity.ok(operationService.createOperation(operation));
    }

    @PutMapping("/{operation_id}")
    public ResponseEntity<Operation> updateOperation(@RequestBody Operation operation,
                                                     @PathVariable Long operation_id) {
        return ResponseEntity.ok(operationService.updateOperation(operation, operation_id));
    }

    @DeleteMapping("/{operation_id}")
    public void deleteOperation(@PathVariable Long operation_id) {
        operationService.deleteOperation(operation_id);
    }

    /*
        OperationType
        /operation/type/{operation_type_id} - вывод операций определенного типа
        /operation/{operation_id}/type/{operation_type_id} - назначение типа операции (put method / update)
    */

    @GetMapping("/type/{operation_type_id}")
    @ResponseBody
    public List<Operation> getOperationsByType(@PathVariable Long operation_type_id) {
        return operationService.getOperationsByType(operation_type_id);
    }

    @PutMapping("/{operation_id}/type/{operation_type_id}")
    public ResponseEntity<Operation> addOperationTypeRelation(@PathVariable Long operation_id,
                                                              @PathVariable Long operation_type_id) {
        return ResponseEntity.ok(operationService.createOperationTypeRelation(operation_id, operation_type_id));
    }

    /*
        AccountingObject
        /operation/object/{account_object_id} - вывод операций определенного объекта учета
        /operation/{operation_id}/object/{accounting_object_id} - добавление ОУ в операцию
    */

    @GetMapping("/object/{accounting_object_id}")
    @ResponseBody
    public List<Operation> getOperationsByAccountingObject(@PathVariable Long accounting_object_id) {
        return operationService.getOperationsByAccountingObject(accounting_object_id);
    }

    @PutMapping("/{operation_id}/object/{accounting_object_id}")
    public ResponseEntity<Operation> addAccountingObjectRelation(@PathVariable Long operation_id,
                                                                 @PathVariable Long accounting_object_id) {
        return ResponseEntity.ok(operationService.createAccountingObjectRelation(operation_id, accounting_object_id));
    }

    /*
        UserInfo
        /operation/user/{user_id} - получение операций определенного пользователя
        /operation/{operation_id}/user/{user_id} - добавление пользователя к операции
    */

    @GetMapping("/user/{user_id}")
    @ResponseBody
    public List<Operation> getOperationsByUser(@PathVariable Long user_id) {
        return operationService.getOperationsByUser(user_id);
    }

    @PutMapping("/{operation_id}/user/{user_id}")
    public ResponseEntity<Operation>  addUserInfoRelation(@PathVariable Long operation_id,
                                                          @PathVariable Long user_id) {
        return ResponseEntity.ok(operationService.createUserInfoRelation(operation_id, user_id));
    }
}
