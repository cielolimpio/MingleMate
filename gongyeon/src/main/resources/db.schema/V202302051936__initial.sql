# CREATE TABLE hibernate_sequence (
#     next_val BIGINT
# ) ENGINE=InnoDB;
#
# insert into hibernate_sequence values ( 1 );

CREATE TABLE IF NOT EXISTS members (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(20) NOT NULL,
    email VARCHAR(50) NOT NULL,
    password VARCHAR(200) NOT NULL,
    gender VARCHAR(10),
    age INT NOT NULL,
    city VARCHAR(20),
    town VARCHAR(20),
    village VARCHAR(20),
    mon BOOL NOT NULL,
    tue BOOL NOT NULL,
    wed BOOL NOT NULL,
    thu BOOL NOT NULL,
    fri BOOL NOT NULL,
    sat BOOL NOT NULL,
    sun BOOL NOT NULL,
    attendance INT NOT NULL,
    kindness INT NOT NULL,
    studying_hard INT NOT NULL,
    role VARCHAR(10) NOT NULL,
    registered_datetime DATETIME NOT NULL,
    last_modified_datetime DATETIME NOT NULL,
    deleted_datetime DATETIME,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS study_fields (
    id BIGINT NOT NULL AUTO_INCREMENT,
    member_id BIGINT NOT NULL,
    name VARCHAR(20) NOT NULL,
    category VARCHAR(20) NOT NULL,
    registered_datetime DATETIME NOT NULL,
    last_modified_datetime DATETIME NOT NULL,
    deleted_datetime DATETIME,
    PRIMARY KEY (id),
    CONSTRAINT study_fields__member_id FOREIGN KEY (member_id) REFERENCES members(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS matches (
    id BIGINT NOT NULL AUTO_INCREMENT,
    requester_id BIGINT NOT NULL,
    responder_id BIGINT NOT NULL,
    matching_status VARCHAR(20) NOT NULL,
    registered_datetime DATETIME NOT NULL,
    last_modified_datetime DATETIME NOT NULL,
    deleted_datetime DATETIME,
    PRIMARY KEY (id),
    CONSTRAINT matches__requester_id FOREIGN KEY (requester_id) REFERENCES members(id),
    CONSTRAINT matches__responder_id FOREIGN KEY (responder_id) REFERENCES members(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS matching_reviews (
    id BIGINT NOT NULL AUTO_INCREMENT,
    match_id BIGINT NOT NULL,
    reviewer_id BIGINT NOT NULL,
    target_member_id BIGINT NOT NULL,
    attendance BOOL,
    kindness BOOL,
    studying_hard BOOL,
    registered_datetime DATETIME NOT NULL,
    last_modified_datetime DATETIME NOT NULL,
    deleted_datetime DATETIME,
    PRIMARY KEY (id),
    CONSTRAINT matching_reviews__match_id FOREIGN KEY (match_id) REFERENCES matches(id),
    CONSTRAINT matching_reviews__reviewer_id FOREIGN KEY (reviewer_id) REFERENCES members(id),
    CONSTRAINT matching_reviews__target_member_id FOREIGN KEY (target_member_id) REFERENCES members(id)
) ENGINE=InnoDB;