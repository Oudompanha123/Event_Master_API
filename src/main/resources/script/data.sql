
-- search member by name
SELECT member.member_id, member_name, gender, phone, email, address, picture, date_of_birth, role
FROM member INNER JOIN otp ON member.member_id = otp.member_id
WHERE org_id = 118
  AND otp.is_verify = true
  AND is_approve = true
  AND status = true
  AND member_name ILIKE '%u%'
LIMIT 8 OFFSET 0