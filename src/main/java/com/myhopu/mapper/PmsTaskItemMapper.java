package com.myhopu.mapper;

import java.util.List;
import com.myhopu.entity.PmsTaskItem;

public interface PmsTaskItemMapper {

	public void add(PmsTaskItem bean);

	public void del(long id);

	public void upd(PmsTaskItem bean);

	public PmsTaskItem findOne(long id);

	public List findAll(long id);
	
	public List findAllMy(long userid);

}
