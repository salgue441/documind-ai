package com.documind.user.mapper;

import com.documind.user.dto.RegisterRequest;
import com.documind.user.dto.UserDto;
import com.documind.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * MapStruct mapper interface for converting between User entities and DTOs.
 *
 * <p>This mapper provides conversion methods between {@link User} entities and their corresponding
 * DTO representations ({@link UserDto}, {@link RegisterRequest}). It handles the transformation of
 * data while ignoring sensitive or automatically managed fields during conversion.
 *
 * @author Carlos Salguero
 * @version 1.0
 * @since 2025-08-06
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

  /**
   * Converts a User entity to its DTO representation.
   *
   * <p>Maps all relevant fields from the User entity to the UserDto, including the computed
   * fullName field.
   *
   * @param user the User entity to convert
   * @return the corresponding UserDto
   */
  @Mapping(target = "fullName", source = "fullName")
  UserDto toDto(User user);

  /**
   * Converts a RegisterRequest DTO to a new User entity.
   *
   * <p>Ignores fields that should not be set during registration, such as ID, password hash,
   * security flags, and audit fields. These fields should be set separately by the service layer.
   *
   * @param registerRequest the registration request DTO
   * @return a new User entity with basic registration fields populated
   */
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "passwordHash", ignore = true)
  @Mapping(target = "role", ignore = true)
  @Mapping(target = "isEnabled", ignore = true)
  @Mapping(target = "isAccountNonExpired", ignore = true)
  @Mapping(target = "isAccountNonLocked", ignore = true)
  @Mapping(target = "isCredentialsNonExpired", ignore = true)
  @Mapping(target = "lastLoginAt", ignore = true)
  @Mapping(target = "failedLoginAttempts", ignore = true)
  @Mapping(target = "lockedUntil", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "updatedBy", ignore = true)
  @Mapping(target = "isDeleted", ignore = true)
  User toEntity(RegisterRequest registerRequest);

  /**
   * Updates an existing User entity from a UserDto.
   *
   * <p>Only updates mutable fields while ignoring immutable fields (username, email, password
   * hash), security-related fields, and audit fields.
   *
   * @param userDto the DTO containing updated user information
   * @param user the target User entity to update
   */
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "username", ignore = true)
  @Mapping(target = "email", ignore = true)
  @Mapping(target = "passwordHash", ignore = true)
  @Mapping(target = "role", ignore = true)
  @Mapping(target = "isAccountNonExpired", ignore = true)
  @Mapping(target = "isAccountNonLocked", ignore = true)
  @Mapping(target = "isCredentialsNonExpired", ignore = true)
  @Mapping(target = "failedLoginAttempts", ignore = true)
  @Mapping(target = "lockedUntil", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "updatedBy", ignore = true)
  @Mapping(target = "isDeleted", ignore = true)
  void updateUserFromDto(UserDto userDto, @MappingTarget User user);
}
