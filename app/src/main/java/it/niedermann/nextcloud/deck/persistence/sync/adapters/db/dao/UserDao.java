package it.niedermann.nextcloud.deck.persistence.sync.adapters.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;
import java.util.Set;

import it.niedermann.nextcloud.deck.model.User;

@Dao
public interface UserDao extends GenericDao<User> {

    @Query("SELECT * FROM user WHERE accountId = :accountId")
    Set<User> getUsersForAccount(final long accountId);

    @Query("SELECT * FROM user WHERE accountId = :accountId and id = :remoteId")
    User getUsersByRemoteId(final long accountId, final long remoteId);

}