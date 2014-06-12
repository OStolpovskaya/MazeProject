package models;

import controllers.routes;
import models.utils.AppException;
import models.utils.Hash;
import play.Logger;
import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import play.i18n.Messages;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * User: yesnault
 * Date: 20/01/12
 */
@Entity
public class User extends Model {

    @Id
    public Long id;

    @Constraints.Required
    @Formats.NonEmpty
    @Column(unique = true)
    public String email;

    @Constraints.Required
    @Formats.NonEmpty
    @Column(unique = true)
    public String fullname;

    public String confirmationToken;

    @Constraints.Required
    @Formats.NonEmpty
    public String passwordHash;

    @Formats.DateTime(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date dateCreation;

    @Formats.NonEmpty
    public Boolean validated = false;

    @OneToOne
    @MapsId
    public World world;

    @Formats.NonEmpty
    private int thirst;

    private int time;

    public static int MAX_THIRST = 720;
    public static Model.Finder<Long, User> find = new Model.Finder<Long, User>(Long.class, User.class);

    /**
     * Retrieve a user from an email.
     *
     * @param email email to search
     * @return a user
     */
    public static User findByEmail(String email) {
        return find.where().eq("email", email).findUnique();
    }

    /**
     * Retrieve a user from a fullname.
     *
     * @param fullname Full name
     * @return a user
     */
    public static User findByFullname(String fullname) {
        return find.where().eq("fullname", fullname).findUnique();
    }

    /**
     * Retrieves a user from a confirmation token.
     *
     * @param token the confirmation token to use.
     * @return a user if the confirmation token is found, null otherwise.
     */
    public static User findByConfirmationToken(String token) {
        return find.where().eq("confirmationToken", token).findUnique();
    }

    /**
     * Authenticate a User, from a email and clear password.
     *
     * @param email         email
     * @param clearPassword clear password
     * @return User if authenticated, null otherwise
     * @throws AppException App Exception
     */
    public static User authenticate(String email, String clearPassword) throws AppException {

        // get the user with email only to keep the salt password
        User user = find.where().eq("email", email).findUnique();
        if (user != null) {
            // get the hash password from the salt + clear password
            if (Hash.checkPassword(clearPassword, user.passwordHash)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Confirms an account.
     *
     * @return true if confirmed, false otherwise.
     * @throws AppException App Exception
     */
    public static boolean confirm(User user) throws AppException {
        if (user == null) {
            return false;
        }

        user.confirmationToken = null;
        user.validated = true;
        user.save();
        return true;
    }

    public void applyNewPlayerSettings() {
        world = null;
        thirst = MAX_THIRST;
        save();
    }

    public void updateTime(int addedTime) {
        time = time + addedTime;
        thirst = thirst - addedTime;
        if (thirst <= 0) {
            die();
            return;
        }
        save();
    }

    private void die() {
        Logger.debug(String.format("User %s dies", fullname));
        World curWorld = world;
        exitWorld();
        World.deleteWorld(curWorld);
    }

    public String getFormattedTime() {
        int hour = time / 60;
        int minutes = time % 60;
        return String.format("%d:%d", hour, minutes);
    }

    public String getFormattedThirst() {
        String formattedThirst;
        if (thirst<=60){
            formattedThirst=String.format("<span style=\"color:red\">%.2f%%</span>",(float)thirst/MAX_THIRST*100);
        }else{
            formattedThirst=String.format("<span style=\"color:blue\">%.2f%%</span>",(float)thirst/MAX_THIRST*100);
        }
        return formattedThirst;
    }


    @Override
    public String toString() {
        return "User{" +
                "fullname='" + fullname + '\'' +
                ", thirst=" + thirst +
                ", time=" + time +
                ", world=" + (world == null ? "null" : world.id) +
                '}';
    }

    public void changePassword(String password) throws AppException {
        this.passwordHash = Hash.createPassword(password);
        this.save();
    }

    public void enterWorld(World world) {
        this.world = world;
        save();
    }

    public void exitWorld() {
        world = null;
        thirst = MAX_THIRST;
        time = 0;
        save();
    }

    public boolean inWorld() {
        if (world == null) {
            return false;
        }
        return true;
    }


}
