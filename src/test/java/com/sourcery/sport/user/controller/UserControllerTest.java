package com.sourcery.sport.user.controller;

import static com.sourcery.sport.utils.UserFactory.CreateMockUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sourcery.sport.global.GenericResponse;
import com.sourcery.sport.mocks.CityServiceMock;
import com.sourcery.sport.mocks.UserServiceMock;
import com.sourcery.sport.tournament.model.City;
import com.sourcery.sport.user.dto.UserDto;
import com.sourcery.sport.user.dto.UserExistsDto;
import com.sourcery.sport.user.exception.UserNotFoundException;
import com.sourcery.sport.user.model.User;
import java.util.Objects;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class UserControllerTest {


  private UserServiceMock mockUserService;
  private CityServiceMock mockCityService;
  private UserController userController;


  @BeforeEach
  public void setUp() {
    mockCityService = new CityServiceMock();
    mockUserService = new UserServiceMock();
    userController = new UserController(mockUserService, mockCityService);
  }

  @Test
  void testGetUser_Success() {
    User mockUser = CreateMockUser();
    mockUserService.addNewUser(mockUser);
    ResponseEntity<UserDto> response = userController.getUser(mockUser.getId());
    assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
    assertEquals(mockUser.getName(), Objects.requireNonNull(response.getBody()).getName());
    assertEquals(mockUser.getSurname(), Objects.requireNonNull(response.getBody()).getSurname());
    assertEquals(mockUser.getId(), Objects.requireNonNull(response.getBody()).getId());
    assertEquals(mockUser.getEmail(), Objects.requireNonNull(response.getBody()).getEmail());
  }

  @Test
  void testGetUserProfile_UserNotFound() {
    User mockUser = CreateMockUser();
    assertThrowsExactly(UserNotFoundException.class, () -> {
      ResponseEntity<UserDto> response = userController.getUser(mockUser.getId());
      assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
      assertNull(response.getBody());
    });
  }

  @Test
  void testDeleteUser_Success() {
    User mockUser = CreateMockUser();
    mockUserService.addNewUser(mockUser);
    ResponseEntity<Void> response = userController.deleteUser(mockUser.getId());
    assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatusCode().value());
    assertThrowsExactly(UserNotFoundException.class, () -> {
      ResponseEntity<UserDto> userLookup = userController.getUser(mockUser.getId());
      assertEquals(HttpStatus.BAD_REQUEST.value(), userLookup.getStatusCode().value());
    });
  }

  @Test
  void testDeleteUser_UserNotFound() {
    User mockUser = CreateMockUser();
    assertThrowsExactly(UserNotFoundException.class, () -> {
      ResponseEntity<Void> response = userController.deleteUser(mockUser.getId());
      assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
      assertNull(response.getBody());
    });
  }

  @Test
  void testCheckUser_Exists() {
    User mockUser = CreateMockUser();
    mockUserService.addNewUser(mockUser);
    ResponseEntity<UserExistsDto> response = userController.checkUser(mockUser.getId());
    assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
    assertTrue(Objects.requireNonNull(response.getBody()).getUserExists());
  }

  @Test
  void testCreateUser_Success() {
    User mockUser = CreateMockUser();
    UUID cityId = UUID.randomUUID();
    City mockCity = new City();
    mockCity.setId(cityId);
    mockUser.setCity(mockCity);
    UserDto userDto = new UserDto(mockUser);
    mockCityService.saveCity(mockCity);
    ResponseEntity<GenericResponse> response = userController.createUser(userDto);
    assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatusCode().value());
    User createdUser = mockUserService.getUserById(mockUser.getId());
    assertNotNull(createdUser);
    assertEquals(mockUser.getName(), createdUser.getName());
    assertEquals(mockCity, createdUser.getCity());
  }


  @Test
  void testUpdateUser_Success() {
    String newName = "testName";
    String newSurname = "testSurname";
    User mockUser = CreateMockUser();
    UUID cityId = UUID.randomUUID();
    City mockCity = new City();
    mockCity.setId(cityId);
    mockUser.setCity(mockCity);

    mockCityService.saveCity(mockCity);
    mockUserService.addNewUser(mockUser);

    mockUser.setName(newName);
    mockUser.setSurname(newSurname);
    UserDto userDto = new UserDto(mockUser);
    ResponseEntity<UserDto> response = userController.updateUser(mockUser.getId(), userDto);

    assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
    assertNotNull(response.getBody());
    assertEquals(newName, response.getBody().getName());
    assertEquals(newSurname, response.getBody().getSurname());
    assertEquals(cityId, response.getBody().getCityId());
  }

  @Test
  void testUpdateUser_UserNotFound() {
    User mockUser = CreateMockUser();
    UserDto userDto = new UserDto(mockUser);
    assertThrowsExactly(UserNotFoundException.class, () -> {
      ResponseEntity<UserDto> response = userController.updateUser(mockUser.getId(), userDto);
      assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
      assertNull(response.getBody());
    });
  }

}