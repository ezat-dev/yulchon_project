package com.tkheat.dao;

import java.util.List;
import javax.annotation.Resource;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.tkheat.domain.Alarm;
import com.tkheat.domain.Users;

@Repository
public class AlarmDaoImpl implements AlarmDao{
	@Resource(name="session")
	private SqlSession sqlSession;

	@Override
	public List<Alarm> getAlarmList(Alarm alarm) {
		return sqlSession.selectList("alarm.getAlarmList",alarm);
	}

	@Override
	public boolean updateAlarmSend(Alarm alarm) {
		int result =  sqlSession.update("alarm.updateAlarmSend", alarm);
		if(result <= 0) {
			return false;
		}
		return true;
	}

	@Override
	public List<Alarm> allAlarmList(Alarm alarm) {
		return sqlSession.selectList("alarm.allAlarmList",alarm);
	}

	@Override
	public boolean updateAlarmGroup(Alarm alarm) {
		int result = sqlSession.update("alarm.updateAlarmGroup", alarm);
		if(result <= 0) {
			return false;
		}
		return true;
	}
	@Override
	public boolean updateAllAlarmGroup(Alarm alarm) {
		int result = sqlSession.update("alarm.updateAllAlarmGroup", alarm);
		if(result <= 0) {
			return false;
		}
		return true;
	}
	@Override
	public List<Alarm> getAlarmGroupName(Alarm alarm) {
		return sqlSession.selectList("alarm.getAlarmGroupName",alarm);
	}
	@Override
	public List<Alarm> selectAlarmList(Alarm alarm) {
		return sqlSession.selectList("alarm.selectAlarmList",alarm);
	}
	@Override
	public boolean insertAlarmGroup(Alarm alarm) {
		int result =  sqlSession.insert("alarm.insertAlarmGroup", alarm);
		if(result <= 0) {
			return false;
		}
		return true;
	}
	@Override
	public boolean deleteAlarmGroup(Alarm alarm) {
		int result =  sqlSession.delete("alarm.deleteAlarmGroup", alarm);
		if(result <= 0) {
			return false;
		}
		return true;
	}
	@Override
	public boolean insertAllAlarmGroup(Alarm alarm) {
		int result = sqlSession.insert("alarm.insertAllAlarmGroup", alarm);
		if(result <= 0) {
			return false;
		}
		return true;
	}
	@Override
	public boolean deleteAllAlarmGroup(Alarm alarm) {
		int result = sqlSession.delete("alarm.deleteAllAlarmGroup", alarm);
		if(result <= 0) {
			return false;
		}
		return true;
	}
	@Override
	public boolean updateAlarmGroupName(Alarm alarm) {
		int result = sqlSession.update("alarm.updateAlarmGroupName", alarm);
		if(result <= 0) {
			return false;
		}
		return true;
	}
	public List<Alarm> selectCreateAlarmList(Alarm alarm) {
		return sqlSession.selectList("alarm.selectCreateAlarmList",alarm);
	}
}
