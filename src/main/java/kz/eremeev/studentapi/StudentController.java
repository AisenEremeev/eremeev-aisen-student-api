package kz.eremeev.studentapi;
import java.net.URI;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
@RestController
@RequestMapping("/api/students")
public class StudentController {
    private final StudentRepository repository;
    public StudentController(StudentRepository repository) { this.repository=repository; }
    @GetMapping public List<Student> all() { return repository.findAll(); }
    @GetMapping("/{id}") public Student one(@PathVariable Long id) { return find(id); }
    @PostMapping public ResponseEntity<Student> create(@Valid @RequestBody StudentRequest request) {
        Student saved=repository.save(new Student(request));
        return ResponseEntity.created(URI.create("/api/students/"+saved.getId())).body(saved);
    }
    // UPDATE_FEATURE
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { repository.delete(find(id)); }
    private Student find(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Student not found"));
    }
}
