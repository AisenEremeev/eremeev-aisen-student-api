package kz.eremeev.studentapi;
import jakarta.validation.constraints.*;
public record StudentRequest(
    @NotBlank @Size(max=150) String name,
    @NotBlank @Email @Size(max=200) String email,
    @NotBlank @Size(max=50) String groupName) {}
