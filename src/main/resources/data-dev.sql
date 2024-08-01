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

INSERT INTO monthly_electricity_consumption (channel_id, KWH, TIME, BILL)
VALUES (-1, 1000, DATEADD('DAY', -1, DATEADD('MONTH', 1, PARSEDATETIME('2024-01-01', 'yyyy-MM-dd'))),
        FLOOR(RAND() * 20) + 40),
       (-1, 1000, DATEADD('DAY', -1, DATEADD('MONTH', 1, PARSEDATETIME('2024-02-01', 'yyyy-MM-dd'))),
        FLOOR(RAND() * 20) + 40),
       (-1, 1000, DATEADD('DAY', -1, DATEADD('MONTH', 1, PARSEDATETIME('2024-03-01', 'yyyy-MM-dd'))),
        FLOOR(RAND() * 20) + 40),
       (-1, 1000, DATEADD('DAY', -1, DATEADD('MONTH', 1, PARSEDATETIME('2024-04-01', 'yyyy-MM-dd'))),
        FLOOR(RAND() * 20) + 40),
       (-1, 1000, DATEADD('DAY', -1, DATEADD('MONTH', 1, PARSEDATETIME('2024-05-01', 'yyyy-MM-dd'))),
        FLOOR(RAND() * 20) + 40),
       (-1, 1000, DATEADD('DAY', -1, DATEADD('MONTH', 1, PARSEDATETIME('2024-06-01', 'yyyy-MM-dd'))),
        FLOOR(RAND() * 20) + 40),
       (-1, 1000, DATEADD('DAY', -1, DATEADD('MONTH', 1, PARSEDATETIME('2024-07-01', 'yyyy-MM-dd'))),
        FLOOR(RAND() * 20) + 40),
       (-1, 1000, DATEADD('DAY', -1, DATEADD('MONTH', 1, PARSEDATETIME('2024-08-01', 'yyyy-MM-dd'))),
        FLOOR(RAND() * 20) + 40),
       (-1, 1000, DATEADD('DAY', -1, DATEADD('MONTH', 1, PARSEDATETIME('2024-09-01', 'yyyy-MM-dd'))),
        FLOOR(RAND() * 20) + 40),
       (-1, 1000, DATEADD('DAY', -1, DATEADD('MONTH', 1, PARSEDATETIME('2024-10-01', 'yyyy-MM-dd'))),
        FLOOR(RAND() * 20) + 40),
       (-1, 1000, DATEADD('DAY', -1, DATEADD('MONTH', 1, PARSEDATETIME('2024-11-01', 'yyyy-MM-dd'))),
        FLOOR(RAND() * 20) + 40),
       (-1, 1000, DATEADD('DAY', -1, DATEADD('MONTH', 1, PARSEDATETIME('2024-12-01', 'yyyy-MM-dd'))),
        FLOOR(RAND() * 20) + 40);

WITH RECURSIVE Dates(DateColumn) AS (SELECT PARSEDATETIME('2024-01-01', 'yyyy-MM-dd')
                                     UNION ALL
                                     SELECT DATEADD('DAY', 1, DateColumn)
                                     FROM Dates
                                     WHERE DATEADD('DAY', 1, DateColumn) <= PARSEDATETIME('2024-12-31', 'yyyy-MM-dd'))
INSERT INTO DAILY_ELECTRICITY_CONSUMPTION (channel_id, KWH, TIME, BILL)
SELECT -1, 1000, DateColumn, FLOOR(RAND() * 20) + 40
FROM Dates;

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
#
#     -- 매일 전력 사용량 데이터 입력
#     WHILE v_month < v_end_month
#         DO
#             WHILE v_day <= DAY(LAST_DAY(CONCAT(v_year, '-', v_month, '-01')))
#                 DO
#                     SET v_date = CONCAT(v_year, '-', LPAD(v_month, 2, '0'), '-', LPAD(v_day, 2, '0'));
#                     INSERT INTO daily_electricity_consumption (channel_id, kwh, time, bill)
#                     VALUES (1, FLOOR(900 + RAND() * 300), v_date, 0); -- 랜덤 전력 사용량 (900 ~ 1200 kWh)
#                     SET v_day = v_day + 1;
#                 END WHILE;
#             SET v_day = 1;
#             SET v_month = v_month + 1;
#         END WHILE;
#
#     -- 매월 전력 사용량 집계 후 입력
#     SET v_month = MONTH(CURDATE());
#     WHILE v_month < v_end_month
#         DO
#             SET v_date = LAST_DAY(CONCAT(v_year, '-', v_month, '-01'));
#             SELECT SUM(kwh)
#             INTO v_monthly_kwh
#             FROM daily_electricity_consumption
#             WHERE DATE(time) BETWEEN CONCAT(v_year, '-', v_month, '-01') AND v_date;
#             INSERT INTO monthly_electricity_consumption (channel_id, KWH, TIME, BILL)
#             VALUES (1, v_monthly_kwh, v_date, 0);
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
#
#     -- 현재 달과 다음 달의 날짜 범위 계산
#     SET v_current_month = CURDATE() - INTERVAL DAY(CURDATE()) - 1 DAY;
#     SET v_next_month = v_current_month + INTERVAL 1 MONTH;
#     SET v_last_day_current_month = LAST_DAY(v_current_month);
#     SET v_last_day_next_month = LAST_DAY(v_next_month);
#
#     -- 현재 달 데이터 입력
#     SET v_current_day = v_current_month;
#     WHILE v_current_day <= v_last_day_current_month
#         DO
#             INSERT INTO daily_predicted_electricity_consumption (channel_id, kwh, time, bill)
#             VALUES (1, FLOOR(800 + RAND() * 400), v_current_day, 0); -- 랜덤 전력 사용량 (800 ~ 1200 kWh)
#             SET v_current_day = v_current_day + INTERVAL 1 DAY;
#         END WHILE;
#
#     -- 다음 달 데이터 입력
#     SET v_current_day = v_next_month;
#     WHILE v_current_day <= v_last_day_next_month
#         DO
#             INSERT INTO daily_predicted_electricity_consumption (channel_id, kwh, time, bill)
#             VALUES (1, FLOOR(800 + RAND() * 400), v_current_day, 0); -- 랜덤 전력 사용량 (800 ~ 1200 kWh)
#             SET v_current_day = v_current_day + INTERVAL 1 DAY;
#         END WHILE;
# END //
#
# DELIMITER ;
#
# CALL InsertPredictedElectricityData();
