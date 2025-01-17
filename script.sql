create table department
(
    id          int auto_increment
        primary key,
    name        varchar(100) not null,
    employee_id int          null,
    constraint unique_department_name
        unique (name)
);

create table employee
(
    id            int auto_increment
        primary key,
    name          varchar(100) not null,
    department_id int          null,
    Salary        double       not null,
    email         varchar(100) not null,
    constraint unique_email
        unique (email),
    constraint employee_ibfk_1
        foreign key (department_id) references department (id)
);

create table attendance
(
    id          int auto_increment
        primary key,
    employee_id int                                 null,
    date        date                                null,
    status      enum ('Present', 'Absent', 'Leave') not null,
    constraint attendance_ibfk_1
        foreign key (employee_id) references employee (id)
);

create index employee_id
    on attendance (employee_id);

alter table department
    add constraint department_employee_id_fk
        foreign key (employee_id) references employee (id);

create index department_id
    on employee (department_id);

create table payroll
(
    id                int auto_increment
        primary key,
    employee_id       int            null,
    days_worked       int            not null,
    calculated_salary decimal(10, 2) null,
    pay_period        varchar(7)     null,
    constraint payroll_ibfk_1
        foreign key (employee_id) references employee (id),
    constraint check_days_worked
        check ((`days_worked` >= 0) and (`days_worked` <= 31))
);

create index employee_id
    on payroll (employee_id);

create definer = root@localhost trigger before_insert_payroll
    before insert
    on payroll
    for each row
BEGIN
DECLARE base_salary DECIMAL(10,2);
SELECT salary INTO base_salary
FROM Employee
WHERE id = NEW.employee_id;
SET NEW.calculated_salary = (base_salary/22)*NEW.days_worked;
END;

create definer = root@localhost trigger before_update_payroll
    before update
    on payroll
    for each row
BEGIN
DECLARE base_salary DECIMAL(10,2);
SELECT salary INTO base_salary
FROM Employee
WHERE id = NEW.employee_id;
SET NEW.calculated_salary = (base_salary/22)*NEW.days_worked;
END;


