package com.yulchon.util;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import com.jacob.activeX.ActiveXComponent;

public class ExcelManager {
	private static ExcelManager instance;
    private GenericObjectPool<ActiveXComponent> pool;

    private ExcelManager() {
        ExcelInstanceFactory factory = new ExcelInstanceFactory();
        GenericObjectPoolConfig<ActiveXComponent> config = new GenericObjectPoolConfig<>();
        config.setMaxTotal(3); // 엑셀 인스턴스 최대 3개
        config.setBlockWhenExhausted(true); // 3개 다 쓰고 있으면 다음 사람 대기
        config.setMaxWaitMillis(10000); // 최대 10초 대기 후 에러 반환
        
        this.pool = new GenericObjectPool<>(factory, config);
    }

    public static synchronized ExcelManager getInstance() {
        if (instance == null) instance = new ExcelManager();
        return instance;
    }

    // 빌려오기
    public ActiveXComponent borrowExcel() throws Exception {
        return pool.borrowObject();
    }

    // 반납하기
    public void returnExcel(ActiveXComponent excel) {
        if (excel != null) pool.returnObject(excel);
    }
}
