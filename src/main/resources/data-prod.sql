insert into organizations (electricity_budget, name, organization_code)
values (300000, 'nhnacademy', '1234');

insert into modbus_sensors (organization_id, sensor_name, sensor_model_name, ip, port, channel_count, alive)
values (1, 'temp_sensor_name', 'asdf1234', '123.123.123.123', 9999, 1, 'UP');

insert into places (organization_id, place_name)
values (1, 'total'),
       (1, 'class_a'),
       (1, 'office');

insert into channels (sensor_id, place_id, channel_name, address, function_code, type)
values (1, 1, 'main', 0, 0, 0),
       (1, 2, 'automatic_door', 0, 0, 0),
       (1, 2, 'ac_indoor_unit', 0, 0, 0),
       (1, 2, 'main', 0, 0, 0),
       (1, 3, 'ac', 0, 0, 0);

insert into channels (channel_id, sensor_id, place_id, channel_name, address, function_code, type)
values (-1, 1, 1, 'main', 0, 0, 0);

insert into budget_history (organization_id, change_time, budget)
values (1, '2024-04-27', 1000);

insert into users (user_id, created_at, name, password, role, organization_id)
values ('asdf', '2024-04-17', 'jun', '$2a$10$V7Q0.Xd9AsWDFFAo5528wORWDumtzibztRSeS4VYU76NlgvvzwZHa', 'ADMIN', 1);

SET @channel_id = -1;  -- 여기서 1은 예시 channel_id입니다. 필요에 따라 변경 가능합니다.

-- 쿼리 실행 날짜에 따라 동적으로 오늘 날짜를 계산
SET @today = CURDATE();

-- 임시 테이블을 생성하여 날짜 시퀀스를 만듭니다.
DROP TEMPORARY TABLE IF EXISTS date_sequence;
CREATE TEMPORARY TABLE date_sequence (date_val DATE);

-- 1월 1일부터 오늘까지의 날짜를 date_sequence 테이블에 삽입
INSERT INTO date_sequence(date_val)
SELECT DATE_ADD('2023-01-01', INTERVAL t.n DAY)
FROM (
         SELECT a.N + b.N * 10 + c.N * 100 AS n
         FROM
             (SELECT 0 AS N UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) a,
             (SELECT 0 AS N UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) b,
             (SELECT 0 AS N UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) c
     ) t
WHERE DATE_ADD('2023-01-01', INTERVAL t.n DAY) <= @today;

-- date_sequence 테이블을 사용하여 daily_electricity_consumption 테이블에 데이터 삽입
INSERT INTO daily_electricity_consumption (channel_id, time, kwh, bill)
SELECT @channel_id, date_sequence.date_val, ROUND(RAND() * 100, 2) AS kwh, ROUND(RAND() * 5000) AS bill
FROM date_sequence;

-- 임시 테이블 제거
DROP TEMPORARY TABLE IF EXISTS date_sequence;

DROP TEMPORARY TABLE IF EXISTS last_day_of_month;
CREATE TEMPORARY TABLE last_day_of_month (date_val DATE);

-- 2023년 1월 1일부터 현재까지 각 달의 마지막 날짜를 last_day_of_month 테이블에 삽입
INSERT INTO last_day_of_month(date_val)
SELECT LAST_DAY(DATE_ADD('2023-01-01', INTERVAL t.n MONTH))
FROM (
         SELECT a.N + b.N * 10 AS n
         FROM
             (SELECT 0 AS N UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) a,
             (SELECT 0 AS N UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) b
     ) t
WHERE LAST_DAY(DATE_ADD('2023-01-01', INTERVAL t.n MONTH)) <= @today;

-- last_day_of_month 테이블을 사용하여 monthly_electricity_consumption 테이블에 데이터 삽입
INSERT INTO monthly_electricity_consumption (channel_id, time, kwh, bill)
SELECT @channel_id, last_day_of_month.date_val, ROUND(RAND() * 100, 2) AS kwh, ROUND(RAND() * 5000) AS bill
FROM last_day_of_month;

-- 임시 테이블 제거
DROP TEMPORARY TABLE IF EXISTS last_day_of_month;

SET @start_date = DATE_FORMAT(@today, '%Y-%m-01');  -- 이번 달 1일
SET @end_date = LAST_DAY(DATE_ADD(@today, INTERVAL 1 MONTH));  -- 다음 달 마지막 날

-- 임시 테이블을 생성하여 날짜 시퀀스를 만듭니다.
DROP TEMPORARY TABLE IF EXISTS date_sequence;
CREATE TEMPORARY TABLE date_sequence (date_val DATE);

-- @start_date부터 @end_date까지의 날짜를 date_sequence 테이블에 삽입
INSERT INTO date_sequence(date_val)
SELECT DATE_ADD(@start_date, INTERVAL t.n DAY)
FROM (
         SELECT a.N + b.N * 10 + c.N * 100 AS n
         FROM
             (SELECT 0 AS N UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) a,
             (SELECT 0 AS N UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) b,
             (SELECT 0 AS N UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) c
     ) t
