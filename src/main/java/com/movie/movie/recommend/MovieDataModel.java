package com.movie.movie.recommend;

import com.movie.movie.dao.common.AccountDao;
import com.movie.movie.dao.common.MovieDao;
import com.movie.movie.dao.common.OrderDao;
import com.movie.movie.entity.common.Order;
import org.apache.mahout.cf.taste.common.NoSuchItemException;
import org.apache.mahout.cf.taste.common.NoSuchUserException;
import org.apache.mahout.cf.taste.common.Refreshable;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastIDSet;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveArrayIterator;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.BooleanUserPreferenceArray;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.Preference;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.common.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MovieDataModel implements DataModel {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private MovieDao movieDao;

    /**
     * @return all user IDs in the model, in order
     * @throws TasteException if an error occurs while accessing the data
     */
    @Override
    public LongPrimitiveIterator getUserIDs() throws TasteException {
        try {
            List<Long> ids = orderDao.findDistinctAccountIdsByStatus(Order.status_paid);
            return new LongPrimitiveArrayIterator(ids.stream().mapToLong(l -> l).toArray());
        } catch (Exception e) {
            throw new TasteException(e);
        }
    }

    /**
     * @return a {@link LongPrimitiveIterator} of all item IDs in the model, in order
     * @throws TasteException if an error occurs while accessing the data
     */
    @Override
    public LongPrimitiveIterator getItemIDs() throws TasteException {
        try {
            List<Long> ids = orderDao.findDistinctMovieIdsByStatus(Order.status_paid);
            logger.info("ItemIDs: " + ids.toString());
            return new LongPrimitiveArrayIterator(ids.stream().mapToLong(l -> l).toArray());
        } catch (Exception e) {
            throw new TasteException(e);
        }
    }

    /**
     * @param userID ID of user to get prefs for
     * @return IDs of items user expresses a preference for
     * @throws NoSuchUserException if the user does not exist
     * @throws TasteException      if an error occurs while accessing the data
     */
    @Override
    public FastIDSet getItemIDsFromUser(long userID) throws TasteException {
        try {
            List<Long> itemIds = orderDao.findDistinctMovieIdsByAccountIdAndStatus(userID, Order.status_paid);
            return new FastIDSet(itemIds.stream().mapToLong(l -> l).toArray());
        } catch (Exception e) {
            throw new TasteException(e);
        }
    }

    private PreferenceArray getPreferencesFromOrders(List<Order> orders) {
        Set<Pair<Long, Long>> set = new HashSet<>();

        PreferenceArray preferences = new BooleanUserPreferenceArray(orders.size());
        for (int i = 0; i < orders.size(); ++i) {
            long userId = orders.get(i).getAccount().getId();
            long itemId = orders.get(i).getCinemaHallSession().getMovie().getId();
            Pair<Long, Long> pair = new Pair<>(userId, itemId);

            if (set.contains(pair)) continue;

            set.add(pair);
            preferences.setUserID(i, userId);
            preferences.setItemID(i, itemId);
        }

        return preferences;
    }

    /**
     * @param userID ID of user to get prefs for
     * @return user's preferences, ordered by item ID
     * @throws NoSuchUserException if the user does not exist
     * @throws TasteException      if an error occurs while accessing the data
     */
    @Override
    public PreferenceArray getPreferencesFromUser(long userID) throws TasteException {
        try {
            List<Order> orders = orderDao.findByAccountIdAndStatus(userID, Order.status_paid);
            if (orders == null || orders.size() == 0) throw new NoSuchUserException(userID);
            return getPreferencesFromOrders(orders);
        } catch (Exception e) {
            throw new TasteException(e);
        }
    }

    /**
     * @param itemID item ID
     * @return all existing {@link Preference}s expressed for that item, ordered by user ID, as an array
     * @throws NoSuchItemException if the item does not exist
     * @throws TasteException      if an error occurs while accessing the data
     */
    @Override
    public PreferenceArray getPreferencesForItem(long itemID) throws TasteException {
        try {
            List<Order> orders = orderDao.findByMovieIdAndStatus(itemID, Order.status_paid);
            if (orders == null || orders.size() == 0) throw new NoSuchItemException(itemID);
            return getPreferencesFromOrders(orders);
        } catch (Exception e) {
            throw new TasteException(e);
        }
    }

    /**
     * Retrieves the preference value for a single user and item.
     *
     * @param userID user ID to get pref value from
     * @param itemID item ID to get pref value for
     * @return preference value from the given user for the given item or null if none exists
     * @throws NoSuchUserException if the user does not exist
     * @throws TasteException      if an error occurs while accessing the data
     */
    @Override
    public Float getPreferenceValue(long userID, long itemID) throws TasteException {
        try {
            List<Order> orders = orderDao.findByAccountIdAndMovieIdAndStatus(userID, itemID, Order.status_paid);
            return (orders == null || orders.size() == 0) ? null : 1.0f;
        } catch (Exception e) {
            throw new TasteException(e);
        }
    }

    /**
     * Retrieves the time at which a preference value from a user and item was set, if known.
     * Time is expressed in the usual way, as a number of milliseconds since the epoch.
     *
     * @param userID user ID for preference in question
     * @param itemID item ID for preference in question
     * @return time at which preference was set or null if no preference exists or its time is not known
     * @throws NoSuchUserException if the user does not exist
     * @throws TasteException      if an error occurs while accessing the data
     */
    @Override
    public Long getPreferenceTime(long userID, long itemID) throws TasteException {
        try {
            List<Order> orders = orderDao.findByAccountIdAndMovieIdAndStatus(userID, itemID, Order.status_paid);
            return (orders == null || orders.size() == 0) ? null : orders.get(0).getUpdateTime().getTime();
        } catch (Exception e) {
            throw new TasteException(e);
        }
    }

    /**
     * @return total number of items known to the model. This is generally the union of all items preferred by
     * at least one user but could include more.
     * @throws TasteException if an error occurs while accessing the data
     */
    @Override
    public int getNumItems() throws TasteException {
        try {
            List<Long> ids = orderDao.findDistinctMovieIdsByStatus(Order.status_paid);
            return ids == null ? 0 : ids.size();
        } catch (Exception e) {
            throw new TasteException(e);
        }
    }

    /**
     * @return total number of users known to the model.
     * @throws TasteException if an error occurs while accessing the data
     */
    @Override
    public int getNumUsers() throws TasteException {
        try {
            List<Long> ids = orderDao.findDistinctMovieIdsByStatus(Order.status_paid);
            return ids == null ? 0 : ids.size();
        } catch (Exception e) {
            throw new TasteException(e);
        }
    }

    public FastIDSet getUserIDsFromItem(long itemID) throws TasteException {
        try {
            List<Order> orders = orderDao.findByMovieIdAndStatus(itemID, Order.status_paid);
            FastIDSet users = new FastIDSet(orders.size());
            for (Order order : orders) {
                users.add(order.getAccount().getId());
            }
            return users;
        } catch (Exception e) {
            throw new TasteException(e);
        }
    }

    public FastIDSet getUserIDsForItem(long itemID1, long itemID2) throws TasteException {
        try {
            FastIDSet users1 = getUserIDsFromItem(itemID1);
            FastIDSet users2 = getUserIDsFromItem(itemID2);
            FastIDSet commonUsers = new FastIDSet();

            LongPrimitiveIterator iterator = users2.iterator();
            while (iterator.hasNext()) {
                long id = iterator.nextLong();
                if (users1.contains(id)) commonUsers.add(id);
            }
            return commonUsers;
        } catch (Exception e) {
            throw new TasteException(e);
        }
    }

    /**
     * @param itemID item ID to check for
     * @return the number of users who have expressed a preference for the item
     * @throws TasteException if an error occurs while accessing the data
     */
    @Override
    public int getNumUsersWithPreferenceFor(long itemID) throws TasteException {
        return getUserIDsFromItem(itemID).size();
    }

    /**
     * @param itemID1 first item ID to check for
     * @param itemID2 second item ID to check for
     * @return the number of users who have expressed a preference for the items
     * @throws TasteException if an error occurs while accessing the data
     */
    @Override
    public int getNumUsersWithPreferenceFor(long itemID1, long itemID2) throws TasteException {
        return getUserIDsForItem(itemID1, itemID2).size();
    }

    /**
     * <p>
     * Sets a particular preference (item plus rating) for a user.
     * </p>
     *
     * @param userID user to set preference for
     * @param itemID item to set preference for
     * @param value  preference value
     * @throws NoSuchItemException if the item does not exist
     * @throws NoSuchUserException if the user does not exist
     * @throws TasteException      if an error occurs while accessing the data
     */
    @Override
    public void setPreference(long userID, long itemID, float value) throws TasteException {
        throw new TasteException(new UnsupportedOperationException());
    }

    /**
     * <p>
     * Removes a particular preference for a user.
     * </p>
     *
     * @param userID user from which to remove preference
     * @param itemID item to remove preference for
     * @throws NoSuchItemException if the item does not exist
     * @throws NoSuchUserException if the user does not exist
     * @throws TasteException      if an error occurs while accessing the data
     */
    @Override
    public void removePreference(long userID, long itemID) throws TasteException {
        throw new TasteException(new UnsupportedOperationException());
    }

    /**
     * @return true if this implementation actually stores and returns distinct preference values;
     * that is, if it is not a 'boolean' DataModel
     */
    @Override
    public boolean hasPreferenceValues() {
        return false;
    }

    /**
     * @return the maximum preference value that is possible in the current problem domain being evaluated. For
     * example, if the domain is movie ratings on a scale of 1 to 5, this should be 5. While a
     * {@link Recommender} may estimate a preference value above 5.0, it
     * isn't "fair" to consider that the system is actually suggesting an impossible rating of, say, 5.4 stars.
     * In practice the application would cap this estimate to 5.0. Since evaluators evaluate
     * the difference between estimated and actual value, this at least prevents this effect from unfairly
     * penalizing a {@link Recommender}
     */
    @Override
    public float getMaxPreference() {
        return 1;
    }

    /**
     * @see #getMaxPreference()
     */
    @Override
    public float getMinPreference() {
        return 0;
    }

    /**
     * <p>
     * Triggers "refresh" -- whatever that means -- of the implementation. The general contract is that any
     * {@link Refreshable} should always leave itself in a consistent, operational state, and that the refresh
     * atomically updates internal state from old to new.
     * </p>
     *
     * @param alreadyRefreshed {@link Refreshable}s that are known to have already been
     *                         refreshed as a result of an initial call to a {#refresh(Collection)} method on some
     *                         object. This ensure that objects in a refresh dependency graph aren't refreshed twice
     *                         needlessly.
     */
    @Override
    public void refresh(Collection<Refreshable> alreadyRefreshed) {

    }
}
