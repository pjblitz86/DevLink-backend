package ca.javau11.dtos;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.constraints.NotEmpty;

public class ProfileDTO {

    private String company;
    private String website;
    private String location;

    @NotEmpty(message = "Status is required")
    private String status;

    @NotEmpty(message = "Skills field is required")
    private String skills;

    private String bio;
    private String githubUserName;
    private String youtube;
    private String twitter;
    private String facebook;
    private String linkedin;
    private String instagram;
    
    public ProfileDTO() {}
    
    public ProfileDTO(String company, String website, String location, String status,
            String skills, String bio, String githubUserName, String youtube,
            String twitter, String facebook, String linkedin, String instagram) {
		this.company = company;
		this.website = website;
		this.location = location;
		this.status = status;
		this.skills = skills;
		this.bio = bio;
		this.githubUserName = githubUserName;
		this.youtube = youtube;
		this.twitter = twitter;
		this.facebook = facebook;
		this.linkedin = linkedin;
		this.instagram = instagram;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getGithubUserName() {
        return githubUserName;
    }

    public void setGithubUserName(String githubUserName) {
        this.githubUserName = githubUserName;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public List<String> getSkillsAsList() {
        if (skills == null || skills.isBlank()) {
            return List.of();
        }
        return Arrays.stream(skills.split(","))
                .map(String::trim)
                .collect(Collectors.toList());
    }
}
