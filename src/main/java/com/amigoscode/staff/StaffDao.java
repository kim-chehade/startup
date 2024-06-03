package com.amigoscode.staff;

import java.util.List;
import java.util.Optional;

public interface StaffDao {
    List<Staff> selectAllStaff();

    Optional<Staff> selectStaffById(Integer id);
}
