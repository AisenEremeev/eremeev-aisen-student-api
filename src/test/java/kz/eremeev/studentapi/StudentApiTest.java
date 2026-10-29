package kz.eremeev.studentapi;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest @AutoConfigureMockMvc @ActiveProfiles("test")
class StudentApiTest {
    @Autowired MockMvc mvc;
    @Autowired StudentRepository repository;
    @Autowired ObjectMapper json;
    @BeforeEach void reset() { repository.deleteAll(); }
    private final String body="{\"name\":\"Aisen Eremeev\",\"email\":\"aisen@example.test\",\"groupName\":\"IT2-2314\"}";
    @Test void createReadAndDelete() throws Exception {
        String result=mvc.perform(post("/api/students").contentType("application/json").content(body))
            .andExpect(status().isCreated()).andExpect(header().exists("Location"))
            .andExpect(jsonPath("$.name").value("Aisen Eremeev")).andReturn().getResponse().getContentAsString();
        long id=json.readTree(result).get("id").asLong();
        mvc.perform(get("/api/students")).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(1));
        mvc.perform(get("/api/students/"+id)).andExpect(status().isOk()).andExpect(jsonPath("$.groupName").value("IT2-2314"));
        mvc.perform(delete("/api/students/"+id)).andExpect(status().isNoContent());
        mvc.perform(get("/api/students/"+id)).andExpect(status().isNotFound());
        assertEquals(0,repository.count());
    }
    @Test void rejectsInvalidInput() throws Exception {
        mvc.perform(post("/api/students").contentType("application/json").content("{\"name\":\"\",\"email\":\"bad\",\"groupName\":\"\"}"))
            .andExpect(status().isBadRequest());
        assertEquals(0,repository.count());
    }
    @Test void missingDeleteReturns404() throws Exception {
        mvc.perform(delete("/api/students/999999")).andExpect(status().isNotFound());
    }
    @Test void displaysStudentName() throws Exception {
        mvc.perform(get("/")).andExpect(status().isOk()).andExpect(jsonPath("$.student").value("CONTROLLED_FAILURE"));
    }
    @Test void updatesStudentAndReturns404ForMissingId() throws Exception {
        Student saved=repository.save(new Student(new StudentRequest("Before", "before@example.test", "OLD")));
        mvc.perform(put("/api/students/"+saved.getId()).contentType("application/json").content(body))
            .andExpect(status().isOk()).andExpect(jsonPath("$.name").value("Aisen Eremeev"));
        assertEquals("IT2-2314",repository.findById(saved.getId()).orElseThrow().getGroupName());
        mvc.perform(put("/api/students/999999").contentType("application/json").content(body))
            .andExpect(status().isNotFound());
    }
}
