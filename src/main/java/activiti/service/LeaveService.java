package activiti.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import activiti.entity.Leave;
import activiti.repository.hibernate.LeaveDao;

@Service
@Transactional(readOnly=true)
public class LeaveService {

	@Autowired
	private LeaveDao thisDao;
	
	@Transactional(readOnly=false)
	public void save(Leave entity){
		thisDao.save(entity);
	}
}
