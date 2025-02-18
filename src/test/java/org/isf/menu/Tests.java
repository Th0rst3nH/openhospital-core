/*
 * Open Hospital (www.open-hospital.org)
 * Copyright © 2006-2024 Informatici Senza Frontiere (info@informaticisenzafrontiere.org)
 *
 * Open Hospital is a free and open source software for healthcare data management.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * https://www.gnu.org/licenses/gpl-3.0-standalone.html
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package org.isf.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;

import org.isf.OHCoreTestCase;
import org.isf.menu.manager.UserBrowsingManager;
import org.isf.menu.model.GroupMenu;
import org.isf.menu.model.User;
import org.isf.menu.model.UserGroup;
import org.isf.menu.model.UserMenuItem;
import org.isf.menu.service.GroupMenuIoOperationRepository;
import org.isf.menu.service.MenuIoOperations;
import org.isf.menu.service.UserGroupIoOperationRepository;
import org.isf.menu.service.UserIoOperationRepository;
import org.isf.menu.service.UserMenuItemIoOperationRepository;
import org.isf.permissions.manager.GroupPermissionManager;
import org.isf.permissions.model.GroupPermission;
import org.isf.permissions.model.Permission;
import org.isf.permissions.service.PermissionIoOperationRepository;
import org.isf.utils.exception.OHDataIntegrityViolationException;
import org.isf.utils.exception.OHDataValidationException;
import org.isf.utils.exception.OHException;
import org.isf.utils.exception.OHServiceException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class Tests extends OHCoreTestCase {

	private static TestUser testUser;
	private static TestUserGroup testUserGroup;
	private static TestUserMenu testUserMenu;
	private static TestGroupMenu testGroupMenu;

	@Autowired
	private MenuIoOperations menuIoOperation;
	@Autowired
	private UserBrowsingManager userBrowsingManager;
	@Autowired
	private GroupMenuIoOperationRepository groupMenuIoOperationRepository;
	@Autowired
	private UserGroupIoOperationRepository userGroupIoOperationRepository;
	@Autowired
	private UserIoOperationRepository userIoOperationRepository;
	@Autowired
	private UserMenuItemIoOperationRepository userMenuItemIoOperationRepository;
	@Autowired
	private PermissionIoOperationRepository permissionIoOperationRepository;
	@Autowired
	private GroupPermissionManager groupPermissionManager;

	@BeforeAll
	static void setUpClass() {
		testUser = new TestUser();
		testUserGroup = new TestUserGroup();
		testUserMenu = new TestUserMenu();
		testGroupMenu = new TestGroupMenu();
	}

	@BeforeEach
	void setUp() {
		cleanH2InMemoryDb();
	}

	@Test
	void testUserGroupGets() throws Exception {
		String code = setupTestUserGroup(false);
		checkUserGroupIntoDb(code);
	}

	@Test
	void testUserGroupSets() throws Exception {
		String code = setupTestUserGroup(true);
		checkUserGroupIntoDb(code);
	}

	@Test
	void testUserGets() throws Exception {
		String userName = setupTestUser(false);
		checkUserIntoDb(userName);
	}

	@Test
	void testUserSets() throws Exception {
		String userName = setupTestUser(true);
		checkUserIntoDb(userName);
	}

	@Test
	void testUserMenuGets() throws Exception {
		String code = setupTestUserMenu(false);
		checkUserMenuIntoDb(code);
	}

	@Test
	void testUserMenuSets() throws Exception {
		String code = setupTestUserMenu(true);
		checkUserMenuIntoDb(code);
	}

	@Test
	void testGroupMenuGets() throws Exception {
		Integer code = setupTestGroupMenu(false);
		checkGroupMenuIntoDb(code);
	}

	@Test
	void testGroupMenuSets() throws Exception {
		Integer code = setupTestGroupMenu(true);
		checkGroupMenuIntoDb(code);
	}

	@Test
	void testIoGetUser() throws Exception {
		String userName = setupTestUser(false);
		User foundUser = userIoOperationRepository.findById(userName).orElse(null);
		assertThat(foundUser).isNotNull();
		List<User> users = menuIoOperation.getUser();
		assertThat(users.get(users.size() - 1).getDesc()).isEqualTo(foundUser.getDesc());
	}

	@Test
	void testIoGetUsersFromGroupId() throws Exception {
		String userName = setupTestUser(false);
		User foundUser = userIoOperationRepository.findById(userName).orElse(null);
		assertThat(foundUser).isNotNull();
		List<User> users = menuIoOperation.getUser(foundUser.getUserGroupName().getCode());
		assertThat(users.get(users.size() - 1).getDesc()).isEqualTo(foundUser.getDesc());
	}

	@Test
	void testIoGetUserByName() throws Exception {
		String userName = setupTestUser(false);
		User foundUser = userIoOperationRepository.findById(userName).orElse(null);
		assertThat(foundUser).isNotNull();
		User user = menuIoOperation.getUserByName(userName);
		assertThat(user.getDesc()).isEqualTo(foundUser.getDesc());
	}

	@Test
	void testIoGetUserInfo() throws Exception {
		String userName = setupTestUser(false);
		User foundUser = userIoOperationRepository.findById(userName).orElse(null);
		assertThat(foundUser).isNotNull();
		String description = menuIoOperation.getUsrInfo(userName);
		assertThat(description).isEqualTo(foundUser.getDesc());
	}

	@Test
	void testIoGetUserGroup() throws Exception {
		String code = setupTestUserGroup(false);
		UserGroup foundUserGroup = userGroupIoOperationRepository.findById(code).orElse(null);
		assertThat(foundUserGroup).isNotNull();
		List<UserGroup> userGroups = menuIoOperation.getUserGroup();
		assertThat(userGroups.get(userGroups.size() - 1).getDesc()).isEqualTo(foundUserGroup.getDesc());
	}

	@Test
	void testIoIsUserNamePresent() throws Exception {
		String userName = setupTestUser(false);
		assertThat(menuIoOperation.isUserNamePresent(userName)).isTrue();
	}

	@Test
	void testIoIsGroupNamePresent() throws Exception {
		String code = setupTestUserGroup(false);
		assertThat(menuIoOperation.isGroupNamePresent(code)).isTrue();
	}

	@Test
	void testIoNewUser() throws Exception {
		UserGroup userGroup = testUserGroup.setup(false);
		User user = testUser.setup(userGroup, false);
		userGroupIoOperationRepository.saveAndFlush(userGroup);
		User newUser = menuIoOperation.newUser(user);
		assertThat(menuIoOperation.getUsrInfo(newUser.getUserName())).isEqualTo(user.getDesc());
		checkUserIntoDb(newUser.getUserName());
	}

	@Test
	void testIoUpdateUser() throws Exception {
		String userName = setupTestUser(false);
		User foundUser = userIoOperationRepository.findById(userName).orElse(null);
		assertThat(foundUser).isNotNull();
		foundUser.setDesc("Update");
		assertThat(menuIoOperation.updateUser(foundUser).getUserName()).isEqualTo(foundUser.getUserName());
		User updatedUser = userIoOperationRepository.findById(userName).orElse(null);
		assertThat(updatedUser).isNotNull();
		assertThat(updatedUser.getDesc()).isEqualTo("Update");
	}

	@Test
	void testIoUpdatePassword() throws Exception {
		String userName = setupTestUser(false);
		User foundUser = userIoOperationRepository.findById(userName).orElse(null);
		assertThat(foundUser).isNotNull();
		foundUser.setPasswd("Update");
		assertThat(menuIoOperation.updatePassword(foundUser).getUserName()).isEqualTo(foundUser.getUserName());
		User updatedUser = userIoOperationRepository.findById(userName).orElse(null);
		assertThat(updatedUser).isNotNull();
		assertThat(updatedUser.getPasswd()).isEqualTo("Update");
	}

	@Test
	void testIoDeleteUser() throws Exception {
		String userName = setupTestUser(false);
		User foundUser = userIoOperationRepository.findById(userName).orElse(null);
		assertThat(foundUser).isNotNull();
		menuIoOperation.deleteUser(foundUser);
		List<User> users = menuIoOperation.getUser(userName);
		assertThat(users).isEmpty();
	}

	@Test
	void testIoGetMenu() throws Exception {
		UserGroup userGroup = testUserGroup.setup(false);
		User user = testUser.setup(userGroup, false);
		UserMenuItem menuItem = testUserMenu.setup(false);
		GroupMenu groupMenu = new GroupMenu(userGroup.getCode(), menuItem.getCode());
		userGroupIoOperationRepository.saveAndFlush(userGroup);
		userIoOperationRepository.saveAndFlush(user);
		userMenuItemIoOperationRepository.saveAndFlush(menuItem);
		groupMenuIoOperationRepository.saveAndFlush(groupMenu);
		List<UserMenuItem> menus = menuIoOperation.getMenu(user);
		assertThat(menus.get(menus.size() - 1).getCode()).isEqualTo(menuItem.getCode());
	}

	@Test
	void testIoGetGroupMenu() throws Exception {
		UserGroup userGroup = testUserGroup.setup(false);
		User user = testUser.setup(userGroup, false);
		UserMenuItem menuItem = testUserMenu.setup(false);
		GroupMenu groupMenu = new GroupMenu(userGroup.getCode(), menuItem.getCode());
		userGroupIoOperationRepository.saveAndFlush(userGroup);
		userIoOperationRepository.saveAndFlush(user);
		userMenuItemIoOperationRepository.saveAndFlush(menuItem);
		groupMenuIoOperationRepository.saveAndFlush(groupMenu);
		List<UserMenuItem> menus = menuIoOperation.getGroupMenu(userGroup);
		assertThat(menus.get(menus.size() - 1).getCode()).isEqualTo(menuItem.getCode());
	}

	@Test
	void testIoSetGroupMenu() throws Exception {
		UserGroup userGroup = testUserGroup.setup(false);
		UserMenuItem menuItem = testUserMenu.setup(false);
		List<UserMenuItem> userMenuItems = new ArrayList<>();
		userMenuItems.add(menuItem);
		assertThat(menuIoOperation.setGroupMenu(userGroup, userMenuItems)).isTrue();
	}

	@Test
	void testIoDeleteGroup() throws Exception {
		String code = setupTestUserGroup(false);
		UserGroup foundUserGroup = userGroupIoOperationRepository.findById(code).orElse(null);
		assertThat(foundUserGroup).isNotNull();
		menuIoOperation.deleteGroup(foundUserGroup);
		assertThat(menuIoOperation.findByCode(foundUserGroup.getCode())).isNull();
		foundUserGroup = userGroupIoOperationRepository.findById(code).orElse(null);
		assertThat(foundUserGroup).isNull();
	}

	@Test
	void testIoNewUserGroup() throws Exception {
		UserGroup userGroup = testUserGroup.setup(false);
		UserGroup newUserGroup = menuIoOperation.newUserGroup(userGroup);
		checkUserGroupIntoDb(newUserGroup.getCode());
	}

	@Test
	void testIoNewUserGroupWithPermissions() throws Exception {
		String code = setupTestUserGroupPermissions();
		UserGroup userGroup = userGroupIoOperationRepository.findById(code).orElse(null);
		List<GroupPermission> groupPermissions = groupPermissionManager.findUserGroupPermissions(userGroup.getCode());
		List<Permission> permissions = groupPermissions.stream().map(GroupPermission::getPermission).toList();
		checkUserGroupAndPermissionsIntoDb(userGroup, permissions);
	}

	@Test
	void testIoUpdateUserGroupPermissions() throws Exception {
		String code = setupTestUserGroupPermissions();
		UserGroup foundUserGroup = userGroupIoOperationRepository.findById(code).orElse(null);
		assertThat(foundUserGroup).isNotNull();
		List<GroupPermission> groupPermissions = groupPermissionManager.findUserGroupPermissions(foundUserGroup.getCode());
		ArrayList<Permission> permissions = new ArrayList<>(groupPermissions.stream().map(GroupPermission::getPermission).toList().subList(0, 2));
		Permission permission = permissions.get(0);
		permission.setName("updated.permission");
		permission.setDescription("Updated permission");
		permissions.set(0, permission);
		foundUserGroup.setDesc("Update");
		assertThat(menuIoOperation.updateUserGroup(foundUserGroup, permissions).getCode()).isEqualTo(foundUserGroup.getCode());
		checkUserGroupAndPermissionsIntoDb(foundUserGroup, permissions);
	}

	@Test
	void testIoUpdateUserGroup() throws Exception {
		String code = setupTestUserGroup(false);
		UserGroup foundUserGroup = userGroupIoOperationRepository.findById(code).orElse(null);
		assertThat(foundUserGroup).isNotNull();
		foundUserGroup.setDesc("Update");
		assertThat(menuIoOperation.updateUserGroup(foundUserGroup).getCode()).isEqualTo(foundUserGroup.getCode());
		UserGroup updatedUserGroup = userGroupIoOperationRepository.findById(code).orElse(null);
		assertThat(updatedUserGroup).isNotNull();
		assertThat(updatedUserGroup.getDesc()).isEqualTo("Update");
	}

	@Test
	void testMgrSoftDeleteUser() throws Exception {
		String userName = setupTestUser(false);
		User user = userBrowsingManager.getUserByName(userName);
		assertThat(user).isNotNull();
		assertThat(user.isDeleted()).isFalse();

		user.setDeleted(true);
		User foundUser = userIoOperationRepository.findByUserName(userName);
		assertThat(foundUser).isNotNull();
		assertThat(foundUser.isDeleted()).isTrue();
		foundUser = userBrowsingManager.getUserByName(foundUser.getUserName());
		assertThat(foundUser).isNull();
	}

	@Test
	void testMgrSoftDeleteUserGroup() throws Exception {
		String userName = setupTestUser(false);
		User foundUser = userBrowsingManager.getUserByName(userName);

		assertThat(foundUser).isNotNull();

		UserGroup foundUserGroup = userBrowsingManager.findUserGroupByCode(foundUser.getUserGroupName().getCode());

		assertThat(foundUserGroup).isNotNull();
		UserGroup userGroup = new UserGroup(foundUserGroup.getCode(), foundUserGroup.getDesc());
		assertThatThrownBy(() -> {
			userBrowsingManager.deleteGroup(userGroup);
		}).isInstanceOf(OHServiceException.class);

		foundUser.setDeleted(true);

		foundUserGroup.setDeleted(true);
		foundUserGroup = userGroupIoOperationRepository.findByCodeAndDeleted(foundUserGroup.getCode(), true);
		assertThat(foundUserGroup).isNotNull();
		assertThat(foundUserGroup.isDeleted()).isTrue();
		foundUserGroup = userBrowsingManager.findUserGroupByCode(foundUserGroup.getCode());
		assertThat(foundUserGroup).isNull();
	}

	@Test
	void testMgrGetUser() throws Exception {
		String userName = setupTestUser(false);
		User foundUser = userIoOperationRepository.findById(userName).orElse(null);
		assertThat(foundUser).isNotNull();
		List<User> users = userBrowsingManager.getUser();
		assertThat(users.get(users.size() - 1).getDesc()).isEqualTo(foundUser.getDesc());
	}

	@Test
	void testMgrGetUsersFromGroupId() throws Exception {
		String userName = setupTestUser(false);
		User foundUser = userIoOperationRepository.findById(userName).orElse(null);
		assertThat(foundUser).isNotNull();
		List<User> users = userBrowsingManager.getUser(foundUser.getUserGroupName().getCode());
		assertThat(users.get(users.size() - 1).getDesc()).isEqualTo(foundUser.getDesc());
	}

	@Test
	void testMgrGetUserByName() throws Exception {
		String userName = setupTestUser(false);
		User foundUser = userIoOperationRepository.findById(userName).orElse(null);
		assertThat(foundUser).isNotNull();
		User user = userBrowsingManager.getUserByName(userName);
		assertThat(user.getDesc()).isEqualTo(foundUser.getDesc());
	}

	@Test
	void testMgrGetUserInfo() throws Exception {
		String userName = setupTestUser(false);
		User foundUser = userIoOperationRepository.findById(userName).orElse(null);
		assertThat(foundUser).isNotNull();
		String description = userBrowsingManager.getUsrInfo(userName);
		assertThat(description).isEqualTo(foundUser.getDesc());
	}

	@Test
	void testMgrDeleteGroup() throws Exception {
		String code = setupTestUserGroup(false);
		UserGroup foundUserGroup = userGroupIoOperationRepository.findById(code).orElse(null);
		assertThat(foundUserGroup).isNotNull();
		userBrowsingManager.deleteGroup(foundUserGroup);
		foundUserGroup = userGroupIoOperationRepository.findById(code).orElse(null);
		assertThat(foundUserGroup).isNull();
	}

	@Test
	void testMgrDeleteGroupAdminGroup() throws Exception {
		assertThatThrownBy(() ->
		{
			UserGroup userGroup = testUserGroup.setup(true);
			userGroup.setCode("admin");
			userGroupIoOperationRepository.saveAndFlush(userGroup);
			userBrowsingManager.deleteGroup(userGroup);
		})
			.isInstanceOf(OHDataValidationException.class);
	}

	@Test
	void testMgrDeleteGroupHasUsers() throws Exception {
		assertThatThrownBy(() ->
		{
			String userName = setupTestUser(true);
			User user = userIoOperationRepository.findById(userName).orElse(null);
			assertThat(user).isNotNull();
			userBrowsingManager.deleteGroup(user.getUserGroupName());
		})
			.isInstanceOf(OHDataIntegrityViolationException.class);
	}

	@Test
	void testMgrGetUserGroup() throws Exception {
		String code = setupTestUserGroup(false);
		UserGroup foundUserGroup = userGroupIoOperationRepository.findById(code).orElse(null);
		assertThat(foundUserGroup).isNotNull();
		List<UserGroup> userGroups = userBrowsingManager.getUserGroup();
		assertThat(userGroups.get(userGroups.size() - 1).getDesc()).isEqualTo(foundUserGroup.getDesc());
	}

	@Test
	void testMgrNewUser() throws Exception {
		UserGroup userGroup = testUserGroup.setup(false);
		User user = testUser.setup(userGroup, false);
		userGroupIoOperationRepository.saveAndFlush(userGroup);
		User newUser = userBrowsingManager.newUser(user);
		assertThat(newUser.getUserName()).isEqualTo(user.getUserName());
		checkUserIntoDb(newUser.getUserName());
	}

	@Test
	void testMgrNewUserAlreadyExists() throws Exception {
		assertThatThrownBy(() ->
		{
			UserGroup userGroup = testUserGroup.setup(false);
			User user = testUser.setup(userGroup, false);
			userGroupIoOperationRepository.saveAndFlush(userGroup);
			userIoOperationRepository.saveAndFlush(user);
			userBrowsingManager.newUser(user);
		})
			.isInstanceOf(OHDataIntegrityViolationException.class);
	}

	@Test
	void testMgrUpdateUser() throws Exception {
		String userName = setupTestUser(false);
		User foundUser = userIoOperationRepository.findById(userName).orElse(null);
		assertThat(foundUser).isNotNull();
		foundUser.setDesc("Update");
		assertThat(userBrowsingManager.updateUser(foundUser).getUserName()).isEqualTo(foundUser.getUserName());
		User updatedUser = userIoOperationRepository.findById(userName).orElse(null);
		assertThat(updatedUser).isNotNull();
		assertThat(updatedUser.getDesc()).isEqualTo("Update");
	}

	@Test
	void testMgrUpdatePassword() throws Exception {
		String userName = setupTestUser(false);
		User foundUser = userIoOperationRepository.findById(userName).orElse(null);
		assertThat(foundUser).isNotNull();
		foundUser.setPasswd("Update");
		assertThat(userBrowsingManager.updatePassword(foundUser).getUserName()).isEqualTo(foundUser.getUserName());
		User updatedUser = userIoOperationRepository.findById(userName).orElse(null);
		assertThat(updatedUser).isNotNull();
		assertThat(updatedUser.getPasswd()).isEqualTo("Update");
	}

	@Test
	void testMgrDeleteUser() throws Exception {
		String userName = setupTestUser(false);
		User foundUser = userIoOperationRepository.findById(userName).orElse(null);
		assertThat(foundUser).isNotNull();
		userBrowsingManager.deleteUser(foundUser);
		foundUser = userIoOperationRepository.findById(userName).orElse(null);
		assertThat(foundUser).isNull();
	}

	@Test
	void testMgrDeleteAdminUser() throws Exception {
		assertThatThrownBy(() ->
		{
			String userName = setupTestUser(false);
			User foundUser = userIoOperationRepository.findById(userName).orElse(null);
			assertThat(foundUser).isNotNull();
			foundUser.setUserName("admin");
			userBrowsingManager.deleteUser(foundUser);
		})
			.isInstanceOf(OHDataValidationException.class);
	}

	@Test
	void testMgrGetMenu() throws Exception {
		UserGroup userGroup = testUserGroup.setup(false);
		User user = testUser.setup(userGroup, false);
		UserMenuItem menuItem = testUserMenu.setup(false);
		GroupMenu groupMenu = new GroupMenu(userGroup.getCode(), menuItem.getCode());
		userGroupIoOperationRepository.saveAndFlush(userGroup);
		userIoOperationRepository.saveAndFlush(user);
		userMenuItemIoOperationRepository.saveAndFlush(menuItem);
		groupMenuIoOperationRepository.saveAndFlush(groupMenu);
		List<UserMenuItem> menus = userBrowsingManager.getMenu(user);
		assertThat(menus.get(menus.size() - 1).getCode()).isEqualTo(menuItem.getCode());
	}

	@Test
	void testMgrGetGroupMenu() throws Exception {
		UserGroup userGroup = testUserGroup.setup(false);
		User user = testUser.setup(userGroup, false);
		UserMenuItem menuItem = testUserMenu.setup(false);
		GroupMenu groupMenu = new GroupMenu(userGroup.getCode(), menuItem.getCode());
		userGroupIoOperationRepository.saveAndFlush(userGroup);
		userIoOperationRepository.saveAndFlush(user);
		userMenuItemIoOperationRepository.saveAndFlush(menuItem);
		groupMenuIoOperationRepository.saveAndFlush(groupMenu);
		List<UserMenuItem> menus = userBrowsingManager.getGroupMenu(userGroup);
		assertThat(menus.get(menus.size() - 1).getCode()).isEqualTo(menuItem.getCode());
	}

	@Test
	void testMgrSetGroupMenu() throws Exception {
		UserGroup userGroup = testUserGroup.setup(false);
		UserMenuItem menuItem = testUserMenu.setup(false);
		List<UserMenuItem> userMenuItems = new ArrayList<>();
		userMenuItems.add(menuItem);
		assertThat(userBrowsingManager.setGroupMenu(userGroup, userMenuItems)).isTrue();
	}

	@Test
	void testMgrNewUserGroup() throws Exception {
		UserGroup userGroup = testUserGroup.setup(false);
		UserGroup newUserGroup = userBrowsingManager.newUserGroup(userGroup);
		List<UserGroup> userGroups = userBrowsingManager.getUserGroup();
		assertThat(userGroups).contains(newUserGroup);
		checkUserGroupIntoDb(newUserGroup.getCode());
	}

	@Test
	void testMgrNewUserGroupAlreadyExists() throws Exception {
		assertThatThrownBy(() ->
		{
			String code = setupTestUserGroup(true);
			UserGroup foundUserGroup = userGroupIoOperationRepository.findById(code).orElse(null);
			assertThat(foundUserGroup).isNotNull();
			userBrowsingManager.newUserGroup(foundUserGroup);
		})
			.isInstanceOf(OHDataIntegrityViolationException.class);
	}

	@Test
	void testMgrUpdateUserGroup() throws Exception {
		String code = setupTestUserGroup(false);
		UserGroup foundUserGroup = userGroupIoOperationRepository.findById(code).orElse(null);
		assertThat(foundUserGroup).isNotNull();
		foundUserGroup.setDesc("Update");
		assertThat(userBrowsingManager.updateUserGroup(foundUserGroup).getCode()).isEqualTo(foundUserGroup.getCode());
		UserGroup updatedUserGroup = userGroupIoOperationRepository.findById(code).orElse(null);
		assertThat(updatedUserGroup).isNotNull();
		assertThat(updatedUserGroup.getDesc()).isEqualTo("Update");
	}

	@Test
	void testGroupMenuSettersGetters() throws Exception {
		GroupMenu groupMenu = testGroupMenu.setup(true);

		Integer code = groupMenu.getCode();
		groupMenu.setCode(-1);
		assertThat(groupMenu.getCode()).isEqualTo(-1);
		groupMenu.setCode(code);

		int active = groupMenu.getActive();
		groupMenu.setActive(-1);
		assertThat(groupMenu.getActive()).isEqualTo(-1);
		groupMenu.setActive(active);
	}

	@Test
	void testGroupMenuEquals() throws Exception {
		GroupMenu groupMenu = testGroupMenu.setup(true);
		groupMenu.setCode(1);

		assertThat(groupMenu)
			.isNotNull()
			.isNotEqualTo("aString");

		GroupMenu groupMenu1 = testGroupMenu.setup(false);
		groupMenu1.setCode(-1);
		assertThat(groupMenu).isNotEqualTo(groupMenu1);

		groupMenu1.setCode(groupMenu.getCode());
		groupMenu1.setUserGroup("someOtherGroup");
		assertThat(groupMenu).isNotEqualTo(groupMenu1);

		groupMenu1.setUserGroup(groupMenu.getUserGroup());
		groupMenu1.setMenuItem("someOtherMenuItem");
		assertThat(groupMenu).isNotEqualTo(groupMenu1);

		groupMenu1.setMenuItem(groupMenu.getMenuItem());
		groupMenu1.setActive(-1);
		assertThat(groupMenu).isNotEqualTo(groupMenu1);

		groupMenu1.setActive(groupMenu.getActive());
		assertThat(groupMenu).isEqualTo(groupMenu1);
	}

	@Test
	void testUserToString() throws Exception {
		UserGroup userGroup = testUserGroup.setup(true);
		User user = testUser.setup(userGroup, true);
		assertThat(user).hasToString(user.getUserName());
	}

	@Test
	void testUserEquals() throws Exception {
		UserGroup userGroup = testUserGroup.setup(true);
		User user = testUser.setup(userGroup, true);

		assertThat(user)
			.isNotNull()
			.isNotEqualTo("someString");

		User user1 = testUser.setup(userGroup, false);
		user1.setUserName("someOtherName");
		assertThat(user).isNotEqualTo(user1);

		user1.setUserName(user.getUserName());
		user1.setDesc("someOtherDescription");
		assertThat(user).isNotEqualTo(user1);

		user1.setDesc(user.getDesc().toLowerCase());
		assertThat(user).isEqualTo(user1);
	}

	@Test
	void testUserHashCode() throws Exception {
		UserGroup userGroup = testUserGroup.setup(true);
		User user = testUser.setup(userGroup, true);
		// compute value
		int hashCode = user.hashCode();
		// reuse value
		assertThat(user.hashCode()).isEqualTo(hashCode);
	}

	@Test
	void testUserGroupToString() throws Exception {
		UserGroup userGroup = testUserGroup.setup(true);
		userGroup.setCode("someCode");
		assertThat(userGroup).hasToString("someCode");
	}

	@Test
	void testUserGroupEquals() throws Exception {
		UserGroup userGroup = testUserGroup.setup(true);

		assertThat(userGroup)
			.isNotNull()
			.isNotEqualTo("someString");

		UserGroup userGroup1 = testUserGroup.setup(false);

		userGroup.setCode("code1");
		userGroup1.setCode("code2");
		assertThat(userGroup).isNotEqualTo(userGroup1);

		userGroup1.setCode(userGroup.getCode());
		userGroup1.setDesc("someOtherDescription");
		assertThat(userGroup).isNotEqualTo(userGroup1);

		userGroup1.setDesc(userGroup.getDesc().toLowerCase());
		assertThat(userGroup).isEqualTo(userGroup1);
	}

	@Test
	void testUserGroupHashCode() throws Exception {
		UserGroup userGroup = testUserGroup.setup(true);
		// compute value
		int hashCode = userGroup.hashCode();
		// reuse value
		assertThat(userGroup.hashCode()).isEqualTo(hashCode);
	}

	@Test
	void testUserMenuItemEquals() throws Exception {
		UserMenuItem userMenuItem = testUserMenu.setup(true);

		assertThat(userMenuItem)
			.isNotNull()
			.isNotEqualTo("someString");

		UserMenuItem userMenuItem1 = testUserMenu.setup(false);
		userMenuItem.setCode("code1");
		userMenuItem1.setCode("code2");
		assertThat(userMenuItem).isNotEqualTo(userMenuItem1);

		userMenuItem1.setCode(userMenuItem.getCode());
		userMenuItem1.setButtonLabel("someOtherButtonLabel");
		assertThat(userMenuItem).isNotEqualTo(userMenuItem1);

		userMenuItem1.setButtonLabel(userMenuItem.getButtonLabel());
		userMenuItem1.setAltLabel("someOtherLabel");
		assertThat(userMenuItem).isNotEqualTo(userMenuItem1);

		userMenuItem1.setAltLabel(userMenuItem.getAltLabel());
		userMenuItem1.setTooltip("someOtherToolTip");
		assertThat(userMenuItem).isNotEqualTo(userMenuItem1);

		userMenuItem1.setTooltip(userMenuItem.getTooltip());
		userMenuItem1.setShortcut('?');
		assertThat(userMenuItem).isNotEqualTo(userMenuItem1);

		userMenuItem1.setShortcut(userMenuItem.getShortcut());
		userMenuItem1.setMySubmenu("someOtherSubMenu");
		assertThat(userMenuItem).isNotEqualTo(userMenuItem1);

		userMenuItem1.setMySubmenu(userMenuItem.getMySubmenu());
		userMenuItem1.setMyClass("someOtherClass");
		assertThat(userMenuItem).isNotEqualTo(userMenuItem1);

		userMenuItem1.setMyClass(userMenuItem.getMyClass());
		userMenuItem1.setASubMenu(!userMenuItem.isASubMenu());
		assertThat(userMenuItem).isNotEqualTo(userMenuItem1);

		userMenuItem1.setASubMenu(userMenuItem.isASubMenu());
		userMenuItem1.setPosition(-1);
		assertThat(userMenuItem).isNotEqualTo(userMenuItem1);

		userMenuItem1.setPosition(userMenuItem.getPosition());
		userMenuItem1.setActive(!userMenuItem.isActive());
		assertThat(userMenuItem).isNotEqualTo(userMenuItem1);

		userMenuItem1.setActive(userMenuItem.isActive());
		assertThat(userMenuItem).isEqualTo(userMenuItem1);
	}

	@Test
	void testUserMenuItemToString() throws Exception {
		UserMenuItem userMenuItem = testUserMenu.setup(true);
		assertThat(userMenuItem).hasToString(userMenuItem.getButtonLabel());
	}

	@Test
	void testUserMenuItemHashCode() throws Exception {
		UserMenuItem userMenuItem = testUserMenu.setup(false);
		userMenuItem.setCode("someCode");
		// compute value
		int hashCode = userMenuItem.hashCode();
		// reuse value
		assertThat(userMenuItem.hashCode()).isEqualTo(hashCode);
	}

	private String setupTestUserGroup(boolean usingSet) throws OHException {
		UserGroup userGroup = testUserGroup.setup(usingSet);
		userGroupIoOperationRepository.saveAndFlush(userGroup);
		return userGroup.getCode();
	}

	private String setupTestUserGroupPermissions() throws OHServiceException {
		UserGroup userGroup = new UserGroup();
		userGroup.setCode("contrib");
		userGroup.setDesc("contributors group");
		List<Permission> permissions = TestPermission.generatePermissions(4);
		List<Permission> savedPermissions = permissionIoOperationRepository.saveAllAndFlush(permissions);
		UserGroup newUserGroup = menuIoOperation.newUserGroup(userGroup, savedPermissions);
		return userGroup.getCode();
	}

	private void checkUserGroupAndPermissionsIntoDb(UserGroup userGroup, List<Permission> permissions) {
		UserGroup foundUserGroup = userGroupIoOperationRepository.findById(userGroup.getCode()).orElse(null);
		assertThat(foundUserGroup).isNotNull();
		assertThat(foundUserGroup.getCode()).isEqualTo(userGroup.getCode());
		assertThat(foundUserGroup.getDesc()).isEqualTo(userGroup.getDesc());

		List<GroupPermission> groupPermissions = groupPermissionManager.findUserGroupPermissions(userGroup.getCode());
		List<Permission> dbGroupPermissions = groupPermissions.stream().map(GroupPermission::getPermission).toList();

		assertThat(permissions.size()).isEqualTo(dbGroupPermissions.size());
		assertThat(permissions.get(0)).isEqualTo(dbGroupPermissions.get(0));
	}

	private void checkUserGroupIntoDb(String code) throws OHException {
		UserGroup foundUserGroup = userGroupIoOperationRepository.findById(code).orElse(null);
		assertThat(foundUserGroup).isNotNull();
		testUserGroup.check(foundUserGroup);
	}

	private String setupTestUser(boolean usingSet) throws OHException {
		UserGroup userGroup = testUserGroup.setup(usingSet);
		User user = testUser.setup(userGroup, usingSet);
		userGroupIoOperationRepository.saveAndFlush(userGroup);
		userIoOperationRepository.saveAndFlush(user);
		return user.getUserName();
	}

	private void checkUserIntoDb(String code) throws OHException {
		User foundUser = userIoOperationRepository.findById(code).orElse(null);
		assertThat(foundUser).isNotNull();
		testUser.check(foundUser);
	}

	private String setupTestUserMenu(boolean usingSet) throws OHException {
		UserMenuItem userMenu = testUserMenu.setup(usingSet);
		userMenuItemIoOperationRepository.saveAndFlush(userMenu);
		return userMenu.getCode();
	}

	private void checkUserMenuIntoDb(String code) throws OHException {
		UserMenuItem foundUserMenu = userMenuItemIoOperationRepository.findById(code).orElse(null);
		assertThat(foundUserMenu).isNotNull();
		testUserMenu.check(foundUserMenu);
	}

	private Integer setupTestGroupMenu(boolean usingSet) throws OHException {
		GroupMenu groupMenu = testGroupMenu.setup(usingSet);
		groupMenuIoOperationRepository.saveAndFlush(groupMenu);
		return groupMenu.getCode();
	}

	private void checkGroupMenuIntoDb(Integer code) throws OHException {
		GroupMenu foundGroupMenu = groupMenuIoOperationRepository.findById(code).orElse(null);
		assertThat(foundGroupMenu).isNotNull();
		testGroupMenu.check(foundGroupMenu);
	}
}
