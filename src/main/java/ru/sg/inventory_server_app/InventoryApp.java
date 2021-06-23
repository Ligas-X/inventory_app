package ru.sg.inventory_server_app;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import ru.sg.inventory_server_app.configurations.SpringFoxConfiguration;
import ru.sg.inventory_server_app.models.AccountingObject;
import ru.sg.inventory_server_app.models.AccountingObjectType;
import ru.sg.inventory_server_app.models.OperationType;
import ru.sg.inventory_server_app.models.UserInfo;
import ru.sg.inventory_server_app.properties.FileStorageProperties;
import ru.sg.inventory_server_app.repositories.AccountingObjectRepository;
import ru.sg.inventory_server_app.repositories.AccountingObjectTypeRepository;
import ru.sg.inventory_server_app.repositories.OperationTypeRepository;
import ru.sg.inventory_server_app.repositories.UserInfoRepository;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@RestController
@EnableConfigurationProperties({
        FileStorageProperties.class
})
public class InventoryApp {
    public static void main(String[] args) {
        SpringApplication.run(InventoryApp.class, args);
    }

    @GetMapping
    public ModelAndView redirectWithUsingRedirectPrefix(ModelMap model) {
        return new ModelAndView("redirect:/swagger-ui/", model);
    }

    @Bean
    CommandLineRunner commandLineRunnerUsers(UserInfoRepository userInfoRepository) {
        List<UserInfo> userInfos = new ArrayList<>();
        return args -> {
            UserInfo petrov = new UserInfo(
                    "Петров",
                    "Петр",
                    "Петрович",
                    "преподаватель",
                    "8(999)444-73-74",
                    "petrov@mail.ru",
                    null
            );
            UserInfo sidorov = new UserInfo(
                    "Сидоров",
                    "Сидр",
                    "Сидорович",
                    "студент",
                    "8(982)123-45-67",
                    null,
                    null
            );
            UserInfo ivanov = new UserInfo(
                    "Иванов",
                    "Иван",
                    "Иванович",
                    "студент",
                    null,
                    "ivanov@mail.ru",
                    null
            );
            userInfos.add(petrov);
            userInfos.add(sidorov);
            userInfos.add(ivanov);
            userInfoRepository.saveAll(userInfos);
        };
    }

    @Bean
    CommandLineRunner commandLineRunnerAccountingObjectTypes(AccountingObjectTypeRepository accountingObjectTypeRepository) {
        List<AccountingObjectType> accountingObjectTypes = new ArrayList<>();
        return args -> {
            AccountingObjectType equipment = new AccountingObjectType(
                    "Оборудование"
            );
            AccountingObjectType components = new AccountingObjectType(
                    "Комплектующие"
            );
            accountingObjectTypes.add(equipment);
            accountingObjectTypes.add(components);
            accountingObjectTypeRepository.saveAll(accountingObjectTypes);
        };
    }

    @Bean
    CommandLineRunner commandLineRunnerOperationTypes(OperationTypeRepository operationTypeRepository) {
        List<OperationType> operationTypes = new ArrayList<>();
        return args -> {
            OperationType arrival = new OperationType(
                    "Приход"
            );
            OperationType consumption = new OperationType(
                    "Расход"
            );
            OperationType returns = new OperationType(
                    "Возврат"
            );
            OperationType disposal = new OperationType(
                    "Списание"
            );
            OperationType inventory = new OperationType(
                    "Инвентаризация"
            );
            operationTypes.add(arrival);
            operationTypes.add(consumption);
            operationTypes.add(returns);
            operationTypes.add(disposal);
            operationTypes.add(inventory);
            operationTypeRepository.saveAll(operationTypes);
        };
    }

    @Bean
    CommandLineRunner commandLineRunnerAccountingObjects(AccountingObjectRepository accountingObjectRepository) {
        List<AccountingObject> accountingObjects = new ArrayList<>();

        Long typeValueOne = 1L; // 1L -> 1 in Long data type
        Long typeValueTwo = 2L;

        AccountingObjectType typeOne = new AccountingObjectType();
        typeOne.setId(typeValueOne);

        AccountingObjectType typeTwo = new AccountingObjectType();
        typeTwo.setId(typeValueTwo);

        return args -> {
            AccountingObject monitor24 = new AccountingObject(
                    "Монитор 24",
                    "101000001",
                    0,
                    0,
                    0,
                    "Разрешение 1920x1080, 24 дюйма",
                    "для работы в лаборатории",
                    null,
                    null,
                    null,
                    null
            );
            monitor24.setAccountingObjectType(typeOne);

            AccountingObject monitor28 = new AccountingObject(
                    "Монитор 28",
                    "101000002",
                    0,
                    0,
                    0,
                    "Разрешение 1920x1200, 28 дюймов",
                    "для работы в лаборатории",
                    null,
                    null,
                    null,
                    null
            );
            monitor28.setAccountingObjectType(typeOne);

            AccountingObject projector = new AccountingObject(
                    "Проектор",
                    "101000003",
                    0,
                    0,
                    0,
                    "Разрешение 8K",
                    "для демонстрации проектов",
                    null,
                    null,
                    null,
                    null
            );
            projector.setAccountingObjectType(typeOne);

            AccountingObject arduinoNano = new AccountingObject(
                    "Arduino Nano",
                    "102000001",
                    0,
                    0,
                    0,
                    "Плата семейства Arduino",
                    "для программирования роботов",
                    null,
                    null,
                    null,
                    null
            );
            arduinoNano.setAccountingObjectType(typeTwo);

            AccountingObject arduinoUno = new AccountingObject(
                    "Arduino Uno",
                    "102000002",
                    0,
                    0,
                    0,
                    "Плата семейства Arduino",
                    "для программирования роботов",
                    null,
                    null,
                    null,
                    null
            );
            arduinoUno.setAccountingObjectType(typeTwo);

            accountingObjects.add(monitor24);
            accountingObjects.add(monitor28);
            accountingObjects.add(projector);
            accountingObjects.add(arduinoNano);
            accountingObjects.add(arduinoUno);
            accountingObjectRepository.saveAll(accountingObjects);
        };
    }
}
