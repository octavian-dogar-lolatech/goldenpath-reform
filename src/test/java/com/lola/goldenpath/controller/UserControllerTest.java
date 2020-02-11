package com.lola.goldenpath.controller;

import com.lola.goldenpath.AbstractTest;
import com.lola.goldenpath.dto.*;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.log4j.Log4j2;
import lombok.var;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(SpringJUnit4ClassRunner.class)
@Log4j2
public class UserControllerTest extends AbstractTest {

    @Test
    public void userControllerTests() throws Exception {
        signUpSuccess();
        signUpFailed();
        signInSuccess();
        signInFailed();
        getProfile();
        updateUserRolesSuccess();
        updateUserRolesFailed();
    }

    private void signUpSuccess() throws Exception {
        final String uri = "/users/signUp";
        final SignUpModelDto signUpModelDto = SignUpModelDto.builder()
                .username("bogdanlola")
                .email("bogdanvorobet@gmail.com")
                .password("bogdanlola")
                .name("Bogdan Vorobet")
                .build();

        final var inputJson = super.mapToJson(signUpModelDto);
        final MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON).content(inputJson)).andReturn();

        final var responseBody = mvcResult.getResponse().getContentAsString();
        final ApiResponseDto apiResponseDto = super.mapFromJson(responseBody, ApiResponseDto.class);

        assertEquals(apiResponseDto.getSuccess(), true);
        assertEquals(apiResponseDto.getMessage(), "User registered successfully");
    }

    private void signUpFailed() throws Exception {
        final String uri = "/users/signUp";
        final SignUpModelDto signUpModelDto = SignUpModelDto.builder()
                .username("bogdanlola")
                .email("bogdanvorobet@gmail.com")
                .password("bogdanlola")
                .name("Bogdan Vorobet")
                .build();

        final var inputJson = super.mapToJson(signUpModelDto);
        final MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON).content(inputJson)).andReturn();

        final var responseBody = mvcResult.getResponse().getContentAsString();
        final ApiResponseDto apiResponseDto = super.mapFromJson(responseBody, ApiResponseDto.class);

        assertEquals(apiResponseDto.getSuccess(), false);
        assertEquals(apiResponseDto.getMessage(), "Username is already taken!");
    }

    private void signInSuccess() throws Exception {
        final String uri = "/users/signIn";
        final LoginModelDto loginModelDto = LoginModelDto.builder().username("bogdanlola").password("bogdanlola").build();

        final var inputJson = super.mapToJson(loginModelDto);
        final MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON).content(inputJson)).andReturn();

        final var responseBody = mvcResult.getResponse().getContentAsString();
        final LoginResponseDto loginResponseDto = super.mapFromJson(responseBody, LoginResponseDto.class);

        assertEquals(loginResponseDto.getTokenType(), "Bearer");
        final var userJwtPart = "eyJhbGciOiJIUzUxMiJ9";
        assertEquals(loginResponseDto.getJwtCode().contains(userJwtPart), true);
    }

    private void signInFailed() throws Exception {
        final String uri = "/users/signIn";
        final LoginModelDto loginModelDto = LoginModelDto.builder().username("bogdanlola1").password("bogdanlola1").build();

        final var inputJson = super.mapToJson(loginModelDto);
        try {
            final MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON).content(inputJson)).andReturn();
            Assert.isTrue(mvcResult.getResponse().getErrorMessage().equals("Bad credentials"));
        } catch (final Exception exception) {
            assertTrue(exception.getMessage().contains("Bad credentials"));
        }
    }

    private void getProfile() throws Exception {
        String uri = "/users/signIn";
        final LoginModelDto loginModelDto = LoginModelDto.builder().username("bogdanlola").password("bogdanlola").build();

        final var inputJson = super.mapToJson(loginModelDto);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON).content(inputJson)).andReturn();

        final var responseBody = mvcResult.getResponse().getContentAsString();
        final LoginResponseDto loginResponseDto = super.mapFromJson(responseBody, LoginResponseDto.class);

        uri = "/v1/api/auth/user/profile";
        mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + loginResponseDto.getJwtCode())).andReturn();

        final var profileBodyString = mvcResult.getResponse().getContentAsString();

        final UserDto profile = super.mapFromJson(profileBodyString, UserDto.class);

        assertEquals(profile.getUsername(), "bogdanlola");
        assertEquals(profile.getName(), "Bogdan Vorobet");
        assertEquals(profile.getEmail(), "bogdanvorobet@gmail.com");
        assertEquals(profile.getRole().size(), 1);
    }

    private void updateUserRolesSuccess() throws Exception {
        final String uriSignIn = "/v1/api/auth/signin";
        final LoginModelDto loginModelDto = LoginModelDto.builder().username("bogdanlola").password("bogdanlola").build();

        var inputJson = super.mapToJson(loginModelDto);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uriSignIn).contentType(MediaType.APPLICATION_JSON).content(inputJson)).andReturn();

        final var responseBody = mvcResult.getResponse().getContentAsString();
        final LoginResponseDto loginResponseDto = super.mapFromJson(responseBody, LoginResponseDto.class);

        final var uriGetProfile = "/v1/api/auth/user/profile";
        mvcResult = mvc.perform(MockMvcRequestBuilders.get(uriGetProfile).contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + loginResponseDto.getJwtCode())).andReturn();

        final var profileBodyString = mvcResult.getResponse().getContentAsString();

        final UserDto profile = super.mapFromJson(profileBodyString, UserDto.class);
        assertEquals(profile.getRole().size(), 1);

        final var uriUpdateRoles = "/v1/api/auth/admin/update/bogdanlola";

        final var rolesList = Arrays.asList(
                RoleDto.builder().roleName("ROLE_USER").build(),
                RoleDto.builder().roleName("ROLE_ADMIN").build());

        inputJson = super.mapToJson(rolesList);
        mvcResult = mvc.perform(MockMvcRequestBuilders.put(uriUpdateRoles).contentType(MediaType.APPLICATION_JSON).content(inputJson)).andReturn();

        final var resultUpdate = mvcResult.getResponse().getContentAsString();
        final ApiResponseDto apiResponseDto = super.mapFromJson(resultUpdate, ApiResponseDto.class);

        assertEquals(apiResponseDto.getSuccess(), true);
        assertEquals(apiResponseDto.getMessage(), "User roles were updated successfully");
    }

    private void updateUserRolesFailed() throws Exception {
        final String uriSignIn = "/v1/api/auth/signin";
        final LoginModelDto loginModelDto = LoginModelDto.builder().username("bogdanlola").password("bogdanlola").build();

        var inputJson = super.mapToJson(loginModelDto);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uriSignIn).contentType(MediaType.APPLICATION_JSON).content(inputJson)).andReturn();

        final var responseBody = mvcResult.getResponse().getContentAsString();
        final LoginResponseDto loginResponseDto = super.mapFromJson(responseBody, LoginResponseDto.class);

        final var uriGetProfile = "/v1/api/auth/user/profile";
        mvcResult = mvc.perform(MockMvcRequestBuilders.get(uriGetProfile).contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + loginResponseDto.getJwtCode())).andReturn();

        final var profileBodyString = mvcResult.getResponse().getContentAsString();

        final UserDto profile = super.mapFromJson(profileBodyString, UserDto.class);
        assertEquals(profile.getRole().size(), 2);

        final var uriUpdateRoles = "/v1/api/auth/admin/update/bogdanlola";

        final var rolesList = Arrays.asList(
                RoleDto.builder().roleName("ROLE_USER").build(),
                RoleDto.builder().roleName("ROLE_ADMINN").build());

        inputJson = super.mapToJson(rolesList);
        mvcResult = mvc.perform(MockMvcRequestBuilders.put(uriUpdateRoles).contentType(MediaType.APPLICATION_JSON).content(inputJson)).andReturn();

        final var resultUpdate = mvcResult.getResponse().getContentAsString();
        final ApiResponseDto apiResponseDto = super.mapFromJson(resultUpdate, ApiResponseDto.class);

        assertEquals(apiResponseDto.getSuccess(), false);
        assertEquals(apiResponseDto.getMessage(), "List of roles contain 1 invalid values");
    }
}
