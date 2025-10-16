CREATE TABLE patient_info (
    doctor_id      VARCHAR(20)   COMMENT '医生ID',
    patient_id     VARCHAR(20)   COMMENT '病人ID',
    name           VARCHAR(50)   COMMENT '姓名',
    gender         VARCHAR(10)   COMMENT '性别',
    age            INT           COMMENT '年龄',
    birth_date     DATE          COMMENT '出生日期',
    ethnicity      VARCHAR(20)   COMMENT '民族',
    id_number      VARCHAR(18)   COMMENT '身份证号',
    admission_id   VARCHAR(20)   COMMENT '住院号',
    phone          VARCHAR(20)   COMMENT '联系方式',
    PRIMARY KEY (patient_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='病人检查信息表';
#创建病人信息表格