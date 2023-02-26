ALTER TABLE study_fields
    DROP COLUMN name,
    DROP CONSTRAINT study_fields__member_id,
    DROP COLUMN member_id,
    ADD COLUMN main_name VARCHAR(20) AFTER id,
    ADD COLUMN sub_name VARCHAR(20) AFTER main_name;

CREATE TABLE IF NOT EXISTS member_study_fields (
    id BIGINT NOT NULL AUTO_INCREMENT,
    member_id BIGINT NOT NULL,
    study_field_id BIGINT NOT NULL,
    name VARCHAR(20) NOT NULL,
    registered_datetime DATETIME NOT NULL,
    last_modified_datetime DATETIME NOT NULL,
    deleted_datetime DATETIME,
    PRIMARY KEY (id),
    CONSTRAINT member_study_fields__member_id FOREIGN KEY (member_id) REFERENCES members(id),
    CONSTRAINT member_study_fields__study_field_id FOREIGN KEY (study_field_id) REFERENCES study_fields(id)
) ENGINE=InnoDB;