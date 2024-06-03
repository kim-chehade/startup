package com.amigoscode.staff;

import com.amigoscode.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffService {
    private final StaffDao staffDao;

    public StaffService(StaffDao staffDao) {
        this.staffDao = staffDao;
    }

    public List<Staff> getAllStaff() {
        return staffDao.selectAllStaff();
    }

    public Staff getStaff(Integer id) {
        return staffDao.selectStaffById(id)
                .orElseThrow(()-> new ResourceNotFoundException("staff with id [%s] not found".formatted(id)));
    }
}
