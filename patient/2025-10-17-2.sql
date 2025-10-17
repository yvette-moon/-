#队列表
create table queue_info(
       queue_id Int AUTO_INCREMENT  primary key, #自动编号，表示排队顺序
       patient_id varchar(20),                   #病人编号
       arrival_time Datetime default current_timestamp, #到达时间
       unique(patient_id),                        #unique 让每个病人不能重复
       foreign key (patient_id) references patient_info(patient_id)
       #外键是patientid
       )  ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
       #会用此队列表维护此排列顺序
       