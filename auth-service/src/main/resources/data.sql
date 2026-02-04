-- Insertar cliente
INSERT INTO client_app (client_id, client_secret, duration_in_minutes, required_proof_key)
VALUES ('my-own-client', '$2a$10$QHLrXJ8nlnDlOXEDUnV6iefGNOkKaW9pqJwZcpF.5HrO42PRPzjcO', 10, 1);

COMMIT;

-- tipos de autorizacion
INSERT INTO client_app_authorization_grant_types (client_app_id, authorization_grant_types)
VALUES ((SELECT id FROM client_app WHERE client_id = 'my-own-client'), 'authorization_code');

INSERT INTO client_app_authorization_grant_types (client_app_id, authorization_grant_types)
VALUES ((SELECT id FROM client_app WHERE client_id = 'my-own-client'), 'refresh_token');

INSERT INTO client_app_authorization_grant_types (client_app_id, authorization_grant_types)
VALUES ((SELECT id FROM client_app WHERE client_id = 'my-own-client'), 'client_credentials');

-- metodos de autenticacion
INSERT INTO client_app_client_authentication_methods (client_app_id, client_authentication_methods)
VALUES ((SELECT id FROM client_app WHERE client_id = 'my-own-client'), 'client_secret_basic');

-- redirect uri
INSERT INTO client_app_redirect_uris (client_app_id, redirect_uris)
VALUES ((SELECT id FROM client_app WHERE client_id = 'my-own-client'), 'https://oauthdebugger.com/debug');

-- scopes
INSERT INTO client_app_scopes (client_app_id, scopes)
VALUES ((SELECT id FROM client_app WHERE client_id = 'my-own-client'), 'openid');

INSERT INTO client_app_scopes (client_app_id, scopes)
VALUES ((SELECT id FROM client_app WHERE client_id = 'my-own-client'), 'ALL');

COMMIT;
