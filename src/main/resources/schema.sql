DROP TABLE IF EXISTS Tutor CASCADE ;
DROP TABLE IF EXISTS Student CASCADE ;
DROP TABLE IF EXISTS Tutor_Student CASCADE ;
DROP TABLE IF EXISTS TaskType CASCADE ;
DROP TABLE IF EXISTS VerificationWork CASCADE ;
DROP TABLE IF EXISTS Task CASCADE ;
DROP TABLE IF EXISTS VerificationWork_Task CASCADE ;
DROP TABLE IF EXISTS VerificationWork_Student CASCADE ;


CREATE TABLE Tutor
(
    id         BIGINT AUTO_INCREMENT,
    full_name  varchar(50)  NOT NULL,
    password   varchar(50)  NOT NULL,
    email      varchar(100) NOT NULL,
    age        int,
    discipline varchar(50)  NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE Student
(
    id        bigint PRIMARY KEY AUTO_INCREMENT,
    full_name varchar(50)  NOT NULL,
    password  varchar(50)  NOT NULL,
    email     varchar(100) NOT NULL,
    age       int
);

CREATE TABLE Tutor_Student
(
    tutor_id   bigint REFERENCES Tutor (id),
    student_id bigint REFERENCES Student (id),
    PRIMARY KEY (tutor_id, student_id)
);


CREATE TABLE TaskType
(
    id    bigint PRIMARY KEY AUTO_INCREMENT,
    title varchar(100) NOT NULL
);

CREATE TABLE VerificationWork
(
    id                   bigint PRIMARY KEY AUTO_INCREMENT,
    title                varchar(200) NOT NULL,
    assignation_datetime TIMESTAMP,
    deadline             TIMESTAMP
);

CREATE TABLE Task
(
    id           bigint PRIMARY KEY AUTO_INCREMENT,
    task_type_id bigint       REFERENCES TaskType (id) ON DELETE set null,
    definition   varchar      NOT NULL,
    answer       varchar(100) NOT NULL
);

CREATE TABLE VerificationWork_Task
(
    verification_work_id bigint REFERENCES VerificationWork (id),
    task_id              bigint REFERENCES Task (id),
    PRIMARY KEY (verification_work_id, task_id)
);

CREATE TABLE VerificationWork_Student
(
    verification_work_id bigint REFERENCES VerificationWork (id),
    student_id           bigint REFERENCES Student (id) ON DELETE CASCADE,
    PRIMARY KEY (verification_work_id, student_id)
);
