package ca.javau11.controllers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.javau11.entities.Profile;
import ca.javau11.entities.User;
import ca.javau11.services.ProfileService;

@WebMvcTest(ProfileController.class)
public class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfileService profileService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetProfiles() throws Exception {
        Profile profile1 = new Profile("Company1", "Location1", "Status1", Arrays.asList("Java", "Spring"));
        Profile profile2 = new Profile("Company2", "Location2", "Status2", Arrays.asList("Python", "Django"));

        when(profileService.getProfiles()).thenReturn(Arrays.asList(profile1, profile2));

        mockMvc.perform(get("/profiles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].company").value("Company1"))
                .andExpect(jsonPath("$[1].company").value("Company2"));
    }

    @Test
    public void testGetProfileByUserId() throws Exception {
        User user = new User();
        user.setId(1L);

        Profile profile = new Profile("Company1", "Location1", "Status1", Arrays.asList("Java", "Spring"));
        profile.setUser(user);

        when(profileService.getProfileByUserId(1L)).thenReturn(Optional.of(profile));

        mockMvc.perform(get("/user/1/profile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.company").value("Company1"));
    }

    @Test
    public void testCreateProfile() throws Exception {
        Profile profile = new Profile("Company1", "Location1", "Status1", Arrays.asList("Java", "Spring"));
        when(profileService.addProfile(eq(1L), any(Profile.class))).thenReturn(profile);

        mockMvc.perform(post("/user/1/profile/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(profile)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.company").value("Company1"));
    }

    @Test
    public void testUpdateProfile() throws Exception {
        Profile profile = new Profile("UpdatedCompany", "UpdatedLocation", "UpdatedStatus", Arrays.asList("Java"));
        when(profileService.updateProfile(eq(1L), any(Profile.class))).thenReturn(Optional.of(profile));

        mockMvc.perform(put("/profile/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(profile)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.company").value("UpdatedCompany"));
    }

    @Test
    public void testDeleteProfile() throws Exception {
        when(profileService.deleteProfile(1L)).thenReturn(true);

        mockMvc.perform(delete("/profile/1"))
                .andExpect(status().isOk());

        when(profileService.deleteProfile(2L)).thenReturn(false);

        mockMvc.perform(delete("/profile/2"))
                .andExpect(status().isNotFound());
    }
}
