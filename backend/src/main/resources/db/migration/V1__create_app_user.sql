CREATE TABLE app_user (
    id             uuid         PRIMARY KEY,
    email          text         NOT NULL,
    email_verified boolean      NOT NULL DEFAULT false,
    display_name   text         NOT NULL,
    created_at     timestamptz  NOT NULL DEFAULT now()
);

CREATE TABLE login_method (
    id          uuid         PRIMARY KEY,
    app_user_id uuid         NOT NULL REFERENCES app_user (id) ON DELETE CASCADE,
    type        text         NOT NULL CHECK (type IN ('PASSWORD', 'GOOGLE', 'APPLE', 'TOTP', 'PASSKEY')),
    created_at  timestamptz  NOT NULL DEFAULT now()
);

CREATE UNIQUE INDEX app_user_email_unique ON app_user (lower(email));