

INSERT INTO `oauth_client_details` (`client_id`, `resource_ids`, `client_secret`, `scope`, `authorized_grant_types`, `web_server_redirect_uri`, `authorities`, `access_token_validity`, `refresh_token_validity`, `additional_information`, `autoapprove`) VALUES
('mobileapp', NULL, '$2a$10$Sr98iMcX/FSZyrtV1smaIu90A1.G3FwHxn1tUTs.ShcUFyn6fKh3q', 'read,write', 'password,refresh_token', NULL, NULL, 36000, 1728000, NULL, NULL),
('webapp', NULL, '$2a$10$Sr98iMcX/FSZyrtV1smaIu90A1.G3FwHxn1tUTs.ShcUFyn6fKh3q', 'read,write', 'password,refresh_token', NULL, NULL, 36000, 1728000, NULL, NULL);


INSERT INTO `roles` (`id`, `name`, `created_at`, `created_by`, `updated_at`, `updated_by`) VALUES
(1, 'ROLE_POLICE', '2019-08-05 20:08:07', 1, '2019-08-05 20:08:07', 1),
(2, 'ROLE_CIVILIAN', '2019-08-05 20:08:07', 1, '2019-08-05 20:08:07', 1);