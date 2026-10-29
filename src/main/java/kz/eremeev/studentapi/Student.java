package kz.eremeev.studentapi;
import jakarta.persistence.*;
@Entity
@Table(name="students")
public class Student {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false) private String name;
    @Column(nullable=false) private String email;
    @Column(name="student_group", nullable=false) private String groupName;
    protected Student() {}
    public Student(StudentRequest r) { apply(r); }
    public void apply(StudentRequest r) { name=r.name(); email=r.email(); groupName=r.groupName(); }
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getGroupName() { return groupName; }
}
