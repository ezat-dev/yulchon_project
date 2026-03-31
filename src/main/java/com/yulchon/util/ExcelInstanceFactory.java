package com.yulchon.util;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class ExcelInstanceFactory extends BasePooledObjectFactory<ActiveXComponent> {

    @Override
    public ActiveXComponent create() throws Exception {
        // 새로운 엑셀 인스턴스 생성
        ActiveXComponent excel = new ActiveXComponent("Excel.Application");
        excel.setProperty("Visible", false);
        excel.setProperty("DisplayAlerts", false);
        excel.setProperty("ScreenUpdating", false);
        return excel;
    }

    @Override
    public PooledObject<ActiveXComponent> wrap(ActiveXComponent excel) {
        return new DefaultPooledObject<>(excel);
    }

    @Override
    public void destroyObject(PooledObject<ActiveXComponent> p) throws Exception {
        // 인스턴스 파괴 시 안전하게 종료
        try {
        	ActiveXComponent excel = p.getObject();
            Dispatch workbooks = excel.getProperty("Workbooks").toDispatch();
            // [핵심] 모든 워크북을 "저장하지 않고(false)" 강제 종료
            Dispatch.call(workbooks, "Close", new Variant(false)); 
            excel.invoke("Quit");
        } catch (Exception ignore) {}
    }
}
