-- 创建数据库
CREATE DATABASE `train`;

-- 使用数据库
USE `train`;


-- 用户信息表
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    user_name VARCHAR(11) UNIQUE NOT NULL COMMENT '登录账号（手机号）',
    encrypted_password VARCHAR(255) NOT NULL COMMENT '加密密码',
    real_name VARCHAR(50) NOT NULL COMMENT '真实姓名',
    id_card VARCHAR(18) UNIQUE NOT NULL COMMENT '身份证号',
    phone VARCHAR(20) COMMENT '联系电话',
		role ENUM('user', 'admin') NOT NULL DEFAULT 'user' COMMENT '用户角色',
    status ENUM('active', 'inactive') NOT NULL DEFAULT 'active' COMMENT '账号状态',
    created_at DATETIME NOT NULL COMMENT '创建时间'
) COMMENT='用户信息表';

-- 车站信息表
CREATE TABLE station (
    station_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '车站ID',
    station_name VARCHAR(100) NOT NULL COMMENT '车站名称',
    city VARCHAR(50) NOT NULL COMMENT '所属城市',
		city_code VARCHAR(50) NOT NULL COMMENT '城市编码',
		UNIQUE KEY unique_station (station_name, city)
) COMMENT='车站信息表';


-- 车次基础信息表
CREATE TABLE train (
    train_number VARCHAR(20) PRIMARY KEY NOT NULL COMMENT '车次编号(如G231)',
    train_type INT NOT NULL COMMENT '列车类型：动车组1，普速列车2',
    start_station_id INT NOT NULL COMMENT '始发站ID',
    end_station_id INT NOT NULL COMMENT '终点站ID',
		departure_time TIME NOT NULL COMMENT '出发时间',
		total_travel_time INT NOT NULL COMMENT '总行程时间（分钟）',
    FOREIGN KEY (start_station_id) REFERENCES station(station_id),
    FOREIGN KEY (end_station_id) REFERENCES station(station_id)
) COMMENT='车次基础信息表';


-- 每日车次信息表
CREATE TABLE daily_train (
    train_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '车次ID',
    train_number VARCHAR(20) NOT NULL COMMENT '车次编号(如G231)',
		departure_date DATE NOT NULL COMMENT '实际发车日期',
    status ENUM('scheduled', 'departed', 'cancelled') NOT NULL DEFAULT 'scheduled' COMMENT '车次状态',
		seat_config VARCHAR(50) NOT NULL COMMENT '座位配置(字符串用,分割数字，9种座位车厢数量)',
    UNIQUE KEY unique_train_date (train_number, departure_date),
		FOREIGN KEY (train_number) REFERENCES train(train_number)
) COMMENT='每日车次信息表';

-- 路线时刻表
CREATE TABLE route (
    route_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '路线ID',
    train_number VARCHAR(20) NOT NULL COMMENT '车次编号',
    station_sequence INT NOT NULL COMMENT '车站顺序',
    station_id INT NOT NULL COMMENT '经停车站ID',
    mileage INT NOT NULL COMMENT '距始发站里程(km)',
    stop_duration INT COMMENT '停留时间(分钟)',
		arrival_time_offset INT NOT NULL DEFAULT 0 COMMENT '到达本站距离始发站的时间偏移（单位分钟）',
    FOREIGN KEY (station_id) REFERENCES station(station_id),
		FOREIGN KEY (train_number) REFERENCES train(train_number),
    UNIQUE KEY unique_route (train_number, station_sequence)
) COMMENT='路线时刻表';

-- 座位类型配置表
CREATE TABLE seat_config (
    type_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '座位类型ID',
    type_name VARCHAR(50) UNIQUE NOT NULL COMMENT '类型名称（如：一等座、硬卧）',
    seats_per_carriage INT NOT NULL COMMENT '每车厢座位数',
    base_price DECIMAL(10,2) NOT NULL COMMENT '基础价格（元/公里）',
    description VARCHAR(255) COMMENT '类型描述'
) COMMENT='座位类型配置表';

-- 物理座位表
CREATE TABLE seat (
    seat_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '物理座位ID',
    train_id INT NOT NULL COMMENT '关联车次ID',
		seat_type_id INT NOT NULL COMMENT '座位类型',
    carriage_number INT NOT NULL COMMENT '车厢号 07',
    seat_number VARCHAR(10) NOT NULL COMMENT '座位号 02A',
		status ENUM('available', 'locked', 'occupied', 'damaged') NOT NULL DEFAULT 'available' COMMENT '座位状态：available-可售, locked-锁定中, occupied-已占用, damaged-损坏停用',
    FOREIGN KEY (train_id) REFERENCES daily_train(train_id),
		FOREIGN KEY (seat_type_id) REFERENCES seat_config(type_id),
    UNIQUE KEY unique_seat (train_id, carriage_number, seat_number)
) COMMENT='物理座位表';



