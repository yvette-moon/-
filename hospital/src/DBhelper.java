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
        //这边写了一个嵌套的sql语句
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
