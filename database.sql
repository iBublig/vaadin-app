create table DOCTOR
(
    ID BIGINT identity constraint DOCTOR_PK primary key,
    FIRSTNAME      VARCHAR(35) NOT NULL,
    LASTNAME       VARCHAR(35) NOT NULL,
    PATRONYMIC     VARCHAR(35) NOT NULL,
    SPECIALIZATION VARCHAR(35) NOT NULL
);
create table PATIENT
(
    ID BIGINT identity constraint PATIENT_PK primary key,
    FIRSTNAME  VARCHAR(35) NOT NULL,
    LASTNAME   VARCHAR(35) NOT NULL,
    PATRONYMIC VARCHAR(35) NOT NULL,
    PHONE      VARCHAR(35) NOT NULL
);
create table RECIPE
(
    ID BIGINT identity constraint RECIPE_PK primary key,
    DESCRIPTION VARCHAR(255) NOT NULL,
    PATIENTID BIGINT,
    DOCTORID BIGINT,
    CREATEDATE DATE NOT NULL,
    VALIDITY DATE NOT NULL,
    STATUS VARCHAR(35) NOT NULL,
    FOREIGN KEY (DOCTORID) REFERENCES DOCTOR (ID),
    FOREIGN KEY (PATIENTID) REFERENCES PATIENT (ID)
);

INSERT INTO PUBLIC.DOCTOR (FIRSTNAME, LASTNAME, PATRONYMIC, SPECIALIZATION)
VALUES ('Таир', 'Григорян', 'Махмудович', 'Терапевт'),
       ('Андрей','Виноградов','Викторович','Хирург'),
       ('Ангелина','Василькова','Михайловна','Стоматолог'),
       ('Павел','Алексеев','Генадъевич','Онколог'),
       ('Татьяна','Кирюшина','Анатольевна','Офтальмолог');

INSERT INTO PUBLIC.PATIENT (FIRSTNAME, LASTNAME, PATRONYMIC, PHONE)
VALUES ('Александр', 'Ложкин', 'Ильич', '8(964)589-15-23'),
       ('Леонид','Никитин','Борисович', '8(901)466-78-69'),
       ('Лариса','Акимова','Николаевна', '8(937)794-58-03'),
       ('Тамара','Золотцева','Сергеевна', '8(927)146-83-19'),
       ('Владислав','Тарасов','Глебович', '8(999)896-14-93');