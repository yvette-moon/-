# 病人信息类
**代码**
```jdk
//用来储存病人数据
public class Patient {
    private String queue_id;
    private String patient_id ;
    private Timestamp arrival_time;


//构造方法
public Patient() {

}
//方法
public String getPatientId() { return patientId; }


public String toString() {
    return name + " (" + patientId + ")";
}
}
```
## 注释
  这一块是用来储存单个病人的信息的，方便后续插入病人信息，叫号的时候显示病人信息
## 注意
  没写构造方法，补全构造方法，还有在BDhelper里面定义了几个关于patient这个类的一些方法，照着补全就行
