USE test;
CREATE TABLE coursera (
course_id int,
Course_Name text,
About_Course text,
Created_By text,
Thought_by text,
Basic_Info text,
Level text,
Language text,
How_to_pass text,
Rate decimal,
	PRIMARY KEY(course_id, Created_By)
);

COPY coursera (course_id,Course_Name,About_Course,Created_By,Thought_by,Basic_Info,Level,Language,How_to_pass,Rate) FROM 'coursera.csv' WITH HEADER=True AND DELIMITER='|';