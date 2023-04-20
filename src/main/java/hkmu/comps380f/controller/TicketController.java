package hkmu.comps380f.controller;

import hkmu.comps380f.dao.TicketService;
import hkmu.comps380f.dao.TicketUserService;
import hkmu.comps380f.dao.UserManagementService;
import hkmu.comps380f.exception.AttachmentNotFound;
import hkmu.comps380f.exception.TicketNotFound;
import hkmu.comps380f.model.Attachment;
import hkmu.comps380f.model.Comment;
import hkmu.comps380f.model.Ticket;
import hkmu.comps380f.model.TicketUser;
import hkmu.comps380f.view.DownloadingView;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/ticket")
public class TicketController {

    @Resource
    private TicketService tService;

    @Resource
    private UserManagementService umService;
    @Resource
    private TicketUserService tUserService;

    // Controller methods, Form-backing object, ...
    @GetMapping(value = {"", "/list"})
    public String list(ModelMap model) {
        model.addAttribute("ticketDatabase", tService.getTickets());
        return "list";
    }

    @GetMapping("/create")
    public ModelAndView create() {
        return new ModelAndView("add", "ticketForm", new Form());
    }

    public static class Form {
        private String subject;
        private String body;
        private List<MultipartFile> attachments;

        private String comment;

        // Getters and Setters of customerName, subject, body, attachments
        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public List<MultipartFile> getAttachments() {
            return attachments;
        }

        public void setAttachments(List<MultipartFile> attachments) {
            this.attachments = attachments;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }
    }

    @PostMapping("/create")
    public View create(Form form, Principal principal) throws IOException {
        long ticketId = tService.createTicket(principal.getName(),
                form.getSubject(), form.getBody(), form.getAttachments());
        return new RedirectView("/ticket/view/" + ticketId, true);
    }

    @GetMapping("/view/{ticketId}")
    public String view(@PathVariable("ticketId") long ticketId,
                       ModelMap model)
            throws TicketNotFound {
        Ticket ticket = tService.getTicket(ticketId);
        model.addAttribute("ticketId", ticketId);
        model.addAttribute("ticket", ticket);
        return "view";
    }

    @GetMapping("/profile/{customerName}")
    public String profile(@PathVariable("customerName") String customerName,
                       ModelMap model)
            throws TicketNotFound {
        List<Ticket> tickets = tService.getTickets();
        List<TicketUser> ticketUsers = umService.getTicketUsers();
        model.addAttribute("tickets", tickets);
        model.addAttribute("customerName", customerName);
        model.addAttribute("ticketUsers", ticketUsers);
        return "profile";
    }

    @GetMapping("/{ticketId}/attachment/{attachment:.+}")
    public View download(@PathVariable("ticketId") long ticketId,
                         @PathVariable("attachment") UUID attachmentId)
            throws TicketNotFound, AttachmentNotFound {
        Attachment attachment = tService.getAttachment(ticketId, attachmentId);
        return new DownloadingView(attachment.getName(),
                    attachment.getMimeContentType(), attachment.getContents());
    }

    @GetMapping("/delete/{ticketId}")
    public String deleteTicket(@PathVariable("ticketId") long ticketId)
            throws TicketNotFound {
        tService.delete(ticketId);
        return "redirect:/ticket/list";
    }

    @GetMapping("/{ticketId}/delete/{attachment:.+}")
    public String deleteAttachment(@PathVariable("ticketId") long ticketId,
                                   @PathVariable("attachment") UUID attachmentId)
            throws TicketNotFound, AttachmentNotFound {
        tService.deleteAttachment(ticketId, attachmentId);
        return "redirect:/ticket/view/" + ticketId;
    }

    @GetMapping("/edit/{ticketId}")
    public ModelAndView showEdit(@PathVariable("ticketId") long ticketId,
                                 Principal principal, HttpServletRequest request)
            throws TicketNotFound {
        Ticket ticket = tService.getTicket(ticketId);
        if (ticket == null
                || (!request.isUserInRole("ROLE_ADMIN")
                && !principal.getName().equals(ticket.getCustomerName()))) {
            return new ModelAndView(new RedirectView("/ticket/list", true));
        }
        ModelAndView modelAndView = new ModelAndView("edit");
        modelAndView.addObject("ticket", ticket);
        Form ticketForm = new Form();
        ticketForm.setSubject(ticket.getSubject());
        ticketForm.setBody(ticket.getBody());
        modelAndView.addObject("ticketForm", ticketForm);
        return modelAndView;
    }

