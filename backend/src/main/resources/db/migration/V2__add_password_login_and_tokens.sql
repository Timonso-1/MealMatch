CREATE TABLE password_credential (
    login_method_id uuid        PRIMARY KEY REFERENCES login_method (id) ON DELETE CASCADE,
    password_hash   text        NOT NULL,
    changed_at      timestamptz NOT NULL DEFAULT now()
);

CREATE INDEX login_method_app_user_id_idx ON login_method (app_user_id);

CREATE UNIQUE INDEX login_method_one_password_per_user
    ON login_method (app_user_id) WHERE type = 'PASSWORD';

CREATE TABLE refresh_token (
    id          uuid        PRIMARY KEY,
    app_user_id uuid        NOT NULL REFERENCES app_user (id) ON DELETE CASCADE,
    family_id   uuid        NOT NULL,
    token_hash  text        NOT NULL,
    expires_at  timestamptz NOT NULL,
    used_at     timestamptz,
    revoked_at  timestamptz,
    created_at  timestamptz NOT NULL DEFAULT now()
);

CREATE UNIQUE INDEX refresh_token_hash_unique ON refresh_token (token_hash);
CREATE INDEX refresh_token_app_user_id_idx ON refresh_token (app_user_id);
CREATE INDEX refresh_token_family_id_idx ON refresh_token (family_id);

CREATE TABLE email_token (
    id          uuid        PRIMARY KEY,
    app_user_id uuid        NOT NULL REFERENCES app_user (id) ON DELETE CASCADE,
    purpose     text        NOT NULL CHECK (purpose IN ('EMAIL_VERIFICATION', 'PASSWORD_RESET')),
    token_hash  text        NOT NULL,
    expires_at  timestamptz NOT NULL,
    used_at     timestamptz,
    created_at  timestamptz NOT NULL DEFAULT now()
);

CREATE UNIQUE INDEX email_token_hash_unique ON email_token (token_hash);
CREATE INDEX email_token_app_user_id_idx ON email_token (app_user_id);