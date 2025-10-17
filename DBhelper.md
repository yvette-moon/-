```java
import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBhelper {
    private static final String URL = "jdbc:mysql://localhost:3306/hospital?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "123456";

    // 获取数据库连接
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    // 将病人直接加入队列
    public static boolean enqueuePatient(Patient p) {
        String sql = "INSERT INTO queue_info (patient_id, queue_id,patient_name, gender, age, birth_date, ethnicity, id_number, admission_id, phone, doctor_name) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getPatientId());// 需要在 Patient 类加 getQueueId()
            stmt.setInt(2, p.getQueueId());
            stmt.setString(3, p.getName());
            stmt.setString(4, p.getGender());
            stmt.setInt(5, p.getAge());
            stmt.setDate(6, p.getBirthDate());
            stmt.setString(7, p.getEthnicity());
            stmt.setString(8, p.getIdNumber());
            stmt.setString(9, p.getAdmissionId());
            stmt.setString(10, p.getPhone());
            stmt.setString(11, p.getDoctorName());

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // 获取队列中的第一个病人
    public static Patient getNextPatient(String doctorName) {
        String sql = "SELECT * FROM queue_info WHERE doctor_name = ? ORDER BY queue_id ASC LIMIT 1";
        //这里获取病人的时候是会根据医生姓名载入的，不同医生的病人不相同
       //意思是从 queue_info 表中，找到指定医生 (doctor_name) 的队列，并按队列顺序返回 最先排队的一个病人 的所有信息。
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, doctorName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Patient(
                            rs.getString("patient_id"),
                            rs.getInt("queue_id"),
                            rs.getString("patient_name"),
                            rs.getString("gender"),
                            rs.getInt("age"),
                            rs.getDate("birth_date"),
                            rs.getString("ethnicity"),
                            rs.getString("id_number"),
                            rs.getString("admission_id"),
                            rs.getString("phone"),
                            rs.getString("doctor_name")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 删除队列中的第一个病人
    public static boolean deleteFirstPatient() {
        String sql = "DELETE FROM queue_info " +
                "WHERE queue_id = (SELECT qid FROM (SELECT MIN(queue_id) AS qid FROM queue_info) AS tmp)";
        //这边写了一个嵌套的sql语句，删除的那一条id要是id最小的那一个
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // 获取指定医生的所有队列病人
    public static List<Patient> getQueueByDoctor(String doctorName) {
        List<Patient> list = new ArrayList<>();
        String sql = "SELECT * FROM queue_info WHERE doctor_name = ? ORDER BY queue_id ASC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, doctorName);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Patient p = new Patient(
                            rs.getString("patient_id"),
                            rs.getInt("queue_id"),
                            rs.getString("patient_name"),
                            rs.getString("gender"),
                            rs.getInt("age"),
                            rs.getDate("birth_date"),
                            rs.getString("ethnicity"),
                            rs.getString("id_number"),
                            rs.getString("admission_id"),
                            rs.getString("phone"),
                            rs.getString("doctor_name")
                    );
                    list.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
```

## 注释
```java
private static final String URL = "jdbc:mysql://localhost:3306/hospital?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "123456";

    // 获取数据库连接
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
```
前面这一块都是载入数据库的

```java
// 将病人直接加入队列
 public static boolean enqueuePatient(Patient p) {
        String sql = "INSERT INTO queue_info (patient_id, queue_id,patient_name, gender, age, birth_date, ethnicity, id_number, admission_id, phone, doctor_name) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getPatientId());
            stmt.setInt(2, p.getQueueId());
            stmt.setString(3, p.getName());
            stmt.setString(4, p.getGender());
            stmt.setInt(5, p.getAge());
            stmt.setDate(6, p.getBirthDate());
            stmt.setString(7, p.getEthnicity());
            stmt.setString(8, p.getIdNumber());
            stmt.setString(9, p.getAdmissionId());
            stmt.setString(10, p.getPhone());
            stmt.setString(11, p.getDoctorName());

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
```
1.定义一个 SQL语句字符串

tring sql = "INSERT INTO queue_info (patient_id, queue_id,patient_name, gender, age, birth_date, ethnicity, id_number, admission_id, phone, doctor_name) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
语义是：
往表 queue_info 中插入一条记录；
字段是 patient_id......
? 是占位符，后面会用 Java 程序传入真实值；

2.ry-with-resources语法，会在语句块执行完后自动关闭数据库连接和语句对象

getConnection()：调用一个方法来获取数据库连接对象（Connection）。

conn.prepareStatement(sql)：基于上面的 SQL 语句创建一个可执行的 SQL 对象

3.getPatientId() 是在patient类里面写的方法

4.Cath异常处理机制的一部分，它专门用来捕获和处理 SQL 相关的异常

```java
 // 获取队列中的第一个病人
    public static Patient getNextPatient(String doctorName) {
        String sql = "SELECT * FROM queue_info WHERE doctor_name = ? ORDER BY queue_id ASC LIMIT 1";
        //这里获取病人的时候是会根据医生姓名载入的，不同医生的病人不相同
       //意思是从 queue_info 表中，找到指定医生 (doctor_name) 的队列，并按队列顺序返回 最先排队的一个病人 的所有信息。
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, doctorName); 
            try (ResultSet rs = ps.executeQuery()) { //执行查询：executeQuery() 返回一个 ResultSet，存放查询结果
                if (rs.next()) {//如果有记录的话返回这些
                    return new Patient(
                            rs.getString("patient_id"),
                            rs.getInt("queue_id"),
                            rs.getString("patient_name"),
                            rs.getString("gender"),
                            rs.getInt("age"),
                            rs.getDate("birth_date"),
                            rs.getString("ethnicity"),
                            rs.getString("id_number"),
                            rs.getString("admission_id"),
                            rs.getString("phone"),
                            rs.getString("doctor_name")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
```
新加了注释


```java
// 删除队列中的第一个病人
    public static boolean deleteFirstPatient() {
        String sql = "DELETE FROM queue_info " +
                "WHERE queue_id = (SELECT qid FROM (SELECT MIN(queue_id) AS qid FROM queue_info) AS tmp)";
        //删除的那一条id要是id最小的那一个
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
```
DELETE FROM queue_info ORDER BY queue_id ASC LIMIT 1;一开始写的是这个

在同一条语句里对同一张表同时读写，所以很多教程推荐使用“子查询包装临时表”的写法

```java
// 获取指定医生的所有队列病人
    public static List<Patient> getQueueByDoctor(String doctorName) {
        List<Patient> list = new ArrayList<>();//创建一个空的 ArrayList，用来存储查询到的 Patient 对象
        String sql = "SELECT * FROM queue_info WHERE doctor_name = ? ORDER BY queue_id ASC";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, doctorName);
            try (ResultSet rs = ps.executeQuery()) {//rs=查询到的相符医生名字的那些信息
                while (rs.next()) {//如果有就把信息加入
                    Patient p = new Patient(
                            rs.getString("patient_id"),
                            rs.getInt("queue_id"),
                            rs.getString("patient_name"),
                            rs.getString("gender"),
                            rs.getInt("age"),
                            rs.getDate("birth_date"),
                            rs.getString("ethnicity"),
                            rs.getString("id_number"),
                            rs.getString("admission_id"),
                            rs.getString("phone"),
                            rs.getString("doctor_name")
                    );
                    list.add(p);//将这个 Patient 对象加入列表 list，那个创建的空的
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;//返回这个表
    }
```