    @PostMapping("/edit/{ticketId}")
    public String edit(@PathVariable("ticketId") long ticketId, Form form,
                       Principal principal, HttpServletRequest request)
            throws IOException, TicketNotFound {
        Ticket ticket = tService.getTicket(ticketId);
        if (ticket == null
                || (!request.isUserInRole("ROLE_ADMIN")
                && !principal.getName().equals(ticket.getCustomerName()))) {
            return "redirect:/ticket/list";
        }
        tService.updateTicket(ticketId, form.getSubject(),
                form.getBody(), form.getAttachments());
        return "redirect:/ticket/view/" + ticketId;
    }

    @GetMapping("/profile/{user}/edit")
    public ModelAndView showEditProfile(@PathVariable("user") String user,
                                 Principal principal, HttpServletRequest request)
            throws TicketNotFound {
        List<TicketUser> ticketUsers = umService.getTicketUsers();
        for(TicketUser ticketUser : ticketUsers){
            if(ticketUser.getUsername().equals(user)){
                ModelAndView modelAndView = new ModelAndView("editProfile");
                modelAndView.addObject("ticketUser", ticketUser);
                Form ticketForm = new Form();
                ticketForm.setBody(ticketUser.getDesc());
                modelAndView.addObject("ticketForm", ticketForm);
                return modelAndView;
            }
        }
        return null;
    }

    @PostMapping("/profile/{user}/edit")
    public String editProfile(@PathVariable("user") String user, Form form,
                       Principal principal, HttpServletRequest request)
            throws IOException, TicketNotFound {

        List<TicketUser> ticketUsers = umService.getTicketUsers();

        for(TicketUser ticketUser:ticketUsers){

        }

        tUserService.updateDescription(user, form.getBody());
        return "redirect:/ticket/profile/" + user;
    }

    @GetMapping("/comment/{ticketId}/{attachmentId}")
    public ModelAndView showComment(@PathVariable("ticketId") long ticketId,
                                    @PathVariable ("attachmentId") UUID attachmentId,
                                    Principal principal, HttpServletRequest request)
            throws TicketNotFound, AttachmentNotFound {
        Ticket ticket = tService.getTicket(ticketId);
        Attachment attachment = tService.getAttachment(ticketId,attachmentId);
        if (ticket == null
                || (!request.isUserInRole("ROLE_ADMIN")
                && !principal.getName().equals(ticket.getCustomerName()))) {
            return new ModelAndView(new RedirectView("/ticket/list", true));
        }
        ModelAndView modelAndView = new ModelAndView("comment");
        modelAndView.addObject("ticket", ticket);
        modelAndView.addObject("attachment", attachment);
        Form ticketForm = new Form();
        ticketForm.setSubject(ticket.getSubject());
        ticketForm.setBody(ticket.getBody());
        modelAndView.addObject("ticketForm", ticketForm);
        return modelAndView;
    }



    @PostMapping("/comment/{ticketId}/{attachmentId}")
    public String comment(@PathVariable("ticketId") long ticketId, Form form,
                       Principal principal, HttpServletRequest request)
            throws IOException, TicketNotFound {
        Ticket ticket = tService.getTicket(ticketId);
        if (ticket == null
                || (!request.isUserInRole("ROLE_ADMIN")
                && !principal.getName().equals(ticket.getCustomerName()))) {
            return "redirect:/ticket/list";
        }
        tService.updateTicket(ticketId, form.getSubject(),
                form.getBody(), form.getAttachments());
        return "redirect:/ticket/view/" + ticketId;
    }



    @ExceptionHandler({TicketNotFound.class, AttachmentNotFound.class})
    public ModelAndView error(Exception e) {
        return new ModelAndView("error", "message", e.getMessage());
    }
}

