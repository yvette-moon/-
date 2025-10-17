```java
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.List;

public class DoctorGUI extends JFrame {
    private JButton btnNext, btnRefresh;//按钮 btnNext（下一个病人）、btnRefresh（刷新队列）
    private JLabel lblCurrent, lblDoctor;标签：lblCurrent（显示当前病人）、lblDoctor（显示当前医生）
    private JTable tblQueue;//表格：tblQueue 显示队列
    private JTextField txtDoctorName;//文本输入医生姓名
    private DefaultTableModel tableModel;//管理表格数据

    public DoctorGUI() {//界面初始化
        setTitle("医生界面");
        setSize(900, 500);
        setLayout(null);

        // 医生姓名输入
        JLabel lblInputDoctor = new JLabel("输入医生姓名：");
        lblInputDoctor.setBounds(20, 20, 100, 25);
        add(lblInputDoctor);

        txtDoctorName = new JTextField();
        txtDoctorName.setBounds(130, 20, 120, 25);
        add(txtDoctorName);

        lblDoctor = new JLabel("目前就诊大夫：无");
        lblDoctor.setBounds(270, 20, 200, 25);
        add(lblDoctor);

        JButton btnSetDoctor = new JButton("设置医生");
        btnSetDoctor.setBounds(480, 20, 100, 25);
        add(btnSetDoctor);
//设置医生按钮事件
        btnSetDoctor.addActionListener(e -> {
            String doctorName = txtDoctorName.getText().trim();
            if (!doctorName.isEmpty()) {
                lblDoctor.setText("目前就诊大夫：" + doctorName);
                refreshQueue();
            } else {
                lblDoctor.setText("目前就诊大夫：无");
            }
        });

      //显示队列
        String[] columns = {"病人ID", "姓名", "性别", "年龄", "出生日期", "民族",  "身份证号", "住院号", "电话"};
        tableModel = new DefaultTableModel(columns, 0);
        tblQueue = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tblQueue);
        scrollPane.setBounds(20, 60, 840, 300);
        add(scrollPane);

        // 当前病人显示
        lblCurrent = new JLabel("当前病人：无");
        lblCurrent.setBounds(20, 370, 400, 30);
        add(lblCurrent);

        // 下一个病人按钮
        btnNext = new JButton("下一个病人");
        btnNext.setBounds(450, 370, 120, 30);
        add(btnNext);
      //下一个病人按钮事件
        btnNext.addActionListener(e -> {
            String doctor = txtDoctorName.getText().trim();
            if (doctor.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请先设置医生姓名");
                return;
            }

            DBhelper.deleteFirstPatient();
            refreshQueue();//在后面
        });

        // 刷新队列按钮
        btnRefresh = new JButton("刷新");
        btnRefresh.setBounds(600, 370, 120, 30);
        add(btnRefresh);
//刷新按钮事件
        btnRefresh.addActionListener(e -> refreshQueue());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
//方法刷新
    private void refreshQueue() {
        String doctor = txtDoctorName.getText().trim();
        tableModel.setRowCount(0);
//获取医生姓名，清空表格数据
//下面代码：填充表格
        if (!doctor.isEmpty()) {
            List<Patient> list = DBhelper.getQueueByDoctor(doctor);
            for (Patient p : list) {
                tableModel.addRow(new Object[]{
                        p.getPatientId(),
                        p.getName(),
                        p.getGender(),
                        p.getAge(),
                        p.getBirthDate(),
                        p.getEthnicity(),
                        p.getIdNumber(),
                        p.getAdmissionId(),
                        p.getPhone(),
                });
            }
            // 更新当前病人显示
            Patient current = DBhelper.getNextPatient(doctor);
            if (current != null) {
                lblCurrent.setText("当前病人：" + current.getName() + " (" + current.getIdNumber() + ")");
            } else {
                lblCurrent.setText("当前病人：无");
            }
        }
    }
}
```

