-- script/scheme.sql was never updated with the column that script/data.sql adds
-- after the fact, but MemberRepository queries it. Apply it here so the test
-- database matches the live one.
ALTER TABLE member ADD COLUMN status BOOLEAN DEFAULT TRUE;
