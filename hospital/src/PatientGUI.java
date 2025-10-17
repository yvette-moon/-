import javax.swing.*;
import java.sql.Date;
import java.sql.*;

public class PatientGUI extends JFrame {
    private JTextField txtPatientId, txtName, txtGender, txtAge, txtBirthDate,
            txtEthnicity, txtIdNumber, txtAdmissionId, txtPhone, txtDoctor;
    private JButton btnAddQueue;

    public PatientGUI() {
        setTitle("病人界面");
        setSize(420, 550);
        setLayout(null);

        int y = 20;
        addLabelAndField("病人ID：", txtPatientId = new JTextField(), y);
        y += 40;
        addLabelAndField("姓名：", txtName = new JTextField(), y);
        y += 40;
        addLabelAndField("性别：", txtGender = new JTextField(), y);
        y += 40;
        addLabelAndField("年龄：", txtAge = new JTextField(), y);
        y += 40;
        addLabelAndField("出生日期(YYYY-MM-DD)：", txtBirthDate = new JTextField(), y);
        y += 40;
        addLabelAndField("民族：", txtEthnicity = new JTextField(), y);
        y += 40;
        addLabelAndField("身份证号：", txtIdNumber = new JTextField(), y);
        y += 40;
        addLabelAndField("住院号：", txtAdmissionId = new JTextField(), y);
        y += 40;
        addLabelAndField("电话：", txtPhone = new JTextField(), y);
        y += 40;
        addLabelAndField("医生姓名：", txtDoctor = new JTextField(), y);

        btnAddQueue = new JButton("加入队列");
        btnAddQueue.setBounds(140, y + 50, 120, 30);
        add(btnAddQueue);

        btnAddQueue.addActionListener(e -> {
            try {
                String patientId = txtPatientId.getText().trim();
                String name = txtName.getText().trim();
                String gender = txtGender.getText().trim();
                String ageText = txtAge.getText().trim();
                String birthText = txtBirthDate.getText().trim();
                String ethnicity = txtEthnicity.getText().trim();
                String idNumber = txtIdNumber.getText().trim();
                String admissionId = txtAdmissionId.getText().trim();
                String phone = txtPhone.getText().trim();
                String doctor = txtDoctor.getText().trim();

                if (patientId.isEmpty() || name.isEmpty() || doctor.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "病人ID、姓名和医生姓名不能为空！");
                    return;
                }

                int age = Integer.parseInt(ageText);
                Date birthDate = Date.valueOf(birthText); // 格式必须为 yyyy-MM-dd

                // 创建 Patient 对象（queue_id=0 因为是新加入队列）
                Patient p = new Patient(
                        patientId,
                        0, // 如果 queue_id 是自增字段，可以传 0 或随意
                        name,
                        gender,
                        age,
                        birthDate,
                        ethnicity,
                        idNumber,
                        admissionId,
                        phone,
                        doctor
                );

                boolean ok = DBhelper.enqueuePatient(p);
                JOptionPane.showMessageDialog(this, ok ? "加入队列成功！" : "加入队列失败！");



            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "输入错误：" + ex.getMessage());
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void addLabelAndField(String label, JTextField field, int y) {
        JLabel lbl = new JLabel(label);
        lbl.setBounds(20, y, 160, 30);
        add(lbl);
        field.setBounds(180, y, 200, 30);
        add(field);
    }
}
