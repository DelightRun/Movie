package com.movie.movie.service.common;

import com.movie.movie.bean.MovieType;
import com.movie.movie.dao.common.AccountDao;
import com.movie.movie.dao.common.MovieDao;
import com.movie.movie.dao.common.OrderDao;
import com.movie.movie.entity.common.Account;
import com.movie.movie.entity.common.Movie;
import com.movie.movie.recommend.MovieDataModel;
import com.movie.movie.recommend.SwingI2ISimilarity;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.PlusAnonymousConcurrentUserDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.CachingUserNeighborhood;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericBooleanPrefItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericBooleanPrefUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.CachingItemSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.CachingUserSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.ItemBasedRecommender;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RecommendService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private MovieDao movieDao;

    @Autowired
    private MovieDataModel baseDataModel;

    private DataModel dataModel;

    private ItemSimilarity itemSimilarity;

    private ItemBasedRecommender itemBasedRecommender;

    private UserSimilarity userSimilarity;
    private UserNeighborhood userNeighborhood;
    private UserBasedRecommender userBasedRecommender;

    @PostConstruct
    public void initializeRecommender() throws Exception {
        dataModel = new PlusAnonymousConcurrentUserDataModel(baseDataModel, 10);

        userSimilarity = new LogLikelihoodSimilarity(dataModel);
        userSimilarity = new CachingUserSimilarity(userSimilarity, dataModel);
        userNeighborhood = new NearestNUserNeighborhood(3, userSimilarity, dataModel);
        userNeighborhood = new CachingUserNeighborhood(userNeighborhood, dataModel);
        userBasedRecommender = new GenericBooleanPrefUserBasedRecommender(dataModel, userNeighborhood, userSimilarity);

        itemSimilarity = new SwingI2ISimilarity(dataModel);
        itemSimilarity = new CachingItemSimilarity(itemSimilarity, dataModel);
        itemBasedRecommender = new GenericBooleanPrefItemBasedRecommender(dataModel, itemSimilarity);
    }

    private List<Movie> getMovieFromRecommendItems(List<RecommendedItem> recommendedItems) {
        if (recommendedItems == null || recommendedItems.size() == 0)
            return new ArrayList<>();

        List<Long> ids = recommendedItems.stream().map(RecommendedItem::getItemID).collect(Collectors.toList());
        return movieDao.findShowListByIds(new Date(), ids);
    }

    private List<Movie> doU2IRecommend(Recommender recommender, Account account, int howMany) {
        try {
            return getMovieFromRecommendItems(recommender.recommend(account.getId(), howMany));
        } catch (TasteException e) {
            logger.error(e.toString());
            return null;
        }
    }

    public List<Movie> recommendForUser(Account account, int howMany) {
        // 热门召回
        List<Movie> base = movieDao.findList(new Date(), howMany);

        List<Movie> u2i = doU2IRecommend(userBasedRecommender, account, howMany);
        List<Movie> i2i = doU2IRecommend(itemBasedRecommender, account, howMany);

        logger.debug("u2i: " + u2i);
        logger.debug("i2i: " + i2i);

        // 排序
        List<Movie> movies = Stream.of(base, u2i, i2i)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .distinct()
                .sorted(Comparator.comparing(Movie::getRate).reversed())
                .collect(Collectors.toList());
        return movies.subList(0, Math.min(howMany, movies.size()));
    }

    private List<Movie> doI2IRecommend(ItemBasedRecommender recommender, Movie movie, int howMany) {
        try {
            return getMovieFromRecommendItems(recommender.mostSimilarItems(movie.getId(), howMany));
        } catch (TasteException e) {
            logger.error(e.toString());
            return null;
        }
    }

    public List<Movie> recommendForMovie(Movie movie, int howMany) {
        List<Movie> tag2i = new ArrayList<>();
        // Tag2I 召回
        for (MovieType movieType : movie.getTypeList()) {
            tag2i.addAll(movieDao.findByType(new Date(), movieType.toString(), howMany));
        }

        // I2I 召回
        List<Movie> i2i = doI2IRecommend(itemBasedRecommender, movie, howMany);

        logger.debug("tag2i: " + tag2i);
        logger.debug("i2i: " + i2i);

        List<Movie> movies = Stream.of(tag2i, i2i)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .distinct()
                .filter(m -> m != movie)
                .sorted(Comparator.comparing(Movie::getRate).reversed())
                .collect(Collectors.toList());
        return movies.subList(0, Math.min(movies.size(), howMany));
    }
}
