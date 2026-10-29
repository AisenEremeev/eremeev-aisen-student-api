package kz.eremeev.studentapi;
import java.util.Map;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.*;
@RestController
public class InfoController {
    private final BuildProperties build;
    public InfoController(BuildProperties build) { this.build=build; }
    @GetMapping("/") public Map<String,String> info() {
        return Map.of("application","eremeev-aisen-student-api","student","Aisen Eremeev",
                      "group","IT2-2314","version",build.getVersion(),"students","/api/students");
    }
}
