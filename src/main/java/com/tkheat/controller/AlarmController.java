package com.tkheat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tkheat.domain.Alarm;
import com.tkheat.domain.Users;
import com.tkheat.service.AlarmService;

@Controller
public class AlarmController {
	@Autowired
	private AlarmService alarmService;

	//알람 조회
	@RequestMapping(value="/alarm/allAlarmList", method=RequestMethod.POST)
	@ResponseBody
	public List<Alarm> allAlarmList(Alarm alarm) {
		return alarmService.allAlarmList(alarm); 
	}
	
	//알람 그룹 업데이트
	@RequestMapping(value="/alarm/updateAlarmGroup", method=RequestMethod.POST)
	@ResponseBody
	public boolean updateAlarmGroup(@RequestBody Alarm alarm) {
		return alarmService.updateAlarmGroup(alarm); 
	}
	
	//알람 그룹 일괄 업데이트
	@RequestMapping(value="/alarm/updateAllAlarmGroup", method=RequestMethod.POST)
	@ResponseBody
	public boolean updateAllAlarmGroup(@RequestBody Alarm alarm) {
		return alarmService.updateAllAlarmGroup(alarm); 
	}
	//알람 그룹 이름 조회
	@RequestMapping(value="/alarm/getAlarmGroupName", method=RequestMethod.POST)
	@ResponseBody
	public List<Alarm> getAlarmGroupName(Alarm alarm) {
		return alarmService.getAlarmGroupName(alarm);
	}
	//알람 조회(속한 그룹까지)
	@RequestMapping(value="/alarm/selectAlarmList", method=RequestMethod.POST)
	@ResponseBody
	public List<Alarm> selectAlarmList(Alarm alarm) {
		return alarmService.selectAlarmList(alarm);
	}
	//그룹에 알람 추가
	@RequestMapping(value="/alarm/insertAlarmGroup", method=RequestMethod.POST)
	@ResponseBody
	public boolean insertAlarmGroup(@RequestBody Alarm alarm) {
		return alarmService.insertAlarmGroup(alarm);

	}
	//그룹에 알람 삭제
	@RequestMapping(value="/alarm/deleteAlarmGroup", method=RequestMethod.POST)
	@ResponseBody
	public boolean deleteAlarmGroup(@RequestBody Alarm alarm) {
		return alarmService.deleteAlarmGroup(alarm);

	}
	//알람 일괄 선택
	@RequestMapping(value="/alarm/insertAllAlarmGroup", method=RequestMethod.POST)
	@ResponseBody
	public boolean insertAllAlarmGroup(@RequestBody Alarm alarm) {
		return alarmService.insertAllAlarmGroup(alarm);

	}
	//알람 일괄 선택
	@RequestMapping(value="/alarm/deleteAllAlarmGroup", method=RequestMethod.POST)
	@ResponseBody
	public boolean deleteAllAlarmGroup(@RequestBody Alarm alarm) {
		return alarmService.deleteAllAlarmGroup(alarm);

	}
	//알람 그룹 이릅 업데이트
	@RequestMapping(value="/alarm/updateAlarmGroupName", method=RequestMethod.POST)
	@ResponseBody
	public boolean updateAlarmGroupName(@RequestBody Alarm alarm) {
		return alarmService.updateAlarmGroupName(alarm);

	}
	//발생 알람 조회
	@RequestMapping(value="/alarm/android/selectCreateAlarmList", method=RequestMethod.POST)
	@ResponseBody
	public List<Alarm> selectCreateAlarmList(@RequestBody Alarm alarm) {
		return alarmService.selectCreateAlarmList(alarm);

	}
}
