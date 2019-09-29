package ru.v1as.tg.autopoller;

import java.util.Collection;
import org.junit.Assert;

public class AbstractCatBotTest extends AbstractGameBotTest {

    public AutoPollBotData getCatBotData() {
        return getCatBot().getData();
    }

    public AutoPoller getCatBot() {
        return (AutoPoller) bot;
    }

//    public ScoreData getCatBotScoreData() {
//        return (getCatBot()).getData().getScoreData();
//    }
//
//    public CatRequest getOnlyOneCatRequest() {
//        Collection<CatRequest> catRequests =
//                getCatBotData().getChatData(getChatId()).getCatRequests();
//        Assert.assertEquals(1, catRequests.size());
//        return catRequests.iterator().next();
//    }
}
