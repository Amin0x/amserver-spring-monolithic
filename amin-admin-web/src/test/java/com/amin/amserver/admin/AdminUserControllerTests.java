import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(UsersAdminController.class)
public class UsersAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UsersAdminController usersAdminController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(usersAdminController).build();
    }

    @Test
    public void testGetUser() throws Exception {
        Long userId = 1L;
        User user = new User();
        when(userService.getUser(userId)).thenReturn(user);

        mockMvc.perform(get("/admin/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("users"))
                .andExpect(view().name("admin/userShowUser"));
    }

    @Test
    public void testGetUsers() throws Exception {
        int page = 1;
        List<User> users = new ArrayList<>();
        when(userService.getAllUser(page)).thenReturn(users);

        mockMvc.perform(get("/admin/users").param("page", String.valueOf(page)))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("users"))
                .andExpect(view().name("admin/usersIndex"));
    }

    @Test
    public void testCreateUser() throws Exception {
        User user = new User();
        MockMultipartFile file = new MockMultipartFile("profileImage", "profile.jpg", "image/jpeg", new byte[0]);
        when(userService.createUser(anyString(), anyString(), anyString(), anyString(), any(MultipartFile.class))).thenReturn(user);

        mockMvc.perform(multipart("/admin/users")
                .file(file)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"test\",\"password\":\"test\",\"name\":\"test\",\"email\":\"test@test.com\"}"))
                .andExpect(status().isOk());
    }

    @Test
public void testCreateDriver() throws Exception {
    MockMultipartFile profileImage = new MockMultipartFile("profileImage", "profile.jpg", "image/jpeg", new byte[0]);
    MockMultipartFile drivingLicenceImage = new MockMultipartFile("drivingLicenceImage", "licence.jpg", "image/jpeg", new byte[0]);
    MockMultipartFile governmentIdImage = new MockMultipartFile("governmentIdImage", "id.jpg", "image/jpeg", new byte[0]);
    UserDriver userDriver = new UserDriver();
    when(userService.createUser(anyString(), anyString(), anyString(), anyString(), any(MultipartFile.class))).thenReturn(new User());
    doNothing().when(userService).createUserDriver(any(User.class), anyString(), any(), any(), any(MultipartFile.class), any(), any(), any(MultipartFile.class));

    mockMvc.perform(multipart("/admin/users/createDriver")
            .file(profileImage)
            .file(drivingLicenceImage)
            .file(governmentIdImage)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\":\"test\",\"password\":\"test\",\"name\":\"test\",\"email\":\"test@test.com\"}"))
            .andExpect(status().isOk());
}

@Test
public void testUpdateUser() throws Exception {
    Long userId = 1L;
    User user = new User();
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    mockMvc.perform(post("/admin/users/{id}/updateUser", userId)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"enabled\":true,\"status\":\"active\",\"email\":\"test@test.com\",\"name\":\"test\",\"verified\":true,\"driverMode\":true}"))
            .andExpect(status().isOk());
}

@Test
public void testDeleteUser() throws Exception {
    Long userId = 1L;
    doNothing().when(userRepository).deleteById(userId);

    mockMvc.perform(post("/admin/users/{id}/delete", userId))
            .andExpect(status().isOk());
}

@Test
public void testUpdateImage() throws Exception {
    Long userId = 1L;
    MockMultipartFile img = new MockMultipartFile("img", "profile.jpg", "image/jpeg", new byte[0]);
    User user = new User();
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    mockMvc.perform(multipart("/admin/users/{id}/updateImage", userId)
            .file(img))
            .andExpect(status().isOk());
}


    // Add more tests for other methods similarly
}
