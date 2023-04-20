INSERT INTO users VALUES ('keith', '{noop}keithpw', '12345678', '123@gmail.com', 'hi im keith');
INSERT INTO user_roles(username, role) VALUES ('keith', 'ROLE_USER');
INSERT INTO user_roles(username, role) VALUES ('keith', 'ROLE_ADMIN');

INSERT INTO users VALUES ('john', '{noop}johnpw', '12345678', '123@gmail.com', 'hi im john');
INSERT INTO user_roles(username, role) VALUES ('john', 'ROLE_ADMIN');

INSERT INTO users VALUES ('mary', '{noop}marypw', '12345678', '123@gmail.com', 'hi im mary');
INSERT INTO user_roles(username, role) VALUES ('mary', 'ROLE_USER');

INSERT INTO users VALUES ('lucas', '{noop}lucaspw', '12345678', '123@gmail.com', 'hi im lucas');
INSERT INTO user_roles(username, role) VALUES ('lucas', 'ROLE_USER');
INSERT INTO user_roles(username, role) VALUES ('lucas', 'ROLE_ADMIN');

