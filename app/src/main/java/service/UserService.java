
package service;

import model.entity.User;
import java.security.spec.KeySpec;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import model.dao.impl.UserDao;

//@Service
public class UserService {

    private final UserDao userDao;

    public UserService() {
        this.userDao = new UserDao();
    }

    public void registerUser(User user) {
        // Hash the password before saving
        try {
            var password = this.hashPassword(user.getPassword());
            user.setPassword(password); // Save the hashed password
            userDao.saveUser(user);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public User getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }

    public String hashPassword(String password) throws Exception {
        String salt = "randomSalt123"; // Store this key somewhere safe

        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] hash = factory.generateSecret(spec).getEncoded();
        return Base64.getEncoder().encodeToString(hash);
    }
}