WHERE DATE_ADD(@start_date, INTERVAL t.n DAY) <= @end_date;

-- date_sequence 테이블을 사용하여 daily_predicted_electricity_consumption 테이블에 데이터 삽입
INSERT INTO daily_predicted_electricity_consumption (channel_id, time, kwh, bill)
SELECT @channel_id, date_sequence.date_val, ROUND(RAND() * 100, 2) AS kwh, ROUND(RAND() * 5000) AS bill
FROM date_sequence;

-- 임시 테이블 제거
DROP TEMPORARY TABLE IF EXISTS date_sequence;

# DELIMITER //
#
# CREATE PROCEDURE InsertElectricityData()
# BEGIN
#     DECLARE v_day INT DEFAULT 1;
#     DECLARE v_month INT DEFAULT MONTH(CURDATE());
#     DECLARE v_year INT DEFAULT YEAR(CURDATE());
#     DECLARE v_end_month INT DEFAULT v_month + 5;
#     DECLARE v_date DATE;
#     DECLARE v_monthly_kwh INT;
#     DECLARE v_bill INT;
#
#     -- 매일 전력 사용량 데이터 입력
#     WHILE v_month < v_end_month DO
#             WHILE v_day <= DAY(LAST_DAY(CONCAT(v_year, '-', v_month, '-01'))) DO
#                     SET v_date = CONCAT(v_year, '-', LPAD(v_month, 2, '0'), '-', LPAD(v_day, 2, '0'));
#                     SET v_bill = FLOOR(40 + RAND() * 2000); -- 랜덤 빌 값 (40 ~ 60)
#                     INSERT INTO daily_electricity_consumption (channel_id, kwh, time, bill)
#                     VALUES (-1, FLOOR(900 + RAND() * 300), v_date, v_bill); -- 랜덤 전력 사용량 (900 ~ 1200 kWh)
#                     SET v_day = v_day + 1;
#                 END WHILE;
#             SET v_day = 1;
#             SET v_month = v_month + 1;
#         END WHILE;
#
#     -- 매월 전력 사용량 집계 후 입력
#     SET v_month = MONTH(CURDATE());
#     WHILE v_month < v_end_month DO
#             SET v_date = LAST_DAY(CONCAT(v_year, '-', v_month, '-01'));
#             SELECT SUM(kwh)
#             INTO v_monthly_kwh
#             FROM daily_electricity_consumption
#             WHERE DATE(time) BETWEEN CONCAT(v_year, '-', v_month, '-01') AND v_date;
#             SET v_bill = FLOOR(40 + RAND() * 2000); -- 랜덤 빌 값 (40 ~ 60)
#             INSERT INTO monthly_electricity_consumption (channel_id, KWH, TIME, BILL)
#             VALUES (-1, v_monthly_kwh, v_date, v_bill);
#             SET v_month = v_month + 1;
#         END WHILE;
# END //
#
# DELIMITER ;
#
# CALL InsertElectricityData();
#
# DELIMITER //
#
# CREATE PROCEDURE InsertPredictedElectricityData()
# BEGIN
#     DECLARE v_current_month DATE;
#     DECLARE v_next_month DATE;
#     DECLARE v_last_day_current_month DATE;
#     DECLARE v_last_day_next_month DATE;
#     DECLARE v_current_day DATE;
#     DECLARE v_bill INT;
#
#     -- 현재 달과 다음 달의 날짜 범위 계산
#     SET v_current_month = CURDATE() - INTERVAL DAY(CURDATE()) - 1 DAY;
#     SET v_next_month = v_current_month + INTERVAL 1 MONTH;
#     SET v_last_day_current_month = LAST_DAY(v_current_month);
#     SET v_last_day_next_month = LAST_DAY(v_next_month);
#
#     -- 현재 달 데이터 입력
#     SET v_current_day = v_current_month;
#     WHILE v_current_day <= v_last_day_current_month DO
#             SET v_bill = FLOOR(40 + RAND() * 2000); -- 랜덤 빌 값 (40 ~ 60)
#             INSERT INTO daily_predicted_electricity_consumption (channel_id, kwh, time, bill)
#             VALUES (-1, FLOOR(800 + RAND() * 400), v_current_day, v_bill); -- 랜덤 전력 사용량 (800 ~ 1200 kWh)
#             SET v_current_day = v_current_day + INTERVAL 1 DAY;
#         END WHILE;
#
#     -- 다음 달 데이터 입력
#     SET v_current_day = v_next_month;
#     WHILE v_current_day <= v_last_day_next_month DO
#             SET v_bill = FLOOR(40 + RAND() * 2000); -- 랜덤 빌 값 (40 ~ 60)
#             INSERT INTO daily_predicted_electricity_consumption (channel_id, kwh, time, bill)
#             VALUES (-1, FLOOR(800 + RAND() * 400), v_current_day, v_bill); -- 랜덤 전력 사용량 (800 ~ 1200 kWh)
#             SET v_current_day = v_current_day + INTERVAL 1 DAY;
#         END WHILE;
# END //
#
# DELIMITER ;
#
# CALL InsertPredictedElectricityData();
