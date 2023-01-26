insert into member (created_date, updated_date, email, height, member_status, nickname, oauth_platform, password, profile_image_url, weight)
values (now(), now(), 'email_1@com', 180, 'MEMBER_ACTIVE', '닉네임_1', 'NONE', '{noop}qwe123!@#', 'http://프사_주소', 50);

insert into member (created_date, updated_date, email, height, member_status, nickname, oauth_platform, password, profile_image_url, weight)
values (now(), now(), 'email_2@com', 170, 'MEMBER_ACTIVE', '닉네임_2', 'NONE', '{noop}qwe123!@#', 'http://프사_주소', 60);

insert into member (created_date, updated_date, email, height, member_status, nickname, oauth_platform, password, profile_image_url, weight)
values (now(), now(), 'email_3@com', 170, 'MEMBER_ACTIVE', '닉네임_3', 'NONE', '{noop}qwe123!@#', 'http://프사_주소', 60);

insert into member (created_date, updated_date, email, height, member_status, nickname, oauth_platform, password, profile_image_url, weight)
values (now(), now(), 'email_4@google.com', 160, 'MEMBER_ACTIVE', '닉네임_4', 'GOOGLE', '{noop}qwe123!@#', 'http://프사_주소', 70);

insert into message (content, created_at, deleted_by_sender, deleted_by_receiver, message_room, sender_id, receiver_id)
values ('메시지 내용', '2000-01-01', FALSE, FALSE, 1, 1, 2);

insert into message (content, created_at, deleted_by_sender, deleted_by_receiver, message_room, sender_id, receiver_id)
values ('메시지 내용', '2000-01-02', FALSE, FALSE, 1, 2, 1);

insert into message (content, created_at, deleted_by_sender, deleted_by_receiver, message_room, sender_id, receiver_id)
values ('메시지 내용', '2000-01-03', FALSE, FALSE, 1, 2, 1);

insert into message (content, created_at, deleted_by_sender, deleted_by_receiver, message_room, sender_id, receiver_id)
values ('메시지 내용', '2000-01-02', FALSE, FALSE, 2, 1, 3);

insert into message (content, created_at, deleted_by_sender, deleted_by_receiver, message_room, sender_id, receiver_id)
values ('메시지 내용', '2000-01-01', FALSE, FALSE, 2, 3, 1);

insert into message (content, created_at, deleted_by_sender, deleted_by_receiver, message_room, sender_id, receiver_id)
values ('메시지 내용', '2000-01-01', FALSE, FALSE, 3, 2, 3);

insert into message (content, created_at, deleted_by_sender, deleted_by_receiver, message_room, sender_id, receiver_id)
values ('메시지 내용', '2000-01-02', FALSE, FALSE, 3, 3, 2);