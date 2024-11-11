package com.sourcery.sport.user.controller;

import static com.sourcery.sport.utils.UserFactory.CreateMockUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sourcery.sport.mocks.CityServiceMock;
import com.sourcery.sport.mocks.UserServiceMock;
import com.sourcery.sport.tournament.model.City;
import com.sourcery.sport.user.model.User;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class UserSearchControllerTest {

  private UserServiceMock mockUserService;
  private CityServiceMock mockCityService;
  private UserSearchController userSearchController;


  @BeforeEach
  public void setUp() {
    mockUserService = new UserServiceMock();
    mockCityService = new CityServiceMock();
    userSearchController = new UserSearchController(mockUserService);
  }

  @Test
  void testSearchUsers_Success() {
    User mockUser = CreateMockUser();
    UUID cityId = UUID.randomUUID();
    City mockCity = new City();
    mockCity.setId(cityId);
    mockUser.setCity(mockCity);
    mockCityService.saveCity(mockCity);
    mockUserService.addNewUser(mockUser);
    ResponseEntity<List<User>> response = userSearchController.searchUsers(mockUser.getName());
    assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
    assertNotNull(response.getBody());
    assertEquals(1, response.getBody().size());
  }

  @Test
  void testSearchUsers_EmptyList() {
    User mockUser = CreateMockUser();
    ResponseEntity<List<User>> response = userSearchController.searchUsers(mockUser.getName());
    assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().isEmpty());
  }

  @Test
  void testGetAllUsers_Success() {
    mockUserService.addNewUser(new User("123", "Alice", "Smith", "alice@example.com", "1234567890", null, null, null));
    mockUserService.addNewUser(new User("456", "Bob", "Brown", "bob@example.com", "0987654321", null, null, null));

    ResponseEntity<List<User>> response = userSearchController.getAllUsers();

    assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
    assertEquals(2, Objects.requireNonNull(response.getBody()).size());
  }
}