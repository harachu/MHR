//package harachu.jazz;
//
//import java.net.URL;
//import java.util.List;
//
//import com.sun.syndication.feed.synd.SyndEntry;
//import com.sun.syndication.feed.synd.SyndFeed;
//import com.sun.syndication.fetcher.FeedFetcher;
//import com.sun.syndication.fetcher.impl.HttpURLFeedFetcher;
//
//public class PrintFeed {
//  // フィードのURL
//  private static final String[] FEED_URLS = {
//      //"http://journal.mycom.co.jp/haishin/rss/enterprise.rdf",
//      //"http://d.hatena.ne.jp/Syunpei/rss",
//      //"http://src-lib43.dv.jp.honda.com/dokuwiki/feed.php",
//      "https://chm01.dv.jp.honda.com:9443/jazz/service/com.ibm.team.repository.common.internal.IFeedService?provider=team&project_area=JTST&user=JP203998"
//    };
//
//  public static void main(String[] args) throws Exception {
//
//    // HTTPを元にフィードを取得するクラス「FeedFetcher」 ………(1)
//    FeedFetcher fetcher = new HttpURLFeedFetcher();
//
//    // フィードの内容、フィードに含まれる記事エントリの内容を出力する
//    for (String url : FEED_URLS) {
//      // フィードの取得 ………(2)
//      SyndFeed feed = fetcher.retrieveFeed(new URL(url));
//
//      System.out.format("フィードタイトル:[%s] 著者:[%s]\n",
//          feed.getTitle(),
//            feed.getUri());
//
//      for (SyndEntry entry : (List<SyndEntry>) feed.getEntries()) {
//        System.out.format("\t更新時刻:[%s] URL:[%s] 記事タイトル:[%s]\n",
//            entry.getPublishedDate(),
//            entry.getLink(),
//            entry.getTitle());
//      }
//    }
//  }
//}