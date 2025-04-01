package RealTimeChat.service;

import RealTimeChat.model.User;
import RealTimeChat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    //****Query****
    public Optional<User> findUserByUsername (String username) {
        return userRepository.findByUsername(username);
    }

    public boolean isUsernameAlreadyTaken(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean isEmailAlreadyRegistered(String email) {
        return userRepository.existsByEmail(email);
    }

    public List<User> findAllOnlineUsers() {
        return userRepository.findByIsOnlineTrue();
    }

    //***Mutations***

    public User registerUser(User user) {
        if (isUsernameAlreadyTaken(user.getUsername())) {
            throw new RuntimeException("Le nom d'utilisateur existe déjà");
        }
        if (isEmailAlreadyRegistered(user.getEmail())) {
            throw new RuntimeException("Cet email est déjà utilisé");
        }
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setIsOnline(false);

        return userRepository.save(user);
    }

    public User updateUserOnlineStatus(Integer userId, boolean isOnline) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            user.setIsOnline(isOnline);
            user.setLastSeenAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
            return userRepository.save(user);
        }
        throw new RuntimeException("Utilisateur non trouvé avec l'ID: " + userId);
    }
}

