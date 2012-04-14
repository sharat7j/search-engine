import edu.uci.ics.crawler4j.crawler.CrawlController;

public class Controller {
        public static void main(String[] args) throws Exception {
                CrawlController controller = new CrawlController("E:\\Dropbox\\Dropbox\\Project\\Dennis\\workspace\\SearchEngine\\CrawlerOutput");
               
                
                controller.addSeed("http://www.dmoz.org/Sports/Golf/");
                controller.addSeed("http://www.dmoz.org/Health/Addictions/");
                controller.addSeed("http://www.dmoz.org/Science/Educational_Resources/");
                controller.addSeed("http://www.dmoz.org/Shopping/Consumer_Electronics/");
                
                controller.addSeed("http://www.dmoz.org/Sports/Motorsports/");
                controller.addSeed("http://www.dmoz.org/Health/Public_Health_and_Safety/");
                controller.addSeed("http://www.dmoz.org/Science/Instruments_and_Supplies/");
                controller.addSeed("http://www.dmoz.org/Shopping/Crafts/");
                
                controller.addSeed("http://www.dmoz.org/Sports/Martial_Arts/");
                controller.addSeed("http://www.dmoz.org/Health/Conditions_and_Diseases/");
                controller.addSeed("http://www.dmoz.org/Science/Educational_Resources/");
                controller.addSeed("http://www.dmoz.org/Shopping/Sports/");
                
                controller.addSeed("http://www.dmoz.org/Sports/Basketball/");
                controller.addSeed("http://www.dmoz.org/Health/Nursing/");
                controller.addSeed("http://www.dmoz.org/Science/Software/");
                controller.addSeed("http://www.dmoz.org/Shopping/Home_and_Garden/");
                
                controller.start(MyCrawler.class, 16);  
        }
}