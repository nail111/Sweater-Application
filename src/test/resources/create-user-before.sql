delete from user_role;
delete from users;

insert into users(id, active, password, username)
values
(1, true, '$2a$08$czfoLIq7McenlwtGTKkPPekxkdYnVBw/9V5i3jEGengGZn7BfrxXu', 'admin'),
(2, true, '$2a$08$2NUTfbvpZPMXT6a0iGxwn.yU2VSX/8tsNpm92djS2BWum0nnEOhHy', 'user');

insert into user_role(user_id, roles) values (1, 'ADMIN'), (1, 'USER'), (2, 'USER');