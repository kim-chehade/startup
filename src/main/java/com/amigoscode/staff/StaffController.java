package com.amigoscode.staff;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StaffController {
    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @GetMapping("api/v1/staff")
    public List<Staff> getStaff(){
        return staffService.getAllStaff();
    }
    @GetMapping("api/v1/staff/{staffId}")
    public Staff getStaff(@PathVariable("staffId") Integer staffId){
        return staffService.getStaff(staffId);
    }


}