-- 订单主表（保留核心订单信息）
CREATE TABLE orders (
    order_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '订单ID',
    order_number CHAR(16) UNIQUE NOT NULL COMMENT '16位订单号',
    user_id INT NOT NULL COMMENT '用户ID',
    train_id INT NOT NULL COMMENT '关联车次ID',
    train_number VARCHAR(20) NOT NULL COMMENT '车次编号',
    train_date DATE NOT NULL COMMENT '发车日期',
    start_station_id INT NOT NULL COMMENT '出发站ID',
    end_station_id INT NOT NULL COMMENT '到达站ID',
		departure_time TIME NOT NULL COMMENT '出发时间',
		arrival_time TIME NOT NULL COMMENT '到达时间',
    payment_amount INT NOT NULL COMMENT '支付金额',
    status INT NOT NULL DEFAULT 0 COMMENT '订单状态 0待支付 1已支付 2已取消 3已完成 4已退款',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (start_station_id) REFERENCES station(station_id),
    FOREIGN KEY (end_station_id) REFERENCES station(station_id),
    FOREIGN KEY (train_id) REFERENCES daily_train(train_id)
) COMMENT='订单主表';

-- 订单乘客关联表（存储乘客及座位信息）
CREATE TABLE order_passengers (
    passenger_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '乘客记录ID',
    order_id INT NOT NULL COMMENT '所属订单ID',
    seat_id INT NOT NULL COMMENT '物理座位ID',
    passenger_name VARCHAR(50) NOT NULL COMMENT '乘车人姓名',
		passenger_id_type INT NOT NULL COMMENT '证件类型',
    passenger_id_card VARCHAR(18) NOT NULL COMMENT '证件号',
    ticket_type INT NOT NULL DEFAULT '0' COMMENT '票种类型，0成人票，1学生票',
    amount INT NOT NULL COMMENT '车票金额',
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (seat_id) REFERENCES seat(seat_id)
) COMMENT='订单乘客信息表';

ALTER TABLE seat ADD INDEX idx_query (seat_type_id, status, train_id, carriage_number, seat_number);

-- 创建复合索引加速查询
ALTER TABLE seat ADD INDEX idx_train_seat_status (
    train_id, 
    seat_type_id, 
    status
);

-- 座位排序索引（可选）
ALTER TABLE seat ADD INDEX idx_seat_order (
    carriage_number,
    seat_number
);

ALTER TABLE orders ADD INDEX idx_status_created (status, created_at);
ALTER TABLE order_passengers ADD INDEX idx_order_id (order_id);
ALTER TABLE seat ADD INDEX idx_seat_status (status);
ALTER TABLE orders ADD INDEX idx_user_created (user_id, created_at);

-- 为登录账号（手机号）添加索引，用于快速查找用户
CREATE INDEX idx_users_user_name ON users(user_name);
-- 为身份证号添加索引，方便根据身份证号查询用户
CREATE INDEX idx_users_id_card ON users(id_card);
-- 为城市编码添加索引，用于根据城市编码查询车站
CREATE INDEX idx_station_city_code ON station(city_code);
-- 为车站名称和城市添加联合索引，用于快速定位特定车站
CREATE INDEX idx_station_station_name_city ON station(station_name, city);
-- 为车次编号添加索引，用于快速查找车次信息
CREATE INDEX idx_train_train_number ON train(train_number);
-- 为始发站ID和终点站ID添加索引，用于根据站点查询车次
CREATE INDEX idx_train_start_station_id ON train(start_station_id);
CREATE INDEX idx_train_end_station_id ON train(end_station_id);
-- 为车次编号和发车日期添加联合索引，用于根据车次和日期查询每日车次信息
CREATE INDEX idx_daily_train_train_number_date ON daily_train(train_number, departure_date);
-- 为车次状态添加索引，用于根据状态筛选车次
CREATE INDEX idx_daily_train_status ON daily_train(status);
-- 为车次编号和车站顺序添加联合索引，用于快速定位特定车次的特定站点信息
CREATE INDEX idx_route_train_number_sequence ON route(train_number, station_sequence);
-- 为车站ID添加索引，用于根据车站查询路线信息
CREATE INDEX idx_route_station_id ON route(station_id);
-- 为座位类型名称添加索引，用于快速查找座位类型信息
CREATE INDEX idx_seat_config_type_name ON seat_config(type_name);
-- 为车次ID和座位状态添加联合索引，用于根据车次和状态查询座位信息
CREATE INDEX idx_seat_train_id_status ON seat(train_id, status);
-- 为座位类型ID添加索引，用于根据座位类型查询座位
CREATE INDEX idx_seat_seat_type_id ON seat(seat_type_id);


-- 为订单号添加索引，用于快速查找订单信息
CREATE INDEX idx_orders_order_number ON orders(order_number);
-- 为用户ID添加索引，用于根据用户查询订单
CREATE INDEX idx_orders_user_id ON orders(user_id);
-- 为车次ID添加索引，用于根据车次查询订单
CREATE INDEX idx_orders_train_id ON orders(train_id);
-- 为发车日期添加索引，用于根据日期筛选订单
CREATE INDEX idx_orders_train_date ON orders(train_date);
-- 为订单状态添加索引，用于根据状态筛选订单
CREATE INDEX idx_orders_status ON orders(status);

-- 为所属订单ID添加索引，用于根据订单查询乘客信息
CREATE INDEX idx_order_passengers_order_id ON order_passengers(order_id);
-- 为物理座位ID添加索引，用于根据座位查询乘客信息
CREATE INDEX idx_order_passengers_seat_id ON order_passengers(seat_id);