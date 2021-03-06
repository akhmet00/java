package com.example.spring.controller;


import com.example.spring.model.Notification;
import com.example.spring.model.Post;
import com.example.spring.model.User;
import com.example.spring.service.FriendService;
import com.example.spring.service.NotificationService;
import com.example.spring.service.PostService;
import com.example.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.*;

@Controller
public class MainController {
    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private FriendService friendService;

    @RequestMapping(value = "/register")
    public String registerUser(Model model) {
        model.addAttribute("user", new User());
        return "registerform";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String saveUser(@Valid User user, BindingResult bindingResult) {
        List<String> allUsernames = userService.getAllUsersUsernames();
        String username = user.getUsername();
        String password = user.getPassword();
        String fname = user.getFname();
        String lname = user.getLname();
        Date date = user.getDate();
        String gender = user.getGender();
        String repeatedPassword = user.getRepeatedPassword();
        Integer money = user.setMoney(5000);

        if (bindingResult.hasErrors() || allUsernames.contains(username) || !password.equals(repeatedPassword)) {
            bindingResult.getAllErrors().forEach(error -> {
                System.out.println(error.getObjectName() + " " + error.getDefaultMessage());
            });
            return "registerform"; }
        else {
            userService.saveUser(user);
            return "redirect:/login"; }
    }

    @RequestMapping(value = "/login")
    public String login() {

            return "login"; 

    }

    @RequestMapping(value = "/home")
    public String home(Model model) {
        User currentUser = userService.getCurrentLoggedUser();
        List<Post> myAllPosts = postService.getMyAllPosts();
        Integer currentUserId = currentUser.getId();
        String currentUsername = currentUser.getUsername();
        List<String> requestFriendsUsersnames = friendService.getAllFriendsRequests();
        List<User> myFriends = friendService.getMyFriends();
        List<Post> myPosts = postService.getMyOwnPosts();
        Integer myNumberOfFriendsInvitations = currentUser.getInvitations();
        Integer myNumberOfNewMessages = currentUser.getNewmessage();
        Integer myNumberOfNotifications = currentUser.getNotification();
        model.addAttribute("invitations", myNumberOfFriendsInvitations);
        model.addAttribute("newmessage", myNumberOfNewMessages);
        model.addAttribute("id", currentUserId);
        model.addAttribute("posts", myAllPosts);
        model.addAttribute("post", new Post());
        model.addAttribute("username", currentUsername);
        model.addAttribute("users", requestFriendsUsersnames);
        model.addAttribute("notifications", myNumberOfNotifications);
        model.addAttribute("user", currentUser);
        model.addAttribute("friends", myFriends);
        model.addAttribute("posts", myPosts);
        model.addAttribute("email",currentUser.getEmail());
        model.addAttribute("username", currentUsername);

        return "index";
    }

    @RequestMapping(value = "/home", method = RequestMethod.POST)
    public String addMessage(@Valid Post post, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error ->{
                System.out.println(error.getObjectName() + " " + error.getDefaultMessage());
            });
            return "redirect:/home";}
            else {
            postService.savePost(post);
            return "redirect:/home";}
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(Model model, @RequestParam(value = "search") String username) {
        String currentUsername = userService.getCurrentLoggedUser().getUsername();
        User currentUser = userService.getCurrentLoggedUser();
        Integer myNumberOfFriendsInvitations = currentUser.getInvitations();
        Integer myNumberOfNewMessages = currentUser.getNewmessage();
        Integer myNumberOfNotifications = currentUser.getNotification();
        List<User> searchedUsers = userService.searchUsersByName(username.trim());

        model.addAttribute("name", username);
        model.addAttribute("user", searchedUsers);
        model.addAttribute("username", currentUsername);
        model.addAttribute("invitations", myNumberOfFriendsInvitations);
        model.addAttribute("newmessage", myNumberOfNewMessages);
        model.addAttribute("notifications", myNumberOfNotifications);
        return "searchresult";
    }

    @RequestMapping(value = "/invitations")
    public String getInvitations(Model model) {
        User currentUser = userService.getCurrentLoggedUser();
        String currentUserUsername = currentUser.getUsername();
        List<String> myFriendsRequests = friendService.getAllFriendsRequests();
        Integer myNumberOfFriendsInvitations = currentUser.getInvitations();
        Integer myNumberOfNewMessages = currentUser.getNewmessage();
        Integer myNumberOfNotifications = currentUser.getNotification();

        model.addAttribute("size", myFriendsRequests.size());
        model.addAttribute("users", myFriendsRequests);
        model.addAttribute("username", currentUserUsername);
        model.addAttribute("invitations", myNumberOfFriendsInvitations);
        model.addAttribute("newmessage", myNumberOfNewMessages);
        model.addAttribute("notifications", myNumberOfNotifications);
        return "invitations";
    }

    @RequestMapping(value = "/notifications")
    public String getNotifications(Model model) {
        User currentUser = userService.getCurrentLoggedUser();
        String currentUserUsername = currentUser.getUsername();
        currentUser.setNotification(0);
        userService.updateUser(currentUser);
        Integer myNumberOfFriendsInvitations = currentUser.getInvitations();
        Integer myNumberOfNewMessages = currentUser.getNewmessage();
        Integer myNumberOfNotifications = currentUser.getNotification();
        List<Notification> myNotifications = notificationService.getMyNotifications();

        model.addAttribute("size", notificationService.getMyNotifications().size());
        model.addAttribute("username", currentUserUsername);
        model.addAttribute("invitations", myNumberOfFriendsInvitations);
        model.addAttribute("newmessage", myNumberOfNewMessages);
        model.addAttribute("notification", myNumberOfNotifications);
        model.addAttribute("notifications", myNotifications);
        return "notifications";
    }
    @RequestMapping(value = "/roulet")
    public String roulet(Model model) {
     //   User curentUser = (User) userService.getAllUsers();
        User currentUser = userService.getCurrentLoggedUser();
        List<Post> myAllPosts = postService.getMyAllPosts();
        Integer currentUserId = currentUser.getId();
        String currentUsername = currentUser.getUsername();
        List<String> requestFriendsUsersnames = friendService.getAllFriendsRequests();
        Integer myNumberOfFriendsInvitations = currentUser.getInvitations();
        Integer myNumberOfNewMessages = currentUser.getNewmessage();
        Integer myNumberOfNotifications = currentUser.getNotification();
        model.addAttribute("invitations", myNumberOfFriendsInvitations);
        model.addAttribute("newmessage", myNumberOfNewMessages);
        model.addAttribute("id", currentUserId);
        model.addAttribute("posts", myAllPosts);
        model.addAttribute("post", new Post());
        model.addAttribute("money", currentUser.getMoney());
        model.addAttribute("email",currentUser.getEmail());
        model.addAttribute("username", currentUsername);
        model.addAttribute("users", requestFriendsUsersnames);
        model.addAttribute("notifications", myNumberOfNotifications);
       // model.addAttribute("curuser", curentUser);
        return "roulet";
    }

}
