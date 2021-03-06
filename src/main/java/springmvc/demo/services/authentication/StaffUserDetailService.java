package springmvc.demo.services.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springmvc.demo.repositories.staffRepository.StaffsRepository;
import springmvc.demo.models.Staff;

import java.util.LinkedList;
import java.util.List;

@Service
public class StaffUserDetailService implements UserDetailsService {

    @Autowired
    private StaffsRepository staffsRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        System.out.println("staff running");

        Staff staff = staffsRepository.getStaffModelByEmail(s);

        if(staff == null) {

            throw new UsernameNotFoundException("user not found");
        }

        List<GrantedAuthority> listAuth = new LinkedList<>();

        listAuth.add(new SimpleGrantedAuthority(staff.getRole()));

        return new org.springframework.security.core.userdetails.User(staff.getEmail(), staff.getPassword(), listAuth);
    }
}
