package hkmu.comps380f.dao;

import hkmu.comps380f.exception.TicketNotFound;
import hkmu.comps380f.model.Attachment;
import hkmu.comps380f.model.Ticket;
import hkmu.comps380f.model.TicketUser;
import hkmu.comps380f.model.UserRole;
import jakarta.annotation.Resource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class TicketUserService implements UserDetailsService {
    @Resource
    TicketUserRepository ticketUserRepo;

    @Resource
    private UserManagementService umService;

    @Resource
    private TicketUserRepository tUserRepo;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        TicketUser ticketUser = ticketUserRepo.findById(username).orElse(null);
        if (ticketUser == null) {
            throw new UsernameNotFoundException("User '" + username + "' not found.");
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (UserRole role : ticketUser.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getRole()));
        }
        return new User(ticketUser.getUsername(), ticketUser.getPassword(), authorities);
    }

    @Transactional
    public void updateDescription(String user, String body)
            throws IOException{
        List<TicketUser> ticketUsers = umService.getTicketUsers();
        for(TicketUser ticketUser:ticketUsers){
            if(ticketUser.getUsername().equals(user)){
                ticketUser.setDesc(body);
                tUserRepo.save(ticketUser);
            }
        }

    }
}