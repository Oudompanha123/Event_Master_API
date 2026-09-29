
-- search member by name
SELECT
    member.member_id,
    member.member_name,
    member.gender,
    member.phone,
    member.email,
    member.address,
    member.picture,
    member.date_of_birth,
    member.role
FROM member
         INNER JOIN otp ON member.member_id = otp.member_id
WHERE member.org_id = 118
  AND otp.is_verify = TRUE
  AND member.is_approve = TRUE
  AND member.status = TRUE
  AND member.member_name ILIKE '%u%'
LIMIT 8 OFFSET 0;

ALTER TABLE member
    ADD COLUMN status BOOLEAN DEFAULT TRUE;