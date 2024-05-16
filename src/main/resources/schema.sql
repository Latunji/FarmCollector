
DROP TABLE IF EXISTS Farm;
CREATE TABLE Farm (
id INT AUTO_INCREMENT  PRIMARY KEY,
name VARCHAR(50) NOT NULL,
field_id INT(8) NOT NULL,
foreign key (field_id) references Field(id)

);

DROP TABLE IF EXISTS Field;
CREATE TABLE Field (
id INT AUTO_INCREMENT  PRIMARY KEY,
planting_area DOUBLE(15,2) NOT NULL
crop VARCHAR(50) NOT NULL,
season VARCHAR(50) NOT NULL,
expected_product DOUBLE(15,2) NOT NULL,
actual_harvested_product DOUBLE(15,2) NOT NULL
);