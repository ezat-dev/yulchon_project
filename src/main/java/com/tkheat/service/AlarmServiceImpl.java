package com.tkheat.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tkheat.dao.AlarmDao;
import com.tkheat.domain.Alarm;
import com.tkheat.domain.Users;

@Service
public class AlarmServiceImpl implements AlarmService{
	@Autowired
	private AlarmDao alarmDao;
	
	@Override
	public List<Alarm> getAlarmList(Alarm alarm) {
		return alarmDao.getAlarmList(alarm);
	}
	@Override
	public boolean updateAlarmSend(Alarm alarm) {
		return alarmDao.updateAlarmSend(alarm);
	}
	@Override
	public List<Alarm> allAlarmList(Alarm alarm) {
		return alarmDao.allAlarmList(alarm);
	}
	@Override
	public boolean updateAlarmGroup(Alarm alarm) {
		return alarmDao.updateAlarmGroup(alarm);
	}
	@Override
	public boolean updateAllAlarmGroup(Alarm alarm) {
		return alarmDao.updateAllAlarmGroup(alarm);
	}
	@Override
	public List<Alarm> getAlarmGroupName(Alarm alarm) {
		return alarmDao.getAlarmGroupName(alarm);
	}
	@Override
	public List<Alarm> selectAlarmList(Alarm alarm) {
		return alarmDao.selectAlarmList(alarm);
	}
	@Override
	public boolean insertAlarmGroup(Alarm alarm) {
		return alarmDao.insertAlarmGroup(alarm);
	}
	@Override
	public boolean deleteAlarmGroup(Alarm alarm) {
		return alarmDao.deleteAlarmGroup(alarm);
	}
	@Override
	public boolean insertAllAlarmGroup(Alarm alarm) {
		return alarmDao.insertAllAlarmGroup(alarm);
	}
	@Override
	public boolean deleteAllAlarmGroup(Alarm alarm) {
		return alarmDao.deleteAllAlarmGroup(alarm);
	}
	@Override
	public boolean updateAlarmGroupName(Alarm alarm) {
		return alarmDao.updateAlarmGroupName(alarm);
	}
	@Override
	public List<Alarm> selectCreateAlarmList(Alarm alarm) {
		return alarmDao.selectCreateAlarmList(alarm);
	}
}
