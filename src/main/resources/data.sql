
INSERT INTO Tutor (full_name, password, email, age, discipline)
VALUES ('Sova Alexander', '000001', 'testmail1@mail.ru', 23, 'math');

INSERT INTO Tutor (full_name, password, email, age, discipline)
VALUES ('Kononova Daria', '000002', 'testmail2@mail.ru', 23, 'math');

--Insert some pupils--

INSERT INTO Student (full_name, password, email, age)
VALUES ('Bochkarev Arseny', '000003', 'testmail3@mail.ru', 18);

INSERT INTO Student (full_name, password, email, age)
VALUES ('Somesn Ekaterina', '000004', 'testmail4@mail.ru', 18);

INSERT INTO Student (full_name, password, email, age)
VALUES ('Korcev Ivan', '000005', 'testmail5@mail.ru', 18);

--education pairs--

INSERT INTO Tutor_Student (tutor_id, student_id) VALUES (1,1);
INSERT INTO Tutor_Student (tutor_id, student_id) VALUES (2,2);
INSERT INTO Tutor_Student (tutor_id, student_id) VALUES (1,3);


INSERT INTO Task(definition, answer)
VALUES ( 'solve the equation: x^2-5x+6=0; enter the sum of all the roots as answer', '5');
INSERT INTO Task(definition, answer)
VALUES ('solve the equation: x^2-x-6=0; enter the sum of all the roots as answer', '1');
INSERT INTO Task(definition, answer)
VALUES ('solve the equation: x^2-6x+8=0; enter the sum of all the roots as answer', '6');
INSERT INTO Task( definition, answer)
VALUES ('solve the equation: x^2+2x-15=0; enter the sum of all the roots as answer', '-2');
INSERT INTO Task(definition, answer)
VALUES ('solve the equation: x^2-5x-50=0; enter the sum of all the roots as answer', '5');

--Add some derivative of a function tasks--

--INSERT INTO TaskType(title) VALUES ('finding the derivative of a function');

INSERT INTO Task(definition, answer)
VALUES ( 'find the derivative of a function: y=x^2-5x+6', '2x-5');
INSERT INTO Task(definition, answer)
VALUES ('find the derivative of a function: y=x-5', '1');
INSERT INTO Task( definition, answer)
VALUES ( 'find the derivative of a function: y=sin(x) + e^(x) + 17x', 'cos(x) + e^(x) + 17');
INSERT INTO Task( definition, answer)
VALUES ( 'find the derivative of a function: y=3x^3 + const', 'nu chto tam');
INSERT INTO Task( definition, answer)
VALUES ( 'find the derivative of a function: y=sin(x^2+8x+1)', '(2x+8)cos(x^2+8x+1)');

--Creating actual and expired tests--

INSERT INTO VerificationWork(title, assignation_datetime, deadline)
VALUES ('quadratic equations', now(), '2024-12-31 05:00:00');

INSERT INTO VerificationWork_Task(verification_work_id, task_id) VALUES (1,1);
INSERT INTO VerificationWork_Task(verification_work_id, task_id) VALUES (1,2);
INSERT INTO VerificationWork_Task(verification_work_id, task_id) VALUES (1,3);
INSERT INTO VerificationWork_Task(verification_work_id, task_id) VALUES (1,4);
INSERT INTO VerificationWork_Task(verification_work_id, task_id) VALUES (1,5);

INSERT INTO VerificationWork(title, assignation_datetime, deadline)
VALUES ('derivative of a function', '2024-09-7 18:00:00', '2024-09-09 12:00:00');

INSERT INTO VerificationWork_Task(verification_work_id, task_id) VALUES (2,6);
INSERT INTO VerificationWork_Task(verification_work_id, task_id) VALUES (2,7);
INSERT INTO VerificationWork_Task(verification_work_id, task_id) VALUES (2,8);
INSERT INTO VerificationWork_Task(verification_work_id, task_id) VALUES (2,9);
INSERT INTO VerificationWork_Task(verification_work_id, task_id) VALUES (2,10);
