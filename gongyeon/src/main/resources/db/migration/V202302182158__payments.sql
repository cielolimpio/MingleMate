CREATE TABLE IF NOT EXISTS payments (
    id BIGINT NOT NULL AUTO_INCREMENT,
    member_id BIGINT NOT NULL,
    match_id BIGINT NOT NULL,
    amount INTEGER NOT NULL,
    registered_datetime DATETIME NOT NULL,
    last_modified_datetime DATETIME NOT NULL,
    deleted_datetime DATETIME,
    PRIMARY KEY (id),
    CONSTRAINT payments__match_id FOREIGN KEY (match_id) REFERENCES matches(id),
    CONSTRAINT payments__member_id FOREIGN KEY (member_id) REFERENCES members(id)
) ENGINE=InnoDB;