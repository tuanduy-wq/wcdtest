CREATE DATABASE IF NOT EXISTS player_evaluation DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE player_evaluation;

CREATE TABLE indexer (
                         index_id INT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         valueMin FLOAT NOT NULL,
                         valueMax FLOAT NOT NULL
);

CREATE TABLE player (
                        player_id INT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        full_name VARCHAR(255),
                        age VARCHAR(50),
                        index_id INT
);

CREATE TABLE player_index (
                              id INT AUTO_INCREMENT PRIMARY KEY,
                              player_id INT,
                              index_id INT,
                              value FLOAT,
                              FOREIGN KEY (player_id) REFERENCES player(player_id) ON DELETE CASCADE,
                              FOREIGN KEY (index_id) REFERENCES indexer(index_id) ON DELETE CASCADE
);

INSERT INTO indexer (name, valueMin, valueMax) VALUES
                                                   ('Tốc độ', 0, 100),
                                                   ('Thể lực', 0, 100),
                                                   ('Kỹ năng', 0, 100),
                                                   ('Chiều cao', 150, 220);