package com.myhopu.mapper;

import java.util.List;
import java.util.Map;

public interface PmsChatMapper {

	List findAllDeptUser();

	void sendMsg(Map map);

	List rcvMsg(long userid);

	List getState(long userid);

	void changeState(long userid);

	List checkIsUser(String name);

}
