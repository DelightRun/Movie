package com.movie.movie.recommend;

import org.apache.mahout.cf.taste.common.Refreshable;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastIDSet;
import org.apache.mahout.cf.taste.impl.common.RefreshHelper;
import org.apache.mahout.cf.taste.impl.similarity.AbstractItemSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public final class SwingI2ISimilarity extends AbstractItemSimilarity {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final double ALPHA = 1;

    public SwingI2ISimilarity(DataModel dataModel) {
        super(dataModel);
    }

    @Override
    public double itemSimilarity(long itemID1, long itemID2) throws TasteException {
        DataModel dataModel = getDataModel();

        PreferenceArray prefs1 = dataModel.getPreferencesForItem(itemID1);
        PreferenceArray prefs2 = dataModel.getPreferencesForItem(itemID2);

        logger.info(itemID1 + " => " + prefs1.toString());
        logger.info(itemID2 + " => " + prefs2.toString());

        return doItemSimilarity(prefs1, prefs2);
    }

    @Override
    public double[] itemSimilarities(long itemID1, long[] itemID2s) throws TasteException {
        DataModel dataModel = getDataModel();
        PreferenceArray prefs1 = dataModel.getPreferencesForItem(itemID1);
        int length = itemID2s.length;
        double[] result = new double[length];
        for (int i = 0; i < length; i++) {
            PreferenceArray prefs2 = dataModel.getPreferencesForItem(itemID2s[i]);
            result[i] = doItemSimilarity(prefs1, prefs2);
        }
        return result;
    }

    private FastIDSet getCommonUsersForPreferences(PreferenceArray prefs1, PreferenceArray prefs2) {
        FastIDSet userIDs = new FastIDSet();

        if (prefs1 != null && prefs2 != null) {
            for (int i = 0; i < prefs1.length(); ++i) {
                for (int j = i + 1; j < prefs2.length(); ++i) {
                    long userID1 = prefs1.getUserID(i);
                    long userID2 = prefs2.getUserID(j);
                    if (userID1 == userID2) userIDs.add(userID1);
                }
            }
        }

        return userIDs;
    }

    private double doItemSimilarity(PreferenceArray prefs1, PreferenceArray prefs2) throws TasteException {
        DataModel dataModel = getDataModel();
        long[] commonUsers = getCommonUsersForPreferences(prefs1, prefs2).toArray();

        double similarity = 0;
        for (int i = 0; i < commonUsers.length - 1; ++i) {
            for (int j = i + 1; j < commonUsers.length; ++j) {
                FastIDSet itemsI = dataModel.getItemIDsFromUser(commonUsers[i]);
                FastIDSet itemsJ = dataModel.getItemIDsFromUser(commonUsers[j]);
                logger.debug("itemsI => " + itemsI.toString());
                logger.debug("itemsJ => " + itemsJ.toString());
                similarity += 1.0 / (itemsI.intersectionSize(itemsJ) + ALPHA);
            }
        }

        return similarity;
    }

    @Override
    public void refresh(Collection<Refreshable> alreadyRefreshed) {
        alreadyRefreshed = RefreshHelper.buildRefreshed(alreadyRefreshed);
        RefreshHelper.maybeRefresh(alreadyRefreshed, getDataModel());
    }

    @Override
    public String toString() {
        return "SwingI2ISimilarity[dataModel:" + getDataModel() + ']';
    }

}
