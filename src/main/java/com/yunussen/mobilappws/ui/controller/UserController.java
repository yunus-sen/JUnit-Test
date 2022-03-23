package com.yunussen.mobilappws.ui.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yunussen.mobilappws.exception.UserServiceException;
import com.yunussen.mobilappws.security.UserPrincipal;
import com.yunussen.mobilappws.service.AddressService;
import com.yunussen.mobilappws.service.UserService;
import com.yunussen.mobilappws.shared.Roles;
import com.yunussen.mobilappws.shared.dto.AdressDto;
import com.yunussen.mobilappws.shared.dto.UserDto;
import com.yunussen.mobilappws.ui.model.request.PasswordResetModel;
import com.yunussen.mobilappws.ui.model.request.PasswordResetRequestModel;
import com.yunussen.mobilappws.ui.model.request.UserDetailsRequestModel;
import com.yunussen.mobilappws.ui.model.response.AddressesRest;
import com.yunussen.mobilappws.ui.model.response.ErrorMessages;
import com.yunussen.mobilappws.ui.model.response.OperationStatusModel;
import com.yunussen.mobilappws.ui.model.response.RequestOperationResult;
import com.yunussen.mobilappws.ui.model.response.UserRest;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

@RestController
@RequestMapping("users")
// @CrossOrigin({"http:/localhost:8080","http:/localhost:8081"})//tüm metotları bu domainlere actım.
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private AddressService addressService;

	@PostAuthorize("hasRole('ADMIN') or returnObject.userId==principal.userId")
	@GetMapping(path = "/{id}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	@ApiImplicitParams({
		@ApiImplicitParam(name="authorization", value="${userController.authorizationHeader.description}", paramType="header")
	})
	public UserRest getUser(@PathVariable(name = "id", required = true) String id) {
		UserDto user = userService.findUserByid(id);

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		System.out.print(((UserPrincipal)principal).getUsername());
		
		
		UserRest returnvalue = modelMapper.map(user, UserRest.class);

		return returnvalue;
	}

	@PostMapping(produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }, consumes = {
			MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {

		if (userDetails.getFirstName().isEmpty())
			throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());

		UserDto userDto = modelMapper.map(userDetails, UserDto.class);		
		//userDto.setRoles(new HashSet<>(Arrays.asList(Roles.ROLE_USER.name())));
		
		userDto.setRole(Roles.ROLE_USER.name());
		
		UserDto createdUser = userService.createUser(userDto);
		UserRest returnvalue = modelMapper.map(createdUser, UserRest.class);

		return returnvalue;

	}

	@PutMapping(path = "{userId}", produces = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_VALUE })
	public UserRest updateUser(@PathVariable(value = "userId") String userId,
			@RequestBody UserDetailsRequestModel userDetails) {

		UserRest returnvalue = new UserRest();
		UserDto userDto = new UserDto();

		BeanUtils.copyProperties(userDetails, userDto);

		UserDto updatedUser = userService.updateUser(userId, userDto);
		BeanUtils.copyProperties(updatedUser, returnvalue);

		return returnvalue;
	}

	//@Secured("ROLE_ADMIN")
	//@PreAuthorize("hasAuthority('DELETE_AUTHORITY')")
	@PreAuthorize("hasRole('ADMIN') or #userId==principal.userId")
	@DeleteMapping(path = "{userId}", produces = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_VALUE })
	public OperationStatusModel deleteUser(@PathVariable(value = "userId") String userId) {
		userService.delete(userId);
		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setActionName(RequestOperationName.DELETE.name());
		returnValue.setActionResult(RequestOperationResult.SUCCES.name());
		return returnValue;
	}

	@GetMapping(produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "50") int limit) {
		List<UserRest> returnValue = new ArrayList<UserRest>();
		List<UserDto> users = userService.getUsers(page, limit);
		for (UserDto userDto : users) {
			UserRest userModel = new UserRest();
			BeanUtils.copyProperties(userDto, userModel);
			returnValue.add(userModel);
		}
		return returnValue;
	}

	@GetMapping(path = "/{id}/addresses", produces = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	public List<AddressesRest> getUserAddresses(@PathVariable String id) {
		List<AddressesRest> returnValue = new ArrayList<AddressesRest>();
		List<AdressDto> addressDto = addressService.getAddresses(id);

		for (AdressDto adress : addressDto) {
			AddressesRest rest = modelMapper.map(adress, AddressesRest.class);
			returnValue.add(rest);
		}

		return returnValue;
	}

	@GetMapping(path = "/{userId}/addresses/{addressId}", produces = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	public EntityModel<AddressesRest> getUserAddress(@PathVariable String userId, @PathVariable String addressId) {

		AdressDto addressDto = addressService.getAddress(addressId);
		AddressesRest rest = modelMapper.map(addressDto, AddressesRest.class);

		// http//localhost:8080/users/<userId>
		Link userLink = WebMvcLinkBuilder.linkTo(UserController.class).slash(userId).withRel("user");
		Link userAddressesLink = WebMvcLinkBuilder.linkTo(UserController.class).slash(userId).slash("addresses")
				.withRel("addresses");
		Link selfLink = WebMvcLinkBuilder.linkTo(UserController.class).slash(userId).slash("addressses")
				.slash(addressId).withSelfRel();

		/*
		 * rest.add(userLink); rest.add(userAddressesLink); rest.add(selfLink);
		 */
		return EntityModel.of(rest, Arrays.asList(userLink, userAddressesLink, selfLink));
	}

	/*
	 * http://localhost:8080/mobile-app-ws/users/email-verification?token=sdfsdf
	 */
	@GetMapping(path = "/email-verification", 
				produces = { MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
	public OperationStatusModel verifyEmailToken(@RequestParam(value = "token") String token) {

		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setActionName(RequestOperationName.VERIFY_EMAIL.name());

		boolean isVerified = userService.verifyEmailToken(token);

		if (isVerified) {
			returnValue.setActionResult(RequestOperationResult.SUCCES.name());
		} else {
			returnValue.setActionResult(RequestOperationResult.ERROR.name());
		}

		return returnValue;
	}

	/*
	 * http:// localhost:8080/mobile-app-ws/users/password-reset-request
	 * 
	 */
	@PostMapping(path = "/password-reset-request",
			produces = { MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE },
			consumes = { MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
	public OperationStatusModel requestReset(@RequestBody PasswordResetRequestModel passwordResetRequestModel) {
		
		OperationStatusModel returnValue = new OperationStatusModel();

		boolean operationResult = userService.requestPasswordReset(passwordResetRequestModel.getEmail());

		returnValue.setActionName(RequestOperationName.REQUEST_PASSWORD_RESET.name());
		returnValue.setActionResult(RequestOperationResult.ERROR.name());

		if (operationResult) {
			returnValue.setActionResult(RequestOperationResult.SUCCES.name());
		}

		return returnValue;
	}

	@PostMapping(path = "/password-reset", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public OperationStatusModel resetPassword(@RequestBody PasswordResetModel passwordResetModel) {
		OperationStatusModel returnValue = new OperationStatusModel();

		boolean operationResult = userService.resetPassword(passwordResetModel.getToken(),
				passwordResetModel.getPassword());

		returnValue.setActionName(RequestOperationName.PASSWORD_RESET.name());
		returnValue.setActionResult(RequestOperationResult.ERROR.name());

		if (operationResult) {
			returnValue.setActionResult(RequestOperationResult.SUCCES.name());
		}

		return returnValue;
	}

}