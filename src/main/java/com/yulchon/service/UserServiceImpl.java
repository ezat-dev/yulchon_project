package com.yulchon.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yulchon.dao.UserDao;
import com.yulchon.domain.UserMenu;
import com.yulchon.domain.Users;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserDao userDao;
	
	@Override
	public Users getLoginUser(Users users) {
		return userDao.getLoginUser(users);
	}
	
	@Override
	public Users userMenuSelectCount(Users users) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Users> usersMenuOkSelect(Users users) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void userMenuDelete(Users users) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void userMenuClick(Users users) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Users> menuList(Users users) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void userLoginMenuSave(UserMenu userMenu) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<UserMenu> userLoginMenuList(UserMenu userMenu) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void userLoginMenuRemove(UserMenu userMenu) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void userLoginHisSave(Users users) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void userInsertInsert(Users users) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Users userDuplicateCheck(Users users) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Users> selectUserList(Users users) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean insertWorkTime(Users users) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateMessage(Users users) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteWorkTime(Users users) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deviceTokenUpdate(Users users) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Users> selectUserModalList(Users users) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Users> getAlarmUser1(Users users) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Users> getAlarmUser2(Users users) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean insertGroup(Users users) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteGroup(Users users) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateGroupTime(Users users) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Users> getGroupUser(Users users) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean insertGroupSchedule(Users users) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Users> getGroupScheduleList(Users users) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateRecieveAlarm(Users users) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Users> getGroupList(Users users) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Users> sendAlarmList(Users users) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateAlarmSend(Users users) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteSchedule(Users users) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateUser(Users users) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteUser(Users users) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateGroupSchedule(Users users) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Users> getGroupName(Users users) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateGroupName(Users users) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteRecieveAlarm(Users users) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Users> getGroupRecieveAlarm(Users users) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean insertCompany(Users users) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean insertCompanyAdmin(Users users) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String selectNewCompanyCode(Users users) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Users selectNewCompany(Users users) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean insertNewGroup(Users users) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean insertNewAlarmGroup(Users users) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Users> getCompanyNames(Users users) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean insertAlarmData(List<Users> datas) {
		// TODO Auto-generated method stub
		return false;
	}

}
