import java.sql.Date;
import java.sql.Timestamp;

//用来储存病人数据
public class Patient {
    private String patient_id;
    private int queue_id;
    private String patient_name;
    private String gender;
    private int age;
    private Date birth_date;
    private String ethnicity;
    private String id_number;
    private String admission_id;
    private String phone;
    private String doctor_name;


//构造方法
public Patient(String patient_id,int queue_id,String patient_name, String gender, int age, Date birth_date,
                 String ethnicity, String id_number, String admission_id, String phone, String doctor_name) {
        this.patient_id =patient_id;
        this.queue_id =queue_id;
        this.patient_name =patient_name;
        this.gender = gender;
        this.age = age;
        this.birth_date=birth_date;
        this.ethnicity = ethnicity;
        this.id_number= id_number;
        this.admission_id= admission_id;
        this.phone = phone;
        this.doctor_name= doctor_name;
}
    public String getPatientId() { return patient_id; }
    public String getName() { return patient_name; }
    public String getGender() { return gender; }
    public int getAge() { return age; }
    public Date getBirthDate() { return birth_date; }
    public String getEthnicity() { return ethnicity; }
    public String getIdNumber() { return id_number; }
    public String getAdmissionId() { return admission_id; }
    public String getPhone() { return phone; }
    public String getDoctorName() { return doctor_name; }
    public int getQueueId() {
        return queue_id;
    }



public String toString() {
    return patient_id+ " (" +patient_name+ ")";
}
}