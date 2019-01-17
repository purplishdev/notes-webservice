INSERT INTO NOTES (id, uuid, version, deleted, title, content, modified, created) VALUES (1, '8070039418fe430cb027cf8fcf774bc7', 0, FALSE, 'Test comment', 'Hello world!', NULL, NOW());
INSERT INTO NOTES (id, uuid, version, deleted, title, content, modified, created) VALUES (2, 'f9645c4e200944339e8a56c1f2700e73', 1, TRUE, 'Temporary comment', 'It should be deleted', NULL, NOW());

INSERT INTO NOTES_HISTORY (id, uuid, note_id, version, title, content, created) VALUES (1, '46fffb578cc64b54ab4c3be79a056eba', 2, 0, 'Temporary comment', 'It should be deleted', NOW());
