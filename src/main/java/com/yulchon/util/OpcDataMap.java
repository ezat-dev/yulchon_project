package com.yulchon.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResult;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.yulchon.controller.MainController;

public class OpcDataMap {
	
	
	public Map<String, Object> getOpcDataListMap(String groupName) throws InterruptedException, ExecutionException{
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		ActionMap actionMap = new ActionMap();
		NodeId rootNodeId = new NodeId(2, groupName);
        
        BrowseDescription browseDescription = new BrowseDescription(
        		rootNodeId, BrowseDirection.Forward, null, true, Unsigned.uint(0xFFFF), Unsigned.uint(0xFFFF));

        BrowseResult browseResult = MainController.client.browse(browseDescription).get();
            
            // 하위 노드 출력
        int tagCount = browseResult.getReferences().length;
        
        List<NodeId> nodeIds = new ArrayList<NodeId>();
        List<String> referenceList = new ArrayList<String>();
        
        for(int i=0; i<tagCount; i++) {
        	ReferenceDescription reference = browseResult.getReferences()[i];
        	String getName = reference.getBrowseName().getName().toString();
        	
        	if(!"BaseObjectType".equals(getName)) {
        		referenceList.add(getName);
        		NodeId nodeId = new NodeId(2, reference.getNodeId().getIdentifier().toString());
        		
        		
        		nodeIds.add(nodeId);
        	}
        }
        
        
        // 여러 노드 읽기
        List<CompletableFuture<DataValue>> futures = nodeIds.stream()
            .map(nodeId -> MainController.client.readValue(0, TimestampsToReturn.Both, nodeId))
            .collect(Collectors.toList());    	

        // CompletableFuture를 사용하여 모든 값을 한꺼번에 대기
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allFutures.get();
        

        
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();

        for (int i = 0; i < nodeIds.size(); i++) {
	        Map<String, Object> multiValues = new HashMap<String, Object>();	        	
            DataValue value = futures.get(i).get();
            
            String tagName = referenceList.get(i);
            String tagType = "";
            
            if("true".equals(value.getValue().getValue().toString())
            		|| "false".equals(value.getValue().getValue().toString())) {
            	tagType = "digital";
//            	System.out.println("tagName : "+tagName+"// Boolean : "+value.getValue().getValue().toString());
            }else {
//            	System.out.println("tagName : "+tagName+"// Integer : "+value.getValue().getValue().toString());
            	tagType = "analog";
            }
            
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("tagName",tagName);
            paramMap.put("tagType",tagType);
            
            
            String action = actionMap.getReturnAction(paramMap);
            
            
            //현재 태그의 값, 구분
            Map<String ,Object> dataMap = new HashMap<String, Object>();
            dataMap.put("value", value.getValue().getValue());
            dataMap.put("action", action);
            
            
            multiValues.put(tagName, dataMap);
            dataList.add(multiValues);
        }
        
        returnMap.put("multiValues", dataList);
		
        return returnMap;
	}
	
	
	public void setOpcData(String opcGroup, String opcData) throws InterruptedException, ExecutionException{
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		
		
		UShort namespaceIndex = Unsigned.ushort(2);
		
		
		NodeId nodeId = new NodeId(namespaceIndex, opcGroup);
		
		DataValue dataValue = new DataValue(new Variant(opcData));
		
		CompletableFuture<StatusCode> writeFuture = MainController.client.writeValue(nodeId, dataValue);
		
        StatusCode statusCode = writeFuture.get();

        // 값이 성공적으로 쓰여졌는지 확인
        if (statusCode.isGood()) {
//            System.out.println("Value written successfully");
        	rtnMap.put("status", "OK");
        } else {
//            System.out.println("Failed to write value: " + statusCode);
        	rtnMap.put("status", "NG");
        }
	}
	
	public void setOpcData(String opcGroup, short opcData) throws InterruptedException, ExecutionException{
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		
		
		UShort namespaceIndex = Unsigned.ushort(2);
		
		
		NodeId nodeId = new NodeId(namespaceIndex, opcGroup);
		
		DataValue dataValue = new DataValue(new Variant(opcData));
		
		CompletableFuture<StatusCode> writeFuture = MainController.client.writeValue(nodeId, dataValue);
		
		StatusCode statusCode = writeFuture.get();
		
		// 값이 성공적으로 쓰여졌는지 확인
		if (statusCode.isGood()) {
//            System.out.println("Value written successfully");
			rtnMap.put("status", "OK");
		} else {
//            System.out.println("Failed to write value: " + statusCode);
			rtnMap.put("status", "NG");
		}
	}
	
