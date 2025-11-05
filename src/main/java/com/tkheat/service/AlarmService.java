package com.tkheat.service;

import java.util.List;

import com.tkheat.domain.Alarm;
import com.tkheat.domain.Users;

public interface AlarmService {
	List<Alarm> getAlarmList(Alarm alarm);
	boolean updateAlarmSend(Alarm alarm);
	List<Alarm> allAlarmList(Alarm alarm);
	boolean updateAlarmGroup(Alarm alarm);
	boolean updateAllAlarmGroup(Alarm alarm);
	List<Alarm> getAlarmGroupName(Alarm alarm);
	List<Alarm> selectAlarmList(Alarm alarm);
	boolean insertAlarmGroup(Alarm alarm);
	boolean deleteAlarmGroup(Alarm alarm);
	boolean insertAllAlarmGroup(Alarm alarm);
	boolean deleteAllAlarmGroup(Alarm alarm);
	boolean updateAlarmGroupName(Alarm alarm);
	List<Alarm> selectCreateAlarmList(Alarm alarm);
}
