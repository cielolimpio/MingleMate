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
    role VARCHAR(10) NOT NULL,
    registered_datetime DATETIME NOT NULL,
    last_modified_datetime DATETIME NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS profile_images (
    id BIGINT NOT NULL AUTO_INCREMENT,
    member_id BIGINT NOT NULL,
    `index` INT NOT NULL,
    filename VARCHAR(200) NOT NULL,
    original_s3_path VARCHAR(200) NOT NULL,
    thumbnail_s3_path VARCHAR(200) NOT NULL,
    registered_datetime DATETIME NOT NULL,
    last_modified_datetime DATETIME NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT profile_images__member_id FOREIGN KEY (member_id) REFERENCES members(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS categories (
    id BIGINT NOT NULL AUTO_INCREMENT,
    parent_category VARCHAR(20) NOT NULL,
    name VARCHAR(20),
    registered_datetime DATETIME NOT NULL,
    last_modified_datetime DATETIME NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS member_categories (
    id BIGINT NOT NULL AUTO_INCREMENT,
    member_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    description VARCHAR(300) NOT NULL,
    period_option VARCHAR(20) NOT NULL,
    registered_datetime DATETIME NOT NULL,
    last_modified_datetime DATETIME NOT NULL,
    deleted_datetime DATETIME,
    PRIMARY KEY (id),
    CONSTRAINT member_categories__member_id FOREIGN KEY (member_id) REFERENCES members(id),
    CONSTRAINT member_categories__category_id FOREIGN KEY (category_id) REFERENCES categories(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS matches (
    id BIGINT NOT NULL AUTO_INCREMENT,
    uuid VARCHAR(30) UNIQUE NOT NULL,
    requester_id BIGINT NOT NULL,
    responder_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL,
    registered_datetime DATETIME NOT NULL,
    last_modified_datetime DATETIME NOT NULL,
    deleted_datetime DATETIME,
    PRIMARY KEY (id),
    CONSTRAINT matches__requester_id FOREIGN KEY (requester_id) REFERENCES members(id),
    CONSTRAINT matches__responder_id FOREIGN KEY (responder_id) REFERENCES members(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS match_histories (
    id BIGINT NOT NULL AUTO_INCREMENT,
    match_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL,
    registered_datetime DATETIME NOT NULL,
    last_modified_datetime DATETIME NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT matching_reviews__match_id FOREIGN KEY (match_id) REFERENCES matches(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS payments (
    id BIGINT NOT NULL AUTO_INCREMENT,
    member_id BIGINT NOT NULL,
    match_id BIGINT NOT NULL,
    amount INT NOT NULL,
    registered_datetime DATETIME NOT NULL,
    last_modified_datetime DATETIME NOT NULL,
    deleted_datetime DATETIME,
    PRIMARY KEY (id),
    CONSTRAINT payments__match_id FOREIGN KEY (match_id) REFERENCES matches(id),
    CONSTRAINT payments__member_id FOREIGN KEY (member_id) REFERENCES members(id)
) ENGINE=InnoDB;