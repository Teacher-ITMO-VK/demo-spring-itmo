CREATE TABLE tasks (
                       id    BIGSERIAL PRIMARY KEY,
                       title TEXT NOT NULL,
                       done  BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE notes (
                       id      BIGSERIAL PRIMARY KEY,
                       task_id BIGINT NOT NULL REFERENCES tasks(id) ON DELETE CASCADE,
                       text    TEXT NOT NULL
);

CREATE INDEX idx_notes_task_id ON notes(task_id);