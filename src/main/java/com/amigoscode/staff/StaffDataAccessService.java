package com.amigoscode.staff;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class StaffDataAccessService implements StaffDao{

    private static List<Staff> staff;

    {
        staff = new ArrayList<>();

              Staff jad = new Staff(
                      1,
                      "Jad",
                      "jad@gmail.com",
                      26
              );
              staff.add(jad);
              Staff hasan = new Staff(
                      2,
                      "Hasan",
                      "hasan@gmail.com",
                      23
              );
              staff.add(hasan);

    }

    @Override
    public List<Staff> selectAllStaff() {
        return staff;
    }

    @Override
    public Optional<Staff> selectStaffById(Integer id) {
        return staff.stream()
                .filter(s->s.getId().equals(id))
                .findFirst();
    }
}
