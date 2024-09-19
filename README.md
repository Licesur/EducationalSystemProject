# Educational system app
# Приложение предназначенное для организации учебного процесса преподавателя и его ученика,
# с возможностью отправки ему домашней/контрольной работы

## В данной версии реализованы следующий функционал:
- CRUD операции для преподавателей, учеников, задач и контрольных работ
- Операции по назначению работ конкретным ученикам и учеников преподавателям.
- Операции исключения работ из списка ученика и ученика из списка преподавателя.

## Перед запуском приложения для иллюстрации работы приложения рекомендуется запустить скрипт resources/sql/order_number.sql для заполнения базы данных начальными значениями

## Для входа в систему необходимо использовать следующие данные:
- Логин - u
- Пароль - u

## Описание сущностей:
### Ученик (Student) - содержит основную информацию об ученике в процессе обучения, а также актуальные работы.
### Учитель (Tutor) - содержит основную информацию об учителе.
### Задача (Task) - сущность используемая для контрольных работ и домашних заданий, содержащая описание задачи и ее ответ.
### Контрольная работа (VerificationWork) -  сущность, содержащая заголовок работы, а аткже список задач, входящихк в нее и учеников, кому эта работа была назначена.

## Рабочий процесс приложения:
