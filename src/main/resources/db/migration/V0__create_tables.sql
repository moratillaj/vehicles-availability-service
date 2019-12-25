
CREATE TABLE vehicles(
    id VARCHAR(200) NOT NULL PRIMARY KEY,
    name VARCHAR(200),
    x FLOAT,
    y FLOAT,
    licence_plate VARCHAR(200),
    vehicle_range INT,
    battery_level INT,
    seats INT,
    helmets INT,
    model VARCHAR(50),
    resource_image_id VARCHAR(200),
    price_per_minute_parking INT,
    price_per_minute_driving INT,
    real_time_data TINYINT,
    resource_type VARCHAR(200),
    engine_type VARCHAR(200),
    company_zone_id INT,
    last_time_available BIGINT);

CREATE TABLE polling_info (
    polling_timestamp BIGINT NOT NULL PRIMARY KEY
);
