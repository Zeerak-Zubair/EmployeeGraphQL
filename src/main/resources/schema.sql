-- Employee table to store employee details
CREATE TABLE employee (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    age INT,
    class_name VARCHAR(100)
);

-- Subject table to store subjects
CREATE TABLE subject (
    employee_id BIGINT REFERENCES employee(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL
);

-- Attendance table to track employee attendance
CREATE TABLE attendance (
    employee_id BIGINT REFERENCES employee(id) ON DELETE CASCADE,
    total_days INT NOT NULL,
    present_days INT NOT NULL
);



drop table subject;
drop table attendance;
drop table employee;