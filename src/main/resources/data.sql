-- Insert dummy data into the Employee table
INSERT INTO employee (name, age, class_name)
VALUES
    ('John Doe', 30, 'Class A'),
    ('Jane Smith', 28, 'Class B'),
    ('Mike Johnson', 35, 'Class A'),
    ('Emily Davis', 22, 'Class C'),
    ('David Brown', 40, 'Class B');

-- Insert dummy data into the Subject table
INSERT INTO subject (employee_id, name)
VALUES
    (1, 'Mathematics'),
    (1, 'Physics'),
    (2, 'Chemistry'),
    (2, 'Biology'),
    (3, 'History'),
    (3, 'Geography'),
    (4, 'English'),
    (4, 'Literature'),
    (5, 'Computer Science'),
    (5, 'Physics');

-- Insert dummy data into the Attendance table
INSERT INTO attendance (employee_id, total_days, present_days)
VALUES
    (1, 180, 175),  -- John Doe
    (2, 180, 160),  -- Jane Smith
    (3, 180, 170),  -- Mike Johnson
    (4, 180, 180),  -- Emily Davis
    (5, 180, 165);  -- David Brown