	public void setOpcData(String opcGroup, boolean opcData) throws InterruptedException, ExecutionException{
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		
		
		UShort namespaceIndex = Unsigned.ushort(2);
		
		
		NodeId nodeId = new NodeId(namespaceIndex, opcGroup);
		
		DataValue dataValue = new DataValue(new Variant(opcData));
		
		CompletableFuture<StatusCode> writeFuture = MainController.client.writeValue(nodeId, dataValue);
		
		StatusCode statusCode = writeFuture.get();
		
		// 값이 성공적으로 쓰여졌는지 확인
		if (statusCode.isGood()) {
//            System.out.println("Value written successfully");
			rtnMap.put("status", "OK");
		} else {
//            System.out.println("Failed to write value: " + statusCode);
			rtnMap.put("status", "NG");
		}
	}
	
	
	//OPC태그값 1개 조회
	public Map<String, Object> getOpcData(String opcTag) throws InterruptedException, ExecutionException{
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		
		UShort namespaceIndex = Unsigned.ushort(2);
		
		NodeId nodeId = new NodeId(namespaceIndex, opcTag);
		
        // Read 요청 보내기
        CompletableFuture<DataValue> future = MainController.client.readValue(0, TimestampsToReturn.Both, nodeId);
        
        DataValue singleValue = future.get();       
        
        rtnMap.put("value", singleValue.getValue().getValue());
        
		return rtnMap;
	}
	
	
	//OPC데이터 조회(태그동작 구분 제외)
	public Map<String, JSONArray> getOpcDataListMap2(String groupName) throws InterruptedException, ExecutionException{
		Map<String, JSONArray> returnMap = new HashMap<String, JSONArray>();
		
		NodeId rootNodeId = new NodeId(2, groupName);
        
        BrowseDescription browseDescription = new BrowseDescription(
        		rootNodeId, BrowseDirection.Forward, null, true, Unsigned.uint(0xFFFF), Unsigned.uint(0xFFFF));

        BrowseResult browseResult = MainController.client.browse(browseDescription).get();
            
            // 하위 노드 출력
        int tagCount = browseResult.getReferences().length;
        
        List<NodeId> nodeIds = new ArrayList<NodeId>();
        List<String> referenceList = new ArrayList<String>();
        
        for(int i=0; i<tagCount; i++) {
        	ReferenceDescription reference = browseResult.getReferences()[i];
        	String getName = reference.getBrowseName().getName().toString();
        	
        	if(!"BaseObjectType".equals(getName)) {
        		referenceList.add(getName);
        		NodeId nodeId = new NodeId(2, reference.getNodeId().getIdentifier().toString());
        		
        		
        		nodeIds.add(nodeId);
        	}
        }
        
        
        // 여러 노드 읽기
        List<CompletableFuture<DataValue>> futures = nodeIds.stream()
            .map(nodeId -> MainController.client.readValue(0, TimestampsToReturn.Both, nodeId))
            .collect(Collectors.toList());    	

        // CompletableFuture를 사용하여 모든 값을 한꺼번에 대기
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allFutures.get();
        

        
        JSONArray dataList = new JSONArray();

        for (int i = 0; i < nodeIds.size(); i++) {
            DataValue value = futures.get(i).get();
            
            String tagName = referenceList.get(i);
            
            //현재 태그의 값, 구분
            JSONObject dataMap = new JSONObject();
            dataMap.put("tagName", tagName);
            dataMap.put("value", value.getValue().getValue());


            dataList.add(dataMap);
        }
        
        returnMap.put("dataList", dataList);
		
        return returnMap;
	}
	
	
	//OPC태그값 1개 조회(OPC 연결상태 확인용)
	public Map<String, Object> getOpcDataConnCheck(OpcUaClient paramClient, String opcTag) throws InterruptedException, ExecutionException{
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		
		UShort namespaceIndex = Unsigned.ushort(2);
		
		NodeId nodeId = new NodeId(namespaceIndex, opcTag);
		
        // Read 요청 보내기
        CompletableFuture<DataValue> future = paramClient.readValue(0, TimestampsToReturn.Both, nodeId);
        
        DataValue singleValue = future.get();       
        
        rtnMap.put("value", singleValue.getValue().getValue());
        
		return rtnMap;
	}	
	
}
